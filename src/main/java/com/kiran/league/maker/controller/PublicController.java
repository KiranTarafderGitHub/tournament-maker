package com.kiran.league.maker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kiran.league.maker.common.bean.rest.CodeBean;
import com.kiran.league.maker.common.bean.rest.LeagueTableView;
import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.StatView;
import com.kiran.league.maker.common.bean.rest.TournamentCreate;
import com.kiran.league.maker.common.bean.rest.TournamentSummaryView;
import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.common.exception.NoDataFoundException;
import com.kiran.league.maker.persist.dto.User;
import com.kiran.league.maker.persist.entity.Headline;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.TournamentType;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.service.HeadlineService;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.TournamentAdminService;
import com.kiran.league.maker.service.TournamnetService;

@Controller
@RequestMapping("/public")
public class PublicController {

	
	private static final Log log = LogFactory.getLog(PublicController.class);
	
	@Autowired
	TournamnetService tournamnetService;
	
	@Autowired
	TournamentAdminService tournamentAdminService;
	
	@Autowired
	HeadlineService headlineService;
	
	@Autowired
	MatchService matchService;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Value("${application.url}")
	String applicationUrl;
	
	@Value("${league.view.admin.url}")
	String adminUrl;
	
	@Value("${league.view.user.url}")
	String userUrl;
	
	@Value("${user.admin.default.password}")
	String defaultAdminPassword;
	
	
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
            model.addObject("errorMsg",e.getMessage());
            model.setViewName("error");
        }
        
        
        return model;
    }
	
	@PostMapping("/league/create.html")
    public ModelAndView createLeaguePost(ModelAndView model, @ModelAttribute TournamentCreate tournamentCreate)
    {
		
        try
        {
        	log.info("createLeaguePost called");
        	log.info(tournamentCreate.toString());
        	Tournament tournament = tournamnetService.createNewTournamnet(tournamentCreate);
        	
        	UserEntity user = tournamentAdminService.creteAdminUserForTournament(tournament);
        	
        	String adminAccessUrl = applicationUrl+adminUrl+tournament.getCode();
        	String userAccessUrl = applicationUrl+userUrl+tournament.getCode();
        	
        	model.addObject("adminAccessUrl",adminAccessUrl);
        	model.addObject("userAccessUrl",userAccessUrl);
        	
        	model.addObject("adminUsername",user.getUsername());
        	model.addObject("adminPassword",defaultAdminPassword);
        	model.addObject("successMsg","League Successfully Created");
        	model.addObject("tournament",tournament);
        	
        	model.setViewName("public/league/credential");
        }
        catch(NoDataFoundException e)
        {
        	log.error(e.getMessage(),e);
        	model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        catch(InvalidDataException e)
        {
        	log.error(e.getMessage(),e);
        	model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
            model.setViewName("error");
        }
        
        return model;
    }
	
	@GetMapping("/league/view/{leagueCode}")
    public ModelAndView viewLeague(ModelAndView model, @PathVariable String leagueCode)
    {
		
        try
        {
        	Tournament tournament = new Tournament();
        	TournamentSummaryView tournamentSummaryView = new TournamentSummaryView();
    		ScheduleView scheduleView = new ScheduleView();
    		List<Headline> headlines = new ArrayList<>();
            
            	
        	tournament = tournamnetService.getTournamentByCode(leagueCode);
        	scheduleView = matchService.getScheduleForTournament(tournament);
        	tournamentSummaryView = matchService.getTournamentSummary(tournament);
            headlines = headlineService.getAllHeadline(tournament);
           
            model.addObject("tournament",tournament);
        	model.addObject("scheduleView",scheduleView);
        	model.addObject("leagueView",tournamentSummaryView.getLeagueTableView());
        	model.addObject("statView",tournamentSummaryView.getStatView());
        	model.addObject("tournamentType",TournamentType.values());
        	model.addObject("headlines",headlines);
        	model.setViewName("public/league/view");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        
        return model;
    }
	
	@GetMapping("/league/view.html")
    public ModelAndView findLeague(ModelAndView model)
    {
		
        try
        {
        	model.addObject("codeBean",new CodeBean());
        	model.setViewName("public/league/find");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        
        return model;
    }
	
	@PostMapping("league/show")
    public ModelAndView viewLeaguePost(ModelAndView model, @ModelAttribute CodeBean codeBean)
    {
		try
        {
        	Tournament tournament = new Tournament();
        	TournamentSummaryView tournamentSummaryView = new TournamentSummaryView();
    		ScheduleView scheduleView = new ScheduleView();
    		LeagueTableView leagueView = new LeagueTableView();
    		List<Headline> headlines = new ArrayList<>();
            	
        	tournament = tournamnetService.getTournamentByCode(codeBean.getCode());
        	scheduleView = matchService.getScheduleForTournament(tournament);
        	tournamentSummaryView = matchService.getTournamentSummary(tournament);
            headlines = headlineService.getAllHeadline(tournament);

           
            model.addObject("tournament",tournament);
        	model.addObject("scheduleView",scheduleView);
        	model.addObject("leagueView",tournamentSummaryView.getLeagueTableView());
        	model.addObject("statView",tournamentSummaryView.getStatView());
        	model.addObject("tournamentType",TournamentType.values());
        	model.addObject("headlines",headlines);
        	model.setViewName("public/league/view");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        	model.setViewName("error");
        }
        
        return model;
    }
}
