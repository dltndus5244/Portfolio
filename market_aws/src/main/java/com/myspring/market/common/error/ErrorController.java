package com.myspring.market.common.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("errorController")
public class ErrorController {
	
	@RequestMapping("/error/405")
	public ModelAndView handle405() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main/main.do");
		return mav;
	}
}
