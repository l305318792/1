-- 在医生表中添加评价相关字段
ALTER TABLE doctor
ADD COLUMN average_rating DECIMAL(2,1) DEFAULT 0.0,
ADD COLUMN rating_count INT DEFAULT 0;

-- 更新现有医生的评价数据
UPDATE doctor d
SET average_rating = (
    SELECT COALESCE(AVG(rating), 0)
    FROM doctor_rating r
    WHERE r.doctor_id = d.id
    AND r.status != 'HIDDEN'
),
rating_count = (
    SELECT COUNT(*)
    FROM doctor_rating r
    WHERE r.doctor_id = d.id
    AND r.status != 'HIDDEN'
); 