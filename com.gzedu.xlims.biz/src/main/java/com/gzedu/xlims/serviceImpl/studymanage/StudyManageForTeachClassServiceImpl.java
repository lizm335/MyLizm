package com.gzedu.xlims.serviceImpl.studymanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.studymanage.StudyManageDao;
import com.gzedu.xlims.pojo.TblPriLoginLog;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.studymanage.StudyManageForTeachClassService;

/**
 * 针对教学班的学习管理服务
 * 
 * @author 欧集红 
 * @Date 2018年4月11日
 * @version 1.0
 * 
 */
@Service
public class StudyManageForTeachClassServiceImpl implements StudyManageForTeachClassService{

	@Autowired
	private StudyManageDao studyManageDao;
	
	@Autowired
	private CacheService cacheService;
	
	/* (non-Javadoc)
	 * 教学班课程考勤
	 * @see com.gzedu.xlims.service.studymanage.StudyManageService#getTeachClassCourseClockingDetail(java.util.Map, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page getTeachClassCourseClockingDetail(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCourseClockingDetail(searchParams, pageRequst);
	}

	/* (non-Javadoc)
	 * 教学班课程考勤
	 * @see com.gzedu.xlims.service.studymanage.StudyManageService#getTeachClassCourseClockingDetailCount(java.util.Map)
	 */
	@Override
	public long getTeachClassCourseClockingDetailCount(Map searchParams) {
		return studyManageDao.getCourseClockingDetailCount(searchParams);
	}
	

	/* (non-Javadoc)
	 * 课程考勤详情下载
	 * @see com.gzedu.xlims.service.studymanage.StudyManageForTeachClassService#downloadCourseClockingDetailExportXls(java.util.Map)
	 */
	@Override
	public Workbook downloadCourseClockingDetailExportXls(Map searchParams) {

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
	
	
	/* (non-Javadoc)
	 * 课程考勤列表
	 * @see com.gzedu.xlims.service.studymanage.StudyManageForTeachClassService#getCourseLoginList(java.util.Map, org.springframework.data.domain.PageRequest)
	 */
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
	 * 考勤分析--》课程考勤下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadCourseLoginListExportXls(Map searchParams) {
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
	 * 处理学员登录详情明细
	 *
	 * @param infos
	 * @return
	 */
	@Override
	public Workbook exportInfoDetails(List<TblPriLoginLog> tblPriLoginLogs) {

		try {
			Workbook book = new XSSFWorkbook();
			Sheet sheet = book.createSheet();
			Row row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("IP地址");
			row.createCell(colIdx++).setCellValue("所在区域");
			row.createCell(colIdx++).setCellValue("学习终端");
			row.createCell(colIdx++).setCellValue("浏览器");
			row.createCell(colIdx++).setCellValue("登入平台时间");
			row.createCell(colIdx++).setCellValue("登出平台时间");
			row.createCell(colIdx++).setCellValue("登录时长");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			if (EmptyUtils.isNotEmpty(tblPriLoginLogs)) {// 存在考勤数据
				for (TblPriLoginLog info : tblPriLoginLogs) {
					row = sheet.createRow(rowIdx++);
					colIdx = 0;
					row.createCell(colIdx++).setCellValue(idx++);
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getLoginIp())));
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString("")));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getLoginType())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getBrowser())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getCreatedDt())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getUpdatedDt())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getLoginTime())));
				}
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 处理课程学情下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downloadCourseStudyListExportXls(Map searchParams) {
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
	
	/**
	 * 学习管理=》课程学情
	 * 
	 * @return
	 */
	@Override
	public Page getCourseStudyList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getCourseStudyList(searchParams, pageRequst);
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
	 * 管理后台--课程学情  学情明细，班级学情 学情明细，学员学情 学情明细
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadCommonCourseDetailExportXls(Map searchParams) {
		List<Map<String, String>> result = studyManageDao.getCommonCourseDetail(searchParams);

		Workbook wb = new HSSFWorkbook();
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

	
	/**
	 * 学习管理=》学员课程学情列表
	 * 
	 * @return
	 */
	@Override
	public Page getStudentCourseList(Map searchParams, PageRequest pageRequst) {
		return studyManageDao.getStudentCourseList(searchParams, pageRequst);
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
		Workbook wb = new HSSFWorkbook();
		//为了能够使用换行，您需要设置单元格的样式 wrap=true  
		CellStyle s = wb.createCellStyle();  
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
	@Override
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

	
}
