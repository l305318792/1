package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.common.Constants;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件服务实现类
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(MultipartFile file, String type) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > Constants.File.MAX_SIZE) {
            throw new BusinessException("文件大小不能超过10MB");
        }

        // 获取文件扩展名
        String extension = "." + FilenameUtils.getExtension(file.getOriginalFilename());
        
        // 检查文件类型
        boolean isAllowedType = false;
        for (String allowType : Constants.File.ALLOW_TYPES) {
            if (allowType.equalsIgnoreCase(extension)) {
                isAllowedType = true;
                break;
            }
        }
        if (!isAllowedType) {
            throw new BusinessException("不支持的文件类型");
        }

        try {
            // 获取项目根路径（去掉后端项目目录）
            String projectPath = System.getProperty("user.dir");
            // 去掉 \后端\demo5
            projectPath = projectPath.substring(0, projectPath.indexOf("\\后端"));
            log.info("项目根路径: {}", projectPath);
            
            // 生成文件存储路径（使用File.separator确保跨平台兼容）
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy" + File.separator + "MM" + File.separator + "dd"));
            String uploadDir = "upload" + File.separator + type.toLowerCase() + File.separator + datePath;
            String absoluteUploadPath = projectPath + File.separator + uploadDir;
            
            log.info("文件存储路径: {}", absoluteUploadPath);
            
            // 创建目录
            File directory = new File(absoluteUploadPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    log.error("创建目录失败: {}", absoluteUploadPath);
                    throw new BusinessException("创建目录失败");
                }
                log.info("创建目录成功: {}", absoluteUploadPath);
            }

            // 生成新的文件名
            String newFileName = UUID.randomUUID().toString().replace("-", "") + extension;
            String filePath = absoluteUploadPath + File.separator + newFileName;
            log.info("完整文件路径: {}", filePath);

            // 保存文件
            File dest = new File(filePath);
            file.transferTo(dest);
            log.info("文件保存成功: {}", filePath);

            // 返回文件访问URL（使用正斜杠，确保URL格式正确）
            String url = "/files/" + type.toLowerCase() + "/" + datePath.replace(File.separator, "/") + "/" + newFileName;
            log.info("文件访问URL: {}", url);
            return url;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            // 获取项目根路径（去掉后端项目目录）
            String projectPath = System.getProperty("user.dir");
            projectPath = projectPath.substring(0, projectPath.indexOf("\\后端"));
            
            // 获取文件的实际路径
            String filePath = projectPath + File.separator + "upload" + fileUrl.substring(6).replace("/", File.separator); // 去掉"/files"前缀
            log.info("要删除的文件路径: {}", filePath);
            
            File file = new File(filePath);
            if (!file.exists()) {
                log.warn("文件不存在: {}", filePath);
                return false;
            }
            
            // 删除文件
            boolean deleted = file.delete();
            if (deleted) {
                log.info("文件删除成功: {}", filePath);
            } else {
                log.error("文件删除失败: {}", filePath);
            }
            return deleted;
            
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new BusinessException("文件删除失败：" + e.getMessage());
        }
    }
} 