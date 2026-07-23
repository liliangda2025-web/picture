package org.example.picturebackend.service;

import org.example.picturebackend.model.dto.picture.PictureUploadRequest;
import org.example.picturebackend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.picturebackend.model.entity.User;
import org.example.picturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 * @description 针对表【picture(图片)】的数据库操作Service
 * @createDate 2026-07-23 16:35:15
 */
public interface PictureService extends IService<Picture> {
    /**
     * 上传文件
     *
     * @param file
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO upload(MultipartFile file, PictureUploadRequest pictureUploadRequest, User loginUser);
}
