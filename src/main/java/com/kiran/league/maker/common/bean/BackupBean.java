package com.kiran.league.maker.common.bean;

import java.util.List;

import com.kiran.league.maker.common.bean.rest.UserBean;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;

public class BackupBean {
	
	Tournament tournament;
	
	List<Round> rounds;
	
	List<Match> matches;
	
	List<Team> teams;
	
	UserBean adminUser;

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public UserBean getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(UserBean adminUser) {
		this.adminUser = adminUser;
	}

	@Override
	public String toString() {
		return "BackupBean [tournament=" + tournament + ", rounds=" + rounds + ", matches=" + matches + ", teams="
				+ teams + ", adminUser=" + adminUser + "]";
	}
}
