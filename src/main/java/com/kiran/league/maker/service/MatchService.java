package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;

public interface MatchService {
	
	public void createMatchSchedule(List<Team> teams, List<Round> rounds);

}
