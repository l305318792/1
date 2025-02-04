USE medical;

-- 设置连接字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

-- 删除现有数据（按照依赖关系顺序）
SET FOREIGN_KEY_CHECKS=0;

DELETE FROM prescription;
DELETE FROM consult_message;
DELETE FROM doctor_rating;
DELETE FROM appointment;
DELETE FROM payment_record;
DELETE FROM finance;
DELETE FROM medical_record;
DELETE FROM consult;
DELETE FROM news_tag;
DELETE FROM schedule;
DELETE FROM content;
DELETE FROM news;
DELETE FROM doctor;
DELETE FROM system_config;
DELETE FROM health_tag;
DELETE FROM health_category;
DELETE FROM department;
DELETE FROM user;

SET FOREIGN_KEY_CHECKS=1;

-- ====================== 插入基础数据 ======================

-- 插入管理员用户
INSERT INTO user (id, username, password, name, phone, email, role, status, create_time, update_time) VALUES
('u1', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '管理', '13800000000', 'admin@medical.com', 'ADMIN', 'ENABLED', NOW(), NOW()),
('u2', 'doctor1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '张医', '13800000001', 'doctor1@medical.com', 'DOCTOR', 'ENABLED', NOW(), NOW()),
('u3', 'doctor2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '李医', '13800000002', 'doctor2@medical.com', 'DOCTOR', 'ENABLED', NOW(), NOW()),
('u4', 'patient1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '王患', '13800000003', 'patient1@medical.com', 'PATIENT', 'ENABLED', NOW(), NOW());

-- 插入科室数据
INSERT INTO department (id, name, parent_id, introduction, status, create_time, update_time) VALUES
('d1', '内科', null, '内科是临床医学的一个专业分支，主要研究人体内脏器官疾病', 'ENABLE', NOW(), NOW()),
('d2', '外科', null, '外科是临床医学的一个专业分支，主要研究需要手术治疗的疾病', 'ENABLE', NOW(), NOW()),
('d3', '儿科', null, '儿科是临床医学的一个专业分支，主要研究儿童疾病', 'ENABLE', NOW(), NOW()),
('d4', '妇产科', null, '妇产科是临床医学的一个专业分支，主要研究妇女疾病和孕产相关疾病', 'ENABLE', NOW(), NOW());

-- 插入医生数据
INSERT INTO doctor (id, user_id, department_id, title, specialty, introduction, consultation_fee, create_time, update_time) VALUES
('doc1', 'u2', 'd1', '主任医师', '心血管疾病', '从事心血管疾病临床工作20年，具有丰富的诊疗经验', 100.00, NOW(), NOW()),
('doc2', 'u3', 'd2', '副主任医师', '普外科手术', '从事普外科临床工作15年，擅长各类手术治疗', 80.00, NOW(), NOW());

-- ====================== 插入内容数据 ======================

-- 插入健康资讯分类
INSERT INTO health_category (id, name, sort_order, status, create_time, update_time) VALUES
('hc1', '健康知识', 1, 'ENABLE', NOW(), NOW()),
('hc2', '疾病预防', 2, 'ENABLE', NOW(), NOW()),
('hc3', '饮食营养', 3, 'ENABLE', NOW(), NOW()),
('hc4', '心理健康', 4, 'ENABLE', NOW(), NOW()),
('hc5', '医疗动态', 5, 'ENABLE', NOW(), NOW());

-- 插入健康标签
INSERT INTO health_tag (id, name, status, create_time, update_time) VALUES
('ht1', '常见病', 'ENABLE', NOW(), NOW()),
('ht2', '慢性病', 'ENABLE', NOW(), NOW()),
('ht3', '养生保健', 'ENABLE', NOW(), NOW()),
('ht4', '心理咨询', 'ENABLE', NOW(), NOW()),
('ht5', '医疗政策', 'ENABLE', NOW(), NOW());

-- 插入资讯
INSERT INTO news (id, title, content, category_id, status, create_time, update_time) VALUES
('n1', '高血压的日常预防', '高血压是常见的慢性病，日常预防很重要...', 'hc2', 'PUBLISHED', NOW(), NOW()),
('n2', '合理膳食的重要性', '合理的膳食搭配对健康的重要性...', 'hc3', 'PUBLISHED', NOW(), NOW());

-- 插入资讯标签关联
INSERT INTO news_tag (news_id, tag_id) VALUES
('n1', 'ht2'),
('n2', 'ht3');

-- ====================== 插入系统配置 ======================

-- 插入系统配置
INSERT INTO system_config (id, config_key, config_value, description, create_time, update_time) VALUES
('sc1', 'SITE_NAME', '运城市移动医疗咨询平台', '站点名称', NOW(), NOW()),
('sc2', 'SITE_DESCRIPTION', '提供在线问诊、预约挂号、健康咨询等服务', '站点描述', NOW(), NOW()),
('sc3', 'CONSULT_FEE', '50', '默认咨询费用', NOW(), NOW()),
('sc4', 'APPOINTMENT_TIME_SLOT', '30', '预约时间间隔(分钟)', NOW(), NOW()),
('sc5', 'MAX_APPOINTMENTS_PER_DAY', '50', '每日最大预约数', NOW(), NOW()),
('sc6', 'WORKING_HOURS', '8:00-18:00', '工作时间', NOW(), NOW()),
('sc7', 'CONTACT_PHONE', '0359-12345678', '联系电话', NOW(), NOW()),
('sc8', 'CONTACT_EMAIL', 'support@medical.com', '联系邮箱', NOW(), NOW()); 