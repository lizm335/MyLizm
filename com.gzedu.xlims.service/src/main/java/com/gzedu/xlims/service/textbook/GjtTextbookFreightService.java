package com.gzedu.xlims.service.textbook;

import com.gzedu.xlims.pojo.textbook.GjtTextbookFreight;

public interface GjtTextbookFreightService extends com.gzedu.xlims.service.base.BaseService<GjtTextbookFreight> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月11日 上午10:42:49
	 * @param districtId
	 * @return
	 */
	GjtTextbookFreight queryByDistrictId(String districtId);
	
	

}
