DROP SCHEMA IF EXISTS `scheduler` ;

-- -----------------------------------------------------
-- Schema scheduler
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `scheduler` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `scheduler` ;

-- -----------------------------------------------------
-- Table `scheduler`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `scheduler`.`task` ;
DROP TABLE IF EXISTS `scheduler`.`task_type` ;
DROP TABLE IF EXISTS `scheduler`.`user` ;


CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `password` varchar(60) NOT NULL,
  `phone` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `task_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `date` varchar(45) NOT NULL,
  `location` varchar(45) DEFAULT NULL,
  `idUser` int(11) NOT NULL,
  `idType` int(11) NOT NULL,
  `isDone` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_Task_User_idx` (`idUser`),
  CONSTRAINT `fk_Task_User` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  KEY `fk_Task_Type_idx` (`idUser`),
  CONSTRAINT `fk_Task_Type` FOREIGN KEY (`idType`) REFERENCES `task_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `scheduler`.`task_type` (`type`) VALUES ('Reminding');
INSERT INTO `scheduler`.`task_type` (`type`) VALUES ('Meeting');
INSERT INTO `scheduler`.`task_type` (`type`) VALUES ('Note');