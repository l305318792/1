-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` VARCHAR(32) NOT NULL COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `name` VARCHAR(50) COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `avatar` VARCHAR(200) COMMENT '头像',
    `gender` CHAR(1) COMMENT '性别',
    `birth_date` DATE COMMENT '出生日期',
    `address` VARCHAR(200) COMMENT '地址',
    `role` VARCHAR(20) NOT NULL COMMENT '角色',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入管理员用户（密码：123456）
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `status`)
VALUES ('1', 'admin', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C', '管理员', 'ADMIN', 1);

-- 插入医生用户
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `status`)
VALUES 
('2', 'doctor1', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C', '张医生', 'DOCTOR', 1),
('3', 'doctor2', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C', '李医生', 'DOCTOR', 1);

-- 插入普通用户
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `status`)
VALUES 
('4', 'user1', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C', '张三', 'USER', 1),
('5', 'user2', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C', '李四', 'USER', 1); 