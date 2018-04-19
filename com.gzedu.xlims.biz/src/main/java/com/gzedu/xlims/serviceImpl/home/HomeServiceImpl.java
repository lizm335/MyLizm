package com.gzedu.xlims.serviceImpl.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.edumanage.GjtGradeSpecialtyDao;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.feedback.LcmsMutualSubjectDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationApplyDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationBatchDao;
import com.gzedu.xlims.dao.home.HomeDao;
import com.gzedu.xlims.dao.myplan.MyPlanDao;
import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.signup.GjtRollPlanDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookFeedbackDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookPlanDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockDao;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtRollPlan;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtWorkOrderAssignPerson;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriRoleOperate;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;
import com.gzedu.xlims.pojo.status.TransAuditRoleEnum;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;
import com.gzedu.xlims.service.edumanage.OpenClassService;
import com.gzedu.xlims.service.home.GjtWorkOrderAssignPersonService;
import com.gzedu.xlims.service.home.HomeService;
import com.gzedu.xlims.service.model.PriRoleOperateService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.vo.Todo;

@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private LcmsMutualSubjectDao lcmsMutualSubjectDao;

	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Autowired
	private GjtRollPlanDao gjtRollPlanDao;

	@Autowired
	private GjtExamBatchNewDao gjtExamBatchNewDao;

	@Autowired
	private GjtTextbookPlanDao gjtTextbookPlanDao;

	@Autowired
	private GjtTextbookStockDao gjtTextbookStockDao;

	@Autowired
	private GjtTextbookOrderDao gjtTextbookOrderDao;

	@Autowired
	private GjtTextbookFeedbackDao gjtTextbookFeedbackDao;

	@Autowired
	private GjtGradeSpecialtyDao gjtGradeSpecialtyDao;

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	private GjtExamPlanNewDao gjtExamPlanNewDao;

	@Autowired
	private GjtExamAppointmentNewDao gjtExamAppointmentNewDao;

	@Autowired
	private GjtGraduationBatchDao gjtGraduationBatchDao;

	@Autowired
	private GjtGraduationApplyDao gjtGraduationApplyDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private HomeDao homeDao;

	@Autowired
	private PriRoleOperateService priRoleOperateService;

	@Autowired
	private GjtWorkOrderAssignPersonService gjtWorkOrderAssignPersonService;

	@Autowired
	private MyPlanDao myPlanDao;

	@Autowired
	private OpenClassService openClassService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;
	
	@Autowired
	GjtSchoolRollTranDao gjtSchoolRollTranDao;

	@Override
	public List<Todo> getTodoList(GjtUserAccount user) {
		List<Todo> list = new ArrayList<Todo>();
		String orgId = user.getGjtOrg().getId();

		if (isPermitted(user, "/lcmsubject/list.html?type=N")) { // 答疑服务
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_forwardAccountId", user.getId());
			searchParams.put("EQ_subjectStatus", "N");
			searchParams.put("EQ_isdeleted", "N");
			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
			Specification<LcmsMutualSubject> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					LcmsMutualSubject.class);
			long count = lcmsMutualSubjectDao.count(spec);
			if (count > 0) {
				Todo todo = new Todo("答疑服务", "你有" + count + "个疑问待解答，请尽快解答！", "/lcmsubject/list?type=N");
				todo.setCode(10);
				todo.setTotal(count);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/studyyear/list$update")) { // 学期计划
			List<GjtGrade> gradeList = gjtGradeDao.findCurrentGrade(orgId);
			if (gradeList == null || gradeList.size() == 0) {
				Todo todo = new Todo("学期计划", "你本学期的学期计划还未制定，请尽快制定！", "/edumanage/studyyear/list");
				todo.setCode(20);
				todo.setTotal(1);
				list.add(todo);
			} else {
				GjtGrade grade = gradeList.get(0);
				if (grade.getOldStudentEnterDate() == null) {
					Todo todo = new Todo("学期计划", "你本学期的学期计划还未制定，请尽快制定！", "/edumanage/studyyear/list");
					todo.setCode(20);
					todo.setTotal(1);
					list.add(todo);
				}
			}
		}

		if (isPermitted(user, "/edumanage/rollPlan/list$create")) { // 学籍计划制定
			List<GjtRollPlan> rollPlanlist = gjtRollPlanDao.findCurrentRollPlanList(orgId);
			if (rollPlanlist == null || rollPlanlist.size() == 0) {
				Todo todo = new Todo("学籍计划", "你本学期的学籍计划还未制定，请尽快制定！", "/edumanage/rollPlan/list");
				todo.setCode(30);
				todo.setTotal(1);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/rollPlan/list$approval")) { // 学籍计划审核
			List<GjtRollPlan> rollPlanlist = gjtRollPlanDao.findByAuditStateAndXxIdAndIsDeleted(new BigDecimal(0),
					orgId, "N");
			if (rollPlanlist != null && rollPlanlist.size() > 0) {
				Todo todo = new Todo("学籍计划", "你有" + rollPlanlist.size() + "个学籍计划待审核，请尽快审核！",
						"/edumanage/rollPlan/list");
				todo.setCode(40);
				todo.setTotal(rollPlanlist.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/rollPlan/list$create")) { // 学籍计划审核不通过
			List<GjtRollPlan> rollPlanlist = gjtRollPlanDao.findByAuditStateAndXxIdAndIsDeleted(new BigDecimal(2),
					orgId, "N");
			if (rollPlanlist != null && rollPlanlist.size() > 0) {
				Todo todo = new Todo("学籍计划", "你有" + rollPlanlist.size() + "个学籍计划审核不通过，请查看！",
						"/edumanage/rollPlan/list");
				todo.setCode(50);
				todo.setTotal(rollPlanlist.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/roll/list$import")) { // 学籍信息
			List<GjtStudentInfo> studentInfoList = gjtStudentInfoDao.findByXxIdAndXjztAndIsDeleted(orgId, "3", "N");
			if (studentInfoList != null && studentInfoList.size() > 0) {
				Todo todo = new Todo("学籍信息", "你有" + studentInfoList.size() + "个学员待注册，请尽快完成注册！", "/edumanage/roll/list");
				todo.setCode(60);
				todo.setTotal(studentInfoList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/exam/new/batch/list$create")) { // 考试计划制定
			List<GjtExamBatchNew> examBatchList = gjtExamBatchNewDao.findCurrentExamBatchList(orgId);
			if (examBatchList == null || examBatchList.size() == 0) {
				Todo todo = new Todo("考试计划", "你本学期的考试计划还未制定，请尽快制定！", "/exam/new/batch/list");
				todo.setCode(70);
				todo.setTotal(1);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/exam/new/batch/list$create")) { // 考试计划审核不通过
			List<GjtExamBatchNew> examBatchList = gjtExamBatchNewDao
					.findByPlanStatusAndXxIdAndIsDeletedAndRecordEndAfter("2", orgId, 0, new Date());
			if (examBatchList != null && examBatchList.size() > 0) {
				Todo todo = new Todo("考试计划", "你有" + examBatchList.size() + "个考试计划审核不通过，请查看！", "/exam/new/batch/list");
				todo.setCode(80);
				todo.setTotal(examBatchList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/exam/new/batch/list$approval")) { // 考试计划审核
			List<GjtExamBatchNew> examBatchList = gjtExamBatchNewDao
					.findByPlanStatusAndXxIdAndIsDeletedAndRecordEndAfter("1", orgId, 0, new Date());
			if (examBatchList != null && examBatchList.size() > 0) {
				Todo todo = new Todo("考试计划", "你有" + examBatchList.size() + "个考试计划待审核，请尽快审核！", "/exam/new/batch/list");
				todo.setCode(90);
				todo.setTotal(examBatchList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/exam/new/plan/list$create") || isPermitted(user, "/exam/new/plan/list$import")) { // 开考科目
			List<GjtExamBatchNew> examBatchList = gjtExamBatchNewDao.findCurrentExamBatchList(orgId);
			if (examBatchList != null && examBatchList.size() > 0) {
				GjtExamBatchNew examBatch = examBatchList.get(0);
				if ("3".equals(examBatch.getPlanStatus()) && examBatch.getPlanSt() != null
						&& examBatch.getPlanEnd() != null && examBatch.getPlanSt().compareTo(new Date()) <= 0
						&& examBatch.getPlanEnd().compareTo(new Date()) >= 0) {
					List<GjtExamPlanNew> examPlanList = gjtExamPlanNewDao
							.findByExamBatchCode(examBatch.getExamBatchCode());
					if (examPlanList == null || examPlanList.size() == 0) {
						Todo todo = new Todo("开考科目", "你本次考试计划的开考科目还未设置，请尽快设置！", "/exam/new/plan/list");
						todo.setCode(100);
						todo.setTotal(1);
						list.add(todo);
					}
				}
			}
		}

		if (isPermitted(user, "/exam/new/student/room/list$import")) { // 排考安排
			List<GjtExamBatchNew> examBatchList = gjtExamBatchNewDao.findCurrentExamBatchList(orgId);
			if (examBatchList != null && examBatchList.size() > 0) {
				GjtExamBatchNew examBatch = examBatchList.get(0);
				if ("3".equals(examBatch.getPlanStatus()) && examBatch.getArrangeSt() != null
						&& examBatch.getArrangeEnd() != null && examBatch.getArrangeSt().compareTo(new Date()) <= 0
						&& examBatch.getArrangeEnd().compareTo(new Date()) >= 0) {
					List<GjtExamAppointmentNew> examAppointmentList = gjtExamAppointmentNewDao
							.findByExamBatchCodeAndStatus(examBatch.getExamBatchCode(), 0);
					if (examAppointmentList != null && examAppointmentList.size() > 0) {
						Todo todo = new Todo("排考安排", "你本次考试的排考还未完成，请尽快完成！", "/exam/new/student/room/list");
						todo.setCode(110);
						todo.setTotal(examAppointmentList.size());
						list.add(todo);
					}
				}
			}
		}

		if (isPermitted(user, "/textbookPlan/list$create")) { // 教材计划制定
			List<GjtTextbookPlan> currentPlanList = gjtTextbookPlanDao.findCurrentPlanList(orgId);
			if (currentPlanList == null || currentPlanList.size() == 0) {
				Todo todo = new Todo("教材计划", "你本学期的教材计划还未制定，请尽快制定！", "/textbookPlan/list");
				todo.setCode(120);
				todo.setTotal(1);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/textbookPlan/list$create")) { // 教材计划审核不通过
			List<GjtTextbookPlan> textbookPlanList = gjtTextbookPlanDao.findByStatusAndOrgIdAndIsDeleted(2, orgId, "N");
			if (textbookPlanList != null && textbookPlanList.size() > 0) {
				Todo todo = new Todo("教材计划", "你有" + textbookPlanList.size() + "个教材计划审核不通过，请查看！", "/textbookPlan/list");
				todo.setCode(130);
				todo.setTotal(textbookPlanList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/textbookPlan/list$approval")) { // 教材计划审核
			List<GjtTextbookPlan> textbookPlanList = gjtTextbookPlanDao.findByStatusAndOrgIdAndIsDeleted(1, orgId, "N");
			if (textbookPlanList != null && textbookPlanList.size() > 0) {
				Todo todo = new Todo("教材计划", "你有" + textbookPlanList.size() + "个教材计划待审核，请尽快审核！", "/textbookPlan/list");
				todo.setCode(140);
				todo.setTotal(textbookPlanList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/textbookStock/list$import")) { // 库存管理
			List<GjtTextbookStock> textbookStockList = gjtTextbookStockDao.findNotEnoughStock(orgId);
			if (textbookStockList != null && textbookStockList.size() > 0) {
				Todo todo = new Todo("库存管理", "你有" + textbookStockList.size() + "本教材库存不足，请尽快补充！", "/textbookStock/list");
				todo.setCode(150);
				todo.setTotal(textbookStockList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/textbookOrder/list$sumit-approval")) { // 教材订购审核不通过
			List<GjtTextbookOrder> textbookOrderList = gjtTextbookOrderDao.findByStatusAndOrgId(2, orgId);
			if (textbookOrderList != null && textbookOrderList.size() > 0) {
				Todo todo = new Todo("教材订购", "你有" + textbookOrderList.size() + "个教材订购审核不通过，请查看！",
						"/textbookOrder/list");
				todo.setCode(160);
				todo.setTotal(textbookOrderList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/textbookOrder/list$approval")) { // 教材订购审核
			List<GjtTextbookOrder> textbookOrderList = gjtTextbookOrderDao.findByStatusAndOrgId(1, orgId);
			if (textbookOrderList != null && textbookOrderList.size() > 0) {
				Todo todo = new Todo("教材订购", "你有" + textbookOrderList.size() + "个教材订购待审核，请尽快审核！",
						"/textbookOrder/list");
				todo.setCode(170);
				todo.setTotal(textbookOrderList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/textbookFeedback/list$update")) { // 学员反馈
			List<GjtTextbookFeedback> feedbackList = gjtTextbookFeedbackDao.findByStatusAndOrgId(1, orgId);
			if (feedbackList != null && feedbackList.size() > 0) {
				Todo todo = new Todo("学员反馈", "你有" + feedbackList.size() + "个学员反馈待处理，请尽快处理！", "/textbookFeedback/list");
				todo.setCode(180);
				todo.setTotal(feedbackList.size());
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/teachPlan/list$create")) { // 开设专业
			List<GjtGradeSpecialty> gradeSpecialtyList = gjtGradeSpecialtyDao.findCurrentGradeSpecialtyList(orgId);
			if (gradeSpecialtyList == null || gradeSpecialtyList.size() == 0) {
				Todo todo = new Todo("开设专业", "你本学期还未开设专业，请尽快开设！", "/edumanage/teachPlan/list");
				todo.setCode(190);
				todo.setTotal(1);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/teachclass/list$allocation")) { // 分配班主任
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_classType", "teach");
			searchParams.put("EQ_gjtSchoolInfo.id", orgId);
			searchParams.put("EQ_isDeleted", "N");
			searchParams.put("ISNULL_gjtBzr", null);
			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
			Specification<GjtClassInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					GjtClassInfo.class);
			long count = gjtClassInfoDao.count(spec);
			if (count > 0) {
				Todo todo = new Todo("教务班级", "你有" + count + "个班级待分配班主任，请尽快分配！", "/edumanage/teachclass/list");
				todo.setCode(200);
				todo.setTotal(count);
				list.add(todo);
			}
			/*
			 * List<GjtClassInfo> classInfoList = gjtClassInfoDao.
			 * findByClassTypeAndGjtSchoolInfoIdAndIsDeletedAndGjtBzrIsNull(
			 * "teach", orgId, "N"); if (classInfoList != null &&
			 * classInfoList.size() > 0) { Todo todo = new Todo("教务班级", "你有" +
			 * classInfoList.size() + "个班级待分配班主任，请尽快分配！",
			 * "/edumanage/teachclass/list"); list.add(todo); }
			 */
		}

		if (isPermitted(user, "/edumanage/courseclass/list$allocationFD")) { // 分配辅导老师
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_classType", "course");
			searchParams.put("EQ_gjtSchoolInfo.id", orgId);
			searchParams.put("EQ_isDeleted", "N");
			searchParams.put("ISNULL_gjtBzr", null);
			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
			Specification<GjtClassInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					GjtClassInfo.class);
			long count = gjtClassInfoDao.count(spec);
			if (count > 0) {
				Todo todo = new Todo("课程班级", "你有" + count + "个班级待分配辅导老师，请尽快分配！", "/edumanage/courseclass/list");
				todo.setCode(210);
				todo.setTotal(count);
				list.add(todo);
			}
			/*
			 * List<GjtClassInfo> classInfoList = gjtClassInfoDao.
			 * findByClassTypeAndGjtSchoolInfoIdAndIsDeletedAndGjtBzrIsNull(
			 * "course", orgId, "N"); if (classInfoList != null &&
			 * classInfoList.size() > 0) { Todo todo = new Todo("课程班级", "你有" +
			 * classInfoList.size() + "个班级待分配辅导老师，请尽快分配！",
			 * "/edumanage/courseclass/list"); list.add(todo); }
			 */
		}

		if (isPermitted(user, "/edumanage/courseclass/list$allocationDD")) { // 分配督导老师
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_classType", "course");
			searchParams.put("EQ_gjtSchoolInfo.id", orgId);
			searchParams.put("EQ_isDeleted", "N");
			searchParams.put("ISNULL_gjtDuDao", null);
			Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
			Specification<GjtClassInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					GjtClassInfo.class);
			long count = gjtClassInfoDao.count(spec);
			if (count > 0) {
				Todo todo = new Todo("课程班级", "你有" + count + "个班级待分配督导老师，请尽快分配！", "/edumanage/courseclass/list");
				todo.setCode(220);
				todo.setTotal(count);
				list.add(todo);
			}
			/*
			 * List<GjtClassInfo> classInfoList = gjtClassInfoDao.
			 * findByClassTypeAndGjtSchoolInfoIdAndIsDeletedAndGjtDuDaoIsNull(
			 * "course", orgId, "N"); if (classInfoList != null &&
			 * classInfoList.size() > 0) { Todo todo = new Todo("课程班级", "你有" +
			 * classInfoList.size() + "个班级待分配督导老师，请尽快分配！",
			 * "/edumanage/courseclass/list"); list.add(todo); }
			 */
		}

		if (isPermitted(user, "/graduation/graduationBatch/list$create")) { // 毕业计划制定
			List<GjtGraduationBatch> graduationBatchList = gjtGraduationBatchDao.findCurrentGraduationBatchList(orgId);
			if (graduationBatchList == null || graduationBatchList.size() == 0) {
				Todo todo = new Todo("毕业计划", "你本学期的毕业计划还未制定，请尽快制定！", "/graduation/graduationBatch/list");
				todo.setCode(230);
				todo.setTotal(1);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/graduation/apply/list$update")
				|| isPermitted(user, "/graduation/specialty/list$update")) { // 论文学员
			List<GjtGraduationBatch> graduationBatchList = gjtGraduationBatchDao.findCurrentGraduationBatchList(orgId);
			if (graduationBatchList != null && graduationBatchList.size() > 0) {
				GjtGraduationBatch graduationBatch = graduationBatchList.get(0);

				Map<String, Object> searchParams = new HashMap<String, Object>();
				searchParams.put("EQ_gjtGraduationBatch.batchId", graduationBatch.getBatchId());
				searchParams.put("EQ_applyType", 1);
				searchParams.put("EQ_isDeleted", "N");
				searchParams.put("ISNULL_guideTeacher", null);
				Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
				Specification<GjtGraduationApply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
						GjtGraduationApply.class);
				long count = gjtGraduationApplyDao.count(spec);
				if (count > 0) {
					Todo todo = new Todo("论文学员", "你本次毕业计划中有 " + count + "个学员待设置论文指导老师，请尽快设置！",
							"/graduation/apply/list");
					todo.setCode(240);
					todo.setTotal(count);
					list.add(todo);
				}
				/*
				 * List<GjtGraduationApply> GraduationApplyList1 =
				 * gjtGraduationApplyDao.
				 * findByGjtGraduationBatchBatchIdAndApplyTypeAndIsDeletedAndGuideTeacherIsNull
				 * (graduationBatch.getBatchId(), 1, "N"); if
				 * (GraduationApplyList1 != null && GraduationApplyList1.size()
				 * > 0) { Todo todo = new Todo("论文学员", "你本次毕业计划中有 " +
				 * GraduationApplyList1.size() + "个学员待设置论文指导老师，请尽快设置！",
				 * "/graduation/apply/list"); list.add(todo); }
				 */

				searchParams.remove("ISNULL_guideTeacher");
				searchParams.put("ISNULL_defenceTeacher", null);
				filters = SearchFilter.parse(searchParams);
				spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationApply.class);
				count = gjtGraduationApplyDao.count(spec);
				if (count > 0) {
					Todo todo = new Todo("论文学员", "你本次毕业计划中有 " + count + "个学员待设置论文答辩老师，请尽快设置！",
							"/graduation/apply/list");
					todo.setCode(250);
					todo.setTotal(count);
					list.add(todo);
				}
				/*
				 * List<GjtGraduationApply> GraduationApplyList2 =
				 * gjtGraduationApplyDao.
				 * findByGjtGraduationBatchBatchIdAndApplyTypeAndIsDeletedAndDefenceTeacherIsNull
				 * (graduationBatch.getBatchId(), 1, "N"); if
				 * (GraduationApplyList2 != null && GraduationApplyList2.size()
				 * > 0) { Todo todo = new Todo("论文学员", "你本次毕业计划中有 " +
				 * GraduationApplyList2.size() + "个学员待设置论文答辩老师，请尽快设置！",
				 * "/graduation/apply/list"); list.add(todo); }
				 */

				searchParams.put("EQ_applyType", 2);
				searchParams.remove("ISNULL_defenceTeacher");
				searchParams.put("ISNULL_guideTeacher", null);
				filters = SearchFilter.parse(searchParams);
				spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationApply.class);
				count = gjtGraduationApplyDao.count(spec);
				if (count > 0) {
					Todo todo = new Todo("论文学员", "你本次毕业计划中有 " + count + "个学员待设置社会实践老师，请尽快设置！",
							"/graduation/apply/list");
					todo.setCode(260);
					todo.setTotal(count);
					list.add(todo);
				}
				/*
				 * List<GjtGraduationApply> GraduationApplyList3 =
				 * gjtGraduationApplyDao.
				 * findByGjtGraduationBatchBatchIdAndApplyTypeAndIsDeletedAndGuideTeacherIsNull
				 * (graduationBatch.getBatchId(), 2, "N"); if
				 * (GraduationApplyList3 != null && GraduationApplyList3.size()
				 * > 0) { Todo todo = new Todo("论文学员", "你本次毕业计划中有 " +
				 * GraduationApplyList3.size() + "个学员待设置社会实践老师，请尽快设置！",
				 * "/graduation/apply/list"); list.add(todo); }
				 */
			}
		}

		if (isPermitted(user, "/home/workOrder/list$create")) { // 我的工单
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_gjtWorkOrder.isDeleted", "N");
			searchParams.put("EQ_userId", user.getId());
			searchParams.put("EQ_gjtWorkOrder.isState", "0");
			long queryAllCount = gjtWorkOrderAssignPersonService.queryAllCount(searchParams);

			if (queryAllCount > 0) {
				Todo todo = new Todo("我的工单", "你有" + queryAllCount + "个工单待完成，请尽快完成", "/home/workOrder/list");
				todo.setCode(270);
				todo.setTotal(queryAllCount);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/myPlan/planList$create")) { // 我的计划
			Map<String, Object> searchParams = new HashMap<String, Object>();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 1);
			searchParams.put("START_DATE", DateUtils.getTimeYMD(DateUtils.getDate()));
			searchParams.put("END_DATE", DateUtils.getTimeYMD(calendar.getTime()));
			searchParams.put("PLAN_STATUS", "1");
			searchParams.put("USER_ID", user.getId());
			int planCount = myPlanDao.getPlanCount(searchParams);

			if (planCount > 0) {
				Todo todo = new Todo("我的计划", "你今日有" + planCount + "个工作计划待完成，请尽快完成", "/myPlan/planList");
				todo.setCode(280);
				todo.setTotal(planCount);
				list.add(todo);
			}
		}

		if (isPermitted(user, "/edumanage/openclass/schoolTeach")) { // 直播
			Map<String, Object> searchParams = new HashMap<String, Object>();
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
			if (employeeInfo != null) {
				searchParams.put("classTeacher", employeeInfo.getEmployeeId());
			}
			searchParams.put("lessonType", "1");
			long lessoningNum = openClassService.getOnlineLessonCount(searchParams);
			if (lessoningNum > 0) {
				Todo todo = new Todo("直播辅导", "你有 " + lessoningNum + "个直播辅导直播中，请尽快进入直播辅导！",
						"/edumanage/openclass/schoolTeach");
				todo.setCode(300);
				todo.setTotal(1);
				list.add(todo);
			}
		}
		
		if (isPermitted(user, "/edumanage/rollTrans/dropOutStudy/list")) {//学籍异动
			Map<String, Object> searchParams = new HashMap<String, Object>();
			String roleCode=TransAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
			if("2".equals(roleCode)){//学支管理员(教务服务部)
				searchParams.put("EQ_transactionType", 4);//退学
				searchParams.put("EQ_auditOperatorRole", roleCode);//审核人角色 
				searchParams.put("EQ_transactionStatus", 3);//审核状态 :劝学中
				Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
				Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(),GjtSchoolRollTran.class);
				long count = gjtSchoolRollTranDao.count(spec);
				if(count>0){
					Todo todo = new Todo("学籍异动", "你有 " + count + "个退学学员待劝学，请尽快处理！",
							"/edumanage/rollTrans/dropOutStudy/list");
					todo.setCode(310);
					todo.setTotal(count);
					list.add(todo);
				}
			}
			if("5".equals(roleCode)){//学籍科
				searchParams.put("EQ_transactionType", 4);//退学
				searchParams.put("EQ_auditOperatorRole", roleCode);//审核人角色 
				searchParams.put("EQ_transactionStatus", 3);//审核状态 :劝学中
				Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
				Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(),GjtSchoolRollTran.class);
				long count = gjtSchoolRollTranDao.count(spec);
				if(count>0){
					Todo todo = new Todo("学籍异动", "你有 " + count + "个退学学员待劝学，请尽快处理！","/edumanage/rollTrans/dropOutStudy/list");
					todo.setCode(310);
					todo.setTotal(count);
					list.add(todo);
				}
				searchParams.remove("EQ_transactionStatus");
				searchParams.put("EQ_transactionStatus", 8);
				Map<String, SearchFilter> filters2 = SearchFilter.parse(searchParams);
				Specification<GjtSchoolRollTran> specs = DynamicSpecifications.bySearchFilter(filters2.values(),GjtSchoolRollTran.class);
				long total = gjtSchoolRollTranDao.count(specs);
				if(total>0){
					Todo todo = new Todo("学籍异动", "你有 " + total + "个学员退学待确认，请尽快处理！","/edumanage/rollTrans/dropOutStudy/list");
					todo.setCode(310);
					todo.setTotal(total);
					list.add(todo);
				}
			}
			if("0".equals(roleCode)){//院长
				searchParams.put("EQ_transactionType", 4);//退学
				searchParams.put("EQ_auditOperatorRole", roleCode);//审核人角色 
				searchParams.put("EQ_transactionStatus",8);//审核状态 :待确认
				Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
				Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(),GjtSchoolRollTran.class);
				long num = gjtSchoolRollTranDao.count(spec);
				if(num>0){
					Todo todo = new Todo("学籍异动", "你有 " + num + "个学员退学待确认，请尽快处理！","/edumanage/rollTrans/dropOutStudy/list");
					todo.setCode(310);
					todo.setTotal(num);
					list.add(todo);
				}
			}
		}
		return list;
	}

	@Override
	public List<Object[]> getMessageList(GjtUserAccount user) {
		return homeDao.getMessageList(user.getId());
	}

	private boolean isPermitted(GjtUserAccount user, String permUrl) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal().toString().equals(user.getLoginAccount())) {
			return subject.isPermitted(permUrl);
		}

		boolean isPerm = false;
		boolean isDollar = permUrl.indexOf("$") != -1;
		List<PriModelInfo> models = user.getPriRoleInfo().getPriModelInfos();
		if (models != null && models.size() > 0) {
			for (PriModelInfo model : models) {
				if (model.getIsLeaf()) {
					if (!isDollar) {
						if (permUrl.equals(model.getModelAddress())) {
							isPerm = true;
							break;
						}
					} else {
						List<PriRoleOperate> roleOperates = priRoleOperateService
								.findByRoleIdAndModelId(user.getPriRoleInfo().getRoleId(), model.getModelId());
						if (roleOperates != null && roleOperates.size() > 0) {
							for (PriRoleOperate roleOperate : roleOperates) {
								if (permUrl.equals(model.getModelAddress() + "$"
										+ roleOperate.getPriOperateInfo().getOperateCode())) {
									isPerm = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		return isPerm;
	}

	@Override
	public List<GjtWorkOrderAssignPerson> getWorkOrderList(GjtUserAccount user) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_userId", user.getId());
		PageRequest page = new PageRequest(0, 5);
		Page<GjtWorkOrderAssignPerson> pageInfo = gjtWorkOrderAssignPersonService.queryPageInfo(searchParams, page);
		return pageInfo.getContent();
	}

}
