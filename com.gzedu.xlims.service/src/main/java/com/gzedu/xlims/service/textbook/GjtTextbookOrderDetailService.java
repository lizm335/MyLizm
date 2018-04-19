package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;

public interface GjtTextbookOrderDetailService {
	
	public Page<Map<String, Object>> findAllSummary(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public Page<GjtTextbookOrderDetail> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public List<GjtTextbookOrderDetail> findAll(List<String> ids);
	
	public GjtTextbookOrderDetail insert(GjtTextbookOrderDetail entity);
	
	public void insert(List<GjtTextbookOrderDetail> entities);

	public void update(GjtTextbookOrderDetail entity);

	public void update(List<GjtTextbookOrderDetail> entities);
	
	public GjtTextbookOrderDetail findOne(String id);
	
	public void delete(GjtTextbookOrderDetail entity);
	
	public void delete(List<GjtTextbookOrderDetail> entities);
	
	public List<GjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId, int status);
	
	public GjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId, String courseId, String planId);
	
	/**
	 * 查询当前待发订单
	 * @param planId
	 * @param textbookType
	 * @return
	 */
	public List<Map<String, String>> queryCurrentDistributeList(String planId, int textbookType);
	
	/**
	 * 查询当前待配送订单
	 * @param orgId
	 * @return
	 */
	public List<Map<String, String>> queryCurrentDistributeList2(String orgId);

}
