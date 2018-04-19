package com.gzedu.xlims.serviceImpl.exam;

import java.util.HashMap;
import java.util.Map;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointNewRepository;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.exam.GjtPointExamNewDao;
import com.gzedu.xlims.service.exam.GjtPointExamNewService;

@Service
public class GjtPointExamNewServiceImpl extends BaseServiceImpl<GjtExamPointNew> implements GjtPointExamNewService {
	
	@Autowired
	GjtPointExamNewDao gjtPointExamNewDao;

	@Autowired
	GjtExamPointNewRepository gjtExamPointNewRepository;

	@Override
	protected BaseDao<GjtExamPointNew, String> getBaseDao() {
		return this.gjtExamPointNewRepository;
	}

	/**
	 * 考点管理
	 */
	@SuppressWarnings("rawtypes")
	public Page<Map<String, Object>> getExamPointList(Map<String, Object> searchParams, PageRequest pageRequest) {
		return gjtPointExamNewDao.getExamPointList(searchParams, pageRequest);
	}

	/**
	 * 启用/停用考点
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map examPointStatus(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			int i = gjtPointExamNewDao.examPointStatus(searchParams);
			resultMap.put("result", ObjectUtils.toString(i));
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 删除考点
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map delExamPoint(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			int i = gjtPointExamNewDao.delExamPoint(searchParams);
			resultMap.put("result", ObjectUtils.toString(i));
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}
}
