-- 添加备注字段到支付记录表
ALTER TABLE payment_record
ADD COLUMN remark TEXT COMMENT '备注' AFTER status; 