package com.kiran.league.maker.persist.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

	Tournament findByCode(String code);
	
	@Transactional
	Long deleteByCode(String code);
}
