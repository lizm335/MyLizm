/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtClassTeacher;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年1月22日
 * @version 1.0
 *
 */
public interface GjtClassTeacherService {
	public List<GjtClassTeacher> queryAll(Integer employeeType, String classId);

	public Page<GjtClassTeacher> queryAll(Integer employeeType, String classId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public void deleteByClassId(String classId, String employeeType);

	public void insert(GjtClassTeacher gjtClassTeacher);

	Page<GjtClassTeacher> queryAll(Integer employeeType, Map<String, Object> searchParams, PageRequest pageRequst);

	long findAllCount(Integer employeeType, Map<String, Object> searchParams);
}
