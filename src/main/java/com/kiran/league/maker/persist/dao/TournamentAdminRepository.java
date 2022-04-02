package com.kiran.league.maker.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.TournamentAdmin;

@Repository
public interface TournamentAdminRepository extends JpaRepository<TournamentAdmin, Long> {

	TournamentAdmin findByTournamentId(Long tournamentId);
	
	TournamentAdmin findByUserId(Long userId);
	
	
}
