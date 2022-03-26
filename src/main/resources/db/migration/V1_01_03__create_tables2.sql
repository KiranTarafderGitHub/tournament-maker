
DROP TABLE IF EXISTS `third_party_authorization`;
CREATE TABLE `third_party_authorization` (
  `id`                   int(11)        NOT NULL AUTO_INCREMENT,
  `security_key`         varchar(150)   NOT NULL,
  `whitelisted_ip`       varchar(100)   NOT NULL,
  `vendor_name`          varchar(150)   NULL,
  `active`               TINYINT        NULL DEFAULT 1,
  
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

