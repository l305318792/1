CREATE DATABASE IF NOT EXISTS medical DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE medical;

-- 设置连接字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- 删除现有表（按照依赖关系顺序）
SET FOREIGN_KEY_CHECKS=0; -- 临时禁用外键检查

-- 删除所有表
DROP TABLE IF EXISTS prescription;
DROP TABLE IF EXISTS consult_message;
DROP TABLE IF EXISTS doctor_rating;
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS payment_record;
DROP TABLE IF EXISTS finance;
DROP TABLE IF EXISTS medical_record;
DROP TABLE IF EXISTS consult;
DROP TABLE IF EXISTS news_tag;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS content;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS system_config;
DROP TABLE IF EXISTS health_tag;
DROP TABLE IF EXISTS health_category;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS flyway_schema_history;

SET FOREIGN_KEY_CHECKS=1; -- 重新启用外键检查

-- ====================== 基础表（无外键依赖） ======================

-- 用户表
CREATE TABLE user (
    id VARCHAR(64) PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(500) COMMENT '头像',
    gender VARCHAR(10) COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    address TEXT COMMENT '地址',
    role VARCHAR(20) NOT NULL COMMENT '角色',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 科室表
CREATE TABLE IF NOT EXISTS department (
    id VARCHAR(64) PRIMARY KEY COMMENT '科室ID',
    name VARCHAR(100) NOT NULL COMMENT '科室名称',
    parent_id VARCHAR(64) COMMENT '父级科室ID',
    introduction TEXT COMMENT '科室介绍',
    status VARCHAR(20) NOT NULL DEFAULT 'ENABLE' COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- 健康资讯分类表
CREATE TABLE health_category (
    id VARCHAR(64) PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康资讯分类表';

-- 健康标签表
CREATE TABLE health_tag (
    id VARCHAR(64) PRIMARY KEY COMMENT '标签ID',
    name VARCHAR(100) NOT NULL COMMENT '标签名称',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康标签表';

-- 系统配置表
CREATE TABLE system_config (
    id VARCHAR(64) PRIMARY KEY COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT NOT NULL COMMENT '配置值',
    description VARCHAR(500) COMMENT '描述',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ====================== 一级依赖表 ======================

-- 医生表
CREATE TABLE doctor (
    id VARCHAR(64) PRIMARY KEY COMMENT '医生ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    department_id VARCHAR(64) NOT NULL COMMENT '科室ID',
    title VARCHAR(100) COMMENT '职称',
    specialty TEXT COMMENT '专长',
    introduction TEXT COMMENT '简介',
    rating DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    consultation_count INT DEFAULT 0 COMMENT '咨询次数',
    consultation_fee DECIMAL(10,2) COMMENT '咨询费用',
    available_status VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '可用状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生表';

-- 资讯表
CREATE TABLE news (
    id VARCHAR(64) PRIMARY KEY COMMENT '资讯ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    category_id VARCHAR(64) NOT NULL COMMENT '分类ID',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES health_category(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资讯表';

-- 内容表
CREATE TABLE content (
    id VARCHAR(64) PRIMARY KEY COMMENT '内容ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    type VARCHAR(20) NOT NULL COMMENT '类型',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';

-- ====================== 二级依赖表 ======================

-- 排班表
CREATE TABLE schedule (
    id VARCHAR(64) PRIMARY KEY COMMENT '排班ID',
    doctor_id VARCHAR(64) NOT NULL COMMENT '医生ID',
    department_id VARCHAR(64) NOT NULL COMMENT '科室ID',
    schedule_date DATE NOT NULL COMMENT '排班日期',
    period VARCHAR(20) NOT NULL COMMENT '时段',
    max_appointments INT NOT NULL COMMENT '最大预约数',
    appointed_count INT NOT NULL DEFAULT 0 COMMENT '已预约数',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排班表';

-- 资讯标签关联表
CREATE TABLE news_tag (
    news_id VARCHAR(64) NOT NULL COMMENT '资讯ID',
    tag_id VARCHAR(64) NOT NULL COMMENT '标签ID',
    PRIMARY KEY (news_id, tag_id),
    FOREIGN KEY (news_id) REFERENCES news(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES health_tag(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资讯标签关联表';

-- 咨询表
CREATE TABLE consult (
    id VARCHAR(64) PRIMARY KEY COMMENT '咨询ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    doctor_id VARCHAR(64) NOT NULL COMMENT '医生ID',
    title VARCHAR(100) COMMENT '标题',
    description TEXT COMMENT '描述',
    fee DECIMAL(10,2) COMMENT '费用',
    payment_status VARCHAR(20) COMMENT '支付状态',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询表';

-- 病历表
CREATE TABLE medical_record (
    id VARCHAR(64) PRIMARY KEY COMMENT '病历ID',
    patient_id VARCHAR(64) NOT NULL COMMENT '患者ID',
    doctor_id VARCHAR(64) NOT NULL COMMENT '医生ID',
    diagnosis TEXT COMMENT '诊断',
    treatment TEXT COMMENT '治疗',
    patient_history TEXT COMMENT '病史',
    visit_time DATETIME COMMENT '就诊时间',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (patient_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病历表';

-- 财务表
CREATE TABLE finance (
    id VARCHAR(64) PRIMARY KEY COMMENT '财务ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    type VARCHAR(20) NOT NULL COMMENT '类型',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务表';

-- 支付记录表
CREATE TABLE payment_record (
    id VARCHAR(64) PRIMARY KEY COMMENT '支付记录ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    business_id VARCHAR(64) NOT NULL COMMENT '业务ID',
    business_type VARCHAR(20) NOT NULL COMMENT '业务类型',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    payment_method VARCHAR(20) NOT NULL COMMENT '支付方式',
    transaction_id VARCHAR(100) COMMENT '交易ID',
    status VARCHAR(20) NOT NULL COMMENT '支付状态',
    payment_time DATETIME COMMENT '支付时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- ====================== 三级依赖表 ======================

-- 预约表
CREATE TABLE appointment (
    id VARCHAR(64) PRIMARY KEY COMMENT '预约ID',
    schedule_id VARCHAR(64) NOT NULL COMMENT '排班ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    doctor_id VARCHAR(64) NOT NULL COMMENT '医生ID',
    department_id VARCHAR(64) NOT NULL COMMENT '科室ID',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    period VARCHAR(20) NOT NULL COMMENT '时间段',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (schedule_id) REFERENCES schedule(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 医生评价表
CREATE TABLE doctor_rating (
    id VARCHAR(64) PRIMARY KEY COMMENT '评价ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    doctor_id VARCHAR(64) NOT NULL COMMENT '医生ID',
    appointment_id VARCHAR(64) NOT NULL COMMENT '预约ID',
    service_attitude DECIMAL(2,1) DEFAULT 5.0 COMMENT '服务态度',
    medical_skill DECIMAL(2,1) DEFAULT 5.0 COMMENT '医疗技能',
    medical_effect DECIMAL(2,1) DEFAULT 5.0 COMMENT '治疗效果',
    comment TEXT NOT NULL COMMENT '评价内容',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (appointment_id) REFERENCES appointment(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生评价表';

-- 咨询消息表
CREATE TABLE consult_message (
    id VARCHAR(64) PRIMARY KEY COMMENT '消息ID',
    consult_id VARCHAR(64) NOT NULL COMMENT '咨询ID',
    sender_id VARCHAR(64) NOT NULL COMMENT '发送者ID',
    content TEXT NOT NULL COMMENT '消息内容',
    type VARCHAR(20) NOT NULL COMMENT '消息类型',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    FOREIGN KEY (consult_id) REFERENCES consult(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询消息表';

-- 处方表
CREATE TABLE prescription (
    id VARCHAR(64) PRIMARY KEY COMMENT '处方ID',
    record_id VARCHAR(64) NOT NULL COMMENT '病历ID',
    medicine TEXT NOT NULL COMMENT '药品',
    usage_method TEXT COMMENT '用法',
    dosage TEXT COMMENT '剂量',
    precautions TEXT COMMENT '注意事项',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (record_id) REFERENCES medical_record(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='处方表';

-- ====================== 创建索引 ======================

-- 用户表索引
CREATE INDEX idx_user_username ON user(username);
CREATE INDEX idx_user_phone ON user(phone);
CREATE INDEX idx_user_status ON user(status);

-- 医生表索引
CREATE INDEX idx_doctor_department ON doctor(department_id);
CREATE INDEX idx_doctor_rating ON doctor(rating);
CREATE INDEX idx_doctor_status ON doctor(available_status);

-- 预约表索引
CREATE INDEX idx_appointment_user ON appointment(user_id);
CREATE INDEX idx_appointment_doctor ON appointment(doctor_id);
CREATE INDEX idx_appointment_status ON appointment(status);
CREATE INDEX idx_appointment_time ON appointment(appointment_date);

-- 咨询表索引
CREATE INDEX idx_consult_user ON consult(user_id);
CREATE INDEX idx_consult_doctor ON consult(doctor_id);
CREATE INDEX idx_consult_status ON consult(status);

-- 医疗记录表索引
CREATE INDEX idx_record_patient ON medical_record(patient_id);
CREATE INDEX idx_record_doctor ON medical_record(doctor_id);
CREATE INDEX idx_record_status ON medical_record(status);

-- 内容相关索引
CREATE INDEX idx_category_status ON health_category(status);
CREATE INDEX idx_tag_status ON health_tag(status);
CREATE INDEX idx_news_category ON news(category_id);
CREATE INDEX idx_news_status ON news(status);
CREATE INDEX idx_content_type ON content(type);
CREATE INDEX idx_content_status ON content(status);

-- 支付相关索引
CREATE INDEX idx_payment_user ON payment_record(user_id);
CREATE INDEX idx_payment_status ON payment_record(status);
CREATE INDEX idx_payment_time ON payment_record(payment_time);
CREATE INDEX idx_finance_user ON finance(user_id);
CREATE INDEX idx_finance_type ON finance(type);
CREATE INDEX idx_finance_status ON finance(status);

-- 系统配置索引
CREATE INDEX idx_config_key ON system_config(config_key);

-- 删除末尾的测试数据插入
-- Insert test data
-- INSERT INTO department (id, name, parent_id, introduction, create_time, update_time) VALUES
-- ('d1', 'Internal Medicine', null, 'Internal Medicine is a branch of clinical medicine that focuses on diseases of internal organs', NOW(), NOW()),
-- ('d2', 'Surgery', null, 'Surgery is a branch of clinical medicine that focuses on diseases requiring surgical treatment', NOW(), NOW()),
-- ('d3', 'Pediatrics', null, 'Pediatrics is a branch of clinical medicine that focuses on childhood diseases', NOW(), NOW());

-- Insert test user
-- INSERT INTO user (id, username, password, name, phone, role, status, create_time, update_time) VALUES
-- ('u1', 'admin', '$2a$10$x6xqFZ4JOT0vxKtAQESbPuR/yRlkrTqUFE5LBEGQtB7YtFYpPV3Uu', '管理员', '13800000000', 'ADMIN', 'ENABLED', NOW(), NOW()); 