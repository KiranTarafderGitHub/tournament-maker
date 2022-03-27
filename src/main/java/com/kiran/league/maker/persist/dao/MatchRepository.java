package com.kiran.league.maker.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{

}
