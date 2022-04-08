package com.kiran.league.maker.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kiran.league.maker.common.bean.rest.LeagueTableView;
import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.UpdateScorePost;
import com.kiran.league.maker.persist.dto.User;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.TournamentType;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.TournamentAdminService;

@Controller
@RequestMapping("/admin")
public class LeagueAdminController {

	private static final Log log = LogFactory.getLog(LeagueAdminController.class);

	@Autowired
	TournamentAdminService tournamentAdminService;
	
	@Autowired
	MatchService matchService;
	
	@Value("${application.url}")
	String applicationUrl;
	
	
	@GetMapping("/league/{leagueCode}")
    public ModelAndView createLeague(Principal principal, ModelAndView model, @PathVariable String leagueCode)
    {
		
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            UserEntity user = ((UserEntity) _principal.getPrincipal());
            
        	Tournament tournament = tournamentAdminService.getTournamentForUser(user);
        	if(!leagueCode.equals(tournament.getCode()))
        	{
        		model.addObject("errorMsg","Invalid League Code Provided");
                model.setViewName("admin/view");
                return model;
        	}
        	
        	//TournamentCreate tournament = new TournamentCreate();
        	model.addObject("tournament",tournament);
        	model.addObject("tournamentType",TournamentType.values());
            model.setViewName("admin/view");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.setViewName("error");
        }
        
        return model;
    }
	
	@GetMapping("/view/league.html")
    public ModelAndView viewLeague(Principal principal, ModelAndView model)
    {
		Tournament tournament = new Tournament();
		ScheduleView scheduleView = new ScheduleView();
		LeagueTableView leagueView = new LeagueTableView();
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
        	scheduleView = matchService.getScheduleForTournament(tournament);
        	leagueView = matchService.getLeagueStanding(tournament);
        	
        	
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        model.addObject("tournament",tournament);
    	model.addObject("scheduleView",scheduleView);
    	model.addObject("leagueView",leagueView);
        model.setViewName("admin/view");
        
        return model;
    }
	
	@GetMapping("/update/score.html")
    public ModelAndView getUpdateScore(Principal principal, ModelAndView model)
    {
		Tournament tournament = new Tournament();
		Map<Round, List<Match>> roundMatches = new HashMap<>();
		ScheduleView scheduleView = new ScheduleView();
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
        	//get match schedule
        	scheduleView = matchService.getScheduleForTournament(tournament);
        
        	
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        model.addObject("updateScore",new UpdateScorePost());
        model.addObject("tournament",tournament);
    	model.addObject("scheduleView",scheduleView);
        model.setViewName("admin/score");
        
        return model;
    }
	
	@PostMapping("/update/score")
	public ModelAndView updateMatchScore(Principal principal, ModelAndView model, @ModelAttribute UpdateScorePost updateScore)
	{
		log.info(updateScore.toString());
		try
		{
			matchService.updateScore(updateScore);
			model.addObject("navigateTo",updateScore.getMatchId());
			model.addObject("successMsg","Match Score successfully updated");
			
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
		}
		
		return getUpdateScore(principal,model);
	}
	
	
	
}
