package org.example.picturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.picturebackend.exception.ErrorCode;
import org.example.picturebackend.exception.ThrowUtils;
import org.example.picturebackend.manager.FileManager;
import org.example.picturebackend.model.dto.file.UploadPictureResult;
import org.example.picturebackend.model.dto.picture.PictureUploadRequest;
import org.example.picturebackend.model.entity.Picture;
import org.example.picturebackend.model.entity.User;
import org.example.picturebackend.model.vo.PictureVO;
import org.example.picturebackend.service.PictureService;
import org.example.picturebackend.mapper.PictureMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Administrator
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2026-07-23 16:35:15
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {

    @Resource
    FileManager fileManager;

    @Override
    public PictureVO upload(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NO_AUTH_ERROR);
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR);
        //判断图片是新增还是更新
        Long pictureId = null;
        if (pictureUploadRequest != null) {
            pictureId = pictureUploadRequest.getId();
        }

        //如果是图片更新，还要校验图片是否存在
        if (pictureId != null) {
            boolean exist = this.lambdaQuery().eq(Picture::getId, pictureId).exists();
            ThrowUtils.throwIf(!exist, ErrorCode.PARAMS_ERROR, "图片不存在！");
        }

        //上传图片，得到信息
        //根据用户id划分目录
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        //获得入库图片信息
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());

        //根据图片id确定是新增还是更新
        if (pictureId != null) {
            //更新需要补充Id和编辑时间
            picture.setId(pictureId);
            picture.setUpdateTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "图片上传失败");
        return PictureVO.objToVo(picture);
    }
}




