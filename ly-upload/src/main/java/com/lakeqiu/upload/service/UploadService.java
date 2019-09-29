package com.lakeqiu.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lakeqiu
 */
public interface UploadService {
    /**
     * 上传图片功能
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);
}
