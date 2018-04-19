package com.gzedu.xlims.web.controller.graduation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviserMsg;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.graduation.GjtGraduationAdviserMsgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 毕业指导老师接收信息权限
 * 
 * @author eenet09
 *
 */
@Controller
@RequestMapping("/graduation/adviserMsg")
public class GjtGraduationAdviserMsgController {

	private static final Log log = LogFactory.getLog(GjtGraduationAdviserMsgController.class);

	@Autowired
	private GjtGraduationAdviserMsgService gjtGraduationAdviserMsgService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	/**
	 * 查询列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam("adviserType") int adviserType, Model model, HttpServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("EQ_teacher.xxId", user.getGjtOrg().getId());
		searchParams.put("EQ_adviserType", adviserType);
		searchParams.put("EQ_msgType", 3);

		Page<GjtGraduationAdviserMsg> pageInfo = gjtGraduationAdviserMsgService.queryPage(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		return "graduation/adviserMsg/adviserMsgList";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(@RequestParam(value = "adviserType") int adviserType, Model model,
			HttpServletRequest request) {
		model.addAttribute("entity", new GjtGraduationAdviserMsg());
		model.addAttribute("action", "create");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (adviserType == 1) {
			List<GjtEmployeeInfo> gjtGraduationAdvisers = gjtEmployeeInfoService.findListByType(
					EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文指导教师.getNum(), user.getGjtOrg().getId());
			model.addAttribute("advisers", gjtGraduationAdvisers);
		} else if (adviserType == 2) {
			List<GjtEmployeeInfo> gjtGraduationAdvisers = gjtEmployeeInfoService.findListByType(
					EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.社会实践教师.getNum(), user.getGjtOrg().getId());
			model.addAttribute("advisers", gjtGraduationAdvisers);
		}

		return "graduation/adviserMsg/adviserMsgForm";
	}

	/**
	 * 新增
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtGraduationAdviserMsg entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			if (entity.getAdvisers() != null && entity.getAdvisers().length > 0) {
				for (String adviser : entity.getAdvisers()) {
					GjtGraduationAdviserMsg msg = new GjtGraduationAdviserMsg();
					msg.setAdviserType(entity.getAdviserType());
					msg.setMsgType(3);

					GjtEmployeeInfo teacher = new GjtEmployeeInfo();
					teacher.setEmployeeId(adviser);
					msg.setTeacher(teacher);
					gjtGraduationAdviserMsgService.insert(msg);
				}
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/adviserMsg/list?adviserType=" + entity.getAdviserType();
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) throws IOException {
		try {
			gjtGraduationAdviserMsgService.delete(ids);
			return new Feedback(true, "删除成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "删除失败，原因:" + e.getMessage());
		}

	}

}
