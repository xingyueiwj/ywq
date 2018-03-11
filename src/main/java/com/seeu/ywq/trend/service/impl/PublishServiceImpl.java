package com.seeu.ywq.trend.service.impl;

import com.seeu.ywq.api.admin.trend.PUBLISH;
import com.seeu.ywq.event_listener.publish_react.ClickLikeEvent;
import com.seeu.ywq.event_listener.publish_react.PublishCommentEvent;
import com.seeu.ywq.exception.*;
import com.seeu.ywq.resource.model.*;
import com.seeu.ywq.trend.dvo.PublishVO;
import com.seeu.ywq.trend.dvo.PublishVOAudio;
import com.seeu.ywq.trend.dvo.PublishVOPicture;
import com.seeu.ywq.trend.dvo.PublishVOVideo;
import com.seeu.ywq.trend.model.*;
import com.seeu.ywq.trend.repository.ComplaintRepository;
import com.seeu.ywq.trend.repository.PublishCommentRepository;
import com.seeu.ywq.trend.repository.PublishLikedUserRepository;
import com.seeu.ywq.trend.repository.PublishRepository;
import com.seeu.ywq.resource.service.*;
import com.seeu.ywq.trend.service.PublishCommentService;
import com.seeu.ywq.trend.service.PublishLikedUserService;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.trend.service.PublishVideoService;
import com.seeu.ywq.user.service.UserInfoService;
import com.seeu.ywq.user.service.UserPictureService;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.utils.PictureYellowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PublishServiceImpl implements PublishService {
    @Resource
    private PublishRepository publishRepository;
    @Autowired
    private UserPictureService userPictureService;
    @Autowired
    private PublishLikedUserService publishLikedUserService;
    @Autowired
    private PublishCommentService publishCommentService;
    @Autowired
    private PublishVideoService publishVideoService;
    @Autowired
    private ResourceAuthService resourceAuthService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ImageService imageService;

    /* 以下两个数据源只在此类使用 **/
    @Resource
    private PublishLikedUserRepository publishLikedUserRepository;
    @Resource
    private PublishCommentRepository publishCommentRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PictureYellowService pictureYellowService;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public Publish findOne(Long publishId) {
        return publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
    }

    @Override
    public Publish findOne(Long uid, Long publishId) {
        return publishRepository.findByIdAndUidAndStatus(publishId, uid, Publish.STATUS.normal);
    }

    @Override
    public boolean exist(Long publishId) {
        if (publishId == null) return false;
//        if (!publishRepository.exists(publishId)) return false;
        if (null == publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal)) return false;
        return true;
    }

    @Transactional
    @Override
    public Publish save(Publish publish) {
        if (publish == null) return null;
        // 用户发布数量加一
        if (publish.getUid() != null)
            userInfoService.publishPlusOne(publish.getUid());
        // 图片鉴黄？
        publish = publishRepository.save(publish);
        pictureYellowService.validated(publish.getId());
        return publish;
    }

    @Override
    public Publish saveWithImage(Publish publish) {
        if (publish == null) return null;
        List<Picture> list = publish.getPictures();
        if (list != null && list.size() != 0) {
            for (Picture picture : list) {
                Image openImage = picture.getImageOpen();
                Image closeImage = picture.getImageClose();
                if (openImage != null)
                    picture.setImageOpen(imageService.save(openImage));
                if (closeImage != null)
                    picture.setImageClose(imageService.save(closeImage));
            }
        }
        return publishRepository.save(publish);
    }

    @Override
    public Page findAllByUid(Long uid, boolean canVisitClosedResource, Pageable pageable) {
        Page page = publishRepository.findAllByUidAndStatus(uid, Publish.STATUS.normal, pageable);
        List<Publish> publishes = page.getContent();
        if (publishes == null || publishes.size() == 0)
            return page;
        List<PublishVO> vos = transferToVO(publishes, canVisitClosedResource);
        return new PageImpl(vos, pageable, page.getTotalElements());
    }


    @Override
    public PublishVO findOneByPublishId(Long publishId, boolean canVisitClosedResource) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        return transferToVO(publish, canVisitClosedResource);
    }

    ///////////////////////////////////////////************** transfer operations ***************////////////////////////////////////////////////////

    private PublishVO transferToVO(Publish publish, boolean canVisitClosedResource) {
        if (publish == null) return null;
        switch (publish.getType()) {
            case picture:
                PublishVOPicture vop = new PublishVOPicture();
                BeanUtils.copyProperties(publish, vop);
                vop.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vop.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vop.setComments(publishCommentService.transferToVO(publish.getComments()));
                // picture
                vop.setCoverPictureUrl(publish.getPictures() == null || publish.getPictures().size() == 0 ? null : userPictureService.transferToVO(publish.getPictures().get(0), canVisitClosedResource));
                vop.setPictures(userPictureService.transferToVO(publish.getPictures(), canVisitClosedResource));
                vop.setUnlockPrice(publish.getUnlockPrice());
                return vop;
            case video:
                PublishVOVideo vod = new PublishVOVideo();
                BeanUtils.copyProperties(publish, vod);
                vod.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vod.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vod.setComments(publishCommentService.transferToVO(publish.getComments()));
                // video
                vod.setVideo(publishVideoService.transferToVO(publish.getVideo(), canVisitClosedResource));
                vod.setUnlockPrice(publish.getUnlockPrice());
                // TODO video 权限得加 checked
                return vod;
            case audio:
                PublishVOAudio voa = new PublishVOAudio();
                BeanUtils.copyProperties(publish, voa);
                voa.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                voa.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                voa.setComments(publishCommentService.transferToVO(publish.getComments()));
                voa.setAudio(publish.getAudio());
                return voa;
            case word:
            default:
                PublishVO vo = new PublishVO();
                BeanUtils.copyProperties(publish, vo);
                vo.setLabels(publish.getLabels() == null ? new ArrayList<>() : Arrays.asList(publish.getLabels().split(",")));
                vo.setLikedUsers(publishLikedUserService.transferToVO(publish.getLikedUsers()));
                vo.setComments(publishCommentService.transferToVO(publish.getComments()));
                return vo;
        }
    }

    private List<PublishVO> transferToVO(List<Publish> publishs, boolean canVisitClosedResource) {
        List<PublishVO> vos = new ArrayList<>();
        for (Publish publish : publishs) {
            if (publish == null) continue;
            vos.add(transferToVO(publish, canVisitClosedResource));
        }
        return vos;
    }

    ///////////////////////////////////////////************** other operations ***************////////////////////////////////////////////////////

    @Override
    public PublishVO viewIt(Long publishId) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null) return null;
        publishRepository.viewItOnce(publishId);
        return transferToVO(publish, false);
    }

    @Override
    public PublishVO viewIt(Long publishId, Long uid) {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null || publish.getUid() == null) return null;
        publishRepository.viewItOnce(publishId);
        boolean canVisit = publish.getUid().equals(uid) || resourceAuthService.canVisitPublish(uid, publishId);
        return transferToVO(publish, canVisit);
    }

    @Transactional
    @Override
    public void deletePublish(Long publishId) throws ResourceNotFoundException {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null)
            throw new ResourceNotFoundException("Can not found Resource[Publish ID: " + publishId + " ]");
        // 软删除
        publish.setStatus(Publish.STATUS.delete);
        if (publish.getPictures() != null && publish.getPictures().size() != 0) {
            Date date = new Date();
            for (Picture picture : publish.getPictures()) {
                picture.setDeleteFlag(Picture.DELETE_FLAG.delete);
                picture.setDeleteTime(date);
            }
        }
        publishRepository.save(publish);
        return;
        // 删除全部信息（包含点赞、评论）
//        publishRepository.delete(publishId);
//        publishLikedUserRepository.removeAllByPublishId(publishId);
//        publishCommentRepository.deleteAllByPublishId(publishId);
//        // 用户发布数量减一
//        if (publish.getPage() != null)
//            userInfoService.publishMinsOne(publish.getPage());
    }

    @Override
    public Page<PublishLikedUser> listLikedUser(Long publishId, Pageable pageable) {
        return publishLikedUserRepository.findAllByPublishId(publishId, pageable);
    }

    @Override
    public void likeIt(Long publishId, UserLogin user) throws ResourceNotFoundException, ActionNotSupportException, ResourceAlreadyExistedException {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null)
            throw new ResourceNotFoundException("Can not found Resource[Publish ID: " + publishId + " ]");
        if (publish.getUid() == user.getUid())
            throw new ActionNotSupportException("点赞操作不可用，不可点赞自己的动态 Resource[Publish ID: " + publishId + " ]");
        // 是否点赞过
        if (publishLikedUserRepository.exists(new PublishLikedUserPKeys(publishId, user.getUid())))
            throw new ResourceAlreadyExistedException("点赞操作不可用，已经点赞过 Resource[Publish ID: " + publishId + " ]");
        PublishLikedUser like = new PublishLikedUser();
        like.setUid(user.getUid());
        like.setHeadIconUrl(user.getHeadIconUrl());
        like.setPublishId(publishId);
        publishLikedUserRepository.save(like);
        publishRepository.likeItOnce(publishId);
        // 通知
        String imgUrl = null;
        if (publish.getPictures() != null && publish.getPictures().size() > 0) {
            Image image = publish.getPictures().get(0).getImageOpen(); // 用户自己收到点赞事件
            imgUrl = image.getThumbImage200pxUrl();
        }
        applicationContext.publishEvent(new ClickLikeEvent(this, publish.getUid(), user.getUid(), user.getNickname(), user.getHeadIconUrl(), publishId, imgUrl));
    }

    @Override
    public void dislikeIt(Long publishId, Long uid) throws ResourceNotFoundException, ActionNotSupportException {
        if (null == publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal))
            throw new ResourceNotFoundException("Can not found Resource[Publish ID: " + publishId + " ]");
        // 是否点赞过
        PublishLikedUserPKeys PK = new PublishLikedUserPKeys(publishId, uid);
        if (!publishLikedUserRepository.exists(PK))
            throw new ActionNotSupportException("取消点赞操作不可用，还未点赞过 Resource[Publish ID: " + publishId + " ]");
        publishLikedUserRepository.delete(PK);
        publishRepository.dislikeItOnce(publishId);
    }

    @Override
    public PublishComment getComment(Long commentId) {
        PublishComment comment = publishCommentRepository.findOne(commentId);
        return comment;
    }

    @Override
    public Page<PublishComment> listComments(Long publishId, Pageable pageable) {
        return publishCommentRepository.findAllByPublishIdAndFatherIdIsNull(publishId, pageable);
    }

    @Override
    public void commentIt(Long publishId, Long fatherId, UserLogin user, String text) throws ResourceNotFoundException, ActionNotSupportException {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null)
            throw new ResourceNotFoundException("Can not found Resource[Publish ID: " + publishId + " ]");
        if (fatherId != null && !publishCommentRepository.exists(fatherId))
            throw new ActionNotSupportException("回复评论操作不可用，无此评论ID：" + fatherId);
        PublishComment comment = new PublishComment();
        comment.setPublishId(publishId);
        comment.setUid(user.getUid());
        comment.setUsername(user.getNickname());
        comment.setHeadIconUrl(user.getHeadIconUrl());
        comment.setFatherId(fatherId == null || fatherId < 1 ? null : fatherId);
        comment.setText(text);
        comment.setCommentDate(new Date());
        publishCommentRepository.save(comment);
        publishRepository.commentItOnce(publishId);
        // 通知
        String imgUrl = null;
        if (publish.getPictures() != null && publish.getPictures().size() > 0) {
            Image image = publish.getPictures().get(0).getImageOpen(); // 用户自己收到点赞事件
            imgUrl = image.getThumbImage200pxUrl();
        }
        applicationContext.publishEvent(new PublishCommentEvent(this, publish.getUid(), user.getUid(), user.getNickname(), user.getHeadIconUrl(), publishId, text, imgUrl));
    }

    @Override
    public void deleteComment(Long commentId) throws ResourceNotFoundException {
        PublishComment comment = publishCommentRepository.findOne(commentId);
        if (comment == null)
            throw new ResourceNotFoundException("Can not found Resource[Comment ID: " + commentId + " ]");
        publishCommentRepository.delete(commentId);
        publishRepository.disCommentItOnce(comment.getPublishId());
    }

    @Override
    public Long getUnlockDiamonds(Long publishId) throws ResourceNotFoundException, PublishTYPENotAllowedException {
        Publish p = findOne(publishId);
        if (p == null)
            throw new ResourceNotFoundException("Can not found Resource Publish[Id: " + publishId + " ]");
        if (p.getType() == null || p.getType() == Publish.PUBLISH_TYPE.word)
            throw new PublishTYPENotAllowedException(p.getType());
        return p.getUnlockPrice();
    }

    @Override
    public Page<Publish> findAll(Pageable pageable) {
        return publishRepository.findAllByStatus(Publish.STATUS.normal, pageable);
    }

    @Override
    public Page<Publish> searchAll(PUBLISH searchType, String word, Pageable pageable) throws ActionParameterException {
        if (searchType == null || word == null || "".equals(word.trim()))
            return findAll(pageable);
        if (searchType == PUBLISH.uid || searchType == PUBLISH.id)
            try {
                if (Long.parseLong(word) < 0)
                    throw new ActionParameterException("参数必须为正整数数字");
            } catch (Exception e) {
                throw new ActionParameterException("参数必须为正整数数字");
            }
        if (searchType == PUBLISH.uid)
            return publishRepository.findAllByStatusAndUid(Publish.STATUS.normal, Long.parseLong(word), pageable);
        if (searchType == PUBLISH.id)
            return publishRepository.findAllByStatusAndId(Publish.STATUS.normal, Long.parseLong(word), pageable);
        if (searchType == PUBLISH.labels)
            return publishRepository.findAllByStatusAndLabelsLike(Publish.STATUS.normal, "%" + word + "%", pageable);
        if (searchType == PUBLISH.text)
            return publishRepository.findAllByStatusAndTextLike(Publish.STATUS.normal, "%" + word + "%", pageable);
        return findAll(pageable);
    }

    @Override
    public Publish getOne(Long publishId) throws ResourceNotFoundException {
        Publish publish = findOne(publishId);
        if (publish == null)
            throw new ResourceNotFoundException("找不到该动态");
        return publish;
    }

    @Override
    public void complaint(UserLogin user, Long publishId) throws ResourceNotFoundException, ActionNotSupportException {
        Publish publish = publishRepository.findByIdAndStatus(publishId, Publish.STATUS.normal);
        if (publish == null)
            throw new ResourceNotFoundException("Can not found Resource[Publish ID: " + publishId + " ]");
        Complaint complaint = new Complaint();
        complaint.setPublishId(publishId);
        System.out.println("===="+user.getUid());
        complaint.setUid(user.getUid());
        complaintRepository.save(complaint);
    }
}
