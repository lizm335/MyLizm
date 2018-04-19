package com.gzedu.xlims.service.graduation;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GjtGraduationAdviserService {
	
	public void deleteBySettingId(String settingId);
	
	/**
	 * 查询指导老师信息
	 * @param adviserType
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryGraduationAdviser(int adviserType, Map<String, Object> searchParams, PageRequest pageRequst);

}
