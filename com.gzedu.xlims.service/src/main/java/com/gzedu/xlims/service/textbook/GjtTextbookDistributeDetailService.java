package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;

public interface GjtTextbookDistributeDetailService {
	
	public void update(GjtTextbookDistributeDetail entity);
	
	public void update(List<GjtTextbookDistributeDetail> entities);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月23日 下午7:17:40
	 * @param searchParams
	 * @return
	 */
	List<GjtTextbookDistributeDetail> findAll(Map<String, Object> searchParams);

}
