package org.example.picturebackend.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.example.picturebackend.annotation.AuthCheck;
import org.example.picturebackend.common.BaseResponse;
import org.example.picturebackend.common.ResultUtils;
import org.example.picturebackend.constant.UserConstant;
import org.example.picturebackend.exception.BusinessException;
import org.example.picturebackend.exception.ErrorCode;
import org.example.picturebackend.exception.ThrowUtils;
import org.example.picturebackend.manager.CosManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class fileController {
    @Resource
    CosManager cosManager;

    /**
     * 测试上传文件
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/test/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> testUpload(@RequestPart("file") MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR);
        String fileName = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", fileName);
        File file = null;
        try {
            //尝试上传文件
            //创建空的临时文件
            file = File.createTempFile(filePath, null);
            //将文件内容复制到空的文件中去
            multipartFile.transferTo(file);
            cosManager.putObject(filePath, file);
            //返回上传路径名
            return ResultUtils.success(filePath);

        } catch (Exception e) {
            log.error("file upload error, filePath = " + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败！");
        } finally {
            //删除临时文件
            boolean isDelete = file.delete();
            if (!isDelete) {
                log.error("file delete error, filePath =  {}", filePath);
            }
        }

    }

    /**
     * 测试文件下载
     *
     * @param filePath
     * @param response
     * @throws IOException
     */
    @GetMapping("/test/download")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public void testFileDownload(String filePath, HttpServletResponse response) throws IOException {
        ThrowUtils.throwIf(filePath == null, ErrorCode.PARAMS_ERROR);
        COSObjectInputStream objectContent = null;
        try {
            COSObject object = cosManager.getObject(filePath);
            objectContent = object.getObjectContent();
            //处理下载到的流
            byte[] objectBytes = IOUtils.toByteArray(objectContent);
            //设计响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filePath);
            response.getOutputStream().write(objectBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filePath = " + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件下载失败！");
        } finally {
            if (objectContent != null) {
                objectContent.close();
            }
        }


    }
}
