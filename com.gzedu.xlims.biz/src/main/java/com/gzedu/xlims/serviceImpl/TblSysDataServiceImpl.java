/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.serviceImpl;

import com.gzedu.xlims.dao.dictionary.TblSysDataDao;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.service.TblSysDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * 功能说明：系统配置实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service
public class TblSysDataServiceImpl implements TblSysDataService {

	@Autowired
	TblSysDataDao tblSysDataDao;

	@Override
	public List<TblSysData> findByIsDeletedAndTypeName(String isDeleted, String TypeName) {
		return tblSysDataDao.findByIsDeletedAndTypeName(isDeleted, TypeName);
	}

	@Override
	public List<TblSysData> findByIsDeletedAndTypeCode(String isDeleted, String typeCode) {
		return tblSysDataDao.findByIsDeletedAndTypeCode(isDeleted, typeCode);
	}

	@Override
	public TblSysData queryById(String id) {
		return tblSysDataDao.findOne(id);
	}

	@Override
	public List<TblSysData> findAll(String typeCode, String ordNo) {
		return tblSysDataDao.findAll(new Sort(typeCode, ordNo));
	}

}
