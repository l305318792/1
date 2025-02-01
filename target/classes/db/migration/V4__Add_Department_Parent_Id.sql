-- 设置字符集
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;

-- 为department表添加parent_id字段
ALTER TABLE department 
ADD COLUMN parent_id VARCHAR(64) COMMENT '父级科室ID' AFTER name;

-- 更新现有数据，设置parent_id为NULL（表示顶级科室）
UPDATE department SET parent_id = NULL WHERE parent_id IS NULL;

-- 添加一些子科室示例数据
INSERT INTO department (id, name, parent_id, introduction, status, create_time, update_time)
VALUES 
-- 内科的子科室
('1001', '消化内科', '1', '主要处理消化系统疾病', 1, NOW(), NOW()),
('1002', '心内科', '1', '主要处理心脏相关疾病', 1, NOW(), NOW()),
('1003', '呼吸内科', '1', '主要处理呼吸系统疾病', 1, NOW(), NOW()),

-- 外科的子科室
('2001', '普通外科', '2', '处理一般外科疾病', 1, NOW(), NOW()),
('2002', '神经外科', '2', '处理神经系统疾病', 1, NOW(), NOW()),
('2003', '心胸外科', '2', '处理心脏和胸腔疾病', 1, NOW(), NOW()); 