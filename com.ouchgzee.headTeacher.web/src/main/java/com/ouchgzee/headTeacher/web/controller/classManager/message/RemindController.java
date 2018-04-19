/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gzedu.xlims.common.DateUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageService;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageUserService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 督促提醒控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年07月13日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/remind")
public class RemindController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(RemindController.class);

	@Autowired
	private BzrGjtMessageService gjtMessageService;

	@Autowired
	private BzrCommonMapService commonMapService;

	@Autowired
	BzrGjtClassService gjtClassService;

	@Autowired
	BzrGjtMessageUserService gjtMessageUserService;

	/**
	 * EE督促提醒列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "eeList", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_infoTool", "4"); // EE
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		searchParams.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());
		Page<BzrGjtMessageInfo> infos = gjtMessageService.queryRemindMessageInfoByPage(searchParams, pageRequst);

		model.addAttribute("infos", infos);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/remind/ee_list";
	}

	/**
	 * 短信督促提醒列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "smsList", method = RequestMethod.GET)
	public String publishList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_infoTool", "3"); // EE
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		searchParams.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());
		Page<BzrGjtMessageInfo> infos = gjtMessageService.queryRemindMessageInfoByPage(searchParams, pageRequst);

		model.addAttribute("infos", infos);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/remind/sms_list";
	}

	/**
	 * 浏览督促提醒
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, ServletRequest request, HttpSession session) {
		BzrGjtMessageInfo messageInfo = gjtMessageService.queryMessageReadSituationById(id);

		model.addAttribute("info", messageInfo);
		model.addAttribute("action", "view");
		return "new/class/remind/view";
	}

	/**
	 * 加载新增督促提醒
	 * 
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, ServletRequest request, HttpSession session) {
		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		Map<String, String> studentMap = commonMapService.getStudentMap(classId);
		model.addAttribute("info", new BzrGjtMessageInfo());
		model.addAttribute("studentMap", studentMap);
		model.addAttribute("action", "create");
		return "new/class/remind/form";
	}

	/**
	 * 新增督促提醒
	 * 
	 * @param messageInfo
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid BzrGjtMessageInfo messageInfo, RedirectAttributes redirectAttributes,
						 ServletRequest request, HttpSession session) {
		BzrGjtUserAccount gjtUserAccount = super.getUser(session).getGjtUserAccount();
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Feedback feedback = new Feedback(true, "添加成功");
		try {
			String[] sendTypes = request.getParameterValues("sendType");
			boolean isSendEE = false, isSendSMS = false;
			if (sendTypes != null && sendTypes.length > 0) {
				for (String sendType : sendTypes) {
					if (!isSendEE)
						isSendEE = "1".equals(sendType);
					if (!isSendSMS)
						isSendSMS = "2".equals(sendType);
				}
			} else {
				feedback = new Feedback(false, "请选择接收方式！");
				redirectAttributes.addFlashAttribute("feedback", feedback);
				return "redirect:/home/class/remind/eeList";
			}
			List<String> lists = new ArrayList<String>();// 所有的userId

			// 单个学生
			String[] userIds = request.getParameterValues("userIds");

			for (String userId : userIds) {
				if (StringUtils.isNoneBlank(userId)) {
					lists.add(userId);
				}
			}

			// 指定班级
			String[] classIds = request.getParameterValues("classIds");
			if (classIds != null && classIds.length > 0) {
				for (String classId : classIds) {
					List<Map<String, Object>> students = gjtClassService.queryStudentByClassId(classId, null);
					for (Map<String, Object> map : students) {
						lists.add((String) map.get("ID"));
					}
				}
			}

			// 去重复
			List<String> userIdList = new ArrayList<String>();
			for (String s : lists) {
				if (Collections.frequency(userIdList, s) < 1)
					userIdList.add(s);
			}

			messageInfo.setCreatedBy(gjtUserAccount.getLoginAccount());
			messageInfo.setCreatedDt(DateUtils.getNowTime());
			messageInfo.setIsDeleted("N");
			messageInfo.setIsEnabled("0");
			messageInfo.setVersion(new BigDecimal(1.0));
			messageInfo.setInfoTool("1");
			messageInfo.setPutUser(gjtUserAccount.getId());
			messageInfo.setXxId(employeeInfo.getXxzxId());
			messageInfo.setGetUserRole("学员");
			messageInfo.setInfoType("21");
			messageInfo.setGjtUserAccount(gjtUserAccount);
			messageInfo.setEffectiveTime(DateUtils.getDate());
			gjtMessageService.insertRemind(employeeInfo.getEeno(), messageInfo, userIdList, isSendEE, isSendSMS);

		} catch (Exception e) {
			feedback = new Feedback(false, "添加失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/remind/eeList";
	}

	/**
	 * 加载督促提醒-弹出框
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreatePopup", method = RequestMethod.GET)
	public String toCreatePopup(Model model, ServletRequest request, HttpSession session) {
		createForm(model, request, session);
		return "new/class/remind/formPopup";
	}

	/**
	 * 新增督促提醒-弹出框
	 * 
	 * @return
	 */
	@RequestMapping(value = "doCreatePopup", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doCreatePopup(@Valid BzrGjtMessageInfo messageInfo, RedirectAttributes redirectAttributes,
								  ServletRequest request, HttpSession session) {
		create(messageInfo, redirectAttributes, request, session);
		Feedback feedback = (Feedback) redirectAttributes.getFlashAttributes().get("feedback");
		return feedback;
	}

	/**
	 * 重新发送
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "resend")
	@ResponseBody
	public Feedback resend(@RequestParam("id") String id, ServletRequest request, HttpSession session) {
		Feedback fb = new Feedback(true, "发送成功");
		try {
			boolean isSendEE = Constants.BOOLEAN_1.equals(request.getParameter("isSendEE"));
			boolean isSendSMS = Constants.BOOLEAN_1.equals(request.getParameter("isSendSMS"));
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

			BzrGjtMessageInfo messageInfo = gjtMessageService.queryById(id);
			List<BzrGjtMessageUser> list = gjtMessageUserService.queryByMessageId(id);

			List<String> userIdList = new ArrayList<String>();
			for (BzrGjtMessageUser item : list) {
				userIdList.add(item.getUserId());
			}
			boolean flag = false;
			if (userIdList.size() > 0) {
				flag = gjtMessageService.insertRemind(employeeInfo.getEeno(), messageInfo, userIdList, isSendEE,
						isSendSMS);
			}
			if (!flag) {
				fb = new Feedback(false, "发送失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "发送失败");
		}
		return fb;
	}

	/**
	 * 删除督促提醒
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids, ServletResponse response, HttpSession session) {
		if (StringUtils.isNotBlank(ids)) {
			try {
				String[] selectedIds = ids.split(",");
				BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
				gjtMessageService.delete(selectedIds, employeeInfo.getGjtUserAccount().getId());
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "addObject", method = RequestMethod.GET)
	public String addObject(HttpServletRequest request, Model model, String searchClassId, String searchName) {
		String currentClassId = "";
		if (StringUtils.isBlank(searchClassId)) {
			currentClassId = super.getCurrentClassId(request.getSession());
		} else {
			currentClassId = searchClassId;
		}
		List<Map<String, Object>> studetnList = gjtClassService.queryStudentByClassId(currentClassId, searchName);// 当前班级学员
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		List<Map<String, Object>> classList = gjtClassService.queryClassInfoByTeachId(employeeInfo.getEmployeeId());// 班主任对应的所有教学班级
		model.addAttribute("classList", classList);
		model.addAttribute("studetnList", studetnList);
		model.addAttribute("studentSize", studetnList.size());
		model.addAttribute("classSize", classList.size());
		model.addAttribute("searchClassId", searchClassId);
		model.addAttribute("searchName", searchName);

		return "new/class/remind/addObject";
	}

}
