package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.pojo.exam.GjtExamStudentRoomNew;

public interface GjtExamStudentRoomNewService {

	public Page<GjtExamStudentRoomNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public Map<String, List<GjtExamStudentRoomNew>> arrangement(String examBatchCode, String examPointId, int examType) throws Exception;

	/**
	 * @param xxId
	 * @param examBatchCode
	 * @return
	 */
	public boolean isArrangeOver(String xxId, String examBatchCode, String examPointId, int examType);

	/**
	 * @param examPointNew
	 * @param examPlanNewId
	 * @param examType
	 * @return
	 */
	List<GjtExamStudentRoomNew> createExamPlanRoom(GjtExamPointNew examPointNew, String examPlanNewId, int examType);

	/**
	 * @param examPointId
	 * @param examPlanId
	 * @return
	 */
	List<GjtExamStudentRoomNew> queryExamStudentRoomNews(String examPointId, String examPlanId);

	/**
	 * @param examBatchCode
	 * @param examPointId
	 * @param examType
	 * @return
	 */
	Workbook exportExamStudentSeat(String examBatchCode, String examPointId, int examType);

	/**
	 * @param examBatchCode
	 * @param examPointId
	 * @param examType
	 * @return
	 */
	Workbook exportExamStudentSeat2(String examBatchCode, String examPointId, int examType);

	/**
	 * @param examRoomId
	 * @param examPlanId
	 * @return
	 */
	List<GjtExamStudentRoomNew> queryExamStudentRoomNewsByExamRoomIdAndExamPlanId(String examRoomId, String examPlanId);

	/**
	 * @param gjtExamAppointmentNew
	 * @param gjtExamPointNew
	 * @param examRoomNew
	 * @param seatNo
	 * @return
	 */
	GjtExamStudentRoomNew initStudentTORoom(GjtExamAppointmentNew gjtExamAppointmentNew,
			GjtExamPointNew gjtExamPointNew, GjtExamRoomNew examRoomNew, int seatNo);

	/**
	 * @param asList
	 */
	public void delete(List<String> asList);
	
	public void delete(GjtExamStudentRoomNew entity);
	
	public GjtExamStudentRoomNew findByExamPlanIdAndExamRoomIdAndSeatNo(String examPlanId, String examRoomId, int seatNo);
	
	public GjtExamStudentRoomNew findByStudentIdAndExamPlanId(String studentId, String examPlanId);

	public GjtExamStudentRoomNew insert(GjtExamStudentRoomNew entity);
	
	public GjtExamStudentRoomNew update(GjtExamStudentRoomNew entity);
	
	/**
	 * 查询准考证信息（临时）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findAdmissionTickets(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 查询排考信息
	 */
	public Page getExamStudentRoomList(Map formMap, PageRequest pageRequst);
}
