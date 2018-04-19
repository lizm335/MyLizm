package com.gzedu.xlims.service.dictionary;

import com.gzedu.xlims.pojo.GjtDistrict;

public interface GjtDistrictService {

	public GjtDistrict queryById(String id);

	public GjtDistrict queryByName(String pid, String name);

	/**
	 * 根据地区id查询地区全名
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月16日 下午2:40:54
	 * @param id
	 * @return
	 */
	String queryAllNameById(String id);

}
