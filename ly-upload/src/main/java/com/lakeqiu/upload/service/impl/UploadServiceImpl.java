package com.lakeqiu.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.upload.config.UploadProperties;
import com.lakeqiu.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * 上传文件服务
 * @author lakeqiu
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl implements UploadService {
    /**
     * 注入上传客户端
     */
    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties properties;

//    private static final List<String> ALLOW_TYPE = Arrays.asList("image/jpeg", "image/png", "image/bmp");

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 校验文件类型
            // 获取文件类型
            String contentType = file.getContentType();
            if (!properties.getAllowTypes().contains(contentType)){
                throw new LyException(ExpectionEnum.INVALID_FILE_TYPE);
            }
            // 校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (null == image){
                throw new LyException(ExpectionEnum.INVALID_FILE_TYPE);
            }
            // 上传到FastDFS
            // 获取格式名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);

            System.out.println(storePath.getFullPath());
            // 返回路径
            return properties.getBaseUrl() + storePath.getFullPath();
        }catch (Exception e){
            // 上传失败
            log.error("上传图片失败", e);
            throw new LyException(ExpectionEnum.INVALID_FILE_TYPE);
        }
    }
}
