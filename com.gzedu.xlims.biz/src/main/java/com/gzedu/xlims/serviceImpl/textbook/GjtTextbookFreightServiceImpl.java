package com.gzedu.xlims.serviceImpl.textbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookFreightDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFreight;
import com.gzedu.xlims.service.textbook.GjtTextbookFreightService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtTextbookFreightServiceImpl extends BaseServiceImpl<GjtTextbookFreight> implements GjtTextbookFreightService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookFreightServiceImpl.class);

	@Autowired
	private GjtTextbookFreightDao gjtTextbookFreightDao;

	@Override
	protected BaseDao<GjtTextbookFreight, String> getBaseDao() {
		return gjtTextbookFreightDao;
	}

	@Override
	public GjtTextbookFreight queryByDistrictId(String districtId) {

		return gjtTextbookFreightDao.queryByDistrictId(districtId);
	}


}
