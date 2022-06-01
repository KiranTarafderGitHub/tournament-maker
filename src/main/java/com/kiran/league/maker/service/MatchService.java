package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.common.bean.rest.LeagueTableView;
import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.StatView;
import com.kiran.league.maker.common.bean.rest.TournamentSummaryView;
import com.kiran.league.maker.common.bean.rest.UpdateScorePost;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;

public interface MatchService {
	
	public void createMatchSchedule(List<Team> teams, List<Round> rounds);
	
	public ScheduleView  getScheduleForTournament(Tournament tournament);
	
	public LeagueTableView getLeagueStanding(Tournament tournament);
	
	public void updateScore(UpdateScorePost updateScorePost );

	List<Match> getAllMatchForRound(Round round);
	
	List<Match> getAllMatchForTeam(Long teamId, Long tournamnetId);
	
	List<Match> getAllMatchForRounds(List<Round> rounds);
	
	public void addMatchForNewTeam(Round round, Team team);
	
	public StatView getLeagueStats(Tournament tournament);
	
	public TournamentSummaryView getTournamentSummary(Tournament tournament);
	
	public void deleteMatches(List<Match> matchs);
}
