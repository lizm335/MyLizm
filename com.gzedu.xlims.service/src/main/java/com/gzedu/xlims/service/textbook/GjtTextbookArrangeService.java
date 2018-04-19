package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookArrange;

public interface GjtTextbookArrangeService {
	
	public Page<GjtTextbookArrange> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtTextbookArrange insert(GjtTextbookArrange entity);
	
	public void update(GjtTextbookArrange entity);
	
	public void insert(List<GjtTextbookArrange> entities);
	
	public GjtTextbookArrange findOne(String id);
	
	public void delete(GjtTextbookArrange entity);
	
	public GjtTextbookArrange findByPlanIdAndTextbookId(String planId, String textbookId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月19日 上午11:35:28
	 * @param orgId
	 * @param gradeId
	 * @return
	 */
	Page<Map<String, Object>> findTextbookArrangeList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 学期添加发放教材
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月20日 下午5:55:06
	 * @param gradeId
	 * @param textbookIds
	 */
	public void saveArrangeTextbooks(String gradeId, List<String> textbookIds);

	/**
	 * 删除年级关联的教材
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月22日 下午3:11:30
	 * @param gradeId
	 * @param textbookId
	 */
	public void removeTextbook(String gradeId, String textbookId);

}
