/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.textbook;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.textbook.GjtStudentAddressDao;
import com.gzedu.xlims.pojo.GjtStudentAddress;
import com.gzedu.xlims.service.textbook.GjtStudentAddressService;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月25日
 * @version 3.0
 *
 */
@Service
public class GjtStudentAddressServiceImpl implements GjtStudentAddressService {
	private static final Log log = LogFactory.getLog(GjtStudentAddressServiceImpl.class);
	@Autowired
	GjtStudentAddressDao gjtStudentAddressDao;

	@Override
	public List<GjtStudentAddress> findByStudentId(String studentId) {
		return gjtStudentAddressDao.findByStudentId(studentId);
	}

	@Override
	public GjtStudentAddress findOne(String addressId) {
		return gjtStudentAddressDao.findOne(addressId);
	}

	@Override
	public GjtStudentAddress save(GjtStudentAddress entity) {
		log.info("entity:[" + entity + "]");
		// 没有收货地址第一条设置为默认收货地址
		List<GjtStudentAddress> list = findByStudentId(entity.getStudentId());
		if(list == null || list.size() == 0) {
			entity.setIsDefault(1);
		} else if (list.size() == 1 && entity.getAddressId().equals(list.get(0).getAddressId())) {
			entity.setIsDefault(1);
		}
		return gjtStudentAddressDao.save(entity);

	}

	@Override
	@Transactional
	public void updateDefualtAddress(GjtStudentAddress address) {
		gjtStudentAddressDao.updateDefaultAddress(address.getStudentId());
		GjtStudentAddress studentAddress = gjtStudentAddressDao.findOne(address.getAddressId());
		studentAddress.setIsDefault(1);
		studentAddress.setUpdatedBy(address.getUpdatedBy());
		studentAddress.setUpdatedDt(new Date());
		gjtStudentAddressDao.save(studentAddress);

	}

}
