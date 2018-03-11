package com.seeu.ywq.user.service.impl;

import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.repository.ImageRepository;
import com.seeu.ywq.resource.service.ResourceAuthService;
import com.seeu.ywq.trend.dvo.PublishPictureVO;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.user.repository.UserPictureRepository;
import com.seeu.ywq.user.service.UserPictureService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserPictureServiceImpl implements UserPictureService {
    @Resource
    UserPictureRepository userPictureRepository;
    @Resource
    ImageRepository imageRepository;
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    private ResourceAuthService resourceAuthService;


    /**
     * 给本人使用的，查看自己的相册时候会使用
     *
     * @param targetUid   目标用户【本人】
     * @param albumType
     * @param pageRequest
     * @return
     */
    @Override
    public Page findAllMine(Long targetUid, Picture.ALBUM_TYPE albumType, PageRequest pageRequest) {
        Page page = userPictureRepository.findAllByUidAndAlbumTypeEqualsAndDeleteFlag(targetUid, albumType, Picture.DELETE_FLAG.show, pageRequest);
        List<Picture> pictures = page.getContent();
        List<PublishPictureVO> publishPictureVOS = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture == null) continue;
            PublishPictureVO vo = new PublishPictureVO();
            BeanUtils.copyProperties(picture, vo);
            vo.setImage(picture.getImageOpen()); // 全部设置为清晰图
            vo.setAlbumType(Picture.ALBUM_TYPE.open); // 设定为开放（对自己开放的）
            publishPictureVOS.add(vo);
        }
        Page voPage = new PageImpl(publishPictureVOS, pageRequest, page.getTotalElements());
        return voPage;
    }

    @Override
    public List<Picture> findAllByPublishIds(Collection<Long> publishIds) {
        return userPictureRepository.findAllByPublishIdInAndDeleteFlag(publishIds, Picture.DELETE_FLAG.show);
    }


    /**
     * @param uid
     * @param publishId
     * @param albumTypes 长度必须与 images 长度一致
     * @param images
     * @return
     * @throws Exception
     */
    @Override
    public List<PublishPictureVO> savePictures(Long uid, Long publishId, Picture.ALBUM_TYPE[] albumTypes, MultipartFile[] images) throws Exception {
        List<PublishPictureVO> publishPictureVOS = new ArrayList<>();
        List<Picture> pictures = getPictureWithOutSave(uid, publishId, albumTypes, images);
        // 存储到个人照片墙中
        List<Picture> savedPictures = userPictureRepository.save(pictures);
        for (Picture picture : savedPictures) {
            PublishPictureVO vo = new PublishPictureVO();
            BeanUtils.copyProperties(picture, vo);
            // 由于此处是存储操作，用户自己可见，所以返回信息为清晰图片地址即可
            vo.setImage(picture.getImageOpen());
            publishPictureVOS.add(vo);
        }
        return publishPictureVOS;
    }

    /**
     * 自个儿用，发布内容时使用
     *
     * @param uid
     * @param publishId
     * @param albumTypes
     * @param images
     * @return
     * @throws Exception
     */
    @Override
    public List<Picture> getPictureWithOutSave(Long uid, Long publishId, Picture.ALBUM_TYPE[] albumTypes, MultipartFile[] images) throws Exception {
        if (images == null || images.length == 0) return new ArrayList<>(); // empty
        if (images.length != albumTypes.length) return new ArrayList<>();//empty
        // 一张张存入阿里云或其他服务器
//        List<Image> imageList = new ArrayList<>();
//        List<Picture> pictures = new ArrayList<>();
//        StorageImageService.Result result = storageImageService.saveImages(images, albumTypes); // 暂时采用的阿里云 OSS
//        if (result != null && result.getStatus() == StorageImageService.Result.STATUS.success) {
//            // 拿到返回的图片信息，未持久化
//            // 逻辑修改了：传回来的每一张图都是可以用的图，不再需要判断是否 close，图片序列如此：open;open,close;open,close;close，依次读取即可
//            imageList = result.getImageList();
////            List<Image> imageListFromStorage = result.getImageList();
////            for (int i = 0; i < result.getImageNum(); i++) {
////                imageList.add(imageListFromStorage.get(2 * i)); // open 图
////                if (albumTypes[i] == Picture.ALBUM_TYPE.close) {
////                    imageList.add(imageListFromStorage.get(2 * i + 1)); // close 图
////                }
////            }
//        }

        // 存储在 七牛 服务器
        List<Image> imageList = fileUploadService.uploadImagesWithSeqList(images, albumTypes);
        List<Picture> pictures = new ArrayList<>();

        // 数据持久化到数据库，以后根据此信息进行访问图片
        List<Image> savedImages = imageRepository.save(imageList);
        int imageIndex = 0;
        for (int i = 0; i < albumTypes.length; i++) {
            // 拿第 2n 号图片信息（清晰的）,2n + 1 号图片信息（模糊的）
            Picture picture = new Picture();
            picture.setPublishId(publishId);
            picture.setUid(uid);
            picture.setAlbumType(albumTypes[i]);
            picture.setDeleteFlag(Picture.DELETE_FLAG.show);
            picture.setImageOpen(savedImages.get(imageIndex++));

            if (albumTypes[i] == Picture.ALBUM_TYPE.close) {
                picture.setImageClose(savedImages.get(imageIndex++));
            }
            picture.setCreateTime(new Date());
            pictures.add(picture);
        }
        return pictures;
    }

    public boolean canVisit(Long uid, Long publishId, Date currentTime) {
        return resourceAuthService.canVisitPublish(uid, publishId, currentTime);
    }

//    /**
//     * 匿名
//     *
//     * @param picture
//     * @return
//     */
//    @Override
//    public PublishPictureVO transferToVO(Picture picture) {
//        if (picture == null) return null;
//        PublishPictureVO vo = new PublishPictureVO();
//        BeanUtils.copyProperties(picture, vo);
//        if (Picture.ALBUM_TYPE.close == picture.getAlbumType())
//            vo.setImage(picture.getImageClose());
//        else {
//            vo.setImage(picture.getImageOpen());
//        }
//        return vo;
//    }

    /**
     * @param picture
     * @param canVisitClosedResource
     * @return
     */
    @Override
    public PublishPictureVO transferToVO(Picture picture, boolean canVisitClosedResource) {
        if (picture == null) return null;
        PublishPictureVO vo = new PublishPictureVO();
        BeanUtils.copyProperties(picture, vo);
        if (Picture.ALBUM_TYPE.open == picture.getAlbumType()) {
            vo.setImage(picture.getImageOpen());
            vo.setAlbumType(Picture.ALBUM_TYPE.open);
        } else {
            if (canVisitClosedResource) {
                vo.setImage(picture.getImageOpen());
                vo.setAlbumType(Picture.ALBUM_TYPE.open);
            } else {
                vo.setImage(picture.getImageClose());
                vo.setAlbumType(Picture.ALBUM_TYPE.close);
            }
        }
        return vo;
    }

    /**
     * 匿名
     *
     * @param pictures
     * @return
     */
    @Override
    public List<PublishPictureVO> transferToVO(List<Picture> pictures, boolean canVisitClosedResource) {
        if (pictures == null || pictures.size() == 0) return new ArrayList<>();
        List<PublishPictureVO> vos = new ArrayList<>();
        for (Picture picture : pictures) {
            vos.add(transferToVO(picture, canVisitClosedResource));
        }
        return vos;
    }

//    /**
//     * 实名或【本人】
//     *
//     * @param pictures
//     * @param uid
//     * @return
//     */
//    @Override
//    public List<PublishPictureVO> transferToVO(List<Picture> pictures, Long uid) {
//        if (pictures == null || pictures.size() == 0) return new ArrayList<>();
//        boolean canVisitPublish = canVisitPublish(uid, pictures.get(0).getPublishId(), new Date());
//        List<PublishPictureVO> vos = new ArrayList<>();
//        for (Picture picture : pictures) {
//            PublishPictureVO vo = new PublishPictureVO();
//            BeanUtils.copyProperties(picture, vo);
//            if (picture.getPage().equals(uid)) {
//                // 由于此处是存储操作，用户自己可见，所以返回信息为清晰图片地址即可
//                vo.setImage(picture.getImageOpen());
//                vo.setAlbumType(Picture.ALBUM_TYPE.open);
//            } else {
//                if (Picture.ALBUM_TYPE.close == picture.getAlbumType())
//                    if (canVisitPublish) {
//                        vo.setImage(picture.getImageOpen());
//                        vo.setAlbumType(Picture.ALBUM_TYPE.open);
//                    } else {
//                        vo.setImage(picture.getImageClose());
//                        vo.setAlbumType(Picture.ALBUM_TYPE.close);
//                    }
//                else {
//                    vo.setImage(picture.getImageOpen());
//                }
//            }
//            vos.add(vo);
//        }
//        return vos;
//    }

}
