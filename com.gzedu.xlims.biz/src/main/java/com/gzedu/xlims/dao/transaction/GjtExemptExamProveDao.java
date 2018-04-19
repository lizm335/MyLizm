package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtExemptExamProve;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月28日
 * @version 2.5
 */
public interface GjtExemptExamProveDao extends JpaRepository<GjtExemptExamProve, String>, JpaSpecificationExecutor<GjtExemptExamProve>{

	List<GjtExemptExamProve> findByExemptExamId(String exemptExamId);

	List<GjtExemptExamProve> findByExemptExamIdAndStudentId(String exemptExamId, String studentId);

}
