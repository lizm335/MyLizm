package com.gzedu.xlims.web.controller.recruitmanage;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 功能说明：缴费管理
 * 
 * @author 卢林林
 * @Date 2016年11月30日
 * @version 2.0
 *
 */
@Controller
@RequestMapping("/recruitmanage/payment")
public class GjtSignupFeeController {
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		
		return "recruitmanage/payment/list";
	}
	/**
	 * 查看详情
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String viewForm(Model model, HttpServletRequest request){
		return "recruitmanage/payment/payment_from";	
	}
}
