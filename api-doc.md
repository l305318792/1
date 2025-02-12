# 运城市移动医疗咨询平台 API文档

## 基础信息
- 基础URL: `http://localhost:8080`
- 认证方式: Bearer Token

## 1. 用户认证

### 1.1 用户登录
- 请求路径：`/api/auth/login`
- 请求方法：POST
- 请求参数：
```json
{
    "username": "doctor1",
    "password": "123456"
}
```
- 返回示例：
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
}
```

## 2. 医生管理

### 2.1 获取医生列表
- 请求路径：`/doctor/list`
- 请求方法：GET
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "success",
    "data": [
        {
            "id": "doc1",
            "name": "张医生",
            "departmentId": "1",
            "departmentName": "内科",
            "title": "主任医师",
            "specialty": "内科疾病",
            "consultationFee": 50,
            "rating": 4.8
        }
    ]
}
```

## 3. 排班管理

### 3.1 获取医生排班
- 请求路径：`/api/admin/schedules`
- 请求方法：GET
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "id": "b79d78c809ce45d18be948894a909a59",
            "doctorId": "4",
            "doctorName": "张医生",
            "departmentId": "1",
            "departmentName": "内科",
            "scheduleDate": "2025-02-04",
            "period": "MORNING",
            "maxAppointments": 10,
            "appointedCount": 0,
            "status": "1",
            "remark": "上午门诊"
        }
    ]
}
```

## 4. 预约管理

### 4.1 创建预约
- 请求路径：`/appointment`
- 请求方法：POST
- 请求头：`Authorization: Bearer {token}`
- 请求参数：
```json
{
    "doctorId": "4",
    "departmentId": "1",
    "scheduleId": "b79d78c809ce45d18be948894a909a59",
    "appointmentTime": "2025-02-04T09:00:00"
}
```
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1886098140035559425",
        "status": "UNPAID",
        "doctorName": "张医生",
        "departmentName": "内科"
    }
}
```

### 4.2 支付预约
- 请求路径：`/appointment/{appointmentId}/pay`
- 请求方法：POST
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1886098140035559425",
        "status": "PAID"
    }
}
```

### 4.3 取消预约
- 请求路径：`/appointment/{appointmentId}/cancel`
- 请求方法：POST
- 请求头：`Authorization: Bearer {token}`
- 请求参数：
```json
{
    "reason": "个人原因"
}
```
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1886642419493511169",
        "status": "CANCELLED"
    }
}
```

### 4.4 完成预约
- 请求路径：`/appointment/{appointmentId}/complete`
- 请求方法：POST
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1886643400818040834",
        "status": "COMPLETED"
    }
}
```

### 4.5 获取我的预约列表
- 请求路径：`/appointment/my`
- 请求方法：GET
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "id": "1886643400818040834",
            "doctorName": "张医生",
            "departmentName": "内科",
            "appointmentTime": "2025-02-04T14:00:00",
            "status": "COMPLETED"
        }
    ]
}
```

## 5. 问诊管理

### 5.1 创建问诊
- 请求路径：`/patient/consultations`
- 请求方法：POST
- 请求头：`Authorization: Bearer {token}`
- 请求参数：
```json
{
    "doctorId": "4",
    "departmentId": "1",
    "symptoms": "头痛，发烧38度"
}
```
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "e0ecf5d55ef24d3ea1d4e7b12b667c08",
        "userName": "张三",
        "status": "PENDING"
    }
}
```

### 5.2 获取我的问诊列表
- 请求路径：`/patient/consultations/my`
- 请求方法：GET
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "id": "e0ecf5d55ef24d3ea1d4e7b12b667c08",
            "userName": "张三",
            "doctorName": "张医生",
            "symptoms": "头痛，发烧38度",
            "status": "PENDING"
        }
    ]
}
```

## 6. 评价管理

### 6.1 创建医生评价
- 请求路径：`/doctor-rating`
- 请求方法：POST
- 请求头：`Authorization: Bearer {token}`
- 请求参数：
```json
{
    "appointmentId": "1886643400818040834",
    "serviceAttitude": 5.0,
    "medicalSkill": 5.0,
    "medicalEffect": 4.5,
    "comment": "医生很专业，服务态度很好，治疗效果不错"
}
```
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "f07981e1-bc94-4a15-bec0-873844a74e62",
        "doctorName": "张医",
        "userName": "张三"
    }
}
```

### 6.2 获取医生评价列表
- 请求路径：`/doctor-rating/doctor/{doctorId}`
- 请求方法：GET
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "id": "f07981e1-bc94-4a15-bec0-873844a74e62",
            "doctorName": "张医",
            "userName": "张三",
            "serviceAttitude": 5,
            "medicalSkill": 5,
            "medicalEffect": 5,
            "comment": "医生很专业，服务态度很好，治疗效果不错"
        }
    ]
}
```

### 6.3 获取我的评价列表
- 请求路径：`/doctor-rating/my`
- 请求方法：GET
- 请求头：`Authorization: Bearer {token}`
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "id": "f07981e1-bc94-4a15-bec0-873844a74e62",
            "doctorName": "张医",
            "serviceAttitude": 5,
            "medicalSkill": 5,
            "medicalEffect": 5,
            "comment": "医生很专业，服务态度很好，治疗效果不错"
        }
    ]
}
```

## 7. 个人信息管理

### 7.1 更新个人信息
- 请求路径：`/patient/profile`
- 请求方法：PUT
- 请求头：`Authorization: Bearer {token}`
- 请求参数：
```json
{
    "name": "张三",
    "phone": "13800000003",
    "email": "zhangsan@example.com"
}
```
- 返回示例：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "u4",
        "username": "patient1",
        "name": "张三",
        "phone": "13800000003",
        "email": "zhangsan@example.com"
    }
}
```

## 8. 注意事项

1. 所有需要认证的接口都需要在请求头中携带token：
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

2. 错误响应格式：
```json
{
    "code": 400,
    "message": "错误信息"
}
```

3. 分页参数说明：
- current: 从1开始的页码
- size: 每页记录数，默认10

## 9. 测试账号

### 9.1 医生账号
- 用户名：doctor1
- 密码：123456

### 9.2 患者账号
- 用户名：patient1
- 密码：123456

### 9.3 管理员账号
- 用户名：admin
- 密码：123456 