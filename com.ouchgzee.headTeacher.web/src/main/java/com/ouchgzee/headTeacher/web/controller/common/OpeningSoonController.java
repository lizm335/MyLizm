package com.ouchgzee.headTeacher.web.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/openingSoon")
public class OpeningSoonController {
	
	@RequestMapping
	public String openingSoon() {
		return "new/opening_soon";
	}

}
