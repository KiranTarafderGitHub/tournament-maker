package com.kiran.league.maker.service;

import com.kiran.league.maker.common.bean.rest.TournamentCreate;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;

public interface TournamnetService {

	public Tournament createNewTournamnet(TournamentCreate tournament);
	
	public Tournament getTournamentByCode(String code);
	
	public Tournament getTournamentById(Long id);
	
	public Tournament addTeamToExistingTournament(Tournament tournament, String teamName);
}
