-- 咨询相关表
CREATE TABLE IF NOT EXISTS consult (
    id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    doctor_id VARCHAR(32) NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

CREATE TABLE IF NOT EXISTS consult_message (
    id VARCHAR(32) PRIMARY KEY,
    consult_id VARCHAR(32) NOT NULL,
    sender_id VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (consult_id) REFERENCES consult(id),
    FOREIGN KEY (sender_id) REFERENCES user(id)
);

-- 病例相关表
CREATE TABLE IF NOT EXISTS medical_record (
    id VARCHAR(32) PRIMARY KEY,
    patient_id VARCHAR(32) NOT NULL,
    doctor_id VARCHAR(32) NOT NULL,
    diagnosis TEXT NOT NULL,
    treatment TEXT,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES user(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

CREATE TABLE IF NOT EXISTS prescription (
    id VARCHAR(32) PRIMARY KEY,
    record_id VARCHAR(32) NOT NULL,
    medicine TEXT NOT NULL,
    usage_method TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (record_id) REFERENCES medical_record(id)
);

-- 内容相关表
CREATE TABLE IF NOT EXISTS news (
    id VARCHAR(32) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS content (
    id VARCHAR(32) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 财务相关表
CREATE TABLE IF NOT EXISTS finance (
    id VARCHAR(32) PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    remark TEXT,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
); 