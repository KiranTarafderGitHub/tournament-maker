INSERT INTO `permission` (id, name, description)  VALUES (1, 'admin.all',                  'All User');
INSERT INTO `permission` (id, name, description)  VALUES (2, 'league.admin',               'Add User Menu');
INSERT INTO `permission` (id, name, description)  VALUES (3, 'league.user',                'Save User');

INSERT INTO `role` VALUES (1,'admin','Admin Role',false),
                          (2,'league_admin','League Admin Role',false),
                          (3,'league_user', 'League User Role', false);

INSERT INTO `role_permission` VALUES 
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 2, 2),
(5, 3, 3);

INSERT INTO `user` VALUES (1,'admin','$2a$10$L1FPxXNNos.SbJKoUbhw1OCjizKI4Ct06r6Q6JANnfzbhp4RPvEgy','ADMINISTRATOR','Admin','Admin','1990-01-01 00:00:00','admin@gmail.com',NULL,'2020-10-05 21:44:12','2020-10-05 21:44:12','System',NULL,NULL,1,0);

INSERT INTO `user_role` VALUES (1,1,1);
