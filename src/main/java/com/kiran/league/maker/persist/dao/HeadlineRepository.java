package com.kiran.league.maker.persist.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Headline;
import com.kiran.league.maker.persist.entity.Tournament;

@Repository
public interface HeadlineRepository extends JpaRepository<Headline, Long>{

	List<Headline> findByTournamentOrderByHeadlineDateDesc(Tournament tournament);
}
