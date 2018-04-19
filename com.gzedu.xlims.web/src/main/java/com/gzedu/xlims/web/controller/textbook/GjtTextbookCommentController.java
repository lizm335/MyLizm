package com.gzedu.xlims.web.controller.textbook;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbookComment;
import com.gzedu.xlims.service.textbook.GjtTextbookCommentService;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookComment")
public class GjtTextbookCommentController {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookCommentController.class);

	@Autowired
	private GjtTextbookCommentService gjtTextbookCommentService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", Direction.DESC.name());
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_GjtStudentInfo.xxId", user.getGjtOrg().getId());
		Page<GjtTextbookComment> page = gjtTextbookCommentService.queryByPage(searchParams, pageRequest);
		model.addAttribute("pageInfo", page);
		searchParams.put("EQ_commentType", 0);
		model.addAttribute("goodCount", gjtTextbookCommentService.queryByPage(searchParams, pageRequest).getTotalElements());
		searchParams.put("EQ_commentType", 1);
		model.addAttribute("middleCount", gjtTextbookCommentService.queryByPage(searchParams, pageRequest).getTotalElements());
		searchParams.put("EQ_commentType", 2);
		model.addAttribute("badCount", gjtTextbookCommentService.queryByPage(searchParams, pageRequest).getTotalElements());
		return "textbook/textbookComment_list";
	}
}
