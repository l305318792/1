-- 创建退款记录表
CREATE TABLE refund_record (
    id VARCHAR(64) PRIMARY KEY COMMENT '退款记录ID',
    payment_id VARCHAR(64) NOT NULL COMMENT '支付记录ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    business_type VARCHAR(20) NOT NULL COMMENT '业务类型',
    business_id VARCHAR(64) NOT NULL COMMENT '业务ID',
    reason VARCHAR(500) COMMENT '退款原因',
    status VARCHAR(20) NOT NULL COMMENT '状态(PENDING-待处理,APPROVED-已同意,REJECTED-已拒绝)',
    reject_reason VARCHAR(500) COMMENT '拒绝原因',
    operator_id VARCHAR(64) COMMENT '处理人ID',
    refund_time DATETIME COMMENT '退款时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (payment_id) REFERENCES payment_record(id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (operator_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款记录表'; 