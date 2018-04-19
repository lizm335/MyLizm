package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbook;

public interface GjtTextbookService {

	public Page<GjtTextbook> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public List<GjtTextbook> findAll(Map<String, Object> searchParams);

	public GjtTextbook findOne(Map<String, Object> searchParams);

	public GjtTextbook findOne(String id);

	public GjtTextbook findByCode(String textbookCode, String orgId);

	public GjtTextbook insert(GjtTextbook entity);

	public void update(GjtTextbook entity);

	public List<GjtTextbook> findAll(Iterable<String> ids);
	
	public List<GjtTextbook> findCurrentArrangeTextbook(String planId, String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月19日 下午4:29:04
	 * @param gradeId
	 * @return
	 */
	List<GjtTextbook> findUnsetTextBookByGradeId(String gradeId);

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月20日 下午7:19:23
	 * @param searchParams
	 * @return
	 */
	Page<GjtTextbook> findArrangeTextBook(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月29日 下午4:11:36
	 * @param gradeSpecialtyId
	 */
	public List findByGradeSpecialtyId(String gradeSpecialtyId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月30日 下午8:05:37
	 * @param gradeId
	 * @param gradeSpecialtyId
	 * @return
	 */
	List findByGradeId(String gradeId, String gradeSpecialtyId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月1日 下午5:04:02
	 * @param distributeId
	 * @param studentId
	 * @return
	 */
	List<Map<String, Object>> findByDistributeId(String distributeId, String studentId);

}
