-- 禁用外键检查
SET FOREIGN_KEY_CHECKS=0;

-- 添加医生表的外键
ALTER TABLE `doctor`
    ADD CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `doctor_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);

-- 添加排班表的外键
ALTER TABLE `schedule`
    ADD CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`);

-- 添加预约表的外键
ALTER TABLE `appointment`
    ADD CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
    ADD CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`);

-- 添加评价表的外键
ALTER TABLE `rating`
    ADD CONSTRAINT `rating_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
    ADD CONSTRAINT `rating_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`),
    ADD CONSTRAINT `rating_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`);

-- 添加咨询消息表的外键
ALTER TABLE `consult_message`
    ADD CONSTRAINT `consult_message_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
    ADD CONSTRAINT `consult_message_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`);

-- 启用外键检查
SET FOREIGN_KEY_CHECKS=1; 