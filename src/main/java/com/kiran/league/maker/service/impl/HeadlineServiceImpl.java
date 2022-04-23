package com.kiran.league.maker.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.persist.dao.HeadlineRepository;
import com.kiran.league.maker.persist.entity.Headline;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.service.HeadlineService;

@Service
public class HeadlineServiceImpl implements HeadlineService {

	private static final Log log = LogFactory.getLog(HeadlineService.class);
	
	@Autowired
	HeadlineRepository headlineRepository;
	
	@Override
	public List<Headline> getAllHeadline(Tournament tournament) {
		List<Headline> headlines = headlineRepository.findByTournamentOrderByHeadlineDateDesc(tournament);
		return CollectionUtils.isNotEmpty(headlines)? headlines: Collections.EMPTY_LIST;
	}

	@Override
	public void saveHeadline(Headline headline) {
		
		headline.setHeadlineDate(new Date());
		headlineRepository.save(headline);
	}

	@Override
	public void deleteHeadline(Long headlineId) {
		headlineRepository.deleteById(headlineId);
	}

}
