package com.gzedu.xlims.serviceImpl.dictionary;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.dictionary.GjtDistrictDao;
import com.gzedu.xlims.pojo.GjtDistrict;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;

@Service
public class GjtDistrictServiceImpl implements GjtDistrictService {

	@Autowired
	private GjtDistrictDao gjtDistrictDao;

	@Autowired
	private CacheService cacheService;

	@Override
	public GjtDistrict queryById(String id) {
		return gjtDistrictDao.findOne(id);
	}

	@Override
	public GjtDistrict queryByName(String pid, String name) {
		GjtDistrict gjtDistrict = null;
		if (StringUtils.isBlank(pid)) {// 省
			gjtDistrict = gjtDistrictDao.findByNameLike(name + "%");
		} else {
			gjtDistrict = gjtDistrictDao.findByPidAndNameLike(pid, name);
		}
		return gjtDistrict;
	}

	@Override
	public String queryAllNameById(String id) {
		if (StringUtils.isBlank(id) || id.length() != 6) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String address = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE,
				id.substring(0, 2) + "0000");
		sb.append(StringUtils.defaultString(address));
		String key = CacheService.DictionaryKey.CITY.replace("${Province}", id.substring(0, 2) + "0000");
		address = cacheService.getCachedDictionaryName(key, id.substring(0, 4) + "00");
		sb.append(StringUtils.defaultString(address));
		key = CacheService.DictionaryKey.AREA.replace("${Province}", id.substring(0, 2) + "0000");
		key = key.replace("${City}", id.substring(0, 4) + "00");
		address = cacheService.getCachedDictionaryName(key, id);
		sb.append(StringUtils.defaultString(address));
		return sb.toString();
	}

}
