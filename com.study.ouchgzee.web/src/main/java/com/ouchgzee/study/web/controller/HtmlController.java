package com.ouchgzee.study.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pcenter")
public class HtmlController {
	
	@RequestMapping(value="/{htmlPage}.html", method={RequestMethod.GET,RequestMethod.POST})
	public String html(@PathVariable String htmlPage) {
		return htmlPage;
	}
	
	@RequestMapping(value="/{path}/{htmlPage}.html")
	public String html(@PathVariable String path, @PathVariable String htmlPage) {
		return path+"/"+htmlPage;
	}
	

}
