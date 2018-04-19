package com.gzedu.xlims.serviceImpl.addressBookImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.addressBook.AddressBookDao;
import com.gzedu.xlims.pojo.dto.AddressBookDto;
import com.gzedu.xlims.service.addressBookService.AddressBookService;

@Service
public class AddressBookServiceImpl implements AddressBookService {
	@Autowired
	private AddressBookDao addressBookDao;

	/**
	 * 查询教学班级的同学
	 */
	@Override
	public Page<AddressBookDto> findByPageHql(Map<String, Object> params, PageRequest pageRequest) {
		return addressBookDao.findByPageHql(params, pageRequest);
	}

	@Override
	public long findAllCount(Map<String, Object> params) {
		return addressBookDao.findAllCount(params);
	}

	@Override
	public long findCourseTeacherCount(Map<String, Object> params) {
		return addressBookDao.findCourseTeacherCount(params);
	}

	@Override
	public Page<Map> findCourseTeacher(Map<String, Object> params, PageRequest pageRequst) {
		return addressBookDao.findCourseTeacher(params, pageRequst);
	}

}
