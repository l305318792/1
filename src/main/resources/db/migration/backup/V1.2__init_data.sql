-- 禁用外键检查
SET FOREIGN_KEY_CHECKS=0;

-- 插入科室数据
INSERT INTO `department` (`id`, `name`, `introduction`, `status`)
VALUES 
('1', '内科', '内科是临床医学的一个专业分支，主要研究人体内脏器官疾病', 1),
('2', '外科', '外科是临床医学的一个专业分支，主要研究需要手术治疗的疾病', 1),
('3', '儿科', '儿科是临床医学的一个专业分支，主要研究儿童疾病', 1),
('4', '妇产科', '妇产科是临床医学的一个专业分支，主要研究妇女疾病和孕产相关疾病', 1);

-- 插入管理员用户 (密码: 123456)
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `phone`, `email`, `status`)
VALUES ('1', 'admin', '$2a$10$VwppB9m3OeA1AUjMQqyU0u4qicb3rnyfKZCbxyTcjFdwCuF.4Y8vG', '系统管理员', 'ADMIN', '13800000000', 'admin@example.com', 1);

-- 插入测试医生用户 (密码: 123456)
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `phone`, `email`, `status`)
VALUES 
('2', 'doctor1', '$2a$10$N.ZOn9G6/YLxy0c/g6lB.evGXS1XxqBqy1gRXQwgUk.ZLKCf9NOqu', '张医生', 'DOCTOR', '13800000001', 'doctor1@example.com', 1),
('3', 'doctor2', '$2a$10$N.ZOn9G6/YLxy0c/g6lB.evGXS1XxqBqy1gRXQwgUk.ZLKCf9NOqu', '李医生', 'DOCTOR', '13800000002', 'doctor2@example.com', 1);

-- 插入测试患者用户 (密码: 123456)
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `phone`, `email`, `status`)
VALUES 
('4', 'patient1', '$2a$10$N.ZOn9G6/YLxy0c/g6lB.evGXS1XxqBqy1gRXQwgUk.ZLKCf9NOqu', '王患者', 'PATIENT', '13800000003', 'patient1@example.com', 1),
('5', 'patient2', '$2a$10$N.ZOn9G6/YLxy0c/g6lB.evGXS1XxqBqy1gRXQwgUk.ZLKCf9NOqu', '赵患者', 'PATIENT', '13800000004', 'patient2@example.com', 1);

-- 插入医生信息
INSERT INTO `doctor` (`id`, `user_id`, `department_id`, `title`, `specialty`, `introduction`, `consultation_fee`, `status`)
VALUES 
('1', '2', '1', '主任医师', '心血管疾病', '从事心血管疾病临床工作20年，具有丰富的临床经验。', 100.00, 1),
('2', '3', '2', '副主任医师', '普外科手术', '从事普外科临床工作15年，擅长各类手术。', 80.00, 1);

-- 插入排班信息
INSERT INTO `schedule` (`id`, `doctor_id`, `date`, `period`, `max_patients`, `status`)
VALUES 
('1', '1', CURDATE(), 'MORNING', 10, 1),
('2', '1', CURDATE(), 'AFTERNOON', 10, 1),
('3', '2', CURDATE(), 'MORNING', 8, 1),
('4', '2', CURDATE(), 'AFTERNOON', 8, 1);

-- 启用外键检查
SET FOREIGN_KEY_CHECKS=1; 