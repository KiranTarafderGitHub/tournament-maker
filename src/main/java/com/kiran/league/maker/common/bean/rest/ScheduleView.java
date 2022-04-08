package com.kiran.league.maker.common.bean.rest;

import java.util.Date;
import java.util.List;

public class ScheduleView {

	List<RoundView> rounds;

	public List<RoundView> getRounds() {
		return rounds;
	}

	public void setRounds(List<RoundView> rounds) {
		this.rounds = rounds;
	}
	
	public void addRound(RoundView r)
	{
		this.rounds.add(r);
	}

	public static class RoundView {
		
		private Long id;

		private String name;

		private Integer roundNumber;

		List<MatchView> matches;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getRoundNumber() {
			return roundNumber;
		}

		public void setRoundNumber(Integer roundNumber) {
			this.roundNumber = roundNumber;
		}

		public List<MatchView> getMatches() {
			return matches;
		}

		public void setMatches(List<MatchView> matches) {
			this.matches = matches;
		}
		
	}

	public static class MatchView {
		
		private Long id;

		private Date matchDate;

		private Long teamHome;

		private Long teamAway;
		
		private String teamHomeName;
		
		private String teamAwayName;
		
		private String teamHomeCode;
		
		private String teamAwayCode;

		private Integer teamHomeScore;

		private Integer teamAwayScore;

		private Integer teamHomePoint;

		private Integer teamAwayPoint;

		private Integer teamHomeBonusPoint;

		private Integer teamAwayBonusPoint;

		private Integer teamHomePenaltyPoint;

		private Integer teamAwayPenaltyPoint;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Date getMatchDate() {
			return matchDate;
		}

		public void setMatchDate(Date matchDate) {
			this.matchDate = matchDate;
		}

		public Long getTeamHome() {
			return teamHome;
		}

		public void setTeamHome(Long teamHome) {
			this.teamHome = teamHome;
		}

		public Long getTeamAway() {
			return teamAway;
		}

		public void setTeamAway(Long teamAway) {
			this.teamAway = teamAway;
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

		public Integer getTeamHomePoint() {
			return teamHomePoint;
		}

		public void setTeamHomePoint(Integer teamHomePoint) {
			this.teamHomePoint = teamHomePoint;
		}

		public Integer getTeamAwayPoint() {
			return teamAwayPoint;
		}

		public void setTeamAwayPoint(Integer teamAwayPoint) {
			this.teamAwayPoint = teamAwayPoint;
		}

		public Integer getTeamHomeBonusPoint() {
			return teamHomeBonusPoint;
		}

		public void setTeamHomeBonusPoint(Integer teamHomeBonusPoint) {
			this.teamHomeBonusPoint = teamHomeBonusPoint;
		}

		public Integer getTeamAwayBonusPoint() {
			return teamAwayBonusPoint;
		}

		public void setTeamAwayBonusPoint(Integer teamAwayBonusPoint) {
			this.teamAwayBonusPoint = teamAwayBonusPoint;
		}

		public Integer getTeamHomePenaltyPoint() {
			return teamHomePenaltyPoint;
		}

		public void setTeamHomePenaltyPoint(Integer teamHomePenaltyPoint) {
			this.teamHomePenaltyPoint = teamHomePenaltyPoint;
		}

		public Integer getTeamAwayPenaltyPoint() {
			return teamAwayPenaltyPoint;
		}

		public void setTeamAwayPenaltyPoint(Integer teamAwayPenaltyPoint) {
			this.teamAwayPenaltyPoint = teamAwayPenaltyPoint;
		}

		public String getTeamHomeName() {
			return teamHomeName;
		}

		public void setTeamHomeName(String teamHomeName) {
			this.teamHomeName = teamHomeName;
		}

		public String getTeamAwayName() {
			return teamAwayName;
		}

		public void setTeamAwayName(String teamAwayName) {
			this.teamAwayName = teamAwayName;
		}

		public String getTeamHomeCode() {
			return teamHomeCode;
		}

		public void setTeamHomeCode(String teamHomeCode) {
			this.teamHomeCode = teamHomeCode;
		}

		public String getTeamAwayCode() {
			return teamAwayCode;
		}

		public void setTeamAwayCode(String teamAwayCode) {
			this.teamAwayCode = teamAwayCode;
		}

		@Override
		public String toString() {
			return "MatchView [id=" + id + ", matchDate=" + matchDate + ", teamHome=" + teamHome + ", teamAway="
					+ teamAway + ", teamHomeName=" + teamHomeName + ", teamAwayName=" + teamAwayName + ", teamHomeCode="
					+ teamHomeCode + ", teamAwayCode=" + teamAwayCode + ", teamHomeScore=" + teamHomeScore
					+ ", teamAwayScore=" + teamAwayScore + ", teamHomePoint=" + teamHomePoint + ", teamAwayPoint="
					+ teamAwayPoint + ", teamHomeBonusPoint=" + teamHomeBonusPoint + ", teamAwayBonusPoint="
					+ teamAwayBonusPoint + ", teamHomePenaltyPoint=" + teamHomePenaltyPoint + ", teamAwayPenaltyPoint="
					+ teamAwayPenaltyPoint + "]";
		}
		
		
		
	}
}
