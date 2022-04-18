package com.kiran.league.maker.common.bean;

public class MiniMatch {
	Long teamHomeId;
	Long teamAwayId;
	
	public MiniMatch() {
	}
	
	public 	MiniMatch(Long teamHomeId, Long teamAwayId) {
		this.teamHomeId = teamHomeId;
		this.teamAwayId = teamAwayId;
	}
	

	public Long getTeamHomeId() {
		return teamHomeId;
	}

	public void setTeamHomeId(Long teamHomeId) {
		this.teamHomeId = teamHomeId;
	}

	public Long getTeamAwayId() {
		return teamAwayId;
	}

	public void setTeamAwayId(Long teamAwayId) {
		this.teamAwayId = teamAwayId;
	}
	
	public boolean isContainsTeams(Long teamHomeId, Long teamAwayId)
	{
		if(teamHomeId.intValue() == this.teamHomeId && teamAwayId.intValue() == this.teamAwayId)
			return true;
		else
			return false;
	}
	
	public boolean isContainsTeam(Long teamId)
	{
		if(teamId.intValue() == this.teamHomeId || teamId.intValue() == this.teamAwayId)
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return " [" + teamHomeId + ", " + teamAwayId + "]";
	}
	
}
