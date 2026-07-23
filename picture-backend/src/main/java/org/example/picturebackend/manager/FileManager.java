package org.example.picturebackend.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.picturebackend.config.CosClientConfig;
import org.example.picturebackend.exception.BusinessException;
import org.example.picturebackend.exception.ErrorCode;
import org.example.picturebackend.exception.ThrowUtils;
import org.example.picturebackend.model.dto.file.UploadPictureResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileManager {

    @Resource
    CosClientConfig cosClientConfig;
    @Resource
    CosManager cosManager;

    /**
     * 图片上传
     *
     * @param file             文件
     * @param uploadPathPrefix 文件路径前缀
     * @return
     */
    public UploadPictureResult uploadPicture(MultipartFile file, String uploadPathPrefix) {
        //校验图片
        validPicture(file);
        //图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originFileName = file.getOriginalFilename();
        String uploadFileName = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originFileName));
        String uploadPath = String.format("%s/%s", uploadPathPrefix, uploadFileName);
        File tempFile = null;
        try {
            //创建临时文件
            tempFile = File.createTempFile(uploadPath, null);
            file.transferTo(tempFile);
            //上传文件
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, tempFile);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            //封转文件解析结果
            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            int picWith = imageInfo.getWidth();
            int picHigh = imageInfo.getHeight();
            //计算宽高比，并四舍五入后两位（因为 round 返回的是 BigDecimal，而最终变量 picScale 声明为 double，所以调用 doubleValue() 将其转换为 double 基本类型。）
            double picScale = NumberUtil.round(picWith * 1.0 / picHigh, 2).doubleValue();

            //封装结果
            uploadPictureResult.setPicName(FileUtil.mainName(originFileName));
            uploadPictureResult.setPicWidth(picWith);
            uploadPictureResult.setPicHeight(picHigh);
            uploadPictureResult.setPicScale(picScale);
            uploadPictureResult.setPicFormat(imageInfo.getFormat());
            uploadPictureResult.setPicSize(FileUtil.size(tempFile));
            uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);

            return uploadPictureResult;
        } catch (Exception e) {
            log.error("图片上传失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败！");
        } finally {
            //删除临时文件
            deleteTempFile(tempFile);
        }

    }

    /**
     * 文件校验
     *
     * @param file
     */
    public void validPicture(MultipartFile file) {
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR, "文件不能为空！");

        //1.校验文件大小
        long fileSize = file.getSize();
        final long OBE_M = 1024 * 1024L;
        ThrowUtils.throwIf(fileSize > 2 * OBE_M, ErrorCode.PARAMS_ERROR, "文件大小不能超过2M!");
        //2.校验文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("png", "jpg", "jpeg", "webp");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(suffix), ErrorCode.PARAMS_ERROR, "文件类型错误！");
    }

    /**
     * 删除临时文件
     *
     * @param file
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        if (!file.delete()) {
            log.error("file delete error, filePath =  {}", file.getAbsolutePath());
        }
    }


}
