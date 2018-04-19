/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao.dictionary;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.TblSysData;

import java.util.List;

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

public interface TblSysDataDao extends BaseDao<TblSysData, String> {

	List<TblSysData> findByIsDeletedAndTypeName(String isDeleted, String TypeName);

	List<TblSysData> findByIsDeletedAndTypeCode(String isDeleted, String TypeCode);

	List<TblSysData> findByIsDeletedAndTypeCodeOrderByOrdNoAsc(String isDeleted, String TypeCode);

	List<TblSysData> findByIsDeletedAndTypeNameOrderByOrdNoAsc(String isDeleted, String TypeName);
	
	TblSysData findByIsDeletedAndTypeCodeAndCode(String isDeleted, String TypeCode, String code);
	
	List<TblSysData> findByIsDeletedAndTypeCodeAndCodeIn(String isDeleted, String TypeCode, Iterable<String> codes);

}
