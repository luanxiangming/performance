-- MySQL dump 10.13  Distrib 5.5.37, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: hs_perf
-- ------------------------------------------------------
-- Server version	5.5.37-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `timestamp` char(15) NOT NULL,
  `end_time` char(15) DEFAULT NULL,
  PRIMARY KEY (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `run_log`
--

DROP TABLE IF EXISTS `run_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `run_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `test_id` char(15) NOT NULL,
  `script_name` char(60) NOT NULL,
  `server` char(20) DEFAULT NULL,
  `start_time` char(15) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `end_time` char(15) DEFAULT NULL,
  `concurrent` int(11) DEFAULT NULL,
  `service` char(30) DEFAULT NULL,
  `avg_response_time` int(11) DEFAULT NULL,
  `avg_throughput` int(11) DEFAULT NULL,
  `err` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`test_id`)
        REFERENCES `test`(`timestamp`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `run_log`
--

LOCK TABLES `run_log` WRITE;
/*!40000 ALTER TABLE `run_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `run_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `run_detail`
--

DROP TABLE IF EXISTS `run_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `run_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` char(15) DEFAULT NULL,
  `run_id` int(11) DEFAULT NULL,
  `response_time` int(11) DEFAULT NULL,
  `bandwidth` int(11) DEFAULT NULL,
  `error` char(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`run_id`)
        REFERENCES `run_log`(`id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `run_detail`
--

LOCK TABLES `run_detail` WRITE;
/*!40000 ALTER TABLE `run_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `run_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `run_metrics`
--

DROP TABLE IF EXISTS `run_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `run_metrics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` char(15) DEFAULT NULL,
  `run_id` int(11) DEFAULT NULL,
  `value` bigint(18) DEFAULT NULL,
  `name` enum('Overall_CPU', 'Overall_Memory', 'Mysql_CPU', 'Mysql_Memory', 'Server_CPU', 'Server_Memory', 'Redis_CPU', 'Redis_Memory') DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`run_id`)
        REFERENCES `run_log`(`id`)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `run_metrics`
--

LOCK TABLES `run_metrics` WRITE;
/*!40000 ALTER TABLE `run_metrics` DISABLE KEYS */;
/*!40000 ALTER TABLE `run_metrics` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-06-30  7:46:37
