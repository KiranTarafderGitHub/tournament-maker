package com.kiran.league.maker.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiran.league.maker.common.bean.BackupBean;
import com.kiran.league.maker.common.bean.rest.LeagueTableView;
import com.kiran.league.maker.common.bean.rest.ScheduleView;
import com.kiran.league.maker.common.bean.rest.UpdateScorePost;
import com.kiran.league.maker.persist.dto.User;
import com.kiran.league.maker.persist.entity.Headline;
import com.kiran.league.maker.persist.entity.Match;
import com.kiran.league.maker.persist.entity.Round;
import com.kiran.league.maker.persist.entity.Team;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.service.HeadlineService;
import com.kiran.league.maker.service.MatchService;
import com.kiran.league.maker.service.RoundService;
import com.kiran.league.maker.service.TeamService;
import com.kiran.league.maker.service.TournamentAdminService;
import com.kiran.league.maker.service.TournamnetService;

@Controller
@RequestMapping("/admin")
public class LeagueAdminController {

	private static final Log log = LogFactory.getLog(LeagueAdminController.class);

	@Autowired
	TournamentAdminService tournamentAdminService;
	
	@Autowired
	MatchService matchService;
	
	@Autowired
	RoundService roundService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	HeadlineService headlineService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	TournamnetService tournamnetService;
	
	@Value("${application.url}")
	String applicationUrl;
	
	
	@GetMapping("/league/{leagueCode}")
    public ModelAndView createLeague(Principal principal, ModelAndView model, @PathVariable String leagueCode)
    {
		
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	Tournament tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
    		ScheduleView scheduleView = new ScheduleView();
    		LeagueTableView leagueView = new LeagueTableView();
        	if(!leagueCode.equals(tournament.getCode()))
        	{
        		model.addObject("errorMsg","Invalid League Code Provided");
                model.setViewName("admin/view");
                return model;
        	}
        	
        	
        	scheduleView = matchService.getScheduleForTournament(tournament);
        	leagueView = matchService.getLeagueStanding(tournament);
        	
        	model.addObject("tournament",tournament);
        	model.addObject("scheduleView",scheduleView);
        	model.addObject("leagueView",leagueView);
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
	
	@GetMapping("/backup/create")
	public ResponseEntity<Resource> createBackup(Principal principal, ModelAndView model, HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	Tournament tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
    		List<Round> rounds = roundService.getRoundsForTournament(tournament);
    		List<Match> matches = matchService.getAllMatchForRounds(rounds);
    		List<Team> teams = teamService.getAllTeamOfTournament(tournament);
    		
    		BackupBean backupBean = new BackupBean();
    		
    		backupBean.setTournament(tournament);
    		backupBean.setRounds(rounds);
    		backupBean.setTeams(teams);
    		backupBean.setMatches(matches);
    		backupBean.setAdminUser(user);
    		
    		String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(backupBean);
    		
    	    File tmpFile = File.createTempFile("league-backup", ".json");
    	    FileWriter writer = new FileWriter(tmpFile);
    	    writer.write(jsonString);
    	    writer.close();

    	    InputStreamResource resource = new InputStreamResource(new FileInputStream(tmpFile));

			
			ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename("league-"+tournament.getCode()+ "-backup.json") .build();
			 
    	    HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            
    	    
            return ResponseEntity.ok()
    	            .headers(headers)
    	            .contentLength(tmpFile.length())
    	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
    	            .body(resource);
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
            
            return ResponseEntity.ok()
    	            .headers(new HttpHeaders())
    	            .contentLength(0)
    	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
    	            .body(null);
		}
		
	}
	
	@GetMapping("/view/headline.html")
    public ModelAndView viewHeadline(Principal principal, ModelAndView model)
    {
		Tournament tournament = new Tournament();
		List<Headline> headlines = new ArrayList<>();
		Headline headline = new Headline();
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
        	headlines = headlineService.getAllHeadline(tournament);
        	
        	
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        model.addObject("tournament",tournament);
    	model.addObject("headlines",headlines);
    	model.addObject("headline",headline);
        model.setViewName("admin/headline");
        
        return model;
    }
	
	@PostMapping("/update/headline.html")
    public ModelAndView updateHeadline(Principal principal, ModelAndView model, @ModelAttribute Headline headline)
    {
		Tournament tournament = new Tournament();
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
        	headline.setTournament(tournament);
        	
        	headlineService.saveHeadline(headline);
        	model.addObject("successMsg","Headline Successfully Saved");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        
        
        return viewHeadline(principal,model);
    }
	
	@PostMapping("/delete/headline.html")
    public ModelAndView deleteHeadline(Principal principal, ModelAndView model, @RequestParam(name = "headlineId") Long headlineId)
    {
		log.info("delete request recieved with id: " + headlineId);
        try
        {
        	headlineService.deleteHeadline(headlineId);
        	model.addObject("successMsg","Headline Successfully Deleted");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        
        return viewHeadline(principal,model);
    }
	
	@GetMapping("/add/team.html")
    public ModelAndView getAddTeam(Principal principal, ModelAndView model)
    {
		Tournament tournament = new Tournament();

        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());

            tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
        	
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        model.addObject("tournament",tournament);
        model.setViewName("admin/teams");
        
        return model;
    }
	
	@PostMapping("/add/team.html")
    public ModelAndView postAddTeam(Principal principal, ModelAndView model, @RequestParam(name = "name") String name)
    {
		Tournament tournament = new Tournament();
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));
        	tournamnetService.addTeamToExistingTournament(tournament, name);
        	
        	model.addObject("successMsg","Headline Successfully Saved");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        
        
        return getAddTeam(principal,model);
    }
	
	@GetMapping("/view/tsetting.html")
    public ModelAndView viewTournamentSetting(Principal principal, ModelAndView model)
    {
		Tournament tournament = new Tournament();
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));        	
        	
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        model.addObject("tournament",tournament);
        model.setViewName("admin/tsetting");
        
        return model;
    }
	
	@PostMapping("/view/tsetting.html")
    public ModelAndView viewTournamentSettingPost(Principal principal, ModelAndView model, @ModelAttribute Tournament tournament)
    {
        try
        {
        	UsernamePasswordAuthenticationToken _principal = ((UsernamePasswordAuthenticationToken) principal);
            User user = ((User) _principal.getPrincipal());
            
        	tournament = tournamentAdminService.getTournamentForUser(new UserEntity(user));        	
        	
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
            model.addObject("errorMsg",e.getMessage());
        }
        model.addObject("tournament",tournament);
        model.setViewName("admin/tsetting");
        
        return model;
    }
	
	
	
}
