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