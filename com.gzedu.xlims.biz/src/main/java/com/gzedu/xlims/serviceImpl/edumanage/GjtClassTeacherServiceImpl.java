/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.edumanage.GjtClassTeacherDao;
import com.gzedu.xlims.pojo.GjtClassTeacher;
import com.gzedu.xlims.service.edumanage.GjtClassTeacherService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年1月22日
 * @version 1.0
 *
 */
@Service
public class GjtClassTeacherServiceImpl implements GjtClassTeacherService {
	@Autowired
	GjtClassTeacherDao gjtClassTeacherDao;

	@Override
	public List<GjtClassTeacher> queryAll(Integer employeeType, String classId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("employeeType", new SearchFilter("employeeType", Operator.EQ, employeeType));
		filters.put("classId", new SearchFilter("classId", Operator.EQ, classId));
		Specification<GjtClassTeacher> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassTeacher.class);
		return gjtClassTeacherDao.findAll(spec);
	}

	@Override
	public Page<GjtClassTeacher> queryAll(Integer employeeType, String classId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("employeeType", new SearchFilter("employeeType", Operator.EQ, employeeType));
		filters.put("classId", new SearchFilter("classId", Operator.EQ, classId));

		Specification<GjtClassTeacher> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassTeacher.class);

		return gjtClassTeacherDao.findAll(spec, pageRequst);
	}

	@Override
	public Page<GjtClassTeacher> queryAll(Integer employeeType, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("employeeType", new SearchFilter("employeeType", Operator.EQ, employeeType));
		Specification<GjtClassTeacher> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassTeacher.class);
		return gjtClassTeacherDao.findAll(spec, pageRequst);
	}

	@Override
	public long findAllCount(Integer employeeType, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("employeeType", new SearchFilter("employeeType", Operator.EQ, employeeType));
		Specification<GjtClassTeacher> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassTeacher.class);
		return gjtClassTeacherDao.count(spec);
	}

	@Override
	public void insert(GjtClassTeacher gjtClassTeacher) {
		gjtClassTeacher.setClassTeacherId(UUIDUtils.random());
		gjtClassTeacherDao.save(gjtClassTeacher);
	}

	@Override
	public void deleteByClassId(String classId, String employeeType) {
		gjtClassTeacherDao.deleteByClassId(classId, employeeType);
	}

}
