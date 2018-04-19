package com.gzedu.xlims.service.recruitmanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtEnrollBatchNew;

/**
 * 
 * 功能说明：
 * 
 * @author lulinlin@eenet.com
 * @Date 2016年11月20日
 *
 */
public interface GjtEnrollBatchNewService{
	/**
	 * 查询招生计划
	 * @param schoolId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtEnrollBatchNew> queryAll(String schoolId,List enrollBatchNewList, String date,Map<String, Object> searchParams, PageRequest pageRequst);
	/**
	 * 查询招生计划(招生完成度有值时，调用该方法)
	 * @param schoolId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	/*public Page<GjtEnrollBatchNew> queryAll(String id, Map<String, Object> searchParams, PageRequest pageRequst,
			String yearDegree, String symbol);*/
	/**
	 * 假删除（单个）
	 * @param ids
	 */
	public void delete(String id);
	/**
	 * 更新招生计划状态
	 * @param ids
	 */
	public void updateStatus(String ids);
	
	public void insert(GjtEnrollBatchNew entity);
	
	public int update(GjtEnrollBatchNew entity);
	/**
	 * 假删除（多个）
	 * @param ids
	 */
	public void delete(Iterable<String> ids);
	/**
	 * 根据ID查询招生计划
	 * @param id
	 * @return
	 */
	public GjtEnrollBatchNew queryById(String id);	
	/**
	 * 查询各状态的总数量
	 * @param xxId
	 * @param status
	 * @return
	 */
	public long queryStatusTotalNum(String orgId,String status);
	/**
	 * 查询时间范围内各状态的总数量
	 * @param xxId
	 * @param status
	 * @return
	 */
	public long queryDateTotalNum(String orgId,String date);
	/**
	 * 查询招生完成度的数量
	 * @param id
	 * @param yearDegree
	 * @param symbol
	 * @return
	 */
	public List queryEnrollBatchNew(String id, String yearDegree, String symbol);
	/**
	 * 删除附件
	 * @param enrollBatchId
	 */
	public void deleteFile(String enrollBatchId);
		
}	
