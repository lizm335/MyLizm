package com.ouchgzee.headTeacher.service.exam;

import java.util.List;
import java.util.Map;

import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Deprecated public interface BzrGjtExamAppointmentService {

	/**
	 * 查询当前学期的考试预约情况
	 * @param classId
	 * @param batchCode
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findCurrentAppointmentList(String classId, String batchCode, Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 查询历史学期的考试预约情况
	 * @param classId
	 * @param batchCode
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findHistoryAppointmentList(String classId, String batchCode, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询学员当前学期的考试预约情况
	 * @param studentId
	 * @param batchCode
	 * @return
	 */
	public List<Map<String, Object>> findCurrentStudentAppointment(String studentId, String batchCode);

	/**
	 * 查询学员历史学期的考试预约情况
	 * @param studentId
	 * @param batchCode
	 * @return
	 */
	public List<Map<String, Object>> findHistoryStudentAppointment(String studentId, String batchCode);
	
	/**
	 * 查询学员的预约考点
	 * @param studentId
	 * @param batchCode
	 * @return
	 */
	public List<Map<String, Object>> findStudentPointAppointment(String studentId, String batchCode);


	/**
	 * 导出考试预约数据
	 * @param list 数据源
	 * @return 表格文件
	 */
	public HSSFWorkbook exportAppointPlan(List<Map<String, Object>> list,BzrGjtExamBatchNew examBatch );
}
