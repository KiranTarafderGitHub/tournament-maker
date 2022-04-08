package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.exception.NoDataFoundException;
import com.kiran.league.maker.persist.dao.MatchRepository;
import com.kiran.league.maker.persist.dao.TeamRepository;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.RoundService;
import com.kiran.league.maker.service.TeamService;

@Service
public class TeamServiceImpl implements TeamService {

	private static final Log log = LogFactory.getLog(TeamServiceImpl.class);
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	RoundService roundService;
	
	@Autowired
	MatchRepository matchRepository;

	
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

	@Override
	public Team getTeamById(Long id) {
		Optional<Team> teamOptional = teamRepository.findById(id);
		if(teamOptional.isEmpty())
			throw new NoDataFoundException("No team found with id" + id);
		
		return teamOptional.get();
	}

	@Override
	public List<Team> getAllTeamOfTournament(Tournament tournament) {
		
		List<Long> allTeamIdWithDuplicate = new ArrayList<>();
		List<Round> rounds = roundService.getRoundsForTournament(tournament);
		rounds.forEach(r -> {
			List<Match> matches = matchRepository.findByRound(r);
			matches.forEach(m -> {
				allTeamIdWithDuplicate.add(m.getTeamHome());
				allTeamIdWithDuplicate.add(m.getTeamAway());
			});
		});
		List<Long> allTeamId = allTeamIdWithDuplicate.stream().distinct().collect(Collectors.toList());
		List<Team> allTeams = teamRepository.findAllById(allTeamId);
		return allTeams;
	
	}

}
