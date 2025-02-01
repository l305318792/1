# 运城市移动医疗咨询平台 API 文档

## 目录
- [1. 概述](#1-概述)
- [2. 公共说明](#2-公共说明)
- [3. 接口列表](#3-接口列表)

## 1. 概述

本文档详细说明运城市移动医疗咨询平台的后端接口规范。

## 2. 公共说明

### 2.1 接口认证
- 除了登录和公共接口外，所有接口都需要在header中携带token
- 格式：`Authorization: Bearer_${token}`

### 2.2 响应格式
```json
{
    "code": 200,          // 状态码：200成功，400错误，500系统异常
    "message": "success", // 响应消息
    "data": {}           // 响应数据
}
```

## 3. 接口列表

### 3.1 认证模块

#### 3.1.1 用户登录
- 请求路径：`/auth/login`
- 请求方法：POST
- 请求参数：
```json
{
    "username": "admin",
    "password": "123456"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "token": "xxx",
        "userId": "1",
        "username": "admin",
        "name": "管理员",
        "role": "ADMIN",
        "doctorId": null
    }
}
```

### 3.2 管理员模块

#### 3.2.1 用户管理

##### 获取用户列表
- 请求路径：`/admin/users`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 10,
        "list": [
            {
                "id": "1",
                "username": "zhangsan",
                "name": "张三",
                "role": "USER",
                "status": "normal",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

##### 创建用户
- 请求路径：`/admin/users`
- 请求方法：POST
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
```json
{
    "username": "zhangsan",
    "password": "123456",
    "name": "张三",
    "role": "USER"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "username": "zhangsan",
        "name": "张三",
        "role": "USER",
        "status": "normal",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

#### 3.2.2 部门管理

##### 获取部门列表
- 请求路径：`/admin/departments`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 3,
        "list": [
            {
                "id": "1",
                "name": "内科",
                "description": "主要处理内脏疾病",
                "status": "normal",
                "doctorCount": 5,
                "consultationCount": 100,
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

##### 创建部门
- 请求路径：`/admin/departments`
- 请求方法：POST
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
```json
{
    "name": "内科",
    "description": "主要处理内脏疾病"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "name": "内科",
        "description": "主要处理内脏疾病",
        "status": "normal",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

#### 3.2.3 医生管理

##### 获取医生列表
- 请求路径：`/admin/doctors`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 5,
        "list": [
            {
                "id": "1",
                "name": "张医生",
                "departmentId": "1",
                "departmentName": "内科",
                "title": "主任医师",
                "specialty": "消化系统疾病",
                "introduction": "从医20年，擅长...",
                "consultationCount": 1000,
                "rating": 4.8,
                "status": "normal",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

### 3.3 医生模块

#### 3.3.1 问诊管理

##### 获取问诊列表
- 请求路径：`/doctor/consultations`
- 请求方法：GET
- 请求头：`Authorization: Bearer_doctor1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 2,
        "list": [
            {
                "id": "1",
                "userId": "u1",
                "userName": "张三",
                "symptoms": "反复头痛，持续一周",
                "status": "PENDING",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

##### 开始问诊
- 请求路径：`/doctor/consultations/{id}/start`
- 请求方法：PUT
- 请求头：`Authorization: Bearer_doctor1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "status": "IN_PROGRESS",
        "startTime": "2024-01-01 10:30:00"
    }
}
```

##### 完成问诊
- 请求路径：`/doctor/consultations/{id}/complete`
- 请求方法：PUT
- 请求头：`Authorization: Bearer_doctor1`
- 请求参数：
```json
{
    "diagnosis": "偏头痛",
    "treatment": "建议服用..."
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "status": "COMPLETED",
        "endTime": "2024-01-01 11:00:00"
    }
}
```

### 3.4 患者模块

#### 3.4.1 预约管理

##### 创建预约
- 请求路径：`/patient/appointments`
- 请求方法：POST
- 请求头：`Authorization: Bearer_patient1`
- 请求参数：
```json
{
    "doctorId": "1",
    "departmentId": "1",
    "scheduleId": "1",
    "symptoms": "反复头痛，持续一周"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "status": "PENDING",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

##### 获取预约列表
- 请求路径：`/patient/appointments`
- 请求方法：GET
- 请求头：`Authorization: Bearer_patient1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 2,
        "list": [
            {
                "id": "1",
                "doctorId": "1",
                "doctorName": "张医生",
                "departmentId": "1",
                "departmentName": "内科",
                "symptoms": "反复头痛，持续一周",
                "status": "PENDING",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

### 3.5 公共模块

#### 3.5.1 获取部门下拉列表
- 请求路径：`/common/departments/select`
- 请求方法：GET
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "label": "内科",
            "value": "1"
        },
        {
            "label": "外科",
            "value": "2"
        }
    ]
}
```

#### 3.5.2 获取医生下拉列表
- 请求路径：`/common/doctors/select`
- 请求方法：GET
- 请求参数：`departmentId=1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "label": "张医生",
            "value": "1"
        },
        {
            "label": "李医生",
            "value": "2"
        }
    ]
}
```

### 3.6 系统管理模块

#### 3.6.1 系统日志管理

##### 获取日志列表
- 请求路径：`/admin/logs`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
  - username: 用户名
  - operationType: 操作类型(LOGIN/LOGOUT)
  - status: 状态(SUCCESS/FAIL)
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 10,
        "list": [
            {
                "id": "1",
                "userId": "1",
                "username": "admin",
                "operationType": "LOGIN",
                "description": "用户登录",
                "module": "认证模块",
                "ip": "127.0.0.1",
                "status": "SUCCESS",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

#### 3.6.2 系统配置管理

##### 获取配置列表
- 请求路径：`/admin/configs`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 5,
        "list": [
            {
                "id": "1",
                "configKey": "system.name",
                "configValue": "运城市移动医疗咨询平台",
                "description": "系统名称",
                "type": "text",
                "editable": true,
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

##### 更新配置
- 请求路径：`/admin/configs/{id}`
- 请求方法：PUT
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
```json
{
    "configValue": "新的系统名称"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": null
}
```

### 3.7 评价管理

#### 3.7.1 医生评价

##### 创建评价
- 请求路径：`/patient/doctors/{doctorId}/ratings`
- 请求方法：POST
- 请求头：`Authorization: Bearer_patient1`
- 请求参数：
```json
{
    "consultationId": "1",
    "rating": 5,
    "content": "医生很专业,态度很好"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

##### 获取医生评价列表
- 请求路径：`/doctor/ratings`
- 请求方法：GET
- 请求头：`Authorization: Bearer_doctor1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 10,
        "list": [
            {
                "id": "1",
                "userId": "u1",
                "userName": "张三",
                "consultationId": "1",
                "rating": 5,
                "content": "医生很专业,态度很好",
                "status": "normal",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

### 3.8 支付管理

#### 3.8.1 支付记录

##### 创建支付订单
- 请求路径：`/patient/payments`
- 请求方法：POST
- 请求头：`Authorization: Bearer_patient1`
- 请求参数：
```json
{
    "orderType": "CONSULTATION",
    "orderId": "1",
    "amount": 100,
    "paymentMethod": "WECHAT"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "status": "PENDING",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

##### 获取支付记录列表
- 请求路径：`/patient/payments`
- 请求方法：GET
- 请求头：`Authorization: Bearer_patient1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 5,
        "list": [
            {
                "id": "1",
                "orderType": "CONSULTATION",
                "orderId": "1",
                "amount": 100,
                "status": "SUCCESS",
                "paymentMethod": "WECHAT",
                "createTime": "2024-01-01 10:00:00",
                "payTime": "2024-01-01 10:01:00"
            }
        ]
    }
}
```

### 3.9 健康资讯

#### 3.9.1 资讯管理

##### 获取资讯列表
- 请求路径：`/common/articles`
- 请求方法：GET
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 10,
        "list": [
            {
                "id": "1",
                "title": "如何预防感冒",
                "summary": "冬季来临,预防感冒很重要...",
                "author": "张医生",
                "viewCount": 1000,
                "status": "published",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

##### 获取资讯详情
- 请求路径：`/common/articles/{id}`
- 请求方法：GET
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "title": "如何预防感冒",
        "content": "1. 注意保暖...",
        "author": "张医生",
        "viewCount": 1000,
        "status": "published",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

### 3.10 数据备份/恢复

#### 3.10.1 备份管理

##### 获取备份列表
- 请求路径：`/admin/backups`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 5,
        "list": [
            {
                "id": "1",
                "fileName": "backup_20240101.sql",
                "fileSize": "1.5MB",
                "type": "AUTO",
                "description": "系统自动备份",
                "status": "SUCCESS",
                "createTime": "2024-01-01 00:00:00"
            }
        ]
    }
}
```

##### 创建备份
- 请求路径：`/admin/backups`
- 请求方法：POST
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
```json
{
    "description": "手动备份"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "status": "SUCCESS",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

##### 恢复数据
- 请求路径：`/admin/backups/{id}/restore`
- 请求方法：POST
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": null
}
```

##### 删除备份
- 请求路径：`/admin/backups/{id}`
- 请求方法：DELETE
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": null
}
```

### 3.11 医生排班管理

#### 3.11.1 排班管理

##### 获取排班列表
- 请求路径：`/admin/schedules`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "total": 10,
        "list": [
            {
                "id": "1",
                "doctorId": "1",
                "doctorName": "张医生",
                "departmentId": "1",
                "departmentName": "内科",
                "scheduleDate": "2024-01-01",
                "period": "MORNING",
                "maxAppointments": 20,
                "appointedCount": 5,
                "status": "normal",
                "createTime": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

##### 创建排班
- 请求路径：`/admin/schedules`
- 请求方法：POST
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
```json
{
    "doctorId": "1",
    "scheduleDate": "2024-01-01",
    "period": "MORNING",
    "maxAppointments": 20
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": "1",
        "createTime": "2024-01-01 10:00:00"
    }
}
```

##### 更新排班
- 请求路径：`/admin/schedules/{id}`
- 请求方法：PUT
- 请求头：`Authorization: Bearer_admin`
- 请求参数：
```json
{
    "maxAppointments": 30,
    "status": "normal"
}
```
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": null
}
```

##### 删除排班
- 请求路径：`/admin/schedules/{id}`
- 请求方法：DELETE
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": null
}
```

### 3.12 统计分析

#### 3.12.1 医生工作统计

##### 获取医生工作统计
- 请求路径：`/doctor/statistics`
- 请求方法：GET
- 请求头：`Authorization: Bearer_doctor1`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "totalConsultations": 1000,
        "completedConsultations": 800,
        "pendingConsultations": 10,
        "cancelledConsultations": 190,
        "monthlyStats": {
            "total": 100,
            "completed": 80,
            "pending": 10,
            "cancelled": 10
        },
        "satisfactionStats": {
            "averageRating": 4.8,
            "ratingCounts": {
                "5": 800,
                "4": 150,
                "3": 40,
                "2": 8,
                "1": 2
            }
        },
        "dailyConsultations": [
            {"date": "2024-01-01", "count": 10},
            {"date": "2024-01-02", "count": 12},
            {"date": "2024-01-03", "count": 8}
        ]
    }
}
```

#### 3.12.2 预约统计

##### 获取预约统计
- 请求路径：`/admin/appointments/statistics`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "totalAppointments": 1000,
        "completedAppointments": 800,
        "pendingAppointments": 100,
        "cancelledAppointments": 100,
        "departmentStats": [
            {
                "departmentId": "1",
                "departmentName": "内科",
                "total": 500,
                "completed": 400
            }
        ],
        "doctorStats": [
            {
                "doctorId": "1",
                "doctorName": "张医生",
                "total": 300,
                "completed": 250
            }
        ],
        "dailyStats": [
            {"date": "2024-01-01", "count": 50},
            {"date": "2024-01-02", "count": 45},
            {"date": "2024-01-03", "count": 55}
        ]
    }
}
```

#### 3.12.3 收入统计

##### 获取收入统计
- 请求路径：`/admin/payments/statistics`
- 请求方法：GET
- 请求头：`Authorization: Bearer_admin`
- 响应数据：
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "totalIncome": 100000,
        "monthlyIncome": 10000,
        "dailyIncome": 1000,
        "paymentMethodStats": {
            "WECHAT": 60000,
            "ALIPAY": 40000
        },
        "monthlyStats": [
            {"month": "2024-01", "amount": 10000},
            {"month": "2024-02", "amount": 12000}
        ],
        "dailyStats": [
            {"date": "2024-01-01", "amount": 1000},
            {"date": "2024-01-02", "amount": 1200}
        ]
    }
}
``` 