package com.gzedu.xlims.dao.recruitmanage;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtEnrollBatchNew;
/**
 * 
 * @author lulinlin@eenet.com
 * @Date 2016年11月20日
 */
public interface GjtEnrollBatchNewDao extends JpaRepository<GjtEnrollBatchNew, String>, JpaSpecificationExecutor<GjtEnrollBatchNew>{
	
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtEnrollBatchNew g set g.isDeleted=?2 where g.enrollBatchId=?1 ")
	public int deleteById(String id, String str);
	
	@Modifying
	@Transactional
	@Query("update GjtEnrollBatchNew g set g.status='2' where g.enrollBatchId=?1 ")
	public void updateStatus(String id);
	
	@Modifying
	@Transactional
	@Query("update GjtEnrollBatchNew g set g.url=?2,g.fileName=?2 where g.enrollBatchId=?1 ")
	public void deleteFileById(String enrollBatchId, String empty);
}
