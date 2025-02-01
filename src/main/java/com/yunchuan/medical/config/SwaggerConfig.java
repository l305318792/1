package com.yunchuan.medical.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("医疗咨询平台 API")
                .description("医疗咨询平台后端接口文档\n\n" + 
                    "# 接口规范说明\n\n" +
                    "## 1. 接口路径规范\n" +
                    "- 管理员接口: /api/admin/*\n" +
                    "- 医生接口: /api/doctor/*\n" +
                    "- 患者接口: /api/patient/*\n" +
                    "- 公共接口: /api/common/*\n" +
                    "- 认证接口: /api/auth/*\n\n" +
                    
                    "## 2. 统一响应格式\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"code\": 200,           // 状态码\n" +
                    "  \"message\": \"success\", // 提示信息\n" +
                    "  \"data\": {}             // 数据主体\n" +
                    "}\n" +
                    "```\n\n" +
                    
                    "## 3. 认证说明\n" +
                    "1. 登录接口: POST /api/auth/login\n" +
                    "   - 请求体: { username: string, password: string }\n" +
                    "   - 响应: { code: number, message: string, data: { token: string, user: {...} } }\n" +
                    "2. 认证方式:\n" +
                    "   - 需要在请求头中携带 token: Authorization: Bearer <token>\n\n" +
                    
                    "## 4. 错误码说明\n" +
                    "- 200: 成功\n" +
                    "- 400: 请求参数错误\n" +
                    "- 401: 未登录/未授权\n" +
                    "- 403: 权限不足\n" +
                    "- 404: 资源不存在\n" +
                    "- 500: 服务器错误\n\n" +
                    
                    "## 5. 分页参数说明\n" +
                    "- 请求参数:\n" +
                    "  - page: 页码，从1开始\n" +
                    "  - size: 每页大小\n" +
                    "- 响应格式:\n" +
                    "```json\n" +
                    "{\n" +
                    "  \"code\": 200,\n" +
                    "  \"message\": \"success\",\n" +
                    "  \"data\": {\n" +
                    "    \"total\": 100,    // 总记录数\n" +
                    "    \"list\": []       // 数据列表\n" +
                    "  }\n" +
                    "}\n" +
                    "```\n")
                .version("1.0.0")
                .contact(new Contact()
                    .name("yunchuan")
                    .email("yunchuan@example.com")));
    }
} 