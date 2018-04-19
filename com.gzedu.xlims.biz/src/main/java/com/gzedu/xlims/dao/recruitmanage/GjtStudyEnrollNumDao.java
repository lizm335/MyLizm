package com.gzedu.xlims.dao.recruitmanage;

import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.pojo.GjtEnrollBatchNew;
import com.gzedu.xlims.pojo.GjtStudyEnrollNum;

/**
 * 功能说明：
 * @author lulinlin@eenet.com
 * @Date 2016年11月20日
 *
 */
public interface GjtStudyEnrollNumDao 
				extends JpaRepository<GjtStudyEnrollNum, String>, JpaSpecificationExecutor<GjtStudyEnrollNum>{

	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudyEnrollNum g set g.isDeleted=?2 where g.enrollBatchId=?1 ")
	public int deleteById(String id, String str);
	/**
	 * 查询同一招生批次下的学习学习中心的招生数量
	 * @param enrollBatchId
	 * @param xxzxId
	 * @return
	 */
	@Query("SELECT g FROM GjtStudyEnrollNum g where g.isDeleted='N' and g.enrollBatchId = ?1 and g.xxzxId = ?2")
	public GjtStudyEnrollNum findByEnrollBatchIdAndXxzxId(String enrollBatchId, String xxzxId);
		
}
