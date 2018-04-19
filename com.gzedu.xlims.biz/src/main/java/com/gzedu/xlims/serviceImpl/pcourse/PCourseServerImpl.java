/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.pcourse;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JSONArray;
import com.gzedu.xlims.common.json.JSONObject;
import com.gzedu.xlims.dao.api.ApiDao;
import com.gzedu.xlims.pojo.opi.*;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年7月15日
 * @version 2.5
 *
 */
@Service
public class PCourseServerImpl implements PCourseServer {

	@Autowired
	ApiDao apiDao;

	/**
	 * 同步课程信息
	 */
	@Override
	public RSimpleData synchroCourse(OpiCourseData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiCourseData.class);
			tranceData.alias("COURSE", OpiCourse.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");
			String url = AppConfig.getProperty("courseSyncServer") + "/opi/course/synchroCourse.do";
			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
			
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 同步期课程信息
	 */
	@Override
	public RSimpleData synchroTermCourse(OpiTermCourseData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiTermCourseData.class);
			tranceData.alias("TERM", OpiTerm.class);
			tranceData.alias("TERM_COURSE", OpiTermCourse.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");

			String url = AppConfig.getProperty("courseSyncServer") + "/opi/termcourse/synchroTermCourse.do";

			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
			
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 同步课程班信息
	 */
	@Override
	public RSimpleData synchroTermClass(OpiTermClassData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiTermClassData.class);
			tranceData.alias("TERMCLASS", OpiTermClass.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");
			String url = AppConfig.getProperty("courseSyncServer") + "/opi/termcourse/synchroTermClass.do";

			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
						
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 删除学员信息同步接口
	 * @param data
	 * @return
	 */
	@Override
	public RSimpleData synchroStudDel(PoiDelStudentData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", PoiDelStudentData.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");

			String url = AppConfig.getProperty("courseSyncServer") + "/opi/student/synchroStudDel.do";

			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
			
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 删除教师信息同步接口
	 * @param data
	 * @return
	 */
	public RSimpleData synchroDelTeacher(OpiDelTeacherData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiDelTeacherData.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");

			String url = AppConfig.getProperty("courseSyncServer") + "/opi/teacher/synchroDelTeacher.do";

			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
			
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 教师基础信息同步接口
	 *
	 * @param data
	 * @return
	 */
	@Override
	public RSimpleData synchroTeacher(OpiTeacherData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiTeacherData.class);
			tranceData.alias("TEACHER", OpiTeacher.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");

			String url = AppConfig.getProperty("courseSyncServer") + "/opi/teacher/synchroTeacher.do";

			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
			
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 学员基础信息同步接口
	 *
	 * @param data
	 * @return
	 */
	@Override
	public RSimpleData synchroStudent(OpiStudentData data) {
		RSimpleData rr = null;
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiStudentData.class);
			tranceData.alias("STUDENT", OpiStudent.class);
			
			String xmlString = tranceData.toXML(data).replace("__", "_");

			String url = AppConfig.getProperty("courseSyncServer") + "/opi/student/synchroStudent.do";

			InputStream inputStream = HttpClientUtils.doHttpPostXml(url, xmlString, 60 * 1000, "GBK");
			
			if (inputStream != null) {
				XStream returnData = new XStream();
				returnData.alias("tranceData", RSimpleData.class);
				rr = (RSimpleData) returnData.fromXML(inputStream);
			}
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", ToolUtil.convertStreamToString(inputStream));
			addSyncRecord(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rr;
	}

	/**
	 * 同步选课信息
	 */
	@Override
	public RSimpleData synchroTermChoose(OpiTermChooseData data) {
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiTermChooseData.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");
			String url = AppConfig.getProperty("courseSyncServer") + "/opi/termcourse/synchroTermChoose.do";
			String xmlStr = HttpClientUtils.doHttpPostXml1(url, xmlString, 60 * 1000, "GBK");
			
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", xmlString);
			formMap.put("RETURN_VAL", xmlStr);
			addSyncRecord(formMap);
			
			if (EmptyUtils.isNotEmpty(xmlStr)) {
				Map tranceDataRoot = AnalyXmlUtil.parserXml(xmlStr);
				RSimpleData rr = new RSimpleData();
				rr.setStatus(NumberUtils.toInt((String) tranceDataRoot.get("status")));
				rr.setMsg((String) tranceDataRoot.get("msg"));
				return rr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除选课信息
	 */
	@Override
	public Map delStudChoose(Map formMap) {
		Map resultMap = new HashMap();
		try {
			String url = AppConfig.getProperty("courseSyncServer") + "/opi/termcourse/delStudChoose.do";
			Map params = new HashMap();
			params.put("formMap.CHOOSE_ID", ObjectUtils.toString(formMap.get("CHOOSE_ID")));
			params.put("formMap.STUD_ID", ObjectUtils.toString(formMap.get("STUD_ID")));
			params.put("formMap.CLASS_ID", ObjectUtils.toString(formMap.get("CLASS_ID")));
			params.put("formMap.TERMCOURSE_ID", ObjectUtils.toString(formMap.get("TERMCOURSE_ID")));
			params.put("formMap.UPDATED_BY", ObjectUtils.toString(formMap.get("UPDATED_BY")));
			String jsonStr = HttpClientUtils.doHttpPost(url, params, 3*1000, "GBK");
			
			// 记录同步接口记录
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", params.toString());
			formMap.put("RETURN_VAL", jsonStr);
			addSyncRecord(formMap);
			
			if (EmptyUtils.isNotEmpty(jsonStr)) {
				Map tranceDataRoot = AnalyXmlUtil.parserXml(jsonStr);
				resultMap.put("state", ObjectUtils.toString(tranceDataRoot.get("state")));
			} else {
				resultMap.put("state", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 调班学习记录迁移新的选课
	 */
	@Override
	public Map copyActDynaToNewCourse(Map formMap) {
		Map resultMap = new HashMap();
		try {
			String url = AppConfig.getProperty("courseSyncServer") + "/opi/termcourse/copyActDynaToNewCourse.do";
			doHttpPostTask(url + "?formMap.OLD_CHOOSEID=" + ObjectUtils.toString(formMap.get("OLD_CHOOSEID"))
					+ "&formMap.NEW_CHOOSEID=" + ObjectUtils.toString(formMap.get("NEW_CHOOSEID")));
			
			System.out.println(url + "?formMap.OLD_CHOOSEID=" + ObjectUtils.toString(formMap.get("OLD_CHOOSEID"))
			+ "&formMap.NEW_CHOOSEID=" + ObjectUtils.toString(formMap.get("NEW_CHOOSEID")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获取学习平台学习记录
	 * @param formMap
	 */
	@Override
	public Map getStudyRecord(Map formMap) {
		Map resultMap = new HashMap();
		try {
			String student_id = ObjectUtils.toString(formMap.get("STUDENT_ID"));
			if (EmptyUtils.isNotEmpty(student_id)) {
				formMap.put("USER_ID", student_id);
				syncStudyData(formMap);
			}
			resultMap.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "0");
		}
		return resultMap;
	}

	/**
	 * 获取学习平台的班级动态数据
	 * @param formMap
	 * @return
	 */
	@Transactional
	@Override
	public Map getClassDyna(Map formMap) {
		Map resultMap = new HashMap();
		try {
			String class_id = ObjectUtils.toString(formMap.get("CLASS_ID"));
			if (EmptyUtils.isNotEmpty(class_id)) {
				// 获取学习平台的班级动态记录接口数据
				String url = AppConfig.getProperty("courseSyncServer") + "/opi/student/getClassDyna.do";
				Map paramsMap = new HashMap();
				paramsMap.put("formMap.CLASS_ID", class_id);
				String jsonStr = HttpClientUtils.doHttpPost(url, paramsMap, 10*1000, "GBK");
				if (EmptyUtils.isNotEmpty(jsonStr)) {
					JSONObject jsonObj = new JSONObject(jsonStr);
					if (EmptyUtils.isNotEmpty(jsonObj)) {
						Map searchParams = new HashMap();
						searchParams.put("CLASS_ID", ObjectUtils.toString(jsonObj.get("CLASS_ID")));
						searchParams.put("CLASS_SUM_USER", ObjectUtils.toString(jsonObj.get("CLASS_SUM_USER")));
						searchParams.put("CLASS_SUM_POINT", ObjectUtils.toString(jsonObj.get("CLASS_SUM_POINT")));
						searchParams.put("CLASS_SUM_PROGRESS", ObjectUtils.toString(jsonObj.get("CLASS_SUM_PROGRESS")));
						searchParams.put("CLASS_SUM_TIME", ObjectUtils.toString(jsonObj.get("CLASS_SUM_TIME")));
						searchParams.put("CLASS_SUM_COUNT", ObjectUtils.toString(jsonObj.get("CLASS_SUM_COUNT")));
						searchParams.put("CLASS_AVG_POINT", ObjectUtils.toString(jsonObj.get("CLASS_AVG_POINT")));
						searchParams.put("CLASS_AVG_PROGRESS", ObjectUtils.toString(jsonObj.get("CLASS_AVG_PROGRESS")));
						searchParams.put("CLASS_AVG_TIME", ObjectUtils.toString(jsonObj.get("CLASS_AVG_TIME")));
						searchParams.put("CLASS_AVG_COUNT", ObjectUtils.toString(jsonObj.get("CLASS_AVG_COUNT")));
						searchParams.put("NOT_LOGIN_NUM", ObjectUtils.toString(jsonObj.get("NOT_LOGIN_NUM")));
						searchParams.put("TODAY_LOGIN_NUM", ObjectUtils.toString(jsonObj.get("TODAY_LOGIN_NUM")));
						searchParams.put("ALL_MUST_ACT", ObjectUtils.toString(jsonObj.get("ALL_MUST_ACT")));
						searchParams.put("ALL_MUST_POINT", ObjectUtils.toString(jsonObj.get("ALL_MUST_POINT")));
						searchParams.put("STATE", ObjectUtils.toString(jsonObj.get("STATE")));
						searchParams.put("NORM_AVG_PROGRESS", ObjectUtils.toString(jsonObj.get("NORM_AVG_PROGRESS")));
						searchParams.put("IS_NORM", ObjectUtils.toString(jsonObj.get("IS_NORM")));
						searchParams.put("COURSE_PASSING_MUSTACT", ObjectUtils.toString(jsonObj.get("COURSE_PASSING_MUSTACT")));
						searchParams.put("COURSE_PASSING_SCORE", ObjectUtils.toString(jsonObj.get("COURSE_PASSING_SCORE")));
						apiDao.updClassDyna(searchParams);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获取今天登陆的student_id
	 * @return
	 */
	@Deprecated
	public List getLoginStudent(Map searchParams) {
		List studentList = apiDao.getLoginStudent(searchParams);
		try {
			if (EmptyUtils.isNotEmpty(studentList)) {
				for (int i=0; i<studentList.size(); i++) {
					Map formMap = (Map)studentList.get(i);
					getStudyRecord(formMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return studentList;
	}

	/**
	 * 同步全部的学生数据
	 * @return
	 */
	@Override
	public int syncAllStudentData(){
		List studentList = apiDao.syncAllStudentData();
		int num = 0;
		try {
			if (EmptyUtils.isNotEmpty(studentList)) {
				for (int i=0; i<studentList.size(); i++) {
					Map formMap = (Map)studentList.get(i);
					formMap.put("TYPE", "all");
					formMap.put("USER_ID", ObjectUtils.toString(formMap.get("STUDENT_ID")));
					syncStudyData(formMap);
					num++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 同步学习平台的学习记录
	 * @param formMap
	 * @return
	 */
	@Deprecated
	public int syncStudyData(Map formMap) {
		int num = 0;
		try {
			List appList = apiDao.getAppIdList();
			if (EmptyUtils.isNotEmpty(appList)) {
				String appId = "";
				for (int i=0; i<appList.size(); i++) {
					Map appMap = (Map)appList.get(i);
					appId += ObjectUtils.toString(appMap.get("APPID")) + ",";
				}
				if (EmptyUtils.isNotEmpty(appId) && ",".equals(appId.substring(appId.length()-1, appId.length()))) {
					appId = appId.substring(0, appId.length()-1);
				}
				String url = AppConfig.getProperty("courseSyncServer") + "/opi/student/getStudentStudySituation.do";
				Map paramsMap = new HashMap();
				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("APP_IDS")))) {
					paramsMap.put("formMap.APP_IDS", ObjectUtils.toString(formMap.get("APP_IDS")));
				} else {
					paramsMap.put("formMap.APP_IDS", appId);
				}
				
				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("TYPE")))) {
					paramsMap.put("formMap.TYPE", ObjectUtils.toString(formMap.get("TYPE")));
				} else {
					paramsMap.put("formMap.TYPE", "");
				}
				
				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("USER_ID")))) {
					paramsMap.put("formMap.USER_ID", ObjectUtils.toString(formMap.get("USER_ID")));
				}
				
				String jsonStr = HttpClientUtils.doHttpPost(url, paramsMap, 60 * 1000, "GBK");
				if (EmptyUtils.isNotEmpty(jsonStr)) {
					JSONObject jsonObj = new JSONObject(jsonStr);
					if (EmptyUtils.isNotEmpty(jsonObj)) {
						if (jsonObj.has("data")) {
							JSONArray dataJson = jsonObj.getJSONArray("data");
							if (EmptyUtils.isNotEmpty(dataJson)) {
								for (int i=0; i<dataJson.length(); i++) {
									JSONObject userObj = (JSONObject) dataJson.get(i);
									if (EmptyUtils.isNotEmpty(userObj)) {
										
										String student_id = ObjectUtils.toString(userObj.get("USER_ID"));
										
										Map searchParams = new HashMap();
										String schedule = "";
										if (userObj.has("MY_PROGRESS")) {
											String my_progress = ObjectUtils.toString(userObj.get("MY_PROGRESS"));
											
											if ("N".equals(my_progress)) {
												schedule = "0";
											} else if ("Y".equals(my_progress)) {
												schedule = "100";
											} else {
												schedule = my_progress;
											}
										}
										if (userObj.has("MY_POINT")) {
											searchParams.put("STUDY_SCORE", ObjectUtils.toString(userObj.get("MY_POINT")));			    // 学习得分
										}
										if (userObj.has("FIRST_LOGIN_TIME")) {
											searchParams.put("FIRST_DATE", ObjectUtils.toString(userObj.get("FIRST_LOGIN_TIME")));  	// 第一次登陆学习平台时间
										}
										if (userObj.has("LOGIN_OUT_DT")) {
											searchParams.put("LAST_DATE", ObjectUtils.toString(userObj.get("LOGIN_OUT_DT"))); 			// 最后一次登陆学习平台时间
										}
										if (userObj.has("ONLINE_COUNT")) {
											searchParams.put("LOGIN_COUNT", ObjectUtils.toString(userObj.get("ONLINE_COUNT"))); 		// 登陆次数
										}
										if (userObj.has("PC_ONLINE_COUNT")) {
											searchParams.put("PC_ONLINE_COUNT", ObjectUtils.toString(userObj.get("PC_ONLINE_COUNT"))); 	// PC登陆次数
										}
										if (userObj.has("APP_ONLINE_COUNT")) {
											searchParams.put("APP_ONLINE_COUNT", ObjectUtils.toString(userObj.get("APP_ONLINE_COUNT")));// APP登陆次数
										}
										if (userObj.has("ONLINE_TIME")) {
											searchParams.put("LOGIN_TIME", ObjectUtils.toString(userObj.get("ONLINE_TIME"))); 			// 登陆时长
										}
										if (userObj.has("PC_ONLINE_TIME")) {
											searchParams.put("PC_ONLINE_TIME", ObjectUtils.toString(userObj.get("PC_ONLINE_TIME"))); 	// PC登陆时长
										}
										if (userObj.has("APP_ONLINE_TIME")) {
											searchParams.put("APP_ONLINE_TIME", ObjectUtils.toString(userObj.get("APP_ONLINE_TIME")));	// APP登陆时长
										}
										if (userObj.has("ACT_COUNT")) {
											searchParams.put("ACT_COUNT", ObjectUtils.toString(userObj.get("ACT_COUNT"))); 				// 课程活动个数
										}
										if (userObj.has("MUST_ACTCOUNT")) {
											searchParams.put("MUST_ACTCOUNT", ObjectUtils.toString(userObj.get("MUST_ACTCOUNT"))); 		// 课程必修活动个数
										}
										if (userObj.has("MY_ACTCOUNT")) {
											searchParams.put("MY_ACTCOUNT", ObjectUtils.toString(userObj.get("MY_ACTCOUNT"))); 			// 我完成的活动个数
										}
										if (userObj.has("MY_MUSTACTCOUNT")) {
											searchParams.put("MY_MUSTACTCOUNT", ObjectUtils.toString(userObj.get("MY_MUSTACTCOUNT")));	// 我完成的必修活动个数
										}
										// 是否在线
										if (userObj.has("IS_ONLINE")) {
											if ("Y".equals(userObj.get("IS_ONLINE"))) {
												searchParams.put("IS_ONLINE", "0");
											} else {
												searchParams.put("IS_ONLINE", "1");
											}
										}

										searchParams.put("SCHEDULE", ObjectUtils.toString(schedule)); // 学习进度
										searchParams.put("STUDENT_ID", student_id);
										searchParams.put("CHOOSE_ID", ObjectUtils.toString(userObj.get("CHOOSE_ID")));
										
										// 初始化学情数据  GJT_STUDY_RECORD这个表后面要废掉，统一用GJT_STUDENT_STUDY_SITUATION这个学情表
										List studentList = apiDao.getRecChoose(formMap);
										if (EmptyUtils.isNotEmpty(studentList)) {
											apiDao.insertStudyRecord(formMap);
										}
										List studyList = apiDao.getStudyChoose(formMap);
										if (EmptyUtils.isNotEmpty(studyList)) {
											apiDao.insertStudySituation(formMap);
										}
										
										apiDao.updStudyRecord(searchParams);
										apiDao.updStudySituation(searchParams);
										
										// 只有学习中的时候才更新下面表的成绩数据
										List recList = apiDao.getRecStudy(searchParams);
										if (EmptyUtils.isNotEmpty(recList)) {
											apiDao.updRecScore(searchParams);
										}
									}
									num++;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 同步考试平台的考试记录
	 * @param formMap
	 * @return
	 */
	public int syncExamData(Map formMap) {
		int num = 0;
		try {
			String url = AppConfig.getProperty("examServer") + "/api/getExamResult.do";
			List studentList = apiDao.getStudentList();
			if(EmptyUtils.isNotEmpty(studentList) && studentList.size() > 0){
				for(int i = 0;i < studentList.size();i++){
					Map<String,Object> param = new HashMap<String,Object>();
					Map<String,Object> paramMap = new HashMap<String,Object>();
					Map m = (Map) studentList.get(i);
					param.put("examNo", m.get("EXAM_NO"));
					param.put("examBatchCode", m.get("EXAM_BATCH_CODE"));
					param.put("userNo", m.get("XH"));
					param.put("userNum", m.get("SFZH"));
					paramMap.put("APPOINTMENT_ID", m.get("APPOINTMENT_ID"));
					String jsonStr = HttpClientUtils.doHttpPost(url, param, 60 * 1000, "UTF-8");
					if(EmptyUtils.isNotEmpty(jsonStr)){
						JSONObject jsonObj = new JSONObject(jsonStr);
						if("200".equals(jsonObj.get("code").toString()) && jsonObj.has("point")){
							if(EmptyUtils.isNotEmpty(jsonObj.get("point"))){
								double point = Double.parseDouble(jsonObj.get("point").toString());
								if(point >= 60){
									paramMap.put("EXAM_STATUS", "1");
								}else{
									paramMap.put("EXAM_STATUS", "2");
								}
								paramMap.put("EXAM_SCORE", point);
								paramMap.put("EXAM_COUNT", jsonObj.get("count"));
								num = num + apiDao.updateExamScore(paramMap);
							}
						}
					}
				}
			}
			apiDao.updateExamState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 同步接口记录新增
	 * @param formMap
	 * @return
	 */
	@Override
	public int addSyncRecord(Map formMap) {
		int num = 0;
		try {
			formMap.put("SYNC_ID", SequenceUUID.getSequence());
			num = apiDao.addSyncRecord(formMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 多线程POST请求  
	 * @throws Exception
	 */
	private Runnable doHttpPostTask(final String url) {
		try {
			String responseMsg = HttpClientUtils.doHttpPost(url, null, 10 * 1000, "GBK");
			// System.out.println("===="+url+"==="+responseMsg);
			// 记录同步接口记录
			Map formMap = new HashMap();
			formMap.put("SYNC_URL", url);
			formMap.put("SYNC_PARM", "");
			formMap.put("RETURN_VAL", responseMsg);
			addSyncRecord(formMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		/*OpiTchchoose tchchoose = new OpiTchchoose();
		tchchoose.setCLASS_ID("55c9c86a0a50822c4e58436937da6ab3");
		tchchoose.setCREATED_BY("admin");
		tchchoose.setFROM_JIJIAO("N");
		tchchoose.setIS_INVISIBILITY("N");
		tchchoose.setLMS_NO("gkxuelidudao");
		tchchoose.setMANAGER_CHOOSE_ID(SequenceUUID.getSequence());
		tchchoose.setMANAGER_ID("30e0e5c7f23a44d18bf30741bf72879c");
		tchchoose.setMANAGER_ROLE("inspector");
		tchchoose.setTERMCOURSE_ID("2402f4d8d5cf4edaa0509355d7abdff6");
		OpiTermChooseData data = new OpiTermChooseData(null, tchchoose);
		
		
		try {
			XStream tranceData = new XStream();
			tranceData.alias("tranceData", OpiTermChooseData.class);
			String xmlString = tranceData.toXML(data).replace("__", "_");
			String url = "http://pcourse.gzedu.com" + "/opi/termcourse/synchroTermChoose.do";
			String xmlStr = HttpClientUtils.doHttpPostXml1(url, xmlString, 60 * 1000, "GBK");
			
			System.out.println(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		String url = "http://texam.oucnet.cn/api/getExamResult.do";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("examBatchCode", "201711220006");
		param.put("examNo", "7092");
		param.put("userNo", "1644101256142");
		String jsonStr = HttpClientUtils.doHttpPost(url, param, 60 * 1000, "UTF-8");
		if(EmptyUtils.isNotEmpty(jsonStr)){
			JSONObject jsonObj = new JSONObject(jsonStr);
			if("200".equals(jsonObj.get("code").toString()) && jsonObj.has("point")){
				double point = (Double) jsonObj.get("point");
				if(point >= 60){
					param.put("EXAM_STATUS", "1");
				}else{
					param.put("EXAM_STATUS", "2");
				}
				param.put("EXAM_SCORE", point);
				System.out.println("param:"+param);
			}
		}
		
	}

}
