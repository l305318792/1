-- 添加备注字段到预约表
ALTER TABLE appointment
ADD COLUMN remark VARCHAR(500) COMMENT '备注' AFTER status; 