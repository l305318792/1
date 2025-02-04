-- 添加科室数据
INSERT INTO department (id, name, introduction, status, create_time, update_time)
VALUES 
('d1', '内科', '主要处理内脏疾病', 1, NOW(), NOW()),
('d2', '外科', '主要处理外伤、骨科等疾病', 1, NOW(), NOW()),
('d3', '儿科', '专门处理儿童疾病', 1, NOW(), NOW()),
('d4', '妇科', '专门处理妇科疾病', 1, NOW(), NOW()),
('d5', '骨科', '主要处理骨骼、关节等疾病', 1, NOW(), NOW());

-- 添加医生与科室的关联
INSERT INTO doctor (id, user_id, name, phone, department_id, title, specialty, introduction, consultation_fee, rating, rating_count, status, consult_count, appointment_count, create_time, update_time)
VALUES 
('doc1', 'u2', '张医', '13800000001', 'd1', '主任医师', '内科疾病', '从事内科临床工作20年', 100.00, 5.0, 0, 1, 0, 0, NOW(), NOW()),
('doc2', 'u3', '李医', '13800000002', 'd2', '副主任医师', '普外科手术', '从事外科临床工作15年', 150.00, 5.0, 0, 1, 0, 0, NOW(), NOW()),
('doc3', 'u4', '王医', '13800000003', 'd3', '主治医师', '儿科常见病', '从事儿科临床工作10年', 80.00, 5.0, 0, 1, 0, 0, NOW(), NOW()); 