/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.dao.menue.TblPriMenueDao;
import com.gzedu.xlims.dao.menue.TblPriMenueSpecs;
import com.gzedu.xlims.pojo.TblPriMenue;
import com.gzedu.xlims.service.menue.TblPriMenueService;

/**
 * 功能说明：
 * 
 * @author liming
 * @Date 2016年4月27日
 * @version 1.0
 *
 */
@Service
public class TblPriMenueServiceImpl implements TblPriMenueService {

	@Autowired
	TblPriMenueDao menueDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.menue.TblPriMenueService#queryAll(com.gzedu.xlims
	 * .pojo.TblPriMenue, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<TblPriMenue> queryAll(TblPriMenue menue, PageRequest pageRequest) {

		return menueDao.findAll(TblPriMenueSpecs.findAllByMenue(menue), pageRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.menue.TblPriMenueService#insertMenue(com.gzedu.
	 * xlims.pojo.TblPriMenue)
	 */
	@Override
	public TblPriMenue insertMenue(TblPriMenue menue) throws RuntimeException {
		if (menue == null) {
			throw new ServiceException("menue == null");
		}
		if (menue.getMenuLevel() == null) {
			throw new ServiceException("MenuLevel == null");
		}
		if (StringUtils.isBlank(menue.getMenuCode())) {
			throw new ServiceException("MenuCode isBlank");
		}
		if (StringUtils.isBlank(menue.getMenuName())) {
			throw new ServiceException("menuName isBlank");
		}
		if (menue.getMenuOrd() == null) {
			throw new ServiceException("MenuOrd == null");
		}
		if (StringUtils.isBlank(menue.getCreatedBy())) {
			throw new ServiceException("CreatedBy isBlank");
		}

		// 设置默认值
		menue.setCreatedDt(new Date());
		menue.setVersion(new BigDecimal(AppConfig.getProperty("version")));
		menue.setIsDeleted("N");
		menue.setMenuId(UUIDUtils.random());

		// String pId = "a9dc67e019fd48e6be7b2d0b32665c4d";
		// TblPriMenue parentMenue = menueDao.findOne(pId);
		// menue.setParentMenue(parentMenue);

		return menueDao.save(menue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.menue.TblPriMenueService#queryMenueByIsLeaf()
	 */
	@Override
	public List<TblPriMenue> queryMenueByIsLeaf() {
		// TODO Auto-generated method stub
		return null;
	}

}
