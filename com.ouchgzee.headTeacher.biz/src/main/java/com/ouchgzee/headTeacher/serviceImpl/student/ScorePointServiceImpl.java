package com.ouchgzee.headTeacher.serviceImpl.student;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.student.ScorePointDao;
import com.ouchgzee.headTeacher.daoImpl.BzrGjtRecResultDaoImpl;
import com.ouchgzee.headTeacher.service.student.BzrScorePointService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by llx on 2017/02/28.
 */
@Deprecated @Service("bzrScorePointServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class ScorePointServiceImpl implements BzrScorePointService {

	@Autowired
	ScorePointDao scorePointDao;

	@Autowired
	private BzrGjtRecResultDaoImpl recResultDao;

	@Autowired
	private CommonDao commonDao;

	/**
	 * 学习管理=》成绩查询
	 * @return
	 */
	@Override
	public Page getScoreList(Map searchParams, PageRequest pageRequst) {
		return scorePointDao.getScoreList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》成绩查询（查询条件统计项）
	 * @return
	 */
	public int getScoreCount(Map searchParams) {
		return scorePointDao.getScoreCount(searchParams);
	}

	/**
	 * 学习管理=》学分查询
	 * @return
	 */
	@Override
	public Page getCreditsList(Map searchParams, PageRequest pageRequst) {
		return scorePointDao.getCreditsList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学分查询（查询条件统计项）
	 * @return
	 */
	public int getCreditsCount(Map searchParams) {
		return scorePointDao.getCreditsCount(searchParams);
	}

	/**
	 * 学习管理=》课程学情
	 * @return
	 */
	public Page getCourseStudyList(Map searchParams, PageRequest pageRequst) {
		return scorePointDao.getCourseStudyList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》课程班学情
	 * @return
	 */
	public Page getCourseClassList(Map searchParams, PageRequest pageRequst) {
		return scorePointDao.getCourseClassList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》教学班学情
	 * @return
	 */
	public Page getTeachClassList(Map searchParams, PageRequest pageRequst) {
		return scorePointDao.getTeachClassList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学员课程学情
	 * @return
	 */
	public Page getStudentCourseList(Map searchParams, PageRequest pageRequst) {
		return scorePointDao.getStudentCourseList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学员专业学情
	 * @return
	 */
	public Page getStudentMajorList(Map searchParams, PageRequest pageRequst){
		return scorePointDao.getStudentMajorList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》教务班考勤
	 * @return
	 */
	public Page getClassLoginList(Map searchParams, PageRequest pageRequst){
		return scorePointDao.getClassLoginList(searchParams, pageRequst);
	}

	/**
	 * 学习管理=》学员考勤
	 * @return
	 */
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequst){
		return scorePointDao.getStudentLoginList(searchParams, pageRequst);
	}

	/**
	 * 成绩与学分详情页
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Map getScorePointDetail(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try {
			List detailList =  scorePointDao.getStudentRecordDetail(searchParams);
			if (EmptyUtils.isNotEmpty(detailList)) {
				resultMap = (Map)detailList.get(0);

				List scoreList = scorePointDao.getScoreList(searchParams);
				resultMap.put("SCORELIST", scoreList);

				List moduleList = recResultDao.getCreditInfoAnd(searchParams);
				resultMap.put("MODULELIST", moduleList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 查看历史成绩
	 *
	 * @param formMap
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getHistoryScore(Map<String, Object> formMap) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String,String> tm = new HashMap<String,String>();
		List<Map<String, String>> list = scorePointDao.getHistoryScore(formMap);
		if(list!=null &&list.size()>0){
			for (Map temp:list) {
				tm.put(ObjectUtils.toString(temp.get("TERM_ID")), ObjectUtils.toString(temp.get("TERM_ID")));
			}
			for (String string:tm.keySet()) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("HISTORY_MSG",new ArrayList());
				for (Map temp:list){
					if(string.equals(ObjectUtils.toString(temp.get("TERM_ID")))){
						map.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
						map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
						if(EmptyUtils.isNotEmpty(ObjectUtils.toString(temp.get("XCX_PERCENT"),""))){
							temp.put("ZJX_PERCENT",100-Integer.parseInt(ObjectUtils.toString(temp.get("XCX_PERCENT"),"0")));
						}else {
							temp.put("ZJX_PERCENT","0");
						}
						((List)map.get("HISTORY_MSG")).add(temp);
					}
				}
				resultList.add(map);
			}
		}
		return resultList;
	}

	/**
	 * 学支平台--学员成绩列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadScoreListExportXls(Map searchParams) {
		List<Map<String, Object>> resultList = scorePointDao.getScoreListNoPage(searchParams);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
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
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BZR_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("FD_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KSFS_NAME")));
				cell = row.createCell(cellIndex++);
				String kcxxbz = ObjectUtils.toString(e.get("KCXXBZ"),"");
				if(EmptyUtils.isNotEmpty(kcxxbz)){
					cell.setCellValue(kcxxbz+"%");
				}else {
					cell.setCellValue("");
				}
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XF")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GET_CREDITS")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("STUDY_TIMES")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ONLINE_TIME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PROGRESS")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE")));
				cell = row.createCell(cellIndex++);

				String examState = ObjectUtils.toString(e.get("EXAM_STATE"),"2");
				if("2".equals(examState)){//学习中没有考试成绩
					cell.setCellValue("");
				}else {
					cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE1")));
				}
				cell = row.createCell(cellIndex++);
				if("3".equals(examState)||"2".equals(examState)){//学习中或者登记中，没有总成绩
					cell.setCellValue("");
				}else {
					cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE2")));
				}
				cell = row.createCell(cellIndex++);
				switch (Integer.parseInt(examState)){
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
					default:cell.setCellValue("");
				}
			}
		}
		return wb;
	}
}







