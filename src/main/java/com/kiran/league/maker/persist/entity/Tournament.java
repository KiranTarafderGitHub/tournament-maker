package com.kiran.league.maker.persist.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tournament")
public class Tournament {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	private String name;

	private String tournamentType;
	
	private String adminEmail;
	
	private String standingColor;

	public Tournament() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	
	public String getStandingColor() {
		return standingColor;
	}

	public void setStandingColor(String standingColor) {
		this.standingColor = standingColor;
	}

	@Override
	public String toString() {
		return "Tournament [id=" + id + ", code=" + code + ", name=" + name + ", tournamentType=" + tournamentType
				+ ", adminEmail=" + adminEmail + "]";
	}
	
}
