package com.kiran.league.maker.common.bean.rest;

import java.util.List;

public class LeagueTableView {

	private List<TeamSummaryView> teams;

	public List<TeamSummaryView> getTeams() {
		return teams;
	}

	public void setTeams(List<TeamSummaryView> teams) {
		this.teams = teams;
	}

	@Override
	public String toString() {
		return "LeagueTableView [teams=" + teams + "]";
	}

	public static class TeamSummaryView {
		
		private Long teamId;

		private String teamName;

		private Integer matchPlayed;

		private Integer matchWon;

		private Integer matchDraw;

		private Integer matchLost;

		private Integer goalScored;

		private Integer goalConceded;

		private Integer goalDifference;

		private Integer totalBonusPoint;

		private Integer totalPenaltyPoint;

		private Integer totalPoints;

		private String standingColor;

		public TeamSummaryView() {
		}

		public Long getTeamId() {
			return teamId;
		}

		public void setTeamId(Long teamId) {
			this.teamId = teamId;
		}

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public Integer getMatchPlayed() {
			return matchPlayed;
		}

		public void setMatchPlayed(Integer matchPlayed) {
			this.matchPlayed = matchPlayed;
		}

		public Integer getMatchWon() {
			return matchWon;
		}

		public void setMatchWon(Integer matchWon) {
			this.matchWon = matchWon;
		}

		public Integer getMatchDraw() {
			return matchDraw;
		}

		public void setMatchDraw(Integer matchDraw) {
			this.matchDraw = matchDraw;
		}

		public Integer getMatchLost() {
			return matchLost;
		}

		public void setMatchLost(Integer matchLost) {
			this.matchLost = matchLost;
		}

		public Integer getGoalScored() {
			return goalScored;
		}

		public void setGoalScored(Integer goalScored) {
			this.goalScored = goalScored;
		}

		public Integer getGoalConceded() {
			return goalConceded;
		}

		public void setGoalConceded(Integer goalConceded) {
			this.goalConceded = goalConceded;
		}

		public Integer getGoalDifference() {
			return goalDifference;
		}

		public void setGoalDifference(Integer goalDifference) {
			this.goalDifference = goalDifference;
		}

		public Integer getTotalBonusPoint() {
			return totalBonusPoint;
		}

		public void setTotalBonusPoint(Integer totalBonusPoint) {
			this.totalBonusPoint = totalBonusPoint;
		}

		public Integer getTotalPenaltyPoint() {
			return totalPenaltyPoint;
		}

		public void setTotalPenaltyPoint(Integer totalPenaltyPoint) {
			this.totalPenaltyPoint = totalPenaltyPoint;
		}

		public Integer getTotalPoints() {
			return totalPoints;
		}

		public void setTotalPoints(Integer totalPoints) {
			this.totalPoints = totalPoints;
		}

		public String getStandingColor() {
			return standingColor;
		}

		public void setStandingColor(String standingColor) {
			this.standingColor = standingColor;
		}

		@Override
		public String toString() {
			return "TeamSummaryView [teamId=" + teamId + ", teamName=" + teamName + ", matchPlayed=" + matchPlayed
					+ ", matchWon=" + matchWon + ", matchDraw=" + matchDraw + ", matchLost=" + matchLost
					+ ", goalScored=" + goalScored + ", goalConceded=" + goalConceded + ", goalDifference="
					+ goalDifference + ", totalBonusPoint=" + totalBonusPoint + ", totalPenaltyPoint="
					+ totalPenaltyPoint + ", totalPoints=" + totalPoints + "]";
		}

	}
}
