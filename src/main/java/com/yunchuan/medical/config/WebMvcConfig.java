package com.yunchuan.medical.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

/**
 * Web MVC 配置
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根路径（去掉后端项目目录）
        String projectPath = System.getProperty("user.dir");
        // 去掉 \后端\demo5
        projectPath = projectPath.substring(0, projectPath.indexOf("\\后端"));
        log.info("项目根路径: {}", projectPath);
        
        // 配置静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
                
        // 配置上传文件访问路径
        String uploadPath = "file:" + projectPath + File.separator + "upload" + File.separator;
        log.info("上传文件访问路径: {}", uploadPath);
        registry.addResourceHandler("/files/**")
                .addResourceLocations(uploadPath);
                
        // 配置Swagger UI资源
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .forEach(converter -> ((MappingJackson2HttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
    }
} 