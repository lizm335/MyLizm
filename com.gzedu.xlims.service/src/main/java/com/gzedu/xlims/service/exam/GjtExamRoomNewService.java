package com.gzedu.xlims.service.exam;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;

public interface GjtExamRoomNewService {

	public GjtExamRoomNew insert(GjtExamRoomNew entity);

	public Page<GjtExamRoomNew> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public int delete(List<String> ids, String xxid);

	public GjtExamRoomNew queryBy(String id);

	public GjtExamRoomNew update(GjtExamRoomNew entity);

	public GjtExamRoomNew closeRoom(GjtExamRoomNew entity);

	public Map<String, List<GjtExamRoomNew>> examPointIdRoomMap(String xxid);

	public List<GjtExamRoomNew> queryGjtExamRoomNewByExamPoint(String examPointId);
	
	public String exportDownLoad(Map searchParams,HttpServletRequest request,HttpServletResponse response);
	
	public Map importExamRoomXls(File filename,Map<String, String> searchParams);
	
	/**
	 * 考场管理列表
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page getExamRoomList(Map<String, Object> searchParams,PageRequest pageRequest);
	
	/**
	 * 查询考试批次
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getExamBatchInfo(Map<String, Object> searchParams);
	
	/**
	 * 查询考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getExamPointList(Map<String, Object> searchParams);
	
	HSSFWorkbook exportRoom(List list);
	
	/**
	 * 查询考场信息
	 */
	public Map getRoomInfo(Map searchParams);
	
	/**
	 * 新增考场
	 */
	public int saveRoomData(Map searchParams);
	
	/**
	 * 修改考场
	 */
	public int updRoomData(Map searchParams);
}
