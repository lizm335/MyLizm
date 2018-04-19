package com.gzedu.xlims.dao.textbook;

import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFreight;


public interface GjtTextbookFreightDao extends BaseDao<GjtTextbookFreight, String> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月11日 上午9:28:32
	 * @param districtId
	 * @return
	 */
	@Query("from GjtTextbookFreight where districtId=?1")
	GjtTextbookFreight queryByDistrictId(String districtId);
	
}
