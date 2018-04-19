package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtStudentAddress;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;

public interface GjtStudentAddressRepository
		extends JpaRepository<GjtStudentAddress, String>, JpaSpecificationExecutor<GjtTextbook> {

	public List<GjtStudentAddress> findByStudentIdAndIsDeleted(String studentId, String isDeleted);

	@Modifying
	@Transactional
	@Query("update GjtStudentAddress a set isDefault = 0 where isDefault = 1 and studentId=?1")
	public void updateDefaultAddress(String studentId);
}
