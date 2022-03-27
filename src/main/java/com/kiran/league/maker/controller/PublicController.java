package com.kiran.league.maker.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/public")
public class PublicController {

	
	private static final Log log = LogFactory.getLog(HomeController.class);
	
	@GetMapping("/league/create.html")
    public ModelAndView landing(ModelAndView model)
    {
        try
        {
           
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        model.setViewName("public/league/create");
        
        return model;
    }
}
