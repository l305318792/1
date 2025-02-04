-- 添加排班数据
INSERT INTO schedule (id, doctor_id, department_id, schedule_date, period, max_appointments, appointed_count, status, remark, create_time, update_time)
VALUES 
('schedule001', '4', '1', '2025-02-06', 'MORNING', 10, 0, 1, '上午门诊', NOW(), NOW()),
('schedule002', '4', '1', '2025-02-06', 'AFTERNOON', 10, 0, 1, '下午门诊', NOW(), NOW()),
('schedule003', '4', '1', '2025-02-07', 'MORNING', 10, 0, 1, '上午门诊', NOW(), NOW()),
('schedule004', '4', '1', '2025-02-07', 'AFTERNOON', 10, 0, 1, '下午门诊', NOW(), NOW());