package com.gzedu.xlims.web.controller.graduation;

import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
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
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.graduation.GjtGraduationGuideRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationStudentProg;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.GraduationApplyStatusEnum;
import com.gzedu.xlims.pojo.status.GraduationProgressCodeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtGraduationAdviserService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyService;
import com.gzedu.xlims.service.graduation.GjtGraduationBatchService;
import com.gzedu.xlims.service.graduation.GjtGraduationGuideRecordService;
import com.gzedu.xlims.service.graduation.GjtGraduationStudentProgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/graduation/adviser")
public class GjtGraduationAdviserController {
	
	private static final Log log = LogFactory.getLog(GjtGraduationAdviserController.class);

	@Autowired
	private GjtGraduationAdviserService gjtGraduationAdviserService;
	
	@Autowired
	private GjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private GjtGraduationBatchService gjtGraduationBatchService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtGraduationStudentProgService gjtGraduationStudentProgService;

	@Autowired
	private GjtGraduationGuideRecordService gjtGraduationGuideRecordService;
	
	/**
	 * 查询指导老师信息
	 * @param pageNumber
	 * @param pageSize
	 * @param adviserType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam("adviserType") int adviserType, Model model, HttpServletRequest request) {
		log.info("adviserType:" + adviserType);
		
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		
		Page<Map<String, Object>> pageInfo = gjtGraduationAdviserService.queryGraduationAdviser(adviserType, searchParams, pageRequst);
		if (pageInfo.getContent() != null && pageInfo.getContent().size() > 0) {
			for (Map<String, Object> teacher : pageInfo.getContent()) {
				//指导专业类型转换
				Object obj = teacher.get("specialtyNames");
				//SerializableClobProxy proxy = (SerializableClobProxy)Proxy.getInvocationHandler(obj);
				//Clob specialtyNames = proxy.getWrappedClob();
				//teacher.put("specialtyNames", clobToString(specialtyNames));
				//teacher.put("specialtyNames", clobToString((Clob)obj));
				teacher.put("specialtyNames", (String)obj);  //这里在预生成环境查出来的直接是string，不需要转换，测试环境查出来的是Clob
				
				if (adviserType == 1) {  //论文指导老师
					Map<String, Object> map = gjtGraduationApplyService.queryGraduationApplyCount(teacher.get("batchId").toString(), 1, 1, teacher.get("teacherId").toString());
					teacher.putAll(map);
				} else if (adviserType == 2) {  //论文答辩老师
					Map<String, Object> map = gjtGraduationApplyService.queryGraduationApplyCount(teacher.get("batchId").toString(), 1, 2, teacher.get("teacherId").toString());
					teacher.putAll(map);
				} else if (adviserType == 3) {  //社会实践指导老师
					Map<String, Object> map = gjtGraduationApplyService.queryGraduationApplyCount(teacher.get("batchId").toString(), 2, 1, teacher.get("teacherId").toString());
					teacher.putAll(map);
				}
				
			}
		}
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("batchMap", gjtGraduationBatchService.getGraduationBatchMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));

		return "graduation/adviser/list";
	}
	
	/**
	 * 查询指导学生列表
	 * @param pageNumber
	 * @param pageSize
	 * @param teacherId
	 * @param applyType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "viewStudentList", method = RequestMethod.GET)
	public String viewStudentList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam(value = "teacherId", required = false) String teacherId,  @RequestParam("applyType") int applyType, 
			Model model, HttpServletRequest request) {
		log.info("teacherId:" + teacherId + ", applyType:" + applyType);
		
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (teacherId == null || "".equals(teacherId)) {
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
			if (employeeInfo != null) {
				teacherId = employeeInfo.getEmployeeId();
			} else {
				teacherId = "0";
			}
		}
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_applyType", applyType);
		searchParams.put("EQ_guideTeacher", teacherId);
		
		Page<GjtGraduationApply> pageInfo = gjtGraduationApplyService.queryPage(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("batchMap", gjtGraduationBatchService.getGraduationBatchMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		model.addAttribute("statusMap", EnumUtil.getThesisApplyStatusMap());
		
		return "graduation/adviser/studentList";
	}
	
	/**
	 * 查询学生指导记录
	 * @param studentId
	 * @param applyType
	 * @param batchId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "viewStudentRecord", method = RequestMethod.GET)
	public String viewStudentRecord(@RequestParam("studentId") String studentId,  @RequestParam("applyType") int applyType,
			@RequestParam("batchId") String batchId, Model model, HttpServletRequest request) {
		log.info("studentId:" + studentId + ", applyType:" + applyType + ", batchId:" + batchId);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);
		GjtGraduationApply apply = gjtGraduationApplyService.queryOneByStudent(batchId, studentId, applyType);
		List<GjtGraduationStudentProg> progList = gjtGraduationStudentProgService.queryListByStudent(batchId, studentId, applyType);
		List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudent(batchId, studentId, applyType);
		
		//构建进度和进度对应的指导记录
		Map<String, List<GjtGraduationGuideRecord>> progRecord = new HashMap<String, List<GjtGraduationGuideRecord>>();
		if (progList != null && progList.size() > 0 && recordList != null && recordList.size() > 0) {
			for (GjtGraduationStudentProg prog : progList) {
				for (GjtGraduationGuideRecord record : recordList) {
					if (record.getProgressCode().equals(prog.getProgressCode())) {
						List<GjtGraduationGuideRecord> records = progRecord.get(prog.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtGraduationGuideRecord>();
							records.add(record);
							progRecord.put(prog.getProgressId(), records);
						} else {
							records.add(record);
						}
					}
				}
			}
		}
		
		model.addAttribute("studentInfo", gjtStudentInfo);
		model.addAttribute("apply", apply);
		model.addAttribute("progList", progList);
		model.addAttribute("progRecord", progRecord);
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		
		if (applyType == 1) {
			model.addAttribute("thesisProgressCodeMap", EnumUtil.getThesisProgressCodeMap());
			return "graduation/adviser/studentRecord1";
		} else if (applyType == 2) {
			model.addAttribute("practiceProgressCodeMap", EnumUtil.getPracticeProgressCodeMap());
			return "graduation/adviser/studentRecord2";
		}

		return "";
	}
	
	/**
	 * 进入我的指导列表
	 * @param pageNumber
	 * @param pageSize
	 * @param teacherId
	 * @param applyType
	 * @param orgId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "viewMyGuideList", method = RequestMethod.GET)
	public String viewMyGuideList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam("applyType") int applyType, Model model, HttpServletRequest request) {
		log.info( "applyType:" + applyType);
		
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "updatedDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_applyType", applyType);
		
		String teacherId = "0";
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
		if (employeeInfo != null) {
			teacherId = employeeInfo.getEmployeeId();
		}
		searchParams.put("EQ_guideTeacher", teacherId);

		Set<Integer> set = new HashSet<Integer>();
		if (applyType == 1) {
			//只查询“已开题”和“论文写作中”的状态
			set.add(GraduationApplyStatusEnum.THESIS_SUBMIT_PROPOSE.getValue());
			set.add(GraduationApplyStatusEnum.THESIS_SUBMIT_THESIS.getValue());
		} else if (applyType == 2) {
			//只查询“待定稿”的状态
			set.add(GraduationApplyStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue());
		}
		searchParams.put("IN_status", set);
		
		Page<GjtGraduationApply> pageInfo = gjtGraduationApplyService.queryMyGuideList(searchParams, pageRequst);
		List<GjtGraduationApply> applyList = pageInfo.getContent();
		Map<String, GjtGraduationGuideRecord> records = new HashMap<String, GjtGraduationGuideRecord>();
		if (applyList != null && applyList.size() > 0) {
			for(GjtGraduationApply apply : applyList) {
				if (applyType == 1) {
					//如果是在开题报告阶段，查询最后一次提交的开题报告
					if (apply.getStatus() == GraduationApplyStatusEnum.THESIS_SUBMIT_PROPOSE.getValue()
							|| apply.getStatus() == GraduationApplyStatusEnum.THESIS_CONFIRM_PROPOSE.getValue()) {
						List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudentAndCode(apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(), applyType, GraduationProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
						if (recordList != null && recordList.size() > 0) {
							records.put(apply.getApplyId(), recordList.get(recordList.size() - 1));
						}
					//如果是在论文写作阶段，查询最后一次提交的论文
					} else if (apply.getStatus() == GraduationApplyStatusEnum.THESIS_SUBMIT_THESIS.getValue()
							|| apply.getStatus() == GraduationApplyStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue()
							|| apply.getStatus() == GraduationApplyStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue()) {
						List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudentAndCode(apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(), applyType, GraduationProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
						if (recordList != null && recordList.size() > 0) {
							records.put(apply.getApplyId(), recordList.get(recordList.size() - 1));
						}
					}
				} else if (applyType == 2) {
					//如果是在论文写作阶段，查询最后一次提交的论文
					if (apply.getStatus() == GraduationApplyStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue()
							|| apply.getStatus() == GraduationApplyStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue()) {
						List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudentAndCode(apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(), applyType, GraduationProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
						if (recordList != null && recordList.size() > 0) {
							records.put(apply.getApplyId(), recordList.get(recordList.size() - 1));
						}
					}
				}
			}
		}
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("records", records);
		model.addAttribute("batchMap", gjtGraduationBatchService.getGraduationBatchMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		
		if (applyType == 1) {
			model.addAttribute("statusMap", EnumUtil.getThesisApplyStatusMap());
		} else if (applyType == 2) {
			model.addAttribute("statusMap", EnumUtil.getPracticeApplyStatusMap());
		}

		return "graduation/adviser/myGuideList";
	}

	/**
	 * 提交指导记录
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "submitRecord", method = RequestMethod.POST)
	@ResponseBody
	public String submitRecord(@Valid GjtGraduationGuideRecord entity, HttpServletRequest request) {
		log.info("entity:[" + entity + "]");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String teacherId = "0";
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
			if (employeeInfo != null) {
				teacherId = employeeInfo.getEmployeeId();
			} else {
				return "操作非法！";
			}
			entity.setTeacherId(teacherId);
			
			GjtGraduationApply apply = gjtGraduationApplyService.queryOneByStudent(entity.getGjtGraduationBatch().getBatchId(), entity.getGjtStudentInfo().getStudentId(), entity.getRecordType());
			if (apply == null) {
				return "操作非法！";
			}
			
			if (apply.getApplyType() == 1 && apply.getStatus() == GraduationApplyStatusEnum.THESIS_SUBMIT_PROPOSE.getValue()) {
				//提交开题报告阶段的指导
				entity.setProgressCode(GraduationProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
			} else if (apply.getApplyType() == 1 && apply.getStatus() == GraduationApplyStatusEnum.THESIS_SUBMIT_THESIS.getValue()) {
				//提交论文初稿阶段的指导
				entity.setProgressCode(GraduationProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
			} else if (apply.getApplyType() == 2 && apply.getStatus() == GraduationApplyStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue()) {
				//提交社会实践初稿阶段的指导
				entity.setProgressCode(GraduationProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
			} else {
				return "操作非法！";
			}
			
			entity.setIsStudent(0);
			entity.setCreatedBy(user.getId());
			gjtGraduationGuideRecordService.insert(entity);
			
			if (apply.getApplyType() == 1) {
				try {
					this.sendMsg(entity.getGjtStudentInfo().getStudentId(), 1);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "提交失败！";
		}
		
		return "提交成功";
	}

	/**
	 * 定稿
	 * @param batchId
	 * @param studentId
	 * @param teacherId
	 * @param applyType
	 * @param orgId
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "confirm", method = RequestMethod.POST)
	public String confirm(@RequestParam("batchId") String batchId, @RequestParam("studentId") String studentId,
			@RequestParam("applyType") int applyType, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "提交成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtGraduationApply apply = gjtGraduationApplyService.queryOneByStudent(batchId, studentId, applyType);
			if (apply == null) {
				feedback = new Feedback(false, "操作非法！");
			} else {
				String teacherId = "0";
				GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
				if (employeeInfo != null) {
					teacherId = employeeInfo.getEmployeeId();
				}
				
				String userId = user.getId();
				
				GjtGraduationStudentProg prog = new GjtGraduationStudentProg();
				prog.setStudentId(studentId);
				prog.setProgressType(applyType);
				prog.setGjtGraduationBatch(apply.getGjtGraduationBatch());
				prog.setCreatedBy(userId);
				
				GjtGraduationGuideRecord record = new GjtGraduationGuideRecord();
				record.setCreatedBy(userId);
				record.setIsStudent(0);
				record.setRecordType(applyType);
				record.setGjtStudentInfo(apply.getGjtStudentInfo());
				record.setTeacherId(teacherId);
				record.setGjtGraduationBatch(apply.getGjtGraduationBatch());
				
				if (apply.getApplyType() == 1 && apply.getStatus() == GraduationApplyStatusEnum.THESIS_SUBMIT_PROPOSE.getValue()) {
					//开题报告定稿
					List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudentAndCode(apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(), applyType, GraduationProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
					if (recordList != null && recordList.size() > 0) {
						GjtGraduationGuideRecord record2 = recordList.get(recordList.size() - 1);
						//更新申请状态
						apply.setStatus(GraduationApplyStatusEnum.THESIS_CONFIRM_PROPOSE.getValue());
						apply.setUpdatedBy(userId);
						gjtGraduationApplyService.update(apply);
						
						//新增学员进度
						prog.setProgressCode(GraduationProgressCodeEnum.THESIS_CONFIRM_PROPOSE.getCode());
						gjtGraduationStudentProgService.insert(prog);

						//新增指导记录
						record.setProgressCode(GraduationProgressCodeEnum.THESIS_CONFIRM_PROPOSE.getCode());
						record.setAttachment(record2.getAttachment());
						record.setAttachmentName(record2.getAttachmentName());
						record.setContent("对我的开题报告进行了定稿");
						gjtGraduationGuideRecordService.insert(record);
						
						try {
							this.sendMsg(record.getGjtStudentInfo().getStudentId(), 2);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					} else {
						feedback = new Feedback(false, "指导学员还未提交开题报告！");
					}
				} else if (apply.getApplyType() == 1 && apply.getStatus() == GraduationApplyStatusEnum.THESIS_SUBMIT_THESIS.getValue()) {
					//论文定稿
					List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudentAndCode(apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(), applyType, GraduationProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
					if (recordList != null && recordList.size() > 0) {
						GjtGraduationGuideRecord record2 = recordList.get(recordList.size() - 1);
						//更新申请状态
						apply.setStatus(GraduationApplyStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue());
						apply.setUpdatedBy(userId);
						gjtGraduationApplyService.update(apply);
						
						//新增学员进度
						prog.setProgressCode(GraduationProgressCodeEnum.THESIS_TEACHER_CONFIRM_THESIS.getCode());
						gjtGraduationStudentProgService.insert(prog);

						//新增指导记录
						record.setProgressCode(GraduationProgressCodeEnum.THESIS_TEACHER_CONFIRM_THESIS.getCode());
						record.setAttachment(record2.getAttachment());
						record.setAttachmentName(record2.getAttachmentName());
						record.setContent("对我的论文进行了定稿");
						gjtGraduationGuideRecordService.insert(record);
					} else {
						feedback = new Feedback(false, "指导学员还未提交论文！");
					}
				} else if (apply.getApplyType() == 2 && apply.getStatus() == GraduationApplyStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue()) {
					//社会实践定稿
					List<GjtGraduationGuideRecord> recordList = gjtGraduationGuideRecordService.queryListByStudentAndCode(apply.getGjtGraduationBatch().getBatchId(), apply.getGjtStudentInfo().getStudentId(), applyType, GraduationProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
					if (recordList != null && recordList.size() > 0) {
						GjtGraduationGuideRecord record2 = recordList.get(recordList.size() - 1);
						//更新申请状态
						apply.setStatus(GraduationApplyStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue());
						apply.setUpdatedBy(userId);
						gjtGraduationApplyService.update(apply);
						
						//新增学员进度
						prog.setProgressCode(GraduationProgressCodeEnum.PRACTICE_CONFIRM_PRACTICE.getCode());
						gjtGraduationStudentProgService.insert(prog);

						//新增指导记录
						record.setProgressCode(GraduationProgressCodeEnum.PRACTICE_CONFIRM_PRACTICE.getCode());
						record.setAttachment(record2.getAttachment());
						record.setAttachmentName(record2.getAttachmentName());
						record.setContent("对我的社会实践报告进行了定稿");
						gjtGraduationGuideRecordService.insert(record);
					} else {
						feedback = new Feedback(false, "指导学员还未提交社会实践报告初稿！");
					}
				} else {
					feedback = new Feedback(false, "操作非法！");
				}
			}
			
		} catch (Exception e) {
			feedback = new Feedback(false, "提交失败");
			log.error(e.getMessage(), e);
		}
		
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/adviser/viewMyGuideList?applyType=" + applyType;
	}
	
	/**
	 * 发送短信
	 * @param studentId
	 * @param type 1-指导，2-开题定稿
	 */
	private void sendMsg(String studentId, int type) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		if (studentInfo != null) {
			String msg = null;
			if (type == 1) {
				msg = "同学您好，您的毕业论文指导老师给您提出了新的指导意见，请登录平台查看并按照老师的要求修改。";
			} else if (type == 2) {
				msg = "同学您好，您的毕业论文指导老师已对您的开题进行定稿，可以进入初稿阶段。";
			}
			
			SMSUtil.sendMessage(studentInfo.getSjh(), msg, "gk");
		}
	}
	
	public String clobToString(Clob clob) {
		if (clob == null) {
			return "";
		}
		
		try {
			Reader inStreamDoc = clob.getCharacterStream();
			char[] tempDoc = new char[(int) clob.length()];
			inStreamDoc.read(tempDoc);
			inStreamDoc.close();
			return new String(tempDoc);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return "";
	}

}
