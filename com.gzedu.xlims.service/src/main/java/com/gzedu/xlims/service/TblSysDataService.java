/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service;

import java.util.List;

import com.gzedu.xlims.pojo.TblSysData;

/**
 * 
 * 功能说明：系统配置
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface TblSysDataService {
	public List<TblSysData> findByIsDeletedAndTypeName(String isDeleted, String typeName);

	public List<TblSysData> findByIsDeletedAndTypeCode(String isDeleted, String typeCode);

	public TblSysData queryById(String id);

	public List<TblSysData> findAll(String typeCode, String ordNo);
}
