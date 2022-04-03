package com.kiran.league.maker.common.bean.rest;

public class UpdateScorePost {

	private Long matchId;
	
	private Integer teamHomeScore;
	
	private Integer teamAwayScore;
	
	private Integer teamHomeBonus;
	
	private Integer teamAwayBonus;
	
	private Integer teamHomePenalty;
	
	private Integer teamAwayPenalty;

	public UpdateScorePost() {
	}
	
	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public Integer getTeamHomeScore() {
		return teamHomeScore;
	}

	public void setTeamHomeScore(Integer teamHomeScore) {
		this.teamHomeScore = teamHomeScore;
	}

	public Integer getTeamAwayScore() {
		return teamAwayScore;
	}

	public void setTeamAwayScore(Integer teamAwayScore) {
		this.teamAwayScore = teamAwayScore;
	}

	public Integer getTeamHomeBonus() {
		return teamHomeBonus;
	}

	public void setTeamHomeBonus(Integer teamHomeBonus) {
		this.teamHomeBonus = teamHomeBonus;
	}

	public Integer getTeamAwayBonus() {
		return teamAwayBonus;
	}

	public void setTeamAwayBonus(Integer teamAwayBonus) {
		this.teamAwayBonus = teamAwayBonus;
	}

	public Integer getTeamHomePenalty() {
		return teamHomePenalty;
	}

	public void setTeamHomePenalty(Integer teamHomePenalty) {
		this.teamHomePenalty = teamHomePenalty;
	}

	public Integer getTeamAwayPenalty() {
		return teamAwayPenalty;
	}

	public void setTeamAwayPenalty(Integer teamAwayPenalty) {
		this.teamAwayPenalty = teamAwayPenalty;
	}

	@Override
	public String toString() {
		return "UpdateScorePost [matchId=" + matchId + ", teamHomeScore=" + teamHomeScore + ", teamAwayScore="
				+ teamAwayScore + ", teamHomeBonus=" + teamHomeBonus + ", teamAwayBonus=" + teamAwayBonus
				+ ", teamHomePenalty=" + teamHomePenalty + ", teamAwayPenalty=" + teamAwayPenalty + "]";
	}
	
}
