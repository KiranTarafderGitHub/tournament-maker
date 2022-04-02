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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.ScheduleView.MatchView;
import com.kiran.league.maker.common.bean.rest.ScheduleView.RoundView;
import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.persist.dao.MatchRepository;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.RoundService;
import com.kiran.league.maker.service.TeamService;

@Service
public class MatchServiceImpl implements MatchService {

	private static final Log log = LogFactory.getLog(MatchService.class);

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	RoundService roundService;
	
	@Autowired
	TeamService teamService;

	Map<String, List<String>> individualTeamOpponetList = new HashMap<>();

	Map<String, Team> teamMap = new HashMap<>();

	@Override
	public void createMatchSchedule(List<Team> teams, List<Round> rounds) {

		individualTeamOpponetList.clear();
		teamMap.clear();

		int totalTeamCount = teams.size();
		int totalRoundCount = rounds.size();

		if (totalRoundCount % (totalTeamCount - 1) != 0)
			throw new InvalidDataException("Round and team count mismatch");

		int legCount = totalRoundCount / (totalTeamCount - 1);

		List<String> allTeams = new ArrayList<>();
		teams.forEach(t -> allTeams.add(t.getName()));

		teams.sort(Comparator.comparing(Team::getId));
		rounds.sort(Comparator.comparing(Round::getId));

		teams.forEach(t -> teamMap.put(t.getName(), t));

		List<Match> matches = new ArrayList<>();
		int roundIndex = 0;
		for (int i = 0; i < legCount; i++) {
			individualTeamOpponetList = getIndividualTeamOpponentList(teams);
			for (int j = 0; j < totalTeamCount - 1; j++) {
				log.info("Going to generate round matches with teams: " + allTeams + " for leg " + (i + 1) + " ===>");
				List<Match> roundMatches = buildMatchesForRound(roundIndex, rounds, allTeams);
				roundMatches.forEach(m -> matches.add(m));
				roundIndex++;
			}
		}

		matchRepository.saveAllAndFlush(matches);

	}
	
	@Override
	public ScheduleView getScheduleForTournament(Tournament tournament) {
		
		ScheduleView scheduleView = new ScheduleView();
		List<RoundView> roundViewList = new ArrayList<ScheduleView.RoundView>();
		
		List<Round> rounds = roundService.getRoundsForTournament(tournament);
		rounds.forEach(r -> {
			
			RoundView rv = new RoundView();
			BeanUtils.copyProperties(r, rv);
			List<MatchView> roundViewMatches = new ArrayList<ScheduleView.MatchView>();
			
			List<Match> roundMatchList = matchRepository.findByRound(r);
			roundMatchList.forEach(m -> {
				MatchView matchView = new MatchView();
				BeanUtils.copyProperties(m, matchView);
				Team teamHome = teamService.getTeamById(m.getTeamHome());
				matchView.setTeamHomeName(teamHome.getName());
				matchView.setTeamHomeCode(teamHome.getCode());
				Team teamAway = teamService.getTeamById(m.getTeamAway());
				matchView.setTeamAwayName(teamAway.getName());
				matchView.setTeamAwayCode(teamAway.getCode());
				
				roundViewMatches.add(matchView);
			});
			
			rv.setMatches(roundViewMatches);
			roundViewList.add(rv);
		});
		
		scheduleView.setRounds(roundViewList);
		
		return scheduleView;
	}
	

	public List<Match> buildMatchesForRound(int roundIndex, List<Round> rounds, List<String> allTeams) {
		List<Match> matchList = new ArrayList<>();

		List<String> perRoundOpponentList = new ArrayList<>();
		allTeams.forEach(e -> perRoundOpponentList.add(e));
		Collections.shuffle(perRoundOpponentList);
		Collections.shuffle(allTeams);

		List<String> matchDayScheduledTeam = new ArrayList<>();
		allTeams.forEach(t -> {

			List<String> opponents = individualTeamOpponetList.get(t);
			log.info(t + "'s remaining opp in Leg " + (roundIndex + 1) + " is " + opponents
					+ " perRoundOpponentList is " + perRoundOpponentList);

			if (!matchDayScheduledTeam.contains(t)) {
				if (CollectionUtils.isNotEmpty(opponents) && CollectionUtils.isNotEmpty(perRoundOpponentList)) {
					List<String> commonAvailableOpponent = new ArrayList<String>(opponents);
					commonAvailableOpponent.retainAll(perRoundOpponentList);
					log.info("Common opponent: " + commonAvailableOpponent);

					if (CollectionUtils.isNotEmpty(commonAvailableOpponent)) {
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
						// remove the opponent from leg opponent list and round opponent list
						int opponentIndex = opponents.indexOf(oponnentTeamName);
						opponents.remove(opponentIndex);
						individualTeamOpponetList.replace(t, opponents);

						// also remove home team from opponent's leg opponent list
						List<String> opponentOpponentList = individualTeamOpponetList.get(oponnentTeamName);
						opponentOpponentList.remove(t);
						individualTeamOpponetList.replace(oponnentTeamName, opponentOpponentList);

						perRoundOpponentList.remove(t);
						perRoundOpponentList.remove(oponnentTeamName);

					}

				}
			} else {
				log.info(t + " match has already been scheduled for round " + (roundIndex + 1));
			}

		});

		return matchList;
	}

	public Map<String, List<String>> getIndividualTeamOpponentList(List<Team> teams) {

		Map<String, List<String>> individualTeamOpponentMap = new HashMap<>();
		teams.forEach(t -> {
			List<String> opponentList = new ArrayList<>();
			// groupBTeams.forEach(team -> opponentList.add(team));
			teams.forEach(team -> {
				if (!team.getName().equals(t.getName())) {
					opponentList.add(team.getName());
				}
			});
			individualTeamOpponentMap.put(t.getName(), opponentList);
		});

		return individualTeamOpponentMap;
	}

}
