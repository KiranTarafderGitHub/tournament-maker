package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.persist.dao.MatchRepository;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.service.MatchService;

@Service
public class MatchServiceImpl  implements MatchService {

	private static final Log log = LogFactory.getLog(MatchService.class);
	
	@Autowired
	MatchRepository matchRepository;
	
	Map<String, List<String>> individualTeamOpponetList = new HashMap<>();
	
	Map<String,Team> teamMap = new HashMap<>();
	
	@Override
	public void createMatchSchedule(List<Team> teams, List<Round> rounds) {
		
		individualTeamOpponetList.clear();
		teamMap.clear();
		
		int totalTeamCount = teams.size();
		int totalRoundCount = rounds.size();
		
		if( totalRoundCount % (totalTeamCount - 1) != 0)
			throw new InvalidDataException("Round and team count mismatch");
		
		int legCount = totalRoundCount / (totalTeamCount - 1);
		
		
		List<String> allTeams = new ArrayList<>();
		teams.forEach(t -> allTeams.add(t.getName()));
		
		
		
		teams.sort(Comparator.comparing(Team::getId));
		rounds.sort(Comparator.comparing(Round::getId));
		
		teams.forEach(t -> teamMap.put(t.getName(), t));
		
		List<Match> matches = new ArrayList<>();
		int roundIndex = 0;
		for(int i = 0 ; i < legCount ; i++)
		{
			individualTeamOpponetList = getIndividualTeamOpponentList(teams);
			for(int j = 0; j < totalTeamCount - 1 ; j++)
			{
				log.info("Going to generate round matches with teams: " +allTeams );
				List<Match> roundMatches = buildMatchesForRound(roundIndex,rounds,allTeams);
				roundMatches.forEach(m -> matches.add(m));
				roundIndex++;
			}
		}
		
		matchRepository.saveAllAndFlush(matches);
		
	}
	
	public List<Match> buildMatchesForRound(int roundIndex, List<Round> rounds,List<String> allTeams)
	{
		List<Match> matchList = new ArrayList<>();
		
		
		
		List<String> perRoundOpponentList = new ArrayList<>();
		allTeams.forEach(e -> perRoundOpponentList.add(e));
		Collections.shuffle(perRoundOpponentList);
		Collections.shuffle(allTeams);
		
		List<String> matchDayScheduledTeam = new ArrayList<>();
		allTeams.forEach(t -> {
			
			

			List<String> opponents =  individualTeamOpponetList.get(t);
			log.info(t + "'s remaining opp in Leg " + (roundIndex+1) + " is "+ opponents + " perRoundOpponentList is " + perRoundOpponentList);
			

			if(!matchDayScheduledTeam.contains(t))
			{
				if(CollectionUtils.isNotEmpty(opponents) && CollectionUtils.isNotEmpty(perRoundOpponentList))
				{
					List<String> commonAvailableOpponent = new ArrayList<String>(opponents);
					commonAvailableOpponent.retainAll(perRoundOpponentList);
					log.info("Common opponent: " + commonAvailableOpponent);
					
					if(CollectionUtils.isNotEmpty(commonAvailableOpponent))
					{
						String oponnentTeamName = commonAvailableOpponent.get(0);
						Team opponentTeam = teamMap.get(oponnentTeamName);
						Team homeTeam = teamMap.get(t);
						
						Match match = new Match();
						Round round = rounds.get(roundIndex);
						
						match.setMatchDate(new Date());
						match.setRound(round);
						match.setTeamHome(homeTeam.getId());
						match.setTeamAway(opponentTeam.getId());
						matchList.add(match);
						matchDayScheduledTeam.add(homeTeam.getName());
						matchDayScheduledTeam.add(opponentTeam.getName());
						
						log.info(t + "'s opponent fixed is: " + opponentTeam.getName());
						//remove the opponent form opponemt list and round opponent list
						int opponentIndex = opponents.indexOf(oponnentTeamName);
						
						
						opponents.remove(opponentIndex);
						perRoundOpponentList.remove(t);
						perRoundOpponentList.remove(oponnentTeamName);
						
						
						individualTeamOpponetList.replace(t, opponents);
					}
					
				}
			}
			else
			{
				log.info(t + " match has already been scheduled for round "+ (roundIndex+1));
			}
			
		});
		
		return matchList;
	}
	
	public Map<String, List<String>> getIndividualTeamOpponentList(List<Team> teams)
	{
		
		Map<String, List<String>> individualTeamOpponentMap = new HashMap<>();
		teams.forEach(t -> {
			List<String> opponentList = new ArrayList<>();
			//groupBTeams.forEach(team -> opponentList.add(team));
			teams.forEach(team -> {
				if(!team.getName().equals(t.getName()))
				{
					opponentList.add(team.getName());
				}
			});
			individualTeamOpponentMap.put(t.getName(), opponentList);
		});
		
		return individualTeamOpponentMap;
	}

}
