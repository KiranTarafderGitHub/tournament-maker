DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id`           int(11)       NOT NULL AUTO_INCREMENT,
  `name`         varchar(100)  NOT NULL,
  `description`  varchar(100)  DEFAULT NULL,
  `internal`     bit(1) DEFAULT b'0',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_permission_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id`          int(11) NOT NULL AUTO_INCREMENT,
  `name`        varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `internal`    bit(1) DEFAULT b'0',

  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_role_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id`             int(11) NOT NULL AUTO_INCREMENT,
  `permission_id`  int(11) NOT NULL,
  `role_id`        int(11) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `role_permission_unique_id` (`permission_id`,`role_id`),
  KEY `INDEX_role_permission_perm_id` (`permission_id`),
  KEY `INDEX_role_permission_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`               int(11) NOT NULL AUTO_INCREMENT,
  `username`         varchar(50) NOT NULL,
  `password`         varchar(150)DEFAULT NULL,
  `user_type`        varchar(25) NOT NULL,
  `first_name`       varchar(250)DEFAULT NULL,
  `last_name`        varchar(250)DEFAULT NULL,
  `dob`              datetime DEFAULT NULL,
  `email`            varchar(150) DEFAULT NULL,
  `phone`            varchar(15) DEFAULT NULL,
  `last_access_date` datetime DEFAULT NULL,
  `created_on`       datetime DEFAULT NULL,
  `created_by`       varchar(50) DEFAULT NULL,
  `updated_on`       datetime DEFAULT NULL,
  `updated_by`       varchar(50) DEFAULT NULL,
  `enabled`          bit(1) DEFAULT NULL,
  `internal`         bit(1) DEFAULT 0,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_user_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id`       int(11) NOT NULL AUTO_INCREMENT,
  `role_id`  int(11) NOT NULL,
  `user_id`  int(11) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `user_role_unique_id` (`user_id`,`role_id`),
  KEY `INDEX_user_role_user_id` (`user_id`),
  KEY `INDEX_user_role_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
