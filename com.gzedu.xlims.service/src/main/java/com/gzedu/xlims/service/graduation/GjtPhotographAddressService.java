/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.graduation;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtPhotographAddress;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
public interface GjtPhotographAddressService {

	public Page<GjtPhotographAddress> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public GjtPhotographAddress save(GjtPhotographAddress entity);

	public GjtPhotographAddress update(GjtPhotographAddress entity);

	public GjtPhotographAddress queryById(String id);

	public int delete(String id);

	public Boolean updateEnabled(String id, String enabled);

}
