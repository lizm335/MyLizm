/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.dictionary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtDistrict;

/**
 * 全国区域信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月8日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtDistrictDao extends BaseDao<GjtDistrict, String> {

	/**
	 * 获取所有区域信息
	 *
	 * @return
	 */
	@Query(value = "SELECT t.ID CODE,t.NAME FROM GJT_DISTRICT t ORDER BY t.ID", nativeQuery = true)
	List<Object[]> findDistrictAll();

	public GjtDistrict findByNameLike(String name);

	public GjtDistrict findByPidAndNameLike(String pid, String name);
}
