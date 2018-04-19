package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtStudentAddressRepository;
import com.gzedu.xlims.pojo.GjtStudentAddress;

@Repository
public class GjtStudentAddressDao {

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private GjtStudentAddressRepository gjtStudentAddressRepository;

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 上午10:51:06
	 */
	public List<GjtStudentAddress> findByStudentId(String studentId) {
		return gjtStudentAddressRepository.findByStudentIdAndIsDeleted(studentId, "N");
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 上午11:43:19
	 * @param addressId
	 * @return
	 */
	public GjtStudentAddress findOne(String addressId) {
		return gjtStudentAddressRepository.findOne(addressId);
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 上午11:52:03
	 * @param gjtStudentAddress
	 * @return
	 */
	public GjtStudentAddress save(GjtStudentAddress gjtStudentAddress) {
		return gjtStudentAddressRepository.save(gjtStudentAddress);
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 下午2:10:05
	 * @param studentId
	 */
	public void updateDefaultAddress(String studentId) {
		gjtStudentAddressRepository.updateDefaultAddress(studentId);

	}

}
