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