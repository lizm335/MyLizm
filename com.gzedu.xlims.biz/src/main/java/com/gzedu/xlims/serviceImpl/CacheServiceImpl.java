/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.dao.dictionary.TblSysDataDao;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.service.CacheService;

/**
 * 缓存服务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月21日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	private TblSysDataDao tblSysDataDao;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOps;

	@Override
	public Object getCached(String key) {
		return null;
	}

	@Override
	public List<Value> getCachedDictionary(String key) {
		List<Value> vlist = (List<Value>) hashOps.get(HKEY_DICTIONARY, key);
		if (vlist == null) {
			List<TblSysData> dataList = tblSysDataDao.findByIsDeletedAndTypeCodeOrderByOrdNoAsc(Constants.BOOLEAN_NO,
					key);
			vlist = new ArrayList();
			for (TblSysData data : dataList) {
				CacheService.Value v = new CacheService.Value(data.getCode(), data.getName());
				vlist.add(v);
			}
			// 添加到缓存中
			this.setCachedDictionary(key, vlist);
		}
		return vlist;
	}

	@Override
	public Map<String, String> getMapCachedDictionary(String key) {
		List<CacheService.Value> trainingLevel = this.getCachedDictionary(key);
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Value value : trainingLevel) {
			map.put(value.getCode(), value.getName());
		}
		return map;
	}
	
	@Override
	public Map<String, String> getMapCachedCode(String key) {
		List<CacheService.Value> trainingLevel = this.getCachedDictionary(key);
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Value value : trainingLevel) {
			map.put(value.getName(), value.getCode());
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getListCachedDictionary(String key) {
		List<CacheService.Value> trainingLevel = this.getCachedDictionary(key);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Value value : trainingLevel) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", value.getCode());
			map.put("name", value.getName());
			list.add(map);
		}
		return list;
	}

	@Override
	public void setCachedDictionary(String key, List<Value> vlist) {
		// 添加到缓存中
		hashOps.put(HKEY_DICTIONARY, key, vlist);
		redisTemplate.expire(HKEY_DICTIONARY, 30, TimeUnit.DAYS);
	}

	@Override
	public Map<String, String> getCachedDictionaryMap(String key) {
		Map<String, String> vmap = new LinkedHashMap<String, String>();
		for (Value v : getCachedDictionary(key)) {
			vmap.put(v.getCode(), v.getName());
		}
		return vmap;
	}

	@Override
	public String getCachedDictionaryName(String key, String code) {
		String name = null;
		if (StringUtils.isNotBlank(code)) {
			List<CacheService.Value> valueList = this.getCachedDictionary(key);
			for (CacheService.Value v : valueList) {
				if (v.getCode().equals(code)) {
					name = v.getName();
					break;
				}
			}
		}
		return name;
	}

	@Override
	public boolean hasCachedDictionary(String key) {
		return hashOps.hasKey(HKEY_DICTIONARY, key);
	}

	@Override
	public boolean updateCached(String key, Object value) {
		return false;
	}

	@Override
	public boolean deleteCached(String key) {
		redisTemplate.delete(key);
		return true;
	}
}
