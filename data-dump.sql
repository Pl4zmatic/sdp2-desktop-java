CREATE DATABASE  IF NOT EXISTS `schooldb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `schooldb`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: schooldb
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- Table structure for table `REPORT`
--

DROP TABLE IF EXISTS `REPORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REPORT` (
  `RAPPORTID` int NOT NULL AUTO_INCREMENT,
  `IMAGES` longblob,
  `NOTES` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `STEPS` longblob NOT NULL,
  `MAINTENANCE_MAINTENANCEID` int DEFAULT NULL,
  PRIMARY KEY (`RAPPORTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPORT`
--

LOCK TABLES `REPORT` WRITE;
/*!40000 ALTER TABLE `REPORT` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPORT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SEQUENCE`
--

DROP TABLE IF EXISTS `SEQUENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQUENCE` (
  `SEQ_NAME` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SEQUENCE`
--

LOCK TABLES `SEQUENCE` WRITE;
/*!40000 ALTER TABLE `SEQUENCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `SEQUENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_prisma_migrations`
--

DROP TABLE IF EXISTS `_prisma_migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `_prisma_migrations` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `finished_at` datetime(3) DEFAULT NULL,
  `migration_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `logs` text COLLATE utf8mb4_unicode_ci,
  `rolled_back_at` datetime(3) DEFAULT NULL,
  `started_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `applied_steps_count` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_prisma_migrations`
--

LOCK TABLES `_prisma_migrations` WRITE;
/*!40000 ALTER TABLE `_prisma_migrations` DISABLE KEYS */;
INSERT INTO `_prisma_migrations` VALUES ('02430c99-a719-4d92-a090-00ec9041fbb6','168c74f2bb56f94ad964ab6ab84c9926ea7fbb10bb0518343ec41d47e5f6c172','2026-03-06 18:13:40.339','20250515105317_site_user',NULL,NULL,'2026-03-06 18:13:40.123',1),('2ba8f24a-7af5-4195-a288-a733f64270dd','f89e1dcb04401ac048dca5c72f84e700fdcb2b71064bb5286b9086b663d7744c','2026-03-06 18:13:39.208','20250506220312_kpi',NULL,NULL,'2026-03-06 18:13:38.113',1),('2c600f0f-82a2-4315-97fc-d67cc259a67c','9dd0aa7acde3264a36c7e9a8a7504372d9e28820ed6de3e9ed715a7a96af5afc','2026-03-06 18:13:39.743','20250507120331_machine_code_added_to_kpi',NULL,NULL,'2026-03-06 18:13:39.552',1),('6dd377cb-5ebc-459e-ba7e-2b1535142be5','35b76fcf9d3ee82f9dbdf3696a6c6910b9da16276fd43468a340b94ba7a2878f','2026-03-06 18:13:39.939','20250507160734_maintenace_cost_and_decimal_limits_added',NULL,NULL,'2026-03-06 18:13:39.748',1),('723d7297-5a4e-4328-a314-116b57c1d9b6','5abc397a2de0dcf9cdc2ff63a8a9ba2001a942fd2690164c750cfa9c25e8bb37','2026-03-06 18:13:39.548','20250507103252_product_and_site_name_reference',NULL,NULL,'2026-03-06 18:13:39.292',1),('910aece0-1fc6-4d8d-a543-a355f9988818','4af2021241ff5bce2deadeb0ada4d7a3b190eeea7ec887d6760247ae378120d6','2026-03-06 18:13:40.118','20250507203703_product_relation_in_machines',NULL,NULL,'2026-03-06 18:13:39.944',1),('b22a473a-e05d-4ef1-b3a8-0d901ac70b07','1c5732d26eff09e2460a769588f3019b033eac297a2418481023a0a52d8e20da','2026-03-06 18:13:39.288','20250507095951_kpi_data',NULL,NULL,'2026-03-06 18:13:39.216',1);
/*!40000 ALTER TABLE `_prisma_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kpi`
--

DROP TABLE IF EXISTS `kpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kpi` (
  `date` date NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `produced` int DEFAULT NULL,
  `productName` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `productionCost` decimal(10,2) DEFAULT NULL,
  `siteName` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `target` int DEFAULT NULL,
  `uptime` int DEFAULT NULL,
  `machineCode` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `kpi_siteName_fkey` (`siteName`),
  KEY `kpi_productName_fkey` (`productName`),
  KEY `kpi_machineCode_fkey` (`machineCode`),
  CONSTRAINT `kpi_machineCode_fkey` FOREIGN KEY (`machineCode`) REFERENCES `machines` (`CODE`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `kpi_productName_fkey` FOREIGN KEY (`productName`) REFERENCES `product` (`NAME`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `kpi_siteName_fkey` FOREIGN KEY (`siteName`) REFERENCES `sites` (`NAME`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kpi`
--

LOCK TABLES `kpi` WRITE;
/*!40000 ALTER TABLE `kpi` DISABLE KEYS */;
INSERT INTO `kpi` VALUES ('2021-03-15',1,13200,'Aardbeien',2.00,'Brussel',5700,55,'a-05'),('2022-03-15',2,15200,'Aardbeien',1.90,'Antwerpen',6700,65,'a-01'),('2023-03-15',3,17200,'Aardbeien',1.80,'Gent',7700,75,'a-02'),('2024-03-15',4,19200,'Aardbeien',1.70,'Leuven',8700,85,'a-04'),('2025-03-15',5,14200,'Aardbeien',2.10,'Brugge',6200,60,'a-03'),('2021-06-22',6,18900,'Tomaten',1.80,'Antwerpen',8200,78,'a-01'),('2022-06-22',7,17900,'Tomaten',1.90,'Gent',7200,68,'a-02'),('2023-06-22',8,16900,'Tomaten',2.00,'Leuven',6200,58,'a-04'),('2024-06-22',9,19900,'Tomaten',1.70,'Brugge',9200,88,'a-03'),('2025-06-22',10,14900,'Tomaten',2.20,'Brussel',5200,48,'a-05'),('2021-09-03',11,11500,'Komkommers',2.20,'Gent',6500,62,'a-02'),('2022-09-03',12,12500,'Komkommers',2.10,'Leuven',7500,72,'a-04'),('2023-09-03',13,13500,'Komkommers',2.00,'Brugge',8500,82,'a-03'),('2024-09-03',14,10500,'Komkommers',2.30,'Brussel',5500,52,'a-05'),('2025-09-03',15,14500,'Komkommers',1.60,'Antwerpen',9500,92,'a-01'),('2021-11-29',16,21000,'Wortelen',1.50,'Leuven',9500,85,'a-04'),('2022-11-29',17,20000,'Wortelen',1.60,'Brugge',8500,75,'a-03'),('2023-11-29',18,19000,'Wortelen',1.70,'Brussel',7500,65,'a-05'),('2024-11-29',19,18000,'Wortelen',1.80,'Antwerpen',6500,55,'a-01'),('2025-11-29',20,22000,'Wortelen',1.40,'Gent',10500,95,'a-02'),('2021-07-14',21,9800,'Sla',2.50,'Brugge',4800,45,'a-03'),('2022-07-14',22,10800,'Sla',2.40,'Brussel',5800,55,'a-05'),('2023-07-14',23,11800,'Sla',2.30,'Antwerpen',6800,65,'a-01'),('2024-07-14',24,12800,'Sla',2.20,'Gent',7800,75,'a-02'),('2025-07-14',25,13800,'Sla',2.10,'Leuven',8800,85,'a-04');
/*!40000 ALTER TABLE `kpi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logs` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ACTION` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DETAILS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TIMESTAMP` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `machines`
--

DROP TABLE IF EXISTS `machines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `machines` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `CODE` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `CURRENTSTATESTRING` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `datum_toekomstige_onderhoud` date DEFAULT NULL,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `laatste_onderhoud_beschrijving` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `laatste_onderhoud_datum` date DEFAULT NULL,
  `LOCATIE` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PRODUCTINFO` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PRODUCTIESTATUS` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `UPTIMEINHOURS` int DEFAULT NULL,
  `sitenaam` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `technieker_id` bigint DEFAULT NULL,
  `productName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE` (`CODE`),
  KEY `FK_machines_sitenaam` (`sitenaam`),
  KEY `FK_machines_technieker_id` (`technieker_id`),
  KEY `machines_productName_fkey` (`productName`),
  CONSTRAINT `FK_machines_sitenaam` FOREIGN KEY (`sitenaam`) REFERENCES `sites` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_machines_technieker_id` FOREIGN KEY (`technieker_id`) REFERENCES `users` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `machines_productName_fkey` FOREIGN KEY (`productName`) REFERENCES `product` (`NAME`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `machines`
--

LOCK TABLES `machines` WRITE;
/*!40000 ALTER TABLE `machines` DISABLE KEYS */;
INSERT INTO `machines` VALUES (1,'a-01','running','2025-12-15',0,'Vervanging van onderdelen','2025-09-03','Groenplaats 12, 2000 Antwerpen','test','Maintenance Required',42,'Antwerpen',3,'Komkommers'),(2,'a-02','stopped','2026-01-10',0,'Smering en kalibratie','2025-11-29','Korenmarkt 5, 9000 Gent','test','Active',128,'Gent',3,'Wortelen'),(3,'a-03','running','2025-11-01',0,'Herstart en test','2025-07-14','Markt 1, 8000 Brugge','test','Inactive',0,'Brugge',3,'Sla'),(4,'a-04','stopped','2025-11-01',0,'Stroomvoorziening nagekeken','2025-07-14','Naamsestraat 22, 3000 Leuven','test','Maintenance Required',87,'Leuven',3,'Sla'),(5,'a-05','running','2025-09-30',0,'Filters vervangen','2025-06-22','Rue du Marché aux Herbes 100, 1000 Brussel','test','Active',215,'Brussel',3,'Tomaten'),(6,'a-06','stopped','2025-12-20',0,'Inspectie uitgevoerd','2025-09-01','Groenplaats 12, 2000 Antwerpen','test','Inactive',0,'Antwerpen',3,'Komkommers'),(7,'a-07','running','2026-01-10',0,'Nazicht koeling','2025-11-29','Korenmarkt 5, 9000 Gent','test','Maintenance Required',64,'Gent',3,'Wortelen'),(8,'a-08','stopped','2025-11-01',0,'Routinecontrole','2025-07-14','Markt 1, 8000 Brugge','test','Active',312,'Brugge',3,'Sla'),(9,'a-09','running','2025-11-01',0,'Sensor vervangen','2025-07-14','Naamsestraat 22, 3000 Leuven','test','Inactive',0,'Leuven',3,'Sla'),(10,'a-10','stopped','2025-09-30',0,'Afstelling motor','2025-06-22','Rue du Marché aux Herbes 100, 1000 Brussel','test','Maintenance Required',178,'Brussel',3,'Tomaten');
/*!40000 ALTER TABLE `machines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenances`
--

DROP TABLE IF EXISTS `maintenances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenances` (
  `MAINTENANCEID` bigint NOT NULL AUTO_INCREMENT,
  `CURRENTSTATESTRING` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENDDATE` date DEFAULT NULL,
  `MACHINECODE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NAMETECHNICIAN` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REASON` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REMARKS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `STARTDATE` date DEFAULT NULL,
  `machine_id` bigint NOT NULL,
  `MAINTENANCEREPORT_RAPPORTID` int DEFAULT NULL,
  `maintenanceCost` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`MAINTENANCEID`),
  KEY `FK_maintenances_MAINTENANCEREPORT_RAPPORTID` (`MAINTENANCEREPORT_RAPPORTID`),
  KEY `FK_maintenances_machine_id` (`machine_id`),
  CONSTRAINT `FK_maintenances_machine_id` FOREIGN KEY (`machine_id`) REFERENCES `machines` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_maintenances_MAINTENANCEREPORT_RAPPORTID` FOREIGN KEY (`MAINTENANCEREPORT_RAPPORTID`) REFERENCES `REPORT` (`RAPPORTID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenances`
--

LOCK TABLES `maintenances` WRITE;
/*!40000 ALTER TABLE `maintenances` DISABLE KEYS */;
INSERT INTO `maintenances` VALUES (1,'FinishedState','2020-06-15','a-01','Technieker de Technieker','Routine maintenance and calibration','All components cleaned and recalibrated. No issues detected.','2020-05-10',1,NULL,950.00),(2,'FinishedState','2023-02-20','a-01','Technieker de Technieker','Motor temperature spikes during operation','Replaced faulty cooling fan and adjusted temperature thresholds.','2023-01-15',1,NULL,1100.00),(3,'ProgressState',NULL,'a-06','Technieker de Technieker','Emergency shutdown due to sensor failure','Sensor diagnostics and replacements in progress.','2025-09-10',6,NULL,NULL),(4,'FinishedState','2021-03-25','a-02','Technieker de Technieker','Annual mechanical inspection','Lubricated moving parts, replaced worn-out belt.','2021-02-18',2,NULL,1200.00),(5,'FinishedState','2024-07-30','a-07','Technieker de Technieker','Irregular vibration detected by monitoring system','Balanced rotating components, resolved misalignment.','2024-06-22',7,NULL,1050.00),(6,'FinishedState','2020-11-12','a-03','Technieker de Technieker','Filter clogging causing reduced throughput','Filters cleaned and replaced. System performance back to normal.','2020-10-05',3,NULL,875.00),(7,'FinishedState','2022-08-18','a-08','Technieker de Technieker','Preventive gearbox inspection','Minor wear found, replaced two gears as precaution.','2022-07-10',8,NULL,1300.00),(8,'FinishedState','2021-09-30','a-04','Technieker de Technieker','Output quality degradation reported','Cleaned optics and recalibrated print head.','2021-08-22',4,NULL,1150.00),(9,'FinishedState','2023-12-15','a-09','Technieker de Technieker','Noise increase during operation','Bearing replaced and re-lubricated. Noise levels normalized.','2023-11-01',9,NULL,925.00),(10,'FinishedState','2020-04-05','a-05','Technieker de Technieker','Startup delay observed','Replaced power relay and updated firmware.','2020-03-01',5,NULL,1000.00),(11,'PlannedState',NULL,'a-10','Technieker de Technieker','Planned inspection before seasonal production spike','Inspection scheduled to ensure readiness and avoid downtime.','2026-02-10',10,NULL,NULL),(12,'FinishedState','2022-05-12','a-02','Technieker de Technieker','Error codes during routine diagnostics','Faulty sensor replaced. Retested and passed.','2022-03-28',2,NULL,1425.00),(13,'FinishedState','2024-01-22','a-07','Technieker de Technieker','Oil leak from hydraulic system','Leak repaired and oil refilled. System tested.','2023-12-15',7,NULL,1100.00),(14,'FinishedState','2021-12-08','a-03','Technieker de Technieker','Unexpected shutdown during shift','Identified short-circuited control board, replaced successfully.','2021-10-30',3,NULL,800.00),(15,'ProgressState',NULL,'a-08','Technieker de Technieker','Ongoing upgrade to improve energy efficiency','In progress: replacing old components with energy-efficient alternatives.','2025-04-18',8,NULL,NULL),(16,'FinishedState','2023-06-30','a-04','Technieker de Technieker','Scheduled mid-year service','General inspection completed. Replaced minor parts.','2023-05-12',4,NULL,975.00),(17,'FinishedState','2025-08-12','a-09','Technieker de Technieker','Worn-out conveyor belt','Replaced conveyor belt. Checked for proper alignment.','2025-07-01',9,NULL,1350.00),(18,'PlannedState',NULL,'a-05','Technieker de Technieker','Quarterly maintenance','To include safety checks and software updates.','2026-03-05',5,NULL,NULL);
/*!40000 ALTER TABLE `maintenances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `MESSAGE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TYPE` int DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `product_NAME_key` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Aardbeien'),(3,'Komkommers'),(5,'Sla'),(2,'Tomaten'),(4,'Wortelen');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_images`
--

DROP TABLE IF EXISTS `report_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_images` (
  `DATA` longblob,
  `EXTENSION` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `report_id` int DEFAULT NULL,
  KEY `FK_report_images_report_id` (`report_id`),
  CONSTRAINT `FK_report_images_report_id` FOREIGN KEY (`report_id`) REFERENCES `REPORT` (`RAPPORTID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_images`
--

LOCK TABLES `report_images` WRITE;
/*!40000 ALTER TABLE `report_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sites`
--

DROP TABLE IF EXISTS `sites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sites` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ADDRESS` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `verantwoordelijke_id` bigint DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK_sites_verantwoordelijke_id` (`verantwoordelijke_id`),
  CONSTRAINT `FK_sites_verantwoordelijke_id` FOREIGN KEY (`verantwoordelijke_id`) REFERENCES `users` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sites`
--

LOCK TABLES `sites` WRITE;
/*!40000 ALTER TABLE `sites` DISABLE KEYS */;
INSERT INTO `sites` VALUES (1,'Antwerpen 23, 9140 Elversele',0,'Antwerpen',3),(2,'Gentstraat 45, 9000 Gent',0,'Gent',3),(3,'Leuvensestraat 56, 8970 Leuven',0,'Leuven',3),(4,'Rue du Marché aux Herbes 100, 1000 Brussels',0,'Brussel',3),(5,'Markt 1, 8000 Bruges',0,'Brugge',3);
/*!40000 ALTER TABLE `sites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_notifications`
--

DROP TABLE IF EXISTS `user_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_notifications` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ISREAD` tinyint(1) DEFAULT '0',
  `notification_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_user_notifications_notification_id` (`notification_id`),
  KEY `FK_user_notifications_user_id` (`user_id`),
  CONSTRAINT `FK_user_notifications_notification_id` FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_user_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notifications`
--

LOCK TABLES `user_notifications` WRITE;
/*!40000 ALTER TABLE `user_notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ADRES` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `BIRTHDATE` date NOT NULL,
  `DELETED` tinyint(1) NOT NULL DEFAULT '0',
  `EMAIL` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `FIRSTNAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `GSMNUMMER` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LASTNAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PASSWORD` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ROL` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EMAIL` (`EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Verantwoordelijkestraat 59','1958-03-20',0,'Verantwoordelijke@mail.com','Verantwoordelijke','1234562','de Verantwoordelijke','$2b$10$SXcKmQ6L8HFhgHwa80o4fOg6pHgQCqbKnz5dOpLweFOlI4xDS3C4C',1),(2,'Techniekstraat 42','1985-07-13',0,'technieker@mail.com','Technieker','9876543','de Technieker','$2b$10$avMkyHCF.6ljBao/uXYurOWVM7lvXgIANGGStmW9QZ3Xt.zeRFrD.',2),(3,'Managerstraat 82','1995-02-21',0,'manager@mail.com','Manager','0493246751','de Manager','$2b$10$hi3.tv5X8pK6wQh4.BiO/eE/Yg1CChqtRSAgzsCdiEJx2mXtBqWFC',3),(4,'Adminstraat 59','2003-05-13',0,'admin@mail.com','admin','1234561','de Admin','$2b$10$pRSZgqFqmp5i78aW6tqvlexU0DN8Fk5pgUmCaZ5oR5miiCN4DbPnG',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-06 19:45:03
