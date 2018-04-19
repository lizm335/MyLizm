package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.service.base.BaseService;

public interface GjtExamAppointmentNewService extends BaseService<GjtExamAppointmentNew> {

	Page<GjtExamAppointmentNew> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询考点
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<GjtExamPointAppointmentNew> queryExamPointAppointmentBy(Map<String, Object> searchParams, Sort sort);

	/**
	 * 查询考点
	 * @param xxId
	 * @param searchParams
	 * @param sort
     * @return
     */
	List<GjtExamPointAppointmentNew> queryExamPointAppointmentBy(String xxId, Map<String, Object> searchParams, Sort sort);

	long isNotNullRecordCount(String xxId, Map<String, Object> searchParams);

	long isNullRecordCount(String xxId, Map<String, Object> searchParams);

	List<GjtExamAppointmentNew> queryExamAppointments(String examBatchCode, int examType,
			List<String> studentIds);

	/** 过渡旧版接口 */
	Map<String, Object> appointExamPlan(String studentid, String teachPlanid, int op);

	/** 新版本(2.5? or 3.0? 反正我分不清楚)接口, 需等个人中心改成新版后才能启用 */
	Map<String, Object> appointExamPlanNew(String studentid, String subjectCode, int op);

	/** 过渡旧版接口 */
	Map<String, Object> appointExamPoint(String studentid, String pointName, String xxid, int op);

	/** 新版本(2.5? or 3.0? 反正我分不清楚)接口, 需等个人中心改成新版后才能启用 */
	Map<String, Object> appointExamPointNew(String studentid, String pointid, String xxid, int op,
			String studyyearid);

	Map<String, Map<String, String>> studentViewList(List<String> ids);

	Map<String, String> appointPointMap(List<String> ids);

	List<Map> appointPointMapBySearch(String examBatchCode, String orgId);

	HSSFWorkbook exportAppointPoint(List<Map> list);
	
	HSSFWorkbook exportExamPointAppointment(List<GjtExamPointAppointmentNew> list);

	@Deprecated
	HSSFWorkbook exportAppointPlan(List<Map> list);

	/**
	 * 导出考试预约数据
	 * @param orgId
	 * @param searchParams
	 * @param path
	 * @return
	 */
	String exportAppointPlan(String orgId, Map searchParams, String path);

	HSSFWorkbook exportExamAppointment(List<GjtExamAppointmentNew> list);

	GjtStudyYearInfo getCurrentStudyYear(String xxid);

	List<Map<String, Object>> appointmentListBySearch(Map<String, String> params);

	/**
	 * @param studentId
	 *            学员ＩＤ
	 * @param teachPlanId
	 *            教学计划ＩＤ（旧）
	 * @return
	 * @throws ServiceException
	 */
	GjtExamAppointmentNew queryGjtExamAppointmentNew(String studentId, String teachPlanId) throws ServiceException;

	/**
	 * @param studentid
	 * @param plan
	 * @param op
	 * @return
	 */
	GjtExamAppointmentNew appointExamPlan(String studentid, GjtExamPlanNew plan, int op);

	/**
	 * 根据考试批次，学员IDS获得预约记录列表
	 * 
	 * @param examBatchCode
	 * @param studentIds
	 * @return
	 */
	Map<String, List<GjtExamAppointmentNew>> queryAllByBatchCodeAndStudents(String examBatchCode,
			Set<String> studentIds);

	/**
	 * @param studyYearId
	 * @param studentIds
	 * @return
	 */
	Map<String, GjtExamPointAppointmentNew> queryAllPointAppointmentByBatchCodeAndStudents(String studyYearId,
			Set<String> studentIds);

	// List<GjtExamAppointmentNew>
	// appointmentsByExamBatchAndExamPoint();TODO @micarol 待完善
	
	List<Map> queryList(Map formMap);

	/**
	 * 查询预约记录，带分页
	 * @param xxId
	 * @param searchParams
	 * @param pageRequest
     * @return
     */
	Page<GjtExamAppointmentNew> queryPage(String xxId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 查询预约记录
	 * @param xxId
	 * @param searchParams
	 * @param sort
     * @return
     */
	List<GjtExamAppointmentNew> queryBy(String xxId, Map<String, Object> searchParams, Sort sort);
	
	GjtExamAppointmentNew findByStudentIdAndExamPlanId(String studentId, String examPlanId);
	
	GjtExamAppointmentNew update(GjtExamAppointmentNew entity);
	
	GjtExamAppointmentNew queryOne(String id);
	
	List<GjtExamAppointmentNew> queryList(Iterable<String> ids);
	
	/**
     * 查询学员预约的考点
     * @return
     */
    List queryStudentPoint(Map formMap);

	/**
	 * 查询预约记录数据
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> queryExamAppointment(Map<String, Object> searchParams);
	
}
