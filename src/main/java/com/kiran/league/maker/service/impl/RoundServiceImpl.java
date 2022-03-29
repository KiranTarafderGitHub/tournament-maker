package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.persist.dao.RoundRepository;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.service.RoundService;

@Service
public class RoundServiceImpl implements RoundService {

	
	@Autowired
	RoundRepository roundRepository;
	
	@Override
	public List<Round> createTournamentRound(Tournament tournament, int roundCount) {
		
		List<Round> rounds = new ArrayList<>();
		for(int i = 1 ; i <= roundCount; i++)
		{
			Round round = new Round();
			round.setName("Round " + i);
			round.setTournament(tournament);
			rounds.add(round);
		}
		
		return roundRepository.saveAllAndFlush(rounds);
		
	}

}
