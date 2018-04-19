/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;

import com.gzedu.xlims.pojo.message.GjtMessageClassify;

/**
 * 功能说明：类型分类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月19日
 * @version 3.0
 *
 */
public interface GjtMessageClassifyService {

	public List<GjtMessageClassify> findList(String infoType);

	public GjtMessageClassify queryById(String id);

	public GjtMessageClassify save(GjtMessageClassify item);

	public GjtMessageClassify update(GjtMessageClassify item);

	public void delete(String id);
}
