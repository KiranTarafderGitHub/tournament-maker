package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.persist.entity.Headline;
import com.kiran.league.maker.persist.entity.Tournament;

public interface HeadlineService {

	List<Headline> getAllHeadline(Tournament tournament);
	
	public void saveHeadline(Headline headline);
	
	public void deleteHeadline(Long headlineId);
}
