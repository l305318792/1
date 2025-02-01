-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 科室表
CREATE TABLE IF NOT EXISTS department (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 医生表
CREATE TABLE IF NOT EXISTS doctor (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    department_id VARCHAR(36) NOT NULL,
    title VARCHAR(50),
    specialty TEXT,
    introduction TEXT,
    status VARCHAR(20) NOT NULL,
    average_rating DECIMAL(2,1),
    rating_count INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- 排班表
CREATE TABLE IF NOT EXISTS schedule (
    id VARCHAR(36) PRIMARY KEY,
    doctor_id VARCHAR(36) NOT NULL,
    department_id VARCHAR(36) NOT NULL,
    schedule_date DATE NOT NULL,
    period VARCHAR(20) NOT NULL,
    max_appointments INT NOT NULL,
    appointed_count INT DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    remark TEXT,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- 预约表
CREATE TABLE IF NOT EXISTS appointment (
    id VARCHAR(36) PRIMARY KEY,
    schedule_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    doctor_id VARCHAR(36) NOT NULL,
    department_id VARCHAR(36) NOT NULL,
    sequence_number INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    cancel_reason TEXT,
    visit_time DATETIME,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (schedule_id) REFERENCES schedule(id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
);

-- 医生评价表
CREATE TABLE IF NOT EXISTS doctor_rating (
    id VARCHAR(36) PRIMARY KEY,
    appointment_id VARCHAR(36) NOT NULL,
    doctor_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    rating INT NOT NULL,
    content TEXT,
    reply TEXT,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointment(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 处方表
CREATE TABLE IF NOT EXISTS prescription (
    id VARCHAR(36) PRIMARY KEY,
    consultation_id VARCHAR(36) NOT NULL,
    doctor_id VARCHAR(36) NOT NULL,
    patient_id VARCHAR(36) NOT NULL,
    diagnosis TEXT,
    medications TEXT,
    dosage TEXT,
    instructions TEXT,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
); 