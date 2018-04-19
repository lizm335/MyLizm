/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtChangeSpecialty;

/**
 * 
 * 功能说明：学籍异动
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月6日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtChangeSpecialtyService {

	// 添加专业信息
	public Boolean saveEntity(GjtChangeSpecialty entity);

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
	public Page<GjtChangeSpecialty> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

}
