package com.kiran.league.maker.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.bean.rest.LeagueTableView;
import com.kiran.league.maker.common.bean.rest.LeagueTableView.TeamSummaryView;
import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.ScheduleView.MatchView;
import com.kiran.league.maker.common.bean.rest.ScheduleView.RoundView;
import com.kiran.league.maker.common.bean.rest.StatView;
import com.kiran.league.maker.common.bean.rest.StatView.TeamStatView;
import com.kiran.league.maker.common.bean.rest.TournamentSummaryView;
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
	public void createMatchSchedule(List<Team> teamList, List<Round> rounds) {

		individualTeamOpponetList.clear();
		teamMap.clear();
		
		int totalTeamCount = teamList.size();
		int totalRoundCount = rounds.size();

		if (totalRoundCount % (totalTeamCount - 1) != 0)
			throw new InvalidDataException("Round and team count mismatch");
		int legCount = totalRoundCount / (totalTeamCount - 1);

		List<Long> teams = teamList.stream().map(t -> t.getId()).collect(Collectors.toList());

		
		List<Match> matches = new ArrayList<>();
		
		
		//List<>
		for (int i = 0; i < legCount; i++) {
			
			List<Long> teamIdList = new ArrayList<>();
			
			teams.forEach(t -> teamIdList.add(t));
			teamIdList.remove(0);
			
			int teamsSize = teamIdList.size();
			int halfSize = totalTeamCount/2;

		    for (int day = 0; day < totalRoundCount; day++)
		    {
		    	
		    	log.info("Matchday===============================> " + (day + 1));
		    	int teamIdx = day % teamsSize;
		    	
		    	Match match = new Match();
		    	Round round = rounds.get(day);
		    	log.info(round);
				
				match.setMatchDate(round.getRoundDate());
				match.setRound(round);
				match.setTeamHome(teamIdList.get(teamIdx));
				match.setTeamAway(teams.get(0));
				matches.add(match);
		        

				log.info(teamIdList.get(teamIdx) + " vs " +  	teams.get(0));
		        

		        for (int idx = 1; idx < halfSize; idx++)
		        {     
		        	
		            int firstTeam = (day + idx) % teamsSize;
		            int secondTeam = (day  + teamsSize - idx) % teamsSize;
		            log.info(teamIdList.get(firstTeam) + " vs " + teamIdList.get(secondTeam));
		            
		            Match match2 = new Match();
			    	
		            match2.setMatchDate(round.getRoundDate());
		            match2.setRound(round);
		            match2.setTeamHome(teamIdList.get(firstTeam));
		            match2.setTeamAway(teamIdList.get(secondTeam));
					
					matches.add(match2);
		        }
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
		//Collections.shuffle(perRoundOpponentList);
		//Collections.shuffle(allTeams);

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
	
	public StatView sortTeamForLeagueStat(StatView statView)
	{
		StatView tmpStatView = new StatView();
		
		List<TeamStatView> tmpCleanSheetList = new ArrayList<>();
		List<TeamStatView> tmpGoalPerMatchList = new ArrayList<>();
		List<TeamStatView> tmpPointsPerGameList = new ArrayList<>();
		List<TeamStatView> tmpTopConceded = new ArrayList<>();
		List<TeamStatView> tmpTopScorersList = new ArrayList<>();

		
		statView.getCleansheets().forEach(t -> tmpCleanSheetList.add(t));
		statView.getGoalPerMatch().forEach(t -> tmpGoalPerMatchList.add(t));
		statView.getPointsPerGame().forEach(t -> tmpPointsPerGameList.add(t));
		statView.getTopConceded().forEach(t -> tmpTopConceded.add(t));
		statView.getTopScorers().forEach(t -> tmpTopScorersList.add(t));
		
		tmpStatView.setCleansheets(tmpCleanSheetList.stream().sorted(Comparator.comparing( TeamStatView::getCleanSheet).reversed()).collect(Collectors.toList()));
		tmpStatView.setGoalPerMatch(tmpGoalPerMatchList.stream().sorted(Comparator.comparing( TeamStatView::getGoalsPerMatch).reversed()).collect(Collectors.toList()));
		tmpStatView.setPointsPerGame(tmpPointsPerGameList.stream().sorted(Comparator.comparing( TeamStatView::getPointsPerMatch).reversed()).collect(Collectors.toList()));
		tmpStatView.setTopConceded(tmpTopConceded.stream().sorted(Comparator.comparing( TeamStatView::getGoalAlloted).reversed()).collect(Collectors.toList()));
		tmpStatView.setTopScorers(tmpTopScorersList.stream().sorted(Comparator.comparing( TeamStatView::getGoalForwarded).reversed()).collect(Collectors.toList()));
		
		return tmpStatView;
	}

	@Override
	public List<Match> getAllMatchForRounds(List<Round> rounds) {
		List<Match> matchList = new ArrayList<>();
		
		if(rounds == null || rounds.isEmpty())
			return Collections.emptyList();
		
		rounds.forEach(r ->   matchList.addAll(getAllMatchForRound(r)));
		
		return matchList;
	}


	@Override
	public void addMatchForNewTeam(Round round, Team team) {
		// TODO Auto-generated method stub
		List<Team> existingTeam = teamService.getAllTeamOfTournament(round.getTournament());
		List<Match> allMatchesForNewTeam = new ArrayList<>();
		
		existingTeam.forEach(et -> {
			
			if(team.getId() != et.getId())
			{
				Match match = new Match();
				match.setTeamHome(team.getId());
				match.setTeamAway(et.getId());
				match.setMatchDate(round.getRoundDate());
				match.setRound(round);
				
				allMatchesForNewTeam.add(match);

			}
		});
		
		matchRepository.saveAllAndFlush(allMatchesForNewTeam);
	}


	@Override
	public StatView getLeagueStats(Tournament tournament) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TournamentSummaryView getTournamentSummary(Tournament tournament) {
		
		TournamentSummaryView tournamentSummaryView = new TournamentSummaryView();
		
		LeagueTableView leagueTableView = new LeagueTableView();
		List<TeamSummaryView> teamSummaryList = new ArrayList<>();

		
		StatView statView = new StatView();
		List<TeamStatView> topScorerList = new ArrayList<>();
		List<TeamStatView> topConcededList = new ArrayList<>();
		List<TeamStatView> goalPerMatchList = new ArrayList<>();
		List<TeamStatView> cleansheetList = new ArrayList<>();
		List<TeamStatView> pointsPerGameList = new ArrayList<>();
		
		
		
		List<Team> allTeam = teamService.getAllTeamOfTournament(tournament);
		for(Team t: allTeam)
		{
			int totalBonus=0;int totalPenalty=0;int totalPoint=0;int matchPlayed=0;int matchWon = 0;
			int matchDraw=0;int matchLost=0;int goalScored = 0;int goalConceded = 0;int goalDifference=0;
			
			int cleanSheet=0;
			
			TeamSummaryView tsView = new TeamSummaryView();
			TeamStatView teamStatView = new TeamStatView();
			
			tsView.setTeamId(t.getId());
			tsView.setTeamName(t.getName());
			
			teamStatView.setId(t.getId());
			teamStatView.setName(t.getName());
			
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
						
						if(m.getTeamAwayScore().intValue() == 0)
							cleanSheet++;
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
						
						if(m.getTeamHomeScore().intValue() == 0)
							cleanSheet++;
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
			
			double goalPerMatch = (double) goalScored / (double) matchPlayed;
			double pointPerMatch = (double) totalPoint / (double) matchPlayed;
			
			goalPerMatch = round(goalPerMatch, 2);
			pointPerMatch = round(pointPerMatch, 2);
			
			teamStatView.setGoalForwarded(goalScored);
			teamStatView.setGoalAlloted(goalConceded);
			teamStatView.setGoalsPerMatch(goalPerMatch);
			teamStatView.setPointsPerMatch(pointPerMatch);
			teamStatView.setCleanSheet(cleanSheet);
			
			topScorerList.add(teamStatView);
			topConcededList.add(teamStatView);
			goalPerMatchList.add(teamStatView);
			cleansheetList.add(teamStatView);
			pointsPerGameList.add(teamStatView);
			
			
		}
		
		
		StatView tmpStatView = new StatView();
		tmpStatView.setCleansheets(cleansheetList);
		tmpStatView.setGoalPerMatch(goalPerMatchList);
		tmpStatView.setPointsPerGame(pointsPerGameList);
		tmpStatView.setTopConceded(topConcededList);
		tmpStatView.setTopScorers(topScorerList);
		
		List<TeamSummaryView> sortedTeamList = sortTeamForLeague(teamSummaryList);
		statView = sortTeamForLeagueStat(tmpStatView);
		
		leagueTableView.setTeams(sortedTeamList);
		
		tournamentSummaryView.setLeagueTableView(leagueTableView);
		tournamentSummaryView.setStatView(statView);
		
		return tournamentSummaryView;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
