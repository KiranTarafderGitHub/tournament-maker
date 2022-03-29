package com.kiran.league.maker.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.bean.rest.TournamentCreate;
import com.kiran.league.maker.common.exception.NoDataFoundException;
import com.kiran.league.maker.common.util.RandomGenerator;
import com.kiran.league.maker.persist.dao.TournamentRepository;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.TournamentType;
import com.kiran.league.maker.service.RoundService;
import com.kiran.league.maker.service.TeamService;
import com.kiran.league.maker.service.TournamnetService;

@Service
public class TournamnetServiceImpl implements TournamnetService {

	private static final Log log = LogFactory.getLog(TournamnetService.class);

	@Autowired
	TeamService teamService;

	@Autowired
	TournamentRepository tournamentRepository;

	@Autowired
	RoundService roundService;

	@Override
	public void createNewTournamnet(TournamentCreate tournamentCreate) {
		
		String teams = tournamentCreate.getTeams();
		
		if(teams == null)
			throw new NoDataFoundException("No team provided");
		
		String[] teamArray = StringUtils.split(teams, "\n");
		
		int teamListSize = teamArray.length;
		int finalTeamSize = 0;
		if(teamListSize % 2 == 0)
			finalTeamSize = teamListSize;
		else
			finalTeamSize = teamListSize + 1;
		
		String[] completeTeamArray = new String[finalTeamSize];
		
		for(int i = 0 ; i < teamListSize ; i++)
		{
			completeTeamArray[i] = teamArray[i];
		}
		
		if(finalTeamSize > teamListSize)
			completeTeamArray[finalTeamSize - 1] = "Computer";
		
		List<String> finalTeamList = Arrays.asList(completeTeamArray);
		List<Team> teamList = teamService.createTeam(finalTeamList);
		
		Tournament tournament = new Tournament();
		
		tournament.setCode(RandomGenerator.generateRandomAlphaNumericString(7));
		tournament.setName(tournamentCreate.getName());
		tournament.setTournamentType(tournamentCreate.getTournamentType());
		tournament.setAdminEmail(tournamentCreate.getEmail());
		
		tournamentRepository.saveAndFlush(tournament);
		
		int roundCount = 0;
		
		if(TournamentType.RR1L.name().equals(tournamentCreate.getTournamentType()))
			roundCount =  (finalTeamSize - 1) * TournamentType.RR1L.getLegCount();
		else if(TournamentType.RR2L.name().equals(tournamentCreate.getTournamentType()))
			roundCount =  (finalTeamSize - 1) * TournamentType.RR2L.getLegCount();
		
		List<Round> rounds = roundService.createTournamentRound(tournament, roundCount);
		
	}
}
