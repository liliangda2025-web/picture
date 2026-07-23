package org.example.picturebackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.picturebackend.common.BaseResponse;
import org.example.picturebackend.common.ResultUtils;
import org.example.picturebackend.model.dto.picture.PictureUploadRequest;
import org.example.picturebackend.model.entity.User;
import org.example.picturebackend.model.vo.PictureVO;
import org.example.picturebackend.service.PictureService;
import org.example.picturebackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/picture")
public class PictureController {
    @Resource
    UserService userService;
    @Resource
    PictureService pictureService;

    /**
     * 图片上传
     *
     * @param multipartFile
     * @param pictureUploadRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile,
                                                 PictureUploadRequest pictureUploadRequest,
                                                 HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO result = pictureService.upload(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(result);
    }
}
