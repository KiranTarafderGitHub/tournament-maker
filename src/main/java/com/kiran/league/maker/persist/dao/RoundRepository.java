package com.kiran.league.maker.persist.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Tournament;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

	List<Round> findByTournament(Tournament tournament);
	
	@Query(value = "SELECT max(round_number)  FROM round  WHERE tournament_id = :tournamentId",  nativeQuery = true)
	Integer findLastRoundNumberForTournament(@Param("tournamentId") Long tournamentId);
	
	@Query(value = "SELECT max(round_date)  FROM round  WHERE tournament_id = :tournamentId",  nativeQuery = true)
	Date findLastRoundDateForTournament(@Param("tournamentId") Long tournamentId);
}
