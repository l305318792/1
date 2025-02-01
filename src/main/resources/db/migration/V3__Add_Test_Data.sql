-- 插入测试新闻
INSERT INTO news (id, title, content, category, status, create_time, update_time) VALUES
('n1', '医院新增专家门诊', '我院新增多个专家门诊，方便患者就医。', '医院新闻', 'ENABLE', NOW(), NOW()),
('n2', '冬季养生指南', '冬季养生要点：保暖、适量运动、合理饮食。', '健康知识', 'ENABLE', NOW(), NOW());

-- 插入测试内容
INSERT INTO content (id, title, content, type, status, create_time, update_time) VALUES
('c1', '就医指南', '预约挂号流程说明...', '指南', 'ENABLE', NOW(), NOW()),
('c2', '医院介绍', '医院地址、科室分布...', '介绍', 'ENABLE', NOW(), NOW());

-- 插入测试咨询记录
INSERT INTO consult (id, user_id, doctor_id, status, create_time, update_time) VALUES
('cs1', 'u1', 'd1', 'PROCESSING', NOW(), NOW());

-- 插入测试咨询消息
INSERT INTO consult_message (id, consult_id, sender_id, content, type, create_time) VALUES
('cm1', 'cs1', 'u1', '医生您好，我想咨询一下...', 'TEXT', NOW());

-- 插入测试病例
INSERT INTO medical_record (id, patient_id, doctor_id, diagnosis, treatment, status, create_time, update_time) VALUES
('mr1', 'u1', 'd1', '感冒', '建议多休息，多喝水', 'COMPLETED', NOW(), NOW());

-- 插入测试处方
INSERT INTO prescription (id, record_id, medicine, usage_method, status, create_time, update_time) VALUES
('p1', 'mr1', '感冒药、维生素C', '每日三次，饭后服用', 'ENABLE', NOW(), NOW());

-- 插入测试财务记录
INSERT INTO finance (id, user_id, amount, type, status, remark, create_time, update_time) VALUES
('f1', 'u1', 100.00, 'PAYMENT', 'COMPLETED', '挂号费', NOW(), NOW()); 