/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceRecord;
import com.ouchgzee.headTeacher.service.serviceManager.BzrGjtServiceManagerService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 服务记录控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/serviceManager")
public class ServiceManagerController extends BaseController {

	@Autowired
	private BzrGjtServiceManagerService gjtServiceManagerService;

	/**
	 * 未结束服务列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_status", Constants.ServiceStatus_0);
		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		Page<BzrGjtServiceInfo> infos = gjtServiceManagerService.queryServiceInfoByClassIdPage(classId, searchParams,
				pageRequst);

		model.addAttribute("infos", infos);
		model.addAttribute("unCount", gjtServiceManagerService.countUnoverNum(classId));
		model.addAttribute("overCount", gjtServiceManagerService.countOverNum(classId));
		model.addAttribute("flag", "0");
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/serviceManager/list";
	}

	/**
	 * 已结束服务列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "overList", method = RequestMethod.GET)
	public String finishList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_status", Constants.ServiceStatus_1);
		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		Page<BzrGjtServiceInfo> infos = gjtServiceManagerService.queryServiceInfoByClassIdPage(classId, searchParams,
				pageRequst);

		model.addAttribute("infos", infos);
		model.addAttribute("unCount", gjtServiceManagerService.countUnoverNum(classId));
		model.addAttribute("overCount", gjtServiceManagerService.countOverNum(classId));
		model.addAttribute("flag", "1");
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/serviceManager/list";
	}

	/**
	 * 浏览服务记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		BzrGjtServiceInfo serviceInfo = gjtServiceManagerService.queryById(id);
		model.addAttribute("info", serviceInfo);
		model.addAttribute("action", "view");
		return "new/class/serviceManager/view";
	}

	/**
	 * 加载新增服务
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreateService", method = RequestMethod.GET)
	public String toCreateService(Model model, ServletRequest request, HttpSession session) {
		// 班级学生根据班级获取
		String classIdByReq = request.getParameter(Servlets.REQUEST_PARAM_CLASSID);
		String classId = StringUtils.isNotEmpty(classIdByReq) ? classIdByReq : super.getCurrentClassId(session);
		// Map<String, String> studentMap =
		// commonMapService.getStudentMap(classId);
		// model.addAttribute("studentMap", studentMap);
		model.addAttribute("classId", classId);// 当前班级id
		model.addAttribute("action", "create");
		return "new/class/serviceManager/form";
	}

	/**
	 * 创建服务
	 * 
	 * @return
	 */
	@RequestMapping(value = "doCreateService", method = RequestMethod.POST)
	public String doCreateService(ServletRequest request, RedirectAttributes redirectAttributes, HttpSession session) {
		Feedback feedback = new Feedback(true, "创建成功");

		String title = request.getParameter("title");
		String way = request.getParameter("way");
		String studentIds = request.getParameter("getUser");
		String content = request.getParameter("content");
		String serviceStartTime = request.getParameter("serviceStartTime");
		String serviceEndTime = request.getParameter("serviceEndTime");
		String status = request.getParameter("status");

		try {
			if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(status) && studentIds.length() > 0) {
				BzrGjtServiceInfo serviceInfo = new BzrGjtServiceInfo();
				BzrGjtServiceRecord serviceRecord = new BzrGjtServiceRecord();
				serviceInfo.setTitle(title);
				serviceInfo.setStatus(status);
				serviceRecord.setContent(content);
				serviceRecord.setWay(way);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date starttimeDate = sdf.parse(serviceStartTime);
				Date endtimeDate = sdf.parse(serviceEndTime);
				serviceRecord.setStarttime(starttimeDate);
				serviceRecord.setEndtime(endtimeDate);

				BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
				String result = gjtServiceManagerService.addServiceInfo(serviceInfo, serviceRecord, studentIds,
						employeeInfo.getGjtUserAccount().getId());
				if (!"1".equals(result)) {
					feedback = new Feedback(false, "创建失败");
				}
			} else {
				feedback = new Feedback(false, "参数缺失");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/serviceManager/list";
	}

	/**
	 * 加载添加记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "toAddRecord", method = RequestMethod.GET)
	public String toAddRecord(@RequestParam("id") String id, Model model, ServletRequest request,
			RedirectAttributes redirectAttributes, HttpSession session) {
		model.addAttribute("id", id);
		return "new/class/serviceManager/addRecord";
	}

	/**
	 * 添加记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "doAddRecord", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doAddRecord(@RequestParam("id") String id, ServletRequest request,
			RedirectAttributes redirectAttributes, HttpSession session) {
		Feedback feedback = new Feedback(true, "新增记录成功");

		String way = request.getParameter("way");
		String content = request.getParameter("content");
		String serviceStartTime = request.getParameter("serviceStartTime");
		String serviceEndTime = request.getParameter("serviceEndTime");

		try {
			BzrGjtServiceRecord serviceRecord = new BzrGjtServiceRecord();
			serviceRecord.setGjtServiceInfo(new BzrGjtServiceInfo(id));
			serviceRecord.setContent(content);
			serviceRecord.setWay(way);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date starttimeDate = sdf.parse(serviceStartTime);
			Date endtimeDate = sdf.parse(serviceEndTime);
			serviceRecord.setStarttime(starttimeDate);
			serviceRecord.setEndtime(endtimeDate);

			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

			String result = gjtServiceManagerService.addServiceRecord(serviceRecord,
					employeeInfo.getGjtUserAccount().getId());
			if (!"1".equals(result)) {
				feedback = new Feedback(false, "新增记录失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "新增记录失败");
		}
		return feedback;
	}

	/**
	 * 结束服务
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "over", method = RequestMethod.GET)
	public String over(@RequestParam("id") String id, ServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

			boolean result = gjtServiceManagerService.over(id, employeeInfo.getGjtUserAccount().getId());
			if (!result) {
				feedback = new Feedback(false, "操作失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "操作失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/serviceManager/overList";
	}

	/**
	 * 加载新增服务-弹出框
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreateServicePopup", method = RequestMethod.GET)
	public String toCreateServicePopup(Model model, ServletRequest request, HttpSession session) {
		toCreateService(model, request, session);
		return "new/class/serviceManager/formPopup";
	}

	/**
	 * 创建服务-弹出框
	 * 
	 * @return
	 */
	@RequestMapping(value = "doCreateServicePopup", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doCreateServicePopup(ServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		doCreateService(request, redirectAttributes, session);
		Feedback feedback = (Feedback) redirectAttributes.getFlashAttributes().get("feedback");
		return feedback;
	}

	/**
	 * 删除服务
	 * 
	 * @param ids
	 */
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids, ServletResponse response, HttpSession session) {
		if (StringUtils.isNotBlank(ids)) {
			try {
				String[] selectedIds = ids.split(",");
				BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
				// gjtServiceManagerService.delete(selectedIds,
				// employeeInfo.getGjtUserAccount().getId());
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	/** 调整至选择班级学员 */
	@RequestMapping(value = "chooseStu")
	public String chooseStu(@RequestParam("classid") String classid, Model model) {
		model.addAttribute("classid", classid);
		return "new/class/serviceManager/choose_stu";
	}

}
