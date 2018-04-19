package com.gzedu.xlims.web.controller.textbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;
import com.gzedu.xlims.service.textbook.GjtTextbookStockOperaBatchService;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookStockOperaBatch")
public class GjtTextbookStockOperaBatchController {

	private static final Log log = LogFactory.getLog(GjtTextbookStockOperaBatchController.class);

	@Autowired
	private GjtTextbookStockOperaBatchService gjtTextbookStockOperaBatchService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		
		Page<GjtTextbookStockOperaBatch> pageInfo = gjtTextbookStockOperaBatchService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		
		List<GjtTextbookStockOperaBatch> list = pageInfo.getContent();
		if (list != null && list.size() > 0) {
			for (GjtTextbookStockOperaBatch operaBatch : list) {
				List<GjtTextbookStockOpera> operas = operaBatch.getGjtTextbookStockOperas();
				boolean stockStatus = true;  //库存状态
				if (operas != null && operas.size() > 0) {
					for (GjtTextbookStockOpera opera : operas) {
						if (opera.getGjtTextbook().getGjtTextbookStock().getPlanOutStockNum() > opera.getGjtTextbook().getGjtTextbookStock().getStockNum()) {
							stockStatus = false;
							break;
						}
					}
				}
				operaBatch.setStockStatus(stockStatus);
			}
		}
		
		//查询“待审核”、“审核通过”和“审核未通过”总数
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_status", 1);
		model.addAttribute("pending", gjtTextbookStockOperaBatchService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("passed", gjtTextbookStockOperaBatchService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 3);
		model.addAttribute("notPassed", gjtTextbookStockOperaBatchService.findAll(map, pageRequst).getTotalElements());
		
		return "textbook/textbookStockOperaBatch_list";
	}

	@RequestMapping(value = "view/{operaBatchId}")
	public String view(@PathVariable("operaBatchId") String operaBatchId, Model model) {
		log.info("operaBatchId:" + operaBatchId);
		GjtTextbookStockOperaBatch operaBatch = gjtTextbookStockOperaBatchService.findOne(operaBatchId);
		List<GjtTextbookStockOpera> operas = operaBatch.getGjtTextbookStockOperas();
		boolean stockStatus = true;  //库存状态
		if (operas != null && operas.size() > 0) {
			for (GjtTextbookStockOpera opera : operas) {
				if (opera.getGjtTextbook().getGjtTextbookStock().getPlanOutStockNum() > opera.getGjtTextbook().getGjtTextbookStock().getStockNum()) {
					stockStatus = false;
					break;
				}
			}
		}
		operaBatch.setStockStatus(stockStatus);
		model.addAttribute("operaBatch", operaBatch);
		
		return "textbook/textbookStockOperaBatch_detail";
	}

	@RequestMapping(value = "approval")
	public String approval(@RequestParam("operaBatchId") String operaBatchId, @RequestParam("operaType") int operaType,
			@RequestParam("description") String description, HttpServletRequest request) {
		log.info("operaBatchId:" + operaBatchId + ", operaType:" + operaType);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		gjtTextbookStockOperaBatchService.approval(operaBatchId, operaType, description, user.getId());
		
		return "redirect:/textbookStockOperaBatch/view/" + operaBatchId;
	}

}
