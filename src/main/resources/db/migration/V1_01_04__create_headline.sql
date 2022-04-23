DROP TABLE IF EXISTS `headline`;
CREATE TABLE `headline` (
  `id`              int(11)       NOT NULL AUTO_INCREMENT,
  `description`     varchar(100)  DEFAULT NULL,
  `tournament_id`   int(11)       NOT NULL,
  `headline_date`   DATETIME      NULL,
  
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_headline_tournament_id` 	FOREIGN KEY (`tournament_id`) 	REFERENCES `tournament` (`id`) 	ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;