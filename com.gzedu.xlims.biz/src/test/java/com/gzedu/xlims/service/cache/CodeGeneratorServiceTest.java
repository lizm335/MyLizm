package com.gzedu.xlims.service.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.dao.exam.GjtExamSubjectNewDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class CodeGeneratorServiceTest {

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private GjtExamSubjectNewService gjtExamSubjectNewService;

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private GjtExamSubjectNewDao gjtExamSubjectNewDao;

	@Autowired
	private GjtCourseDao gjtCourseDao;

	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;
	@Autowired
	private GjtExamPlanNewDao gjtExamPlanNewDao;
	@Autowired
	private GjtExamAppointmentNewDao gjtExamAppointmentNewDao;
	@Autowired
	private GjtExamPointNewDao gjtExamPointNewDao;

	// @Test
	public void testCodeGenerator() {
		System.out.println("~~~~~~~" + codeGeneratorService.codeGenerator(CacheConstants.SUBJECT_CODE));
	}

	// @Test
	public void jpaTest() {
		GjtExamSubjectNew entity = new GjtExamSubjectNew();
		entity.setCourseId("4bf5621bf7514866a8c671ea88a0b3c9");
		entity.setCreatedBy("124");
		entity.setName("test1");
		;
		entity.setType(2);
		entity.setCreatedDt(new Date());
		entity.setXxId("14eb762198734d4983b5b1865ef2c899");
		entity = gjtExamSubjectNewService.insert(entity);
		System.out.println(entity);
	}

	@Test
	public void sqlTest() {
		// List<GjtCourse> list =
		// gjtCourseDao.findByGjtSchoolInfoId("14eb762198734d4983b5b1865ef2c899");
		// System.out.println(list.size());
//		 testGjtExamPlanNew();
//		testGjtExamSubjectNew();
		 testGjtExamBatchNew();
//		testGjtExamAppointMent();
	}
	
	// @Test
	public void testCourse() {
		//List<GjtCourse> list = gjtCourseDao.findByXxid("14eb762198734d4983b5b1865ef2c899");
		//System.out.println("``````````````````````" + list.size());
	}

	public void testGjtExamAppointMent() {
//		GjtExamPlanNew entity = gjtExamAppointmentNewDao.getExamPlanByCourseid("123");
//		System.out.println(1);
		List<String> ids = new ArrayList<String>();
		ids.add("4e7ed08a7f00000122d555b2950dd47a");
		ids.add("4e7ed0267f00000122d555b2720ace3a");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "0");
		params.put("examBatchCode", "201610130007");
		
		List<Map<String, Object>> list = gjtExamAppointmentNewDao.appointmentListBySearch(params);
		System.out.println(list.toString());
	}
	
	public void testGjtExamSubjectNew() {
		// List<GjtCourse> courseList =
		// gjtExamSubjectNewDao.noSubjectCourseList("28949de42f0244b9a70d9c8e06c6cfd4");
		// System.out.println("~~~~~~~"+courseList.size());
		// System.out.println(gjtExamSubjectNewDao.noSubjectCourseList("14eb762198734d4983b5b1865ef2c899"));
		List<GjtExamSubjectNew> list = new ArrayList<GjtExamSubjectNew>();
		GjtExamSubjectNew subject = new GjtExamSubjectNew();
		// subject.setSubjectId("s123");
		subject.setSubjectCode("BS201609230002");
		subject.setExamNo("no124");
		subject.setName("test科目");
		subject.setMemo("junit test data");
		subject.setCreatedBy("junit test impl");
		subject.setXxId("14eb762198734d4983b5b1865ef2c899");
		subject.setType(3);
		subject.setStatus(3);
		subject.setCourseId("4bf5621bf7514866a8c671ea88a0b3c9");
		// list.add(subject);
		// if(null != gjtExamSubjectNewService.insert(subject)) {
		// System.out.println("create success");
		// }

		List<String> ids = new ArrayList<String>();
		ids.add("998fe567a061087238d9800ac2b12d2f");
		ids.add("e21b111ba121d8aed81428bee5b9d376");
		// list = gjtExamSubjectNewExtraDao.existList(ids, 2);
//		Map<String, GjtExamSubjectNew> map = gjtExamSubjectNewDao.existList(ids, 2);
//		System.out.println(map.toString());
//		
//		ids.clear();
//		ids.add("ddddeb70741aafaba8e6f79b611ccdaa");
//		Map<String, String> map1 = gjtExamSubjectNewDao.plansCountBySubject(ids);
//		System.out.println(map1.toString());
//		System.out.println(gjtExamSubjectNewService.delete(ids, "696c75a310b44a7f9dd61516ea84fbe3"));

		// GjtExamSubjectNew subject1 = new GjtExamSubjectNew();
		// subject1.setSubjectId("s123");
		// subject1.setSubjectCode("BS201609230001");
		// subject1.setExamNo("no123");
		// subject1.setName("test科目");
		// subject1.setMemo("junit test data1");
		// subject1.setCreatedBy("junit test");
		// subject1.setXxId("14eb762198734d4983b5b1865ef2c899");
		// subject1.setType(2);
		// subject1.setStatus(3);
		// subject1.setCourseId("9d82ca123acb4af5bee24bf68576d0c1");
		// list.add(subject1);
		//
		// int rs = gjtExamSubjectNewService.insertBatch(list);
		// System.out.println("~~~~~~~~~~~~~~~~~~"+rs);

	}

	public void testGjtExamBatchNew() {
		GjtExamBatchNew batch = new GjtExamBatchNew();
		batch.setBookEnd(new Date());
		batch.setBookSt(new Date());
		batch.setCreatedBy("junit test1");
		batch.setName("test1");
		batch.setOfflineEnd(new Date());
		batch.setOfflineSt(new Date());
		batch.setOnlineEnd(new Date());
		batch.setOnlineSt(new Date());
		batch.setStudyYearId("e44ea0d99298709f2776ffc040b37886");
		batch.setXxId("9d82ca123acb4af5bee24bf68576d0c1");

//		if (null != gjtExamBatchNewService.insert(batch)) {
//			System.out.println("create success");
//		}
//		List<String> ids = new ArrayList();
//		ids.add("113b79aba29d7f83ea65a888305ac87d");
//		int rs = gjtExamBatchNewService.delete(ids, "696c75a310b44a7f9dd61516ea84fbe3");
//		System.out.println("`````````````````"+rs);
		
//		Map<String, Object> map = gjtExamBatchNewService.deleteBatch("e3856daf569e5ac0986b07fc25107446", "696c75a310b44a7f9dd61516ea84fbe3");
//		System.out.println(map);
//		GjtExamBatchNew b = gjtExamBatchNewService.queryByExamBatchCodeAndXxId("201609300005", "696c75a310b44a7f9dd61516ea84fbe3");
//		System.out.println(b.getName());
//		
//		GjtExamBatchNew b1 = gjtExamBatchNewService.queryByExamBatchCode("201609300005");
//		System.out.println(b1.getName());
		
//		List<String> list = gjtExamPointNewDao.appointStudentidList("4e3d66af9d6b4749885ba100d461f143", "955ca70376ec4d60abe139d07bff9bdf");
//		System.out.println(list.toString());
		
		int t = gjtExamAppointmentNewDao.countAppointMents("123");
		System.out.println(t);
	}

	public void testGjtExamPlanNew() {
		GjtExamPlanNew plan = new GjtExamPlanNew();
		GjtExamBatchNew batch = new GjtExamBatchNew();
		batch.setExamBatchCode("201709230002");
		batch.setExamBatchId("cb5bbf2d00a793ac0400ba7cae24c142");
		plan.setExamBatchCode("201709230002");
		plan.setExamBatchNew(batch);
		GjtExamSubjectNew subject = new GjtExamSubjectNew();
		plan.setExamSubjectNew(subject);
		subject.setSubjectCode("BS201709230001");
		subject.setSubjectId("ddddeb70741aafaba8e6f79b611ccdaa");
		plan.setSubjectCode("BS201709230001");
		plan.setBookEnd(new Date());
		plan.setBookSt(new Date());
//		plan.setExamBatchId("cb5bbf2d00a793ac0400ba7cae24c142");
//		plan.setExamEnd(new Date());
//		plan.setExamSt(new Date());
		plan.setStudyYearId("e44ea0d99298709f2776ffc040b37886");
		plan.setCreatedBy("junit test1");
		plan.setUpdatedBy("junit test1");
//		plan.setSubjectId("ddddeb70741aafaba8e6f79b611ccdaa");
		plan.setXxId("14eb762198734d4983b5b1865ef2c899");

//		 if(null != gjtExamPlanNewService.insert(plan)) {
//			 System.out.println("create success");
//		 }
		// List<GjtExamPlanNew> l = new ArrayList();
		// l.add(plan);
		////
		// GjtExamPlanNew plan1 = new GjtExamPlanNew();
		// plan1.setExamPlanId("BS201609230001201609230001");
		// plan1.setBookEnd(new Date());
		// plan1.setBookSt(new Date());
		// plan1.setExamBatchId("90046d69578067d60ceeced25f1ddd7d");
		// plan1.setExamEnd(new Date());
		// plan1.setExamSt(new Date());
		// plan1.setStudyYearId("e44ea0d99298709f2776ffc040b37886");
		// plan1.setSubjectId("ddddeb70741aafaba8e6f79b611ccdaa");
		// plan1.setCreatedBy("junit test update?");
		// plan1.setXxId("14eb762198734d4983b5b1865ef2c899");
		// l.add(plan1);
		//
		// l = gjtExamPlanNewDao.save(l);
		// System.out.println(l.size());
//		List<GjtExamPlanNew> list = gjtExamPlanNewService.plansForSettingByBatchCode("a7d06ec60d54f0a057990cab7471a953");
//		System.out.println(list.get(0).getExamBatchNew().getName());

		List<String> planidList = new ArrayList<String>();
		planidList.add("BS201609230001201609290001");
		planidList.add("BS201709230001201709230002");
		Map<String, GjtExamPlanNew> planMap = gjtExamPlanNewDao.updatePlanMapByIds(planidList);
		
		List<GjtExamPlanNew> updateList = new ArrayList<GjtExamPlanNew>();
		GjtExamPlanNew plan1;
		for (String pid : planidList) {
			plan1 = planMap.get(pid);
			plan1.setUpdatedBy("1");
			if("BS201709230001201709230002".equals(plan1.getExamPlanId())) {
				updateList.add(plan1);
			}
		}
		
		gjtExamPlanNewDao.save(updateList);
		System.out.println("~~~~~~~~~over");
		
//		System.out.println(planMap);
		// Map<String, Object> searchParams = new HashMap();
		// PageRequest pageRequst = new PageRequest(0, 10);
		// Page<GjtExamPlanNew> list =
		// gjtExamPlanNewService.queryAll("14eb762198734d4983b5b1865ef2c899",
		// searchParams, pageRequst);
		// for (GjtExamPlanNew e : list) {
		// System.out.println("id:"+e.getStudyYearId());
		// System.out.println("code:"+e.getStudyYearInfo().getStudyYearCode());
		// }
	}

}
