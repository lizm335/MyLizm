package com.gzedu.xlims.web.controller.exam;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamCorrectPapers;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamCorrectPapersService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 报告批改控制层
 */
@Controller
@RequestMapping("/exam/new/correctPapers")
public class GjtExamCorrectPapersController {
	
	@Autowired
	private GjtExamCorrectPapersService gjtExamCorrectPapersService;

	@Autowired
	private CommonMapService commonMapService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId= user.getGjtOrg().getId();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        // 默认选择当前期(批次)
        if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EQ_examBatchCode"))){
            String code = commonMapService.getCurrentGjtExamBatchNew(orgId);
            searchParams.put("EQ_examBatchCode", code);
            model.addAttribute("examBatchCode",code);
        }else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_examBatchCode")) ){
            model.addAttribute("examBatchCode", ObjectUtils.toString(searchParams.get("EQ_examBatchCode")));
        }
        
        Page<GjtExamCorrectPapers> pageInfo = gjtExamCorrectPapersService.findAll(orgId, searchParams, pageRequst);
		Map<String, Long> countStateMap = new HashMap<String, Long>();
		Object correctState = searchParams.remove("EQ_correctState");
		long countAll = gjtExamCorrectPapersService.count(orgId, searchParams);
		countStateMap.put("", countAll);
		searchParams.put("EQ_correctState", 0);
		long count0 = gjtExamCorrectPapersService.count(orgId, searchParams);
		countStateMap.put("0", count0);
		searchParams.put("EQ_correctState", 1);
		long count1 = gjtExamCorrectPapersService.count(orgId, searchParams);
		countStateMap.put("1", count1);
		searchParams.put("EQ_correctState", 2);
		long count2 = gjtExamCorrectPapersService.count(orgId, searchParams);
		countStateMap.put("2", count2);
		searchParams.put("EQ_correctState", correctState);
        
        model.addAttribute("termMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(orgId));
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("countStateMap", countStateMap);
		return "edumanage/exam/correctPapers/exam_correct_papers_list";
	}

	/**
	 * 查看报告批改
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id,
						HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		GjtExamCorrectPapers info = gjtExamCorrectPapersService.queryById(id);
		model.addAttribute("info", info);
		model.addAttribute("action", "view");
		return "edumanage/exam/correctPapers/exam_correct_papers_form";
	}

	/**
	 * 报告批改-评分
	 * @param id
	 * @param score
	 * @throws Exception
	 */
	@SysLog("报告批改-评分")
	@RequiresPermissions("/exam/new/correctPapers/list$approval")
	@RequestMapping(value = "approval", method = RequestMethod.POST)
	@ResponseBody
	public Feedback approval(@RequestParam String id,
						 @RequestParam int score,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "success");
		GjtExamCorrectPapers info = gjtExamCorrectPapersService.queryById(id);
		if(info.getCorrectState() == 1 || info.getCorrectState() == 2) {
			feedback = new Feedback(false, "报告已批改，不能重复批改！");
			return feedback;
		}
		boolean flag = gjtExamCorrectPapersService.updateScore(id, score, user.getId());
		if(!flag) {
			feedback = new Feedback(false, "操作失败！");
		}
		return feedback;
	}

	/**
	 * 导出补考缴费记录
	 *
	 * @param examBatchCode
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SysLog("报告批改-导出成绩")
	@RequiresPermissions("/exam/new/correctPapers/list$export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	@ResponseBody
	public void export(@RequestParam String examBatchCode, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		/*Map<String, Object> params = new HashMap<String, Object>();
		params.put("EQ_examBatchCode", examBatchCode);

		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);

		Page<GjtExamCost> pageInfo = gjtExamCostService.findAll(user.getGjtOrg().getId(), params, pageRequst);

		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent());

		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("补考缴费记录.xls".getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());*/
	}

}
