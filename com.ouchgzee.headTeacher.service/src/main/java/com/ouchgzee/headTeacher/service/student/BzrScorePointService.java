package com.ouchgzee.headTeacher.service.student;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by llx on 2017/02/28.
 */
@Deprecated public interface BzrScorePointService {

	/**
	 * 学习管理=》成绩查询
	 * @return
	 */
	Page getScoreList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》成绩查询（查询条件统计项）
	 * @return
	 */
	int getScoreCount(Map searchParams);

	/**
	 * 学习管理=》学分查询
	 * @return
	 */
	Page getCreditsList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学分查询（查询条件统计项）
	 * @return
	 */
	int getCreditsCount(Map searchParams);

	/**
	 * 学习管理=》课程学情
	 * @return
	 */
	Page getCourseStudyList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》课程班学情
	 * @return
	 */
	Page getCourseClassList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》教学班学情
	 * @return
	 */
	Page getTeachClassList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学员课程学情
	 * @return
	 */
	Page getStudentCourseList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学员专业学情
	 * @return
	 */
	Page getStudentMajorList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》教务班考勤
	 * @return
	 */
	Page getClassLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学员考勤
	 * @return
	 */
	Page getStudentLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 成绩与学分详情页
	 * @param searchParams
	 * @return
	 */
    Map getScorePointDetail(Map<String, Object> searchParams);

	/**
	 * 查看历史成绩
	 * @param formMap
	 * @return
	 */
	List<Map<String,Object>> getHistoryScore(Map<String, Object> formMap);

	/**
	 * 学支平台--学员成绩列表下载
	 * @param searchParams
	 * @return
	 */
    Workbook downLoadScoreListExportXls(Map searchParams);
}
