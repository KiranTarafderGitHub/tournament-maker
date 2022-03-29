package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.persist.entity.Team;

public interface TeamService {
	List<Team> createTeam(List<String> teams);
}
