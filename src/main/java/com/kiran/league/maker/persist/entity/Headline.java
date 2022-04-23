package com.kiran.league.maker.persist.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "headline")
public class Headline {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@ManyToOne
	private Tournament tournament;

	private Date headlineDate;
	
	public Headline() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Date getHeadlineDate() {
		return headlineDate;
	}

	public void setHeadlineDate(Date headlineDate) {
		this.headlineDate = headlineDate;
	}

	@Override
	public String toString() {
		return "Headline [id=" + id + ", description=" + description + ", tournament=" + tournament + ", headlineDate="
				+ headlineDate + "]";
	}
	
}
