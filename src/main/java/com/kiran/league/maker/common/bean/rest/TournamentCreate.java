package com.kiran.league.maker.common.bean.rest;

public class TournamentCreate {

	String name;
	
	String tournamentType;
	
	String teams;
	
	String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTournamentType() {
		return tournamentType;
	}

	public void setTournamentType(String tournamentType) {
		this.tournamentType = tournamentType;
	}

	public String getTeams() {
		return teams;
	}

	public void setTeams(String teams) {
		this.teams = teams;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "TournamentCreate [name=" + name + ", tournamentType=" + tournamentType + ", teams=" + teams + ", email="
				+ email + "]";
	}
	
	
}
