/*
SQLyog Ultimate v8.32 
MySQL - 5.7.33-log : Database - hailtaxi-driver
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hailtaxi-driver` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `hailtaxi-driver`;

/*Table structure for table `car` */

DROP TABLE IF EXISTS `car`;

CREATE TABLE `car` (
  `id` varchar(60) NOT NULL,
  `plateNumber` varchar(60) NOT NULL COMMENT '车牌',
  `brand` varchar(100) NOT NULL COMMENT '品牌',
  `color` varchar(10) DEFAULT NULL COMMENT '颜色',
  `status` int(1) DEFAULT NULL COMMENT '状态   0 空闲，1 繁忙',
  `car_model` int(2) DEFAULT NULL COMMENT '车型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `car` */

LOCK TABLES `car` WRITE;

UNLOCK TABLES;

/*Table structure for table `car_model` */

DROP TABLE IF EXISTS `car_model`;

CREATE TABLE `car_model` (
  `id` int(11) NOT NULL COMMENT '车型  0  快车   1   专车  2  出租车   3  六座商务车',
  `name` varchar(100) NOT NULL COMMENT '车型描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `car_model` */

LOCK TABLES `car_model` WRITE;

UNLOCK TABLES;

/*Table structure for table `driver` */

DROP TABLE IF EXISTS `driver`;

CREATE TABLE `driver` (
  `id` varchar(60) NOT NULL,
  `name` varchar(60) NOT NULL COMMENT '司机名字',
  `star` float DEFAULT NULL COMMENT '好评',
  `car_id` varchar(60) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '/状态： 0 未上线，1 在线空闲， 2 接单中  3 接到乘客，行程进行中',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `driver` */

LOCK TABLES `driver` WRITE;

insert  into `driver`(`id`,`name`,`star`,`car_id`,`status`) values ('1','张三',4.9,'1',8);

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
