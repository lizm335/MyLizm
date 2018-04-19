package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.GjtExemptExamInstall;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月22日
 * @version 2.5
 */
public interface GjtExemptExamInstallDao extends JpaRepository<GjtExemptExamInstall, String>, JpaSpecificationExecutor<GjtExemptExamInstall>{

	GjtExemptExamInstall findByInstallId(String installId);
	
	@Modifying
	@Query(value="update GjtExemptExamInstall T set T.isDeleted='Y',T.updatedDt=sysdate WHERE T.installId=?1")
	void updateInstall(String installId);

	List<GjtExemptExamInstall> findByXxIdAndIsDeletedAndStatus(String xxId, String booleanNo,String status);

	List<GjtExemptExamInstall> findByXxIdAndCourseIdAndIsDeleted(String xxId, String courseId, String booleanNo);

}
