-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `consult_message`;
DROP TABLE IF EXISTS `rating`;
DROP TABLE IF EXISTS `appointment`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `doctor`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `department`;

-- 科室表
CREATE TABLE `department` (
    `id` VARCHAR(32) NOT NULL COMMENT '科室ID',
    `name` VARCHAR(50) NOT NULL COMMENT '科室名称',
    `introduction` TEXT COMMENT '科室介绍',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室表';

-- 用户表
CREATE TABLE `user` (
    `id` VARCHAR(32) NOT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `name` VARCHAR(50) COMMENT '姓名',
    `role` VARCHAR(20) NOT NULL COMMENT '角色',
    `phone` VARCHAR(20) COMMENT '电话',
    `email` VARCHAR(50) COMMENT '邮箱',
    `avatar` VARCHAR(200) COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 医生表
CREATE TABLE `doctor` (
    `id` VARCHAR(32) NOT NULL COMMENT '医生ID',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID',
    `department_id` VARCHAR(32) NOT NULL COMMENT '科室ID',
    `title` VARCHAR(50) NOT NULL COMMENT '职称',
    `specialty` VARCHAR(200) COMMENT '专长',
    `introduction` TEXT COMMENT '简介',
    `consultation_fee` DECIMAL(10,2) NOT NULL COMMENT '咨询费用',
    `rating` DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    `rating_count` INT DEFAULT 0 COMMENT '评分次数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停诊，1-接诊',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生表';

-- 排班表
CREATE TABLE `schedule` (
    `id` VARCHAR(32) NOT NULL COMMENT '排班ID',
    `doctor_id` VARCHAR(32) NOT NULL COMMENT '医生ID',
    `date` DATE NOT NULL COMMENT '日期',
    `period` VARCHAR(20) NOT NULL COMMENT '时段：MORNING-上午，AFTERNOON-下午',
    `max_patients` INT NOT NULL COMMENT '最大接诊人数',
    `booked_patients` INT NOT NULL DEFAULT 0 COMMENT '已预约人数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停诊，1-正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_doctor_date_period` (`doctor_id`, `date`, `period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排班表';

-- 预约表
CREATE TABLE `appointment` (
    `id` VARCHAR(32) NOT NULL COMMENT '预约ID',
    `patient_id` VARCHAR(32) NOT NULL COMMENT '患者ID',
    `doctor_id` VARCHAR(32) NOT NULL COMMENT '医生ID',
    `schedule_id` VARCHAR(32) NOT NULL COMMENT '排班ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待就诊，1-已完成，2-已取消',
    `symptoms` TEXT COMMENT '症状描述',
    `diagnosis` TEXT COMMENT '诊断结果',
    `prescription` TEXT COMMENT '处方',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_patient_id` (`patient_id`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_schedule_id` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 评价表
CREATE TABLE `rating` (
    `id` VARCHAR(32) NOT NULL COMMENT '评价ID',
    `appointment_id` VARCHAR(32) NOT NULL COMMENT '预约ID',
    `patient_id` VARCHAR(32) NOT NULL COMMENT '患者ID',
    `doctor_id` VARCHAR(32) NOT NULL COMMENT '医生ID',
    `score` INT NOT NULL COMMENT '评分(1-5)',
    `comment` TEXT COMMENT '评价内容',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_appointment` (`appointment_id`),
    KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 咨询消息表
CREATE TABLE `consult_message` (
    `id` VARCHAR(32) NOT NULL COMMENT '消息ID',
    `appointment_id` VARCHAR(32) NOT NULL COMMENT '预约ID',
    `sender_id` VARCHAR(32) NOT NULL COMMENT '发送者ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_appointment_id` (`appointment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询消息表'; 