/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao.area;

import com.gzedu.xlims.dao.area.repository.GjtAreaRepository;
import com.gzedu.xlims.pojo.GjtArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年10月8日
 * @version 2.5
 *
 */
@Component
public class GjtAreaDao {
	@Autowired
	GjtAreaRepository gjtAreaRepository;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	// 省 列表
	public List<GjtArea> queryProvince() {
		return gjtAreaRepository.queryProvince();
	}

	// 县级列表 by 省CODE
	public List<GjtArea> queryCity(String provinceCode) {
		String code = provinceCode.substring(0, 2);
		String prefix = code + "%00";
		String province = code + "0000";
		return gjtAreaRepository.queryCity(prefix, province);
	}

	// 县级列表 by 省CODE
	public List<GjtArea> queryDistrict(String cityCode) {
		String code = cityCode.substring(0, 4);
		String prefix = code + "%";
		String city = code + "00";
		return gjtAreaRepository.queryDistrict(prefix, city);
	}

	public GjtArea queryProvinceByCode(String code) {
		String district = code.substring(0, 2) + "0000";
		return gjtAreaRepository.findByDistrict(district);
	}

	public GjtArea queryCityByCode(String code) {
		String district = code.substring(0, 4) + "00";
		return gjtAreaRepository.findByDistrict(district);
	}

	public GjtArea queryDistrictByCode(String code) {
		String district = code.substring(0, 6);
		return gjtAreaRepository.findByDistrict(district);
	}

	public String queryAreaName(String code){

		return gjtAreaRepository.queryAreaNam(code);
	}
}
