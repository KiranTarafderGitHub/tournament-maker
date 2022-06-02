package com.kiran.league.maker.service;

import com.kiran.league.maker.common.bean.BackupBean;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.UserEntity;

public interface TournamentAdminService {

	UserEntity creteAdminUserForTournament(Tournament tournament);
	
	Tournament getTournamentForUser(UserEntity user);
	
	UserEntity getTournamentAdminUser(Tournament tournament);
	
	UserEntity restoreAdminUserForTournament(Tournament tournament, BackupBean backupBean);

}
