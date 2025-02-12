# 运城市移动医疗咨询平台

## 项目介绍
运城市移动医疗咨询平台是一个基于Spring Boot的医疗咨询系统，提供在线问诊、预约挂号、视频问诊等功能。

## 技术栈
- Spring Boot 3.2.0
- Spring Security
- MyBatis-Plus
- MySQL
- Redis
- WebSocket
- JWT
- Swagger/OpenAPI

## 主要功能
- 用户认证与授权
- 在线问诊
- 预约挂号
- 视频问诊
- 医生排班
- 病历管理
- AI智能问诊
- 支付管理

## 环境要求
- JDK 21
- MySQL 8.0+
- Redis 6.0+
- Maven 3.8+

## 快速开始

### 1. 配置环境变量
```bash
# 数据库配置
export MYSQL_USERNAME=your_username
export MYSQL_PASSWORD=your_password

# Redis配置
export REDIS_HOST=localhost
export REDIS_PORT=6379

# JWT配置
export JWT_SECRET=your_jwt_secret

# AI配置
export AI_MODEL=deepseek-r1
export AI_API_URL=http://localhost:11434/api/generate
```

### 2. 初始化数据库
```sql
CREATE DATABASE medical DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 编译运行
```bash
mvn clean package
java -jar target/medical-1.0.0.jar
```

## API文档
启动应用后访问: http://localhost:8080/swagger-ui.html

## 目录结构
```
src/main/java/com/yunchuan/medical/
├── config/          # 配置类
├── controller/      # 控制器
├── service/         # 服务层
├── mapper/          # MyBatis映射器
├── entity/          # 实体类
├── dto/            # 数据传输对象
├── security/       # 安全相关
└── util/           # 工具类
```

## 开发团队
- 开发者: yunchuan

## 许可证
MIT License 