package com.ouchgzee.study.serviceImpl.course;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JSONObject;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.GjtExamRecordNewDao;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ouchgzee.study.dao.course.CourseLearningDao;
import com.ouchgzee.study.dao.exam.ExamServeDao;
import com.ouchgzee.study.service.course.CourseLearningService;

import pdf.CreatePdf;

@Service
public class CourseLearningServiceImpl implements CourseLearningService {

	private final static Logger log = LoggerFactory.getLogger(CourseLearningServiceImpl.class);

	private static DecimalFormat FMT = new DecimalFormat("###.#");
	private static DecimalFormat JBRSFMT = new DecimalFormat("###");
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static final String KEY = "/book/index/student/urlLogin.do"; // 暂未用

	@Autowired
	private CourseLearningDao courseLearningDao;

	@Autowired
	private ExamServeDao examServeDao;

	@Autowired
	private GjtExamRecordNewDao gjtExamRecordNewDao;

	@Resource
	private CourseRemoteService courseRemoteService;

	@Resource
	private CourseChangeService courseChangeService;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	private CommonDao commonDao;

	private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.#");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map learningByTerm(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			// 增加返回学籍状态
			resultMap.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));

			String userType = ObjectUtils.toString(searchParams.get("USER_TYPE"), "");
			String xjzt = ObjectUtils.toString(searchParams.get("XJZT"), "");
			List userTypeList = examServeDao.getStuXjZtAndUserType(searchParams);
			if (EmptyUtils.isNotEmpty(userTypeList)) {
				Map typeMap = (Map) userTypeList.get(0);
				userType = ObjectUtils.toString(typeMap.get("USER_TYPE"));
				xjzt = ObjectUtils.toString(typeMap.get("XJZT"));
			}

			searchParams.put("USER_TYPE", userType);
			searchParams.put("XJZT", xjzt);

			String entranceTerm = null;
			List entrance = courseLearningDao.getEntranceTermByStu(searchParams);
			if (EmptyUtils.isNotEmpty(entrance)) {
				Map tEntMap = (Map) entrance.get(0);
				entranceTerm = ObjectUtils.toString(tEntMap.get("GRADE_ID"), "");
			}
			List terms = courseLearningDao.getTermListByLoginStudent(searchParams); // 学期列表
			List indexList = courseLearningDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
			if (EmptyUtils.isNotEmpty(indexList)) {
				Map map = (Map) indexList.get(0);
				int index = Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")));

				if (ObjectUtils.toString(map.get("GRADE_ID")).equals(entranceTerm)) {
					resultMap.put("IS_NEWSTU", Constants.IS_NEWSTUDENT);
				} else {
					resultMap.put("IS_NEWSTU", Constants.IS_NONEWSTUDENT);
				}

				if (index <= 5) {
					if (EmptyUtils.isNotEmpty(terms)) {
						String term_id = ObjectUtils.toString(map.get("GRADE_ID"));
						for (int i = 0; i < terms.size(); i++) {
							Map tMap = (Map) terms.get(i);
							if (ObjectUtils.toString(tMap.get("TERM_ID")).equals(term_id)) {
								resultMap.put("TERMINDEX", ObjectUtils.toString(tMap.get("KKXQ")));
							} else {
								if (terms.size() < Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")))) {
									resultMap.put("TERMINDEX", terms.size());
								} else {
									resultMap.put("TERMINDEX", ObjectUtils.toString(map.get("GRADE_INDEX")));
								}
							}
						}
					}
					// resultMap.put("TERMINDEX",
					// ObjectUtils.toString(map.get("GRADE_INDEX")));
				} else {
					if (EmptyUtils.isNotEmpty(terms)) {
						resultMap.put("TERMINDEX", terms.size());
					} else {
						resultMap.put("TERMINDEX", "1");
					}
				}
			} else {
				// 查不到当前学期时，根据学期列表中的学期结束时间与当前时间进行对比，查找离当前时间最近的数据，取对应的开课学期KKXQ
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String current = sdf.format(new Date());
				Date currentDate = sdf.parse(current);
				List listDate = new ArrayList();
				List listIndex = new ArrayList();
				for (int i = 0; i < terms.size(); i++) {
					Map dateMap = (Map) terms.get(i);
					String strEnd = (String) dateMap.get("END_DATE");
					Date endDate = sdf.parse(strEnd);
					long days = (endDate.getTime() - currentDate.getTime()) / (1000 * 3600 * 24);
					Map map = new HashMap();
					map.put("days", Math.abs(days));
					map.put("kkxq", ObjectUtils.toString(dateMap.get("KKXQ")));
					listDate.add(map);
					listIndex.add(Math.abs(days));
				}
				Long longIndex = (Long) Collections.min(listIndex);
				String index = null;
				for (int i = 0; i < listDate.size(); i++) {
					Map map1 = (Map) listDate.get(i);
					Long days = (Long) map1.get("days");
					if (longIndex.equals(days)) {
						index = (String) map1.get("kkxq");
					}
				}
				resultMap.put("TERMINDEX", index);
			}

			// ===========待预约考试统计开始==============//
			searchParams.put("CURRENT_FLAG", "1");
			List current_plan = examServeDao.getExamBatchData(searchParams); // 查询当前考试计划
			String exam_batch_code = null;
			String end_date = null;
			if (EmptyUtils.isNotEmpty(current_plan)) {
				Map currentMap = (Map) current_plan.get(0);
				exam_batch_code = ObjectUtils.toString(currentMap.get("EXAM_BATCH_CODE"));
				end_date = ObjectUtils.toString(currentMap.get("END_DATE"));
			}
			searchParams.remove("CURRENT_FLAG");

			List<Object> resultList = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(exam_batch_code)) {
				searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
				searchParams.put("BESPEAK_STATE", "0");
				searchParams.put("END_DATE", end_date);
				List recResultList = examServeDao.getChooseCourseByStudent(searchParams);
				if (EmptyUtils.isNotEmpty(recResultList)) {
					for (int i = 0; i < recResultList.size(); i++) {
						Map temp1 = (Map) recResultList.get(i);
						Map courseMap = new HashMap();

						courseMap.put("APPOINTMENT_FLAG", "1"); // 待预约列表
						courseMap.put("XX_ID", searchParams.get("XX_ID"));
						courseMap.put("EXAM_BATCH_CODE", searchParams.get("EXAM_BATCH_CODE"));
						courseMap.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
						// courseMap.put("TERM_ID",ObjectUtils.toString(temp1.get("TERM_ID")));
						courseMap.put("USER_TYPE", ObjectUtils.toString(searchParams.get("USER_TYPE")));
						courseMap.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));
						courseMap.put("SCHOOL_MODEL", ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")));

						List courseListInfo = examServeDao.getExamPlanAndCourseInfo(courseMap);

						if (EmptyUtils.isNotEmpty(courseListInfo)) {
							for (int j = 0; j < courseListInfo.size(); j++) {
								Map temp2 = (Map) courseListInfo.get(j);

								Map temp3 = new LinkedHashMap();

								temp3.put("TERM_CODE", ObjectUtils.toString(temp1.get("TERM_CODE")));
								temp3.put("TERM_ID", ObjectUtils.toString(temp1.get("TERM_ID")));
								temp3.put("TERM_NAME", ObjectUtils.toString(temp1.get("TERM_NAME")));
								temp3.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
								temp3.put("COURSE_NAME", ObjectUtils.toString(temp1.get("COURSE_NAME")));
								temp3.put("TYPE", ObjectUtils.toString(temp2.get("TYPE")));
								temp3.put("KCH", ObjectUtils.toString(temp1.get("KCH")));
								temp3.put("COURSE_COST", ObjectUtils.toString(temp1.get("COURSE_COST")));
								temp3.put("MAKEUP", ObjectUtils.toString(temp1.get("MAKEUP")));
								temp3.put("PAY_STATE", ObjectUtils.toString(temp1.get("PAY_STATE")));
								temp3.put("BESPEAK_STATE", ObjectUtils.toString(temp1.get("BESPEAK_STATE")));
								temp3.put("REC_ID", ObjectUtils.toString(temp1.get("REC_ID")));

								resultList.add(temp3);
							}
						}
					}
				}

				// 删除重复数据(根据REC_ID相同的)
				/*
				 * for(int i=0;i<resultList.size();i++){ Map map1 =
				 * (Map)resultList.get(i); for(int
				 * j=resultList.size()-1;j>i;j--){ Map map2 =
				 * (Map)resultList.get(j);
				 * if((ObjectUtils.toString(map1.get("REC_ID")).equals(
				 * ObjectUtils.toString(map2.get("REC_ID"))))){
				 * resultList.remove(j); } } }
				 */

				List<Object> myAppointmentList = new ArrayList<Object>();
				if (EmptyUtils.isNotEmpty(resultList)) {
					for (int i = 0; i < resultList.size(); i++) {
						Map tempPlan = (Map) resultList.get(i);
						Map viewMap = new HashMap();
						viewMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
						viewMap.put("COURSE_ID", ObjectUtils.toString(tempPlan.get("COURSE_ID")));
						viewMap.put("TYPE", ObjectUtils.toString(tempPlan.get("TYPE")));
						List viewList = examServeDao.getViewExamPlanByAcadeMy(viewMap);
						if (EmptyUtils.isNotEmpty(viewList)) {
							for (int j = 0; j < viewList.size(); j++) {
								Map viewScMap = (Map) viewList.get(j);
								if (ObjectUtils.toString(searchParams.get("KKZY"))
										.equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
										|| "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))) {
									myAppointmentList.add(viewCourseMap(tempPlan));
									break;
								}
							}
						}
					}
				}

				for (int i = 0; i < myAppointmentList.size(); i++) {
					Map map1 = (Map) myAppointmentList.get(i);
					for (int j = myAppointmentList.size() - 1; j > i; j--) {
						Map map2 = (Map) myAppointmentList.get(j);
						if ((ObjectUtils.toString(map1.get("COURSE_ID"))
								.equals(ObjectUtils.toString(map2.get("COURSE_ID"))))) {
							myAppointmentList.remove(j);
						}
					}
				}

				int pending_count = 0;
				for (int i = 0; i < myAppointmentList.size(); i++) {
					Map tMap = (Map) myAppointmentList.get(i);
					if ("0".equals(ObjectUtils.toString(tMap.get("BESPEAK_STATE")))) {
						pending_count = pending_count + 1;
					}
				}
				// 待预约考试科目
				resultMap.put("APPOINTMENT_COUNT", ObjectUtils.toString(pending_count));
			}

			searchParams.remove("APPOINTMENT_FLAG");
			searchParams.remove("BESPEAK_STATE");
			searchParams.remove("EXAM_BATCH_CODE");
			List<Object> termsList = new ArrayList<Object>();
			List courseList = new ArrayList();
			Map courseMap = new HashMap();
			if (EmptyUtils.isNotEmpty(terms)) {
				if (terms.size() > 5) {
					for (int i = 0; i < 5; i++) {
						Map temp = (Map) terms.get(i);
						Map<String, Object> termMap = new LinkedHashMap<String, Object>();
						termMap.put("KKXQ", ObjectUtils.toString(temp.get("KKXQ")));
						termMap.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
						termMap.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
						termMap.put("START_DATE", ObjectUtils.toString(temp.get("START_DATE")));
						termMap.put("END_DATE", ObjectUtils.toString(temp.get("END_DATE")));
						// 第一学期提前开放
						if (i == 0) {
							termMap.put("IS_OPEN", "0");
						} else {
							termMap.put("IS_OPEN", ObjectUtils.toString(temp.get("IS_OPEN")));
						}

						courseMap.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
						courseMap.put("KKZY", ObjectUtils.toString(searchParams.get("KKZY")));
						courseMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
						courseList = courseLearningDao.queryTeachPlan(courseMap);
						if (EmptyUtils.isEmpty(courseList)) {
							termMap.put("COURSE_FLAG", "0");
						} else {
							courseMap.put("EXAM_STATE", "3");
							if (EmptyUtils.isNotEmpty(courseLearningDao.queryTeachPlan(courseMap))) {
								termMap.put("COURSE_FLAG", "1");
							} else {
								termMap.put("COURSE_FLAG", "2");
							}
							courseMap.remove("EXAM_STATE");
						}
						
						// TODO 特殊处理，等移动端发布版本后删掉，有一批转学期学员需要学习未开放学期的课程
						if ("2628269978894b0293f9748247308984".equals(termMap.get("TERM_ID") )) {
							if (Constants.TEMP_XH.indexOf(ObjectUtils.toString(searchParams.get("XH")))>-1) {
								termMap.put("IS_OPEN", "0");
							}
						}

						termsList.add(termMap);
					}
				} else {
					for (int i = 0; i < terms.size(); i++) {
						Map temp = (Map) terms.get(i);
						Map<String, Object> termMap = new LinkedHashMap<String, Object>();
						termMap.put("KKXQ", ObjectUtils.toString(temp.get("KKXQ")));
						termMap.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
						termMap.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
						termMap.put("START_DATE", ObjectUtils.toString(temp.get("START_DATE")));
						termMap.put("END_DATE", ObjectUtils.toString(temp.get("END_DATE")));
						// 第一学期提前开放
						if (i == 0) {
							termMap.put("IS_OPEN", "0");
						} else {
							termMap.put("IS_OPEN", ObjectUtils.toString(temp.get("IS_OPEN")));
						}
						termMap.put("IS_END", ObjectUtils.toString(temp.get("IS_END")));
						if (i == 0) {
							termMap.put("PREV_END", ObjectUtils.toString("2"));
						} else {
							Map prevMap = (Map) terms.get(i - 1);
							termMap.put("PREV_END", ObjectUtils.toString(prevMap.get("IS_END")));
						}

						courseMap.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
						courseMap.put("KKZY", ObjectUtils.toString(searchParams.get("KKZY")));
						courseMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
						courseList = courseLearningDao.queryTeachPlan(courseMap);
						if (EmptyUtils.isEmpty(courseList)) {
							termMap.put("COURSE_FLAG", "0");
						} else {
							courseMap.put("EXAM_STATE", "3");
							if (EmptyUtils.isNotEmpty(courseLearningDao.queryTeachPlan(courseMap))) {
								termMap.put("COURSE_FLAG", "1");
							} else {
								termMap.put("COURSE_FLAG", "2");
							}
							courseMap.remove("EXAM_STATE");
						}

						// TODO 特殊处理，等移动端发布版本后删掉，有一批转学期学员需要学习未开放学期的课程
						if ("2628269978894b0293f9748247308984".equals(termMap.get("TERM_ID") )) {
							if (Constants.TEMP_XH.indexOf(ObjectUtils.toString(searchParams.get("XH")))>-1) {
								termMap.put("IS_OPEN", "0");
							}
						}
						
						termsList.add(termMap);
					}
				}
			}

			resultMap.put("TERMS", termsList);

			List pmbhList = courseLearningDao.getProgressOrder(searchParams);// 查询排名
			if (EmptyUtils.isNotEmpty(pmbhList)) {
				Map pmbh = (Map) pmbhList.get(0);
				Date updateDt = (Date) pmbh.get("UPDATE_DT"); // 修改时间
				Date currentLoginTime = (Date) pmbh.get("CURRENT_LOGIN_TIME"); // 本次登录单天时间
				if (currentLoginTime != null) {
					if (currentLoginTime.getTime() != updateDt.getTime()) {
						courseLearningDao.updateProgressOrder(pmbh);
					}
				} else {
					courseLearningDao.updateProgressOrder(pmbh);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getCourseResult(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			searchParams.put("EXAM_STATE", "2");
			List teachList = courseLearningDao.getCourseResult(searchParams);
			if (EmptyUtils.isNotEmpty(teachList)) {
				for (int i = 0; i < teachList.size(); i++) {
					Map teachMap = (Map) teachList.get(i);
					if ("2".equals(ObjectUtils.toString(teachMap.get("EXAM_STATE")))) {

						// 直接取学习平台LCMS_USER_DYNA表中MY_POINT的数据
						String xcxScore = ObjectUtils.toString(teachMap.get("MY_POINT"));

						String recId = ObjectUtils.toString(teachMap.get("CHOOSE_ID"));
						/*
						 * List<String> chooseIds = new ArrayList<String>();
						 * chooseIds.add(recId); List scores =
						 * courseRemoteService.getCourseScore(chooseIds);
						 * //String xcxScore =
						 * ObjectUtils.toString(teachMap.get("EXAM_SCORE"));
						 * String progress = "0";
						 * if(EmptyUtils.isNotEmpty(scores)){ for(Iterator iter
						 * = scores.iterator();iter.hasNext();){ Map score =
						 * (Map) iter.next(); String chooseId =
						 * ObjectUtils.toString(score.get("CHOOSE_ID"));
						 * if(chooseId.equals(recId)){ String point =
						 * ObjectUtils.toString(score.get("POINT")); xcxScore =
						 * StringUtils.isNotBlank(point) ? point : "0"; progress
						 * = ObjectUtils.toString(score.get("PROGRESS")); if
						 * ("N".equals(progress) ||
						 * EmptyUtils.isEmpty(progress)) { progress = "0"; }
						 * else if ("Y".equals(progress)) { progress = "100"; }
						 * } } }
						 */
						String zjxScore = ObjectUtils.toString(teachMap.get("EXAM_SCORE1"));
						// 从接口获取考试成绩(时时的最大成绩)
						// Double recentScore =
						// courseRemoteService.courseRemoteService(recId);
						String score = ObjectUtils.toString(teachMap.get("TEST_POINT"));
						if (EmptyUtils.isNotEmpty(score)) {
							zjxScore = ObjectUtils.toString(score);
						}

						/*
						 * if (EmptyUtils.isNotEmpty(recentScore) &&
						 * EmptyUtils.isNotEmpty(zjxScore) && recentScore >
						 * Double.parseDouble(zjxScore)) { zjxScore =
						 * ObjectUtils.toString(recentScore); }
						 */

						Map temp = new HashMap();
						// zjxScore = EmptyUtils.isEmpty(zjxScore)? "0" :
						// zjxScore;
						zjxScore = EmptyUtils.isNotEmpty(zjxScore) ? zjxScore : "";
						if (EmptyUtils.isNotEmpty(zjxScore)) {
							Double zjxScore_1 = Double.parseDouble(zjxScore);
							temp.put("zjxScore", this.formatNumber(zjxScore_1));
						}
						// xcxScore = EmptyUtils.isEmpty(xcxScore)? "0" :
						// xcxScore;
						xcxScore = EmptyUtils.isNotEmpty(xcxScore) ? xcxScore : "";
						if (EmptyUtils.isNotEmpty(xcxScore)) {
							Double xcxScore_1 = Double.parseDouble(xcxScore);
							temp.put("xcxScore", this.formatNumber(xcxScore_1));
						}
						temp.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
						temp.put("CHOOSE_ID", recId);
						temp.put("TEACH_PLAN_ID", ObjectUtils.toString(teachMap.get("TEACH_PLAN_ID")));
						// courseLearningDao.updateRecResultScore(temp);
						courseChangeService.changeRecResultScore(temp);

						// 更新课程班级学员学习情况
						// temp.put("SCORE", xcxScore);
						// temp.put("PROGRESS", progress);
						// courseLearningDao.updateStudySituation(temp);
					}
				}
			}

			if ("1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
				searchParams.remove("EXAM_STATE");
				List<Object> course_list = new ArrayList<Object>();
				List acadeMyList = courseLearningDao.getCourseResult(searchParams);
				if (EmptyUtils.isNotEmpty(acadeMyList)) {
					for (int i = 0; i < acadeMyList.size(); i++) {
						Map temp = (Map) acadeMyList.get(i);

						course_list.add(courseResultMap(temp));
						resultMap.put("COURSELIST", course_list);
					}
				}
			} else if ("5".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
				// TODO 先这样，业务确定下来再优化
				for (int i = 0; i < teachList.size(); i++) {
					Map tempMap = (Map) teachList.get(i);
					String user_name = ObjectUtils.toString(searchParams.get("XH"));
					String course_code = ObjectUtils.toString(tempMap.get("COURSE_CODE"));
					String dateStr = DateUtils.getTodayStr("yyyyMMdd");
					String secretKey = "VpTR0U9WXTDwsLhafQiCqOaYyqx0ObUFHV96HYOdxrDQwcd5qvG98xYSVariRH1c";
					String signature = Md5Util.getMD5(user_name + course_code + dateStr + secretKey);

					String url = "http://sichuan.ouchn.cn/userinfo/index.php";
					String data = "{\"USER_NAME\":\"" + user_name + "\",\"COURSE_CODE\":\"" + course_code
							+ "\",\"ORG_CODE\":\"\",\"SIGNATURE\":\"" + signature + "\"}";
					Map pMap = new HashMap();
					pMap.put("DATA", data);
					String resultStr = HttpClientUtils.doHttpPost(url, pMap, 6000, Constants.CHARSET);
					if (EmptyUtils.isNotEmpty(resultStr)) {
						JSONObject jsonObj = new JSONObject(resultStr);
						if (EmptyUtils.isNotEmpty(jsonObj)) {
							String code = ObjectUtils.toString(jsonObj.get("CODE"));
							if ("200".equals(code)) {
								List dataList = (List) JSON.parse(ObjectUtils.toString(jsonObj.get("RESULT_LIST")));
								if (EmptyUtils.isNotEmpty(dataList)) {
									Map dataMap = (Map) dataList.get(0);
									String learning_score = ObjectUtils.toString(dataMap.get("LEARNING_SCORE"));
									String learning_rate = ObjectUtils.toString(dataMap.get("LEARNING_RATE"));
									String learning_count = ObjectUtils.toString(dataMap.get("LEARNING_COUNT"));

									Map temp = new HashMap();
									String recId = ObjectUtils.toString(tempMap.get("CHOOSE_ID"));
									temp.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
									temp.put("CHOOSE_ID", recId);
									temp.put("TEACH_PLAN_ID", ObjectUtils.toString(tempMap.get("TEACH_PLAN_ID")));

									if (EmptyUtils.isNotEmpty(learning_score)) {
										temp.put("xcxScore", learning_score);
									}
									if (EmptyUtils.isNotEmpty(learning_rate) && ToolUtil.isNumeric(learning_rate)) {
										temp.put("progress", learning_rate);
									}
									courseChangeService.changeRecResultScore(temp);
								}
							}
						}
					}
				}

			} else {
				searchParams.remove("EXAM_STATE");
				List courseList = courseLearningDao.getCourseResult(searchParams);
				Map<String, Object> planMap = new HashMap();
				if (EmptyUtils.isNotEmpty(courseList)) {
					for (int i = 0; i < courseList.size(); i++) {
						Map temp = (Map) courseList.get(i);
						planMap.put(ObjectUtils.toString(temp.get("TERM_ID")),
								ObjectUtils.toString(temp.get("TERM_ID")));
					}
				}

				List<Object> list1 = new ArrayList<Object>();
				if (EmptyUtils.isNotEmpty(courseList)) {
					for (Object key : planMap.keySet()) {
						List<Object> course_list = new ArrayList<Object>();

						for (int i = 0; i < courseList.size(); i++) {
							Map temp = (Map) courseList.get(i);
							if (ObjectUtils.toString(key).equals(ObjectUtils.toString(temp.get("TERM_ID")))) {

								course_list.add(courseResultMap(temp));
								resultMap.put("COURSELIST", course_list);

							}
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked", "static-access" })
	@Transactional
	public Map courseLearning(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		List resultList = new ArrayList();

		try {

			searchParams.remove("EXAM_STATE");
			List teachPlans = courseLearningDao.queryTeachPlan(searchParams);
			Map tempMap = null;
			int sumScore = 0;
			if (EmptyUtils.isNotEmpty(teachPlans)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					tempMap = (Map) teachPlans.get(i);
					sumScore += Integer.parseInt(ObjectUtils.toString(tempMap.get("CREDIT")));
				}
			}
			resultMap.put("TOTALCOURSE", EmptyUtils.isNotEmpty(teachPlans) ? teachPlans.size() : 0); // 课程总数
			resultMap.put("TOTALCREDIT", sumScore); // 总学分

			searchParams.put("EXAM_STATE", "1");
			List pass = courseLearningDao.getCourseResult(searchParams);
			Map passMap = null;
			int passScore = 0;
			if (EmptyUtils.isNotEmpty(pass)) {
				for (int i = 0; i < pass.size(); i++) {
					passMap = (Map) pass.get(i);
					passScore += Integer.parseInt(ObjectUtils.toString(passMap.get("CREDIT")));
				}
			}
			resultMap.put("PASSCOURSECOUNT", EmptyUtils.isNotEmpty(teachPlans) ? pass.size() : 0); // 通过课程数
			resultMap.put("PASSCREDIT", passScore); // 已获学分

			List indexList = courseLearningDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
			String indexTerm = null;
			if (EmptyUtils.isNotEmpty(indexList)) {
				Map map = (Map) indexList.get(0);
				indexTerm = ObjectUtils.toString(map.get("GRADE_ID"));

			}

			String bh = "0";
			String jbrs = "0";
			int pm = 0;
			String zzbh = "";
			List progress = courseLearningDao.getProgressOrder(searchParams);
			if (EmptyUtils.isNotEmpty(progress) && progress.size() > 0) {
				Map progressorder = (Map) progress.get(0); // 每天排名
				searchParams.put("STUDENT_PM", "1");
				List listPM = courseLearningDao.getClassStudentCount(searchParams);
				if (EmptyUtils.isNotEmpty(listPM)) {
					Map pmMap = (Map) listPM.get(0);
					pm = Integer.parseInt(ObjectUtils.toString(pmMap.get("STD_RANK")));
				}
				searchParams.remove("STUDENT_PM");
				String searchTermId = ObjectUtils.toString(searchParams.get("TERM_ID"));
				if (searchTermId.equals(indexTerm)) {
					String zh = ObjectUtils.toString(progressorder.get("LAST_LOGIN_ORDER"));
					if (!"0".equals(zh)) {
						bh = ObjectUtils.toString(progressorder.get("ORDER_CHANGE"));

						BigInteger b = new BigInteger(bh);
						b = b.abs();
						if (Integer.parseInt(bh) > 0) {
							zzbh = "↑ " + b;
						} else if (Integer.parseInt(bh) < 0) {
							zzbh = "↓ " + b;
						} else {
							zzbh = "" + b;
						}
					} else {
						zzbh = "";
					}
				} else {
					zzbh = "";
				}

			}

			resultMap.put("RANKING", pm); // 排名
			resultMap.put("jbrs", zzbh); // 击败人数

			searchParams.remove("EXAM_STATE");
			List teachPlans1 = courseLearningDao.queryTeachPlan(searchParams);
			Map<String, Object> planMap = new HashMap();
			if (EmptyUtils.isNotEmpty(teachPlans1)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					Map temp = (Map) teachPlans.get(i);
					planMap.put(ObjectUtils.toString(temp.get("TERM_ID")), ObjectUtils.toString(temp.get("TERM_ID")));
				}
			}

			List<Object> list1 = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(teachPlans1)) {
				for (Object key : planMap.keySet()) {
					// Map<String, Object> map = new LinkedHashMap<String,
					// Object>();
					List<Object> course_list = new ArrayList<Object>();

					for (int i = 0; i < teachPlans1.size(); i++) {
						Map temp = (Map) teachPlans1.get(i);
						if (ObjectUtils.toString(key).equals(ObjectUtils.toString(temp.get("TERM_ID")))) {
							Map<String, Object> course_map = new LinkedHashMap<String, Object>();
							String teachPlanId = ObjectUtils.toString(temp.get("TEACH_PLAN_ID"));
							course_map.put("TEACH_PLAN_ID", teachPlanId);
							if (EmptyUtils.isNotEmpty(ObjectUtils.toString(temp.get("TERMCOURSE_ID")))) {
								course_map.put("TERMCOURSE_ID", ObjectUtils.toString(temp.get("TERMCOURSE_ID")));
							} else {
								course_map.put("TERMCOURSE_ID", teachPlanId);
							}
							course_map.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
							course_map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
							course_map.put("START_DATE", ObjectUtils.toString(temp.get("START_DATE")));
							course_map.put("END_DATE", ObjectUtils.toString(temp.get("END_DATE")));
							course_map.put("COURSE_START_DATE", ObjectUtils.toString(temp.get("COURSE_START_DATE")));
							course_map.put("COURSE_END_DATE", ObjectUtils.toString(temp.get("COURSE_END_DATE")));
							course_map.put("IS_COURSE_STARTDATE",
									ObjectUtils.toString(temp.get("IS_COURSE_STARTDATE")));
							course_map.put("TERMCOURSE_ID_STATUS",
									ObjectUtils.toString(temp.get("TERMCOURSE_ID_STATUS")));
							course_map.put("CLASS_ID", ObjectUtils.toString(temp.get("CLASS_ID")));
							course_map.put("KCFM", ObjectUtils.toString(temp.get("KCFM")));
							course_map.put("COUNTCOURSE", ObjectUtils.toString(temp.get("COUNTCOURSE")));
							course_map.put("WSJXZK", ObjectUtils.toString(temp.get("WSJXZK")));
							course_map.put("COURSE_ID", ObjectUtils.toString(temp.get("COURSE_ID")));
							course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
							course_map.put("COURSE_CODE", ObjectUtils.toString(temp.get("COURSE_CODE")));
							course_map.put("SOURCE_COURSE_ID", ObjectUtils.toString(temp.get("SOURCE_COURSE_ID")));
							course_map.put("SOURCE_KCH", ObjectUtils.toString(temp.get("SOURCE_KCH")));
							course_map.put("SOURCE_KCMC", ObjectUtils.toString(temp.get("SOURCE_KCMC")));
							course_map.put("COURSE_STYLE", ObjectUtils.toString(temp.get("COURSE_STYLE")));
							course_map.put("CREDIT", ObjectUtils.toString(temp.get("CREDIT")));
							course_map.put("COUNSELOR", ObjectUtils.toString(temp.get("COUNSELOR")));
							course_map.put("IS_ONLINE", ObjectUtils.toString(temp.get("IS_ONLINE")));
							course_map.put("SCHEDULE", ObjectUtils.toString(temp.get("SCHEDULE")));
							course_map.put("EXAM_SCORE", ObjectUtils.toString(temp.get("EXAM_SCORE")));
							course_map.put("EXAM_SCORE1", ObjectUtils.toString(temp.get("EXAM_SCORE1")));
							course_map.put("LEARN_STATUS", ObjectUtils.toString(temp.get("LEARN_STATUS")));

							String examState = ObjectUtils.toString(temp.get("EXAM_STATE"));
							String examScore2 = ObjectUtils.toString(temp.get("EXAM_SCORE2"));

							if ("0".equals(examState) || "1".equals(examState)) {
								course_map.put("EXAM_SCORE2", examScore2);
								course_map.put("EXAM_STATE", examState);
							} else if ("2".equals(examState)) {
								course_map.put("EXAM_SCORE2", "学习中");
								course_map.put("EXAM_STATE", examState);
							} else if ("3".equals(examState)) {
								course_map.put("EXAM_SCORE2", "登记中");
								course_map.put("EXAM_STATE", examState);
							} else {
								course_map.put("EXAM_SCORE2", examScore2);
								course_map.put("EXAM_STATE", examState);
							}

							course_map.put("KCXXBZ", ObjectUtils.toString(temp.get("KCXXBZ")));
							course_map.put("KCKSBZ", ObjectUtils.toString(temp.get("KCKSBZ")));
							course_map.put("CHOOSE_ID", ObjectUtils.toString(temp.get("CHOOSE_ID")));
							course_map.put("REC_ID", ObjectUtils.toString(temp.get("REC_ID")));
							course_map.put("action", AppConfig.getProperty("oclass.url"));

							String str = new EncryptUtils().encrypt(ObjectUtils.toString(temp.get("CHOOSE_ID")) + ","
									+ ObjectUtils.toString(searchParams.get("STUDENT_ID")));
							course_map.put("USER_INFO", str);

							course_list.add(course_map);
							resultMap.put("PLANLIST", course_list);

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	@Override
	public Map getStudyRankByTerm(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try {
			List terms = courseLearningDao.getTermListByLoginStudent(searchParams); // 学期列表
			List indexList = courseLearningDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
			if (EmptyUtils.isNotEmpty(indexList)) {
				Map map = (Map) indexList.get(0);
				int index = Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")));
				if (index <= 5) {
					resultMap.put("TERM_ID", ObjectUtils.toString(map.get("GRADE_ID")));
				} else {
					if (EmptyUtils.isNotEmpty(terms)) {
						Map termMap = (Map) terms.get(terms.size() - 1);
						resultMap.put("TERM_ID", termMap.get("TERM_ID"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 个人中心-首页-学习排名top5
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getStudyRank(Map<String, Object> searchParams) {
		List<Map<String, Object>> resultList = courseLearningDao.getStudyRank(searchParams);
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	public Map getOrderPM(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try {
			List list = courseLearningDao.getOrderPM(searchParams);
			if (EmptyUtils.isNotEmpty(list)) {
				resultMap = (Map) list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	public List getProgressOrder(Map<String, Object> searchParams) {
		return courseLearningDao.getProgressOrder(searchParams);
	}

	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	private String formatNumber(double number) {
		return DECIMAL_FORMAT.format(number);
	}

	/**
	 * 确认重修
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map learningRepair(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		int recResult = 0;
		int repairResult = 0;

		try {
			List recList = courseLearningDao.getRecResultDetail(searchParams);
			log.info("确认重修参数：{}", searchParams);
			Map recMap = new HashMap();
			recMap.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
			recMap.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
			recMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
			recMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))); // 20171127
																										// 特殊处理麦当劳项目2016秋学员期末考试预约工作，全部无需缴费，预约期过了就可以去掉代码，没办法老是不按流程来
			recResult = courseLearningDao.updateRecordState(recMap);

			if (recResult == 1) {
				gjtRecResultService.updateRebuildState(ObjectUtils.toString(searchParams.get("CHOOSE_ID"))); // 重修状态

				/*
				 * 导成绩已经插入过一遍历史成绩了，这里不需要再记录！ Map tMap = (Map) recList.get(0);
				 * 
				 * Map repairMap = new HashMap(); repairMap.put("REPAIR_ID",
				 * SequenceUUID.getSequence()); repairMap.put("TEACH_PLAN_ID",
				 * ObjectUtils.toString(tMap.get("TEACH_PLAN_ID")));
				 * repairMap.put("XCX_SCORE",
				 * ObjectUtils.toString(tMap.get("XCX_SCORE")));
				 * repairMap.put("ZJX_SCORE",
				 * ObjectUtils.toString(tMap.get("ZJX_SCORE")));
				 * repairMap.put("ZCJ_SCORE",
				 * ObjectUtils.toString(tMap.get("ZCJ_SCORE")));
				 * repairMap.put("STATUS", "1"); repairMap.put("STUDENT_ID",
				 * ObjectUtils.toString(searchParams.get("STUDENT_ID")));
				 * repairMap.put("XH",
				 * ObjectUtils.toString(searchParams.get("XH")));
				 * repairMap.put("COURSE_CODE",
				 * ObjectUtils.toString(tMap.get("COURSE_CODE")));
				 * repairMap.put("CREATED_BY",
				 * ObjectUtils.toString(searchParams.get("CREATED_BY"))); String
				 * student_name =
				 * URLDecoder.decode(ObjectUtils.toString(searchParams.get(
				 * "STUDENT_NAME")), "UTF-8"); repairMap.put("STUDENT_NAME",
				 * student_name); repairMap.put("SUBJECT_NAME",
				 * ObjectUtils.toString(tMap.get("SUBJECT_NAME")));
				 * 
				 * repairResult =
				 * courseLearningDao.insertLearnRepair(repairMap);
				 */
			}

			resultMap.put("RECRESULT", recResult); // 1-成功 0-失败
			resultMap.put("REPAIRRESULT", repairResult);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getCourseAndTermData(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try {
			List terms = courseLearningDao.getTermListByLoginStudent(searchParams); // 学期列表
			List indexList = courseLearningDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
			if (EmptyUtils.isNotEmpty(indexList)) {
				Map map = (Map) indexList.get(0);
				int index = Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")));
				if (index <= 5) {
					if (EmptyUtils.isNotEmpty(terms)) {
						String term_id = ObjectUtils.toString(map.get("GRADE_ID"));
						for (int i = 0; i < terms.size(); i++) {
							Map tMap = (Map) terms.get(i);
							if (ObjectUtils.toString(tMap.get("TERM_ID")).equals(term_id)) {
								resultMap.put("TERMINDEX", ObjectUtils.toString(tMap.get("KKXQ")));
								break;
							} else {
								if (terms.size() < Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")))) {
									resultMap.put("TERMINDEX", terms.size());
								} else {
									resultMap.put("TERMINDEX", ObjectUtils.toString(map.get("GRADE_INDEX")));
								}
							}
						}
					}
					// resultMap.put("TERMINDEX",
					// ObjectUtils.toString(map.get("GRADE_INDEX")));
				} else {
					if (EmptyUtils.isNotEmpty(terms)) {
						resultMap.put("TERMINDEX", terms.size());
					} else {
						// 当前学期列表为空时，TERMINDEX指为1
						resultMap.put("TERMINDEX", "1");
					}
				}
			} else {
				// 查不到当前学期时，默认为1
				resultMap.put("TERMINDEX", "1");
			}
			List teachPlans = courseLearningDao.queryTeachPlan(searchParams); // 全部课程
			if (EmptyUtils.isNotEmpty(teachPlans)) {
				resultMap.put("ALL_COURSE", teachPlans.size());
			} else {
				resultMap.put("ALL_COURSE", "0");
			}

			searchParams.put("EXAM_STATE", "1");
			List teachPlans_1 = courseLearningDao.queryTeachPlan(searchParams); // 已通过课程
			if (EmptyUtils.isNotEmpty(teachPlans_1)) {
				resultMap.put("COMPLETE_COURSE", teachPlans_1.size());
			} else {
				resultMap.put("COMPLETE_COURSE", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 统计在学课程人数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getPersonalCountByCourse(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();

		try {
			List list = courseRemoteService.getOnlineLearningCount(searchParams);
			if (EmptyUtils.isNotEmpty(list)) {
				Map map = (Map) list.get(0);
				if ("0".equals(ObjectUtils.toString(map.get("TEACH_LIST")))) {
					resultMap.put("TEACH_LIST", new ArrayList());
				} else {
					resultMap.put("TEACH_LIST", map.get("TEACH_LIST"));
				}
			} else {
				resultMap.put("TEACH_LIST", new ArrayList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 课程学习(院校模式)
	 * 
	 * @param searchParams
	 * @return
	 */
	@Transactional
	public Map acadeMyLearnCourse(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			List pmbhList = courseLearningDao.getProgressOrder(searchParams);// 查询排名
			if (EmptyUtils.isNotEmpty(pmbhList)) {
				Map pmbh = (Map) pmbhList.get(0);
				Date updateDt = (Date) pmbh.get("UPDATE_DT"); // 修改时间
				Date currentLoginTime = (Date) pmbh.get("CURRENT_LOGIN_TIME"); // 本次登录单天时间
				if (currentLoginTime != null) {
					if (currentLoginTime.getTime() != updateDt.getTime()) {
						courseLearningDao.updateProgressOrder(pmbh);
					}
				} else {
					courseLearningDao.updateProgressOrder(pmbh);
				}

			}

			List teachPlans = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("TOTALCOURSE", EmptyUtils.isNotEmpty(teachPlans) ? teachPlans.size() : 0); // 课程总数

			searchParams.put("EXAM_STATE", "0");
			List nopass = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("NO_PASSSCOUNT", EmptyUtils.isNotEmpty(nopass) ? nopass.size() : 0); // 未通过课程数

			searchParams.put("EXAM_STATE", "1");
			List pass = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("PASSCOURSECOUNT", EmptyUtils.isNotEmpty(pass) ? pass.size() : 0); // 通过课程数

			searchParams.put("EXAM_STATE", "2");
			List learning = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("LEARNINGCOUNT", EmptyUtils.isNotEmpty(learning) ? learning.size() : 0); // 学习中课程数

			String bh = "0";
			int pm = 0;
			String zzbh = "";
			List progress = courseLearningDao.getProgressOrder(searchParams);
			if (EmptyUtils.isNotEmpty(progress) && progress.size() > 0) {
				Map progressorder = (Map) progress.get(0);
				searchParams.put("STUDENT_PM", "1");
				List listPM = courseLearningDao.getClassStudentCount(searchParams); // 班级排名
				if (EmptyUtils.isNotEmpty(listPM)) {
					Map pmMap = (Map) listPM.get(0);
					pm = Integer.parseInt(ObjectUtils.toString(pmMap.get("STD_RANK")));
				}
				searchParams.remove("STUDENT_PM");

				String zh = ObjectUtils.toString(progressorder.get("LAST_LOGIN_ORDER"));
				if (!"0".equals(zh)) {
					bh = ObjectUtils.toString(progressorder.get("ORDER_CHANGE"));

					BigInteger b = new BigInteger(bh);
					b = b.abs();
					if (Integer.parseInt(bh) > 0) {
						zzbh = "↑ " + b;
					} else if (Integer.parseInt(bh) < 0) {
						zzbh = "↓ " + b;
					} else {
						zzbh = "" + b;
					}
				} else {
					zzbh = "";
				}
			}
			resultMap.put("RANKING", pm); // 排名
			resultMap.put("jbrs", zzbh); // 击败人数

			// ===========待预约考试统计开始==============//
			searchParams.put("CURRENT_FLAG", "2");
			List current_plan = examServeDao.getExamBatchData(searchParams); // 查询当前考试计划
			String exam_batch_code = null;
			String end_date = null;
			if (EmptyUtils.isNotEmpty(current_plan)) {
				Map currentMap = (Map) current_plan.get(0);
				exam_batch_code = ObjectUtils.toString(currentMap.get("EXAM_BATCH_CODE"));
				end_date = ObjectUtils.toString(currentMap.get("END_DATE"));
			}

			searchParams.remove("CURRENT_FLAG");

			List<Object> resultList = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(exam_batch_code)) {
				searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
				searchParams.put("BESPEAK_STATE", "0");
				searchParams.put("END_DATE", end_date);
				List recResultList = examServeDao.getChooseCourseByStudent(searchParams);
				if (EmptyUtils.isNotEmpty(recResultList)) {
					for (int i = 0; i < recResultList.size(); i++) {
						Map temp1 = (Map) recResultList.get(i);
						Map courseMap = new HashMap();
						courseMap.put("APPOINTMENT_FLAG", "1"); // 待预约列表
						courseMap.put("XX_ID", searchParams.get("XX_ID"));
						courseMap.put("EXAM_BATCH_CODE", searchParams.get("EXAM_BATCH_CODE"));
						courseMap.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
						courseMap.put("TERM_ID", ObjectUtils.toString(temp1.get("TERM_ID")));
						if ("1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
							courseMap.put("SCHOOL_MODEL", ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")));
							courseMap.put("TYPE", "7");
						}

						List courseListInfo = examServeDao.getExamPlanAndCourseInfo(courseMap);

						if (EmptyUtils.isNotEmpty(courseListInfo)) {
							for (int j = 0; j < courseListInfo.size(); j++) {
								Map temp2 = (Map) courseListInfo.get(j);

								Map temp3 = new LinkedHashMap();

								temp3.put("TERM_CODE", ObjectUtils.toString(temp1.get("TERM_CODE")));
								temp3.put("TERM_ID", ObjectUtils.toString(temp1.get("TERM_ID")));
								temp3.put("TERM_NAME", ObjectUtils.toString(temp1.get("TERM_NAME")));
								temp3.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
								temp3.put("COURSE_NAME", ObjectUtils.toString(temp1.get("COURSE_NAME")));
								temp3.put("TYPE", ObjectUtils.toString(temp2.get("TYPE")));
								temp3.put("KCH", ObjectUtils.toString(temp1.get("KCH")));
								temp3.put("COURSE_COST", ObjectUtils.toString(temp1.get("COURSE_COST")));
								temp3.put("MAKEUP", ObjectUtils.toString(temp1.get("MAKEUP")));
								temp3.put("PAY_STATE", ObjectUtils.toString(temp1.get("PAY_STATE")));
								temp3.put("BESPEAK_STATE", ObjectUtils.toString(temp1.get("BESPEAK_STATE")));
								temp3.put("REC_ID", ObjectUtils.toString(temp1.get("REC_ID")));

								resultList.add(temp3);
							}
						}
					}
				}

				// 删除重复数据(根据REC_ID相同的)
				for (int i = 0; i < resultList.size(); i++) {
					Map map1 = (Map) resultList.get(i);
					for (int j = resultList.size() - 1; j > i; j--) {
						Map map2 = (Map) resultList.get(j);
						if ((ObjectUtils.toString(map1.get("REC_ID"))
								.equals(ObjectUtils.toString(map2.get("REC_ID"))))) {
							resultList.remove(j);
						}
					}
				}

				List<Object> myAppointmentList = new ArrayList<Object>();
				if (EmptyUtils.isNotEmpty(resultList)) {
					for (int i = 0; i < resultList.size(); i++) {
						Map tempPlan = (Map) resultList.get(i);
						Map viewMap = new HashMap();
						viewMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
						viewMap.put("COURSE_ID", ObjectUtils.toString(tempPlan.get("COURSE_ID")));
						viewMap.put("TYPE", ObjectUtils.toString(tempPlan.get("TYPE")));
						List viewList = examServeDao.getViewExamPlanByAcadeMy(viewMap);
						if (EmptyUtils.isNotEmpty(viewList)) {
							for (int j = 0; j < viewList.size(); j++) {
								Map viewScMap = (Map) viewList.get(j);
								if (ObjectUtils.toString(searchParams.get("KKZY"))
										.equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
										|| "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))) {
									myAppointmentList.add(viewCourseMap(tempPlan));
									break;
								}
							}
						}
					}
				}

				int pending_count = 0;
				for (int i = 0; i < myAppointmentList.size(); i++) {
					Map tMap = (Map) myAppointmentList.get(i);
					if ("0".equals(ObjectUtils.toString(tMap.get("BESPEAK_STATE")))) {
						pending_count = pending_count + 1;
					}
				}
				// 待预约考试科目
				resultMap.put("APPOINTMENT_COUNT", ObjectUtils.toString(pending_count));
			}

			// =======查询课程学习列表开始======//
			searchParams.remove("EXAM_STATE");
			List<Object> course_list = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(teachPlans)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					Map tempPlan = (Map) teachPlans.get(i);
					Map<String, Object> course_map = new LinkedHashMap<String, Object>();

					course_map.put("TEACH_PLAN_ID", ObjectUtils.toString(tempPlan.get("TEACH_PLAN_ID")));
					if (EmptyUtils.isNotEmpty(ObjectUtils.toString(tempPlan.get("TERMCOURSE_ID")))) {
						course_map.put("TERMCOURSE_ID", ObjectUtils.toString(tempPlan.get("TERMCOURSE_ID")));
					} else {
						course_map.put("TERMCOURSE_ID", ObjectUtils.toString(tempPlan.get("TEACH_PLAN_ID")));
					}
					course_map.put("TERM_ID", ObjectUtils.toString(tempPlan.get("TERM_ID")));
					course_map.put("TERM_NAME", ObjectUtils.toString(tempPlan.get("TERM_NAME")));
					course_map.put("START_DATE", ObjectUtils.toString(tempPlan.get("START_DATE")));
					course_map.put("END_DATE", ObjectUtils.toString(tempPlan.get("END_DATE")));
					course_map.put("COURSE_START_DATE", ObjectUtils.toString(tempPlan.get("COURSE_START_DATE")));
					course_map.put("COURSE_END_DATE", ObjectUtils.toString(tempPlan.get("COURSE_END_DATE")));
					course_map.put("IS_COURSE_STARTDATE", ObjectUtils.toString(tempPlan.get("IS_COURSE_STARTDATE")));
					course_map.put("TERMCOURSE_ID_STATUS", ObjectUtils.toString(tempPlan.get("TERMCOURSE_ID_STATUS")));
					course_map.put("CLASS_ID", ObjectUtils.toString(tempPlan.get("CLASS_ID")));
					course_map.put("KCFM", ObjectUtils.toString(tempPlan.get("KCFM")));
					course_map.put("COUNTCOURSE", ObjectUtils.toString(tempPlan.get("COUNTCOURSE")));
					course_map.put("COURSE_ID", ObjectUtils.toString(tempPlan.get("COURSE_ID")));
					course_map.put("WSJXZK", ObjectUtils.toString(tempPlan.get("WSJXZK")));
					course_map.put("COURSE_NAME", ObjectUtils.toString(tempPlan.get("COURSE_NAME")));
					course_map.put("COURSE_CODE", ObjectUtils.toString(tempPlan.get("COURSE_CODE")));
					course_map.put("SOURCE_COURSE_ID", ObjectUtils.toString(tempPlan.get("SOURCE_COURSE_ID")));
					course_map.put("SOURCE_KCH", ObjectUtils.toString(tempPlan.get("SOURCE_KCH")));
					course_map.put("SOURCE_KCMC", ObjectUtils.toString(tempPlan.get("SOURCE_KCMC")));
					course_map.put("COURSE_STYLE", ObjectUtils.toString(tempPlan.get("COURSE_STYLE"))); // 课程类型
																										// 必修选修补修
					course_map.put("COUNSELOR", ObjectUtils.toString(tempPlan.get("COUNSELOR")));
					course_map.put("IS_ONLINE", ObjectUtils.toString(tempPlan.get("IS_ONLINE")));
					course_map.put("SCHEDULE", ObjectUtils.toString(tempPlan.get("SCHEDULE")));
					course_map.put("EXAM_SCORE", ObjectUtils.toString(tempPlan.get("EXAM_SCORE")));
					course_map.put("EXAM_SCORE1", ObjectUtils.toString(tempPlan.get("EXAM_SCORE1")));
					course_map.put("LEARN_STATUS", ObjectUtils.toString(tempPlan.get("LEARN_STATUS")));

					if ("0".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))
							|| "1".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))) {
						course_map.put("EXAM_SCORE2", ObjectUtils.toString(tempPlan.get("EXAM_SCORE2")));
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					} else if ("2".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))) {
						course_map.put("EXAM_SCORE2", "学习中");
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					} else if ("3".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))) {
						course_map.put("EXAM_SCORE2", "登记中");
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					} else {
						course_map.put("EXAM_SCORE2", ObjectUtils.toString(tempPlan.get("EXAM_SCORE2")));
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					}

					course_map.put("KCXXBZ", ObjectUtils.toString(tempPlan.get("KCXXBZ")));
					course_map.put("KCKSBZ", ObjectUtils.toString(tempPlan.get("KCKSBZ")));
					course_map.put("CHOOSE_ID", ObjectUtils.toString(tempPlan.get("CHOOSE_ID")));
					course_map.put("REC_ID", ObjectUtils.toString(tempPlan.get("REC_ID")));
					course_map.put("action", AppConfig.getProperty("oclass.url"));

					String str = new EncryptUtils().encrypt(ObjectUtils.toString(tempPlan.get("CHOOSE_ID")) + ","
							+ ObjectUtils.toString(searchParams.get("STUDENT_ID")));
					course_map.put("USER_INFO", str);

					course_list.add(course_map);
					resultMap.put("PLANLIST", course_list);
				}
			}
			// =======查询课程学习列表结束======//

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 匹配开考科目与课程(待预约考试)
	 * 
	 * @param viewMap
	 * @return
	 */
	public static Map<String, Object> viewCourseMap(Map viewMap) {
		Map resutlMap = new LinkedHashMap();
		try {
			resutlMap.put("TERM_ID", ObjectUtils.toString(viewMap.get("TERM_ID")));
			resutlMap.put("TERM_NAME", ObjectUtils.toString(viewMap.get("TERM_NAME")));
			resutlMap.put("COURSE_ID", ObjectUtils.toString(viewMap.get("COURSE_ID")));
			resutlMap.put("COURSE_NAME", ObjectUtils.toString(viewMap.get("COURSE_NAME")));
			resutlMap.put("KCH", ObjectUtils.toString(viewMap.get("KCH")));
			resutlMap.put("COURSE_CODE", ObjectUtils.toString(viewMap.get("COURSE_CODE")));
			resutlMap.put("COURSE_COST", ObjectUtils.toString(viewMap.get("COURSE_COST")));
			resutlMap.put("EXAM_TYPE", ObjectUtils.toString(viewMap.get("EXAM_TYPE")));
			resutlMap.put("EXAM_STYLE", ObjectUtils.toString(viewMap.get("EXAM_STYLE")));
			resutlMap.put("KSFS_FLAG", ObjectUtils.toString(viewMap.get("KSFS_FLAG")));
			resutlMap.put("EXAM_PLAN_ID", ObjectUtils.toString(viewMap.get("EXAM_PLAN_ID")));
			resutlMap.put("EXAM_STIME", ObjectUtils.toString(viewMap.get("EXAM_STIME")));
			resutlMap.put("EXAM_ETIME", ObjectUtils.toString(viewMap.get("EXAM_ETIME")));
			resutlMap.put("BOOK_STARTTIME", ObjectUtils.toString(viewMap.get("BOOK_STARTTIME")));
			resutlMap.put("BOOK_ENDTIME", ObjectUtils.toString(viewMap.get("BOOK_ENDTIME")));
			resutlMap.put("MAKEUP", ObjectUtils.toString(viewMap.get("MAKEUP")));
			resutlMap.put("PAY_STATE", ObjectUtils.toString(viewMap.get("PAY_STATE")));
			resutlMap.put("BESPEAK_STATE", ObjectUtils.toString(viewMap.get("BESPEAK_STATE")));
			resutlMap.put("TEACH_PLAN_ID", ObjectUtils.toString(viewMap.get("TEACH_PLAN_ID")));
			resutlMap.put("url", ObjectUtils.toString(viewMap.get("payHost")));
			resutlMap.put("productId", ObjectUtils.toString(viewMap.get("productId00815")).trim());
			resutlMap.put("username", ObjectUtils.toString(viewMap.get("username")).trim());
			resutlMap.put("referrer", ObjectUtils.toString(viewMap.get("payReferrer")).trim()); // 缴费固定的referrer值为041
			resutlMap.put("REMARK", ObjectUtils.toString(viewMap.get("REMARK"))); // 值不同,保证在招生平台缴费时，传的值每次不一样
			resutlMap.put("LEARNCENTER_CODE", ObjectUtils.toString(viewMap.get("LEARNCENTER_CODE")).trim());
			resutlMap.put("REC_ID", ObjectUtils.toString(viewMap.get("REC_ID")));
			resutlMap.put("URL_NEW", ObjectUtils.toString(viewMap.get("URL_NEW")));
			resutlMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(viewMap.get("EXAM_BATCH_CODE")));
			resutlMap.put("EXAM_PLAN_ID", ObjectUtils.toString(viewMap.get("EXAM_PLAN_ID")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resutlMap;
	}

	public static Map<String, Object> courseResultMap(Map temp) {
		Map<String, Object> course_map = new LinkedHashMap<String, Object>();
		try {
			String teachPlanId = ObjectUtils.toString(temp.get("TEACH_PLAN_ID"));
			course_map.put("TEACH_PLAN_ID", teachPlanId);
			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(temp.get("TERMCOURSE_ID")))) {
				course_map.put("TERMCOURSE_ID", ObjectUtils.toString(temp.get("TERMCOURSE_ID")));
			} else {
				course_map.put("TERMCOURSE_ID", teachPlanId);
			}
			course_map.put("SCHEDULE", ObjectUtils.toString(temp.get("SCHEDULE")));
			course_map.put("EXAM_SCORE", ObjectUtils.toString(temp.get("EXAM_SCORE")));
			course_map.put("EXAM_SCORE1", ObjectUtils.toString(temp.get("EXAM_SCORE1")));
			course_map.put("LEARN_STATUS", ObjectUtils.toString(temp.get("LEARN_STATUS")));
			course_map.put("COUNTCOURSE", ObjectUtils.toString(temp.get("COUNTCOURSE")));
			course_map.put("COURSE_ID", ObjectUtils.toString(temp.get("COURSE_ID")));
			course_map.put("WSJXZK", ObjectUtils.toString(temp.get("WSJXZK")));
			course_map.put("COURSE_CODE", ObjectUtils.toString(temp.get("COURSE_CODE")));
			course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
			course_map.put("SOURCE_COURSE_ID", ObjectUtils.toString(temp.get("SOURCE_COURSE_ID")));
			course_map.put("SOURCE_KCH", ObjectUtils.toString(temp.get("SOURCE_KCH")));
			course_map.put("SOURCE_KCMC", ObjectUtils.toString(temp.get("SOURCE_KCMC")));
			course_map.put("START_DATE", ObjectUtils.toString(temp.get("START_DATE")));
			course_map.put("END_DATE", ObjectUtils.toString(temp.get("END_DATE")));
			course_map.put("COURSE_START_DATE", ObjectUtils.toString(temp.get("COURSE_START_DATE")));
			course_map.put("COURSE_END_DATE", ObjectUtils.toString(temp.get("COURSE_END_DATE")));
			course_map.put("IS_COURSE_STARTDATE", ObjectUtils.toString(temp.get("IS_COURSE_STARTDATE")));
			course_map.put("TERMCOURSE_ID_STATUS", ObjectUtils.toString(temp.get("TERMCOURSE_ID_STATUS")));

			String examState = ObjectUtils.toString(temp.get("EXAM_STATE"));
			String examScore2 = ObjectUtils.toString(temp.get("EXAM_SCORE2"));
			if ("0".equals(examState) || "1".equals(examState)) {
				course_map.put("EXAM_SCORE2", examScore2);
				course_map.put("EXAM_STATE", examState);
			} else if ("2".equals(examState)) {
				course_map.put("EXAM_SCORE2", "学习中");
				course_map.put("EXAM_STATE", examState);
			} else if ("3".equals(examState)) {
				course_map.put("EXAM_SCORE2", "登记中");
				course_map.put("EXAM_STATE", examState);
			} else {
				course_map.put("EXAM_SCORE2", examScore2);
				course_map.put("EXAM_STATE", examState);
			}

			course_map.put("KCXXBZ", ObjectUtils.toString(temp.get("KCXXBZ")));
			course_map.put("KCKSBZ", ObjectUtils.toString(temp.get("KCKSBZ")));
			course_map.put("WSJXZK", ObjectUtils.toString(temp.get("WSJXZK")));
			course_map.put("COURSE_CODE", ObjectUtils.toString(temp.get("COURSE_CODE")));
			course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
			course_map.put("CHOOSE_ID", ObjectUtils.toString(temp.get("CHOOSE_ID")));
			course_map.put("REC_ID", ObjectUtils.toString(temp.get("REC_ID")));
			course_map.put("action", AppConfig.getProperty("oclass.url"));

			String str = new EncryptUtils().encrypt(
					ObjectUtils.toString(temp.get("CHOOSE_ID")) + "," + ObjectUtils.toString(temp.get("STUDENT_ID")));
			course_map.put("USER_INFO", str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return course_map;
	}

	/**
	 * 课程学习(无考试模式)
	 * 
	 * @param searchParams
	 * @return
	 */
	public Map acadeMyLearningByNotExam(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			List<String> chooseIds = new ArrayList<String>();
			List list = courseLearningDao.getCourseLearningByNoExam(searchParams);

			int ytg_count = 0;
			int wxi_count = 0;
			int xxz_count = 0;
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map tempMap = (Map) list.get(i);
					if ("4".equals(ObjectUtils.toString(tempMap.get("STATE")))) {
						ytg_count = ytg_count + 1;
					}
					if ("0".equals(ObjectUtils.toString(tempMap.get("STATE")))
							|| "".equals(ObjectUtils.toString(tempMap.get("STATE")))
							|| "--".equals(ObjectUtils.toString(tempMap.get("STATE")))) {
						wxi_count = wxi_count + 1;
					}
					if ("1".equals(ObjectUtils.toString(tempMap.get("STATE")))
							|| "2".equals(ObjectUtils.toString(tempMap.get("STATE")))
							|| "3".equals(ObjectUtils.toString(tempMap.get("STATE")))) {
						xxz_count = xxz_count + 1;
					}
				}
			}
			resultMap.put("YTG_COUNT", ytg_count); // 已通过
			resultMap.put("WXI_COUNT", wxi_count); // 未学习
			resultMap.put("XXZ_COUNT", xxz_count); // 学习中
			resultMap.put("COURSE_COUNT", EmptyUtils.isNotEmpty(list) ? list.size() : 0); // 课程总数

			String bh = "0";
			int pm = 0;
			String zzbh = "";
			List progress = courseLearningDao.getProgressOrder(searchParams);
			if (EmptyUtils.isNotEmpty(progress) && progress.size() > 0) {
				Map progressorder = (Map) progress.get(0);
				searchParams.put("STUDENT_PM", "1");
				List listPM = courseLearningDao.getClassStudentCount(searchParams); // 班级排名
				if (EmptyUtils.isNotEmpty(listPM)) {
					Map pmMap = (Map) listPM.get(0);
					pm = Integer.parseInt(ObjectUtils.toString(pmMap.get("STD_RANK")));
				}
				searchParams.remove("STUDENT_PM");

				String zh = ObjectUtils.toString(progressorder.get("LAST_LOGIN_ORDER"));
				if (!"0".equals(zh)) {
					bh = ObjectUtils.toString(progressorder.get("ORDER_CHANGE"));

					BigInteger b = new BigInteger(bh);
					b = b.abs();
					if (Integer.parseInt(bh) > 0) {
						zzbh = "↑ " + b;
					} else if (Integer.parseInt(bh) < 0) {
						zzbh = "↓ " + b;
					} else {
						zzbh = "" + b;
					}
				} else {
					zzbh = "";
				}
			}
			resultMap.put("RANKING", pm); // 排名
			resultMap.put("jbrs", zzbh); // 击败人数

			List<Object> courseList = new ArrayList<Object>();
			for (int i = 0; i < list.size(); i++) {
				Map tMap = (Map) list.get(i);
				Map<String, Object> courseMap = new LinkedHashMap<String, Object>();

				courseMap.put("KKXQ", ObjectUtils.toString(tMap.get("KKXQ")));
				courseMap.put("TERM_ID", ObjectUtils.toString(tMap.get("TERM_ID")));
				courseMap.put("TERM_NAME", ObjectUtils.toString(tMap.get("TERM_NAME")));
				courseMap.put("START_DATE", ObjectUtils.toString(tMap.get("START_DATE")));
				courseMap.put("END_DATE", ObjectUtils.toString(tMap.get("END_DATE")));
				courseMap.put("COURSE_START_DATE", ObjectUtils.toString(tMap.get("COURSE_START_DATE")));
				courseMap.put("COURSE_END_DATE", ObjectUtils.toString(tMap.get("COURSE_END_DATE")));
				courseMap.put("IS_COURSE_STARTDATE", ObjectUtils.toString(tMap.get("IS_COURSE_STARTDATE")));
				courseMap.put("TERMCOURSE_ID_STATUS", ObjectUtils.toString(tMap.get("TERMCOURSE_ID_STATUS")));
				courseMap.put("IS_OPEN", ObjectUtils.toString(tMap.get("IS_OPEN")));
				courseMap.put("TEACH_PLAN_ID", ObjectUtils.toString(tMap.get("TEACH_PLAN_ID")));
				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(tMap.get("TERMCOURSE_ID")))) {
					courseMap.put("TERMCOURSE_ID", ObjectUtils.toString(tMap.get("TERMCOURSE_ID")));
				} else {
					courseMap.put("TERMCOURSE_ID", ObjectUtils.toString(tMap.get("TEACH_PLAN_ID")));
				}
				courseMap.put("STUDENT_ID", ObjectUtils.toString(tMap.get("STUDENT_ID")));
				courseMap.put("COURSE_ID", ObjectUtils.toString(tMap.get("COURSE_ID")));
				courseMap.put("WSJXZK", ObjectUtils.toString(tMap.get("WSJXZK")));
				courseMap.put("COURSE_CODE", ObjectUtils.toString(tMap.get("COURSE_CODE")));
				courseMap.put("COURSE_NAME", ObjectUtils.toString(tMap.get("COURSE_NAME")));
				courseMap.put("COURSE_CATEGORY", ObjectUtils.toString(tMap.get("COURSE_CATEGORY")));
				courseMap.put("SOURCE_COURSE_ID", ObjectUtils.toString(tMap.get("SOURCE_COURSE_ID")));
				courseMap.put("SOURCE_KCH", ObjectUtils.toString(tMap.get("SOURCE_KCH")));
				courseMap.put("SOURCE_KCMC", ObjectUtils.toString(tMap.get("SOURCE_KCMC")));
				courseMap.put("KCFM", ObjectUtils.toString(tMap.get("KCFM")));
				courseMap.put("COUNTCOURSE", ObjectUtils.toString(tMap.get("COUNTCOURSE")));
				courseMap.put("COURSE_STYLE", ObjectUtils.toString(tMap.get("COURSE_STYLE")));
				courseMap.put("COURSE_TYPE", ObjectUtils.toString(tMap.get("COURSE_TYPE")));
				courseMap.put("CREDIT", ObjectUtils.toString(tMap.get("CREDIT")));
				courseMap.put("COUNSELOR", ObjectUtils.toString(tMap.get("COUNSELOR")));
				courseMap.put("IS_ONLINE", ObjectUtils.toString(tMap.get("IS_ONLINE")));
				courseMap.put("CLASS_ID", ObjectUtils.toString(tMap.get("CLASS_ID")));
				courseMap.put("KCXXBZ", ObjectUtils.toString(tMap.get("KCXXBZ")));
				courseMap.put("KCKSBZ", ObjectUtils.toString(tMap.get("KCKSBZ")));
				courseMap.put("REC_ID", ObjectUtils.toString(tMap.get("REC_ID")));
				courseMap.put("CHOOSE_ID", ObjectUtils.toString(tMap.get("CHOOSE_ID")));
				courseMap.put("LEARN_STATE", ObjectUtils.toString(tMap.get("LEARN_STATE")));
				courseMap.put("COURSE_SCHEDULE", ObjectUtils.toString(tMap.get("COURSE_SCHEDULE")));
				courseMap.put("EXAM_SCORE", ObjectUtils.toString(tMap.get("EXAM_SCORE")));
				courseMap.put("SCHEDULE", ObjectUtils.toString(tMap.get("SCHEDULE")));
				courseMap.put("STATE", ObjectUtils.toString(tMap.get("STATE")));
				courseMap.put("LEARN_STATUS", ObjectUtils.toString(tMap.get("LEARN_STATUS")));
				courseMap.put("action", AppConfig.getProperty("oclass.url"));
				String str = new EncryptUtils().encrypt(ObjectUtils.toString(tMap.get("CHOOSE_ID")) + ","
						+ ObjectUtils.toString(searchParams.get("STUDENT_ID")));
				courseMap.put("USER_INFO", str);
				courseList.add(courseMap);
				resultMap.put("PLANLIST", courseList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map getGrantCourseCert(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		List resultList = new ArrayList();

		try {

			Map planMap = new LinkedHashMap();
			List planList = new ArrayList();

			Map grantMap = new LinkedHashMap();
			List grantList = new ArrayList();

			Map stuMap = new LinkedHashMap();
			List stuList = new ArrayList();

			List list = courseLearningDao.getGrantCourseCert(searchParams);

			for (int i = 0; i < list.size(); i++) {
				Map tMap = (Map) list.get(i);

				String teach_plan_id = ObjectUtils.toString(tMap.get("TEACH_PLAN_ID"));
				if (EmptyUtils.isEmpty(planMap.get(teach_plan_id))) {
					Map cMap = new LinkedHashMap();
					cMap.put("TEACH_PLAN_ID", teach_plan_id);
					planList.add(cMap);
					planMap.put(teach_plan_id, planList);
				}

				String grant_id = ObjectUtils.toString(tMap.get("GRANT_ID"));
				String courseTheme = ObjectUtils.toString(tMap.get("COURSE_THEME"));
				String addr = ObjectUtils.toString(tMap.get("ADDR"));
				String startDate = ObjectUtils.toString(tMap.get("START_DATE"));
				String entDate = ObjectUtils.toString(tMap.get("ENT_DATE"));
				String teacher = ObjectUtils.toString(tMap.get("TEACHER"));
				String certificateStatus = ObjectUtils.toString(tMap.get("CERTIFICATE_STATUS"));
				if (EmptyUtils.isEmpty(grantMap.get(grant_id))) {
					Map gMap = new LinkedHashMap();
					gMap.put("TEACH_PLAN_ID", teach_plan_id);
					gMap.put("GRANT_ID", grant_id);
					gMap.put("COURSE_THEME", courseTheme);
					gMap.put("ADDR", addr);
					gMap.put("START_DATE", startDate);
					gMap.put("ENT_DATE", entDate);
					gMap.put("TEACHER", teacher);
					gMap.put("CERTIFICATE_STATUS", certificateStatus);
					grantList.add(gMap);
					grantMap.put(grant_id, grantList);
				}

				String studentName = ObjectUtils.toString(tMap.get("STUDENT_NAME"));
				String studentNo = ObjectUtils.toString(tMap.get("STUDENT_NO"));
				String singStatus = ObjectUtils.toString(tMap.get("SING_STATUS"));
				String image = ObjectUtils.toString(tMap.get("IMAGE"));
				if (EmptyUtils.isEmpty(stuMap.get(studentNo))) {
					Map sMap = new LinkedHashMap();
					sMap.put("GRANT_ID", grant_id);
					sMap.put("STUDENT_NAME", studentName);
					sMap.put("STUDENT_NO", studentNo);
					sMap.put("SING_STATUS", singStatus);
					sMap.put("IMAGE", image);
					stuList.add(sMap);
					stuMap.put(studentNo, stuList);
				}
			}

			for (int i = 0; i < planList.size(); i++) {
				Map temp1 = (Map) planList.get(i);
				List grantTemp = new ArrayList();
				for (int j = 0; j < grantList.size(); j++) {
					Map temp2 = (Map) grantList.get(j);
					if (ObjectUtils.toString(temp1.get("TEACH_PLAN_ID"))
							.equals(ObjectUtils.toString(temp2.get("TEACH_PLAN_ID")))) {
						List stuTemp = new ArrayList();
						for (int k = 0; k < stuList.size(); k++) {
							Map temp3 = (Map) stuList.get(k);
							if (ObjectUtils.toString(temp2.get("GRANT_ID"))
									.equals(ObjectUtils.toString(temp3.get("GRANT_ID")))) {
								stuTemp.add(temp3);
							}
						}
						temp2.put("stuList", stuTemp);
						grantTemp.add(temp2);
					}
				}
				temp1.put("grantList", grantTemp);
				resultList.add(temp1);
			}
			resultMap.put("LIST", resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map getGrantCousePlan(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			String teachPlanId = ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID"), "").trim();
			if (EmptyUtils.isEmpty(teachPlanId)) {
				resultMap.put("result", "2");
				resultMap.put("messageInfo", "TEACH_PLAN_ID不能为空！");
				return resultMap;
			}

			List list = courseLearningDao.getGrantCousePlan(searchParams);
			List grantList = new ArrayList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);

					Map tMap = new LinkedHashMap();
					tMap.put("TEACH_PLAN_ID", ObjectUtils.toString(map.get("TEACH_PLAN_ID")));
					tMap.put("GRANT_ID", ObjectUtils.toString(map.get("GRANT_ID")));
					tMap.put("COURSE_THEME", ObjectUtils.toString(map.get("COURSE_THEME")));
					tMap.put("ADDR", ObjectUtils.toString(map.get("ADDR")));
					tMap.put("START_DATE", ObjectUtils.toString(map.get("START_DATE")));
					tMap.put("ENT_DATE", ObjectUtils.toString(map.get("ENT_DATE")));
					tMap.put("TEACHER", ObjectUtils.toString(map.get("TEACHER")));
					tMap.put("CERTIFICATE_STATUS", ObjectUtils.toString(map.get("CERTIFICATE_STATUS")));

					grantList.add(tMap);
				}
				resultMap.put("grantList", grantList);
			} else {
				resultMap.put("grantList", new ArrayList());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map getGrantCourseCertData(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			String grantId = ObjectUtils.toString(searchParams.get("GRANT_ID"), "").trim();
			if (EmptyUtils.isEmpty(grantId)) {
				resultMap.put("result", "2");
				resultMap.put("messageInfo", "GRANT_ID不能为空！");
				return resultMap;
			}

			List list = courseLearningDao.getGrantCourseCertData(searchParams);
			List certList = new ArrayList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);

					Map tMap = new LinkedHashMap();
					tMap.put("GRANT_ID", ObjectUtils.toString(map.get("GRANT_ID")));
					tMap.put("STUDENT_NAME", ObjectUtils.toString(map.get("STUDENT_NAME")));
					tMap.put("STUDENT_NO", ObjectUtils.toString(map.get("STUDENT_NO")));
					tMap.put("SING_STATUS", ObjectUtils.toString(map.get("SING_STATUS")));
					certList.add(tMap);
				}
				resultMap.put("certList", certList);
			} else {
				resultMap.put("certList", new ArrayList());
			}

			List imageList = courseLearningDao.getGrantCourseCertImage(searchParams);
			if (EmptyUtils.isNotEmpty(imageList)) {
				resultMap.put("imageList", imageList);
			} else {
				resultMap.put("imageList", new ArrayList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void changeRecResultScore(Map map) {
		courseLearningDao.updateRecResultScore(map);
	}

	@Override
	public Map getCourseLearningData(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();

		try {
			searchParams.remove("EXAM_STATE");
			List teachPlans = courseLearningDao.getCourseLearningData(searchParams);
			Map tempMap = null;
			int sumScore = 0;
			if (EmptyUtils.isNotEmpty(teachPlans)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					tempMap = (Map) teachPlans.get(i);
					sumScore += Integer.parseInt(ObjectUtils.toString(tempMap.get("CREDIT")));
				}
			}
			resultMap.put("TOTALCOURSE", EmptyUtils.isNotEmpty(teachPlans) ? teachPlans.size() : 0); // 课程总数
			resultMap.put("TOTALCREDIT", sumScore); // 总学分

			searchParams.put("EXAM_STATE", "1");
			List pass = courseLearningDao.getCourseLearningData(searchParams);
			Map passMap = null;
			int passScore = 0;
			if (EmptyUtils.isNotEmpty(pass)) {
				for (int i = 0; i < pass.size(); i++) {
					passMap = (Map) pass.get(i);
					passScore += Integer.parseInt(ObjectUtils.toString(passMap.get("CREDIT")));
				}
			}
			resultMap.put("PASSCOURSECOUNT", EmptyUtils.isNotEmpty(teachPlans) ? pass.size() : 0); // 通过课程数
			resultMap.put("PASSCREDIT", passScore); // 已获学分

			/*
			 * searchParams.put("EXAM_STATE","2"); List teachList =
			 * courseLearningDao.getCourseLearningData(searchParams); if
			 * (EmptyUtils.isNotEmpty(teachList)){ for (int
			 * i=0;i<teachList.size();i++){ Map teachMap = (Map)
			 * teachList.get(i);
			 * 
			 * if("2".equals(ObjectUtils.toString(teachMap.get("EXAM_STATE")))){
			 * 
			 * String recId = ObjectUtils.toString(teachMap.get("CHOOSE_ID"));
			 * //直接取学习平台LCMS_USER_DYNA表中MY_POINT的数据 String xcxScore =
			 * ObjectUtils.toString(teachMap.get("MY_POINT")); String zjxScore =
			 * ObjectUtils.toString(teachMap.get("EXAM_SCORE1")); String score =
			 * ObjectUtils.toString(teachMap.get("TEST_POINT"));
			 * 
			 * if (EmptyUtils.isNotEmpty(score)){ zjxScore =
			 * ObjectUtils.toString(score); }
			 * 
			 * Map temp = new HashMap(); zjxScore =
			 * EmptyUtils.isNotEmpty(zjxScore)? zjxScore:"";
			 * if(EmptyUtils.isNotEmpty(zjxScore)){ Double zjxScore_1 =
			 * Double.parseDouble(zjxScore); temp.put("zjxScore",
			 * this.formatNumber(zjxScore_1)); } xcxScore =
			 * EmptyUtils.isNotEmpty(xcxScore)? xcxScore :
			 * ObjectUtils.toString(teachMap.get("EXAM_SCORE"));
			 * if(EmptyUtils.isNotEmpty(xcxScore)){ Double xcxScore_1 =
			 * Double.parseDouble(xcxScore); temp.put("xcxScore",
			 * this.formatNumber(xcxScore_1)); }
			 * 
			 * temp.put("UPDATED_BY",
			 * ObjectUtils.toString(searchParams.get("STUDENT_ID")));
			 * temp.put("CHOOSE_ID", recId); temp.put("TEACH_PLAN_ID",
			 * ObjectUtils.toString(teachMap.get("TEACH_PLAN_ID")));
			 * //courseLearningDao.updateRecResultScore(temp);
			 * //changeRecResultScore(temp);
			 * courseChangeService.changeRecResultScore(temp); } } }
			 */

			List terms = courseLearningDao.getTermListByLoginStudent(searchParams); // 学期列表
			List indexList = courseLearningDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
			String indexTerm = null;
			String indexCode = null;
			if (EmptyUtils.isNotEmpty(indexList)) {
				Map map = (Map) indexList.get(0);
				indexTerm = ObjectUtils.toString(map.get("GRADE_ID"));
				int index = Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")));
				if (index <= 5) {
					if (EmptyUtils.isNotEmpty(terms)) {
						for (int i = 0; i < terms.size(); i++) {
							Map indexMap = (Map) terms.get(i);
							if (indexTerm.equals(ObjectUtils.toString(indexMap.get("TERM_ID")))) {
								indexCode = ObjectUtils.toString(indexMap.get("KKXQ"));
								break;
							}
						}

						if ("".equals(indexCode) || indexCode == null) {
							Map index1 = (Map) terms.get(terms.size() - 1);
							indexCode = ObjectUtils.toString(index1.get("KKXQ"));
							indexTerm = ObjectUtils.toString(index1.get("TERM_ID"));
						}

					}
				} else {
					if (EmptyUtils.isNotEmpty(terms)) {
						Map index1 = (Map) terms.get(terms.size() - 1);
						indexCode = ObjectUtils.toString(index1.get("KKXQ"));
						indexTerm = ObjectUtils.toString(index1.get("TERM_ID"));
					}
				}
			} else {
				if (EmptyUtils.isNotEmpty(terms)) {
					Map index1 = (Map) terms.get(0);
					indexCode = ObjectUtils.toString(index1.get("KKXQ"));
					indexTerm = ObjectUtils.toString(index1.get("TERM_ID"));
				}
			}

			String bh = "0";
			String jbrs = "0";
			int pm = 0;
			String zzbh = "";
			List progress = courseLearningDao.getProgressOrder(searchParams);
			if (EmptyUtils.isNotEmpty(progress) && progress.size() > 0) {
				Map progressorder = (Map) progress.get(0); // 每天排名
				searchParams.put("STUDENT_PM", "1");
				List listPM = courseLearningDao.getClassStudentCount(searchParams);
				if (EmptyUtils.isNotEmpty(listPM)) {
					Map pmMap = (Map) listPM.get(0);
					pm = Integer.parseInt(ObjectUtils.toString(pmMap.get("STD_RANK")));
				}
				searchParams.remove("STUDENT_PM");
				String searchTermId = ObjectUtils.toString(searchParams.get("TERM_ID"));
				if (searchTermId.equals(indexTerm)) {
					String zh = ObjectUtils.toString(progressorder.get("LAST_LOGIN_ORDER"));
					if (!"0".equals(zh)) {
						bh = ObjectUtils.toString(progressorder.get("ORDER_CHANGE"));

						BigInteger b = new BigInteger(bh);
						b = b.abs();
						if (Integer.parseInt(bh) > 0) {
							zzbh = "↑ " + b;
						} else if (Integer.parseInt(bh) < 0) {
							zzbh = "↓ " + b;
						} else {
							zzbh = "" + b;
						}
					} else {
						zzbh = "";
					}
				} else {
					zzbh = "";
				}
			}
			resultMap.put("RANKING", pm); // 排名
			resultMap.put("jbrs", zzbh); // 击败人数

			String entranceTerm = null;
			List entrance = courseLearningDao.getEntranceTermByStu(searchParams);
			if (EmptyUtils.isNotEmpty(entrance)) {
				Map tEntMap = (Map) entrance.get(0);
				entranceTerm = ObjectUtils.toString(tEntMap.get("GRADE_ID"), "");
			}

			if (indexTerm.equals(entranceTerm)) {
				resultMap.put("IS_NEWSTU", Constants.IS_NEWSTUDENT);
			} else {
				resultMap.put("IS_NEWSTU", Constants.IS_NONEWSTUDENT);
			}

			searchParams.remove("EXAM_STATE");
			List teachPlans1 = courseLearningDao.getCourseLearningData(searchParams);
			Map<String, String> planMap = new HashMap<String, String>();
			if (EmptyUtils.isNotEmpty(teachPlans1)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					Map temp = (Map) teachPlans.get(i);
					String termId = ObjectUtils.toString(temp.get("TERM_ID"));
					planMap.put(termId, termId);
				}
			}
			if (EmptyUtils.isNotEmpty(teachPlans1)) {
				for (String key : planMap.keySet()) {
					List<Object> course_list = new ArrayList<Object>();
					for (int i = 0; i < teachPlans1.size(); i++) {
						Map temp = (Map) teachPlans1.get(i);
						String termId = ObjectUtils.toString(temp.get("TERM_ID"));
						if (key.equals(termId)) {
							Map<String, Object> course_map = new LinkedHashMap<String, Object>();

							// LEARN_STATUS 3 待验收 5 部分启用 是可以学习的
							// if
							// ("5".equals(ObjectUtils.toString(temp.get("LEARN_STATUS")))
							// ||
							// "3".equals(ObjectUtils.toString(temp.get("LEARN_STATUS"))))
							// {
							// temp.put("LEARN_STATUS", "1");
							// }
							// 课程是否已经有验收过，已经启用过
							String learnStatus = ObjectUtils.toString(temp.get("LEARN_STATUS"));
							String termcourseIdStatus = ObjectUtils.toString(temp.get("TERMCOURSE_ID_STATUS"));
							String isCourseStartdate = ObjectUtils.toString(temp.get("IS_COURSE_STARTDATE"));
							String searchTermId = ObjectUtils.toString(searchParams.get("TERM_ID"));
							// 当前学期判断规则，提供给移动端
							if (indexTerm.equals(searchTermId)) {
								if ("0".equals(isCourseStartdate)) { // 教学周期未开始
									if ("1".equals(termcourseIdStatus) && "1".equals(learnStatus)) {
										course_map.put("IS_PREPARE", "0"); // 进入预习
										temp.put("IS_OPEN", "0"); // 预习可以开放
									} else if ("0".equals(termcourseIdStatus) && !"1".equals(learnStatus)) {
										course_map.put("IS_PREPARE", "2"); // 暂未开放学习
									} else if ("0".equals(termcourseIdStatus) || !"1".equals(learnStatus)) {
										course_map.put("IS_PREPARE", "2"); // 暂未开放学习
									}
								} else if ("1".equals(isCourseStartdate) || "2".equals(isCourseStartdate)) { // 教学周期已开始
									if ("1".equals(termcourseIdStatus) && "1".equals(learnStatus)) {
										course_map.put("IS_PREPARE", "1"); // 进入学习
									} else if ("0".equals(termcourseIdStatus) && !"1".equals(learnStatus)) {
										course_map.put("IS_PREPARE", "2"); // 暂未开放学习
									} else if ("0".equals(termcourseIdStatus) || !"1".equals(learnStatus)) {
										course_map.put("IS_PREPARE", "2"); // 暂未开放学习
									}
								}
							} else {
								course_map.put("IS_PREPARE", ""); // 其他情况：学期还没开始，或者学期已过
							}

							// 当为老生情况，且判断当前学期的下一个学期
							int termCode = Integer.parseInt(indexCode) + 1;
							if ("1".equals(ObjectUtils.toString(resultMap.get("IS_NEWSTU"))) && ObjectUtils
									.toString(searchParams.get("KKXQ")).equals(ObjectUtils.toString(termCode))) {
								if ("0".equals(ObjectUtils.toString(temp.get("IS_OPEN")))) { // 学期已开始
									if ("0".equals(isCourseStartdate)) { // 教学周期未开始
										if ("1".equals(termcourseIdStatus) && "1".equals(learnStatus)) {
											course_map.put("IS_PREPARE", "0"); // 进入预习
											temp.put("IS_OPEN", "0"); // 预习可以开放
										} else if ("0".equals(termcourseIdStatus) && !"1".equals(learnStatus)) {
											course_map.put("IS_PREPARE", "2"); // 暂未开放学习
										} else if ("0".equals(termcourseIdStatus) || !"1".equals(learnStatus)) {
											course_map.put("IS_PREPARE", "2"); // 暂未开放学习
										}
									} else if ("1".equals(isCourseStartdate) || "2".equals(isCourseStartdate)) { // 教学周期已开始
										if ("1".equals(termcourseIdStatus) && "1".equals(learnStatus)) {
											course_map.put("IS_PREPARE", "1"); // 进入学习
										} else if ("0".equals(termcourseIdStatus) && !"1".equals(learnStatus)) {
											course_map.put("IS_PREPARE", "2"); // 暂未开放学习
										} else if ("0".equals(termcourseIdStatus) || !"1".equals(learnStatus)) {
											course_map.put("IS_PREPARE", "2"); // 暂未开放学习
										}   
									}
								}
							} else if (!(indexTerm.equals(searchTermId))) {
								course_map.put("IS_PREPARE", ""); // 其他情况：学期还没开始，或者学期已过
							}
							
							if (EmptyUtils.isEmpty(course_map.get("IS_PREPARE")) || "2".equals(course_map.get("IS_PREPARE"))) {
								if(NumberUtils.toInt(ObjectUtils.toString(searchParams.get("KKXQ"))) < NumberUtils.toInt(indexCode)) {
								    // 之前的学期
									course_map.put("IS_PREPARE", "1");
								} else if (EmptyUtils.isNotEmpty(temp.get("SCHEDULE")) && Double.parseDouble(ObjectUtils.toString(temp.get("SCHEDULE")))>0) {
									course_map.put("IS_PREPARE", "0");
								}
							}
							
							String teachPlanId = ObjectUtils.toString(temp.get("TEACH_PLAN_ID"));
							String termcourseId = ObjectUtils.toString(temp.get("TERMCOURSE_ID"));
							course_map.put("TEACH_PLAN_ID", teachPlanId);
							if (EmptyUtils.isNotEmpty(termcourseId)) {
								course_map.put("TERMCOURSE_ID", termcourseId);
							} else {
								course_map.put("TERMCOURSE_ID", teachPlanId);
							}
							course_map.put("TERM_ID", termId);
							course_map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
							course_map.put("START_DATE", ObjectUtils.toString(temp.get("START_DATE")));
							course_map.put("END_DATE", ObjectUtils.toString(temp.get("END_DATE")));
							course_map.put("COURSE_START_DATE", ObjectUtils.toString(temp.get("COURSE_START_DATE")));
							course_map.put("COURSE_END_DATE", ObjectUtils.toString(temp.get("COURSE_END_DATE")));
							course_map.put("IS_COURSE_STARTDATE", isCourseStartdate);
							course_map.put("TERMCOURSE_ID_STATUS", termcourseIdStatus);
							course_map.put("IS_OPEN", ObjectUtils.toString(temp.get("IS_OPEN")));
							course_map.put("CLASS_ID", ObjectUtils.toString(temp.get("CLASS_ID")));
							course_map.put("KCFM", ObjectUtils.toString(temp.get("KCFM")));
							course_map.put("COUNTCOURSE", ObjectUtils.toString(temp.get("COUNTCOURSE")));
							course_map.put("WSJXZK", ObjectUtils.toString(temp.get("WSJXZK")));
							course_map.put("COURSE_ID", ObjectUtils.toString(temp.get("COURSE_ID")));
							course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
							course_map.put("COURSE_CODE", ObjectUtils.toString(temp.get("COURSE_CODE")));
							course_map.put("COURSE_CATEGORY", ObjectUtils.toString(temp.get("COURSE_CATEGORY")));
							course_map.put("SOURCE_COURSE_ID", ObjectUtils.toString(temp.get("SOURCE_COURSE_ID")));
							course_map.put("SOURCE_KCH", ObjectUtils.toString(temp.get("SOURCE_KCH")));
							course_map.put("SOURCE_KCMC", ObjectUtils.toString(temp.get("SOURCE_KCMC")));
							course_map.put("COURSE_STYLE", ObjectUtils.toString(temp.get("COURSE_STYLE")));
							course_map.put("CREDIT", ObjectUtils.toString(temp.get("CREDIT")));
							course_map.put("COUNSELOR", ObjectUtils.toString(temp.get("COUNSELOR")));
							course_map.put("IS_ONLINE", ObjectUtils.toString(temp.get("IS_ONLINE")));

							if ("5".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
								course_map.put("SCHEDULE", ObjectUtils.toString(temp.get("PROGRESS")));
								course_map.put("EXAM_SCORE", ObjectUtils.toString(temp.get("STUDY_SCORE")));
							} else {
								course_map.put("SCHEDULE", ObjectUtils.toString(temp.get("SCHEDULE")));
								course_map.put("EXAM_SCORE", ObjectUtils.toString(temp.get("EXAM_SCORE")));
							}

							course_map.put("EXAM_SCORE1", ObjectUtils.toString(temp.get("EXAM_SCORE1")));
							course_map.put("LEARN_STATUS", learnStatus);

							String examState = ObjectUtils.toString(temp.get("EXAM_STATE"));
							String examScore2 = ObjectUtils.toString(temp.get("EXAM_SCORE2"));
							if ("0".equals(examState) || "1".equals(examState)) {
								course_map.put("EXAM_SCORE2", examScore2);
								course_map.put("EXAM_STATE", examState);
							} else if ("2".equals(examState)) {
								course_map.put("EXAM_SCORE2", "学习中");
								course_map.put("EXAM_STATE", examState);
							} else if ("3".equals(examState)) {
								course_map.put("EXAM_SCORE2", "登记中");
								course_map.put("EXAM_STATE", examState);
							} else {
								course_map.put("EXAM_SCORE2", examScore2);
								course_map.put("EXAM_STATE", examState);
							}

							course_map.put("KCXXBZ", ObjectUtils.toString(temp.get("KCXXBZ")));
							course_map.put("KCKSBZ", ObjectUtils.toString(temp.get("KCKSBZ")));
							course_map.put("CHOOSE_ID", ObjectUtils.toString(temp.get("CHOOSE_ID")));
							course_map.put("REC_ID", ObjectUtils.toString(temp.get("REC_ID")));
							course_map.put("action", AppConfig.getProperty("oclass.url"));
							course_map.put("GUIDE_PATH", ObjectUtils.toString(temp.get("GUIDE_PATH")));
							course_map.put("LEARNING_STYLE", ObjectUtils.toString(temp.get("LEARNING_STYLE")));

							String str = new EncryptUtils().encrypt(ObjectUtils.toString(temp.get("CHOOSE_ID")) + ","
									+ ObjectUtils.toString(searchParams.get("STUDENT_ID")));
							course_map.put("USER_INFO", str);

							course_map.put("SCHOOL_MODEL", ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")));
							// 学习网模式跳转到学习网学习
							if ("5".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
								// TODO 先测试用
								// 将这七个参数按字CourseClass、CourseCode、Fullname、Rand、RoleID、SemesterCode、UserName典排序，升序连接为一个字符串
								String CourseClass = "[{\"OrganizationCode\":\"\",\"CourseClassCode\":\"\",\"CourseClassName\":\"\"}]";
								String CourseCode = ObjectUtils.toString(temp.get("SOURCE_KCH"));
								String Fullname = ObjectUtils.toString(searchParams.get("Fullname"));
								String Rand = ToolUtil.getRandomInt(10);
								String RoleID = "4";
								String SemesterCode = "180";
								String UserName = ObjectUtils.toString(searchParams.get("UserName"));
								String Signature = "CourseClass=" + CourseClass + "CourseCode=" + CourseCode
										+ "FullName=" + Fullname + "Rand=" + Rand + "RoleID=" + RoleID + "SemesterCode="
										+ SemesterCode + "UserName=" + UserName
										+ "secretKey=VpTR0U9WXTDwsLhafQiCqOaYyqx0ObUFHV96HYOdxrDQwcd5qvG98xYSVariRH1c";
								Signature = Md5Util.getMD5(Signature);

								String xxwUrl = AppConfig.getProperty("xxw.login");
								if (EmptyUtils.isNotEmpty(xxwUrl)) {
									if (xxwUrl.indexOf("production") > -1) {
										xxwUrl = xxwUrl.replace("production", "sichuan.ouchn.cn");
									} else if (xxwUrl.indexOf("develope") > -1) {
										xxwUrl = xxwUrl.replace("develope", "zhuli.ouchn.cn");
									}
								}

								String xxw_url = xxwUrl + "?UserName=" + UserName + "&FullName=" + Fullname + "&RoleID="
										+ RoleID + "&SemesterCode=" + SemesterCode + "&CourseCode=" + CourseCode
										+ "&Rand=" + Rand + "&CourseClass=" + CourseClass + "&Signature=" + Signature;
								course_map.put("XXW_URL", xxw_url);
							}

							course_list.add(course_map);
							resultMap.put("PLANLIST", course_list);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return resultMap;
	}

	@Override
	@Transactional
	public Map acadeMyLearnCourseData(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			List pmbhList = courseLearningDao.getProgressOrder(searchParams);// 查询排名
			if (EmptyUtils.isNotEmpty(pmbhList)) {
				Map pmbh = (Map) pmbhList.get(0);
				Date updateDt = (Date) pmbh.get("UPDATE_DT"); // 修改时间
				Date currentLoginTime = (Date) pmbh.get("CURRENT_LOGIN_TIME"); // 本次登录单天时间
				if (currentLoginTime != null) {
					if (currentLoginTime.getTime() != updateDt.getTime()) {
						courseLearningDao.updateProgressOrder(pmbh);
					}
				} else {
					courseLearningDao.updateProgressOrder(pmbh);
				}
			}

			List teachPlans = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("TOTALCOURSE", EmptyUtils.isNotEmpty(teachPlans) ? teachPlans.size() : 0); // 课程总数

			searchParams.put("EXAM_STATE", "0");
			List nopass = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("NO_PASSSCOUNT", EmptyUtils.isNotEmpty(nopass) ? nopass.size() : 0); // 未通过课程数

			searchParams.put("EXAM_STATE", "1");
			List pass = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("PASSCOURSECOUNT", EmptyUtils.isNotEmpty(pass) ? pass.size() : 0); // 通过课程数

			searchParams.put("EXAM_STATE", "2");
			List learning = courseLearningDao.acadeMyLearnCourse(searchParams);
			resultMap.put("LEARNINGCOUNT", EmptyUtils.isNotEmpty(learning) ? learning.size() : 0); // 学习中课程数
			if (EmptyUtils.isNotEmpty(learning)) {
				for (int i = 0; i < learning.size(); i++) {
					Map teachMap = (Map) learning.get(i);
					if ("2".equals(ObjectUtils.toString(teachMap.get("EXAM_STATE")))) {

						String recId = ObjectUtils.toString(teachMap.get("CHOOSE_ID"));
						// 直接取学习平台LCMS_USER_DYNA表中MY_POINT的数据
						String xcxScore = ObjectUtils.toString(teachMap.get("MY_POINT"));
						String zjxScore = ObjectUtils.toString(teachMap.get("EXAM_SCORE1"));
						String score = ObjectUtils.toString(teachMap.get("TEST_POINT"));
						if (EmptyUtils.isNotEmpty(score)) {
							zjxScore = ObjectUtils.toString(score);
						}
						Map temp = new HashMap();
						zjxScore = EmptyUtils.isNotEmpty(zjxScore) ? zjxScore : "";
						if (EmptyUtils.isNotEmpty(zjxScore)) {
							Double zjxScore_1 = Double.parseDouble(zjxScore);
							temp.put("zjxScore", this.formatNumber(zjxScore_1));
						}
						xcxScore = EmptyUtils.isNotEmpty(xcxScore) ? xcxScore : "";
						if (EmptyUtils.isNotEmpty(xcxScore)) {
							Double xcxScore_1 = Double.parseDouble(xcxScore);
							temp.put("xcxScore", this.formatNumber(xcxScore_1));
						}
						temp.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
						temp.put("CHOOSE_ID", recId);
						temp.put("TEACH_PLAN_ID", ObjectUtils.toString(teachMap.get("TEACH_PLAN_ID")));
						// courseLearningDao.updateRecResultScore(temp);
						changeRecResultScore(temp);
					}
				}
			}

			String bh = "0";
			int pm = 0;
			String zzbh = "";
			List progress = courseLearningDao.getProgressOrder(searchParams);
			if (EmptyUtils.isNotEmpty(progress) && progress.size() > 0) {
				Map progressorder = (Map) progress.get(0);
				searchParams.put("STUDENT_PM", "1");
				List listPM = courseLearningDao.getClassStudentCount(searchParams); // 班级排名
				if (EmptyUtils.isNotEmpty(listPM)) {
					Map pmMap = (Map) listPM.get(0);
					pm = Integer.parseInt(ObjectUtils.toString(pmMap.get("STD_RANK")));
				}
				searchParams.remove("STUDENT_PM");

				String zh = ObjectUtils.toString(progressorder.get("LAST_LOGIN_ORDER"));
				if (!"0".equals(zh)) {
					bh = ObjectUtils.toString(progressorder.get("ORDER_CHANGE"));

					BigInteger b = new BigInteger(bh);
					b = b.abs();
					if (Integer.parseInt(bh) > 0) {
						zzbh = "↑ " + b;
					} else if (Integer.parseInt(bh) < 0) {
						zzbh = "↓ " + b;
					} else {
						zzbh = "" + b;
					}
				} else {
					zzbh = "";
				}
			}
			resultMap.put("RANKING", pm); // 排名
			resultMap.put("jbrs", zzbh); // 击败人数

			// ===========待预约考试统计开始==============//
			searchParams.put("CURRENT_FLAG", "2");
			List current_plan = examServeDao.getExamBatchData(searchParams); // 查询当前考试计划
			String exam_batch_code = null;
			String end_date = null;
			if (EmptyUtils.isNotEmpty(current_plan)) {
				Map currentMap = (Map) current_plan.get(0);
				exam_batch_code = ObjectUtils.toString(currentMap.get("EXAM_BATCH_CODE"));
				end_date = ObjectUtils.toString(currentMap.get("END_DATE"));
			}
			searchParams.remove("CURRENT_FLAG");
			List<Object> resultList = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(exam_batch_code)) {
				searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
				searchParams.put("BESPEAK_STATE", "0");
				searchParams.put("END_DATE", end_date);
				List recResultList = examServeDao.getChooseCourseByStudent(searchParams);
				if (EmptyUtils.isNotEmpty(recResultList)) {
					for (int i = 0; i < recResultList.size(); i++) {
						Map temp1 = (Map) recResultList.get(i);
						Map courseMap = new HashMap();
						courseMap.put("APPOINTMENT_FLAG", "1"); // 待预约列表
						courseMap.put("XX_ID", searchParams.get("XX_ID"));
						courseMap.put("EXAM_BATCH_CODE", searchParams.get("EXAM_BATCH_CODE"));
						courseMap.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
						courseMap.put("TERM_ID", ObjectUtils.toString(temp1.get("TERM_ID")));
						if ("1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
							courseMap.put("SCHOOL_MODEL", ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")));
							courseMap.put("TYPE", "7");
						}

						List courseListInfo = examServeDao.getExamPlanAndCourseInfo(courseMap);

						if (EmptyUtils.isNotEmpty(courseListInfo)) {
							for (int j = 0; j < courseListInfo.size(); j++) {
								Map temp2 = (Map) courseListInfo.get(j);

								Map temp3 = new LinkedHashMap();

								temp3.put("TERM_CODE", ObjectUtils.toString(temp1.get("TERM_CODE")));
								temp3.put("TERM_ID", ObjectUtils.toString(temp1.get("TERM_ID")));
								temp3.put("TERM_NAME", ObjectUtils.toString(temp1.get("TERM_NAME")));
								temp3.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
								temp3.put("COURSE_NAME", ObjectUtils.toString(temp1.get("COURSE_NAME")));
								temp3.put("TYPE", ObjectUtils.toString(temp2.get("TYPE")));
								temp3.put("KCH", ObjectUtils.toString(temp1.get("KCH")));
								temp3.put("COURSE_COST", ObjectUtils.toString(temp1.get("COURSE_COST")));
								temp3.put("MAKEUP", ObjectUtils.toString(temp1.get("MAKEUP")));
								temp3.put("PAY_STATE", ObjectUtils.toString(temp1.get("PAY_STATE")));
								temp3.put("BESPEAK_STATE", ObjectUtils.toString(temp1.get("BESPEAK_STATE")));
								temp3.put("REC_ID", ObjectUtils.toString(temp1.get("REC_ID")));

								resultList.add(temp3);
							}
						}
					}
				}

				// 删除重复数据(根据REC_ID相同的)
				for (int i = 0; i < resultList.size(); i++) {
					Map map1 = (Map) resultList.get(i);
					for (int j = resultList.size() - 1; j > i; j--) {
						Map map2 = (Map) resultList.get(j);
						if ((ObjectUtils.toString(map1.get("REC_ID"))
								.equals(ObjectUtils.toString(map2.get("REC_ID"))))) {
							resultList.remove(j);
						}
					}
				}
				List<Object> myAppointmentList = new ArrayList<Object>();
				if (EmptyUtils.isNotEmpty(resultList)) {
					for (int i = 0; i < resultList.size(); i++) {
						Map tempPlan = (Map) resultList.get(i);
						Map viewMap = new HashMap();
						viewMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
						viewMap.put("COURSE_ID", ObjectUtils.toString(tempPlan.get("COURSE_ID")));
						viewMap.put("TYPE", ObjectUtils.toString(tempPlan.get("TYPE")));
						List viewList = examServeDao.getViewExamPlanByAcadeMy(viewMap);
						if (EmptyUtils.isNotEmpty(viewList)) {
							for (int j = 0; j < viewList.size(); j++) {
								Map viewScMap = (Map) viewList.get(j);
								if (ObjectUtils.toString(searchParams.get("KKZY"))
										.equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
										|| "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))) {
									myAppointmentList.add(viewCourseMap(tempPlan));
									break;
								}
							}
						}
					}
				}
				int pending_count = 0;
				for (int i = 0; i < myAppointmentList.size(); i++) {
					Map tMap = (Map) myAppointmentList.get(i);
					if ("0".equals(ObjectUtils.toString(tMap.get("BESPEAK_STATE")))) {
						pending_count = pending_count + 1;
					}
				}
				// 待预约考试科目
				resultMap.put("APPOINTMENT_COUNT", ObjectUtils.toString(pending_count));
			}

			// =======查询课程学习列表开始======//
			searchParams.remove("EXAM_STATE");
			List teachPlans1 = courseLearningDao.acadeMyLearnCourse(searchParams);
			List<Object> course_list = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(teachPlans1)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					Map tempPlan = (Map) teachPlans1.get(i);
					Map<String, Object> course_map = new LinkedHashMap<String, Object>();

					course_map.put("TEACH_PLAN_ID", ObjectUtils.toString(tempPlan.get("TEACH_PLAN_ID")));
					if (EmptyUtils.isNotEmpty(ObjectUtils.toString(tempPlan.get("TERMCOURSE_ID")))) {
						course_map.put("TERMCOURSE_ID", ObjectUtils.toString(tempPlan.get("TERMCOURSE_ID")));
					} else {
						course_map.put("TERMCOURSE_ID", ObjectUtils.toString(tempPlan.get("TEACH_PLAN_ID")));
					}
					course_map.put("TERM_ID", ObjectUtils.toString(tempPlan.get("TERM_ID")));
					course_map.put("TERM_NAME", ObjectUtils.toString(tempPlan.get("TERM_NAME")));
					course_map.put("START_DATE", ObjectUtils.toString(tempPlan.get("START_DATE")));
					course_map.put("END_DATE", ObjectUtils.toString(tempPlan.get("END_DATE")));
					course_map.put("COURSE_START_DATE", ObjectUtils.toString(tempPlan.get("COURSE_START_DATE")));
					course_map.put("COURSE_END_DATE", ObjectUtils.toString(tempPlan.get("COURSE_END_DATE")));
					course_map.put("IS_COURSE_STARTDATE", ObjectUtils.toString(tempPlan.get("IS_COURSE_STARTDATE")));
					course_map.put("TERMCOURSE_ID_STATUS", ObjectUtils.toString(tempPlan.get("TERMCOURSE_ID_STATUS")));
					course_map.put("IS_OPEN", ObjectUtils.toString(tempPlan.get("IS_OPEN")));
					course_map.put("CLASS_ID", ObjectUtils.toString(tempPlan.get("CLASS_ID")));
					course_map.put("KCFM", ObjectUtils.toString(tempPlan.get("KCFM")));
					course_map.put("COUNTCOURSE", ObjectUtils.toString(tempPlan.get("COUNTCOURSE")));
					course_map.put("COURSE_ID", ObjectUtils.toString(tempPlan.get("COURSE_ID")));
					course_map.put("WSJXZK", ObjectUtils.toString(tempPlan.get("WSJXZK")));
					course_map.put("COURSE_NAME", ObjectUtils.toString(tempPlan.get("COURSE_NAME")));
					course_map.put("COURSE_CODE", ObjectUtils.toString(tempPlan.get("COURSE_CODE")));
					course_map.put("COURSE_CATEGORY", ObjectUtils.toString(tempPlan.get("COURSE_CATEGORY")));
					course_map.put("SOURCE_COURSE_ID", ObjectUtils.toString(tempPlan.get("SOURCE_COURSE_ID")));
					course_map.put("SOURCE_KCH", ObjectUtils.toString(tempPlan.get("SOURCE_KCH")));
					course_map.put("SOURCE_KCMC", ObjectUtils.toString(tempPlan.get("SOURCE_KCMC")));
					course_map.put("COURSE_STYLE", ObjectUtils.toString(tempPlan.get("COURSE_STYLE"))); // 课程类型
																										// 必修选修补修
					course_map.put("COUNSELOR", ObjectUtils.toString(tempPlan.get("COUNSELOR")));
					course_map.put("IS_ONLINE", ObjectUtils.toString(tempPlan.get("IS_ONLINE")));
					course_map.put("SCHEDULE", ObjectUtils.toString(tempPlan.get("SCHEDULE")));
					course_map.put("EXAM_SCORE", ObjectUtils.toString(tempPlan.get("EXAM_SCORE")));
					course_map.put("EXAM_SCORE1", ObjectUtils.toString(tempPlan.get("EXAM_SCORE1")));
					course_map.put("LEARN_STATUS", ObjectUtils.toString(tempPlan.get("LEARN_STATUS")));

					if ("0".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))
							|| "1".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))) {
						course_map.put("EXAM_SCORE2", ObjectUtils.toString(tempPlan.get("EXAM_SCORE2")));
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					} else if ("2".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))) {
						course_map.put("EXAM_SCORE2", "学习中");
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					} else if ("3".equals(ObjectUtils.toString(tempPlan.get("EXAM_STATE")))) {
						course_map.put("EXAM_SCORE2", "登记中");
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					} else {
						course_map.put("EXAM_SCORE2", ObjectUtils.toString(tempPlan.get("EXAM_SCORE2")));
						course_map.put("EXAM_STATE", ObjectUtils.toString(tempPlan.get("EXAM_STATE")));
					}

					course_map.put("KCXXBZ", ObjectUtils.toString(tempPlan.get("KCXXBZ")));
					course_map.put("KCKSBZ", ObjectUtils.toString(tempPlan.get("KCKSBZ")));
					course_map.put("CHOOSE_ID", ObjectUtils.toString(tempPlan.get("CHOOSE_ID")));
					course_map.put("REC_ID", ObjectUtils.toString(tempPlan.get("REC_ID")));
					course_map.put("action", AppConfig.getProperty("oclass.url"));

					String str = new EncryptUtils().encrypt(ObjectUtils.toString(tempPlan.get("CHOOSE_ID")) + ","
							+ ObjectUtils.toString(searchParams.get("STUDENT_ID")));
					course_map.put("USER_INFO", str);

					course_list.add(course_map);
					resultMap.put("PLANLIST", course_list);
				}
			}
			// =======查询课程学习列表结束======//
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 下载成绩单
	 */
	public void downGradesExcel(Map formMap, HttpServletRequest request, HttpServletResponse response,
			String basePath) {
		try {
			String filePath = basePath + "/html/" + SequenceUUID.getSequence() + ".pdf";
			Map resultMap = new HashMap();
			List detailList = gjtExamRecordNewDao.getExamRecordDetail(formMap);
			if (EmptyUtils.isNotEmpty(detailList)) {

				Map tempMap = (Map) detailList.get(0);

				String xh = ObjectUtils.toString(tempMap.get("XH"));
				String xm = ObjectUtils.toString(tempMap.get("XM"));
				String xb = "";
				if ("1".equals(ObjectUtils.toString(tempMap.get("XBM")))) {
					xb = "男";
				} else {
					xb = "女";
				}
				String zymc = ObjectUtils.toString(tempMap.get("ZYMC"));
				String rule_code = ObjectUtils.toString(tempMap.get("RULE_CODE"));
				String pycc_name = ObjectUtils.toString(tempMap.get("PYCC_NAME"));
				String user_type_name = ObjectUtils.toString(tempMap.get("USER_TYPE_NAME"));
				String grade_name = ObjectUtils.toString(tempMap.get("GRADE_NAME"));
				String zdbyxf = ObjectUtils.toString(tempMap.get("ZDBYXF"));
				String get_point = ObjectUtils.toString(tempMap.get("GET_POINT"));
				String year_name = ObjectUtils.toString(tempMap.get("YEAR_NAME"));
				String zyddksxf = ObjectUtils.toString(tempMap.get("ZYDDKSXF"));

				File file = new File(filePath);
				file.createNewFile();

				Document document = new Document();// 建立一个Document对象
				document.setPageSize(PageSize.A3);// 设置页面大小
				PdfWriter.getInstance(document, new FileOutputStream(file));
				document.open();

				PdfPTable table = CreatePdf.createTable(12, 820);
				float[] widths = { 3f, 3f, 4f, 3f, 2f, 4f, 2f, 2f, 2f, 3f, 3f, 2f };
				table.setWidths(widths);
				table.addCell(CreatePdf.createCell("学生成绩记录", CreatePdf.keyfont, Element.ALIGN_CENTER, 12, true, null));

				table.addCell(CreatePdf.createCell("学号：" + xh, CreatePdf.keyfont, Element.ALIGN_CENTER, 4, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("姓名：" + xm, CreatePdf.keyfont, Element.ALIGN_CENTER, 2, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("性别：" + xb, CreatePdf.keyfont, Element.ALIGN_CENTER, 2, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("专业：" + zymc, CreatePdf.keyfont, Element.ALIGN_CENTER, 2, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("规则号：" + rule_code, CreatePdf.keyfont, Element.ALIGN_CENTER, 2, true,
						CreatePdf.gray1));

				table.addCell(CreatePdf.createCell("专业层次：" + pycc_name, CreatePdf.keyfont, Element.ALIGN_CENTER, 4,
						true, CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("学生类型：" + user_type_name, CreatePdf.keyfont, Element.ALIGN_CENTER, 2,
						true, CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("年度：" + year_name, CreatePdf.keyfont, Element.ALIGN_CENTER, 2, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("学期：" + grade_name, CreatePdf.keyfont, Element.ALIGN_CENTER, 4, true,
						CreatePdf.gray1));

				table.addCell(CreatePdf.createCell("专业规则要求", CreatePdf.keyfont, Element.ALIGN_CENTER, 4, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("毕业学分：" + zdbyxf, CreatePdf.keyfont, Element.ALIGN_CENTER, 4, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("中央电大考试学分：" + zyddksxf, CreatePdf.keyfont, Element.ALIGN_CENTER, 4,
						true, CreatePdf.gray1));

				table.addCell(
						CreatePdf.createCell("模块", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true, CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("模块最低学分", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("模块中央最低学分", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("模块已通过学分", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("课程ID", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("课程名称", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("课程性质", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("课程类型", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(
						CreatePdf.createCell("学分", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true, CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("建议开设学期", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(CreatePdf.createCell("要求考试单位", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true,
						CreatePdf.gray1));
				table.addCell(
						CreatePdf.createCell("成绩", CreatePdf.keyfont, Element.ALIGN_CENTER, 0, true, CreatePdf.gray1));

				formMap.put("student_id", ObjectUtils.toString(formMap.get("STUDENT_ID")));
				List moduleList = gjtRecResultService.getCreditInfoAnd(formMap);

				int num = 0;
				for (int j = 0; j < moduleList.size(); j++) {
					Map moduleMap = (Map) moduleList.get(j);
					String moduleName = ObjectUtils.toString(moduleMap.get("NAME"));
					String sum_point = ObjectUtils.toString(moduleMap.get("GET_POINT"));
					String limit_score = ObjectUtils.toString(moduleMap.get("LIMIT_SCORE"));
					String crtvu_score = ObjectUtils.toString(moduleMap.get("CRTVU_SCORE"));

					formMap.put("MODULE_ID", ObjectUtils.toString(moduleMap.get("MODULE_ID")));
					List scoreList = gjtExamRecordNewDao.getScoreList(formMap);
					int scoreSize = scoreList.size();

					table.addCell(CreatePdf.createCell(moduleName, CreatePdf.textfont, scoreSize, CreatePdf.gray1));
					table.addCell(CreatePdf.createCell(limit_score, CreatePdf.textfont, scoreSize, CreatePdf.gray1));
					table.addCell(CreatePdf.createCell(crtvu_score, CreatePdf.textfont, scoreSize, CreatePdf.gray1));
					table.addCell(CreatePdf.createCell(sum_point, CreatePdf.textfont, scoreSize, CreatePdf.gray1));

					for (int i = 0; i < scoreSize; i++) {
						Map scoreMap = (Map) scoreList.get(i);
						String kch = ObjectUtils.toString(scoreMap.get("KCH"));
						String kcmc = ObjectUtils.toString(scoreMap.get("KCMC"));
						String kcsx = ObjectUtils.toString(scoreMap.get("KCSX"));
						if ("0".equals(kcsx)) {
							kcsx = "必修";
						} else if ("1".equals(kcsx)) {
							kcsx = "选修";
						} else if ("2".equals(kcsx)) {
							kcsx = "补修";
						} else {
							kcsx = "";
						}
						String course_type = ObjectUtils.toString(scoreMap.get("COURSE_TYPE"));
						if ("0".equals(course_type)) {
							course_type = "统设";
						} else if ("1".equals(course_type)) {
							course_type = "非统设";
						} else {
							course_type = "";
						}
						String ksdw = ObjectUtils.toString(scoreMap.get("KSDW"));
						if ("1".equals(ksdw)) {
							ksdw = "省";
						} else if ("2".equals(ksdw)) {
							ksdw = "中央";
						} else if ("3".equals(ksdw)) {
							ksdw = "分校";
						} else {
							ksdw = "";
						}
						String get_credits = ObjectUtils.toString(scoreMap.get("GET_CREDITS"));
						String exam_score2 = ObjectUtils.toString(scoreMap.get("EXAM_SCORE2"));
						String kkxq = ObjectUtils.toString(scoreMap.get("KKXQ"));
						if (EmptyUtils.isEmpty(get_credits) || "0".equals(get_credits)) {
							get_credits = "--";
						}
						if (EmptyUtils.isEmpty(exam_score2) || "0".equals(exam_score2)) {
							exam_score2 = "--";
						}

						table.addCell(CreatePdf.createCell(kch, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(kcmc, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(kcsx, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(course_type, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(get_credits, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(kkxq, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(ksdw, CreatePdf.textfont, 0, null));
						table.addCell(CreatePdf.createCell(exam_score2, CreatePdf.textfont, 0, null));
						num++;
					}
				}
				document.add(table);
				document.close();

				ToolUtil.download(filePath, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Page<Map<String, Object>> queryOnlineLessonList(String studentId, String lessonName, Integer status,
			Integer lessonType, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select lol.onlinetutor_name \"lessonName\",");
		sql.append("  lol.onlinetutor_label \"label\",");
		sql.append("  to_char(lol.onlinetutor_start, 'yyyy-MM-dd HH:mm') \"startTime\",");
		sql.append("  to_char(lol.onlinetutor_finish, 'yyyy-MM-dd HH:mm') \"endTime\",");
		sql.append("  lol.img_url \"imgUrl\",");
		sql.append("  lol.id \"id\",");
		sql.append("  lol.lesson_type \"type\",");
		sql.append("  NVL((select count(1)");
		sql.append("  from lcms_onlinetutor_user t");
		sql.append("  where t.onlinetutor_id = lol.onlinetutor_id");
		sql.append("  and t.is_into = 'Y'),");
		sql.append("  0) \"joinNum\",");
		sql.append("  (select zp from gjt_employee_info gei where gei.account_id=lol.created_by) \"teacherImg\",");
		sql.append("  (case");
		sql.append("  when lol.onlinetutor_start > sysdate then");
		sql.append("  0");
		sql.append("  when lol.onlinetutor_finish < sysdate then");
		sql.append("  2");
		sql.append("  else");
		sql.append("  1");
		sql.append("  end) \"status\"");
		sql.append("  from lcms_online_lesson lol");
		sql.append("  where lol.is_deleted = 'N'");
		sql.append("  and exists");
		sql.append("  (select 1");
		sql.append("  from lcms_onlinetutor_user t");
		sql.append("  where t.onlinetutor_id = lol.onlinetutor_id");
		sql.append("  and t.isdeleted = 'N'");
		sql.append("  and t.user_id = :stduentId)");

		params.put("stduentId", studentId);
		if (StringUtils.isNotEmpty(lessonName)) {
			sql.append(" and lol.onlinetutor_name like :lessonName");
			params.put("lessonName", "%" + lessonName + "%");
		}
		if (null == status) {

		} else if (0 == status) {// 未开始
			sql.append(" and lol.onlinetutor_start > sysdate");
		} else if (1 == status) {// 直播中
			sql.append(" and lol.onlinetutor_start<=sysdate and lol.onlinetutor_finish > sysdate");
		} else if (3 == status) {// 已结束
			sql.append(" and lol.onlinetutor_finish < sysdate");
		} else if (4 == status) {// 直播中和未开始的
			sql.append(" and lol.onlinetutor_finish > sysdate");
		}
		if (null != lessonType && (lessonType == 0 || lessonType == 1)) {
			sql.append(" and lol.lesson_type=:lessonType");
			params.put("lessonType", lessonType);
		}

		sql.append(" order by lol.onlinetutor_start desc");

		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	public static Object getMinKey(Map<Long, String> map) {
		if (map == null)
			return null;
		Set<Long> set = map.keySet();
		Object[] obj = set.toArray();
		Arrays.sort(obj);
		return obj[0];
	}
}
