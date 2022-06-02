package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Tournament;

public interface RoundService {
	
	List<Round> createTournamentRound(Tournament tournament,int roundCount, int roundIntervalDay);
	
	public List<Round> getRoundsForTournament(Tournament tournament);
	
	public Round addRoundToExistingTournament(Tournament tournament);
	
	public void deleteRounds(List<Round> rounds);
	
	public Round saveRound(Round round);

}
