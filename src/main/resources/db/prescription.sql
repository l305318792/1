SET NAMES utf8mb4;

DROP TABLE IF EXISTS `prescription`;

CREATE TABLE `prescription` (
    `id` varchar(32) NOT NULL COMMENT '主键ID',
    `consultation_id` varchar(32) DEFAULT NULL COMMENT '问诊ID',
    `doctor_id` varchar(32) DEFAULT NULL COMMENT '医生ID',
    `patient_id` varchar(32) DEFAULT NULL COMMENT '患者ID',
    `diagnosis` text DEFAULT NULL COMMENT '诊断结果',
    `medications` text DEFAULT NULL COMMENT '药品信息',
    `dosage` text DEFAULT NULL COMMENT '用药剂量',
    `instructions` text DEFAULT NULL COMMENT '用药说明',
    `status` varchar(20) DEFAULT NULL COMMENT '状态：PENDING-待处理，PROCESSING-处理中，COMPLETED-已完成',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_consultation_id` (`consultation_id`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_patient_id` (`patient_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='处方表';