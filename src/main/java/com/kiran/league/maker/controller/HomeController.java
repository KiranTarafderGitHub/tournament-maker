package com.kiran.league.maker.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
private static final Log log = LogFactory.getLog(HomeController.class);
    
    @GetMapping("/")
    public ModelAndView landing(ModelAndView model)
    {
        try
        {
           
        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        model.setViewName("dashboard/index");
        
        return model;
    }
}
