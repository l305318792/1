-- MySQL dump 10.13  Distrib 9.2.0, for Win64 (x86_64)
--
-- Host: localhost    Database: medical
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `consult`
--

DROP TABLE IF EXISTS `consult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consult` (
  `id` varchar(32) NOT NULL COMMENT '咨询ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `doctor_id` varchar(32) NOT NULL COMMENT '医生ID',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `title` varchar(100) NOT NULL COMMENT '咨询标题',
  `description` text COMMENT '咨询描述',
  `fee` decimal(10,2) NOT NULL COMMENT '咨询费用',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  KEY `idx_consult_user` (`user_id`),
  KEY `idx_consult_doctor` (`doctor_id`),
  KEY `idx_consult_status` (`status`),
  CONSTRAINT `fk_consult_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `fk_consult_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='咨询表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consult`
--

LOCK TABLES `consult` WRITE;
/*!40000 ALTER TABLE `consult` DISABLE KEYS */;
/*!40000 ALTER TABLE `consult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consult_message`
--

DROP TABLE IF EXISTS `consult_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consult_message` (
  `id` varchar(32) NOT NULL COMMENT '消息ID',
  `appointment_id` varchar(32) NOT NULL COMMENT '预约ID',
  `sender_id` varchar(32) NOT NULL COMMENT '发送者ID',
  `content` text NOT NULL COMMENT '消息内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_appointment_id` (`appointment_id`),
  KEY `consult_message_ibfk_2` (`sender_id`),
  CONSTRAINT `consult_message_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
  CONSTRAINT `consult_message_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='咨询消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consult_message`
--

LOCK TABLES `consult_message` WRITE;
/*!40000 ALTER TABLE `consult_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `consult_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultation`
--

DROP TABLE IF EXISTS `consultation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultation` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `doctor_id` varchar(64) NOT NULL,
  `department_id` varchar(64) NOT NULL,
  `appointment_id` varchar(64) DEFAULT NULL,
  `symptoms` text,
  `diagnosis` text,
  `treatment` text,
  `status` varchar(20) NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_consultation_user` (`user_id`),
  KEY `idx_consultation_doctor` (`doctor_id`),
  KEY `idx_consultation_department` (`department_id`),
  KEY `idx_consultation_appointment` (`appointment_id`),
  KEY `idx_consultation_status` (`status`),
  KEY `idx_consultation_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultation`
--

LOCK TABLES `consultation` WRITE;
/*!40000 ALTER TABLE `consultation` DISABLE KEYS */;
INSERT INTO `consultation` VALUES ('1','c3d4e5f6','b2c3d4e5','1001',NULL,'胃部不适，恶心想吐','急性胃炎','1. 建议清淡饮食，少食多餐\n2. 避免刺激性食物\n3. 规律作息，保持充足睡眠','COMPLETED','2025-01-31 23:42:07',NULL,'2025-01-31 23:40:55','2025-02-01 00:17:04');
/*!40000 ALTER TABLE `consultation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `content` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_content_type` (`type`),
  KEY `idx_content_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='内容表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
/*!40000 ALTER TABLE `content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` varchar(32) NOT NULL COMMENT '科室ID',
  `name` varchar(50) NOT NULL COMMENT '科室名称',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '鐖剁骇绉戝?ID',
  `introduction` text COMMENT '科室介绍',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='科室表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES ('1','内科',NULL,'内科是临床医学的一个专业分支，主要研究人体内脏器官疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('1001','消化内科','1','主要处理消化系统疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('1002','心内科','1','主要处理心脏相关疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('1003','呼吸内科','1','主要处理呼吸系统疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('1885345271405649921','测试科室',NULL,'这是一个测试科室',1,'2025-01-31 23:11:55','2025-01-31 23:11:55'),('1885346146496847873','测试科室',NULL,'这是一个测试科室',1,'2025-01-31 23:15:24','2025-01-31 23:15:24'),('1885347133928284162','测试科室',NULL,'这是一个测试科室',1,'2025-01-31 23:19:19','2025-01-31 23:19:19'),('2','外科',NULL,'外科是临床医学的一个专业分支，主要研究需要手术治疗的疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('2001','普通外科','2','处理一般外科疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('2002','神经外科','2','处理神经系统疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('2003','心胸外科','2','处理心脏和胸腔疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('3','儿科',NULL,'儿科是临床医学的一个专业分支，主要研究儿童疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('3001','儿童内科','3','处理儿童内科疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('3002','儿童外科','3','处理儿童外科疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('3003','新生儿科','3','处理新生儿疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('4','妇产科',NULL,'妇产科是临床医学的一个专业分支，主要研究妇女疾病和孕产相关疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('4001','妇科','4','处理妇科疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('4002','产科','4','处理孕产相关疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('4003','计划生育科','4','提供计划生育服务',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('5','骨科',NULL,'骨科是临床医学的一个专业分支，主要研究骨骼、关节等疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('5001','脊柱外科','5','处理脊柱相关疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('5002','关节外科','5','处理关节相关疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23'),('5003','手足外科','5','处理手部和足部疾病',1,'2025-01-31 22:25:23','2025-01-31 22:25:23');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `id` varchar(32) NOT NULL COMMENT '医生ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `department_id` varchar(32) NOT NULL COMMENT '科室ID',
  `title` varchar(50) NOT NULL COMMENT '职称',
  `specialty` varchar(200) DEFAULT NULL COMMENT '专长',
  `introduction` text COMMENT '简介',
  `consultation_fee` decimal(10,2) NOT NULL COMMENT '咨询费用',
  `rating` decimal(2,1) DEFAULT '5.0' COMMENT '评分',
  `rating_count` int DEFAULT '0' COMMENT '评分次数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-停诊，1-接诊',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `consult_count` int NOT NULL DEFAULT '0' COMMENT '咨询次数',
  `appointment_count` int NOT NULL DEFAULT '0' COMMENT '预约次数',
  PRIMARY KEY (`id`),
  KEY `doctor_ibfk_1` (`user_id`),
  KEY `idx_doctor_department` (`department_id`),
  KEY `idx_doctor_rating` (`rating`),
  KEY `idx_doctor_status` (`status`),
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `doctor_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `fk_doctor_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `fk_doctor_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医生表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES ('1','2','张医生','13800000001','1','主任医师','内科疾病','从事内科临床工作20年',100.00,5.0,0,1,'2025-01-28 18:16:59','2025-01-28 18:16:59',0,0),('2','3','李医生','13800000002','2','副主任医师','普外科手术','从事外科临床工作15年',150.00,5.0,0,1,'2025-01-28 18:16:59','2025-01-28 18:16:59',0,0),('3','4','王医生','13800000003','3','主治医师','儿科常见病','从事儿科临床工作10年',80.00,5.0,0,1,'2025-01-28 18:16:59','2025-01-28 18:16:59',0,0),('4','2','张医生','13800000000','1','主任医师','擅长各种内科疾病的诊断和治疗','从医20年，具有丰富的临床经验',100.00,5.0,0,1,'2025-02-02 00:31:00','2025-02-02 00:31:00',0,0);
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_rating`
--

DROP TABLE IF EXISTS `doctor_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_rating` (
  `id` varchar(32) NOT NULL COMMENT '评价ID',
  `appointment_id` varchar(32) NOT NULL COMMENT '预约ID',
  `doctor_id` varchar(32) NOT NULL COMMENT '医生ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `rating` int NOT NULL DEFAULT '5' COMMENT '总体评分',
  `content` text NOT NULL COMMENT '评价内容',
  `reply` text COMMENT '医生回复',
  `status` varchar(20) NOT NULL DEFAULT 'PUBLISHED' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `service_attitude` int DEFAULT '5' COMMENT '服务态度评分',
  `medical_skill` int DEFAULT '5' COMMENT '医疗技术评分',
  `medical_effect` int DEFAULT '5' COMMENT '治疗效果评分',
  PRIMARY KEY (`id`),
  KEY `fk_doctor_rating_appointment` (`appointment_id`),
  KEY `fk_doctor_rating_doctor` (`doctor_id`),
  KEY `fk_doctor_rating_user` (`user_id`),
  CONSTRAINT `fk_doctor_rating_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
  CONSTRAINT `fk_doctor_rating_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `fk_doctor_rating_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医生评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_rating`
--

LOCK TABLES `doctor_rating` WRITE;
/*!40000 ALTER TABLE `doctor_rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finance`
--

DROP TABLE IF EXISTS `finance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finance` (
  `id` varchar(32) NOT NULL COMMENT '财务记录ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_finance_user` (`user_id`),
  CONSTRAINT `fk_finance_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='财务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finance`
--

LOCK TABLES `finance` WRITE;
/*!40000 ALTER TABLE `finance` DISABLE KEYS */;
/*!40000 ALTER TABLE `finance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `script` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1.0','create tables','SQL','V1.0__create_tables.sql',748078249,'root','2025-01-28 10:16:58',430,1),(2,'1.0.1','create article tables','SQL','V1.0.1__create_article_tables.sql',2089489079,'root','2025-01-28 10:16:58',459,1),(3,'1.0.2','add article cover image','SQL','V1.0.2__add_article_cover_image.sql',-1505220178,'root','2025-01-28 10:16:58',107,1),(4,'1.1','add foreign keys','SQL','V1.1__add_foreign_keys.sql',-1678334502,'root','2025-01-28 10:16:59',269,1),(5,'1.3','add content table','SQL','V1.3__add_content_table.sql',900092503,'root','2025-01-28 10:16:59',40,1),(6,'1.4','add test accounts','SQL','V1.4__add_test_accounts.sql',-1606572418,'root','2025-01-28 10:16:59',3,1),(7,'1.4.2','add name to doctor','SQL','V1_4_2__add_name_to_doctor.sql',1007378590,'root','2025-01-28 10:16:59',107,1),(8,'1.4.3','add phone to doctor','SQL','V1_4_3__add_phone_to_doctor.sql',-1241300185,'root','2025-01-28 10:16:59',103,1),(9,'2','Add Business Tables','SQL','V2__Add_Business_Tables.sql',-91962391,'root','2025-01-28 10:16:59',255,1),(10,'3.1','Add Department Doctor','SQL','V3_1__Add_Department_Doctor.sql',-1154961942,'root','2025-01-28 10:16:59',8,1),(11,'3.2','Fix User Names','SQL','V3_2__Fix_User_Names.sql',1737681553,'root','2025-01-28 10:16:59',4,1),(12,'4','Update Table Structure','SQL','V4__Update_Table_Structure.sql',-1693452933,'root','2025-01-28 10:17:00',478,1),(13,'5','Add Indexes','SQL','V5__Add_Indexes.sql',1997985427,'root','2025-01-28 10:17:00',794,1),(14,'6','Add Additional Tables','SQL','V6__Add_Additional_Tables.sql',-934176161,'root','2025-01-28 10:17:01',729,1),(15,'7','Add Remark To PaymentRecord','SQL','V7__Add_Remark_To_PaymentRecord.sql',-280169007,'root','2025-01-28 10:17:01',3,1),(16,'8','create consult table','SQL','V8__create_consult_table.sql',1492944730,'root','2025-01-28 10:17:01',8,1),(17,'3.3','Update Doctor Passwords','SQL','V3_3__Update_Doctor_Passwords.sql',2003412217,'root','2025-01-28 10:25:26',5,1),(18,'3.4','Update User Status','SQL','V3_4__Update_User_Status.sql',-99668542,'root','2025-01-28 10:40:11',4,1),(19,'3.5','Update Admin Password','SQL','V3_5__Update_Admin_Password.sql',1629112814,'root','2025-01-28 14:36:33',8,1),(20,'3.6','Fix Admin Password','SQL','V3_6__Fix_Admin_Password.sql',-655973594,'root','2025-01-28 14:42:58',5,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article`
--

DROP TABLE IF EXISTS `health_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `summary` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摘要',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `author_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者ID',
  `category_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类ID',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `favorite_count` int DEFAULT '0' COMMENT '收藏数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布，DELETED-已删除',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cover_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面图片URL',
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康文章表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article`
--

LOCK TABLES `health_article` WRITE;
/*!40000 ALTER TABLE `health_article` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_category`
--

DROP TABLE IF EXISTS `health_article_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_category` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类描述',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_category`
--

LOCK TABLES `health_article_category` WRITE;
/*!40000 ALTER TABLE `health_article_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_comment`
--

DROP TABLE IF EXISTS `health_article_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_comment` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `article_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论用户ID',
  `parent_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父评论ID',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_article` (`article_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_comment`
--

LOCK TABLES `health_article_comment` WRITE;
/*!40000 ALTER TABLE `health_article_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_comment_like`
--

DROP TABLE IF EXISTS `health_article_comment_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_comment_like` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论ID',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`,`user_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论点赞记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_comment_like`
--

LOCK TABLES `health_article_comment_like` WRITE;
/*!40000 ALTER TABLE `health_article_comment_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_comment_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_favorite`
--

DROP TABLE IF EXISTS `health_article_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_favorite` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `article_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_user` (`article_id`,`user_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章收藏记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_favorite`
--

LOCK TABLES `health_article_favorite` WRITE;
/*!40000 ALTER TABLE `health_article_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_like`
--

DROP TABLE IF EXISTS `health_article_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_like` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `article_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_user` (`article_id`,`user_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章点赞记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_like`
--

LOCK TABLES `health_article_like` WRITE;
/*!40000 ALTER TABLE `health_article_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_tag`
--

DROP TABLE IF EXISTS `health_article_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_tag` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签描述',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_tag`
--

LOCK TABLES `health_article_tag` WRITE;
/*!40000 ALTER TABLE `health_article_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_article_tag_rel`
--

DROP TABLE IF EXISTS `health_article_tag_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_article_tag_rel` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `article_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID',
  `tag_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`),
  KEY `idx_tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章-标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_article_tag_rel`
--

LOCK TABLES `health_article_tag_rel` WRITE;
/*!40000 ALTER TABLE `health_article_tag_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_article_tag_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_category`
--

DROP TABLE IF EXISTS `health_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_category` (
  `id` varchar(32) NOT NULL COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` varchar(20) NOT NULL DEFAULT 'ENABLED' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='健康资讯分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_category`
--

LOCK TABLES `health_category` WRITE;
/*!40000 ALTER TABLE `health_category` DISABLE KEYS */;
INSERT INTO `health_category` VALUES ('hc1','健康知识',1,'ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('hc2','疾病预防',2,'ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('hc3','饮食营养',3,'ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('hc4','心理健康',4,'ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('hc5','医疗动态',5,'ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01');
/*!40000 ALTER TABLE `health_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_tag`
--

DROP TABLE IF EXISTS `health_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_tag` (
  `id` varchar(32) NOT NULL COMMENT '标签ID',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `status` varchar(20) NOT NULL DEFAULT 'ENABLED' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tag_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='健康资讯标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_tag`
--

LOCK TABLES `health_tag` WRITE;
/*!40000 ALTER TABLE `health_tag` DISABLE KEYS */;
INSERT INTO `health_tag` VALUES ('ht1','常见病','ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('ht2','慢性病','ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('ht3','养生保健','ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('ht4','心理咨询','ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01'),('ht5','医疗政策','ENABLE','2025-01-28 18:17:01','2025-01-28 18:17:01');
/*!40000 ALTER TABLE `health_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `id_mapping`
--

DROP TABLE IF EXISTS `id_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `id_mapping` (
  `old_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `new_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `entity_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`old_id`,`entity_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `id_mapping`
--

LOCK TABLES `id_mapping` WRITE;
/*!40000 ALTER TABLE `id_mapping` DISABLE KEYS */;
INSERT INTO `id_mapping` VALUES ('1885591183185014785','4','doctor'),('b2e01c18338c416688c18c1b7308b18f','SCH001','schedule'),('dept_001','1','department'),('dept_002','1002','department'),('dept_003','1001','department');
/*!40000 ALTER TABLE `id_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_record`
--

DROP TABLE IF EXISTS `medical_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_record` (
  `id` varchar(32) NOT NULL COMMENT '病例ID',
  `patient_id` varchar(32) NOT NULL COMMENT '患者ID',
  `doctor_id` varchar(32) NOT NULL COMMENT '医生ID',
  `diagnosis` text NOT NULL COMMENT '诊断',
  `treatment` text COMMENT '治疗方案',
  `status` varchar(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `visit_time` datetime NOT NULL COMMENT '就诊时间',
  `chief_complaint` text NOT NULL COMMENT '主诉',
  `present_illness` text NOT NULL COMMENT '现病史',
  `past_history` text COMMENT '既往史',
  `physical_exam` text COMMENT '体格检查',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_record_patient` (`patient_id`),
  KEY `idx_record_doctor` (`doctor_id`),
  KEY `idx_record_status` (`status`),
  CONSTRAINT `fk_record_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `fk_record_patient` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='病例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_record`
--

LOCK TABLES `medical_record` WRITE;
/*!40000 ALTER TABLE `medical_record` DISABLE KEYS */;
INSERT INTO `medical_record` VALUES ('MR41877578','c3d4e5f6','1','急性胃炎','1. 建议清淡饮食，少食多餐\n2. 避免刺激性食物\n3. 规律作息，保持充足睡眠','enabled','2025-02-01 00:44:38','2025-02-01 00:44:38','2025-02-01 00:00:00','无','无',NULL,NULL,NULL);
/*!40000 ALTER TABLE `medical_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `id` varchar(32) NOT NULL COMMENT '新闻ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `category_id` varchar(32) NOT NULL COMMENT '分类ID',
  `status` varchar(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_news_status` (`status`),
  KEY `fk_news_category` (`category_id`),
  CONSTRAINT `fk_news_category` FOREIGN KEY (`category_id`) REFERENCES `health_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news_tag`
--

DROP TABLE IF EXISTS `news_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news_tag` (
  `news_id` varchar(32) NOT NULL COMMENT '新闻ID',
  `tag_id` varchar(32) NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`news_id`,`tag_id`),
  KEY `fk_news_tag_tag` (`tag_id`),
  CONSTRAINT `fk_news_tag_news` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`),
  CONSTRAINT `fk_news_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `health_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news_tag`
--

LOCK TABLES `news_tag` WRITE;
/*!40000 ALTER TABLE `news_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `news_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_record`
--

DROP TABLE IF EXISTS `payment_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_record` (
  `id` varchar(32) NOT NULL COMMENT '支付记录ID',
  `order_id` varchar(32) NOT NULL COMMENT '订单ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `payment_type` varchar(20) NOT NULL COMMENT '支付类型',
  `payment_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '交易流水号',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_payment_user` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_payment_time` (`create_time`),
  CONSTRAINT `fk_payment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_record`
--

LOCK TABLES `payment_record` WRITE;
/*!40000 ALTER TABLE `payment_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription` (
  `id` varchar(64) NOT NULL,
  `consultation_id` varchar(64) NOT NULL,
  `doctor_id` varchar(64) NOT NULL,
  `patient_id` varchar(64) NOT NULL,
  `diagnosis` text,
  `medications` text,
  `dosage` text,
  `instructions` text,
  `status` varchar(20) NOT NULL DEFAULT 'PENDING',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `consultation_id` (`consultation_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `patient_id` (`patient_id`),
  CONSTRAINT `prescription_ibfk_1` FOREIGN KEY (`consultation_id`) REFERENCES `consultation` (`id`),
  CONSTRAINT `prescription_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `user` (`id`),
  CONSTRAINT `prescription_ibfk_3` FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
INSERT INTO `prescription` VALUES ('3d13c344cc5c4c8592a6a4a736e8ab3b','1','b2c3d4e5','c3d4e5f6','急性胃炎','奥美拉唑胶囊 20mg 每日2次 共3天\n多潘立酮片 10mg 每日3次 共3天','餐前30分钟服用','1. 按时服药\n2. 注意饮食卫生\n3. 如症状加重请立即就医','PENDING','2025-02-01 00:18:30','2025-02-01 00:18:30'),('78523e2df271412caa6c4a21ed6299d1','1','b2c3d4e5','c3d4e5f6','偏头痛','布洛芬缓释胶囊 0.3g 每日2次 共3天','每次1粒，每日2次，共3天','饭后服用，如有不适请及时就医','PENDING','2025-01-31 23:53:02','2025-01-31 23:53:02');
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` varchar(32) NOT NULL COMMENT '排班ID',
  `doctor_id` varchar(32) NOT NULL COMMENT '医生ID',
  `date` date NOT NULL COMMENT '日期',
  `period` varchar(20) NOT NULL COMMENT '时段：MORNING-上午，AFTERNOON-下午',
  `max_patients` int NOT NULL COMMENT '最大接诊人数',
  `booked_patients` int NOT NULL DEFAULT '0' COMMENT '已预约人数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-停诊，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_doctor_date_period` (`doctor_id`,`date`,`period`),
  CONSTRAINT `fk_schedule_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='排班表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES ('SCH001','4','2024-02-02','MORNING',20,0,1,'2025-02-01 15:32:25','2025-02-01 15:32:25');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config` (
  `id` varchar(32) NOT NULL COMMENT '配置ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` text NOT NULL COMMENT '配置值',
  `description` varchar(200) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` VALUES ('sc1','SITE_NAME','运城市移动医疗咨询平台','站点名称','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc2','SITE_DESCRIPTION','提供在线问诊、预约挂号、健康咨询等服务','站点描述','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc3','CONSULT_FEE','50','默认咨询费用','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc4','APPOINTMENT_TIME_SLOT','30','预约时间间隔(分钟)','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc5','MAX_APPOINTMENTS_PER_DAY','50','每日最大预约数','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc6','WORKING_HOURS','8:00-18:00','工作时间','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc7','CONTACT_PHONE','0359-12345678','联系电话','2025-01-28 18:17:01','2025-01-28 18:17:01'),('sc8','CONTACT_EMAIL','support@medical.com','联系邮箱','2025-01-28 18:17:01','2025-01-28 18:17:01');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `role` varchar(20) NOT NULL COMMENT '角色',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-启用，INACTIVE-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `address` text COMMENT '地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_user_username` (`username`),
  KEY `idx_user_phone` (`phone`),
  KEY `idx_user_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('2','doctor1','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','张医','DOCTOR','13800000001',NULL,NULL,'ACTIVE','2025-01-28 18:16:59','2025-01-28 18:25:26',NULL,NULL,NULL),('3','doctor2','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','李医','DOCTOR','13800000002',NULL,NULL,'ACTIVE','2025-01-28 18:16:59','2025-01-28 18:25:26',NULL,NULL,NULL),('4','doctor3','$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C','王医','DOCTOR','13800000003',NULL,NULL,'ACTIVE','2025-01-28 18:16:59','2025-01-28 18:25:26',NULL,NULL,NULL),('a1b2c3d4','admin','$2a$10$VwppB9m3OeA1AUjMQqyU0u4qicb3rnyfKZCbxyTcjFdwCuF.4Y8vG','管理员','ADMIN',NULL,NULL,NULL,'ACTIVE','2025-01-28 18:16:59','2025-02-01 23:48:34',NULL,NULL,NULL),('b2c3d4e5','doctor','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt2s7u.','测试医生','DOCTOR',NULL,NULL,NULL,'ACTIVE','2025-01-28 18:16:59','2025-01-28 21:43:36',NULL,NULL,NULL),('c3d4e5f6','user','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt2s7u.','测试用户','USER',NULL,NULL,NULL,'ACTIVE','2025-01-28 18:16:59','2025-01-28 21:15:44',NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-02  0:39:45
