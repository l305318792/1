-- MySQL dump 10.13  Distrib 9.2.0, for Win64 (x86_64)
--
-- Host: localhost    Database: medical_consultation
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
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `schedule_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `doctor_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `department_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `sequence_number` int NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `cancel_reason` text COLLATE utf8mb4_general_ci,
  `visit_time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `schedule_id` (`schedule_id`),
  KEY `user_id` (`user_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `appointment_ibfk_4` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES ('1885671976313884674','b2e01c18338c416688c18c1b7308b18f','1885661643981516802','1885591183185014785','dept_001',2,'UNPAID',NULL,NULL,'2025-02-01 20:50:08','2025-02-01 20:50:08'),('1885672856362758145','b2e01c18338c416688c18c1b7308b18f','1885661643981516802','1885591183185014785','dept_001',3,'CANCELLED',NULL,'2024-02-02 09:00:00','2025-02-01 20:53:38','2025-02-01 21:57:55'),('1885679361627578369','b2e01c18338c416688c18c1b7308b18f','1885661643981516802','1885591183185014785','dept_001',4,'PAID',NULL,'2024-02-02 09:00:00','2025-02-01 21:19:29','2025-02-01 21:30:27'),('apt_001','b2e01c18338c416688c18c1b7308b18f','user_001','1885591183185014785','dept_001',1,'已完成',NULL,'2025-02-01 19:07:33','2025-02-01 19:07:33','2025-02-01 19:07:33');
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultation`
--

DROP TABLE IF EXISTS `consultation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultation` (
  `id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '患者ID',
  `doctor_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '医生ID',
  `department_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '科室ID',
  `appointment_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预约ID',
  `symptoms` text COLLATE utf8mb4_general_ci COMMENT '症状描述',
  `diagnosis` text COLLATE utf8mb4_general_ci COMMENT '诊断结果',
  `treatment` text COLLATE utf8mb4_general_ci COMMENT '治疗方案',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='问诊记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultation`
--

LOCK TABLES `consultation` WRITE;
/*!40000 ALTER TABLE `consultation` DISABLE KEYS */;
INSERT INTO `consultation` VALUES ('475bd0610d7e4b37bf16d325d84d12a2','user_001','1885591183185014785','dept_001',NULL,'发烧38度，咳嗽三天','上呼吸道感染','1. 口服布洛芬缓解发热 2. 多休息多喝水 3. 必要时服用止咳药','COMPLETED','2025-02-01 15:45:20',NULL,'2025-02-01 15:44:07','2025-02-01 15:45:55');
/*!40000 ALTER TABLE `consultation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `parent_id` varchar(36) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '父级科室ID',
  `introduction` text COLLATE utf8mb4_general_ci COMMENT '科室介绍',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES ('dept_001','内科',NULL,'内科诊疗','NORMAL','2025-02-01 15:12:36','2025-02-01 15:12:36'),('dept_002','心内科','dept_001','心脏内科诊疗','NORMAL','2025-02-01 15:13:49','2025-02-01 15:13:49'),('dept_003','消化内科','dept_001','消化系统疾病诊疗','NORMAL','2025-02-01 15:15:26','2025-02-01 15:15:26');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `department_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `specialty` text COLLATE utf8mb4_general_ci,
  `introduction` text COLLATE utf8mb4_general_ci,
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `average_rating` decimal(2,1) DEFAULT NULL,
  `rating_count` int DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `doctor_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES ('1885591183185014785','张医生','dept_001','主任医师','擅长各种内科疾病的诊断和治疗','从医20年，具有丰富的临床经验','NORMAL',4.7,1,'2025-02-01 15:29:05','2025-02-01 15:29:05');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_rating`
--

DROP TABLE IF EXISTS `doctor_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_rating` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `appointment_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `doctor_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `service_attitude` decimal(3,1) NOT NULL COMMENT '服务态度评分',
  `medical_skill` decimal(3,1) NOT NULL COMMENT '医疗技术评分',
  `medical_effect` decimal(3,1) NOT NULL COMMENT '治疗效果评分',
  `comment` text COLLATE utf8mb4_general_ci COMMENT '评价内容',
  PRIMARY KEY (`id`),
  KEY `appointment_id` (`appointment_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `doctor_rating_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
  CONSTRAINT `doctor_rating_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `doctor_rating_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_rating`
--

LOCK TABLES `doctor_rating` WRITE;
/*!40000 ALTER TABLE `doctor_rating` DISABLE KEYS */;
INSERT INTO `doctor_rating` VALUES ('248c8128-ce56-4354-b170-f4ec24d92d2f','apt_001','1885591183185014785','user_001','2025-02-01 19:38:23','2025-02-01 19:38:23',4.5,5.0,4.5,'医生很专业，态度也很好，解答很详细');
/*!40000 ALTER TABLE `doctor_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `consultation_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `doctor_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `patient_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `diagnosis` text COLLATE utf8mb4_general_ci,
  `medications` text COLLATE utf8mb4_general_ci,
  `dosage` text COLLATE utf8mb4_general_ci,
  `instructions` text COLLATE utf8mb4_general_ci,
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `doctor_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `department_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `schedule_date` date NOT NULL,
  `period` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `max_appointments` int NOT NULL,
  `appointed_count` int DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `remark` text COLLATE utf8mb4_general_ci,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES ('b2e01c18338c416688c18c1b7308b18f','1885591183185014785','dept_001','2024-02-02','MORNING',20,0,'AVAILABLE','上午门诊','2025-02-01 15:32:25','2025-02-01 15:32:25');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1885660236171759617','patient001','$2a$10$XdwgSpZJMMXsQbQIylmmCeEfq9f3u3egYZTYpN5sy1zydu5lXzlRq','李四','13800138000','USER','normal','2025-02-01 20:03:29','2025-02-01 20:03:29'),('1885661643981516802','patient002','$2a$10$a3oHMMtS6XOCtejSvcp/8ux8Zx/RA9ZHCXC5Lr4p7fWdNoMZF2wXW','张三','13900139000','USER','normal','2025-02-01 20:09:04','2025-02-01 20:09:04'),('user_001','zhangsan','123456','张三','13800138000','PATIENT','NORMAL','2025-02-01 18:54:36','2025-02-01 18:54:36');
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
