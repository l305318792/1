-- 用户表索引
CREATE INDEX idx_user_username ON user(username);
CREATE INDEX idx_user_phone ON user(phone);
CREATE INDEX idx_user_status ON user(status);

-- 医生表索引
CREATE INDEX idx_doctor_department ON doctor(department_id);
CREATE INDEX idx_doctor_rating ON doctor(rating);
CREATE INDEX idx_doctor_status ON doctor(status);

-- 预约表索引
CREATE INDEX idx_appointment_user ON appointment(user_id);
CREATE INDEX idx_appointment_doctor ON appointment(doctor_id);
CREATE INDEX idx_appointment_status ON appointment(status);
CREATE INDEX idx_appointment_time ON appointment(appointment_time);

-- 咨询表索引
CREATE INDEX idx_consult_user ON consult(user_id);
CREATE INDEX idx_consult_doctor ON consult(doctor_id);
CREATE INDEX idx_consult_status ON consult(status);

-- 病例表索引
CREATE INDEX idx_record_patient ON medical_record(patient_id);
CREATE INDEX idx_record_doctor ON medical_record(doctor_id);
CREATE INDEX idx_record_status ON medical_record(status);

-- 内容表索引
CREATE INDEX idx_news_category ON news(category);
CREATE INDEX idx_news_status ON news(status);
CREATE INDEX idx_content_type ON content(type);
CREATE INDEX idx_content_status ON content(status); 