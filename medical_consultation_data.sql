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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-02  0:20:16
