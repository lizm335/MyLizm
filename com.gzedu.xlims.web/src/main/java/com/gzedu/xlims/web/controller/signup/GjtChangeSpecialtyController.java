/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.signup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtChangeSpecialty;
import com.gzedu.xlims.pojo.GjtEnrollBatch;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.map.ChangeTypeMap;
import com.gzedu.xlims.pojo.status.ChangeTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtChangeSpecialtyService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/changespecialty")
public class GjtChangeSpecialtyController {

	@Autowired
	GjtChangeSpecialtyService gjtChangeSpecialtyService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Sort.Direction.DESC, "createdDt");
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String choice = request.getParameter("search_EQ_changetype");
		if (StringUtils.isBlank(choice)) {
			searchParams.put("EQ_changetype", "101");
		}
		Page<GjtChangeSpecialty> page = gjtChangeSpecialtyService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> changeTypeMap = ChangeTypeMap.getChangeType();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("changeTypeMap", changeTypeMap);
		model.addAttribute("pageInfo", page);
		
		
		return "edumanage/changespecialty/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("item", new GjtChangeSpecialty());
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("action", "create");
		return "edumanage/changespecialty/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(String studentId, String changeType, String remark, String studyId, String gradeId,
			String specialtyId, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		if (changeType != null) {
			int type = Integer.parseInt(changeType);
			if (studentInfo != null) {
				GjtSpecialty oldGjtSpecialty = studentInfo.getGjtSpecialty();
				GjtStudyCenter oldGjtStudyCenter = studentInfo.getGjtStudyCenter();

				GjtChangeSpecialty gcs = new GjtChangeSpecialty();
				gcs.setId(UUIDUtils.random());
				gcs.setCreatedDt(DateUtils.getNowTime());
				gcs.setState("1");
				gcs.setGjtStudentInfo(studentInfo);
				gcs.setRemark(remark);
				if (type == ChangeTypeEnum.转专业.getNum()) {
					// 查询专业，更改新专业
					GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(specialtyId);
					studentInfo.setGjtSpecialty(gjtSpecialty);
					gjtStudentInfoService.updateEntity(studentInfo);

					// 插入记录到学籍异动
					gcs.setChangetype(String.valueOf(ChangeTypeEnum.转专业.getNum()));
					gcs.setOldGjtSpecialty(oldGjtSpecialty);// 为什么是对象，兼容以前
					gcs.setNowGjtSpecialty(gjtSpecialty);
					gcs.setaName(gjtSpecialty.getZymc());
					gcs.setbName(oldGjtSpecialty.getZymc());

				} else if (type == ChangeTypeEnum.转学习中心.getNum()) {
					// 更改学习中心
					GjtStudyCenter gjtStudyCenter = gjtStudyCenterService.queryById(studyId);
					studentInfo.setGjtStudyCenter(gjtStudyCenter);
					gjtStudentInfoService.updateEntity(studentInfo);

					// 插入记录到学籍异动
					gcs.setChangetype(String.valueOf(ChangeTypeEnum.转学习中心.getNum()));
					gcs.setaStudycenterid(gjtStudyCenter.getId());
					gcs.setbStudycenterid(oldGjtStudyCenter.getId());
					gcs.setaName(gjtStudyCenter.getScName());
					gcs.setbName(oldGjtStudyCenter.getScName());

				} else if (type == ChangeTypeEnum.转年级.getNum()) {
					GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
					List<GjtEnrollBatch> list = gjtGradeService.findByGjtGrade(gjtGrade);
					String enrollBatchId = "";
					for (GjtEnrollBatch gjtEnrollBatch : list) {
						enrollBatchId = gjtEnrollBatch.getEnrollBatchId();
					}
					// 修改学员年级，通过中间表
					gjtStudentInfoService.updateStudentGrade(studentInfo.getStudentId(), enrollBatchId);

					// 记录
					gcs.setChangetype(String.valueOf(ChangeTypeEnum.转年级.getNum()));
					gcs.setaGjtgradeid(gjtGrade.getGradeId());
					gcs.setbGjtgradeid(studentInfo.getGjtGrade().getGradeId());
					gcs.setaName(gjtGrade.getGradeName());
					gcs.setbName(studentInfo.getGjtGrade().getGradeName());

				}

				gjtChangeSpecialtyService.saveEntity(gcs);
			} else {
				feedback = new Feedback(false, "查找不到改学生");
			}
		} else {
			feedback = new Feedback(false, "参数异常");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/changespecialty/list";
	}

	@RequestMapping(value = "queryByXh", method = RequestMethod.POST)
	@ResponseBody
	public Feedback queryByXh(String xh) {
		Feedback feedback = new Feedback(false, "查询失败");
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(xh == null ? "" : xh.trim());
		Map<String, Object> student = new HashMap<String, Object>();
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		if (studentInfo != null) {
			student.put("studentId", studentInfo.getStudentId());
			student.put("xh", studentInfo.getXh());
			student.put("xm", studentInfo.getXm());
			student.put("xbm", studentInfo.getXbm() == "1" ? "男" : "女");
			student.put("pycc", pyccMap.get(studentInfo.getPycc()));
			student.put("gradeName", studentInfo.getGjtGrade().getGradeName());
			student.put("major", studentInfo.getGjtSpecialty().getZymc());
			student.put("studyCenter", studentInfo.getGjtStudyCenter().getScName());
			feedback = new Feedback(true, "查询成功", "", student);
		}
		return feedback;
	}
}
