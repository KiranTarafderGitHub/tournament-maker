package com.kiran.league.maker.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
