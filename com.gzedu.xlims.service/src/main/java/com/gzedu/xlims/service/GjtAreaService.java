package com.gzedu.xlims.service;

import com.gzedu.xlims.pojo.GjtArea;

import java.util.List;

public interface GjtAreaService {

	// 省 列表
	public List<GjtArea> queryProvince();

	// 县级列表 by 省CODE
	public List<GjtArea> queryCity(String provinceCode);

	// 县级列表 by 省CODE
	public List<GjtArea> queryDistrict(String cityCode);

	// 根据CODE获得它的省份
	public GjtArea queryProvinceByCode(String code);

	// 根据CODE获得它的县/市
	public GjtArea queryCityByCode(String code);

	// 根据CODE获得它的 区/镇
	public GjtArea queryDistrictByCode(String code);

	/**
	 * 根据code获得他的全地区名称
	 * @param code
	 * @return
	 */
	String queryAreaName(String code);
}
