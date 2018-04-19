package com.gzedu.xlims.service.exam;

import java.util.Map;

public interface GjtRoomExamService {

	/**
	 * 删除考场
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map truncExamRoom(Map<String, Object> searchParams);
	
	/**
	 * 启用/停用考场
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map examRoomStatus(Map<String, Object> searchParams);
	
}
