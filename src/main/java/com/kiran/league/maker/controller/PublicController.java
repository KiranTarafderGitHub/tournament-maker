package com.kiran.league.maker.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kiran.league.maker.common.bean.rest.TournamentCreate;
import com.kiran.league.maker.persist.entity.TournamentType;

@Controller
@RequestMapping("/public")
public class PublicController {

	
	private static final Log log = LogFactory.getLog(PublicController.class);
	
	@GetMapping("/league/create.html")
    public ModelAndView createLeague(ModelAndView model)
    {
		
        try
        {
        	TournamentCreate tournament = new TournamentCreate();
        	model.addObject("tournament",tournament);
        	model.addObject("tournamentType",TournamentType.values());
        	model.setViewName("public/league/create");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.setViewName("error");
        }
        
        
        return model;
    }
	
	@PostMapping("/league/create.html")
    public ModelAndView createLeaguePost(ModelAndView model, @ModelAttribute TournamentCreate tournament)
    {
		
        try
        {
        	log.info("createLeaguePost called");
        	log.info(tournament.toString());
        	
        	
        	model.addObject("tournament",tournament);
        	model.setViewName("public/league/create");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        
        return model;
    }
}
