/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.home;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.IPUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtUserBehavior;
import com.gzedu.xlims.pojo.TblPriLoginLog;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.home.HomeService;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.service.vo.Todo;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 工作统计<br/>
 * 功能说明：记录各个角色的工作效率，并统计<br/>
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @date 2017年08月02日
 * @version 3.0
 */
@Controller
@RequestMapping("/admin/workStatistics")
public class WorkStatisticsController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(WorkStatisticsController.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private PriRoleInfoService roleInfoService;

	@Autowired
	private HomeService homeService;

	@Autowired
	private TblPriLoginLogService tblPriLoginLogService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 工作统计
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param orderProperty
	 * @param orderDirection
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "8") int pageSize,
			@RequestParam(value = "orderProperty", defaultValue = "createdDt") String orderProperty,
			@RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, orderProperty, orderDirection);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Map<String, String> roles = roleInfoService.queryRoles(user.getGrantType(), user.getPriRoleInfo().getRoleId());
		model.addAttribute("roles", roles);

		if (EmptyUtils.isEmpty(searchParams.get("EQ_priRoleInfo.roleId"))) {
			List<String> roleIdList = new ArrayList<String>();
			roleIdList.addAll(roles.keySet());
			searchParams.put("IN_priRoleInfo.roleId", roleIdList);
		}

		Page<Map<String, Object>> pageInfo = gjtUserAccountService.queryWorkStatisticsByPage(user.getGjtOrg().getId(),
				searchParams, pageRequst);

		// --------------------------------------------------------------------------------------------
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(searchParams);
		// 选项卡统计
		params.put("STUDY_STATUS", "");// 全部
		long totalAll = gjtUserAccountService.countWorkStatisticsBy(user.getGjtOrg().getId(), params);
		params.put("STUDY_STATUS", "0");// 在线
		long total0 = gjtUserAccountService.countWorkStatisticsBy(user.getGjtOrg().getId(), params);
		params.put("STUDY_STATUS", "1");// 离线（7天以上未学习）
		long total1 = gjtUserAccountService.countWorkStatisticsBy(user.getGjtOrg().getId(), params);
		params.put("STUDY_STATUS", "2");// 离线（3天以上未学习）
		long total2 = gjtUserAccountService.countWorkStatisticsBy(user.getGjtOrg().getId(), params);
		params.put("STUDY_STATUS", "3");// 离线（3天内未学习）
		long total3 = gjtUserAccountService.countWorkStatisticsBy(user.getGjtOrg().getId(), params);
		params.put("STUDY_STATUS", "4");// 从未登录
		long total4 = gjtUserAccountService.countWorkStatisticsBy(user.getGjtOrg().getId(), params);
		// 选项卡统计

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("totalAll", totalAll);
		model.addAttribute("total0", total0);
		model.addAttribute("total1", total1);
		model.addAttribute("total2", total2);
		model.addAttribute("total3", total3);
		model.addAttribute("total4", total4);
		request.getSession().setAttribute("downLoadExcelExportByWorkStatistics", searchParams);// 导出数据的查询条件
		return "home/workStatistics/work_statistics_list";
	}

	/**
	 * 工作统计明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") String id, HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_id", id);
		List<Map<String, Object>> list = gjtUserAccountService.queryWorkStatisticsBy(user.getGjtOrg().getId(),
				searchParams);
		Map<String, Object> info = list != null && list.size() > 0 ? list.get(0) : null;
		List<Todo> todoList = homeService.getTodoList(gjtUserAccountService.findOne(id));
		int totalTodo = 0;
		for (Todo todo : todoList) {
			totalTodo += todo.getTotal();
		}
		info.put("totalTodo", totalTodo + "");

		Map<String, Object> countMutualSubject = gjtUserAccountService.countWorkStatisticsMutualSubjectByAccountId(id);

		model.addAttribute("info", info);
		model.addAttribute("todoList", todoList);
		model.addAttribute("countMutualSubject", countMutualSubject);
		model.addAttribute("datetime", DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
		request.getSession().setAttribute("downLoadExcelExportByWorkStatistics", searchParams);// 导出数据的查询条件
		return "home/workStatistics/work_statistics_view";
	}

	/**
	 * 登录日志
	 * 
	 * @param id
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "viewLoginLog/{id}")
	public String list(@PathVariable("id") String id, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_createdBy", id);

		Page<TblPriLoginLog> pageInfo = tblPriLoginLogService.queryByPage(searchParams, pageRequst);
		for (Iterator<TblPriLoginLog> iter = pageInfo.iterator(); iter.hasNext();) {
			TblPriLoginLog info = iter.next();
			if (StringUtils.isNotBlank(info.getLoginIp()) && info.getLoginAddress() == null) {
				info.setLoginAddress(IPUtils.getAddress(info.getLoginIp()));
				tblPriLoginLogService.updateEntity(info);
			}
		}

		model.addAttribute("pageInfo", pageInfo);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "home/workStatistics/work_statistics_view_login_log";
	}

	/**
	 * 获取用户有行为的时间
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getAllDate/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Feedback getAllDate(@PathVariable("id") String id, HttpServletRequest request, Model model) {
		Feedback feedback = new Feedback(true, "获取成功");
		feedback.setObj(gjtUserAccountService.queryAllDate(id));
		return feedback;
	}

	/**
	 * 统计登录情况
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "countLoginLog/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Feedback listUserBehaviorByPage(@PathVariable("id") String id, HttpServletRequest request, Model model) {
		Feedback feedback = new Feedback(true, "获取成功");
		Map<String, Object> searchParams = new HashMap<String, Object>();
		String datetime = request.getParameter("datetime");
		if (StringUtils.isBlank(datetime)) {
			datetime = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
		}
		searchParams.put("GTE_createdDt", datetime);
		searchParams.put("LTE_createdDt", datetime);

		Map<String, Object> countLoginLog = gjtUserAccountService.countProLoginLogByAccountId(id, searchParams);
		feedback.setObj(countLoginLog);
		return feedback;
	}

	/**
	 * 工作动态分页获取
	 * 
	 * @param id
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "listUserBehaviorByPage/{id}")
	@ResponseBody
	public Feedback listUserBehaviorByPage(@PathVariable("id") String id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {
		Feedback feedback = new Feedback(true, "获取成功");
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
		pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), new Sort("createDate"));

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_userId", id);
		String datetime = request.getParameter("datetime");
		if (StringUtils.isBlank(datetime)) {
			datetime = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
		}
		searchParams.put("GTE_createDate", datetime);
		searchParams.put("LTE_createDate", datetime);

		Page<GjtUserBehavior> pageInfo = gjtUserAccountService.queryUserBehaviorByPage(searchParams, pageRequest);
		feedback.setObj(pageInfo);
		return feedback;
	}

	/**
	 * 工作统计--》导出工作统计表
	 */
	@SysLog("工作统计-导出工作统计表")
	@RequestMapping(value = "exportWorkStatistics", method = RequestMethod.POST)
	public void exportWorkStatistics(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			Map<String, Object> searchParams = (Map) request.getSession()
					.getAttribute("downLoadExcelExportByWorkStatistics");

			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "workStatistics" + File.separator;
			String outFile = gjtUserAccountService.exportWorkStatistics(user.getGjtOrg().getId(), searchParams, path);

			super.downloadFile(request, response, path + outFile);
			FileKit.delFile(path + outFile); // 删除临时文件
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 工作统计--》导出工作统计明细表
	 */
	@SysLog("工作统计-导出工作统计明细表")
	@RequestMapping(value = "exportWorkStatisticsDetail", method = RequestMethod.POST)
	public void exportWorkStatisticsDetail(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			Map<String, Object> searchParams = (Map) request.getSession()
					.getAttribute("downLoadExcelExportByWorkStatistics");

			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "workStatistics" + File.separator;
			String outputFilePath = gjtUserAccountService.exportWorkStatisticsDetail(user.getGjtOrg().getId(),
					searchParams, path);

			super.downloadFile(request, response, outputFilePath);
			FileKit.delFile(outputFilePath); // 删除临时文件
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 导出前选择时间，导出对应的时间段数据
	 * 
	 * @param totalNum
	 * @param formAction
	 * @return
	 */
	@RequestMapping(value = "/exportLoginSituation/{totalNum}", method = { RequestMethod.GET, RequestMethod.POST })
	public String scoreListExport(@PathVariable("totalNum") String totalNum,
			@RequestParam("formAction") String formAction, HttpServletRequest request, Model model) {
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("formAction", formAction);
		return "home/workStatistics/work_statistics_login_log_export";
	}

	/**
	 * 工作统计--》导出登录情况明细表
	 */
	@SysLog("工作统计-导出登录情况明细表")
	@RequestMapping(value = "exportLoginSituation", method = RequestMethod.POST)
	public void exportLoginSituation(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, Object> searchParams = (Map) request.getSession()
				.getAttribute("downLoadExcelExportByWorkStatistics");

		Map<String, Object> searchParams2 = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("GTE_createdDt", searchParams2.get("GTE_createdDt"));
		searchParams.put("LTE_createdDt", searchParams2.get("LTE_createdDt"));

		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
				+ "workStatistics" + File.separator;
		String outputFilePath = gjtUserAccountService.exportLoginSituation(user.getGjtOrg().getId(), searchParams,
				path);

		super.downloadFile(request, response, outputFilePath);
		FileKit.delFile(outputFilePath); // 删除临时文件
	}

}
