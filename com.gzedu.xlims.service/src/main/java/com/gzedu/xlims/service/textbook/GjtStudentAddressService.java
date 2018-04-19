package com.gzedu.xlims.service.textbook;

import java.util.List;

import com.gzedu.xlims.pojo.GjtStudentAddress;

public interface GjtStudentAddressService {

	/**
	 * 查询学生的收货地址
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 上午10:49:12
	 * @param studentId
	 * @return
	 */
	List<GjtStudentAddress> findByStudentId(String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 上午11:42:27
	 * @param addressId
	 */
	GjtStudentAddress findOne(String addressId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 上午11:51:21
	 * @param gjtStudentAddress
	 * @return
	 */
	GjtStudentAddress save(GjtStudentAddress gjtStudentAddress);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 下午1:59:47
	 * @param address
	 */
	void updateDefualtAddress(GjtStudentAddress address);

}
