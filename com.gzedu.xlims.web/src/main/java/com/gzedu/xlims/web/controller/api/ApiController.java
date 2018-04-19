/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.organization.GjtStudentStudySituationDao;
import com.gzedu.xlims.dao.organization.GjtStudyYearCourseDao;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.exam.GjtExamRecordNewService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.recruitmanage.GjtEnrollBatchNewService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：对外的接口
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月22日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/api")
public class ApiController {

	@Autowired
	GjtUserAccountService gjtUserAccountService;
	@Autowired
	GjtClassStudentService gjtClassStudentService;
	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtStudyYearCourseDao gjtStudyyearCourseDao;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	PCourseServer pCourseServer;

	@Autowired
	GjtGradeSpecialtyPlanService gjtGradeSpecialtyPlanService;

	@Autowired
	GjtStudentStudySituationDao gjtStudentStudySituationDao;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtEnrollBatchNewService gjtEnrollBatchNewService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;
	
	@Autowired
	GjtExamRecordNewService gjtExamRecordNewService;

	/*@Deprecated
	@RequestMapping(value = "signupdata/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> signupdataAdd(Signupdata signupdata) {
		Map<String, String> m = new HashMap<String, String>();

		// 招生批次
		// String batchId = signupdata.getBatchId();
		// GjtEnrollBatchNew enrollBatch =
		// gjtEnrollBatchNewService.queryById(batchId);

		// if (enrollBatch == null) {
		// m.put("result", "failure");
		// m.put("message", "招生批次不存在:" + batchId);
		// return m;
		// }

		// 年级
		String gradeId = signupdata.getGradeId();
		GjtGrade grade = gjtGradeService.queryById(gradeId);
		if (grade == null) {
			m.put("result", "failure");
			m.put("message", "年级不存在:" + gradeId);
			return m;
		}

		// 专业
		String majorId = signupdata.getMajorId();
		GjtSpecialty specialty = gjtSpecialtyService.queryById(majorId);
		if (specialty == null) {
			m.put("result", "failure");
			m.put("message", "专业不存在:" + majorId);
			return m;
		}

		GjtSchoolInfo gjtSchoolInfo = grade.getGjtSchoolInfo();

		// 院校CODE
		// String xxCode = signupdata.getCollegeCode();
		// GjtSchoolInfo gjtSchoolInfo =
		// gjtSchoolInfoService.queryByCode(xxCode);
		if (gjtSchoolInfo == null) {
			m.put("result", "failure");
			m.put("message", "院校不存在:" + gjtSchoolInfo);
			return m;
		}

		// // 学习中 心
		// GjtStudyCenter gjtStudyCenter =
		// gjtStudyCenterService.queryById(enrollBatch.getXxzxId());
		// if (gjtStudyCenter == null) {
		// m.put("result", "failure");
		// m.put("message", "批次下的学习中心不存在:" + gjtStudyCenter);
		// return m;
		// }
		// 学生
		if (signupdata.getStudentId() == null) {
			signupdata.setStudentId(UUIDUtils.random().toString());
		}
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(signupdata.getStudentId());
		if (studentInfo == null) {
			studentInfo = new GjtStudentInfo();
			studentInfo.setStudentId(signupdata.getStudentId());
			studentInfo.setXm(signupdata.getName());// 姓名
			studentInfo.setGjtSpecialty(specialty);
			studentInfo.setGjtGrade(grade);
			studentInfo.setGjtSchoolInfo(gjtSchoolInfo);
			studentInfo.setIsDeleted("N");
			studentInfo.setIsEnabled("1");
			studentInfo.setCreatedDt(new Date());
			studentInfo.setCreatedBy("同步数据111");
			studentInfo.setXh(signupdata.getStudentNo());
			studentInfo.setSfzh(signupdata.getStudentNo());
			gjtStudentInfoService.saveEntity(studentInfo);
		}
		// 用户
		GjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount(studentInfo.getXh());
		if (userAccount == null) {
			userAccount = gjtUserAccountService.saveEntity(studentInfo.getXm(), studentInfo.getXh(),
					gjtSchoolInfo.getId(), 1);
		}
		{
			// 学生同步到教学平台
			// OpiStudent op = new OpiStudent();
			// op.setCREATED_BY("创建人ID");
			// op.setEE_NO("学员EE账号");
			// op.setEMAIL("学员邮箱");
			// op.setIMG_PATH("学员头像URL地址");
			// op.setLMS_NO(userAccount.getId());
			// op.setMANAGER_ID("班主任id");
			// op.setMOBILE_NO("学员手机号码");
			// op.setREALNAME(studentInfo.getXm());
			// op.setSEX("1");
			// op.setSTUD_AREA("学员所在省份");
			// op.setSTUD_ID(studentInfo.getStudentId());
			// op.setXLCLASS_ID(studentInfo.getGjtGrade().getGradeId());
			// op.setXLCLASS_NAME(studentInfo.getGjtGrade().getGradeName());
			//
			// OpiStudentData od = new OpiStudentData(gjtSchoolInfo.getAppid(),
			// op);
			// RSimpleData synchroCourse = pCourseServer.synchroStudent(od);
			// if (synchroCourse == null) {
			// System.out.println("同步失败");
			// } else {
			// System.out.println(synchroCourse.getStatus() +
			// synchroCourse.getMsg());
			// }
		}

		// 自动分配教学班
		GjtClassInfo teachClassInfo = gjtClassInfoService.queryTeachClassInfo(studentInfo.getStudentId());
		try {
			if (teachClassInfo == null) {
				// 班号 暂时为1
				int bh = 1;
				teachClassInfo = gjtClassInfoService.createTeachClassInfo(gradeId, majorId, bh, gjtSchoolInfo.getId(), null);
			}
			// 学员加入该班级
			GjtClassStudent item = new GjtClassStudent();
			item.setClassStudentId(UUIDUtils.random());
			item.setGjtClassInfo(teachClassInfo);
			item.setGjtStudentInfo(studentInfo);
			item.setCreatedDt(DateUtils.getNowTime());
			item.setIsDeleted("N");
			item.setGjtSchoolInfo(gjtSchoolInfo);
			item.setIsEnabled("1");
			item.setVersion(BigDecimal.valueOf(2.5));
			gjtClassStudentService.save(item);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// 自动分配课程班
		gjtClassInfoService.insertCreateClass(gjtSchoolInfo.getId(), gradeId, majorId);

		// 当前学年度(报读第一学期？)
		int studyYearCode = StudyYear.getCode(grade.getEnterDt(), 1);
		// 根据学生的专业查找对应的课程
		GjtGrade studentGrade = studentInfo.getGjtGrade();
		GjtSpecialty studentSpecialty = studentInfo.getGjtSpecialty();

		List<GjtGradeSpecialtyPlan> gradeSpecialtyPlans = gjtGradeSpecialtyPlanService
				.queryGradeSpecialtyPlan(studentGrade.getGradeId(), studentSpecialty.getSpecialtyId());

		for (GjtGradeSpecialtyPlan gradeSpecialtyPlan : gradeSpecialtyPlans) {
			// String studyYearCourseId =
			// gradeSpecialtyPlan.getStudyYearCourseId();

			// String studyYearId = gradeSpecialtyPlan.getStudyYearId();

			// GjtCourse gjtCourse = gradeSpecialtyPlan.getGjtCourse();
			// GjtStudyYearInfo gjtStudyYearInfo =
			// gradeSpecialtyPlan.getGjtStudyYearInfo();

			// GjtStudyYearCourse studyYearCourse =
			// gjtStudyYearService.queryByCourseAndStudyYearInfo(gjtCourse,
			// gjtStudyYearInfo);

			// List<GjtClassInfo> classInfos = studyYearCourse.getClassInfos();

			// String studyYearCourseId = studyYearCourse.getId();

			GjtClassInfo classInfo = gjtClassInfoService.queryClassInfosByCourseIdAndStudyYearCode(
					gradeSpecialtyPlan.getCourseId(), gradeSpecialtyPlan.getStudyYearCode());

			if (classInfo != null) {
				GjtClassStudent item = new GjtClassStudent();
				item.setClassStudentId(UUIDUtils.random());
				item.setGjtClassInfo(classInfo);
				item.setGjtStudentInfo(studentInfo);
				item.setCreatedDt(DateUtils.getNowTime());
				item.setIsDeleted("N");
				item.setGjtGrade(studentGrade);
				item.setGjtSchoolInfo(gjtSchoolInfo);
				// item.setGjtStudyCenter(gjtStudyCenter);
				item.setIsEnabled("1");
				item.setVersion(BigDecimal.valueOf(2.5));
				gjtClassStudentService.save(item);
			}

			// for (GjtClassInfo gjtClassInfo : classInfos) {

			// 学员选课记录
			// GjtStudentStudySituation studySituation =
			// gjtStudentStudySituationDao
			// .findByStudentIdAndTeachPlanId(studentInfo.getStudentId(),
			// studyYearCourseId);

			// if (studySituation == null) {
			// studySituation = new GjtStudentStudySituation();
			// studySituation.setChooseId(UUIDUtils.random());
			// studySituation.setStudentId(studentInfo.getStudentId());
			// studySituation.setTeachPlanId(studyYearCourseId);
			// studySituation.setCreatedDt(new Date());
			// gjtStudentStudySituationDao.save(studySituation);
			// }

			// OpiStudchoose STUDCHOOSE = new OpiStudchoose();
			// STUDCHOOSE.setCHOOSE_ID(studySituation.getChooseId());
			// STUDCHOOSE.setTERMCOURSE_ID(studyYearCourseId);
			// STUDCHOOSE.setSTUD_ID(studentInfo.getStudentId());
			// STUDCHOOSE.setCLASS_ID(gjtClassInfo.getClassId());
			// STUDCHOOSE.setCREATED_BY("自动同步");

			// OpiTchchoose TCHCHOOSE = new OpiTchchoose();
			// TCHCHOOSE.setCLASS_ID("123");
			// TCHCHOOSE.setCREATED_BY("sdf");
			// TCHCHOOSE.setFROM_JIJIAO("Y");
			// TCHCHOOSE.setIS_INVISIBILITY("sdf");
			// TCHCHOOSE.setLMS_NO("sdf");
			// TCHCHOOSE.setMANAGER_CHOOSE_ID("sdf");
			// TCHCHOOSE.setMANAGER_ID("sdf");
			// TCHCHOOSE.setMANAGER_ROLE("sdf");

			// OpiTermChooseData data = new OpiTermChooseData(STUDCHOOSE);
			//
			// RSimpleData synchroTermChoose =
			// pCourseServer.synchroTermChoose(data);
			// if (synchroTermChoose == null) {
			// System.out.println("同步失败");
			// } else {
			// System.out.println(synchroTermChoose.getStatus() +
			// synchroTermChoose.getMsg());
			// }
			// }
		}

		m.put("result", "success");
		return m;
	}*/
	
	/**
	 * 获取学习平台学习记录
	 * @param formMap
	 */
	@RequestMapping(value = "getStudyRecord")
	@ResponseBody
	public Map getStudyRecord(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return pCourseServer.getStudyRecord(formMap);
	}
	
	/**
	 * 获取学习平台学习记录
	 * @param formMap
	 */
	@RequestMapping(value = "getClassDyna")
	@ResponseBody
	public Map getClassDyna(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return pCourseServer.getClassDyna(formMap);
	}
	
	/**
	 * 获取今天登陆的student_id
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "getLoginStudent") 
	@ResponseBody
	public List getLoginStudent(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return pCourseServer.getLoginStudent(formMap);
	}

	public static void main(String[] args) {
		Signupdata data = new Signupdata("123456", "test1", "430426198914155521", "234e6aec232f47c996f3ed4cc682d6fb",
				"9b54718072e74cbdb0f2d7add892df40");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", data.getStudentId());
		map.put("name", data.getName());
		map.put("studentNo", data.getStudentNo());
		map.put("gradeId", data.getGradeId());
		map.put("majorId", data.getMajorId());

		String url = "http://localhost:8088/api/signupdata/add.do";
		String result = HttpClientUtils.doHttpPost(url, map, 3000, "UTF-8");
		System.out.println(result);
	}

	/**
	 * 同步全部学员的学习记录考勤记录。
	 * @param data
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "syncAllStudentData/{data}")
	@ResponseBody
	public Map syncAllStudentData(@PathVariable("data") String data, HttpServletRequest request){
		Map map = new HashMap();
		int num = 0;
		if(EmptyUtils.isNotEmpty(data)&&data.equals(DateUtils.getToday())){
			num = pCourseServer.syncAllStudentData();
		}
		map.put("success",num);
		return map;
	}
	
	
	/**
	 * 定时同步学习平台的学习记录（直接取学习平台那边更新时间在一定范围内的数据）
	 * @param data
	 * @param map 
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "syncStudyData/{data}")
	@ResponseBody
	public Map syncStudyData(@PathVariable("data") String data,HttpServletRequest request){
		Map map = new HashMap();
		int num = 0;
		Map formMap = Servlets.getParametersStartingWith(request, "");
		if (EmptyUtils.isNotEmpty(data) && data.equals(DateUtils.getToday())){
			num = pCourseServer.syncStudyData(formMap);
		}
		map.put("success",num);
		return map;
	}
	
	/**
	 * 定时同步考试平台的考试记录（直接取考试平台那边更新时间在一定范围内的数据）
	 * @param data
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "syncExamData/{data}")
	@ResponseBody
	public Map syncExamData(@PathVariable("data") String data, HttpServletRequest request){
		Map map = new HashMap();
		int num = 0;
		Map formMap = Servlets.getParametersStartingWith(request, "");
		//if (EmptyUtils.isNotEmpty(data) && data.equals(DateUtils.getToday())) {
			num = pCourseServer.syncExamData(formMap);
		//}
		map.put("success",num);
		return map;
	}
	
	/**
	 * 定时锁定登记成绩
	 * @param data
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "registerExamState/{data}")
	@ResponseBody
	public Map registerExamState(@PathVariable("data") String data, HttpServletRequest request){
		Map map = new HashMap();
		int num = 0;
		Map formMap = Servlets.getParametersStartingWith(request, "");
		//if (EmptyUtils.isNotEmpty(data) && data.equals(DateUtils.getToday())) {
			num = gjtExamRecordNewService.registerExamState(formMap);
		//}
		map.put("success",num);
		return map;
	}
}
