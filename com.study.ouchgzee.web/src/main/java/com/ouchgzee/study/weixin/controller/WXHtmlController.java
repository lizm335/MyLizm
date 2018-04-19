package com.ouchgzee.study.weixin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 微信网页版访问路径支持<br>
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月22日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/wx")
public class WXHtmlController {
	
	@RequestMapping(value="/{htmlPage}.html", method={RequestMethod.GET,RequestMethod.POST})
	public String html(@PathVariable String htmlPage) {
		return htmlPage;
	}
	
	@RequestMapping(value="/{path}/{htmlPage}.html")
	public String html(@PathVariable String path, @PathVariable String htmlPage) {
		return path+"/"+htmlPage;
	}
	
	@RequestMapping(value="/{path}/{pathTwo}/{htmlPage}.html")
	public String html(@PathVariable String path, 
			@PathVariable String pathTwo, @PathVariable String htmlPage) {
		return path+"/"+pathTwo+"/"+htmlPage;
	}
	
	@RequestMapping(value="/{path}/{pathTwo}/{pathThree}/{htmlPage}.html")
	public String html(@PathVariable String path, 
			@PathVariable String pathTwo, 
			@PathVariable String pathThree, @PathVariable String htmlPage) {
		return path+"/"+pathTwo+"/"+pathThree+"/"+htmlPage;
	}

}
