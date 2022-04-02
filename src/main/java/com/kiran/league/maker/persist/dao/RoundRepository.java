package com.kiran.league.maker.persist.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Tournament;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {

	List<Round> findByTournament(Tournament tournament);
}
