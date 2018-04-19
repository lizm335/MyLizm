package com.gzedu.xlims.serviceImpl;

import com.gzedu.xlims.dao.area.GjtAreaDao;
import com.gzedu.xlims.pojo.GjtArea;
import com.gzedu.xlims.service.GjtAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * 功能说明：省市区
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月23日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service
public class GjtAreaServiceImpl implements GjtAreaService {
	@Autowired
	private GjtAreaDao gjtAreaDao;

	// 省 列表
	@Override
	public List<GjtArea> queryProvince() {
		return gjtAreaDao.queryProvince();
	}

	// 县级列表 by 省CODE
	@Override
	public List<GjtArea> queryCity(String provinceCode) {
		return gjtAreaDao.queryCity(provinceCode);
	}

	// 县级列表 by 省CODE
	@Override
	public List<GjtArea> queryDistrict(String cityCode) {
		return gjtAreaDao.queryDistrict(cityCode);
	}

	@Override
	public GjtArea queryProvinceByCode(String code) {
		return gjtAreaDao.queryProvinceByCode(code);
	}

	@Override
	public GjtArea queryCityByCode(String code) {
		return gjtAreaDao.queryCityByCode(code);
	}

	@Override
	public GjtArea queryDistrictByCode(String code) {
		return gjtAreaDao.queryDistrictByCode(code);
	}

	/**
	 * 根据code获得他的全地区名称
	 *
	 * @param code
	 * @return
	 */
	@Override
	public String queryAreaName(String code) {

		return gjtAreaDao.queryAreaName(code);
	}
}
