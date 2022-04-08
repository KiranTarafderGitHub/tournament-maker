package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;

public interface TeamService {
	
	List<Team> createTeam(List<String> teams);
	
	Team getTeamById(Long id);
	
	List<Team> getAllTeamOfTournament(Tournament tournament);
}
