
DROP TABLE IF EXISTS `tournament`;
CREATE TABLE `tournament` (
  `id`                   int(11)        NOT NULL AUTO_INCREMENT,
  `code`                 varchar(50)   NOT NULL,
  `name`                 varchar(150)   NOT NULL,
  `tournament_type`      varchar(150)   NULL, 
  `admin_email`			 varchar(150)   NULL, 
  
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `round`;
CREATE TABLE `round` (
  `id`                   int(11)        NOT NULL AUTO_INCREMENT,
  `name`                 varchar(100)   NOT NULL,
  `tournament_id`        int(11)        NOT NULL,  
  
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_tournament_id` 	FOREIGN KEY (`tournament_id`) 	REFERENCES `tournament` (`id`) 	ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id`                   int(11)        NOT NULL AUTO_INCREMENT,
  `name`                 varchar(100)   NOT NULL,
  `code`                 varchar(10)   NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `match_lu`;
CREATE TABLE `match_lu` (
  `id`                   int(11)        NOT NULL AUTO_INCREMENT,
  `round_id`             int(11)        NOT NULL,
  `match_date`           DATETIME        NULL,  
  `team_home`			     int(11)        NOT NULL,
  `team_away`			     int(11)        NOT NULL,
  `team_home_score`			int(11)         NULL,
  `team_away_score`			int(11)         NULL,
  `team_home_point`			int(11)         NULL,
  `team_away_point`			int(11)         NULL,
  
  `team_home_bonus_point`			int(11)         NULL,
  `team_away_bonus_point`			int(11)         NULL,
  `team_home_penalty_point`			int(11)         NULL,
  `team_away_penalty_point`			int(11)         NULL,
  
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_round_id` 	FOREIGN KEY (`round_id`) 	REFERENCES `round` (`id`) 	ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

