package com.ouchgzee.study.service.index;

import java.util.List;
import java.util.Map;

public interface IndexService {
	
	@SuppressWarnings("rawtypes")
	Map getHeadTeacher(Map searchParams);

	/**
	 * 获取班级在线学员
	 * @param parms
	 * @return
	 */
	List<Map<String,String>> getOnlineStudent(Map<String, Object> parms);

	/**
	 * 获取班级学员的实时动态
	 * @param param
	 * @return
	 */
	List<Map<String,String>> getStudentDynamic(Map<String, Object> param);
}
