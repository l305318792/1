# API测试进度记录

## ✅ 已完成测试的模块

### 1. 用户认证
- 登录 POST `/auth/login`
- 登出 POST `/auth/logout`

### 2. 医生问诊管理
- 获取问诊列表 GET `/doctor/consultations`
- 获取问诊详情 GET `/doctor/consultations/{id}`
- 开始问诊 POST `/doctor/consultations/{id}/start`
- 结束问诊 POST `/doctor/consultations/{id}/complete`
- 开具处方 POST `/doctor/consultations/{consultationId}/prescriptions`
- 获取处方列表 GET `/doctor/consultations/{consultationId}/prescriptions`

## ❌ 待测试的模块

### 1. 医生工作台
- 获取医生排班列表 GET `/doctor/schedules`
- 更新排班信息 PUT `/doctor/schedules/{id}`
- 获取待处理问诊列表 GET `/doctor/consultations/pending`
- 获取问诊统计信息 GET `/doctor/statistics`

### 2. 患者管理
- 获取患者列表 GET `/doctor/patients`
- 获取患者详情 GET `/doctor/patients/{id}`
- 获取患者问诊记录 GET `/doctor/patients/{id}/consultations`
- 获取患者处方记录 GET `/doctor/patients/{id}/prescriptions`

### 3. 评价管理
- 获取医生评价列表 GET `/doctor/ratings`
- 回复患者评价 POST `/doctor/ratings/{id}/reply`

### 4. 系统管理（管理员）
- 科室管理（增删改查）
  - 获取科室列表 GET `/admin/departments`
  - 获取科室详情 GET `/admin/departments/{id}`
  - 创建科室 POST `/admin/departments`
  - 更新科室 PUT `/admin/departments/{id}`
  - 删除科室 DELETE `/admin/departments/{id}`

- 医生管理（增删改查）
  - 获取医生列表 GET `/admin/doctors`
  - 获取医生详情 GET `/admin/doctors/{id}`
  - 创建医生 POST `/admin/doctors`
  - 更新医生 PUT `/admin/doctors/{id}`
  - 删除医生 DELETE `/admin/doctors/{id}`

- 用户管理（增删改查）
  - 获取用户列表 GET `/admin/users`
  - 获取用户详情 GET `/admin/users/{id}`
  - 创建用户 POST `/admin/users`
  - 更新用户 PUT `/admin/users/{id}`
  - 删除用户 DELETE `/admin/users/{id}`

- 问诊管理
  - 获取问诊列表 GET `/admin/consultations`
  - 获取问诊详情 GET `/admin/consultations/{id}`
  - 审核问诊 POST `/admin/consultations/{id}/review`

- 处方管理
  - 获取处方列表 GET `/admin/prescriptions`
  - 获取处方详情 GET `/admin/prescriptions/{id}`
  - 审核处方 POST `/admin/prescriptions/{id}/review`

- 评价管理
  - 获取评价列表 GET `/admin/ratings`
  - 获取评价详情 GET `/admin/ratings/{id}`
  - 审核评价 POST `/admin/ratings/{id}/review`

- 公告管理
  - 获取公告列表 GET `/admin/announcements`
  - 获取公告详情 GET `/admin/announcements/{id}`
  - 创建公告 POST `/admin/announcements`
  - 更新公告 PUT `/admin/announcements/{id}`
  - 删除公告 DELETE `/admin/announcements/{id}`
  - 发布公告 POST `/admin/announcements/{id}/publish`

- 统计报表
  - 获取问诊统计 GET `/admin/statistics/consultations`
  - 获取收入统计 GET `/admin/statistics/income`
  - 获取评价统计 GET `/admin/statistics/ratings`

## 测试进度
- 已完成：8个接口
- 待测试：约40个接口
- 总计：约48个接口
- 完成度：约17%

## 下一步计划
继续测试医生工作台模块的接口：
1. 获取医生排班列表
2. 更新排班信息
3. 获取待处理问诊列表
4. 获取问诊统计信息 