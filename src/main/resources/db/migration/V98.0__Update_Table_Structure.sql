-- 更新用户表
ALTER TABLE user
ADD COLUMN email VARCHAR(100),
ADD COLUMN avatar VARCHAR(255),
ADD COLUMN gender VARCHAR(10),
ADD COLUMN birth_date DATE,
ADD COLUMN address TEXT;

-- 更新医生表
ALTER TABLE doctor
ADD COLUMN rating DECIMAL(2,1) DEFAULT 5.0,
ADD COLUMN rating_count INT DEFAULT 0,
ADD COLUMN consult_count INT DEFAULT 0,
ADD COLUMN appointment_count INT DEFAULT 0;

-- 更新预约表
ALTER TABLE appointment
ADD COLUMN appointment_time DATETIME,
ADD COLUMN symptom TEXT,
ADD COLUMN remark TEXT;

-- 更新咨询表
ALTER TABLE consult
ADD COLUMN title VARCHAR(100),
ADD COLUMN description TEXT,
ADD COLUMN fee DECIMAL(10,2),
ADD COLUMN end_time DATETIME;

-- 更新病例表
ALTER TABLE medical_record
ADD COLUMN visit_time DATETIME,
ADD COLUMN chief_complaint TEXT,
ADD COLUMN present_illness TEXT,
ADD COLUMN past_history TEXT,
ADD COLUMN physical_exam TEXT,
ADD COLUMN remark TEXT;

-- 更新处方表
ALTER TABLE prescription
ADD COLUMN dosage VARCHAR(100),
ADD COLUMN frequency VARCHAR(100),
ADD COLUMN duration VARCHAR(100),
ADD COLUMN precautions TEXT,
ADD COLUMN remark TEXT; 