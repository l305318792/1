-- 添加预约金额字段
ALTER TABLE appointment
ADD COLUMN amount DECIMAL(10,2) COMMENT '预约金额' AFTER visit_time;

-- 添加支付记录ID字段
ALTER TABLE appointment
ADD COLUMN payment_id VARCHAR(32) COMMENT '支付记录ID' AFTER amount; 