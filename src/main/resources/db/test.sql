USE medical;

-- 1. 测试用户相关
-- 检查管理员用户
SELECT * FROM user WHERE username = 'admin';
-- 检查医生用户
SELECT * FROM user WHERE role = 'DOCTOR';
-- 检查患者用户
SELECT * FROM user WHERE role = 'PATIENT';

-- 2. 测试科室相关
-- 检查所有科室
SELECT * FROM department;
-- 检查科室状态
SELECT status, COUNT(*) FROM department GROUP BY status;

-- 3. 测试医生相关
-- 检查医生信息（关联用户和科室）
SELECT d.*, u.name as doctor_name, dep.name as department_name 
FROM doctor d 
JOIN user u ON d.user_id = u.id 
JOIN department dep ON d.department_id = dep.id;

-- 4. 测试外键约束
-- 测试医生-用户外键
INSERT INTO doctor (id, user_id, department_id, create_time, update_time) 
VALUES ('test_doc', 'non_exist_user', 'd1', NOW(), NOW());

-- 测试医生-科室外键
INSERT INTO doctor (id, user_id, department_id, create_time, update_time) 
VALUES ('test_doc', 'u1', 'non_exist_dept', NOW(), NOW());

-- 5. 测试唯一约束
-- 测试用户名唯一约束
INSERT INTO user (id, username, password, name, role, status, create_time, update_time) 
VALUES ('test_user', 'admin', 'test', 'Test User', 'PATIENT', 'ENABLED', NOW(), NOW());

-- 6. 测试健康资讯相关
-- 检查资讯分类
SELECT * FROM health_category ORDER BY sort_order;
-- 检查资讯标签
SELECT * FROM health_tag;
-- 检查资讯及其分类和标签
SELECT n.*, c.name as category_name, GROUP_CONCAT(t.name) as tags
FROM news n
JOIN health_category c ON n.category_id = c.id
LEFT JOIN news_tag nt ON n.id = nt.news_id
LEFT JOIN health_tag t ON nt.tag_id = t.id
GROUP BY n.id;

-- 7. 测试系统配置
-- 检查所有系统配置
SELECT * FROM system_config ORDER BY config_key;

-- 8. 测试索引（通过EXPLAIN分析查询计划）
EXPLAIN SELECT * FROM user WHERE username = 'admin';
EXPLAIN SELECT * FROM doctor WHERE department_id = 'd1';
EXPLAIN SELECT * FROM appointment WHERE user_id = 'u1' AND status = 'PENDING';

-- 9. 测试级联删除（注意：仅测试，不要在生产环境中执行）
-- START TRANSACTION;
-- DELETE FROM user WHERE id = 'u2';
-- SELECT * FROM doctor WHERE user_id = 'u2';
-- ROLLBACK;

-- 10. 测试数据完整性
-- 检查是否有孤立的医生记录
SELECT * FROM doctor d LEFT JOIN user u ON d.user_id = u.id WHERE u.id IS NULL;
-- 检查是否有孤立的预约记录
SELECT * FROM appointment a LEFT JOIN user u ON a.user_id = u.id WHERE u.id IS NULL;
-- 检查是否有孤立的咨询记录
SELECT * FROM consult c LEFT JOIN user u ON c.user_id = u.id WHERE u.id IS NULL; 