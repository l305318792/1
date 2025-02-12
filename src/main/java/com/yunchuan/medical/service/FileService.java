package com.yunchuan.medical.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {
    
    /**
     * 上传文件
     * @param file 文件
     * @param type 文件类型（AVATAR-头像，MEDICAL-医疗文件，OTHER-其他）
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String type);
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
} 