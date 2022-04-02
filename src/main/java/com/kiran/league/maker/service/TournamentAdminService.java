package com.kiran.league.maker.service;

import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.UserEntity;

public interface TournamentAdminService {

	UserEntity creteAdminUserForTournament(Tournament tournament);
	
	Tournament getTournamentForUser(UserEntity user);

}
