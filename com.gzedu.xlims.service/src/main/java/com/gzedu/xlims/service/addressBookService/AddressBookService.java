package com.gzedu.xlims.service.addressBookService;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.dto.AddressBookDto;

public interface AddressBookService {

	// 查询教学班级的同学
	public Page<AddressBookDto> findByPageHql(Map<String, Object> params, PageRequest pageRequest);

	// 统计教学班级的同学
	public long findAllCount(Map<String, Object> params);

	// 统计学生的辅导老师
	public long findCourseTeacherCount(Map<String, Object> params);

	public Page<Map> findCourseTeacher(Map<String, Object> params, PageRequest pageRequst);

}
