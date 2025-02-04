-- 预约表
CREATE TABLE appointment (
    id VARCHAR(32) NOT NULL COMMENT '预约ID',
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    doctor_id VARCHAR(32) NOT NULL COMMENT '医生ID',
    doctor_name VARCHAR(50) NOT NULL COMMENT '医生姓名',
    department_id VARCHAR(32) NOT NULL COMMENT '科室ID',
    department_name VARCHAR(50) NOT NULL COMMENT '科室名称',
    schedule_id VARCHAR(32) NOT NULL COMMENT '排班ID',
    appointment_time DATETIME NOT NULL COMMENT '预约时间',
    status VARCHAR(20) NOT NULL COMMENT '状态：UNPAID-待支付，PAID-已支付，COMPLETED-已完成，CANCELLED-已取消',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 创建支付表
CREATE TABLE IF NOT EXISTS payment (
    id VARCHAR(32) NOT NULL COMMENT 'ID',
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    user_name VARCHAR(50) NOT NULL COMMENT '用户姓名',
    order_type VARCHAR(20) NOT NULL COMMENT '订单类型（APPOINTMENT-预约挂号，PRESCRIPTION-处方药品）',
    order_id VARCHAR(32) NOT NULL COMMENT '订单ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status VARCHAR(20) NOT NULL COMMENT '支付状态（PENDING-待支付，SUCCESS-支付成功，FAILED-支付失败）',
    payment_method VARCHAR(20) NOT NULL COMMENT '支付方式（WECHAT-微信，ALIPAY-支付宝）',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    pay_time DATETIME COMMENT '支付时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付表'; 