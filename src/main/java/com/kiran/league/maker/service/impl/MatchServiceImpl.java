package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.bean.rest.LeagueTableView;
import com.kiran.league.maker.common.bean.rest.LeagueTableView.TeamSummaryView;
import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.ScheduleView.MatchView;
import com.kiran.league.maker.common.bean.rest.ScheduleView.RoundView;
import com.kiran.league.maker.common.bean.rest.UpdateScorePost;
import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.persist.dao.MatchRepository;
import com.kiran.league.maker.persist.dao.TournamentRepository;
import com.kiran.league.maker.persist.dto.Result;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.RoundService;
import com.kiran.league.maker.service.TeamService;
import com.kiran.league.maker.service.TournamnetService;

@Service
public class MatchServiceImpl implements MatchService {

	private static final Log log = LogFactory.getLog(MatchService.class);

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	RoundService roundService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	TournamentRepository tournamentRepository;
	
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
				log.info("Going to generate round " +(roundIndex +1) + " matches with teams: " + allTeams + " for leg " + (i + 1) + " ================>");
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
				matchView.setTeamHomeScore(m.getTeamHomeScore());
				
				Team teamAway = teamService.getTeamById(m.getTeamAway());
				matchView.setTeamAwayName(teamAway.getName());
				matchView.setTeamAwayCode(teamAway.getCode());
				matchView.setTeamAwayScore(m.getTeamAwayScore());
				
				//log.info(matchView.toString());
				roundViewMatches.add(matchView);
			});
			
			rv.setMatches(roundViewMatches);
			roundViewList.add(rv);
		});
		
		scheduleView.setRounds(roundViewList);
		
		return scheduleView;
	}
	
	
	@Override
	public LeagueTableView getLeagueStanding(Tournament tournament) {
		LeagueTableView leagueTableView = new LeagueTableView();
		List<TeamSummaryView> teamSummaryList = new ArrayList<>();
		
		
		List<Team> allTeam = teamService.getAllTeamOfTournament(tournament);
		for(Team t: allTeam)
		{
			int totalBonus=0;int totalPenalty=0;int totalPoint=0;int matchPlayed=0;int matchWon = 0;
			int matchDraw=0;int matchLost=0;int goalScored = 0;int goalConceded = 0;int goalDifference=0;
			
			TeamSummaryView tsView = new TeamSummaryView();
			
			tsView.setTeamId(t.getId());
			tsView.setTeamName(t.getName());
			
			List<Match> matchList = getAllMatchForTeam(t.getId(),tournament.getId());
			for(Match m : matchList)
			{
				if(m.getTeamHome().intValue() == t.getId().intValue())
				{
					if( m.getTeamHomeScore() != null )
					{
						matchPlayed++;
						goalScored += m.getTeamHomeScore().intValue();
						goalConceded += m.getTeamAwayScore().intValue();
						totalBonus += ( m.getTeamHomeBonusPoint() != null ? m.getTeamHomeBonusPoint().intValue() : 0);
						totalPenalty += ( m.getTeamHomePenaltyPoint() != null ? m.getTeamHomePenaltyPoint().intValue() : 0);
						
						if(m.getTeamHomeScore().intValue() > m.getTeamAwayScore().intValue())
						{
							matchWon++;
							totalPoint += Result.WIN.getPoint().intValue();
						}
						else if(m.getTeamHomeScore().intValue() == m.getTeamAwayScore().intValue())
						{
							matchDraw++;
							totalPoint += Result.DRAW.getPoint().intValue();
						}
						else
						{
							matchLost++;
							totalPoint += Result.LOSS.getPoint().intValue();
						}
					}
				}
				else if(m.getTeamAway().intValue() == t.getId().intValue())
				{
					if( m.getTeamAwayScore() != null )
					{
						matchPlayed++;
						goalScored += m.getTeamAwayScore().intValue();
						goalConceded += m.getTeamHomeScore().intValue();
						totalBonus += ( m.getTeamAwayBonusPoint() != null ? m.getTeamAwayBonusPoint().intValue() : 0);
						totalPenalty += ( m.getTeamAwayPenaltyPoint() != null ? m.getTeamAwayPenaltyPoint().intValue() : 0);
						
						if(m.getTeamAwayScore().intValue() > m.getTeamHomeScore().intValue())
						{
							matchWon++;
							totalPoint += Result.WIN.getPoint().intValue();
						}
						else if(m.getTeamAwayScore().intValue() ==  m.getTeamHomeScore().intValue())
						{
							matchDraw++;
							totalPoint += Result.DRAW.getPoint().intValue();
						}
						else
						{
							matchLost++;
							totalPoint += Result.LOSS.getPoint().intValue();
						}
					}
				}
			}
			
			totalPoint += (totalBonus - totalPenalty);
			goalDifference = goalScored - goalConceded;
			
			tsView.setMatchPlayed(matchPlayed);
			tsView.setMatchWon(matchWon);
			tsView.setMatchDraw(matchDraw);
			tsView.setMatchLost(matchLost);
			tsView.setGoalScored(goalScored);
			tsView.setGoalConceded(goalConceded);
			tsView.setGoalDifference(goalDifference);
			tsView.setTotalBonusPoint(totalBonus);
			tsView.setTotalPenaltyPoint(totalPenalty);
			tsView.setTotalPoints(totalPoint);
			
			teamSummaryList.add(tsView);
		}
		
		List<TeamSummaryView> sortedTeamList = sortTeamForLeague(teamSummaryList);
		//Collections.reverse(sortedTeamList);
		
		leagueTableView.setTeams(sortedTeamList);
		
		return leagueTableView;
	}
	
	@Override
	public List<Match> getAllMatchForTeam(Long teamId, Long tournamnetId) {
		List<Match> matchListForTeam = new ArrayList<>();
		Tournament tournament = tournamentRepository.getById(tournamnetId);
		List<Round> rounds = roundService.getRoundsForTournament(tournament);
		rounds.forEach(r -> {
			List<Match> roundMatches = getAllMatchForRound(r);
			roundMatches.forEach(m -> {
				if(m.getTeamHome() == teamId)
					matchListForTeam.add(m);
				else if(m.getTeamAway() == teamId)
					matchListForTeam.add(m);
			});
		});
		 
		return matchListForTeam;
	}

	@Override
	public void updateScore(UpdateScorePost score) {


		Match match = matchRepository.getById(score.getMatchId());
		
		int homeScore = score.getTeamHomeScore();
		int awayScore = score.getTeamAwayScore();
		
		match.setTeamHomeScore(score.getTeamHomeScore());
		match.setTeamAwayScore(score.getTeamAwayScore());
		
		match.setTeamHomeBonusPoint(score.getTeamHomeBonus());
		match.setTeamAwayBonusPoint(score.getTeamAwayBonus());
		
		match.setTeamHomePenaltyPoint(score.getTeamHomePenalty());
		match.setTeamAwayPenaltyPoint(score.getTeamAwayPenalty());
		
		if(homeScore > awayScore)
		{
			match.setTeamHomePoint(Result.WIN.getPoint());
			match.setTeamAwayPoint(Result.LOSS.getPoint());
		}
		else if (homeScore == awayScore)
		{
			match.setTeamHomePoint(Result.DRAW.getPoint());
			match.setTeamAwayPoint(Result.DRAW.getPoint());
		}
		else
		{
			match.setTeamHomePoint(Result.LOSS.getPoint());
			match.setTeamAwayPoint(Result.WIN.getPoint());
		}
		
		matchRepository.save(match);
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
			log.info(t + "'s remaining opp in current Leg for Round " + (roundIndex + 1) + " is " + opponents
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

						
						match.setMatchDate(round.getRoundDate());
						match.setRound(round);
						match.setTeamHome(homeTeam.getId());
						match.setTeamAway(opponentTeam.getId());
						matchList.add(match);
						matchDayScheduledTeam.add(homeTeam.getName());
						matchDayScheduledTeam.add(opponentTeam.getName());

						log.info(t + "'s opponent fixed is: " + opponentTeam.getName());
						// remove the opponent from leg opponent list and round opponent list
						List<String> remainingOpponentList = new ArrayList<>();
						opponents.forEach(opp -> {
							if(!opponentTeam.getName().equals(opp))
							{
								remainingOpponentList.add(opp);
							}
						});
						
						individualTeamOpponetList.replace(t, remainingOpponentList);

						// also remove home team from opponent's leg opponent list
						List<String> opponentOpponentList = individualTeamOpponetList.get(oponnentTeamName);
						opponentOpponentList.remove(t);
						individualTeamOpponetList.replace(oponnentTeamName, opponentOpponentList);

						perRoundOpponentList.remove(t);
						perRoundOpponentList.remove(oponnentTeamName);

					}

				}
				else
				{
					log.info(t + "'s match has cannot be scheduled for round " + (roundIndex + 1) + ", because either teams opponent list is empty or all opponent match has been scheduler. opponents size " + opponents.size() +", round oppo size : " + perRoundOpponentList.size());
				}
			} else {
				log.info(t + "'s match has already been scheduled for round " + (roundIndex + 1));
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

	@Override
	public List<Match> getAllMatchForRound(Round round) {
		return matchRepository.findByRound(round);
	}



	public List<TeamSummaryView> sortTeamForLeague(List<TeamSummaryView> teamList)
	{
		if(teamList.isEmpty())
		{
			return Collections.emptyList();
		}
		
		List<TeamSummaryView> tmpTeamList = new ArrayList<>();
		teamList.forEach(t-> tmpTeamList.add(t));
		
		Comparator<TeamSummaryView> compareByPoint = Comparator.comparing( TeamSummaryView::getTotalPoints).reversed();
		Comparator<TeamSummaryView> compareByGoalDifference = Comparator.comparing( TeamSummaryView::getGoalDifference).reversed();
		Comparator<TeamSummaryView> compareByGoalForwarded = Comparator.comparing(TeamSummaryView::getGoalScored).reversed();

		Comparator<TeamSummaryView> compareTeam = compareByPoint.thenComparing(compareByGoalDifference).thenComparing(compareByGoalForwarded);

		

		return tmpTeamList.stream().sorted(compareTeam).collect(Collectors.toList());
	}

	@Override
	public List<Match> getAllMatchForRounds(List<Round> rounds) {
		List<Match> matchList = new ArrayList<>();
		
		if(rounds == null || rounds.isEmpty())
			return Collections.emptyList();
		
		rounds.forEach(r ->   matchList.addAll(getAllMatchForRound(r)));
		
		return matchList;
	}

}
