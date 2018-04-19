package com.gzedu.xlims.serviceImpl.exam;

import java.util.HashMap;
import java.util.Map;


import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.exam.GjtExamRoomNewDao;
import com.gzedu.xlims.service.exam.GjtRoomExamService;

@Service
public class GjtRoomExamServiceImpl implements GjtRoomExamService {

	@Autowired
	GjtExamRoomNewDao gjtExamRoomNewDao;
	
	/**
	 * 删除考场
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map truncExamRoom(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			int i = gjtExamRoomNewDao.truncExamRoom(searchParams);
			resultMap.put("result", ObjectUtils.toString(i));
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 启用/停用考场
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map examRoomStatus(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			int i = gjtExamRoomNewDao.examRoomStatus(searchParams);
			resultMap.put("result", ObjectUtils.toString(i));
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

}
