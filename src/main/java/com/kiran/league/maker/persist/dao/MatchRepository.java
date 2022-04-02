package com.kiran.league.maker.persist.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{

	List<Match> findByRound(Round round);
}
