package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	public List<Round> createTournamentRound(Tournament tournament, int roundCount,int roundIntervalDay) {
		
		List<Round> rounds = new ArrayList<>();
		for(int i = 1 ; i <= roundCount; i++)
		{
			Round round = new Round();
			
			round.setName("Round " + i);
			round.setRoundNumber(i);
			round.setTournament(tournament);
			int roundIntervalInDay = roundIntervalDay > 0 ? roundIntervalDay : 7;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, (roundIntervalInDay * (i-1)) );
			round.setRoundDate(cal.getTime());
			rounds.add(round);
		}
		
		return roundRepository.saveAllAndFlush(rounds);
		
	}

	@Override
	public List<Round> getRoundsForTournament(Tournament tournament) {
		return roundRepository.findByTournament(tournament);
	}

	@Override
	public Round addRoundToExistingTournament(Tournament tournament) {
		
		Integer maxRound = roundRepository.findLastRoundNumberForTournament(tournament.getId());
		Date lastRoundDate = roundRepository.findLastRoundDateForTournament(tournament.getId());
		
		Round round = new Round();
		int roundNumber = maxRound + 1;
		round.setName("Round " + roundNumber);
		round.setRoundNumber(roundNumber);
		round.setRoundDate(lastRoundDate);
		round.setTournament(tournament);
		
		roundRepository.saveAndFlush(round);
		
		return round;
	}

	@Override
	public void deleteRounds(List<Round> rounds) {
		rounds.forEach(r -> {
			Optional<Round> optionalRound = roundRepository.findById(r.getId());
			if(optionalRound.isPresent())
				roundRepository.delete(r);
		});
	}

	@Override
	public Round saveRound(Round round) {
		return roundRepository.saveAndFlush(round);
	}

}
