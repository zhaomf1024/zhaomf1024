create database if not exists bank;

use bank;

CREATE TABLE `account` (
  `name` varchar(255) DEFAULT NULL,
  `money` int(10) DEFAULT NULL,
  `cardNo` bigint(15) NOT NULL,
  PRIMARY KEY (`cardNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `bank`.`account`(`name`, `money`, `cardNo`) VALUES ('李大雷', 10000, 6029621011000);
INSERT INTO `bank`.`account`(`name`, `money`, `cardNo`) VALUES ('韩梅梅 ', 10000, 6029621011001);
