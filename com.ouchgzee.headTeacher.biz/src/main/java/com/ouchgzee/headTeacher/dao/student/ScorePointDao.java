package com.ouchgzee.headTeacher.dao.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by llx on 2017/02/28.
 */
public interface ScorePointDao {

	Page getScoreList(Map searchParams, PageRequest pageRequst);

	List<Map<String, Object>> getScoreListNoPage(Map searchParams);

	int getScoreCount(Map searchParams);

	Page getCreditsList(Map searchParams, PageRequest pageRequst);

	int getCreditsCount(Map searchParams);

	Page getCourseStudyList(Map searchParams, PageRequest pageRequst);

	Page getCourseClassList(Map searchParams, PageRequest pageRequst);

	Page getTeachClassList(Map searchParams, PageRequest pageRequst);

	Page getStudentCourseList(Map searchParams, PageRequest pageRequst);

	Page getStudentMajorList(Map searchParams, PageRequest pageRequst);

	Page getClassLoginList(Map searchParams, PageRequest pageRequst);

	Page getStudentLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学员选课成绩查询
	 * @param searchParams
	 * @return
	 */
	List getScoreList(Map<String, Object> searchParams);

	/**
	 * 学支平台--学员信息--选课信息总览
	 * @param searchParams
	 * @return
	 */
	List getStudentRecordDetail(Map<String, Object> searchParams);

	/**
	 * 查看历史成绩
	 * @param formMap
	 * @return
	 */
	List getHistoryScore(Map<String, Object> formMap);
}


