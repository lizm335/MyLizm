/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtTermInfo;

/**
 * 
 * 功能说明：学期管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtTermInfoService {

	public Boolean saveEntity(GjtTermInfo entity);

	public Boolean updateEntity(GjtTermInfo entity);

	public GjtTermInfo queryById(String id);

	public List<GjtTermInfo> queryAll();

	/**
	 * 假
	 * 
	 * @param ids
	 * @return
	 */
	public Boolean deleteById(String[] ids);

	/**
	 * 真
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtTermInfo> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

}
