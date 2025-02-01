-- 健康资讯相关表
CREATE TABLE health_category (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sort_order INT DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE health_tag (
    id VARCHAR(32) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE news_tag (
    news_id VARCHAR(32) NOT NULL,
    tag_id VARCHAR(32) NOT NULL,
    PRIMARY KEY (news_id, tag_id),
    FOREIGN KEY (news_id) REFERENCES news(id),
    FOREIGN KEY (tag_id) REFERENCES health_tag(id)
);

-- 更新医生评价表结构
ALTER TABLE doctor_rating
ADD COLUMN service_attitude INT DEFAULT 5,
ADD COLUMN medical_skill INT DEFAULT 5,
ADD COLUMN medical_effect INT DEFAULT 5;

-- 支付记录表
CREATE TABLE payment_record (
    id VARCHAR(32) PRIMARY KEY,
    order_id VARCHAR(32) NOT NULL,
    user_id VARCHAR(32) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_type VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(100),
    remark TEXT,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 系统配置表
CREATE TABLE system_config (
    id VARCHAR(32) PRIMARY KEY,
    config_key VARCHAR(50) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    description VARCHAR(200),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 添加索引
CREATE INDEX idx_category_status ON health_category(status);
CREATE INDEX idx_tag_status ON health_tag(status);
CREATE INDEX idx_payment_user ON payment_record(user_id);
CREATE INDEX idx_payment_status ON payment_record(payment_status);
CREATE INDEX idx_payment_time ON payment_record(create_time);
CREATE INDEX idx_config_key ON system_config(config_key); 