
/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File MenueDaoTest.java
 * @Date:2016年4月26日下午2:18:30
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.service.pcourse;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.pojo.opi.OpiCourse;
import com.gzedu.xlims.pojo.opi.OpiCourseData;
import com.gzedu.xlims.pojo.opi.OpiDelTeacherData;
import com.gzedu.xlims.pojo.opi.OpiStudent;
import com.gzedu.xlims.pojo.opi.OpiStudentData;
import com.gzedu.xlims.pojo.opi.OpiTeacher;
import com.gzedu.xlims.pojo.opi.OpiTeacherData;
import com.gzedu.xlims.pojo.opi.OpiTerm;
import com.gzedu.xlims.pojo.opi.OpiTermClass;
import com.gzedu.xlims.pojo.opi.OpiTermClassData;
import com.gzedu.xlims.pojo.opi.OpiTermCourse;
import com.gzedu.xlims.pojo.opi.OpiTermCourseData;
import com.gzedu.xlims.pojo.opi.PoiDelStudentData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.serviceImpl.pcourse.PCourseServerImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class TblPriMenueServiceTest {

	@Autowired
	private PCourseServerImpl pCourseServer;

	String APP_ID = "APP014";

	@Test // 课程同步
	public void synchroCourse() {
		OpiCourse obj = new OpiCourse();

		obj.setCOURSE_ID("8b28f37123123123555b20c5d7491");
		obj.setCOURSE_NAME("sdf");
		obj.setCOURSE_STATUS("sdf");
		obj.setCREATED_BY("sdf");
		obj.setPUBLISH_DT("sdf");
		obj.setCOURSE_CODE("123");
		obj.setCOURSE_DES("sdf");

		OpiCourseData data = new OpiCourseData(APP_ID, obj);
		RSimpleData synchroCourse = pCourseServer.synchroCourse(data);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus() == 1);
		}
	}

	@Test // 学年度课程
	public void synchroTermCourse() {
		OpiTerm opiTerm = new OpiTerm();
		opiTerm.setCREATED_BY("123");
		opiTerm.setTERM_CODE("11");
		opiTerm.setTERM_END_DT(DateUtils.getTimeYMD(new Date()));
		opiTerm.setTERM_START_DT(DateUtils.getTimeYMD(new Date()));
		opiTerm.setTERM_NAME(StudyYear.getName(11));
		opiTerm.setTERM_ID("123");

		OpiTermCourse opiTermCourse = new OpiTermCourse();
		opiTermCourse.setTERMCOURSE_ID("561651651651sdf");
		opiTermCourse.setTERM_ID("123");
		opiTermCourse.setCOURSE_ID("4abd5c1c7d2540db84ce05c36fae7b43");
		opiTermCourse.setCREATED_BY("123");
		opiTermCourse.setTERMCOURSE_START_DT(DateUtils.getTimeYMD(new Date()));
		opiTermCourse.setTERMCOURSE_END_DT(DateUtils.getTimeYMD(new Date()));

		OpiTermCourseData data = new OpiTermCourseData(APP_ID, opiTerm, opiTermCourse);
		RSimpleData synchroCourse = pCourseServer.synchroTermCourse(data);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus() + synchroCourse.getMsg());
		}
	}

	@Test // 期班级同步接口
	public void synchroTermClass() {
		OpiTermClass termClass = new OpiTermClass();
		termClass.setCLASS_ID("sdf");
		termClass.setTERMCOURSE_ID("sdf");
		termClass.setCLASS_NAME("sdf");
		termClass.setCREATED_BY("sdf");
		termClass.setIS_EXPERIENCE("Y");

		OpiTermClassData data = new OpiTermClassData(termClass);
		RSimpleData synchroCourse = pCourseServer.synchroTermClass(data);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus() + synchroCourse.getMsg());
		}
	}

	/**
	 * 删除学员信息同步接口
	 */
	@Test
	public void synchroStudDel() {
		PoiDelStudentData term = new PoiDelStudentData("8b28f37123123123555b20c5d7491", "注销学籍");

		RSimpleData synchroCourse = pCourseServer.synchroStudDel(term);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus());
		}
	}

	/**
	 * 删除教师信息同步接口
	 */
	@Test
	public void synchroDelTeacher() {
		OpiDelTeacherData term = new OpiDelTeacherData("8b28f37123123123555b20c5d7491,8b28f37123123123555b20c5d7492");

		RSimpleData synchroCourse = pCourseServer.synchroDelTeacher(term);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus());
		}
	}

	/**
	 * 教师基础信息同步接口
	 */
	@Test
	public void synchroTeacher() {
		OpiTeacher teach = new OpiTeacher();
		teach.setCREATED_BY("2016-07-19 15:28:51");
		teach.setEE_NO("111");
		teach.setEMAIL("ee@eenet.com");
		teach.setIMG_PATH("头像地址");
		teach.setLMS_NO("8b28f3747656464122d555b20c5d7411");
		teach.setMANAGER_ID("8b28f3747f12400122d555b20c5d7491");
		teach.setMANAGER_ROLE("teacher");
		teach.setMOBILE_NO("13800138000");
		teach.setREALNAME("张三");
		teach.setSEX("1");
		OpiTeacherData opiTeacherData = new OpiTeacherData(APP_ID, teach);
		RSimpleData synchroCourse = pCourseServer.synchroTeacher(opiTeacherData);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus() + synchroCourse.getMsg());
		}
	}

	/**
	 * 教师基础信息同步接口
	 */
	@Test
	public void synchroStudent() {
		OpiStudent op = new OpiStudent();
		op.setCREATED_BY("创建人ID");
		op.setEE_NO("学员EE账号");
		op.setEMAIL("学员邮箱");
		op.setIMG_PATH("学员头像URL地址");
		op.setLMS_NO("学员学号");
		op.setMANAGER_ID("班主任id");
		op.setMOBILE_NO("学员手机号码");
		op.setREALNAME("学员姓名");
		op.setSEX("学员性别");
		op.setSTUD_AREA("学员所在省份");
		op.setSTUD_ID("");
		op.setSTUD_USER_ID("ATID");
		op.setXLCLASS_ID("实体班级id");
		op.setXLCLASS_NAME("实体班级名称");

		OpiStudentData od = new OpiStudentData(APP_ID, op);
		RSimpleData synchroCourse = pCourseServer.synchroStudent(od);
		if (synchroCourse == null) {
			System.out.println("同步失败");
		} else {
			System.out.println(synchroCourse.getStatus() + synchroCourse.getMsg());
		}
	}

}
