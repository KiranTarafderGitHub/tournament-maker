package com.kiran.league.maker.common.bean.rest;

public class TournamentSummaryView {
	
	LeagueTableView leagueTableView;
	
	StatView statView;

	public LeagueTableView getLeagueTableView() {
		return leagueTableView;
	}

	public void setLeagueTableView(LeagueTableView leagueTableView) {
		this.leagueTableView = leagueTableView;
	}

	public StatView getStatView() {
		return statView;
	}

	public void setStatView(StatView statView) {
		this.statView = statView;
	}

	@Override
	public String toString() {
		return "TournamentSummaryView [leagueTableView=" + leagueTableView + ", statView=" + statView + "]";
	}
	
}
