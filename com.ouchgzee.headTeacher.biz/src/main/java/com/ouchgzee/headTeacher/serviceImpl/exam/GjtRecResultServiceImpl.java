/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.exam;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.exam.GjtExamPlanDao;
import com.ouchgzee.headTeacher.dao.exam.GjtExamPointDao;
import com.ouchgzee.headTeacher.dao.exam.GjtExamRecordDao;
import com.ouchgzee.headTeacher.dao.exam.GjtRecResultDao;
import com.ouchgzee.headTeacher.dao.student.GjtStudentInfoDao;
import com.ouchgzee.headTeacher.dao.student.GjtTermInfoDao;
import com.ouchgzee.headTeacher.daoImpl.BzrGjtRecResultDaoImpl;
import com.ouchgzee.headTeacher.dto.StudentLearnDto;
import com.ouchgzee.headTeacher.dto.StudentRecResultDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtExamPoint;
import com.ouchgzee.headTeacher.pojo.BzrGjtExamRecord;
import com.ouchgzee.headTeacher.pojo.BzrGjtRecResult;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.status.ExamState;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtRecResultService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtRecResultServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtRecResultServiceImpl extends BaseServiceImpl<BzrGjtRecResult> implements BzrGjtRecResultService {

	private static Logger log = LoggerFactory.getLogger(GjtRecResultServiceImpl.class);

	@Autowired
	private BzrCacheService cacheService;

	@Autowired
	private GjtRecResultDao gjtRecResultDao;

	@Autowired
	private GjtExamRecordDao gjtExamRecordDao;

	@Autowired
	private GjtExamPointDao gjtExamPointDao;

	@Autowired
	private GjtExamPlanDao gjtExamPlanDao;

	@Autowired
	private BzrGjtRecResultDaoImpl recResultDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private GjtTermInfoDao gjtTermInfoDao;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private BzrCommonMapService commonMapService;

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Override
	protected BaseDao<BzrGjtRecResult, String> getBaseDao() {
		return gjtRecResultDao;
	}

	@Override
	public Page<StudentRecResultDto> queryStudentRecResultByPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.created_dt"));
		}

		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		Page<BzrGjtStudentInfo> studentPage = gjtStudentInfoDao.findAll(spec,
				new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "createdDt")));

		if (studentPage != null && studentPage.getNumberOfElements() > 0) {
			String currentTermId = gjtTermInfoDao
					.findByStudentCurrentTerm(studentPage.getContent().get(0).getStudentId());
			searchParams.put("termId", currentTermId);
			Page<Map> result = recResultDao.findStudentRecResultByPage(classId, searchParams, pageRequest);

			List<StudentRecResultDto> stuRecDtoList = new ArrayList();
			for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
				Map info = iter.next();

				StudentRecResultDto stuRecDto = new StudentRecResultDto();
				stuRecDto.setStudentId((String) info.get("STUDENT_ID"));
				stuRecDto.setXm((String) info.get("XM"));
				stuRecDto.setGradeName((String) info.get("GRADE_NAME"));
				stuRecDto.setPycc((String) info.get("PYCC"));
				stuRecDto.setZymc((String) info.get("ZYMC"));
				stuRecDto.setCanExamNum((BigDecimal) info.get("CANEXAMNUM"));
				stuRecDto.setAlreadyExamNum((BigDecimal) info.get("ALREADYEXAMNUM"));
				stuRecDto.setExamPointName((String) info.get("EXAMPOINTNAME"));

				stuRecDtoList.add(stuRecDto);
			}
			return new PageImpl(stuRecDtoList, pageRequest, result.getTotalElements());
		}
		return new PageImpl(Collections.emptyList(), pageRequest, 0);
	}

	@Override
	public List<Map> queryStudentRecResultDetail(String studentId) {
		return recResultDao.findStudentRecResultSituation(studentId, Collections.EMPTY_MAP, null);
	}

	@Override
	public HSSFWorkbook exportStudentRecResultDetailToExcel(String studentId) {
		List<Map> infos = this.queryStudentRecResultDetail(studentId);

		Map<String, String> examStateMap = cacheService.getCachedDictionaryMap(BzrCacheService.DictionaryKey.EXAM_STATE);
		Map<String, String> userTypeMap = cacheService.getCachedDictionaryMap(BzrCacheService.DictionaryKey.USERTYPE);
		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("学期");
			row.createCell(colIdx++).setCellValue("课程名称");
			row.createCell(colIdx++).setCellValue("距离预约结束天数");
			row.createCell(colIdx++).setCellValue("考试预约");
			row.createCell(colIdx++).setCellValue("考试预约操作时间");
			row.createCell(colIdx++).setCellValue("考试预约操作人");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行
			// 生成一个样式
			HSSFCellStyle style = book.createCellStyle();
			// 生成一个字体
			HSSFFont font = book.createFont();
			font.setColor(HSSFColor.RED.index);
			// 把字体应用到当前的样式
			style.setFont(font);

			for (Map info : infos) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("TERM_NAME"))));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("KCMC"))));
				BigDecimal RESERVESTARTDAYS = (BigDecimal) info.get("RESERVESTARTDAYS");
				BigDecimal RESERVEENDDAYS = (BigDecimal) info.get("RESERVEENDDAYS");
				String endDay = "";
				if (RESERVESTARTDAYS != null && RESERVESTARTDAYS.intValue() < 0) {
					endDay = "未开始";
				} else {
					if (RESERVEENDDAYS != null && RESERVEENDDAYS.intValue() < 0) {
						endDay = "已结束";
					} else {
						endDay = RESERVEENDDAYS + "";
					}
				}
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(endDay)));
				String EXAM_STATE = (String) info.get("EXAM_STATE");
				if ("1".equals(EXAM_STATE)) {
					HSSFCell cell = row.createCell(colIdx++);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(examStateMap.get(EXAM_STATE));
					cell.setCellValue(text);
				} else {
					row.createCell(colIdx++).setCellValue(
							new HSSFRichTextString(EXAM_STATE != null ? examStateMap.get(EXAM_STATE) : null));
				}
				Date UPDATED_DT = (Date) info.get("UPDATED_DT");
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils
						.toString(UPDATED_DT != null ? DateFormatUtils.ISO_DATETIME_FORMAT.format(UPDATED_DT) : null)));
				BigDecimal USER_TYPE = (BigDecimal) info.get("USER_TYPE");
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
						ObjectUtils.toString(USER_TYPE != null ? userTypeMap.get(USER_TYPE.toString()) : null)));
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<StudentLearnDto> queryLearningSituationByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtUserAccount.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		Page<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, pageRequest);

		List<StudentLearnDto> stuLearnDtoList = this.encapsulationLearnDto(result.getContent());
		return new PageImpl<StudentLearnDto>(stuLearnDtoList, pageRequest, result.getTotalElements());
	}

	@Override
	public Map countClassStudentLearningSituation(String teachClassId, String courseId) {
		return recResultDao.countClassStudentLearningSituation(teachClassId, courseId);
	}

	/**
	 * 共用方法 - 封装发票信息
	 * 
	 * @param result
	 * @return
	 */
	private List<StudentLearnDto> encapsulationLearnDto(List<BzrGjtStudentInfo> result) {
		List<StudentLearnDto> stuLearnDtoList = new ArrayList();
		for (Iterator<BzrGjtStudentInfo> iter = result.iterator(); iter.hasNext();) {
			BzrGjtStudentInfo info = iter.next();

			StudentLearnDto stuLearnDto = new StudentLearnDto();
			stuLearnDto.setStudentId(info.getStudentId());
			stuLearnDto.setXm(info.getXm());
			stuLearnDto.setSfzh(info.getSfzh());

			// ......

			stuLearnDtoList.add(stuLearnDto);
		}
		return stuLearnDtoList;
	}

	/**
	 * 学支平台--导出学员学情统计表
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	@Override
	public HSSFWorkbook exportLearningSituationToExcel(Map<String, Object> searchParams, Sort sort) {
		List<Map<String, Object>> resultList = queryAllLearningSituations(searchParams);
		try {
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			if(EmptyUtils.isNotEmpty(resultList)){
				HSSFRow row;
				HSSFCell cell;
				int rowIndex = 0;
				int cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				//标题栏
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
				cell.setCellValue("已通过课程数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("未通过课程数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习中课程数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("未学习课程数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习总次数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习总时长");

				for (Map e :resultList){
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
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map> queryStudentRecResultLearningDetail(String studentId) {
		List<Map> courseScoreList = recResultDao.findStudentRecResultScore(studentId);
		return courseScoreList;
	}

	@Override
	public HSSFWorkbook exportStudentRecResultLearningDetailToExcel(String studentId) {
		List<Map> infos = this.queryStudentRecResultLearningDetail(studentId);
		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("学期");
			row.createCell(colIdx++).setCellValue("课程名称");
			row.createCell(colIdx++).setCellValue("学习状态");
			row.createCell(colIdx++).setCellValue("学习次数");
			row.createCell(colIdx++).setCellValue("学习时长");
			row.createCell(colIdx++).setCellValue("上次学习间隔天数");
			row.createCell(colIdx++).setCellValue("已完成活动/活动总数");
			row.createCell(colIdx++).setCellValue("已完成必修活动/必修活动总数");
			row.createCell(colIdx++).setCellValue("学习成绩");
			row.createCell(colIdx++).setCellValue("考试成绩");
			row.createCell(colIdx++).setCellValue("总成绩");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (Map info : infos) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("TERM_NAME"))));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("KCMC"))));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(null)));
				String LONGINCOUNT = (String) info.get("LONGINCOUNT");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(ObjectUtils.toString(LONGINCOUNT != null ? LONGINCOUNT : "-")));
				String USERTOTALTIME = (String) info.get("USERTOTALTIME");
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
						ObjectUtils.toString(USERTOTALTIME != null ? USERTOTALTIME + "小时" : "-")));
				String LONGINDYA = (String) info.get("LONGINDYA");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(ObjectUtils.toString(LONGINDYA != null ? LONGINDYA + "天" : "-")));
				String DYNACOUNT = (String) info.get("DYNACOUNT");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(ObjectUtils.toString(DYNACOUNT != null ? DYNACOUNT : "" + "/")));
				String MUSTDYNACOUNT = (String) info.get("MUSTDYNACOUNT");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(ObjectUtils.toString(MUSTDYNACOUNT != null ? MUSTDYNACOUNT : "" + "/")));
				String ALLPOINT = (String) info.get("ALLPOINT");
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(ALLPOINT != null ? ALLPOINT : "-")));
				String EXAM_SCORE1 = (String) info.get("EXAM_SCORE1");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(ObjectUtils.toString(EXAM_SCORE1 != null ? EXAM_SCORE1 : "-")));
				String EXAM_SCORE2 = (String) info.get("EXAM_SCORE2");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(ObjectUtils.toString(EXAM_SCORE2 != null ? EXAM_SCORE2 : "-")));
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在课程班级集合中找到课程对应的班级
	 * 
	 * @param courseClass
	 * @param teachPlanId
	 * @return
	 */
	private String getCourseClassId(List<Map> courseClass, String teachPlanId) {
		String courseClassId = null;
		for (Map cc : courseClass) {
			if (teachPlanId.equals(cc.get("TEACH_PLAN_ID"))) {
				courseClassId = (String) cc.get("CLASS_ID");
				break;
			}
		}
		return courseClassId;
	}

	@Override
	public Object[] countStudentReserveSituation(String studentId) {
		return recResultDao.countStudentReserveSituation(studentId);
	}

	@Override
	public long countClassStudentWaitExam(String classId) {
		return recResultDao.countClassStudentWaitExam(classId);
	}

	@Override
	public long countClassStudentWaitExamPoint(String classId) {
		return recResultDao.countClassStudentWaitExamPoint(classId);
	}

	@Override
	public boolean updateRecExamCourse(String studentId, String recId, String updatedBy) {
		BzrGjtRecResult gjtRecResult = queryById(recId);
		// 检查当前该选课的状态
		if (NumberUtils.toInt(gjtRecResult.getExamState()) != ExamState.UNAPPOINTMENT.getValue()) {
			log.error("课程状态为:" + gjtRecResult.getExamState() + "，不能进行预约。");
			return false;
		}
		Date now = new Date();
		// 修改选课表当中考试预约状态修改为已预约状态。
		gjtRecResult.setExamState(ExamState.APPOINTMENT.getValue() + "");
		gjtRecResult.setUpdatedBy(updatedBy);
		gjtRecResult.setUpdatedDt(now);
		gjtRecResultDao.save(gjtRecResult);
		// 修改考试记录表中当前选课ID的所有记录
		gjtExamRecordDao.inactiveExamState(recId);
		// 添加考试预约，并设置状态为预约状态
		BzrGjtExamRecord gjtExamRecord = new BzrGjtExamRecord();
		gjtExamRecord.setId(UUIDUtils.random());
		gjtExamRecord.setRecId(recId);
		gjtExamRecord.setTeachPlanId(gjtRecResult.getTeachPlanId());
		gjtExamRecord.setStudentId(gjtRecResult.getStudentId());
		gjtExamRecord.setIsCancel(Constants.BOOLEAN_0);
		gjtExamRecord.setExamState(ExamState.APPOINTMENT.getValue() + "");
		gjtExamRecord.setBookTime(now);
		gjtExamRecordDao.save(gjtExamRecord);
		return true;
	}

	@Override
	public boolean updateRecExamCourse(String studentId, String[] recIds, String updatedBy) {
		if (recIds != null && recIds.length > 0) {
			for (String recId : recIds) {
				this.updateRecExamCourse(studentId, recId, updatedBy);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCancelRecExamCourse(String studentId, String recId, String updatedBy) {
		BzrGjtRecResult gjtRecResult = queryById(recId);
		// 检查当前该选课的状态
		if (NumberUtils.toInt(gjtRecResult.getExamState()) != ExamState.APPOINTMENT.getValue()) {
			log.error("课程状态为:" + gjtRecResult.getExamState() + "，不能进行取消预约操作。");
			return false;
		}
		Date now = new Date();
		// 设置选课状态
		gjtRecResult.setExamState(ExamState.UNAPPOINTMENT.getValue() + "");
		gjtRecResult.setUpdatedBy(updatedBy);
		gjtRecResult.setUpdatedDt(now);
		gjtRecResultDao.save(gjtRecResult);
		// 考试记录表设置为未有效状态
		gjtExamRecordDao.inactiveExamState(recId);
		// 删除考试表里面的记录
		gjtExamPlanDao.deletedExamPlan(gjtRecResult.getStudentId(), gjtRecResult.getCourseId(), updatedBy, now);
		return true;
	}

	@Override
	public Page<BzrGjtExamPoint> queryExamPointByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtExamPoint> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamPointDao.findAll(spec, pageRequest);
	}

	@Override
	public List<BzrGjtExamPoint> queryExamPointBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<BzrGjtExamPoint> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamPointDao.findAll(spec, sort);
	}

	@Override
	public boolean updateRecExamPoint(String studentId, String termId, String examPointId, String updatedBy) {
		int num = recResultDao.reserveRecExamPoint(studentId, termId, examPointId);
		return num != 0;
	}

	/**
	 * 根据学员信息查询学员详细信息
	 *
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> queryStudent(Map<String, Object> map) {

		StringBuilder sql = new StringBuilder();
		sql.append("  		SELECT");
		sql.append("  			gsi.STUDENT_ID,gsi.ZP,gsi.XM,gsi.XH,gsi.SJH MOBILEPHONE,gsi.SFZH CARDNO,GSI.DZXX EMAIL,GSI.TXDZ ADDRESS,gs.ZYMC,gs.SPECIALTY_ID,gs.BXXF,gs.ZDBYXF,gs.ZXF,gsi.RXNY,gsi.SC_CO,");
		sql.append(" 			(SELECT gog.ORG_NAME FROM GJT_ORG gog WHERE gog.IS_DELETED='N' AND gog.ID=gsi.ORG_ID) ORGNAME,");
		sql.append("  			(SELECT gge.GRADE_NAME FROM GJT_GRADE gge WHERE gge.IS_DELETED='N' AND gge.GRADE_ID=gci.GRADE_ID) GRADE_NAME,");
		sql.append("  			(SELECT gso.XXMC FROM GJT_SCHOOL_INFO gso WHERE gso.IS_DELETED='N' AND gsi.XX_ID=gso.ID) XXMC,");
		sql.append("  			(SELECT gsc.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) CENTER_NAME,");
		sql.append("  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = gsi.xbm AND TSD.TYPE_CODE = 'Sex') SEX,");
		sql.append("  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  			(SELECT SUM( NVL( GRR.GET_CREDITS, 0 )) FROM GJT_REC_RESULT GRR WHERE GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID) SUM_CREDITS, ");
		sql.append("  			(SELECT COUNT( grr.COURSE_ID ) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID ) COUNT_COURSE,");
		sql.append("  			(SELECT COUNT( grr.COURSE_ID ) FROM GJT_REC_RESULT grr,VIEW_TEACH_PLAN gtp WHERE");
		sql.append("  					grr.IS_DELETED = 'N' AND gtp.IS_DELETED = 'N' AND grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID");
		sql.append("  					AND gtp.SUBJECT_ID IS NOT NULL AND grr.STUDENT_ID = gsi.STUDENT_ID) COUNT_EXAM,");
		sql.append("  			(SELECT COUNT( GRR.COURSE_ID ) FROM GJT_REC_RESULT GRR WHERE GRR.IS_DELETED = 'N' AND GRR.EXAM_SCORE >= 60 AND GRR.STUDENT_ID = GSI.STUDENT_ID) PASS_COURSE_COUNT,");
		sql.append("  			(SELECT COUNT( GRR.COURSE_ID ) FROM GJT_REC_RESULT GRR WHERE GRR.IS_DELETED = 'N' AND GRR.EXAM_SCORE1 >= 60 AND GRR.STUDENT_ID = GSI.STUDENT_ID ) PASS_EXAM_COUNT");
		sql.append("  		FROM");
		sql.append("  			GJT_CLASS_INFO gci,");
		sql.append("  			GJT_CLASS_STUDENT gcs,");
		sql.append("  			GJT_STUDENT_INFO gsi");
		sql.append("  		LEFT JOIN GJT_SPECIALTY gs ON");
		sql.append("  			gsi.MAJOR = gs.SPECIALTY_ID");
		sql.append("  		WHERE");
		sql.append("  			gci.IS_DELETED = 'N'");
		sql.append("  			AND gcs.IS_DELETED = 'N'");
		sql.append("  			AND gsi.IS_DELETED = 'N'");
		sql.append("  			AND gs.IS_DELETED = 'N'");
		sql.append("  			AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  			AND gcs.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  			AND gci.GRADE_ID = gsi.NJ");
		sql.append("  			AND gci.CLASS_TYPE = 'teach' ");
		sql.append("  			AND gci.BZR_ID=:bzrId");
		sql.append("  			AND gci.CLASS_ID =:classId");
		sql.append("  			AND gci.GRADE_ID =:gradeId");
		sql.append("  			AND gsi.STUDENT_ID=:studentId");

		return commonDao.queryObjectToMapNative(sql.toString(), map);
	}

	/**
	 * 根据学员id查询每个学期信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryTerm(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append("  	DISTINCT gg.GRADE_CODE TERM_CODE,");
		sql.append("  	gg.GRADE_ID TERM_ID");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE gg");
		sql.append("  INNER JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  	gg.GRADE_ID = gtp.ACTUAL_GRADE_ID");
		sql.append("  	AND gg.IS_DELETED = 'N'");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_REC_RESULT grr ON");
		sql.append("  	gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID AND grr.IS_DELETED='N'");
		sql.append("  INNER JOIN GJT_STUDENT_INFO gsi ON ");
		sql.append(
				"  	grr.STUDENT_ID=gsi.STUDENT_ID AND gsi.IS_DELETED='N' AND gsi.STUDENT_ID=:studentId ORDER BY gg.GRADE_CODE ASC");

		map.put("studentId", id);
		List<Map<String, Object>> list = commonDao.queryForMapListNative(sql.toString(), map);
		return list;
	}

	/**
	 * 每个学期的明细
	 *
	 * @param id
	 * @param term
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryStudentSourceDetail(String id, String term) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	gtp.TEACH_PLAN_ID,");
		sql.append("  	grr.STUDENT_ID,");
		sql.append(
				"  	(SELECT gg.GRADE_NAME FROM GJT_GRADE gg WHERE gg.GRADE_ID=gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED='N' AND ROWNUM<2) TERM_NAME,");
		sql.append(
				"  	(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'CourseType' AND tsd.code = gtp.KCLBM ) KCLBM_NAME,");
		sql.append(
				"  	(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'ExaminationMode' AND tsd.code = gtp.KSFS ) KSFS_NAME,");
		sql.append("  	gc.KCMC,");
		sql.append("  	gtp.XF,");
		sql.append("  	nvl(grr.GET_CREDITS,0) GET_CREDITS,");
		sql.append(
				"  	NVL((SELECT gep.XK_PERCENT FROM GJT_EXAM_PLAN_NEW gep WHERE gep.COURSE_ID=gtp.COURSE_ID AND gtp.KSFS=gep.\"TYPE\" AND gep.IS_DELETED=0 AND gep.XX_ID=grr.XX_ID AND ROWNUM<2),0) XK_PERCENT,");
		sql.append("  	grr.EXAM_SCORE,");
		sql.append("  	grr.EXAM_SCORE1,");
		sql.append("  	grr.EXAM_SCORE2,");
		sql.append("  	grr.EXAM_STATE,");
		sql.append(
				"  	(SELECT DISTINCT COUNT(*) FROM GJT_LEARN_REPAIR glr WHERE glr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID AND glr.STUDENT_ID=grr.STUDENT_ID AND glr.COURSE_CODE=gtp.COURSE_CODE) EXAM_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  INNER JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  	grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID ");
		sql.append("  	AND grr.STUDENT_ID = :studentId ");
		sql.append("  INNER JOIN GJT_COURSE gc ON");
		sql.append("  	gtp.COURSE_ID = gc.COURSE_ID ");
		sql.append("  	WHERE grr.IS_DELETED='N' AND gtp.IS_DELETED='N' AND gc.IS_DELETED='N'");
		sql.append("    AND GTP.ACTUAL_GRADE_ID = :term");
		sql.append("  ORDER BY gtp.CREATED_DT");

		map.put("studentId", id);
		map.put("term", term);
		List<Map<String, Object>> list = commonDao.queryForMapListNative(sql.toString(), map);
		return list;
	}

	/**
	 * 查询学分 使用下面的方法 getCreditInfoAnd(Map<String, Object> params)
	 *
	 * @param id
	 * @param specialtyId
	 * @return
	 */
	@Override
	@Deprecated
	public List getCreditInfoAnd(String id, String specialtyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append("  	tsd.NAME,");
		sql.append("  	gsi.MAJOR,");
		sql.append("  	gtp.KCLBM,");
		sql.append("  	gtp.kclbm kclbm_code,");
		sql.append("  	COUNT( grr.COURSE_ID ) COURSE_COUNT,");
		sql.append("  	SUM( NVL( gtp.XF, 0 )) XF_COUNT,");
		sql.append("  	SUM( NVL( grr.GET_CREDITS, 0 )) AS GET_POINT,");
		// sql.append(" NVL((SELECT COUNT(*) FROM GJT_REC_RESULT grrr WHERE
		// grrr.IS_DELETED='N' AND grrr.REC_ID=grr.REC_ID AND
		// grrr.EXAM_SCORE2>60),0) PASS,");
		sql.append("  	NVL(gsml.SCORE,0) AS LIMIT_SCORE");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE gg,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_SPECIALTY_MODULE_LIMIT gsml ON");
		sql.append("  	gsi.MAJOR = gsml.SPECIALTY_ID,");
		sql.append("  	VIEW_TEACH_PLAN gtp ");
		sql.append("  LEFT JOIN GJT_COURSE gc ON gtp.COURSE_ID=gc.COURSE_ID");
		sql.append("  LEFT JOIN TBL_SYS_DATA tsd ON");
		sql.append("  	tsd.TYPE_CODE = 'CourseType'");
		sql.append("  	AND tsd.CODE = gtp.KCLBM,");
		sql.append("  	GJT_REC_RESULT grr ");
		sql.append("  WHERE");
		sql.append("  	gg.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED = 'N'");
		sql.append("  	AND gc.IS_DELETED = 'N'");
		sql.append("  	AND gsml.IS_DELETED = 'N'");
		sql.append("  	AND gg.GRADE_ID=gtp.ACTUAL_GRADE_ID");
		sql.append("  	AND grr.COURSE_ID = gtp.COURSE_ID");
		sql.append("  	AND gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID");
		sql.append("  	AND gsi.STUDENT_ID=grr.STUDENT_ID");
		sql.append("  	AND gsml.SPECIALTY_ID = gsi.MAJOR");
		sql.append("  	AND gsml.MODULE_ID = tsd.ID");
		sql.append("  	AND GSI.MAJOR = :specialtyId ");
		sql.append("  	AND gsi.STUDENT_ID =:studentId");
		sql.append("  GROUP BY");
		sql.append("  	GTP.KCLBM,");
		sql.append("  	tsd.NAME,");
		sql.append("  	GSML.SCORE,");
		sql.append("  	gsi.MAJOR");
		sql.append("  ORDER BY");
		sql.append("  	GTP.KCLBM");

		map.put("studentId", id);
		map.put("specialtyId", specialtyId);
		return commonDao.queryForMapListNative(sql.toString(), map);
	}


	/**
	 * 学员学情详情页-- 学分详情
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List getCreditInfoAnd(Map<String, Object> param) {
		return recResultDao.getCreditInfoAnd(param);
	}

	@Override
	public List getPassCreditInfoAnd(String id, String specialtyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select dic.name kclbm, gtp.kclbm kclbm_code,DIC.ID KCLBM_ID, sum(gtp.xf) xf, count(1) pass  from VIEW_TEACH_PLAN   gtp,"
						+ "  gjt_rec_result   grr,   gjt_student_info gsi,   tbl_sys_data     dic  where gtp.teach_plan_id = grr.teach_plan_id"
						+ "   and gsi.student_id = grr.student_id  and gtp.kclbm = dic.code  and dic.is_deleted = 'N'    and gtp.is_deleted = 'N'"
						+ "   and grr.is_deleted = 'N'  and gsi.is_deleted = 'N'  and dic.type_code = 'CourseType'  and grr.exam_score2 >= 60 "
						+ "  and gsi.STUDENT_ID = :studentId  group by gtp.kclbm, dic.name,dic.ID  order by gtp.kclbm");

		map.put("studentId", id);
		List<Map<String, Object>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;
	}

	@Override
	public List getMinAndSum(String id, String specialtyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT to_char(sum(d.xf)) XF,  to_char(sum(d.zdf)) ZDXF,to_char(sum(d.YXXF)) YXXF from (select t.kclbm KCLBM,"
						+ " sum(t.Score) XF, count(*) KCSL, t.kclbm_code,sum(EXAM_SCORE2) YXXF,(select sum(a.score) "
						+ " from gjt_specialty_module_limit a, tbl_sys_data b  WHERE a.MODULE_ID = b.id AND b.name = t.kclbm "
						+ " and a.is_deleted='N' and b.is_deleted='N' AND a.specialty_id = :specialtyId ) as ZDF"
						+ " from (SELECT (SELECT tsd.name  FROM tbl_sys_data tsd WHERE IS_DELETED='N' AND tsd.type_code = 'CourseType'"
						+ " AND tsd.code = gtp.course_type_id) AS KCLBM, (SELECT GTP.Score FROM GJT_REC_RESULT grr"
						+ " WHERE grr.TEACH_PLAN_ID = gtp.id and vs.STUDENT_ID = grr.student_id and grr.is_deleted = 'N' "
						+ " and grr.exam_score2 >= 60) AS EXAM_SCORE2, GTP.Score,gtp.course_type_id as kclbm_code FROM"
						+ " gjt_grade_specialty_plan gtp,GJT_COURSE   gc,  GJT_TERM_INFO gti, GJT_SPECIALTY  gs,"
						+ " view_student_info vs WHERE gtp.course_id = gc.course_id  AND gs.specialty_id = gtp.specialty_id"
						+ " AND gc.IS_DELETED = 'N'  AND vs.MAJOR = gtp.specialty_id AND vs.STUDENT_ID=:studentId  and "
						+ " vs.GRADE_ID = gti.grade_id) t group by t.kclbm, t.kclbm_code order by kclbm_code) d   ");
		map.put("studentId", id);
		map.put("specialtyId", specialtyId);
		List<Map<String, Object>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;
	}

	/**
	 * 学员学情列表处理sql
	 * @param searchParams
	 * @return
	 */
	public Map<String,Object> queryLearningSituationsSqlHandler(Map<String,Object> searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  	  SELECT * FROM (");
		sql.append("  	   SELECT");
		sql.append("  			GRADE_ID,CLASS_ID,YEAR_NAME,GRADE_NAME,BJMC,STUDENT_ID,XM,XH,SJH,PYCC,PYCC_NAME,XXZX_ID,SC_NAME,");
		sql.append("  			SPECIALTY_ID,ZYMC,ZXF,ZDBYXF,NOW_TERM,SUM_GET_CREDITS,REC_COUNT,UNPASS_REC_COUNT,");
		sql.append("  			LEARNING_REC_COUNT,UNLEARN_REC_COUNT,PASS_REC_COUNT,START_GRADE,ZP,EENO,XJZT,");
		sql.append("  			LOGIN_TIMES,LOGIN_TIME_COUNT,NVL(IS_ONLINE,'N') IS_ONLINE,LAST_LOGIN_TIME,");
		sql.append("  			(CASE WHEN DEVICE = 'PC' THEN 'PC' WHEN DEVICE = 'PHONE' THEN 'APP' WHEN DEVICE = 'PAD' THEN 'APP' ELSE '--' END) DEVICE,");
		sql.append("  			(CASE WHEN ZXF > 0 THEN FLOOR( SUM_GET_CREDITS / ZXF * 100 ) ELSE 0 END) XF_BL,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR( PASS_REC_COUNT / REC_COUNT * 100 ) ELSE 0 END) PASS_BL");
		sql.append("  	   FROM (  SELECT");
		sql.append("  					GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GCI.BJMC,GSI.STUDENT_ID,gsi.XXZX_ID,");
		sql.append("  					(SELECT gsc.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) SC_NAME,");
		sql.append("  					GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME YEAR_NAME,gsi.AVATAR ZP,gsi.EENO,GSI.XJZT,");
		sql.append("  					(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  					GSY.SPECIALTY_ID,GSY.ZYMC,NVL( GSY.ZDBYXF, 0 ) ZDBYXF,");
		sql.append("  					(SELECT NVL( SUM( GTP.XF ), 0 ) FROM GJT_REC_RESULT GRR,VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) ZXF,");
		sql.append("  					(SELECT GG.GRADE_NAME FROM GJT_GRADE gg WHERE gg.IS_DELETED = 'N' AND gg.START_DATE <= SYSDATE AND gg.END_DATE >= SYSDATE");
		sql.append("  							AND gg.XX_ID =(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' AND org.ORG_TYPE = '1' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID) AND rownum =1) NOW_TERM,");
		sql.append("  					(SELECT SUM( NVL( GRR.GET_CREDITS, 0 )) FROM GJT_REC_RESULT GRR WHERE GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID) SUM_GET_CREDITS,");
		sql.append("  					(SELECT COUNT( GRR.REC_ID ) FROM GJT_REC_RESULT GRR WHERE GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID) REC_COUNT,");
		sql.append("  					(SELECT COUNT( GRR.REC_ID) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '0') UNPASS_REC_COUNT,");
		sql.append("  					(SELECT COUNT( GRR.REC_ID ) FROM GJT_REC_RESULT GRR LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID AND gsr.EXAM_STATE = '2' AND gsr.LOGIN_TIMES > 0) LEARNING_REC_COUNT,");
		sql.append("  					(SELECT COUNT( GRR.REC_ID ) FROM GJT_REC_RESULT GRR LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID AND NVL(gsr.LOGIN_TIMES,0) = 0 ) UNLEARN_REC_COUNT,");
		sql.append("  					(SELECT COUNT( GRR.REC_ID) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '1') PASS_REC_COUNT,");
		sql.append("  					(SELECT SUM( NVL( vss.LOGIN_TIMES, 0 )) FROM  GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND  vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIMES,");
		sql.append("  					(SELECT ROUND( SUM( NVL( gsr.ONLINE_TIME, 0 ))/ 60, 1 ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIME_COUNT,");
		sql.append("  					(SELECT vss.IS_ONLINE  FROM VIEW_STUDENT_STUDY_SITUATION vss  WHERE vss.STUDENT_ID=gsi.STUDENT_ID AND VSS.IS_ONLINE='Y' AND ROWNUM = 1) IS_ONLINE,");
		sql.append("  					(SELECT vss.BYOD_TYPE FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID AND vss.LAST_LOGIN_DATE =(SELECT MAX( vs.LAST_LOGIN_DATE ) FROM VIEW_STUDENT_STUDY_SITUATION vs");
		sql.append("  								WHERE vs.STUDENT_ID = GSI.STUDENT_ID AND ROWNUM = 1) AND ROWNUM = 1) DEVICE,");
		sql.append("  					(SELECT FLOOR( SYSDATE - MAX( gsr.LAST_LOGIN_DATE )) FROM VIEW_STUDENT_STUDY_SITUATION gsr WHERE gsr.STUDENT_ID = gsi.STUDENT_ID) LAST_LOGIN_TIME,");
		sql.append("  					(SELECT g.GRADE_NAME FROM GJT_GRADE g WHERE g.GRADE_ID = gsi.NJ AND g.IS_DELETED = 'N' AND rownum = 1) START_GRADE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GR,");
		sql.append("  					GJT_YEAR GY,");
		sql.append("  					GJT_CLASS_INFO GCI,");
		sql.append("  					GJT_CLASS_STUDENT GCS,");
		sql.append("  					GJT_STUDENT_INFO GSI,");
		sql.append("  					GJT_SPECIALTY GSY");
		sql.append("  				WHERE");
		sql.append("  					GR.IS_DELETED = 'N'");
		sql.append("  					AND GCI.IS_DELETED = 'N'");
		sql.append("  					AND GSI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GSY.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  					AND GR.GRADE_ID = GCI.GRADE_ID");
		sql.append("  					AND GY.GRADE_ID = GR.YEAR_ID");
		sql.append("  					AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  					AND GCI.CLASS_ID = :classId");
		sql.append("  					AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  					AND GSI.MAJOR = GSY.SPECIALTY_ID");

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		if (EmptyUtils.isNotEmpty(studyId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxId);
		}

		params.put("classId", ObjectUtils.toString(searchParams.get("classId")));

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GR.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%"+ObjectUtils.toString(searchParams.get("GRADE_NAME"))+"%");
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			params.put("BJMC", "%"+ObjectUtils.toString(searchParams.get("BJMC"))+"%");
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
			sql.append(" AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID",ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))){
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("))  WHERE 1 = 1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XF_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL > "+ObjectUtils.toString(searchParams.get("XF_BL"))+"");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL < "+ObjectUtils.toString(searchParams.get("XF_BL"))+"");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL >= "+ObjectUtils.toString(searchParams.get("XF_BL"))+"");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL <= "+ObjectUtils.toString(searchParams.get("XF_BL"))+"");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL = "+ObjectUtils.toString(searchParams.get("XF_BL"))+"");
			}
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PASS_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL > "+ObjectUtils.toString(searchParams.get("PASS_BL"))+"");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL < "+ObjectUtils.toString(searchParams.get("PASS_BL"))+"");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL >= "+ObjectUtils.toString(searchParams.get("PASS_BL"))+"");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL <= "+ObjectUtils.toString(searchParams.get("PASS_BL"))+"");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL = "+ObjectUtils.toString(searchParams.get("PASS_BL"))+"");
			}
		}
/*
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("passState")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("passState"), ""))) {// 不满足
				sql.append(" AND temp.credits_count < temp.credits_min");
			}
			if ("1".equals(ObjectUtils.toString(searchParams.get("passState"), ""))) {// 满足
				sql.append(" AND temp.credits_count >= temp.credits_min");
			}
		}
*/

		Map<String,Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql",sql.toString());
		handlerMap.put("params",params);
		return handlerMap;
	}



	/**
	 * 获取班级学员的学情
	 * @param searchParams
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> queryLearningSituations(Map<String, Object> searchParams,PageRequest pageRequest) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.queryLearningSituationsSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForPageNative(sql.toString(),params,pageRequest);
	}

	/**
	 * 根据条件查询全部学员学情
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryAllLearningSituations(Map<String, Object> searchParams) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.queryLearningSituationsSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql,params);
	}

	/**
	 * 根据条件获取此班级所有学员所有科目的考试预约情况列表
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> queryStudentRecResultPage(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		String termId = gjtTermInfoDao.findByClassCurrentTerm(ObjectUtils.toString(searchParams.get("classId"), ""));

		sql.append("  SELECT");
		sql.append("  	t.student_id,");
		sql.append("  	( SELECT");
		sql.append("  			tsd.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA tsd");
		sql.append("  		WHERE");
		sql.append("  			tsd.TYPE_CODE = 'TrainingLevel'");
		sql.append("  			AND tsd.is_deleted = 'N'");
		sql.append("  			AND tsd.CODE = t.PYCC");
		sql.append("  	) pycc_name,");
		sql.append("  	t.PYCC,");
		sql.append("  	(SELECT");
		sql.append("  			gti.TERM_NAME");
		sql.append("  		FROM");
		sql.append("  			gjt_term_info gti");
		sql.append("  		INNER JOIN gjt_student_term gst ON");
		sql.append("  			gti.term_id = gst.term_id");
		sql.append("  		WHERE");
		sql.append("  			gti.is_deleted = 'N'");
		sql.append("  			AND SYSDATE >= gti.START_DATE");
		sql.append(" AND SYSDATE <= gti.END_DATE");
		sql.append("  			AND gst.STUDENT_ID = t.STUDENT_ID");
		sql.append("  			AND rownum <= 1");
		sql.append("  	) term_name, t.XH,");
		sql.append("  	t.SJH, t.xm,");
		sql.append("  	d.zymc, d.SPECIALTY_ID,");
		sql.append("  	e.COURSE_ID, gc.KCMC,");
		sql.append("  	e.TERM_ID, e.IS_RESERVE,");
		sql.append("  	w.grade_name, w.GRADE_ID,");
		sql.append("  	e.rec_id CANEXAM_NUM,");
		sql.append("  	f.id ALREADY_EXAMNUM,");
		sql.append("  	h.name EXAM_POINT_NAME,");
		sql.append("    gtp.KSFS,");
		sql.append(
				"    (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GTP.KSFS AND TSD.TYPE_CODE = 'ExaminationMode' ) KSFS_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO t");
		sql.append("  INNER JOIN GJT_CLASS_STUDENT b ON");
		sql.append("  	b.student_id = t.student_id");
		sql.append("  	AND b.is_deleted = 'N'");
		sql.append("  INNER JOIN GJT_CLASS_INFO c ON");
		sql.append("  	c.class_id = b.class_id");
		sql.append("  	AND c.is_deleted = 'N'");
		sql.append("  LEFT JOIN GJT_SPECIALTY d ON");
		sql.append("  	d.specialty_id = t.major");
		sql.append("  INNER JOIN GJT_SIGNUP u ON");
		sql.append("  	u.student_id = t.student_id");
		sql.append("  	AND u.is_deleted = 'N'");
		sql.append("  INNER JOIN GJT_ENROLL_BATCH v ON");
		sql.append("  	v.enroll_batch_id = u.enroll_batch_id");
		sql.append("  	AND v.is_deleted = 'N'");
		sql.append("  INNER JOIN GJT_GRADE w ON");
		sql.append("  	w.grade_id = v.grade_id");
		sql.append("  	AND w.is_deleted = 'N'");
		sql.append("  LEFT JOIN GJT_REC_RESULT e ON");
		sql.append("  	e.student_id = t.student_id");
		sql.append("  	AND e.is_deleted = 'N'  ");
		sql.append(" LEFT JOIN VIEW_TEACH_PLAN gtp ON e.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID AND gtp.IS_DELETED='N'");
		sql.append("  LEFT JOIN GJT_COURSE gc ON ");
		sql.append("  	e.COURSE_ID=gc.COURSE_ID");
		sql.append("  LEFT JOIN GJT_EXAM_RECORD f ON");
		sql.append("  	f.rec_id = e.rec_id");
		sql.append("  	AND f.exam_state = '2'");//
		sql.append("  LEFT JOIN GJT_STUDENT_EXAM_POINT g ON");
		sql.append("  	g.student_id = t.student_id");
		sql.append("  	AND g.term_id = :termId");//
		sql.append("  	AND g.is_deleted = 'N'");
		sql.append("  LEFT JOIN GJT_EXAM_POINT h ON");
		sql.append("  	h.id = g.exam_point_id");
		sql.append("  WHERE");
		sql.append("  	t.is_deleted = 'N'");
		sql.append("  	AND c.class_id = :classId");

		params.put("termId", termId);
		params.put("classId", ObjectUtils.toString(searchParams.get("classId"), ""));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("xm"), ""))) {
			sql.append("  	AND t.XM LIKE :xm ");
			params.put("xm", "%" + ObjectUtils.toString(searchParams.get("xm"), "") + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("xh"), ""))) {
			sql.append("  	AND t.XH LIKE :xh ");
			params.put("xh", "%" + ObjectUtils.toString(searchParams.get("xh"), "") + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("specialtyId"), ""))) {
			sql.append("  	AND d.SPECIALTY_ID= :specialtyId ");
			params.put("specialtyId", ObjectUtils.toString(searchParams.get("specialtyId"), ""));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("gradeId"), ""))) {
			sql.append("  	AND w.GRADE_ID= :gradeId ");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId"), ""));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("pycc"), ""))) {
			sql.append("  	AND t.PYCC = :pycc ");
			params.put("pycc", ObjectUtils.toString(searchParams.get("pycc"), ""));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("kcmc"), ""))) {
			sql.append("  	AND gc.KCMC LIKE :kcmc ");
			params.put("kcmc", "%" + ObjectUtils.toString(searchParams.get("kcmc"), "") + "%");
		}

		String chooseType = ObjectUtils.toString(searchParams.get("chooseType"), "");
		if (EmptyUtils.isNotEmpty(chooseType)) {
			if ("1".equals(chooseType)) {
				sql.append(" AND F.ID IS NULL");// 待预约考试
			}
			if ("2".equals(chooseType)) {
				sql.append(" AND F.ID IS NOT NULL");// 已预约考试
			}
			if ("3".equals(chooseType)) {
				sql.append(" AND H.NAME IS NULL");// 待预约考点
			}
			if ("4".equals(chooseType)) {
				sql.append(" AND H.NAME IS NOT NULL");// 已预约考点
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ksfs"), ""))) {
			sql.append("  AND gtp.KSFS= :ksfs");
			params.put("ksfs", ObjectUtils.toString(searchParams.get("ksfs")));
		}
		// AND F.ID IS NOT NULL AND H.NAME IS NOT NULL

		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 根据条件获取此班级学员的全部预约信息
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryStudentRecResults(Map<String, Object> searchParams) {
		String termId = gjtTermInfoDao.findByClassCurrentTerm(ObjectUtils.toString(searchParams.get("classId"), ""));
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  SELECT");
		sql.append("  	t.student_id,");
		sql.append("  	( SELECT");
		sql.append("  			tsd.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA tsd");
		sql.append("  		WHERE");
		sql.append("  			tsd.TYPE_CODE = 'TrainingLevel'");
		sql.append("  			AND tsd.is_deleted = 'N'");
		sql.append("  			AND tsd.CODE = t.PYCC");
		sql.append("  	) pycc_name,");
		sql.append("  	t.PYCC,");
		sql.append("  	(SELECT");
		sql.append("  			gti.TERM_NAME");
		sql.append("  		FROM");
		sql.append("  			gjt_term_info gti");
		sql.append("  		INNER JOIN gjt_student_term gst ON");
		sql.append("  			gti.term_id = gst.term_id");
		sql.append("  		WHERE");
		sql.append("  			gti.is_deleted = 'N'");
		sql.append("  			AND SYSDATE >= gti.START_DATE");
		sql.append(" AND SYSDATE <= gti.END_DATE");
		sql.append("  			AND gst.STUDENT_ID = t.STUDENT_ID");
		sql.append("  			AND rownum <= 1");
		sql.append("  	) term_name, t.XH,");
		sql.append("  	t.SJH, t.xm,");
		sql.append("  	d.zymc, d.SPECIALTY_ID,");
		sql.append("  	e.COURSE_ID, gc.KCMC,");
		sql.append("  	e.TERM_ID, e.IS_RESERVE,");
		sql.append("  	w.grade_name, w.GRADE_ID,");
		sql.append("  	e.rec_id CANEXAM_NUM,");
		sql.append("  	f.id ALREADY_EXAMNUM,");
		sql.append("  	h.name EXAM_POINT_NAME,");
		sql.append("    gtp.KSFS,");
		sql.append(
				"    (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GTP.KSFS AND TSD.TYPE_CODE = 'ExaminationMode' ) KSFS_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO t");
		sql.append("  INNER JOIN GJT_CLASS_STUDENT b ON");
		sql.append("  	b.student_id = t.student_id");
		sql.append("  	AND b.is_deleted = 'N'");
		sql.append("  INNER JOIN GJT_CLASS_INFO c ON");
		sql.append("  	c.class_id = b.class_id");
		sql.append("  	AND c.is_deleted = 'N'");
		sql.append("  LEFT JOIN GJT_SPECIALTY d ON");
		sql.append("  	d.specialty_id = t.major");
		sql.append("  INNER JOIN GJT_SIGNUP u ON");
		sql.append("  	u.student_id = t.student_id");
		sql.append("  	AND u.is_deleted = 'N'");
		sql.append("  INNER JOIN GJT_ENROLL_BATCH v ON");
		sql.append("  	v.enroll_batch_id = u.enroll_batch_id");
		sql.append("  	AND v.is_deleted = 'N'");
		sql.append("  INNER JOIN GJT_GRADE w ON");
		sql.append("  	w.grade_id = v.grade_id");
		sql.append("  	AND w.is_deleted = 'N'");
		sql.append("  LEFT JOIN GJT_REC_RESULT e ON");
		sql.append("  	e.student_id = t.student_id");
		sql.append("  	AND e.is_deleted = 'N'  ");
		sql.append(" LEFT JOIN VIEW_TEACH_PLAN gtp ON e.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID AND gtp.IS_DELETED='N'");
		sql.append("  LEFT JOIN GJT_COURSE gc ON ");
		sql.append("  	e.COURSE_ID=gc.COURSE_ID");
		sql.append("  LEFT JOIN GJT_EXAM_RECORD f ON");
		sql.append("  	f.rec_id = e.rec_id");
		sql.append("  	AND f.exam_state = '2'");//
		sql.append("  LEFT JOIN GJT_STUDENT_EXAM_POINT g ON");
		sql.append("  	g.student_id = t.student_id");
		sql.append("  	AND g.term_id = :termId");//
		sql.append("  	AND g.is_deleted = 'N'");
		sql.append("  LEFT JOIN GJT_EXAM_POINT h ON");
		sql.append("  	h.id = g.exam_point_id");
		sql.append("  WHERE");
		sql.append("  	t.is_deleted = 'N'");
		sql.append("  	AND c.class_id = :classId");

		params.put("termId", termId);
		params.put("classId", ObjectUtils.toString(searchParams.get("classId"), ""));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("xm"), ""))) {
			sql.append("  	AND t.XM LIKE :xm ");
			params.put("xm", "%" + ObjectUtils.toString(searchParams.get("xm"), "") + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("xh"), ""))) {
			sql.append("  	AND t.XH LIKE :xh ");
			params.put("xh", "%" + ObjectUtils.toString(searchParams.get("xh"), "") + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("specialtyId"), ""))) {
			sql.append("  	AND d.SPECIALTY_ID= :specialtyId ");
			params.put("specialtyId", ObjectUtils.toString(searchParams.get("specialtyId"), ""));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("gradeId"), ""))) {
			sql.append("  	AND w.GRADE_ID= :gradeId ");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId"), ""));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("pycc"), ""))) {
			sql.append("  	AND t.PYCC = :pycc ");
			params.put("pycc", ObjectUtils.toString(searchParams.get("pycc"), ""));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("kcmc"), ""))) {
			sql.append("  	AND gc.KCMC LIKE :kcmc ");
			params.put("kcmc", "%" + ObjectUtils.toString(searchParams.get("kcmc"), "") + "%");
		}
		sql.append(" ORDER BY t.XM");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 根据班级的条件信息导出班级的考试预约信息
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public HSSFWorkbook exportStudentRecResultsToExcel(Map<String, Object> searchParams) {
		List<Map<String, Object>> infos = this.queryStudentRecResults(searchParams);

		try {
			HSSFWorkbook book;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("姓名");
			row.createCell(colIdx++).setCellValue("学号");
			row.createCell(colIdx++).setCellValue("年级");
			row.createCell(colIdx++).setCellValue("学期");
			row.createCell(colIdx++).setCellValue("专业");
			row.createCell(colIdx++).setCellValue("课程");
			row.createCell(colIdx++).setCellValue("考试方式");
			row.createCell(colIdx++).setCellValue("考试预约状态");
			row.createCell(colIdx++).setCellValue("考点预约状态");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (Map info : infos) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("XM"), "")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("XH"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("GRADE_NAME"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("TERM_NAME"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("ZYMC"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("KCMC"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("KSFS_NAME"), "")));

				String ALREADY_EXAMNUM = ObjectUtils.toString(info.get("ALREADY_EXAMNUM"), "");
				if (EmptyUtils.isNotEmpty(ALREADY_EXAMNUM)) {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString("已预约"));
				} else {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString("待预约"));
				}

				String EXAM_POINT_NAME = ObjectUtils.toString(info.get("EXAM_POINT_NAME"), "");
				if (EmptyUtils.isNotEmpty(EXAM_POINT_NAME)) {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString("已预约"));
				} else {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString("待预约"));
				}
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据教学计划获取此教学计划里面拥有的考试方式
	 *
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryExamTypeByTeachplan() {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	TSD.NAME,");
		sql.append("  	tsd.CODE");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA TSD,");
		sql.append("  	VIEW_TEACH_PLAN gtp");
		sql.append("  WHERE");
		sql.append("  	TSD.IS_DELETED = 'N'");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  	AND TSD.CODE = GTP.KSFS");
		sql.append("  	AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  GROUP BY");
		sql.append("  	tsd.NAME,");
		sql.append("  	tsd.CODE");

		return commonDao.queryForMapListNative(sql.toString(), null);
	}

	/**
	 * 学情分析--课程学情
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> getCourseListPage(Map<String, Object> searchParams, PageRequest pageRequest) {

		return recResultDao.getCourseListPage(searchParams,pageRequest);
	}

	/**
	 * 获取班级数据用于导出
	 *
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCourseList(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  gsi.STUDENT_ID,");
		sql.append("    gsi.XM,");
		sql.append("  	gci.CLASS_ID,");
		sql.append("  	gti.TERM_ID,");
		sql.append("  	gti.TERM_NAME,");
		sql.append("  	gc.KCMC,");
		sql.append("  	gc.COURSE_ID,");
		sql.append("  	gsr.GET_POINT,");
		sql.append("  	gsr.STUDY_SCORE,");
		sql.append("  	gsr.EXAM_SCORE,");
		sql.append("  	gsr.SUM_SCORE,");
		sql.append("  	gsr.SCORE_STATE,");
		sql.append("  	gsr.STUDY_STATE,");
		sql.append("  	gsr.LOGIN_COUNT,");
		sql.append("  	TO_CHAR((NVL(GSR.LOGIN_TIME,0))/60,'FM99999990.09') LOGIN_TIME,");
		sql.append("  	gsr.SCHEDULE");
		sql.append("  FROM");
		sql.append("  	GJT_EMPLOYEE_INFO gei,");
		sql.append("  	GJT_CLASS_INFO gci");
		sql.append("  LEFT JOIN GJT_SPECIALTY gs ON");
		sql.append("  	gci.SPECIALTY_ID = gs.SPECIALTY_ID,");
		sql.append("  	GJT_GRADE gg,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	GJT_STUDENT_TERM gst,");
		sql.append("  	GJT_TERM_INFO gti,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_REC_RESULT grr ON");
		sql.append("  	gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_STUDY_RECORD gsr ON");
		sql.append("  	grr.STUDENT_ID = gsr.STUDENT_ID");
		sql.append("  LEFT JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  	gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID,");
		sql.append("  	GJT_COURSE gc");
		sql.append("  WHERE");
		sql.append("  	gei.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gti.IS_DELETED = 'N'");
		sql.append("  	AND gg.IS_DELETED = 'N'");
		sql.append("  	AND gc.IS_DELETED = 'N'");
		sql.append("  	AND gci.GRADE_ID = gg.GRADE_ID");
		sql.append("  	AND gg.GRADE_ID = gsi.NJ");
		sql.append("  	AND gsi.NJ = gtp.GRADE_ID");
		sql.append("  	AND gsi.MAJOR = gs.SPECIALTY_ID");
		sql.append("  	AND gsi.STUDENT_ID = gst.STUDENT_ID");
		sql.append("  	AND gst.TERM_ID = gti.TERM_ID");
		sql.append("  	AND gti.TERM_ID = gtp.TERM_ID");
		sql.append("  	AND gti.TERM_ID = grr.TERM_ID");
		sql.append("  	AND gei.EMPLOYEE_ID = gci.BZR_ID");
		sql.append("  	AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  	AND gcs.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  	AND gsr.COURSE_ID = gc.COURSE_ID");
		sql.append("  	AND gc.COURSE_ID = grr.COURSE_ID");
		sql.append("  	AND grr.COURSE_ID = gtp.COURSE_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'teach'");
		sql.append("  	AND gci.CLASS_ID = :classId ");

		sql.append("  ORDER BY");
		sql.append("  	GSI.XM,");
		sql.append("  	GTI.TERM_CODE");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询学员的登录，时长，学习进度，学习成绩等状态数量
	 *
	 * @param studentId
	 * @return
	 */
	@Override
	public Map<String, Object> queryStuStudyRecord(String studentId) {

		StringBuffer sql = new StringBuffer();

		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  SELECT");
		sql.append("  	COUNT(GSR.CHOOSE_ID) COURSE_COUNTS,");
		sql.append("  	SUM(NVL(GSR.LOGIN_COUNT,0)) LOGIN_COUNTS,");
		sql.append("  	TO_CHAR(SUM(NVL(GSR.LOGIN_TIME,0))/60,'FM99999990.09') LOGIN_TIMES,");// 格式化四舍五入问题。
		sql.append("  	SUM(case when GSR.SCORE_STATE='0' then 1 else 0 end) pass,");
		sql.append("  	SUM(case when GSR.SCORE_STATE='1' then 1 else 0 end) unpass,");
		sql.append(
				"  	SUM(CASE WHEN (gsr.SCHEDULE !='0' AND  gsr.SCHEDULE IS NOT NULL ) THEN 1 ELSE 0 END ) progress_ok,");
		sql.append("  	SUM(CASE WHEN (gsr.SCHEDULE = '0' OR  gsr.SCHEDULE IS  NULL) THEN 1 ELSE 0 END ) progress_no");
		sql.append("  FROM");
		sql.append("  	GJT_STUDY_RECORD GSR");
		sql.append("  WHERE");
		sql.append("  	GSR.STUDENT_ID = :studentId");

		params.put("studentId", studentId);

		return commonDao.queryObjectToMapNative(sql.toString(), params);
	}

	/**
	 * 导出课程学情
	 *
	 * @param classId
	 * @return
	 */
	@Override
	public HSSFWorkbook exportStuCourse(String classId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classId", classId);
		List<Map<String, Object>> infos = this.getCourseList(params);

		try {
			HSSFWorkbook book;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("学员姓名");
			row.createCell(colIdx++).setCellValue("学期");
			row.createCell(colIdx++).setCellValue("课程");
			row.createCell(colIdx++).setCellValue("学习次数");
			row.createCell(colIdx++).setCellValue("学习时长");
			row.createCell(colIdx++).setCellValue("学习进度");
			row.createCell(colIdx++).setCellValue("进度状态");
			row.createCell(colIdx++).setCellValue("学习成绩");
			row.createCell(colIdx++).setCellValue("成绩状态");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (Map info : infos) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("XM"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("TERM_NAME"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("KCMC"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("LOGIN_COUNT"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("LOGIN_TIME"), "")));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("SCHEDULE"), "")));

				String STUDY_STATE = ObjectUtils.toString(info.get("STUDY_STATE"), "");
				if (EmptyUtils.isNotEmpty(STUDY_STATE)) {
					if ("0".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("未学习"));
					}
					if ("1".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("落后"));
					}
					if ("2".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("正常"));
					}
					if ("3".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("学霸"));
					}
					if ("4".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("考核通过"));
					}
				} else {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString("未学习"));
				}
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.get("SUM_SCORE"), "")));

				String SCORE_STATE = ObjectUtils.toString(info.get("SCORE_STATE"), "");

				if (EmptyUtils.isNotEmpty(SCORE_STATE)) {
					if ("0".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("已通过"));
					}
					if ("1".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("未通过"));
					}
					if ("2".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("待登记"));
					}
					if ("3".equals(STUDY_STATE)) {
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString("登记中"));
					}
				} else {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(""));
				}

			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询学员的学情总体情况统计，以及个人的一些基本信息
	 *
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> queryStuStudyCondition(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> args = new HashMap<String, Object>();

		sql.append("  SELECT ");
		sql.append("  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,GSI.AVATAR ZP,gsi.MAJOR,GY.NAME,GG.GRADE_NAME,GS.ZDBYXF CREDITS_MIN,gsi.NJ,gci.BJMC,");
		sql.append("  	NVL((SELECT vss.IS_ONLINE  FROM VIEW_STUDENT_STUDY_SITUATION vss  WHERE vss.STUDENT_ID=gsi.STUDENT_ID AND VSS.IS_ONLINE='Y' AND ROWNUM=1),'N') IS_ONLINE,");
		sql.append("  	(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.XBM AND TSD.TYPE_CODE = 'Sex') SEX,");
		sql.append("  	(SELECT gs.zymc FROM GJT_SPECIALTY gs WHERE gs.IS_DELETED = 'N' AND gs.SPECIALTY_ID = gsi.MAJOR) ZYMC,gsi.PYCC,");
		sql.append("  	(SELECT NVL( SUM( GTP.XF ), 0 ) FROM GJT_REC_RESULT GRR,VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) ZXF,");
		sql.append("  	(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = gsi.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	(SELECT ROUND( NVL( DECODE( SUM( NVL( gsr.MY_ACTCOUNT, 0 )), 0, 0, SUM( NVL( gsr.MY_ACTCOUNT, 0 ))/ SUM( NVL( gsr.ACT_COUNT, 0 ))* 100 ), 0 ), 1 ) ACT_PROGRESS FROM VIEW_STUDENT_STUDY_SITUATION gsr");
		sql.append("  		WHERE gsr.STUDENT_ID = gsi.STUDENT_ID GROUP BY gsr.STUDENT_ID) ACT_PROGRESS,");
		sql.append("  	(SELECT ggg.grade_name FROM GJT_GRADE ggg WHERE ggg.IS_DELETED = 'N' AND ggg.START_DATE < SYSDATE AND ggg.END_DATE > SYSDATE AND ggg.GRADE_ID = gg.GRADE_ID");
		sql.append("  			AND ggg.XX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID) AND rownum = 1) CURRENT_TERM,");
		sql.append("  	SUM( NVL( grr.GET_CREDITS, 0 )) credits_count,COUNT( grr.COURSE_ID ) course_count,COUNT( grr.TEACH_PLAN_ID ) exam_count,");
		sql.append("  	(SELECT SUM( NVL( gsr.LOGIN_TIMES, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIMES,");
		sql.append("  	(SELECT ROUND( SUM( NVL( gsr.ONLINE_TIME, 0 ))/ 60, 1 ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIME,");
		sql.append("  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '0') UNPASS_COURSE,");
		sql.append("  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '1') PASS_COURSE,");
		sql.append("  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '2' AND gsr.LOGIN_TIMES > 0) LEARNING_COURSE,");
		sql.append("  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '2' AND(gsr.LOGIN_TIMES = 0 OR GSR.LOGIN_TIMES IS NULL)) UNLEARN_COURSE,");
		sql.append("  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '3') REGISTER_COURSE,");
		sql.append("  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE IN ('0','1','3')) EXAM_FINISH");
		sql.append("  FROM");
		sql.append("  	GJT_CLASS_INFO gci");
		sql.append("  LEFT JOIN GJT_GRADE gg ON");
		sql.append("  	gg.GRADE_ID = gci.GRADE_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON");
		sql.append("  	gg.YEAR_ID = gy.GRADE_ID,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_REC_RESULT grr ON");
		sql.append("  	gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY gs ON");
		sql.append("  	gsi.MAJOR = gs.SPECIALTY_ID");
		sql.append("  WHERE");
		sql.append("  	gci.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gg.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  	AND gcs.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  	AND gci.CLASS_ID =:classId");
		sql.append("  	AND gsi.STUDENT_ID =:studentId");
		sql.append("  GROUP BY");
		sql.append("  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsi.MAJOR,gsi.PYCC,gsi.NJ,gg.GRADE_ID,GY.NAME,GG.GRADE_NAME,GSI.XBM,GSI.AVATAR,GS.ZDBYXF,gci.BJMC");

		args.put("xxId", params.get("xxId"));
		args.put("classId", params.get("classId"));
		args.put("studentId", params.get("studentId"));
		return commonDao.queryObjectToMapNative(sql.toString(), args);
	}

	/**
	 * 课程学情总览
	 *
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> courseConditionDetials(Map<String, Object> params) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		Map args = new HashMap();

		sql.append("  SELECT");
		sql.append("  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsi.PYCC,gy.NAME,vss.EXAM_SCORE2,grr.REC_ID,grr.TERMCOURSE_ID,gcs.CLASS_ID,gci.COURSE_ID");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO gsi ");
		sql.append("  	LEFT JOIN GJT_SPECIALTY gs ON gsi.MAJOR=gs.SPECIALTY_ID ");
		sql.append("  	LEFT JOIN GJT_REC_RESULT grr ON gsi.STUDENT_ID=grr.STUDENT_ID ");
		sql.append("  	LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID,");
		sql.append("  	GJT_CLASS_INFO gci,");
		sql.append("  	GJT_CLASS_STUDENT gcs");
		sql.append("  LEFT JOIN GJT_GRADE gg ON gg.GRADE_ID=gcs.GRADE_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON gg.YEAR_ID=gy.GRADE_ID");
		sql.append("  WHERE");
		sql.append("  	gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED='N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gs.IS_DELETED='N'");
		sql.append("  	AND gcs.IS_DELETED='N'");
		sql.append("  	AND grr.IS_DELETED='N'");
		sql.append("  	AND gci.CLASS_TYPE='course'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND grr.TERMCOURSE_ID=vss.TERMCOURSE_ID");
		sql.append("  	AND vss.TERMCOURSE_ID=gci.TERMCOURSE_ID");
		sql.append("  	AND gci.COURSE_ID=:courseId");
		sql.append("  	AND grr.TERMCOURSE_ID= :termcourseId");
		sql.append("  	AND gsi.STUDENT_ID = :studentId");

		args.put("courseId",ObjectUtils.toString(params.get("courseId")));
		args.put("termcourseId",ObjectUtils.toString(params.get("termcourseId")));
		args.put("studentId",ObjectUtils.toString(params.get("studentId")));

		Map<String, Object> result = commonDao.queryObjectToMapNative(sql.toString(), args);
		if (EmptyUtils.isNotEmpty(result)) {
			resultMap.put("CLASS_ID", ObjectUtils.toString(result.get("CLASS_ID"), ""));
			resultMap.put("TERMCOURSE_ID", ObjectUtils.toString(result.get("TERMCOURSE_ID"), ""));
			resultMap.put("STUDENT_ID", ObjectUtils.toString(result.get("STUDENT_ID"), ""));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("formMap.TERMCOURSE_ID", ObjectUtils.toString(params.get("termcourseId"), ""));
			param.put("formMap.CLASS_ID", ObjectUtils.toString(result.get("CLASS_ID"), ""));
			param.put("formMap.STUD_ID", ObjectUtils.toString(params.get("studentId"), ""));

			String oclassUrl = AppConfig.getProperty("oclassUrl", "http://oclass.oucnet.cn");
			String jsonObject = HttpClientUtils.doHttpPost(oclassUrl + "/app/teacher/studDetailIndex.do", param, 10000,
					"UTF-8");

			String jsonObject1 = HttpClientUtils.doHttpPost(oclassUrl + "/app/teacher/getStudAnalysisDetail.do", param,
					10000, "UTF-8");

			Map map = (Map) JSON.parse(jsonObject);
			if (EmptyUtils.isNotEmpty(map)) {
				resultMap.putAll(map);
			}

			Map map1 = (Map) JSON.parse(jsonObject1);
			if (EmptyUtils.isNotEmpty(map1)) {
				resultMap.put("MY_POINT", ObjectUtils.toString(map1.get("MY_POINT"), ""));
				resultMap.put("MY_PROGRESS", ObjectUtils.toString(map1.get("MY_PROGRESS"), ""));
			}
		}
		return resultMap;
	}

	/**
	 * 查询出全部选课
	 *
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryAllRecResults() {
		Map param = new HashMap();
		param.put("IS_DELETED","N");
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT grr.REC_ID,TO_CHAR(grr.SYNC_DEVICE_DATE,'yyyyMMddhh24miss') SYNC_DEVICE_DATE FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED=:IS_DELETED ");
		sql.append(" AND (grr.SYNC_DEVICE_DATE IS NULL OR TO_CHAR(grr.SYNC_DEVICE_DATE,'yyyy-MM-dd')<TO_CHAR(SYSDATE-1,'yyyy-MM-dd'))");

		return commonDao.queryForMapListNative(sql.toString(),param);
	}

	/**
	 * 更新用户选课的主要应用设备信息
	 *
	 * @param data
	 */
	@Override
	@Transactional(value="transactionManagerBzr")
	public boolean updateRecResultDeviceMsg(Map<String, Object> data) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE GJT_REC_RESULT grr SET grr.SYNC_DEVICE_DATE=SYSDATE,grr.MAIN_DEVICE=:MAIN_DEVICE,grr.MAIN_DEVICE_PERCENT=:MAIN_DEVICE_PERCENT WHERE grr.REC_ID=:REC_ID");
		int flag = commonDao.updateForMapNative(sql.toString(),data);
		return flag > 0;
	}

	@Override
	public Page<Map<String, Object>> courseLearnConditionDetails(Map<String, Object> searchParams, PageRequest pageRequest) {

		return  recResultDao.courseLearnConditionDetails(searchParams,pageRequest);
	}

	/**
	 * 学支平台--首页批量导出学生学情。
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List<File> getClassConditons(Map searchParams) throws ExecutionException, InterruptedException {
		final Map<String,Object> params = new HashMap<String, Object>();
		params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("BJMC",ObjectUtils.toString(searchParams.get("bjmc")));
		params.put("gradeId",ObjectUtils.toString(searchParams.get("grade_id")));
		String class_ids = ObjectUtils.toString(searchParams.get("class_ids"), "");
		final String[] class_arr;

		final Map<String, String> classInfo = commonMapService.getClassInfoMapByBzrId(ObjectUtils.toString(searchParams.get("BZR_ID")));
		if(EmptyUtils.isNotEmpty(class_ids)){
			if("all".equals(class_ids)){
				class_arr =  classInfo.keySet().toArray(new String[]{});
			}else {
				class_arr = class_ids.split(",");
			}
			if(EmptyUtils.isNotEmpty(class_arr)){//班级id存在
				List<File> workbookList = new ArrayList<File>();
				ExecutorService pool = Executors.newCachedThreadPool();
				List<Future> futureList = new ArrayList<Future>();
				for (int i=0;i<class_arr.length;i++){
					params.put("CLASS_ID",class_arr[i]);
					Callable<File> callable = new Callable<File>() {
						String BJMC = ObjectUtils.toString(params.get("BJMC"));
						String XX_ID = ObjectUtils.toString(params.get("XX_ID"));
						String CLASS_ID = ObjectUtils.toString(params.get("CLASS_ID"));
						String gradeId = ObjectUtils.toString(params.get("gradeId"));
						@Override
						public File call() throws Exception {
							Map<String,Object> tempParams = new HashMap<String, Object>();
							tempParams.put("BJMC",BJMC);
							tempParams.put("XX_ID",XX_ID);
							tempParams.put("CLASS_ID",CLASS_ID);
							tempParams.put("gradeId",gradeId);
							Workbook workbook = gjtStudentService.courseLearnConditionDetails(tempParams);
							File file = new File(classInfo.get(ObjectUtils.toString(tempParams.get("CLASS_ID")))+"-课程学情.xls");
							FileOutputStream fos = new FileOutputStream(file);
							workbook.write(fos);
							fos.close();
							return file;
						}
					};
					Future<File> future = pool.submit(callable);
					futureList.add(future);
				}
				pool.shutdown();
				for(Future<File> f:futureList){
					workbookList.add(f.get());
				}
				return workbookList;
			}
		}
		return null;
	}

	/**
	 * 学支平台--首页批量导出班级的学员信息
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List<File> getClassStudInfo(Map searchParams) throws ExecutionException, InterruptedException {
		final Map<String,Object> params = new HashMap<String, Object>();
		params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("BJMC",ObjectUtils.toString(searchParams.get("bjmc")));
		params.put("gradeId",ObjectUtils.toString(searchParams.get("grade_id")));
		params.put("BZR_ID",ObjectUtils.toString(searchParams.get("BZR_ID")));
		String class_ids = ObjectUtils.toString(searchParams.get("class_ids"), "");
		final String[] class_arr;

		final Map<String, String> classInfo = commonMapService.getClassInfoMapByBzrId(ObjectUtils.toString(searchParams.get("BZR_ID")));
		if(EmptyUtils.isNotEmpty(class_ids)){
			if("all".equals(class_ids)){
				class_arr =  classInfo.keySet().toArray(new String[]{});
			}else {
				class_arr = class_ids.split(",");
			}
			if(EmptyUtils.isNotEmpty(class_arr)){//班级id存在
				List<File> workbookList = new ArrayList<File>();
				ExecutorService pool = Executors.newCachedThreadPool();
				List<Future> futureList = new ArrayList<Future>();
				for (int i=0;i<class_arr.length;i++){
					params.put("CLASS_ID",class_arr[i]);
					Callable<File> callable = new Callable<File>() {
						String CLASS_ID = ObjectUtils.toString(params.get("CLASS_ID"));
						@Override
						public File call() throws Exception {
							Map<String,Object> searchParams = new HashMap<String, Object>();
							searchParams.put("EQ_classId", CLASS_ID);
							HSSFWorkbook workbook = gjtStudentService.exportStudentInfoSpecialtyToExcel(CLASS_ID, searchParams, null);
							File file = new File(classInfo.get(CLASS_ID)+"-学员信息.xls");
							FileOutputStream fos = new FileOutputStream(file);
							workbook.write(fos);
							fos.close();
							return file;
						}
					};
					Future<File> future = pool.submit(callable);
					futureList.add(future);
				}
				pool.shutdown();
				for(Future<File> f:futureList){
					workbookList.add(f.get());
				}
				return workbookList;
			}
		}
		return null;
	}

}
