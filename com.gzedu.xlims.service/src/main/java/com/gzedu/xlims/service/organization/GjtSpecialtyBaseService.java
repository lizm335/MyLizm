/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtSpecialtyBase;

/**
 * 
 * 功能说明：专业管理 实现接口
 * 
 * @author liangyijian
 * @Date 2017年8月24日
 *
 */
public interface GjtSpecialtyBaseService {

	public void save(GjtSpecialtyBase entity);
	
	public GjtSpecialtyBase queryById(String id);
	
	/**
	 * 假删除
	 */
	public Boolean deleteById(String... ids);

	/**
	 * 专业管理-列表查询
	 * @param xxId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtSpecialtyBase> queryAll(String xxId,
			Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 根据院校ID及专业ID，查询专业基本信息ID
	 * @param xxId
	 * @param specialtyId
	 * @return
	 */
	public GjtSpecialtyBase findByXxIdAndSpecialtyId(String xxId, String specialtyId);
	
	/**
	 * 查询专业基本信息
	 * @param xxId
	 * @param specialtyCode
	 * @return
	 */
	public GjtSpecialtyBase findByCodeAndLayer(String xxId,String specialtyCode,int specialtyLayer);
	/**
	 * 启用/停用
	 * @param id
	 * @param status
	 */
	public void updateStatus(String id, int status);

	


}
