package com.kiran.league.maker.persist.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "match_lu")
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Round round;

	private Date matchDate;

	private Long teamHome;

	private Long teamAway;

	private Integer teamHomeScore;

	private Integer teamAwayScore;

	private Integer teamHomePoint;

	private Integer teamAwayPoint;

	private Integer teamHomeBonusPoint;

	private Integer teamAwayBonusPoint;

	private Integer teamHomePenaltyPoint;

	private Integer teamAwayPenaltyPoint;

	public Match() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
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

	@Override
	public String toString() {
		return "Match [id=" + id + ", round=" + round + ", matchDate=" + matchDate + ", teamHome=" + teamHome
				+ ", teamAway=" + teamAway + ", teamHomeScore=" + teamHomeScore + ", teamAwayScore=" + teamAwayScore
				+ ", teamHomePoint=" + teamHomePoint + ", teamAwayPoint=" + teamAwayPoint + ", teamHomeBonusPoint="
				+ teamHomeBonusPoint + ", teamAwayBonusPoint=" + teamAwayBonusPoint + ", teamHomePenaltyPoint="
				+ teamHomePenaltyPoint + ", teamAwayPenaltyPoint=" + teamAwayPenaltyPoint + "]";
	}

	
}
