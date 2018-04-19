package com.gzedu.xlims.web.controller.graduation;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviser;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;
import com.gzedu.xlims.pojo.graduation.GjtGraduationDefencePlan;
import com.gzedu.xlims.pojo.graduation.GjtGraduationDefenceTeacher;
import com.gzedu.xlims.pojo.graduation.GjtGraduationSpecialty;
import com.gzedu.xlims.pojo.graduation.GjtGraduationStudentProg;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.GraduationApplyStatusEnum;
import com.gzedu.xlims.pojo.status.GraduationProgressCodeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtGraduationAdviserService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyService;
import com.gzedu.xlims.service.graduation.GjtGraduationBatchService;
import com.gzedu.xlims.service.graduation.GjtGraduationDefencePlanService;
import com.gzedu.xlims.service.graduation.GjtGraduationDefenceTeacherService;
import com.gzedu.xlims.service.graduation.GjtGraduationSpecialtyService;
import com.gzedu.xlims.service.graduation.GjtGraduationStudentProgService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/graduation/specialty")
public class GjtGraduationSpecialtyController {

	private static final Log log = LogFactory.getLog(GjtGraduationSpecialtyController.class);

	@Autowired
	private GjtGraduationSpecialtyService gjtGraduationSpecialtyService;

	@Autowired
	private GjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private GjtGraduationBatchService gjtGraduationBatchService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtGraduationAdviserService gjtGraduationAdviserService;

	@Autowired
	private GjtGraduationStudentProgService gjtGraduationStudentProgService;

	@Autowired
	private GjtGraduationDefencePlanService gjtGraduationDefencePlanService;

	@Autowired
	private GjtGraduationDefenceTeacherService gjtGraduationDefenceTeacherService;

	@Autowired
	private GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	private CommonMapService commonMapService;

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
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		log.info("---------GjtGraduationSpecialtyController.list start--------");
		long startTime = System.currentTimeMillis();

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<Map<String, Object>> pageInfo = gjtGraduationSpecialtyService.queryGraduationSpecialtyList(searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		// 按状态统计
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_orgId", user.getGjtOrg().getId());
		map.put("EQ_status", "1"); // 未开始
		model.addAttribute("notStart",
				gjtGraduationSpecialtyService.queryGraduationSpecialtyList(map, pageRequst).getTotalElements());
		map.put("EQ_status", "2"); // 进行中
		model.addAttribute("starting",
				gjtGraduationSpecialtyService.queryGraduationSpecialtyList(map, pageRequst).getTotalElements());
		map.put("EQ_status", "3"); // 已结束
		model.addAttribute("end",
				gjtGraduationSpecialtyService.queryGraduationSpecialtyList(map, pageRequst).getTotalElements());

		model.addAttribute("batchMap", gjtGraduationBatchService.getGraduationBatchMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());

		long endTime = System.currentTimeMillis();
		log.info("cost time:[" + (endTime - startTime) + "]");
		log.info("---------GjtGraduationSpecialtyController.list end--------");

		return "graduation/specialty/list";
	}

	/**
	 * 返回更新页面
	 * 
	 * @param batchId
	 * @param specialtyId
	 * @param trainingLevel
	 * @param specialtyName
	 * @param trainingName
	 * @param studyYearCode
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String updateForm(@RequestParam("batchId") String batchId, @RequestParam("specialtyId") String specialtyId,
			@RequestParam("trainingLevel") String trainingLevel, @RequestParam("specialtyName") String specialtyName,
			@RequestParam("trainingName") String trainingName, @RequestParam("gradeId") String gradeId,
			@RequestParam("gradeName") String gradeName, Boolean showDefenceSetting, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// 先查询是否存在记录
		GjtGraduationSpecialty gjtGraduationSpecialty = gjtGraduationSpecialtyService.queryOneBySpecialty(batchId,
				specialtyId, trainingLevel);
		if (gjtGraduationSpecialty == null) {
			gjtGraduationSpecialty = new GjtGraduationSpecialty();
			gjtGraduationSpecialty.setSpecialtyId(specialtyId);
			gjtGraduationSpecialty.setTrainingLevel(trainingLevel);

			GjtGraduationBatch gjtGraduationBatch = new GjtGraduationBatch();
			gjtGraduationBatch.setBatchId(batchId);
			gjtGraduationSpecialty.setGjtGraduationBatch(gjtGraduationBatch);
			model.addAttribute("action", "create");
		} else {
			model.addAttribute("action", "update");
		}

		gjtGraduationSpecialty.setSpecialtyName(specialtyName);
		gjtGraduationSpecialty.setTrainingName(trainingName);
		gjtGraduationSpecialty.setGradeId(gradeId);
		gjtGraduationSpecialty.setGradeName(gradeName);;
		model.addAttribute("entity", gjtGraduationSpecialty);

		// 学员申请列表
		List<GjtGraduationApply> applyList = gjtGraduationApplyService.queryListBySpecialty(batchId, specialtyId);
		model.addAttribute("applyList", applyList);

		// 毕业批次
		GjtGraduationBatch graduationBatch = gjtGraduationBatchService.queryById(batchId);
		model.addAttribute("graduationBatch", graduationBatch);

		// 指导老师列表
		List<GjtEmployeeInfo> gjtGraduationAdvisers1 = gjtEmployeeInfoService.findListByType(
				EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文指导教师.getNum(), user.getGjtOrg().getId());
		List<GjtEmployeeInfo> gjtGraduationAdvisers2 = gjtEmployeeInfoService.findListByType(
				EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文答辩教师.getNum(), user.getGjtOrg().getId());
		List<GjtEmployeeInfo> gjtGraduationAdvisers3 = gjtEmployeeInfoService.findListByType(
				EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.社会实践教师.getNum(), user.getGjtOrg().getId());
		model.addAttribute("gjtGraduationAdvisers1", gjtGraduationAdvisers1);
		model.addAttribute("gjtGraduationAdvisers2", gjtGraduationAdvisers2);
		model.addAttribute("gjtGraduationAdvisers3", gjtGraduationAdvisers3);

		// 已经设置的指导老师列表
		String gjtGraduationAdvisers1Value = "";
		String gjtGraduationAdvisers2Value = "";
		String gjtGraduationAdvisers3Value = "";
		if (gjtGraduationSpecialty.getGjtGraduationAdvisers1() != null
				&& gjtGraduationSpecialty.getGjtGraduationAdvisers1().size() > 0) {
			for (GjtGraduationAdviser adviser : gjtGraduationSpecialty.getGjtGraduationAdvisers1()) {
				gjtGraduationAdvisers1Value += adviser.getTeacher().getEmployeeId() + ",";
			}
		}
		if (gjtGraduationSpecialty.getGjtGraduationAdvisers2() != null
				&& gjtGraduationSpecialty.getGjtGraduationAdvisers2().size() > 0) {
			for (GjtGraduationAdviser adviser : gjtGraduationSpecialty.getGjtGraduationAdvisers2()) {
				gjtGraduationAdvisers2Value += adviser.getTeacher().getEmployeeId() + ",";
			}
		}
		if (gjtGraduationSpecialty.getGjtGraduationAdvisers3() != null
				&& gjtGraduationSpecialty.getGjtGraduationAdvisers3().size() > 0) {
			for (GjtGraduationAdviser adviser : gjtGraduationSpecialty.getGjtGraduationAdvisers3()) {
				gjtGraduationAdvisers3Value += adviser.getTeacher().getEmployeeId() + ",";
			}
		}
		model.addAttribute("gjtGraduationAdvisers1Value", gjtGraduationAdvisers1Value);
		model.addAttribute("gjtGraduationAdvisers2Value", gjtGraduationAdvisers2Value);
		model.addAttribute("gjtGraduationAdvisers3Value", gjtGraduationAdvisers3Value);

		return "graduation/specialty/form";
	}

	/**
	 * 新增记录
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtGraduationSpecialty entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			entity.setCreatedBy(user.getId());
			this.setGjtGraduationAdvisers(entity);
			gjtGraduationSpecialtyService.insert(entity);

			this.dispatchTeacher(entity, user);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/specialty/list";
	}

	/**
	 * 修改记录
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid GjtGraduationSpecialty entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");

		try {
			// 先删除指导老师
			gjtGraduationAdviserService.deleteBySettingId(entity.getSettingId());

			this.setGjtGraduationAdvisers(entity);

			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtGraduationSpecialty specialty = gjtGraduationSpecialtyService.queryById(entity.getSettingId());
			specialty.setTopic(entity.getTopic());
			specialty.setExample(entity.getExample());
			specialty.setExampleName(entity.getExampleName());
			specialty.setUpdatedBy(user.getId());
			specialty.setGjtGraduationAdvisers(entity.getGjtGraduationAdvisers());

			gjtGraduationSpecialtyService.update(specialty);
			this.dispatchTeacher(entity, user);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/specialty/list";
	}

	/**
	 * 设置指导老师
	 * 
	 * @param entity
	 */
	private void setGjtGraduationAdvisers(GjtGraduationSpecialty entity) {
		entity.setGjtGraduationAdvisers(new ArrayList<GjtGraduationAdviser>());
		if (entity.getAdvisers1() != null && entity.getAdvisers1().length > 0) {
			for (String teacherId : entity.getAdvisers1()) {
				GjtEmployeeInfo teacher = new GjtEmployeeInfo();
				teacher.setEmployeeId(teacherId);

				GjtGraduationAdviser adviser = new GjtGraduationAdviser();
				adviser.setAdviserId(UUIDUtils.random());
				adviser.setAdviserType(1);
				adviser.setTeacher(teacher);
				adviser.setGjtGraduationSpecialty(entity);

				entity.getGjtGraduationAdvisers().add(adviser);
			}
		}
		if (entity.getAdvisers2() != null && entity.getAdvisers2().length > 0) {
			for (String teacherId : entity.getAdvisers2()) {
				GjtEmployeeInfo teacher = new GjtEmployeeInfo();
				teacher.setEmployeeId(teacherId);

				GjtGraduationAdviser adviser = new GjtGraduationAdviser();
				adviser.setAdviserId(UUIDUtils.random());
				adviser.setAdviserType(2);
				adviser.setTeacher(teacher);
				adviser.setGjtGraduationSpecialty(entity);

				entity.getGjtGraduationAdvisers().add(adviser);
			}
		}
		if (entity.getAdvisers3() != null && entity.getAdvisers3().length > 0) {
			for (String teacherId : entity.getAdvisers3()) {
				GjtEmployeeInfo teacher = new GjtEmployeeInfo();
				teacher.setEmployeeId(teacherId);

				GjtGraduationAdviser adviser = new GjtGraduationAdviser();
				adviser.setAdviserId(UUIDUtils.random());
				adviser.setAdviserType(3);
				adviser.setTeacher(teacher);
				adviser.setGjtGraduationSpecialty(entity);

				entity.getGjtGraduationAdvisers().add(adviser);
			}
		}

	}

	/**
	 * 分配指导老师
	 * 
	 * @param entity
	 */
	private void dispatchTeacher(GjtGraduationSpecialty entity, GjtUserAccount user) {
		GjtGraduationSpecialty specialty = gjtGraduationSpecialtyService.queryById(entity.getSettingId());
		List<GjtGraduationAdviser> adviserList = specialty.getGjtGraduationAdvisers();
		List<GjtGraduationAdviser> adviserList1 = new ArrayList<GjtGraduationAdviser>();
		List<GjtGraduationAdviser> adviserList3 = new ArrayList<GjtGraduationAdviser>();

		if (adviserList != null && adviserList.size() > 0) {
			for (GjtGraduationAdviser adviser : adviserList) {
				if (adviser.getAdviserType() == 1) {
					adviserList1.add(adviser);
				} else if (adviser.getAdviserType() == 3) {
					adviserList3.add(adviser);
				}
			}
		}

		// 学员申请列表
		List<GjtGraduationApply> applyList = gjtGraduationApplyService
				.queryListBySpecialty(entity.getGjtGraduationBatch().getBatchId(), entity.getSpecialtyId());
		// 申请毕业论文的列表
		List<GjtGraduationApply> applyThesisList = new ArrayList<GjtGraduationApply>();
		// 申请社会实践的列表
		List<GjtGraduationApply> applyPracticeList = new ArrayList<GjtGraduationApply>();

		if (applyList != null && applyList.size() > 0) {
			for (GjtGraduationApply apply : applyList) {
				if (apply.getApplyType() == 1) {
					applyThesisList.add(apply);
				} else if (apply.getApplyType() == 2) {
					applyPracticeList.add(apply);
				}
			}
		}

		// 分配毕业论文指导老师
		if (adviserList1.size() > 0 && applyThesisList.size() > 0) {
			int num = 1; // 每个老师分配的人数
			if (applyThesisList.size() % adviserList1.size() == 0) {
				num = applyThesisList.size() / adviserList1.size();
			} else {
				num = (applyThesisList.size() / adviserList1.size()) + 1;
			}

			for (int i = 0; i < adviserList1.size(); i++) {
				for (int j = i * num; j < (i + 1) * num && j < applyThesisList.size(); j++) {
					applyThesisList.get(j).setGuideTeacher(adviserList1.get(i).getTeacher().getEmployeeId());
					applyThesisList.get(j).setUpdatedDt(new Date());
					applyThesisList.get(j).setUpdatedBy(user.getId());

					// 当状态等于“已申请”的时候，修改状态为“待开题”
					if (applyThesisList.get(j).getStatus() == GraduationApplyStatusEnum.THESIS_APPLY.getValue()) {
						applyThesisList.get(j).setStatus(GraduationApplyStatusEnum.THESIS_STAY_OPEN.getValue());
					}
				}
			}
		}

		// 分配社会实践指导老师
		if (adviserList3.size() > 0 && applyPracticeList.size() > 0) {
			int num = 1; // 每个老师分配的人数
			if (applyPracticeList.size() % adviserList3.size() == 0) {
				num = applyPracticeList.size() / adviserList3.size();
			} else {
				num = (applyPracticeList.size() / adviserList3.size()) + 1;
			}

			for (int i = 0; i < adviserList3.size(); i++) {
				for (int j = i * num; j < (i + 1) * num && j < applyPracticeList.size(); j++) {
					applyPracticeList.get(j).setGuideTeacher(adviserList3.get(i).getTeacher().getEmployeeId());
					applyPracticeList.get(j).setUpdatedDt(new Date());
					applyPracticeList.get(j).setUpdatedBy(user.getId());

					// 当状态等于“已申请”的时候，修改状态为“待提交初稿”
					if (applyPracticeList.get(j).getStatus() == GraduationApplyStatusEnum.PRACTICE_APPLY.getValue()) {
						applyPracticeList.get(j).setStatus(GraduationApplyStatusEnum.PRACTICE_STAY_OPEN.getValue());
					}
				}
			}
		}

		if (applyList != null && applyList.size() > 0) {
			gjtGraduationApplyService.update(applyList);
			this.saveProgressLog(applyList, user);
		}
	}

	/**
	 * 保存学生的进度
	 * 
	 * @param applyList
	 * @param user
	 */
	private void saveProgressLog(List<GjtGraduationApply> applyList, GjtUserAccount user) {
		List<GjtGraduationStudentProg> progresses = new ArrayList<GjtGraduationStudentProg>();
		for (GjtGraduationApply apply : applyList) {
			if (apply.getGuideTeacher() != null && !"".equals(apply.getGuideTeacher())) { // 分配了指导老师才需要新增进度
				// 先查询进度是否已添加
				GjtGraduationStudentProg studentProg = gjtGraduationStudentProgService.queryOneByCode(
						apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(),
						apply.getApplyType(),
						apply.getApplyType() == 1 ? GraduationProgressCodeEnum.THESIS_STAY_OPEN.getCode()
								: GraduationProgressCodeEnum.PRACTICE_STAY_OPEN.getCode());
				if (studentProg == null) {
					GjtGraduationStudentProg progress = new GjtGraduationStudentProg();
					progress.setProgressId(UUIDUtils.random());
					progress.setCreatedDt(new Date());
					progress.setCreatedBy(user.getId());
					progress.setStudentId(apply.getGjtStudentInfo().getStudentId());
					progress.setGjtGraduationBatch(apply.getGjtGraduationBatch());

					if (apply.getApplyType() == 1) {
						progress.setProgressType(1);
						progress.setProgressCode(GraduationProgressCodeEnum.THESIS_STAY_OPEN.getCode());
					} else {
						progress.setProgressType(2);
						progress.setProgressCode(GraduationProgressCodeEnum.PRACTICE_STAY_OPEN.getCode());
					}

					progresses.add(progress);
				}
			}
		}

		if (progresses.size() > 0) {
			gjtGraduationStudentProgService.insert(progresses);
		}
	}

	/**
	 * 保存毕业专业答辩安排
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "savePlan", method = RequestMethod.POST)
	public String savePlan(GjtGraduationDefencePlan entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws UnsupportedEncodingException {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			this.setGjtGraduationDefenceTeachers(entity);
			if (entity.getDefencePlace() == null || "".equals(entity.getDefencePlace())) {
				entity.setDefencePlace("--");
			}
			if (entity.getTrafficGuidance() == null || "".equals(entity.getTrafficGuidance())) {
				entity.setTrafficGuidance("--");
			}

			if (entity.getPlanId() != null && !"".equals(entity.getPlanId())) {
				// 更新操作：先删除答辩老师，再插入
				gjtGraduationDefenceTeacherService.deleteByPlanId(entity.getPlanId());

				GjtGraduationDefencePlan defencePlan = gjtGraduationDefencePlanService.queryById(entity.getPlanId());
				defencePlan.setBeginTime(entity.getBeginTime());
				defencePlan.setEndTime(entity.getEndTime());
				defencePlan.setDefenceType(entity.getDefenceType());
				defencePlan.setDefencePlace(entity.getDefencePlace());
				defencePlan.setTrafficGuidance(entity.getTrafficGuidance());
				defencePlan.setDefenceNum(entity.getDefenceNum());
				defencePlan.setUpdatedBy(user.getId());
				defencePlan.setGjtGraduationDefenceTeachers(entity.getGjtGraduationDefenceTeachers());
				gjtGraduationDefencePlanService.update(defencePlan);
				feedback = new Feedback(true, "修改成功");
			} else {
				// 新增操作
				entity.setCreatedBy(user.getId());
				gjtGraduationDefencePlanService.insert(entity);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);

		GjtGraduationSpecialty specialty = gjtGraduationSpecialtyService.queryById(entity.getSettingId());
		GjtSpecialty specialty2 = gjtSpecialtyService.findBySpecialtyById(specialty.getSpecialtyId());
		Map<String, String> pyccMap = commonMapService.getPyccMap();

		return "redirect:/graduation/specialty/update?batchId=" + specialty.getGjtGraduationBatch().getBatchId()
				+ "&specialtyId=" + specialty.getSpecialtyId() + "&trainingLevel=" + specialty.getTrainingLevel()
				+ "&specialtyName=" + java.net.URLEncoder.encode(specialty2.getZymc(), "UTF-8") + "&trainingName="
				+ java.net.URLEncoder.encode(pyccMap.get(specialty.getTrainingLevel()), "UTF-8") + "&studyYearCode="
				+ specialty.getGjtGraduationBatch().getStudyYearCode() + "&showDefenceSetting=" + true;
	}

	/**
	 * 设置答辩老师
	 * 
	 * @param entity
	 */
	private void setGjtGraduationDefenceTeachers(GjtGraduationDefencePlan entity) {
		entity.setGjtGraduationDefenceTeachers(new ArrayList<GjtGraduationDefenceTeacher>());
		if (entity.getDefenceTeachers1() != null && entity.getDefenceTeachers1().length > 0) {
			for (String defenceTeacherId : entity.getDefenceTeachers1()) {
				GjtEmployeeInfo teacher = new GjtEmployeeInfo();
				teacher.setEmployeeId(defenceTeacherId);

				GjtGraduationDefenceTeacher defenceTeacher = new GjtGraduationDefenceTeacher();
				defenceTeacher.setId(UUIDUtils.random());
				defenceTeacher.setType(1);
				defenceTeacher.setTeacher(teacher);
				defenceTeacher.setGjtGraduationDefencePlan(entity);

				entity.getGjtGraduationDefenceTeachers().add(defenceTeacher);
			}
		}

		if (entity.getDefenceTeachers2() != null && entity.getDefenceTeachers2().length > 0) {
			for (String defenceTeacherId : entity.getDefenceTeachers2()) {
				GjtEmployeeInfo teacher = new GjtEmployeeInfo();
				teacher.setEmployeeId(defenceTeacherId);

				GjtGraduationDefenceTeacher defenceTeacher = new GjtGraduationDefenceTeacher();
				defenceTeacher.setId(UUIDUtils.random());
				defenceTeacher.setType(2);
				defenceTeacher.setTeacher(teacher);
				defenceTeacher.setGjtGraduationDefencePlan(entity);

				entity.getGjtGraduationDefenceTeachers().add(defenceTeacher);
			}
		}
	}

	/**
	 * 查找毕业专业答辩安排，返回json格式
	 * 
	 * @param planId
	 * @return
	 */
	@RequestMapping(value = "findPlan", method = RequestMethod.GET)
	@ResponseBody
	public GjtGraduationDefencePlan findPlan(@RequestParam("planId") String planId) {
		log.info("planId:" + planId);
		return gjtGraduationDefencePlanService.queryById(planId);
	}

	/**
	 * 逻辑删除毕业专业答辩安排
	 * 
	 * @param planId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "deletePlan")
	@ResponseBody
	public Feedback deletePlan(@RequestParam("planId") String planId, HttpServletRequest request) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtGraduationDefencePlan defencePlan = gjtGraduationDefencePlanService.queryById(planId);
			defencePlan.setDeletedBy(user.getId());
			gjtGraduationDefencePlanService.delete(defencePlan);
			return new Feedback(true, "删除成功");
		} catch (Exception e) {
			return new Feedback(false, "删除失败，原因:" + e.getMessage());
		}
	}

	/**
	 * 分配答辩场次
	 * 
	 * @param batchId
	 * @param specialtyId
	 * @param trainingLevel
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "dispatch")
	@ResponseBody
	public Feedback dispatch(@RequestParam("batchId") String batchId, @RequestParam("specialtyId") String specialtyId,
			@RequestParam("trainingLevel") String trainingLevel, HttpServletRequest request) {
		try {
			GjtGraduationSpecialty gjtGraduationSpecialty = gjtGraduationSpecialtyService.queryOneBySpecialty(batchId,
					specialtyId, trainingLevel);
			if (gjtGraduationSpecialty != null) {
				// 现场答辩安排
				List<GjtGraduationDefencePlan> defencePlans1 = gjtGraduationSpecialty.getGjtGraduationDefencePlans1();
				// 远程答辩安排
				List<GjtGraduationDefencePlan> defencePlans2 = gjtGraduationSpecialty.getGjtGraduationDefencePlans2();
				if ((defencePlans1 != null && defencePlans1.size() > 0)
						|| (defencePlans2 != null && defencePlans2.size() > 0)) {
					GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

					if (defencePlans1 != null && defencePlans1.size() > 0) {
						// 查询可分配答辩场次的现场答辩的学员列表
						Set<Integer> status = new HashSet<Integer>();
						status.add(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
						status.add(GraduationApplyStatusEnum.THESIS_DEFENCE.getValue());
						List<GjtGraduationApply> applyList = gjtGraduationApplyService.queryList(batchId, specialtyId,
								1, 1, status);
						if (applyList != null && applyList.size() > 0) {
							int i = 0;
							for (GjtGraduationDefencePlan defencePlan : defencePlans1) {
								for (int j = 0; j < defencePlan.getDefenceNum() && i < applyList.size(); j++, i++) {
									GjtGraduationApply apply = applyList.get(i);
									apply.setGjtGraduationDefencePlan(defencePlan);
									apply.setStatus(GraduationApplyStatusEnum.THESIS_DEFENCE.getValue());
									apply.setUpdatedBy(user.getId());
									apply.setUpdatedDt(new Date());
								}
								if (i >= applyList.size()) {
									break;
								}
							}

							// 剩下的学员设置状态为"答辩安排中"
							while (i < applyList.size()) {
								GjtGraduationApply apply = applyList.get(i);
								apply.setGjtGraduationDefencePlan(null);
								apply.setStatus(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
								apply.setUpdatedBy(user.getId());
								apply.setUpdatedDt(new Date());
								i++;
							}

							gjtGraduationApplyService.update(applyList);
							this.saveProgressLog2(applyList, user);
						}
					} else {
						// 查询已分配答辩场次的现场答辩的学员列表，还原成"答辩安排中"
						Set<Integer> status = new HashSet<Integer>();
						status.add(GraduationApplyStatusEnum.THESIS_DEFENCE.getValue());
						List<GjtGraduationApply> applyList = gjtGraduationApplyService.queryList(batchId, specialtyId,
								1, 1, status);
						if (applyList != null && applyList.size() > 0) {
							for (GjtGraduationApply apply : applyList) {
								apply.setGjtGraduationDefencePlan(null);
								apply.setStatus(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
								apply.setUpdatedBy(user.getId());
								apply.setUpdatedDt(new Date());
							}

							gjtGraduationApplyService.update(applyList);
							this.saveProgressLog2(applyList, user);
						}
					}

					if (defencePlans2 != null && defencePlans2.size() > 0) {
						// 查询可分配答辩场次的远程答辩的学员列表
						Set<Integer> status = new HashSet<Integer>();
						status.add(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
						status.add(GraduationApplyStatusEnum.THESIS_DEFENCE.getValue());
						List<GjtGraduationApply> applyList = gjtGraduationApplyService.queryList(batchId, specialtyId,
								1, 2, status);
						if (applyList != null && applyList.size() > 0) {
							int i = 0;
							for (GjtGraduationDefencePlan defencePlan : defencePlans2) {
								for (int j = 0; j < defencePlan.getDefenceNum() && i < applyList.size(); j++, i++) {
									GjtGraduationApply apply = applyList.get(i);
									apply.setGjtGraduationDefencePlan(defencePlan);
									apply.setStatus(GraduationApplyStatusEnum.THESIS_DEFENCE.getValue());
									apply.setUpdatedBy(user.getId());
									apply.setUpdatedDt(new Date());
								}
								if (i >= applyList.size()) {
									break;
								}
							}

							// 剩下的学员设置状态为"答辩安排中"
							while (i < applyList.size()) {
								GjtGraduationApply apply = applyList.get(i);
								apply.setGjtGraduationDefencePlan(null);
								apply.setStatus(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
								apply.setUpdatedBy(user.getId());
								apply.setUpdatedDt(new Date());
								i++;
							}

							gjtGraduationApplyService.update(applyList);
							this.saveProgressLog2(applyList, user);
						}
					} else {
						// 查询已分配答辩场次的远程答辩的学员列表，还原成"答辩安排中"
						Set<Integer> status = new HashSet<Integer>();
						status.add(GraduationApplyStatusEnum.THESIS_DEFENCE.getValue());
						List<GjtGraduationApply> applyList = gjtGraduationApplyService.queryList(batchId, specialtyId,
								1, 2, status);
						if (applyList != null && applyList.size() > 0) {
							for (GjtGraduationApply apply : applyList) {
								apply.setGjtGraduationDefencePlan(null);
								apply.setStatus(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
								apply.setUpdatedBy(user.getId());
								apply.setUpdatedDt(new Date());
							}

							gjtGraduationApplyService.update(applyList);
							this.saveProgressLog2(applyList, user);
						}
					}

					return new Feedback(true, "分配成功");
				} else {
					return new Feedback(false, "还未设置相关安排！");
				}
			} else {
				return new Feedback(false, "还未设置相关安排！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "分配失败，原因:" + e.getMessage());
		}
	}

	/**
	 * 保存学生的进度
	 * 
	 * @param applyList
	 * @param user
	 */
	private void saveProgressLog2(List<GjtGraduationApply> applyList, GjtUserAccount user) {
		List<GjtGraduationStudentProg> progresses = new ArrayList<GjtGraduationStudentProg>();
		List<GjtGraduationStudentProg> progresses2 = new ArrayList<GjtGraduationStudentProg>();
		for (GjtGraduationApply apply : applyList) {
			if (apply.getGjtGraduationDefencePlan() != null) { // 分配了答辩场次才需要新增进度
				// 先查询进度是否已添加
				GjtGraduationStudentProg studentProg = gjtGraduationStudentProgService.queryOneByCode(
						apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(),
						apply.getApplyType(), GraduationProgressCodeEnum.THESIS_STAY_DEFENCE.getCode());
				if (studentProg == null) {
					GjtGraduationStudentProg progress = new GjtGraduationStudentProg();
					progress.setProgressId(UUIDUtils.random());
					progress.setCreatedDt(new Date());
					progress.setCreatedBy(user.getId());
					progress.setStudentId(apply.getGjtStudentInfo().getStudentId());
					progress.setGjtGraduationBatch(apply.getGjtGraduationBatch());
					progress.setProgressType(1);
					progress.setProgressCode(GraduationProgressCodeEnum.THESIS_STAY_DEFENCE.getCode());

					progresses.add(progress);
				}
			} else {
				// 删除已有的进度
				GjtGraduationStudentProg studentProg = gjtGraduationStudentProgService.queryOneByCode(
						apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(),
						apply.getApplyType(), GraduationProgressCodeEnum.THESIS_STAY_DEFENCE.getCode());
				if (studentProg != null) {
					progresses2.add(studentProg);
				}
			}
		}

		if (progresses.size() > 0) {
			gjtGraduationStudentProgService.insert(progresses);
		}
		if (progresses2.size() > 0) {
			gjtGraduationStudentProgService.delete(progresses2);
		}
	}

	/**
	 * 前后端日期格式转换
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值
	}

}
