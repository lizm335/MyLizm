/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao.area.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.GjtArea;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月23日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtAreaRepository extends JpaRepository<GjtArea, String>, JpaSpecificationExecutor<GjtArea> {

	public GjtArea findByDistrict(String district);

	/**
	 * 获得 省 列表
	 * 
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from gjt_area where district like '%0000' order by district")
	public List<GjtArea> queryProvince();

	/**
	 * 根据省CODE 获得 市/县 列表
	 * 
	 * @param prefix
	 *            省2位 前缀，如：?1 = '44%00'
	 * @param province
	 *            去除当前 省 ?2 != '440000'
	 * 
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from gjt_area where district like ?1% and district != ?2 order by district")
	public List<GjtArea> queryCity(String prefix, String province);

	/**
	 * 根据 市/县 CODE 获得 区/镇 列表
	 * 
	 * @param prefix
	 *            省2位 前缀，如：?1 = '4401%%'
	 * @param city
	 *            去除当前 省 ?2 != '440100'
	 * 
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from gjt_area where district like ?1% and district != ?2 order by district")
	public List<GjtArea> queryDistrict(String prefix, String city);

	@Query(nativeQuery = true,value = "SELECT ( SELECT MAX( GAA.CITYNAME ) FROM GJT_AREA GAA WHERE GAA.DISTRICT LIKE '%' || SUBSTR( GA.DISTRICT, 1, 2 )|| '0000%' )|| '-' ||( SELECT MAX( GAA.CITYNAME ) FROM GJT_AREA GAA WHERE GAA.DISTRICT LIKE '%' || SUBSTR( GA.DISTRICT, 1, 4 )|| '00%' )|| '-' || GA.CITYNAME AREA_NAME FROM GJT_AREA ga WHERE ga.IS_DELETED = 'N' AND ga.DISTRICT = ?1")
	String queryAreaNam(String code);
}
