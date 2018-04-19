package com.gzedu.xlims.serviceImpl.studymanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.signup.GjtSignUpInfoDataDao;
import com.gzedu.xlims.dao.studymanage.StudyManageDao;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.study.dao.course.CourseLearningDao;
import com.ouchgzee.study.dao.exam.ExamServeDao;

import net.spy.memcached.MemcachedClient;

@Service
public class StudyManageServiceImpl implements StudyManageService {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(StudyManageServiceImpl.class);

	private static final String URL_IM = AppConfig.getProperty("api.eeim.login");

	private static final String URL_TOKEN = AppConfig.getProperty("api.eeim.token");

	@Autowired
	StudyManageDao studyManageDao;

	@Autowired
	CourseLearningDao courseLearningDao;

	@Autowired
	ExamServeDao examServeDao;
	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtSignUpInfoDataDao gjtSignUpInfoDataDao;

	@Autowired
	MemcachedClient memcachedClient;

	@Autowired
	private CacheService cacheService;

	/**
	 * 学习管理=》成绩查询
	 * 
	 * @return
	 */
	@Override
	public Page getScoreList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getScoreList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》成绩查询（查询条件统计项）
	 * 
	 * @return
	 */
	public long getScoreCount(Map searchParams) {
		return studyManageDao.getScoreCount(searchParams);
	}

	/**
	 * 学习管理=》学分查询
	 * 
	 * @return
	 */
	@Override
	public Page getCreditsList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCreditsList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学分查询（查询条件统计项）
	 * 
	 * @return
	 */
	public long getCreditsCount(Map searchParams) {
		return studyManageDao.getCreditsCount(searchParams);
	}

	/**
	 * 学习管理=》课程学情
	 * 
	 * @return
	 */
	public Page getCourseStudyList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCourseStudyList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》课程班学情
	 * 
	 * @return
	 */
	public Page getCourseClassList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCourseClassList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》教学班学情
	 * 
	 * @return
	 */
	public Page getTeachClassList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getTeachClassList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学员课程学情
	 * 
	 * @return
	 */
	@Override
	public Page getStudentCourseList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getStudentCourseList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学员课程学情统计
	 *
	 * @return
	 */
	@Override
	public long getStudentCourseCount(Map searchParams) {
		return studyManageDao.getStudentCourseCount(searchParams);
	}

	/**
	 * 学习管理=》学员课程学情
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStudentCourseList(Map searchParams) {
		return studyManageDao.getStudentCourseList(searchParams);
	}

	/**
	 * 学习管理=》学员专业学情
	 * 
	 * @return
	 */
	public Page getStudentMajorList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getStudentMajorList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》教务班考勤
	 * 
	 * @return
	 */
	public Map getClassLoginList(Map searchParams, PageRequest pageRequst) {
		Map resultMap = new HashMap();
		Page page = studyManageDao.getClassLoginList(searchParams, pageRequst);
		if (EmptyUtils.isNotEmpty(page)) {
			List list = page.getContent();
			List userList = new ArrayList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map tempMap = (Map) list.get(i);
					int count = Integer.parseInt(ObjectUtils.toString(tempMap.get("STUDENT_COUNT")));
					tempMap.put("TYPE", "7");
					int count7 = studyManageDao.getClassLoginCount(tempMap);
					tempMap.put("TYPE", "3_7");
					int count3_7 = studyManageDao.getClassLoginCount(tempMap);
					tempMap.put("TYPE", "3");
					int count3 = studyManageDao.getClassLoginCount(tempMap);
					int count0 = count - count7 - count3_7 - count3;
					tempMap.put("COUNT7", count7);
					tempMap.put("COUNT3_7", count3_7);
					tempMap.put("COUNT3", count3);
					tempMap.put("COUNT0", count0);
					userList.add(tempMap);
				}
				resultMap.put("USERLIST", userList);
			}
		}
		resultMap.put("page", page);
		return resultMap;
	}

	/**
	 * 学习管理=》学员考勤
	 * 
	 * @return
	 */
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getStudentLoginList(searchParams, pageRequst);
	}

	/**
	 * 学员考勤列表统计项
	 * 
	 * @param searchParams
	 * @return
	 */
	public int getStudentLoginCount(Map searchParams) {
		return studyManageDao.getStudentLoginCount(searchParams);
	}

	/**
	 * 考勤分析=》学员考勤(考勤详情)
	 * 
	 * @return
	 */
	public Map getStudentLoginDetail(Map searchParams) {
		Map resultMap = new HashMap();
		PageRequest pageRequest = new PageRequest(0, 1, null);
		Page studentInfo = studyManageDao.getStudentLoginList(searchParams, pageRequest);// 个人信息，学习比重等
		Map studentMap = new HashMap();
		if (EmptyUtils.isNotEmpty(studentInfo)) {// 学生头部信息
			if (EmptyUtils.isNotEmpty(studentInfo.getContent())) {
				studentMap.putAll((Map) studentInfo.getContent().get(0));
			}
		}

		List resultList = studyManageDao.getStudentLoginDetail(searchParams);// 课程考勤明细

		resultMap.put("studentMap", studentMap);
		resultMap.put("resultList", resultList);
		return resultMap;
	}

	/**
	 * 考勤分析=》课程班考勤
	 * 
	 * @return
	 */
	public Page getCourseClassLoginList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCourseClassLoginList(searchParams, pageRequst);
	}

	/**
	 * 课程学情=>课程学情详情
	 *
	 * @param seachParams
	 * @return
	 */
	@Override
	public Page getCourseStudyDetails(Map<String, Object> seachParams, PageRequest pageRequst) {

		return studyManageDao.getCourseStudyDetails(seachParams, pageRequst);
	}

	/**
	 * 课程班学情=》课程班学情明细
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page getCourseClassDetail(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCourseStudyDetails(searchParams, pageRequst);
	}

	/**
	 * 处理课程学情下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadcourseStudyListExportXls(Map searchParams) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		List<Map<String, String>> resultList = studyManageDao.getCourseStudyList(searchParams);
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程名称");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("选课人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习进度");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均考试成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均总成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("已通过人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未通过人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("登记中人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未学习人数");

			if (EmptyUtils.isNotEmpty(resultList)) {
				for (Map e : resultList) {
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
					cell = row.createCell(cellIndex++);
					String time_flg = ObjectUtils.toString(e.get("TIME_FLG"));// 开课状态
					if ("1".equals(time_flg)) {
						cell.setCellValue("开课中");
					} else if ("2".equals(time_flg)) {
						cell.setCellValue("待开课");
					} else if ("3".equals(time_flg)) {
						cell.setCellValue("已结束");
					} else {
						cell.setCellValue("已结束");
					}
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("REC_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_SCHEDULE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_TIME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_STUDY_SCORE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_EXAM_SCORE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_TOTAL_SCORE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_PASS_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_UNPASS_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_STUDY_IMG")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_REGISTER_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_NEVER_STUDY")));

				}
			}
		}
		return wb;
	}

	@Override
	public Page getCourseLoginList(Map searchParams, PageRequest pageRequst) {

		return studyManageDao.getCourseLoginList(searchParams, pageRequst);
	}

	/**
	 * 根据开课状态返回考勤统计数
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public long getCourseLoginCount(Map searchParams) {
		return studyManageDao.getCourseLoginCount(searchParams);
	}

	/**
	 * 考勤分析--》课程考勤下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadcourseLoginListExportXls(Map searchParams) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		List<Map<String, String>> resultList = studyManageDao.getCourseLoginList(searchParams);
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("选课人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("7天以上未学习人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("3天以上未学习人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("3天以内未学习人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("从未学习人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("当前在学人数");

			if (EmptyUtils.isNotEmpty(resultList)) {
				for (Map e : resultList) {
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
					cell = row.createCell(cellIndex++);
					String time_flg = ObjectUtils.toString(e.get("TIME_FLG"));// 开课状态
					if ("1".equals(time_flg)) {
						cell.setCellValue("开课中");
					} else if ("2".equals(time_flg)) {
						cell.setCellValue("待开课");
					} else if ("3".equals(time_flg)) {
						cell.setCellValue("已结束");
					} else {
						cell.setCellValue("已结束");
					}
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("REC_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_LOGIN_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_COUNT")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("DAY7_LOGIN")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("DAY3_7_LOGIN")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("DAY3_LOGIN")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("NO_DAY_LOGIN")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("ONLINE_STUDENT_COUNT")));
				}
			}
		}
		return wb;
	}

	/**
	 * 考勤分析--》课程考勤--》课程考情详情
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page getCourseClockingDetail(Map searchParams, PageRequest pageRequst) {

		return studyManageDao.getCourseClockingDetail(searchParams, pageRequst);
	}

	
	/* (non-Javadoc)
	 * 指定设备的考勤数量
	 * @see com.gzedu.xlims.service.studymanage.StudyManageService#getCourseClockingDetailCount(java.util.Map)
	 */
	@Override
	public long getCourseClockingDetailCount(Map searchParams) {
		return studyManageDao.getCourseClockingDetailCount(searchParams);
	}
	
	
	/**
	 * 考勤分析--》课程考勤--》课程考情详情下载
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadClockingDetailExportXls(Map searchParams) {

		List<Map<String, String>> resultList = studyManageDao.getCourseClockingDetail(searchParams);
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("报读专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习时长（小时）");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("主要应用终端");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习日期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习状态");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				int app_count = Integer.valueOf(ObjectUtils.toString(e.get("APP_ONLINE_COUNT"), "0"));
				int pc_count = Integer.valueOf(ObjectUtils.toString(e.get("PC_ONLINE_COUNT"), "0"));
				if (app_count > pc_count) {
					cell.setCellValue(ObjectUtils.toString("APP"));
				} else if (app_count <= pc_count && pc_count != 0) {
					cell.setCellValue(ObjectUtils.toString("PC"));
				} else {
					cell.setCellValue("");
				}
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LAST_DATE")));
				cell = row.createCell(cellIndex++);
				String isOnline = ObjectUtils.toString(e.get("IS_ONLINE"), "");
				if ("0".equals(ObjectUtils.toString(e.get("LOGIN_COUNT")))) {
					cell.setCellValue("从未学习");
				} else {
					if ("".equals(isOnline)) {
						cell.setCellValue("从未学习");
					} else if ("N".equals(isOnline)) {
						cell.setCellValue("离线");
					} else if ("Y".equals(isOnline)) {
						cell.setCellValue("在线");
					}
				}
			}
		}
		return wb;
	}

	/**
	 * 考勤分析--》学员课程考勤--》学员课程考勤详情下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadStudentDetailExportXls(Map searchParams) {

		Map resultMap = getStudentLoginDetail(searchParams);
		Map studentInfo = new HashMap();
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		if (EmptyUtils.isNotEmpty(resultMap)) {
			studentInfo = (Map) resultMap.get("studentMap");
			resultList = (List<Map<String, String>>) resultMap.get("resultList");
		}

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习时长（小时）");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("主要应用终端");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习日期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习状态");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("XH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				int app_count = Integer.valueOf(ObjectUtils.toString(e.get("APP_ONLINE_COUNT"), "0"));
				int pc_count = Integer.valueOf(ObjectUtils.toString(e.get("PC_ONLINE_COUNT"), "0"));
				if (app_count > pc_count) {
					cell.setCellValue(ObjectUtils.toString("APP"));
				} else if (app_count <= pc_count && pc_count != 0) {
					cell.setCellValue(ObjectUtils.toString("PC"));
				} else {
					cell.setCellValue("");
				}
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LAST_DATE")));
				cell = row.createCell(cellIndex++);
				String isOnline = ObjectUtils.toString(e.get("IS_ONLINE"), "");
				if ("0".equals(ObjectUtils.toString(e.get("LOGIN_COUNT")))) {
					cell.setCellValue("从未学习");
				} else {
					if ("".equals(isOnline)) {
						cell.setCellValue("从未学习");
					} else if ("N".equals(isOnline)) {
						cell.setCellValue("离线");
					} else if ("Y".equals(isOnline)) {
						cell.setCellValue("在线");
					}
				}
			}
		}
		return wb;
	}

	/**
	 * 考勤分析--》学员课程考勤--》学员课程考勤列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadStudentLoginListExportXls(Map searchParams) {
		List<Map<String, String>> resultList = studyManageDao.getStudentLoginList(searchParams);
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学员类型");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学籍状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("报读专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("教务班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("登录次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("登录时长（小时）");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("主要应用终端");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("PC端登陆次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("APP登录次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("APP端登录占比");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习日期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("当前学习状态");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, ObjectUtils.toString(e.get("USER_TYPE"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, ObjectUtils.toString(e.get("XJZT"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIMES")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME_COUNT")));
				cell = row.createCell(cellIndex++);
				int app_count = Integer.valueOf(ObjectUtils.toString(e.get("APP_ONLINE_COUNT"), "0"));
				int pc_count = Integer.valueOf(ObjectUtils.toString(e.get("PC_ONLINE_COUNT"), "0"));
				if (app_count > pc_count) {
					cell.setCellValue(ObjectUtils.toString("APP"));
				} else if (app_count <= pc_count && pc_count != 0) {
					cell.setCellValue(ObjectUtils.toString("PC"));
				} else {
					cell.setCellValue("");
				}
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PC_ONLINE_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("APP_ONLINE_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("APP_ONLINE_PERCENT"), "0") + "%");
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("MAX_LAST_LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				String isOnline = ObjectUtils.toString(e.get("IS_ONLINE"), "");
				if ("Y".equals(isOnline)) {
					cell.setCellValue("在线");
				} else if ("N".equals(isOnline)) {
					if ("0".equals(ObjectUtils.toString(e.get("LOGIN_TIMES")))) {
						cell.setCellValue("从未学习");
					} else {
						cell.setCellValue("离线");
					}
				} else if ("".equals(isOnline)) {
					cell.setCellValue("从未学习");
				}
			}
		}
		return wb;
	}

	/**
	 * 考勤分析--》课程班考勤列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadCourseClassListExportXls(Map searchParams) {
		List<Map<String, String>> resultList = studyManageDao.getCourseClassLoginList(searchParams);
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("班级人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("7天以上未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("3天以上未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("3天以内未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("从未学习人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("当前学习人数");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_LOGIN_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DAY7_LOGIN")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DAY3_7_LOGIN")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DAY3_LOGIN")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("NO_DAY_LOGIN")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(Integer.valueOf(ObjectUtils.toString(e.get("ONLINE_STUDENT_COUNT"), "0")));
			}
		}
		return wb;
	}

	@Override
	public Page<Map<String, Object>> getStudentStudyList(Map<String, Object> searchParams, PageRequest pageRequst) {
		return getStudentStudyList(searchParams, null, pageRequst);
	}

	@Override
	public Page<Map<String, Object>> getStudentStudyList(Map<String, Object> searchParams, Map<String, String> orderMap,
			PageRequest pageRequst) {
		return studyManageDao.getStudentStudyList(searchParams, orderMap, pageRequst);
	}

	/**
	 * 根据手机号查询学员身份证、姓名、学号
	 * 
	 * @param searchParams
	 * @return
	 */
	public Map getStudentBaseInfo(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			String sjh = ObjectUtils.toString(searchParams.get("sjh"), "").trim();
			if (EmptyUtils.isEmpty(sjh)) {
				resultMap.put("result", "fail");
				resultMap.put("message", "手机号不能为空！");
				return resultMap;
			}

			List list = studyManageDao.getStudentBaseInfo(searchParams);
			List stuList = new ArrayList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map listMap = (Map) list.get(i);

					Map stuMap = new LinkedHashMap();
					stuMap.put("sfzh", ObjectUtils.toString(listMap.get("SFZH"), ""));
					stuMap.put("xm", ObjectUtils.toString(listMap.get("XM"), ""));
					stuMap.put("xh", ObjectUtils.toString(listMap.get("XH"), ""));
					stuMap.put("xjzt", ObjectUtils.toString(listMap.get("XJZT"), ""));
					stuMap.put("atId", ObjectUtils.toString(listMap.get("ATID"), ""));
					stuList.add(stuMap);
				}
			}

			resultMap.put("result", "success");
			resultMap.put("STU_LIST", stuList);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "fail");
			resultMap.put("message", e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 兼容专本连读情况,返回多条数据
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Map countStudentLearnInfo(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
			String sfzh = ObjectUtils.toString(searchParams.get("sfzh"), "").trim();
			if (EmptyUtils.isEmpty(sfzh)) {
				resultMap.put("result", "fail");
				resultMap.put("message", "身份证号不能为空！");
				return resultMap;
			}

			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("CHOOSE_SFZH_" + sfzh)))) {
				return (Map) memcachedClient.get("CHOOSE_SFZH_" + sfzh);
			} else {
				// *************1.学员报读专业名称开始*************//
				List specialList = new ArrayList();
				List listStu = studyManageDao.getMoreSpecialtyInfo(searchParams);
				if (EmptyUtils.isNotEmpty(listStu)) {
					for (int i = 0; i < listStu.size(); i++) {
						Map tMap = (Map) listStu.get(i);
						tMap.put("MZ", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.NATIONALITYCODE, (String) tMap.get("MZM")));
						String provinceCode = (String) tMap.get("PROVINCE_CODE");
						if (provinceCode != null) {
							tMap.put("PROVINCE_NAME", com.gzedu.xlims.common.Objects.toString(
									cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE, provinceCode), ""));
							String cityCode = (String) tMap.get("CITY_CODE");
							if (cityCode != null) {
								tMap.put("CITY_NAME", com.gzedu.xlims.common.Objects.toString(
										cacheService.getCachedDictionaryName(
												CacheService.DictionaryKey.CITY.replace("${Province}", provinceCode), cityCode),
										""));
								tMap.put("AREA_NAME", Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.AREA
												.replace("${Province}", provinceCode).replace("${City}", cityCode), (String) tMap.get("AREA_CODE")),
										""));
							}
						}

						// 单点登录参数
						String loginUrl = gjtUserAccountService.studentSimulation(
								ObjectUtils.toString(tMap.get("STUDENT_ID")), ObjectUtils.toString(tMap.get("XH")));

						Map specialMap = new LinkedHashMap();
						specialMap.put("STUDENT_ID", ObjectUtils.toString(tMap.get("STUDENT_ID")));
						specialMap.put("XJZT", ObjectUtils.toString(tMap.get("XJZT")));
						specialMap.put("SPECIALTY_ID", ObjectUtils.toString(tMap.get("SPECIALTY_ID")));
						specialMap.put("ZYMC", ObjectUtils.toString(tMap.get("ZYMC")));
						specialMap.put("PYCC", ObjectUtils.toString(tMap.get("PYCC")));
						specialMap.put("CHARGE", ObjectUtils.toString(tMap.get("CHARGE")));
						specialMap.put("XM", ObjectUtils.toString(tMap.get("XM")));
						specialMap.put("XH", ObjectUtils.toString(tMap.get("XH")));
						specialMap.put("SJH", ObjectUtils.toString(tMap.get("SJH")));
						specialMap.put("EENO", ObjectUtils.toString(tMap.get("EENO")));
						specialMap.put("ATID", ObjectUtils.toString(tMap.get("ATID")));
						specialMap.put("SFZH", ObjectUtils.toString(tMap.get("SFZH")));
						specialMap.put("BIRTHDAY", ObjectUtils.toString(tMap.get("BIRTHDAY")));
						specialMap.put("EMAIL", ObjectUtils.toString(tMap.get("EMAIL")));
						specialMap.put("COMPANY", ObjectUtils.toString(tMap.get("COMPANY")));
						specialMap.put("CLASS_TYPE", ObjectUtils.toString(tMap.get("CLASS_TYPE")));
						specialMap.put("FIRSTLOGIN", ObjectUtils.toString(tMap.get("FIRSTLOGIN")));
						specialMap.put("AVATAR", ObjectUtils.toString(tMap.get("AVATAR")));
						specialMap.put("HKSZD", ObjectUtils.toString(tMap.get("HKSZD")));
						specialMap.put("SCCO", ObjectUtils.toString(tMap.get("SCCO")));
						specialMap.put("WEIXIN", ObjectUtils.toString(tMap.get("WEIXIN")));
						specialMap.put("QQ", ObjectUtils.toString(tMap.get("QQ")));
						specialMap.put("PRE_ZYMC", ObjectUtils.toString(tMap.get("PRE_ZYMC")));
						specialMap.put("XBM", ObjectUtils.toString(tMap.get("XBM")));
						specialMap.put("CONFIRM", ObjectUtils.toString(tMap.get("CONFIRM")));
						specialMap.put("USER_TYPE", ObjectUtils.toString(tMap.get("USER_TYPE")));
						specialMap.put("LXDH", ObjectUtils.toString(tMap.get("LXDH")));
						specialMap.put("ZYH", ObjectUtils.toString(tMap.get("ZYH")));
						specialMap.put("BH", ObjectUtils.toString(tMap.get("BH")));
						specialMap.put("ADDRESS", ObjectUtils.toString(tMap.get("ADDRESS")));
						specialMap.put("MZ", ObjectUtils.toString(tMap.get("MZ")));
						specialMap.put("JG", ObjectUtils.toString(tMap.get("JG")));
						specialMap.put("USER_ID", ObjectUtils.toString(tMap.get("USER_ID")));
						specialMap.put("XXZX_ID", ObjectUtils.toString(tMap.get("XXZX_ID")));
						specialMap.put("XX_NAME", ObjectUtils.toString(tMap.get("XX_NAME")));
						specialMap.put("FX_NAME", ObjectUtils.toString(tMap.get("FX_NAME")));
						specialMap.put("XXZX_NAME", ObjectUtils.toString(tMap.get("XXZX_NAME")));

						final String studetId = ObjectUtils.toString(tMap.get("STUDENT_ID"));
						Map<String, Object> imObj = null;
						ExecutorService threadPool = null;
						try {
							threadPool = Executors.newSingleThreadExecutor();
							Future<Map<String, Object>> submit = threadPool.submit(new Callable<Map<String, Object>>() {
								@Override
								public Map<String, Object> call() throws Exception {
									return loginWithoutPassword(studetId);
								}
							});
							imObj = submit.get(3, TimeUnit.SECONDS);
						} catch (Throwable e) {
							log.error("线程创建失败或调用EE接口请求超时！");
							e.printStackTrace();
						} finally {
							if(threadPool != null && !threadPool.isShutdown()) {
								threadPool.shutdown();
							}
						}
						specialMap.put("imObj", imObj);

						specialMap.put("PROVINCE_ID", ObjectUtils.toString(tMap.get("PROVINCE_ID")));
						specialMap.put("PROVINCE_NAME", ObjectUtils.toString(tMap.get("PROVINCE_NAME")));
						specialMap.put("CITY_ID", ObjectUtils.toString(tMap.get("CITY_ID")));
						specialMap.put("CITY_NAME", ObjectUtils.toString(tMap.get("CITY_NAME")));
						specialMap.put("AREA_ID", ObjectUtils.toString(tMap.get("AREA_ID")));
						specialMap.put("AREA_NAME", ObjectUtils.toString(tMap.get("AREA_NAME")));
						specialMap.put("CLAZZ", ObjectUtils.toString(tMap.get("CLAZZ")));
						specialMap.put("TEACHING_CLASS_NAME", ObjectUtils.toString(tMap.get("TEACHING_CLASS_NAME")));
						specialMap.put("AUDIT_STATE", ObjectUtils.toString(tMap.get("AUDIT_STATE")));
						specialMap.put("APPID", ObjectUtils.toString(tMap.get("APPID")));
						specialMap.put("loginUrl", loginUrl);

						searchParams.put("STUDENT_ID", ObjectUtils.toString(tMap.get("STUDENT_ID")));
						searchParams.put("XX_ID", ObjectUtils.toString(tMap.get("XX_ID")));
						// *************2.学员当前学期开始*************//
						String ruXue = null;
						List ruxueTerm = studyManageDao.getRuXueTerm(searchParams);
						if (EmptyUtils.isNotEmpty(ruxueTerm)) {
							Map ruXueMap = (Map) ruxueTerm.get(0);
							ruXue = ObjectUtils.toString(ruXueMap.get("GRADE_NAME"));
						}
						List terms = studyManageDao.getTermListByLoginStudent(searchParams); // 学期列表
						List indexList = studyManageDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
						if (EmptyUtils.isNotEmpty(indexList)) {
							Map map = (Map) indexList.get(0);
							int index = Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")));
							if (index <= 5) {
								if (EmptyUtils.isNotEmpty(terms)) {
									String term_id = ObjectUtils.toString(map.get("GRADE_ID"));
									for (int j = 0; j < terms.size(); j++) {
										Map tMap1 = (Map) terms.get(j);
										if (ObjectUtils.toString(tMap1.get("TERM_ID")).equals(term_id)) {
											specialMap.put("TERMINDEX", ObjectUtils.toString(tMap1.get("KKXQ")));
											// dataMap.put("TERM_NAME",ObjectUtils.toString(tMap.get("GRADE_NAME")));
											// 修改为查询学员入学学期
											specialMap.put("TERM_NAME", ruXue);
											searchParams.put("TERM_ID", ObjectUtils.toString(tMap1.get("TERM_ID")));
											break;
										} else {
											specialMap.put("TERMINDEX", ObjectUtils.toString(map.get("GRADE_INDEX")));
											// dataMap.put("TERM_NAME",ObjectUtils.toString(map.get("GRADE_NAME")));
											specialMap.put("TERM_NAME", ruXue);
											Map termMap = (Map) terms.get(terms.size() - 1);
											searchParams.put("TERM_ID", ObjectUtils.toString(termMap.get("TERM_ID")));
										}
									}
								}
							} else {
								if (EmptyUtils.isNotEmpty(terms)) {
									specialMap.put("TERMINDEX", terms.size());
									Map temp = (Map) terms.get(terms.size() - 1);
									// dataMap.put("TERM_NAME",ObjectUtils.toString(temp.get("GRADE_NAME")));
									specialMap.put("TERM_NAME", ruXue);
									Map termMap1 = (Map) terms.get(terms.size() - 1);
									searchParams.put("TERM_ID", ObjectUtils.toString(termMap1.get("TERM_ID")));
								} else {
									specialMap.put("TERMINDEX", "1");
									specialMap.put("TERM_NAME", ruXue);
								}
							}
						} else {
							//查不到当前学期时，根据学期列表中的学期结束时间与当前时间进行对比，查找离当前时间最近的数据，取对应的开课学期KKXQ
							if(terms != null && terms.size() > 0) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								String current = sdf.format(new Date());
								Date currentDate = sdf.parse(current);
								List listDate = new ArrayList();
								List<Long> listIndex = new ArrayList<Long>();
								for (int m=0;m<terms.size();m++){
									Map dateMap = (Map) terms.get(m);
									String strEnd = (String) dateMap.get("END_DATE");
									Date endDate = sdf.parse(strEnd);
									long days = (endDate.getTime()-currentDate.getTime())/(1000*3600*24);
									Map map = new HashMap();
									map.put("days",Math.abs(days));
									map.put("kkxq",ObjectUtils.toString(dateMap.get("KKXQ")));
									listDate.add(map);
									listIndex.add(Math.abs(days));
								}
								Long longIndex = Collections.min(listIndex);
								String termIndex = null;
								for (int j=0;j<listDate.size();j++){
									Map map1 = (Map) listDate.get(j);
									Long days = (Long)map1.get("days");
									if (longIndex.equals(days)){
										termIndex = (String) map1.get("kkxq");
									}
								}
								specialMap.put("TERMINDEX", termIndex);
							}
							specialMap.put("TERM_NAME", ruXue);
						}
						// *************2.学员当前学期结束*************//
						searchParams.remove("EXAM_STATE");
						// *************所报读课程学习进度开始*************//
						List teachProgess = courseLearningDao.queryTeachPlan(searchParams);
						List progessList = new ArrayList();
						if (EmptyUtils.isNotEmpty(teachProgess)) {
							for (int k = 0; k < teachProgess.size(); k++) {
								Map tProgessMap = (Map) teachProgess.get(k);

								Map progessMap = new LinkedHashMap();

								progessMap.put("COURSE_ID", ObjectUtils.toString(tProgessMap.get("COURSE_ID")));
								;
								progessMap.put("COURSE_NAME", ObjectUtils.toString(tProgessMap.get("COURSE_NAME")));
								progessMap.put("SCHEDULE", ObjectUtils.toString(tProgessMap.get("SCHEDULE")));
								progessList.add(progessMap);
							}
						}
						specialMap.put("PROGESS_LIST", progessList);
						// *************所报读课程学习进度结束*************//
						// *************3.学员已获得学分/总学分，6.当前学习课程数/总课程数
						// 开始*************//
						searchParams.remove("TERM_ID");
						List teachPlans = courseLearningDao.queryTeachPlan(searchParams);
						Map tempMap = null;
						double sumScore = 0;
						if (EmptyUtils.isNotEmpty(teachPlans)) {
							for (int m = 0; m < teachPlans.size(); m++) {
								tempMap = (Map) teachPlans.get(m);
								sumScore += Double.parseDouble(ObjectUtils.toString(tempMap.get("CREDIT")));
							}
						}
						specialMap.put("TOTALCOURSE", EmptyUtils.isNotEmpty(teachPlans) ? teachPlans.size() : 0); // 总课程数
						specialMap.put("TOTALCREDIT", sumScore); // 总学分

						searchParams.put("EXAM_STATE", "2"); // 学习中
						List learning = courseLearningDao.queryTeachPlan(searchParams);
						specialMap.put("LEARNINGCOUNT", EmptyUtils.isNotEmpty(learning) ? learning.size() : 0); // 学习中课程数

						searchParams.put("EXAM_STATE", "1"); // 已通过
						List pass = courseLearningDao.queryTeachPlan(searchParams);
						Map passMap = null;
						int passScore = 0;
						if (EmptyUtils.isNotEmpty(pass)) {
							for (int n = 0; n < pass.size(); n++) {
								passMap = (Map) pass.get(n);
								passScore += Integer.parseInt(ObjectUtils.toString(passMap.get("CREDIT")));
							}
						}
						specialMap.put("PASSCREDIT", passScore); // 已获学分
						searchParams.remove("EXAM_STATE");
						// *************3.学员已获得学分/总学分，6.当前学习课程数/总课程数
						// 结束*************//

						// 待预约考试科目 20171211登录查这么多数据太慢了就不查考试的了
						specialMap.put("PENDING_COUNT", Constants.BOOLEAN_0);
						specialMap.put("DKS_COUNT", Constants.BOOLEAN_0); // 待考试

						specialList.add(specialMap);
					}

				} else {
					resultMap.put("result", "fail");
					resultMap.put("message", "学员信息不存在！");
					if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("CHOOSE_SFZH_" + sfzh)))) {
						memcachedClient.delete("CHOOSE_SFZH_" + sfzh);
					}
					return resultMap;
				}

				resultMap.put("result", "success");
				resultMap.put("data", specialList);

				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("CHOOSE_SFZH_" + sfzh)))) {
					memcachedClient.delete("CHOOSE_SFZH_" + sfzh);
				}
				memcachedClient.add("CHOOSE_SFZH_" + sfzh, 600, resultMap);

			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "fail");
			resultMap.put("message", e.getMessage());
			if (EmptyUtils.isNotEmpty(ObjectUtils
					.toString(memcachedClient.get("CHOOSE_SFZH_" + ObjectUtils.toString(searchParams.get("sfzh")))))) {
				memcachedClient.delete("CHOOSE_SFZH_" + ObjectUtils.toString(searchParams.get("sfzh")));
			}
		}
		return resultMap;
	}

	@Override
	public Map countStudyInfo(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			Map<String, Object> dataMap = new LinkedHashMap<String, Object>();

			String atId = ObjectUtils.toString(searchParams.get("atId"), "").trim();
			if (EmptyUtils.isEmpty(atId)) {
				resultMap.put("result", "fail");
				resultMap.put("message", "atId参数不能为空！");
				return resultMap;
			}

			List stuList = studyManageDao.getStudentId(searchParams);
			String studentId = null;
			String xh = null;
			if (EmptyUtils.isNotEmpty(stuList)) {
				Map stuMap = (Map) stuList.get(0);
				studentId = ObjectUtils.toString(stuMap.get("STUDENT_ID"));
				xh = ObjectUtils.toString(stuMap.get("XH"));
			} else {
				resultMap.put("result", "fail");
				resultMap.put("message", "学员信息不存在！");
				return resultMap;
			}

			searchParams.put("STUDENT_ID", studentId);

			// *************1.学员报读专业名称开始*************//
			List specialtyList = studyManageDao.getSpecialtyInfo(searchParams);
			if (EmptyUtils.isNotEmpty(specialtyList)) {
				Map tspecMap = (Map) specialtyList.get(0);
				dataMap.put("ZYMC", ObjectUtils.toString(tspecMap.get("ZYMC")));
				searchParams.put("XX_ID", ObjectUtils.toString(tspecMap.get("XX_ID")));
			}
			// *************1.学员报读专业名称结束*************//

			// *************2.学员当前学期开始*************//
			String ruXue = null;
			List ruxueTerm = studyManageDao.getRuXueTerm(searchParams);
			if (EmptyUtils.isNotEmpty(ruxueTerm)) {
				Map ruXueMap = (Map) ruxueTerm.get(0);
				ruXue = ObjectUtils.toString(ruXueMap.get("GRADE_NAME"));
			}

			List terms = studyManageDao.getTermListByLoginStudent(searchParams); // 学期列表
			List indexList = studyManageDao.getTermListIndex(searchParams); // 定位当前学员在第几个开课学期
			if (EmptyUtils.isNotEmpty(indexList)) {
				Map map = (Map) indexList.get(0);
				int index = Integer.parseInt(ObjectUtils.toString(map.get("GRADE_INDEX")));
				if (index <= 5) {
					if (EmptyUtils.isNotEmpty(terms)) {
						String term_id = ObjectUtils.toString(map.get("GRADE_ID"));
						for (int i = 0; i < terms.size(); i++) {
							Map tMap = (Map) terms.get(i);
							if (ObjectUtils.toString(tMap.get("TERM_ID")).equals(term_id)) {
								dataMap.put("TERMINDEX", ObjectUtils.toString(tMap.get("KKXQ")));
								// dataMap.put("TERM_NAME",ObjectUtils.toString(tMap.get("GRADE_NAME")));
								// 修改为查询学员入学学期
								dataMap.put("TERM_NAME", ruXue);
								searchParams.put("TERM_ID", ObjectUtils.toString(tMap.get("TERM_ID")));
							} else {
								dataMap.put("TERMINDEX", ObjectUtils.toString(map.get("GRADE_INDEX")));
								// dataMap.put("TERM_NAME",ObjectUtils.toString(map.get("GRADE_NAME")));
								dataMap.put("TERM_NAME", ruXue);
								searchParams.put("TERM_ID", ObjectUtils.toString(map.get("GRADE_ID")));
							}
						}
					}
				} else {
					if (EmptyUtils.isNotEmpty(terms)) {
						dataMap.put("TERMINDEX", terms.size());
						Map temp = (Map) terms.get(terms.size() - 1);
						// dataMap.put("TERM_NAME",ObjectUtils.toString(temp.get("GRADE_NAME")));
						dataMap.put("TERM_NAME", ruXue);
						searchParams.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
					} else {
						dataMap.put("TERMINDEX", "1");
					}
				}
			} else {
				dataMap.put("TERMINDEX", "1");
			}
			// *************2.学员当前学期结束*************//
			searchParams.remove("EXAM_STATE");

			// *************所报读课程学习进度开始*************//
			List teachProgess = courseLearningDao.queryTeachPlan(searchParams);
			List progessList = new ArrayList();

			if (EmptyUtils.isNotEmpty(teachProgess)) {
				for (int i = 0; i < teachProgess.size(); i++) {
					Map tProgessMap = (Map) teachProgess.get(i);

					Map progessMap = new LinkedHashMap();

					progessMap.put("COURSE_ID", ObjectUtils.toString(tProgessMap.get("COURSE_ID")));
					;
					progessMap.put("COURSE_NAME", ObjectUtils.toString(tProgessMap.get("COURSE_NAME")));
					progessMap.put("SCHEDULE", ObjectUtils.toString(tProgessMap.get("SCHEDULE")));
					progessList.add(progessMap);
				}
			}

			dataMap.put("PROGESS_LIST", progessList);
			// *************所报读课程学习进度结束*************//

			// *************3.学员已获得学分/总学分，6.当前学习课程数/总课程数 开始*************//

			// *************3.学员已获得学分/总学分，6.当前学习课程数/总课程数 开始*************//
			searchParams.remove("TERM_ID");
			List teachPlans = courseLearningDao.queryTeachPlan(searchParams);
			Map tempMap = null;
			int sumScore = 0;
			if (EmptyUtils.isNotEmpty(teachPlans)) {
				for (int i = 0; i < teachPlans.size(); i++) {
					tempMap = (Map) teachPlans.get(i);
					sumScore += Integer.parseInt(ObjectUtils.toString(tempMap.get("CREDIT")));
				}
			}

			dataMap.put("TOTALCOURSE", EmptyUtils.isNotEmpty(teachPlans) ? teachPlans.size() : 0); // 总课程数
			dataMap.put("TOTALCREDIT", sumScore); // 总学分

			searchParams.put("EXAM_STATE", "2"); // 学习中
			List learning = courseLearningDao.queryTeachPlan(searchParams);
			dataMap.put("LEARNINGCOUNT", EmptyUtils.isNotEmpty(learning) ? learning.size() : 0); // 学习中课程数

			searchParams.put("EXAM_STATE", "1"); // 已通过
			List pass = courseLearningDao.queryTeachPlan(searchParams);
			Map passMap = null;
			int passScore = 0;
			if (EmptyUtils.isNotEmpty(pass)) {
				for (int i = 0; i < pass.size(); i++) {
					passMap = (Map) pass.get(i);
					passScore += Integer.parseInt(ObjectUtils.toString(passMap.get("CREDIT")));
				}
			}
			dataMap.put("PASSCREDIT", passScore); // 已获学分
			searchParams.remove("EXAM_STATE");
			// *************3.学员已获得学分/总学分，6.当前学习课程数/总课程数 结束*************//

			// 单点登录参数
			String loginUrl = gjtUserAccountService.studentSimulation(studentId, xh);
			dataMap.put("loginUrl", loginUrl);

			// *************4.学员待预约考试数，5.学员待考试科目数 开始*************//

			// 4.学员待预约考试数
			searchParams.put("CURRENT_FLAG", "1");
			List current_plan = examServeDao.getExamBatchData(searchParams); // 查询当前考试计划
			String exam_batch_code = null;
			String end_date = null;
			String term_id = null;
			if (EmptyUtils.isNotEmpty(current_plan)) {
				Map currentMap = (Map) current_plan.get(0);
				exam_batch_code = ObjectUtils.toString(currentMap.get("EXAM_BATCH_CODE"));
				end_date = ObjectUtils.toString(currentMap.get("END_DATE"));
				term_id = ObjectUtils.toString(currentMap.get("GRADE_ID"));
			}
			searchParams.remove("CURRENT_FLAG");
			List<Object> recList = new ArrayList<Object>();
			if (EmptyUtils.isNotEmpty(exam_batch_code)) {
				searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
				searchParams.put("BESPEAK_STATE", 0);
				searchParams.put("END_DATE", end_date);
				List list_course = examServeDao.getChooseCourseByStudent(searchParams);
				if (EmptyUtils.isNotEmpty(list_course)) {
					for (int i = 0; i < list_course.size(); i++) {
						Map recMap = (Map) list_course.get(i);
						searchParams.put("COURSE_ID", ObjectUtils.toString(recMap.get("COURSE_ID")));
						searchParams.put("APPOINTMENT_FLAG", "1");
						searchParams.put("TERM_ID", term_id);
						List planAndCourse = examServeDao.getExamPlanAndCourseInfo(searchParams);
						if (EmptyUtils.isNotEmpty(planAndCourse)) {
							for (int j = 0; j < planAndCourse.size(); j++) {
								Map tempPlan = (Map) planAndCourse.get(j);
								Map temp3 = new LinkedHashMap();
								temp3.put("TERM_CODE", ObjectUtils.toString(recMap.get("TERM_CODE")));
								temp3.put("TERM_ID", ObjectUtils.toString(recMap.get("TERM_ID")));
								temp3.put("TERM_NAME", ObjectUtils.toString(recMap.get("TERM_NAME")));
								temp3.put("COURSE_ID", ObjectUtils.toString(recMap.get("COURSE_ID")));
								temp3.put("COURSE_NAME", ObjectUtils.toString(recMap.get("COURSE_NAME")));
								temp3.put("TYPE", ObjectUtils.toString(tempPlan.get("TYPE")));
								temp3.put("KCH", ObjectUtils.toString(recMap.get("KCH")));
								temp3.put("COURSE_COST", ObjectUtils.toString(recMap.get("COURSE_COST")));
								temp3.put("MAKEUP", ObjectUtils.toString(recMap.get("MAKEUP")));
								temp3.put("PAY_STATE", ObjectUtils.toString(recMap.get("PAY_STATE")));
								temp3.put("BESPEAK_STATE", ObjectUtils.toString(recMap.get("BESPEAK_STATE")));
								recList.add(temp3);
							}
						}
					}
				}
			}
			// 删除重复数据(根据COURSE_ID相同的)
			for (int i = 0; i < recList.size(); i++) {
				Map map1 = (Map) recList.get(i);
				for (int j = recList.size() - 1; j > i; j--) {
					Map map2 = (Map) recList.get(j);
					if (ObjectUtils.toString(map1.get("COURSE_ID"))
							.equals(ObjectUtils.toString(map2.get("COURSE_ID")))) {
						recList.remove(j);
					}
				}
			}
			List<Object> planList = new ArrayList<Object>();
			for (int i = 0; i < recList.size(); i++) {
				Map tempPlan = (Map) recList.get(i);
				Map planMap = new LinkedHashMap();
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
							planMap.put("TERM_ID", ObjectUtils.toString(tempPlan.get("TERM_ID")));
							planMap.put("TERM_NAME", ObjectUtils.toString(tempPlan.get("TERM_NAME")));
							planMap.put("COURSE_ID", ObjectUtils.toString(tempPlan.get("COURSE_ID")));
							planMap.put("COURSE_NAME", ObjectUtils.toString(tempPlan.get("COURSE_NAME")));
							planMap.put("KCH", ObjectUtils.toString(tempPlan.get("KCH")));
							planMap.put("COURSE_COST", ObjectUtils.toString(tempPlan.get("COURSE_COST")));
							planMap.put("MAKEUP", ObjectUtils.toString(tempPlan.get("MAKEUP")));
							planMap.put("PAY_STATE", ObjectUtils.toString(tempPlan.get("PAY_STATE")));
							planMap.put("BESPEAK_STATE", ObjectUtils.toString(tempPlan.get("BESPEAK_STATE")));
							planList.add(planMap);
							break;
						}
					}
				}
			}
			int pending_count = 0;
			for (int i = 0; i < planList.size(); i++) {
				Map tMap = (Map) planList.get(i);
				if ("0".equals(ObjectUtils.toString(tMap.get("BESPEAK_STATE")))) {
					pending_count = pending_count + 1;
				}
			}
			// 待预约考试科目
			dataMap.put("PENDING_COUNT", ObjectUtils.toString(pending_count));
			searchParams.remove("APPOINTMENT_FLAG");
			searchParams.remove("COURSE_ID");
			// 5.学员待考试科目数
			List<Object> myPlanList = new ArrayList<Object>();
			List myPlanAndCourse = examServeDao.getExamPlanAndCourseInfo(searchParams);
			if (EmptyUtils.isNotEmpty(myPlanAndCourse)) {
				for (int i = 0; i < myPlanAndCourse.size(); i++) {
					Map tempPlan = (Map) myPlanAndCourse.get(i);

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
								myPlanList.add(viewCourseMap(tempPlan));
								break;
							}
						}
					}
				}
			}

			searchParams.put("BESPEAK_STATE", "1");
			List<Object> myExamResult = new ArrayList<Object>();
			List myExamList = examServeDao.getChooseCourseByStudent(searchParams);
			if (EmptyUtils.isNotEmpty(myExamList)) {
				for (int i = 0; i < myExamList.size(); i++) {
					Map temp1 = (Map) myExamList.get(i);
					for (int j = 0; j < myPlanList.size(); j++) {
						Map temp2 = (Map) myPlanList.get(j);

						Map temp3 = new LinkedHashMap();

						if (ObjectUtils.toString(temp1.get("COURSE_ID"))
								.equals(ObjectUtils.toString(temp2.get("COURSE_ID")))) {
							temp3.put("TERM_CODE", ObjectUtils.toString(temp1.get("TERM_CODE")));
							temp3.put("TERM_ID", ObjectUtils.toString(temp1.get("TERM_ID")));
							temp3.put("TERM_NAME", ObjectUtils.toString(temp1.get("TERM_NAME")));
							temp3.put("COURSE_ID", ObjectUtils.toString(temp1.get("COURSE_ID")));
							temp3.put("COURSE_NAME", ObjectUtils.toString(temp1.get("COURSE_NAME")));
							temp3.put("TYPE", ObjectUtils.toString(temp2.get("TYPE")));
							temp3.put("KCH", ObjectUtils.toString(temp1.get("KCH")));
							temp3.put("KSFS_FLAG", ObjectUtils.toString(temp2.get("KSFS_FLAG")));
							temp3.put("EXAM_STATE", ObjectUtils.toString(temp2.get("EXAM_STATE")));
							temp3.put("EXAM_STYLE", ObjectUtils.toString(temp2.get("EXAM_STYLE")));
							temp3.put("EXAM_STIME", ObjectUtils.toString(temp2.get("EXAM_STIME")));
							temp3.put("EXAM_ETIME", ObjectUtils.toString(temp2.get("EXAM_ETIME")));

							myExamResult.add(temp3);
						}
					}
				}
			}

			searchParams.remove("BESPEAK_STATE");
			// 删除重复数据(根据COURSE_ID相同的)
			List<Object> myExamResultNew = new ArrayList<Object>();
			for (int r = 0; r < myExamResult.size(); r++) {
				boolean flag = true;
				Map map1 = (Map) myExamResult.get(r);
				for (int s = myExamResult.size() - 1; s > r; s--) {
					Map map2 = (Map) myExamResult.get(s);
					if (ObjectUtils.toString(map1.get("COURSE_ID"))
							.equals(ObjectUtils.toString(map2.get("COURSE_ID")))) {
						flag = false;
						break;
					}
				}
				if(flag) {
					myExamResultNew.add(map1);
				}
			}

			int dks_count = 0;
			for (int t = 0; t < myExamResultNew.size(); t++) {
				Map tMap = (Map) myExamResultNew.get(t);

				if ("0".equals(ObjectUtils.toString(tMap.get("EXAM_STATE")))
						|| "--".equals(ObjectUtils.toString(tMap.get("EXAM_STATE")))) {
					dks_count = dks_count + 1;
				}
			}

			dataMap.put("DKS_COUNT", ObjectUtils.toString(dks_count)); // 待考试
			// *************4.学员待预约考试数，5.学员待考试科目数 结束*************//

			resultMap.put("result", "success");
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "fail");
			resultMap.put("message", e.getMessage());
		}

		return resultMap;
	}

	/**
	 * 匹配开考科目与课程
	 * 
	 * @param viewMap
	 * @return
	 */
	public static Map<String, Object> viewCourseMap(Map viewMap) {
		Map resutlMap = new LinkedHashMap();
		try {
			resutlMap.put("EXAM_PLAN_ID", ObjectUtils.toString(viewMap.get("EXAM_PLAN_ID")));
			resutlMap.put("COURSE_ID", ObjectUtils.toString(viewMap.get("COURSE_ID")));
			resutlMap.put("COURSE_NAME", ObjectUtils.toString(viewMap.get("COURSE_NAME")));
			resutlMap.put("KSFS_FLAG", ObjectUtils.toString(viewMap.get("TYPE")));
			resutlMap.put("EXAM_STYLE", ObjectUtils.toString(viewMap.get("EXAM_STYLE")));
			resutlMap.put("EXAM_NO", ObjectUtils.toString(viewMap.get("EXAM_NO")));
			resutlMap.put("EXAM_STATE", ObjectUtils.toString(viewMap.get("EXAM_STATE")));
			resutlMap.put("EXAM_STIME", ObjectUtils.toString(viewMap.get("EXAM_STIME")));
			resutlMap.put("EXAM_ETIME", ObjectUtils.toString(viewMap.get("EXAM_ETIME")));
			resutlMap.put("BOOK_STARTTIME", ObjectUtils.toString(viewMap.get("BOOK_STARTTIME")));
			resutlMap.put("BOOK_ENDTIME", ObjectUtils.toString(viewMap.get("BOOK_ENDTIME")));
			resutlMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(viewMap.get("EXAM_BATCH_CODE")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resutlMap;
	}

	@Override
	public List<Map<String, Object>> countByOrgIdsAndSpecialtyName(List<String> orgIdList, String specialtyName) {
		return studyManageDao.countByOrgIdsAndSpecialtyName(orgIdList, specialtyName);
	}

	/**
	 * 学情分析--》课程班学情列表导出
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadCourseClassConditionListExportXls(Map searchParams) {
		List<Map<String, String>> resultList = studyManageDao.getCourseClassList(searchParams);
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("班级人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习进度");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均考试成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均总成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("已通过人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未通过人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("登记中人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未学习人数");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_SCHEDULE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_STUDY_SCORE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_EXAM_SCORE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_TOTAL_SCORE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_PASS_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_UNPASS_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_STUDY_IMG")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_REGISTER_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_NEVER_STUDY")));
			}
		}
		return wb;
	}

	/**
	 * 学情分析--》学员学情分析列表导出
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadStudentCourseListExportXls(Map searchParams) {
		List<Map<String, String>> resultList = studyManageDao.getStudentCourseList(searchParams);
		XSSFWorkbook wb = new XSSFWorkbook();
		//为了能够使用换行，您需要设置单元格的样式 wrap=true  
		XSSFCellStyle s = wb.createCellStyle();  
        s.setWrapText(true);
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学员类型");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学籍状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("总学分");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("已获学分");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最低毕业学分");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("是否满足毕业学分");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程总数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("正常的");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("落后的");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("已通过");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未通过");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总时长");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, ObjectUtils.toString(e.get("USER_TYPE"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, ObjectUtils.toString(e.get("XJZT"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZXF")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUM_GET_CREDITS")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZDBYXF")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("NORMAL_REC_COUNT")));
				cell = row.createCell(cellIndex++);
				BigDecimal behindRecCount = (BigDecimal) e.get("BEHIND_REC_COUNT");
				cell.setCellValue(behindRecCount.intValue() == 0 ? ObjectUtils.toString(behindRecCount) : behindRecCount + "\n(异常)");
				cell.setCellStyle(s);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PASS_REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("UNPASS_REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LEARNING_REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("UNLEARN_REC_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIMES")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME_COUNT")));
			}
		}
		return wb;
	}

	/**
	 * 成绩查询-成绩列表导出
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadScoreListExportXls(Map searchParams) {
		List<Map<String, Object>> recordlist = studyManageDao.getScoreList(searchParams);
		int page = 1, pageSize = 65500;
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			while(((page - 1)*pageSize) < recordlist.size()) { // 是否有下一页
				//  插入工作表
				HSSFSheet sheet = wb.createSheet();
				HSSFRow row;
				HSSFCell cell;
				int rowIndex = 0;
				int cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				// 标题栏
				cell = row.createCell(cellIndex++);
				cell.setCellValue("姓名");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学号");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("手机");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("层次");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("年级");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学期");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("专业");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("教务班级");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("班主任");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("课程代码");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("课程名称");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("辅导老师");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("考试方式");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("形考比例");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学分");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("已获学分");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习次数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习时长");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习进度");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习成绩");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("考试成绩");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("总成绩");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("状态");
				for(int i = (page - 1)*pageSize; i < page*pageSize && i < recordlist.size(); i++) { // 当前页数据
					Map<String, Object> info = recordlist.get(i);
					// 填充数据
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("XM")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("XH")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("SJH")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("PYCC_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("YEAR_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("GRADE_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("ZYMC")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("BJMC")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("BZR_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("KCH")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("KCMC")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("FD_NAME")));
					cell = row.createCell(cellIndex++);

					String exam_state = ObjectUtils.toString(info.get("EXAM_STATE"));
					if ("0".equals(exam_state) || "1".equals(exam_state)) {
						if(com.gzedu.xlims.common.StringUtils.isNotBlank(info.get("EXAM_PLAN_KSFS_NAME"))) {
							cell.setCellValue(ObjectUtils.toString(info.get("EXAM_PLAN_KSFS_NAME")));
						} else {
							cell.setCellValue(ObjectUtils.toString(info.get("KSFS_NAME")));
						}
					} else {
						cell.setCellValue(ObjectUtils.toString(info.get("KSFS_NAME")));
					}
					cell = row.createCell(cellIndex++);
					String kcxxbz = ObjectUtils.toString(info.get("KCXXBZ"), "");
					if (EmptyUtils.isNotEmpty(kcxxbz)) {
						cell.setCellValue(kcxxbz + "%");
					} else {
						cell.setCellValue("");
					}
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("XF")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("GET_CREDITS")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("STUDY_TIMES")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("ONLINE_TIME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("PROGRESS")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("EXAM_SCORE")));
					cell = row.createCell(cellIndex++);

					String examState = ObjectUtils.toString(info.get("EXAM_STATE"), "2");
					if ("2".equals(examState)) {// 学习中没有考试成绩
						cell.setCellValue("");
					} else {
						cell.setCellValue(ObjectUtils.toString(info.get("EXAM_SCORE1")));
					}
					cell = row.createCell(cellIndex++);
					if ("3".equals(examState) || "2".equals(examState)) {// 学习中或者登记中，没有总成绩
						cell.setCellValue("");
					} else {
						cell.setCellValue(ObjectUtils.toString(info.get("EXAM_SCORE2")));
					}
					cell = row.createCell(cellIndex++);
					switch (Integer.parseInt(examState)) {
						case 0:
							cell.setCellValue("未通过");
							break;
						case 1:
							cell.setCellValue("已通过");
							break;
						case 2:
							cell.setCellValue("学习中");
							break;
						case 3:
							cell.setCellValue("登记中");
							break;
						default:
							cell.setCellValue("");
					}
				}
				page++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 管理后台--课程学情学情明细，班级学情学情明细，学员学情学情明细
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadCommonCourseDetailExportXls(Map searchParams) {
		List<Map<String, String>> result = studyManageDao.getCommonCourseDetail(searchParams);

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(result)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程模块");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习进度");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习状态");

			for (Map e : result) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("START_GRADE")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.COURSETYPE, ObjectUtils.toString(e.get("KCLBM"))));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SCHEDULE"), "0") + "%");

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));

				cell = row.createCell(cellIndex++);
				String exam_state = ObjectUtils.toString(e.get("EXAM_STATE"), "");
				if ("学习中".equals(exam_state) && "0".equals(ObjectUtils.toString(e.get("LOGIN_COUNT"), "0"))) {
					cell.setCellValue("未学习");
				} else {
					cell.setCellValue(exam_state);
				}

			}
		}
		return wb;
	}

	@Override
	public Page getRollDataInfo(Map<String, Object> searchParams, PageRequest pageRequest) {
		return studyManageDao.getRollDataInfo(searchParams, pageRequest);
	}

	@Override
	public Map getRollCountInfo(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			List list = studyManageDao.getRollCountInfo(searchParams);
			int sum_count = 0;
			int pending_count = 0;
			int atSchool_count = 0;
			int transfer_count = 0;
			int register_count = 0;
			int leave_count = 0;
			int quit_count = 0;
			int graduation_count = 0;

			if (EmptyUtils.isNotEmpty(list)) {

				sum_count = list.size();

				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);

					if ("3".equals(ObjectUtils.toString(map.get("XJZT")))) {
						pending_count = pending_count + 1;
					} else if ("2".equals(ObjectUtils.toString(map.get("XJZT")))) {
						atSchool_count = atSchool_count + 1;
					} else if ("10".equals(ObjectUtils.toString(map.get("XJZT")))) {
						transfer_count = transfer_count + 1;
					} else if ("0".equals(ObjectUtils.toString(map.get("XJZT")))) {
						register_count = register_count + 1;
					} else if ("5".equals(ObjectUtils.toString(map.get("XJZT")))) {
						leave_count = leave_count + 1;
					} else if ("2".equals(ObjectUtils.toString(map.get("XJZT")))) {
						quit_count = quit_count + 1;
					} else if ("8".equals(ObjectUtils.toString(map.get("XJZT")))) {
						graduation_count = graduation_count + 1;
					}
				}
			}

			resultMap.put("SUM_COUNT", sum_count); // 总数
			resultMap.put("PENDING_COUNT", pending_count); // 待注册
			resultMap.put("ATSCHOOL_COUNT", atSchool_count); // 在籍
			resultMap.put("TRANSFER_COUNT", transfer_count); // 转学
			resultMap.put("REGISTER_COUNT", register_count); // 注册中
			resultMap.put("LEAVE_COUNT", leave_count); // 退学
			resultMap.put("QUIT_COUNT", quit_count); // 休学
			resultMap.put("GRADUATION_COUNT", graduation_count); // 毕业
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public Map getRollBaseData(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {

			List gradeList = studyManageDao.getGradeData(searchParams); // 学期
			if (EmptyUtils.isNotEmpty(gradeList)) {
				resultMap.put("gradeList", gradeList);
			} else {
				resultMap.put("gradeList", new ArrayList());
			}

			List specialList = studyManageDao.getSpecialtyData(searchParams); // 专业
			if (EmptyUtils.isNotEmpty(specialList)) {
				resultMap.put("specialList", specialList);
			} else {
				resultMap.put("specialList", new ArrayList());
			}

			List pyccList = studyManageDao.getPyccData(searchParams); // 层次
			if (EmptyUtils.isNotEmpty(pyccList)) {
				resultMap.put("pyccList", pyccList);
			} else {
				resultMap.put("pyccList", new ArrayList());
			}

			// 学籍管理
			if ("1".equals(ObjectUtils.toString(searchParams.get("MODEL_TYPE")))) {
				List xjztList = studyManageDao.getXjztData(searchParams); // 学籍状态
				if (EmptyUtils.isNotEmpty(xjztList)) {
					resultMap.put("xjztList", xjztList);
				} else {
					resultMap.put("xjztList", new ArrayList());
				}

				List xbList = studyManageDao.getSexData(searchParams); // 性别
				if (EmptyUtils.isNotEmpty(xbList)) {
					resultMap.put("xbList", xbList);
				} else {
					resultMap.put("xbList", new ArrayList());
				}

			}

			// 考试管理
			if ("2".equals(ObjectUtils.toString(searchParams.get("MODEL_TYPE")))) {
				List batchList = studyManageDao.getExamBatchInfo(searchParams); // 查询考试计划
				if (EmptyUtils.isNotEmpty(batchList)) {
					resultMap.put("batchList", batchList);
				} else {
					resultMap.put("batchList", new ArrayList());
				}

				List planList = studyManageDao.getExamPlanInfo(searchParams); // 查询考试科目
				if (EmptyUtils.isNotEmpty(planList)) {
					resultMap.put("planList", planList);
				} else {
					resultMap.put("planList", new ArrayList());
				}

				List examModeList = studyManageDao.getExaminationMode(searchParams); // 查询考试形式
				if (EmptyUtils.isNotEmpty(examModeList)) {
					resultMap.put("examModeList", examModeList);
				} else {
					resultMap.put("examModeList", new ArrayList());
				}

				List courseList = studyManageDao.getCourseInfo(searchParams); // 查询课程信息
				if (EmptyUtils.isNotEmpty(courseList)) {
					resultMap.put("courseList", courseList);
				} else {
					resultMap.put("courseList", new ArrayList());
				}

			}

			// 成绩管理
			if ("3".equals(ObjectUtils.toString(searchParams.get("MODEL_TYPE")))) {
				List batchList = studyManageDao.getExamBatchInfo(searchParams); // 查询考试计划
				if (EmptyUtils.isNotEmpty(batchList)) {
					resultMap.put("batchList", batchList);
				} else {
					resultMap.put("batchList", new ArrayList());
				}

				List planList = studyManageDao.getExamPlanInfo(searchParams); // 查询考试科目
				if (EmptyUtils.isNotEmpty(planList)) {
					resultMap.put("planList", planList);
				} else {
					resultMap.put("planList", new ArrayList());
				}
			}

			// 学习管理
			if ("5".equals(ObjectUtils.toString(searchParams.get("MODEL_TYPE")))) {
				// 从公共部分获取学期，专业，层次数据
			}

			// 毕业管理
			if ("4".equals(ObjectUtils.toString(searchParams.get("MODEL_TYPE")))) {
				List<Map<Integer, String>> applyStatusList = new ArrayList();
				List<Map<Integer, String>> practiceStatusList = new ArrayList();
				// 查询毕业论文状态
				Map<Integer, String> applyStatusMap = EnumUtil.getThesisApplyStatusMap();
				if (EmptyUtils.isNotEmpty(applyStatusMap)) {
					applyStatusList.add(applyStatusMap);
					resultMap.put("applyStatusList", applyStatusList);
				} else {
					resultMap.put("applyStatusList", applyStatusList);
				}
				// 查询社会实践状态
				Map<Integer, String> practiceStatusMap = EnumUtil.getPracticeApplyStatusMap();
				if (EmptyUtils.isNotEmpty(practiceStatusMap)) {
					practiceStatusList.add(practiceStatusMap);
					resultMap.put("practiceStatusList", practiceStatusList);
				} else {
					resultMap.put("practiceStatusList", practiceStatusList);
				}
			}
			resultMap.put("result", "1");
			resultMap.put("message", "查询成功!");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "2");
			resultMap.put("message", e.getMessage());
		}

		return resultMap;

	}

	public Map getSpecialAndSingUp(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			List<Map<String, Object>> list = gjtSignUpInfoDataDao.querySpecialCountTopFive(searchParams);
			for (Map<String, Object> info : list) {
				info.put("STU_COUNT", info.get("ZYMC_COUNT"));
			}
			resultMap.put("SPECIAL_STU_LIST", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获得学员的成绩(统计)
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Map getExamScoreCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();

		searchParams.put("EXAM_STATE_TEMP", "0");
		int exam_state0 = studyManageDao.getExamScoreCount(searchParams);
		resultMap.put("EXAM_STATE0", ObjectUtils.toString(exam_state0));

		searchParams.put("EXAM_STATE_TEMP", "1");
		int exam_state1 = studyManageDao.getExamScoreCount(searchParams);
		resultMap.put("EXAM_STATE1", ObjectUtils.toString(exam_state1));

		searchParams.put("EXAM_STATE_TEMP", "2");
		int exam_state2 = studyManageDao.getExamScoreCount(searchParams);
		resultMap.put("EXAM_STATE2", ObjectUtils.toString(exam_state2));

		searchParams.put("EXAM_STATE_TEMP", "3");
		int exam_state3 = studyManageDao.getExamScoreCount(searchParams);
		resultMap.put("EXAM_STATE3", ObjectUtils.toString(exam_state3));

		searchParams.put("EXAM_STATE_TEMP", "4");
		int exam_state4 = studyManageDao.getExamScoreCount(searchParams);
		resultMap.put("EXAM_STATE4", ObjectUtils.toString(exam_state4));

		int exam_state_all = exam_state0 + exam_state1 + exam_state2 + exam_state3 + exam_state4;
		resultMap.put("EXAM_STATE_ALL", exam_state_all);
		return resultMap;
	}

	/**
	 * 获得学员的成绩
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Page getExamScoreList(Map<String, Object> searchParams, PageRequest pageRequest) {
		return studyManageDao.getExamScoreList(searchParams, pageRequest);
	}

	@Override
	public Map getEnrolmentCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		Map temp = null;
		try {
			searchParams.put("AUDIT_STATE", "4"); // 未提交
			List list_4 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if (EmptyUtils.isNotEmpty(list_4)) {
				temp = (Map) list_4.get(0);
				resultMap.put("UN_COMMIT", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "3"); // 待审核
			List list_3 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if (EmptyUtils.isNotEmpty(list_3)) {
				temp = (Map) list_3.get(0);
				resultMap.put("WAIT_AUDIT", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "1"); // 审核通过
			List list_1 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if (EmptyUtils.isNotEmpty(list_1)) {
				temp = (Map) list_1.get(0);
				resultMap.put("AUDIT_PASS", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "2"); // 重新提交
			List list_2 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if (EmptyUtils.isNotEmpty(list_2)) {
				temp = (Map) list_2.get(0);
				resultMap.put("RES_SUBMIT", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "0"); // 审核不通过
			List list_0 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if (EmptyUtils.isNotEmpty(list_0)) {
				temp = (Map) list_0.get(0);
				resultMap.put("AUDIT_NOPASS", temp.get("STUDENT_COUNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map getEnterpriseSignUp(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			Map<String, Object> countMap = gjtSignUpInfoDataDao.countStudentRollSituationBy(searchParams);
			if (countMap != null) {
				resultMap.put("GRAD_COUNT", countMap.get("GRADUATIONNUM"));
				resultMap.put("LEAVESCHOOLNUM", countMap.get("LEAVESCHOOLNUM"));
				resultMap.put("LEAVE_COUNT", countMap.get("TEMPORARYSCHOOLNUM"));
				resultMap.put("ATSCHOOL_COUNT", countMap.get("ALREADYREGNUM"));
				resultMap.put("NOTREG_COUNT", countMap.get("NOTREGNUM"));
				resultMap.put("STU_COUNT", countMap.get("SIGNUPNUM"));
				resultMap.put("SPECIALTY_COUNT", countMap.get("SPECIALTY_COUNT"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map getLearningRank(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			List rankList = studyManageDao.getLearningRank(searchParams);
			if (EmptyUtils.isNotEmpty(rankList)) {
				resultMap.put("rankList", rankList);
			} else {
				resultMap.put("rankList", new ArrayList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public Map<String, Object> loginWithoutPassword(String uid) {
		JsonObject json = new JsonObject();
		json.addProperty("USER_ID", uid);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("data", json.toString());

		String rsp = HttpClientUtils.doHttpPost(URL_IM, args, 3000, "UTF-8");

		Object[] params = new Object[] { URL_IM + "?data=" + json.toString(), rsp };
		log.info("用户登录(免密码)[url: {}], 返回值: {}", params);

		Map<String, Object> result = null;

		if (StringUtils.isNotBlank(rsp)) {
			Gson gson = new GsonBuilder().create();
			result = gson.fromJson(rsp, HashMap.class);
			String status = ObjectUtils.toString(result.get("STATUS"));
			if (!StringUtils.equals("2", status)) {
				return result;
			}
		}

		return result;
	}

	public String getSigByPhoneNumber(String studentId, String eeno) {
		String token = StringUtils.EMPTY;

		JsonObject json = new JsonObject();
		json.addProperty("USER_ID", studentId);
		json.addProperty("TEL", eeno); // 20170908 改成传ee号

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("data", json.toString());

		String rsp = HttpClientUtils.doHttpPost(URL_TOKEN, args, 3000, "UTF-8");

		Object[] params = new Object[] { URL_TOKEN + "?data=" + json.toString(), rsp };
		log.info("根据手机号码获取Sig令牌[url: {}], 返回值: {}", params);

		if (StringUtils.isNotBlank(rsp)) {
			Gson gson = new GsonBuilder().create();
			Map<String, Object> result = gson.fromJson(rsp, HashMap.class);
			String status = ObjectUtils.toString(result.get("STATUS"));
			if (StringUtils.equals("1", status)) {
				token = ObjectUtils.toString(result.get("RESULT"));
			}
		}

		return token;
	}

	@Override
	public Workbook downCourseSubjectExport(Map<String, Object> searchParams) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		List<Map<String, String>> resultList = studyManageDao.getSubjectList(searchParams);

		Row row;
		Cell cell;
		int rowIndex = 0;
		int cellIndex = 0;
		row = sheet.createRow(rowIndex++);
		// 标题栏
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程名称");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("答疑标题");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("答疑内容");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("题问学生");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("回复内容");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生提问时间");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("回复老师");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("回复内容");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("回复时间");

		if (EmptyUtils.isNotEmpty(resultList)) {
			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("TERM_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("COURSE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUBJECT_TITLE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SUBJECT_CONTENT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ASK_STUDENT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ASK_CONTENT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("STUDENT_ASK_TIME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("REPLY_TEACHER")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("REPLY_CONTENT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("REPLY_TIME")));
			}
		} else {
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(0);
			cell.setCellValue("查无数据");
		}
		return wb;
	}

	@Override
	public Workbook downCourseActivityExport(Map<String, Object> searchParams) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();

		Row row;
		Cell cell;
		int rowIndex = 0;
		int cellIndex = 0;
		row = sheet.createRow(rowIndex++);
		// 标题栏
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程名称");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("讨论标题");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("讨论内容");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生回复内容");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生回复时间");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("评论人");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("评论内容");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("评论时间");
		long count = studyManageDao.getCourseActivityCount(searchParams);
		if (count > 0) {
			List<Map<String, String>> resultList = studyManageDao.getCourseActivityList(searchParams);
			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("TERM_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("COURSE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("FORUM_TITLE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("FORUM_CONTENT")));

				String forumId = ObjectUtils.toString(e.get("FORUM_ID"));
				List<Map<String, String>> forumList = studyManageDao.getRealyActivityList(forumId);
				for (Map<String, String> map : forumList) {
					String parent_id = ObjectUtils.toString(map.get("PARENT_ID"));
					if (StringUtils.isNotBlank(parent_id)) {// 说明是评论
						cell = row.createCell(4);
						cell.setCellValue(ObjectUtils.toString(map.get("USER_NAME")));
						cell = row.createCell(5);
						cell.setCellValue(ObjectUtils.toString(map.get("FORUM_CONTENT")));
						cell = row.createCell(6);
						cell.setCellValue(ObjectUtils.toString(map.get("CREATED_DT")));
						row = sheet.createRow(rowIndex++);
					} else {// 是评论下面的讨论
						cell = row.createCell(7);
						cell.setCellValue(ObjectUtils.toString(map.get("USER_NAME")));
						cell = row.createCell(8);
						cell.setCellValue(ObjectUtils.toString(map.get("FORUM_CONTENT")));
						cell = row.createCell(9);
						cell.setCellValue(ObjectUtils.toString(map.get("CREATED_DT")));
						row = sheet.createRow(rowIndex++);
					}
				}

				// String parentId =
				// ObjectUtils.toString(e.get("FORUM_REPLY_ID"));
				// List<Map<String, String>> activityList =
				// studyManageDao.getRealyCourseActivityList(parentId);
				// for (Map<String, String> map : activityList) {
				// cell = row.createCell(8);
				// cell.setCellValue(ObjectUtils.toString(map.get("USER_NAME")));
				// cell = row.createCell(9);
				// cell.setCellValue(ObjectUtils.toString(map.get("FORUM_CONTENT")));
				// cell = row.createCell(10);
				// cell.setCellValue(ObjectUtils.toString(map.get("CREATED_DT")));
				// row = sheet.createRow(rowIndex++);
				// }
			}
		} else {
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(0);
			cell.setCellValue("查无数据");
		}
		return wb;
	}

	/**
	 * 学生综合信息查询=》链接
	 * @return
	 */
	@Override
	public Page getStudentLinkList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getStudentLinkList(searchParams, pageRequst);
	}

	/**
	 * 学生综合信息查询=》链接
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getStudentLinkList(Map searchParams) {
		return studyManageDao.getStudentLinkList(searchParams);
	}

	/**
	 * 学生综合信息查询=》链接
	 * @return
	 */
	@Override
	public long getStudentLinkCount(Map searchParams) {
		return studyManageDao.getStudentLinkCount(searchParams);
	}

	/**
	 * 学生综合信息查询=》链接导出
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadStudentLinkListExportXls(Map searchParams) {
		List<Map<String, Object>> resultList = studyManageDao.getStudentLinkList(searchParams);
		XSSFWorkbook wb = new XSSFWorkbook();
		//为了能够使用换行，您需要设置单元格的样式 wrap=true  
		XSSFCellStyle s = wb.createCellStyle();  
        s.setWrapText(true);
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学员类型");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学籍状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("安装APP");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("使用PC");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("关注微信公众号");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, ObjectUtils.toString(e.get("USER_TYPE"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, ObjectUtils.toString(e.get("XJZT"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				BigDecimal appLastLoginTime = (BigDecimal) e.get("APP_LAST_LOGIN_TIME");
				String appIsOnline = (String) e.get("APP_IS_ONLINE");
				String app = "";
				if(appLastLoginTime != null) {
					app += "已安装";
					if(StringUtils.equals(appIsOnline, Constants.BOOLEAN_YES)) {
						app += "\n当前在线";
					} else {
						if(appLastLoginTime.intValue() == 0) {
							app += "\n(从未使用)";
						} else if(appLastLoginTime.intValue() <= 7) {
							app += "\n("+appLastLoginTime.intValue()+"天内使用过)";
						} else if(appLastLoginTime.intValue() > 7) {
							app += "\n(超过"+appLastLoginTime.intValue()+"天未使用)";
							app += "\n(异常)";
						}
					}
				} else {
					app += "未安装";
				}
				cell.setCellValue(app);
				cell.setCellStyle(s);
				cell = row.createCell(cellIndex++);
				BigDecimal pcLastLoginTime = (BigDecimal) e.get("PC_LAST_LOGIN_TIME");
				String pcIsOnline = (String) e.get("PC_IS_ONLINE");
				String pc = "";
				if(pcLastLoginTime != null) {
					pc += "已使用";
					if(StringUtils.equals(pcIsOnline, Constants.BOOLEAN_YES)) {
						pc += "\n当前在线";
					} else {
						if(pcLastLoginTime.intValue() == 0) {
							pc += "\n(从未登录)";
						} else if(pcLastLoginTime.intValue() <= 7) {
							pc += "\n("+pcLastLoginTime.intValue()+"天内登录过)";
						} else if(pcLastLoginTime.intValue() > 7) {
							pc += "\n(超过"+pcLastLoginTime.intValue()+"天未登录)";
							pc += "\n(异常)";
						}
					}
				} else {
					pc += "未使用";
				}
				cell.setCellValue(pc);
				cell.setCellStyle(s);
				cell = row.createCell(cellIndex++);
				BigDecimal isBandingWx = (BigDecimal) e.get("IS_BANDING_WX");
				cell.setCellValue(isBandingWx.intValue() == 1 ? "已关注" : "未关注");
			}
		}
		return wb;
	}

	@Override
	public Map<String, Object> countStudentStudySituation(Map searchParams) {
		return studyManageDao.countStudentStudySituation(searchParams);
	}

}
