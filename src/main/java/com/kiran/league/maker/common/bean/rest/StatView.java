package com.kiran.league.maker.common.bean.rest;

import java.util.List;

public class StatView {
	
	List<TeamStatView> topScorers;
	
	List<TeamStatView> topConceded;
	
	List<TeamStatView> goalPerMatch;
	
	List<TeamStatView> cleansheets;
	
	List<TeamStatView> pointsPerGame;
	
	
	public List<TeamStatView> getTopScorers() {
		return topScorers;
	}

	public void setTopScorers(List<TeamStatView> topScorers) {
		this.topScorers = topScorers;
	}

	public List<TeamStatView> getTopConceded() {
		return topConceded;
	}

	public void setTopConceded(List<TeamStatView> topConceded) {
		this.topConceded = topConceded;
	}

	public List<TeamStatView> getGoalPerMatch() {
		return goalPerMatch;
	}

	public void setGoalPerMatch(List<TeamStatView> goalPerMatch) {
		this.goalPerMatch = goalPerMatch;
	}

	public List<TeamStatView> getCleansheets() {
		return cleansheets;
	}

	public void setCleansheets(List<TeamStatView> cleansheets) {
		this.cleansheets = cleansheets;
	}

	public List<TeamStatView> getPointsPerGame() {
		return pointsPerGame;
	}

	public void setPointsPerGame(List<TeamStatView> pointsPerGame) {
		this.pointsPerGame = pointsPerGame;
	}




	public static class TeamStatView
	{
		Long id;
		
		String name;
		
		Integer goalForwarded;
		
		Integer goalAlloted;
		
		Double goalsPerMatch;
		
		Integer cleanSheet;
		
		Double pointsPerMatch;

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

		public Integer getGoalForwarded() {
			return goalForwarded;
		}

		public void setGoalForwarded(Integer goalForwarded) {
			this.goalForwarded = goalForwarded;
		}

		public Integer getGoalAlloted() {
			return goalAlloted;
		}

		public void setGoalAlloted(Integer goalAlloted) {
			this.goalAlloted = goalAlloted;
		}

		public Double getGoalsPerMatch() {
			return goalsPerMatch;
		}

		public void setGoalsPerMatch(Double goalsPerMatch) {
			this.goalsPerMatch = goalsPerMatch;
		}

		public Integer getCleanSheet() {
			return cleanSheet;
		}

		public void setCleanSheet(Integer cleanSheet) {
			this.cleanSheet = cleanSheet;
		}

		public Double getPointsPerMatch() {
			return pointsPerMatch;
		}

		public void setPointsPerMatch(Double pointsPerMatch) {
			this.pointsPerMatch = pointsPerMatch;
		}
		
	}
}
