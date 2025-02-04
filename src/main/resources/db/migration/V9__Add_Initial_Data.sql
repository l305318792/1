-- 添加医生账号
INSERT INTO user (id, username, password, name, role, status, create_time, update_time)
VALUES 
('2', 'doctor1', '$2a$10$n9S0AehL7KHWeKPw.h8INOmFtBWY1q8GS3G8PRnkA7tGGfUHWkR7y', '张医', 'DOCTOR', 'ACTIVE', NOW(), NOW()),
('3', 'doctor2', '$2a$10$n9S0AehL7KHWeKPw.h8INOmFtBWY1q8GS3G8PRnkA7tGGfUHWkR7y', '李医', 'DOCTOR', 'ACTIVE', NOW(), NOW()),
('4', 'doctor3', '$2a$10$n9S0AehL7KHWeKPw.h8INOmFtBWY1q8GS3G8PRnkA7tGGfUHWkR7y', '王医', 'DOCTOR', 'ACTIVE', NOW(), NOW());

-- 添加患者账号
INSERT INTO user (id, username, password, name, role, status, create_time, update_time)
VALUES 
('5', 'patient1', '$2a$10$n9S0AehL7KHWeKPw.h8INOmFtBWY1q8GS3G8PRnkA7tGGfUHWkR7y', '张三', 'USER', 'ACTIVE', NOW(), NOW()),
('6', 'patient2', '$2a$10$n9S0AehL7KHWeKPw.h8INOmFtBWY1q8GS3G8PRnkA7tGGfUHWkR7y', '李四', 'USER', 'ACTIVE', NOW(), NOW());

-- 添加科室数据
INSERT INTO department (id, name, introduction, status, create_time, update_time)
VALUES 
('1', '内科', '主要处理内脏疾病', 'ENABLE', NOW(), NOW()),
('2', '外科', '主要处理外伤、骨科等疾病', 'ENABLE', NOW(), NOW()),
('3', '儿科', '专门处理儿童疾病', 'ENABLE', NOW(), NOW()),
('4', '妇科', '专门处理妇科疾病', 'ENABLE', NOW(), NOW()),
('5', '骨科', '主要处理骨骼、关节等疾病', 'ENABLE', NOW(), NOW());

-- 添加医生与科室的关联
INSERT INTO doctor (id, user_id, department_id, title, specialty, introduction, rating, consultation_count, consultation_fee, available_status, create_time, update_time)
VALUES 
('1', '2', '1', '主任医师', '内科疾病', '从事内科临床工作20年', 5.0, 0, 100.00, 'AVAILABLE', NOW(), NOW()),
('2', '3', '2', '副主任医师', '普外科手术', '从事外科临床工作15年', 5.0, 0, 150.00, 'AVAILABLE', NOW(), NOW()),
('3', '4', '3', '主治医师', '儿科常见病', '从事儿科临床工作10年', 5.0, 0, 80.00, 'AVAILABLE', NOW(), NOW()); 