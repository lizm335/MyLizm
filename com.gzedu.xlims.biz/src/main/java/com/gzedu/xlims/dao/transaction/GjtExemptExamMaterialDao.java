package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtExemptExamMaterial;
/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月24日
 * @version 2.5
 */


public interface GjtExemptExamMaterialDao extends JpaRepository<GjtExemptExamMaterial,String>,JpaSpecificationExecutor<GjtExemptExamMaterial>{

	List<GjtExemptExamMaterial> findByInstallIdAndIsDeleted(String installId,String booleanNo);
	
	@Modifying
	@Transactional
	@Query(value="delete GjtExemptExamMaterial T WHERE T.installId=?1")
	void deletedMaterial(String installId);
	
	@Modifying
	@Query(value="update GjtExemptExamMaterial T set T.isDeleted='Y',T.updatedDt=sysdate WHERE T.installId=?1")
	void updateMaterial(String installId);
}
