package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.bean.BackupBean;
import com.kiran.league.maker.common.bean.OldNewMapper;
import com.kiran.league.maker.common.bean.rest.TournamentCreate;
import com.kiran.league.maker.common.bean.rest.UserBean;
import com.kiran.league.maker.common.exception.NoDataFoundException;
import com.kiran.league.maker.common.util.RandomGenerator;
import com.kiran.league.maker.persist.dao.TournamentRepository;
import com.kiran.league.maker.persist.dto.User;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.TournamentType;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.RoundService;
import com.kiran.league.maker.service.TeamService;
import com.kiran.league.maker.service.TournamnetService;
import com.kiran.league.maker.service.UserService;

@Service
public class TournamnetServiceImpl implements TournamnetService {

	private static final Log log = LogFactory.getLog(TournamnetService.class);

	@Autowired
	TeamService teamService;

	@Autowired
	TournamentRepository tournamentRepository;
	
	@Autowired
	MatchService matchService;

	@Autowired
	RoundService roundService;
	
	@Autowired
	UserService userService;

	@Override
	public Tournament createNewTournamnet(TournamentCreate tournamentCreate) {
		
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
			completeTeamArray[i] = teamArray[i].trim();
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
		
		List<Round> rounds = roundService.createTournamentRound(tournament, roundCount, tournamentCreate.getRoundIntervalDay());
		
		matchService.createMatchSchedule(teamList, rounds);
		
		return tournament;
	}

	@Override
	public Tournament getTournamentByCode(String code) {
		Tournament tournament = tournamentRepository.findByCode(code);
		if(tournament == null)
			throw new NoDataFoundException("No tournament found for code: " + code);
		
		return tournament;
	}

	@Override
	public Tournament getTournamentById(Long id) {
		Tournament tournament = tournamentRepository.getById(id);
		if(tournament == null)
			throw new NoDataFoundException("No tournament found for id: " + id);
		
		return tournament;
	}

	@Override
	public Tournament addTeamToExistingTournament(Tournament tournament, String teamName) {
		
		List<String> teamNames = new ArrayList<>();
		teamNames.add(teamName);
		
		
		List<Team> newTeamList = teamService.createTeam(teamNames);
		if(TournamentType.RR1L.name().equals(tournament.getTournamentType()))
		{
			Round round = roundService.addRoundToExistingTournament(tournament);
			matchService.addMatchForNewTeam(round, newTeamList.get(0));
		}
		else if(TournamentType.RR2L.name().equals(tournament.getTournamentType()))
		{
			Round round = roundService.addRoundToExistingTournament(tournament);
			matchService.addMatchForNewTeam(round, newTeamList.get(0));
			
			Round round2 = roundService.addRoundToExistingTournament(tournament);
			matchService.addMatchForNewTeam(round2, newTeamList.get(0));
		}
		
		return tournament;
	}

	@Override
	public Tournament saveTournament(Tournament tournament) {
		
		return tournamentRepository.save(tournament);
		
	}

	@Override
	public Tournament restoreTournament(BackupBean backupBean) {
		
		clearPastTournamentData(backupBean);
		
		return createTournamentFromBackup(backupBean);
	}
	
	public Tournament createTournamentFromBackup(BackupBean backupBean)
	{
		
		
		//create two set of data, one is old data of tournamnet and other is new data 
		OldNewMapper<Tournament, Tournament> tournamnetHolder = new OldNewMapper<>();
		tournamnetHolder.setOldClass(backupBean.getTournament());
		tournamnetHolder.setNewClass(new Tournament());
		
		Map<Long, OldNewMapper<Round, Round>> roundHolderMap = new HashMap<>();
		backupBean.getRounds().forEach(r -> roundHolderMap.put(r.getId(), new OldNewMapper(r,new Round())));
		
		Map<Long, OldNewMapper<Team, Team>> teamHolderMap = new HashMap<>();
		backupBean.getTeams().forEach(t -> teamHolderMap.put(t.getId(), new OldNewMapper(t, null)));
		
		OldNewMapper<UserBean, UserBean> userHolder = new OldNewMapper<>();
		userHolder.setOldClass(backupBean.getAdminUser());
		userHolder.setNewClass( new UserBean());
		
		Map<Long, OldNewMapper<Match, Match>> matchHolderMap = new HashMap<>();
		backupBean.getMatches().forEach(m -> matchHolderMap.put(m.getId(), new OldNewMapper(m,null)));
		
		//create new tournament
		Tournament tournament = new Tournament();
		
		tournament.setAdminEmail(backupBean.getTournament().getAdminEmail());
		tournament.setCode(backupBean.getTournament().getCode());
		tournament.setName(backupBean.getTournament().getName());
		tournament.setStandingColor(backupBean.getTournament().getStandingColor());
		tournament.setTournamentType(backupBean.getTournament().getTournamentType());
		
		tournamentRepository.saveAndFlush(tournament);
		
		backupBean.getTeams().forEach(t -> {
			Team team = new Team();
			team.setCode(t.getCode());
			team.setName(t.getName());
			
			team = teamService.saveTeam(team);
			
			OldNewMapper<Team, Team> tmpTeam = teamHolderMap.get(t.getId());
			tmpTeam.setNewClass(team);
		});
		
		
		backupBean.getRounds().forEach(r -> {
			Round round = new Round();
			round.setName(r.getName());
			round.setRoundNumber(r.getRoundNumber());
			round.setTournament(tournament);
			round.setRoundDate(r.getRoundDate());
			
			round = roundService.saveRound(round);
			
			OldNewMapper<Round, Round> tmpRound = roundHolderMap.get(r.getId());
			tmpRound.setNewClass(round);
		});
		
		backupBean.getMatches().forEach(m -> {
			Match match = new Match();
			
			Round round;
			OldNewMapper<Round, Round> newRoundMap = roundHolderMap.get(m.getRound().getId());
			round = newRoundMap.getNewClass();
			match.setRound(round);
			
			match.setMatchDate(m.getMatchDate());
			
			Team teamHome;
			OldNewMapper<Team, Team> newTeamHomeMap = teamHolderMap.get(m.getTeamHome());
			teamHome = newTeamHomeMap.getNewClass();
			match.setTeamHome(teamHome.getId());
			
			Team teamAway;
			OldNewMapper<Team, Team> newTeamAwayMap = teamHolderMap.get(m.getTeamAway());
			teamAway = newTeamAwayMap.getNewClass();
			match.setTeamAway(teamAway.getId());
			
			match.setTeamHomeScore(m.getTeamHomeScore());
			match.setTeamAwayScore(m.getTeamAwayScore());
			match.setTeamHomePoint(m.getTeamHomePoint());
			match.setTeamAwayPoint(m.getTeamAwayPoint());
			match.setTeamHomeBonusPoint(m.getTeamHomeBonusPoint());
			match.setTeamAwayBonusPoint(m.getTeamAwayBonusPoint());
			match.setTeamHomePenaltyPoint(m.getTeamHomePenaltyPoint());
			match.setTeamAwayPenaltyPoint(m.getTeamAwayPenaltyPoint());
			
			matchService.saveMatch(match);
			
		});
		
		return tournament;
		
	}
	
	public void clearPastTournamentData(BackupBean backupBean)
	{
		
		//find if any previous tournament with same code exists or not
		Tournament prevTournament = tournamentRepository.findByCode(backupBean.getTournament().getCode());
		
		if(prevTournament != null)
		{
			//1. delete all matches from db
			matchService.deleteMatches(backupBean.getMatches());
			
			//2. delete all rounds
			roundService.deleteRounds(backupBean.getRounds());
			
			//3. delete all teams
			teamService.deleteTeams(backupBean.getTeams());
			
			try {
				//4. delete user
				User pastAdminUser = userService.getUserByUsername(backupBean.getAdminUser().getUsername());
				if( pastAdminUser != null)
				{
					userService.deleteUserById(pastAdminUser.getId());
				}
				
			} catch (Exception e) {
				log.warn("Error deleting user for the past tournament. " + e.getMessage());
			}
			
			//5. delete tournament
			tournamentRepository.deleteByCode(prevTournament.getCode());
		}
		
		
	}

}
