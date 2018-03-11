package com.seeu.file.aliyun_storage;

import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.trend.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @see com.seeu.third.filestore.FileUploadService
 * <p>
 * 该接口已经被 FileUploadService 替换，请及时更替（更替完成）
 */
@Deprecated
public interface StorageImageService {


    Result saveImages(MultipartFile[] files, Picture.ALBUM_TYPE[] albumTypes) throws Exception;


    public class Result {
        public enum STATUS {
            success,
            failure,
            exception
        }

        private STATUS status;
        private List<Image> imageList;
        private int imageNum;

        public STATUS getStatus() {
            return status;
        }

        public void setStatus(STATUS status) {
            this.status = status;
        }

        public List<Image> getImageList() {
            return imageList;
        }

        public void setImageList(List<Image> imageList) {
            this.imageList = imageList;
        }

        public int getImageNum() {
            return imageNum;
        }

        public void setImageNum(int imageNum) {
            this.imageNum = imageNum;
        }
    }
}
