package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.persist.dao.TeamRepository;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.service.TeamService;

@Service
public class TeamServiceImpl implements TeamService {

	private static final Log log = LogFactory.getLog(TeamServiceImpl.class);
	
	@Autowired
	TeamRepository teamRepository;
	
	@Override
	public List<Team> createTeam(List<String> teams) {
		
		List<Team> newTeams = new ArrayList<>();
		teams.forEach(t -> {
			Team team = new Team();
			team.setName(t);
			String code = "";
			for(int i = 0 ; i < t.length(); i++)
			{
				code = t.substring(0, i+1);
				if( i == 2)
					break;
			}
			team.setCode(code.toUpperCase());
			newTeams.add(team);
		});
		
		return teamRepository.saveAllAndFlush(newTeams);
		
	}

}
