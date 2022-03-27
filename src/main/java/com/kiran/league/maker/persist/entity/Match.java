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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private Round round;

	private Date matchDate;

	private Long teamA;

	private Long teamB;

	private Integer teamAScore;

	private Integer teamBScore;

	private Integer teamAPoint;

	private Integer teamBPoint;

	private Integer teamABonusPoint;

	private Integer teamBBonusPoint;

	private Integer teamAPenaltyPoint;

	private Integer teamBPenaltyPoint;

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

	public Long getTeamA() {
		return teamA;
	}

	public void setTeamA(Long teamA) {
		this.teamA = teamA;
	}

	public Long getTeamB() {
		return teamB;
	}

	public void setTeamB(Long teamB) {
		this.teamB = teamB;
	}

	public Integer getTeamAScore() {
		return teamAScore;
	}

	public void setTeamAScore(Integer teamAScore) {
		this.teamAScore = teamAScore;
	}

	public Integer getTeamBScore() {
		return teamBScore;
	}

	public void setTeamBScore(Integer teamBScore) {
		this.teamBScore = teamBScore;
	}

	public Integer getTeamAPoint() {
		return teamAPoint;
	}

	public void setTeamAPoint(Integer teamAPoint) {
		this.teamAPoint = teamAPoint;
	}

	public Integer getTeamBPoint() {
		return teamBPoint;
	}

	public void setTeamBPoint(Integer teamBPoint) {
		this.teamBPoint = teamBPoint;
	}

	public Integer getTeamABonusPoint() {
		return teamABonusPoint;
	}

	public void setTeamABonusPoint(Integer teamABonusPoint) {
		this.teamABonusPoint = teamABonusPoint;
	}

	public Integer getTeamBBonusPoint() {
		return teamBBonusPoint;
	}

	public void setTeamBBonusPoint(Integer teamBBonusPoint) {
		this.teamBBonusPoint = teamBBonusPoint;
	}

	public Integer getTeamAPenaltyPoint() {
		return teamAPenaltyPoint;
	}

	public void setTeamAPenaltyPoint(Integer teamAPenaltyPoint) {
		this.teamAPenaltyPoint = teamAPenaltyPoint;
	}

	public Integer getTeamBPenaltyPoint() {
		return teamBPenaltyPoint;
	}

	public void setTeamBPenaltyPoint(Integer teamBPenaltyPoint) {
		this.teamBPenaltyPoint = teamBPenaltyPoint;
	}

	@Override
	public String toString() {
		return "Match [id=" + id + ", round=" + round + ", matchDate=" + matchDate + ", teamA=" + teamA + ", teamB="
				+ teamB + ", teamAScore=" + teamAScore + ", teamBScore=" + teamBScore + ", teamAPoint=" + teamAPoint
				+ ", teamBPoint=" + teamBPoint + ", teamABonusPoint=" + teamABonusPoint + ", teamBBonusPoint="
				+ teamBBonusPoint + ", teamAPenaltyPoint=" + teamAPenaltyPoint + ", teamBPenaltyPoint="
				+ teamBPenaltyPoint + "]";
	}

	
}
