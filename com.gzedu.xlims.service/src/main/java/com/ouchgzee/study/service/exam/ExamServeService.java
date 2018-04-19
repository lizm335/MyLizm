package com.ouchgzee.study.service.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.common.exception.ServiceException;

public interface ExamServeService {
	
	@SuppressWarnings("rawtypes")
	/**
	 * 考试须知
	 * @param searchParams
	 * @return
	 */
	Map examAttention(Map<String, Object> searchParams);
	
	@SuppressWarnings("rawtypes")
	/**
	 * 查询准考证信息
	 * @param searchParams
	 * @return
	 */
	Map examAdmissionInfo(Map<String, Object> searchParams);

	/**
	 * 获取学员的考点
	 * @param searchParams
	 * @return
	 */
	List<Map> getAppointExamPointByStudent(Map<String, Object> searchParams);

	/**
	 * 查询考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map queryPointInfo(Map<String, Object> searchParams);
	
	
	@SuppressWarnings("rawtypes")
	List queryExamPoint(Map<String, Object> searchParams);
	
	
	/**
	 * 查询待预约考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map queryAppointmentExam(Map<String, Object> searchParams);

	/**
	 * 查询待预约考试(院校模式)
	 * @param searchParams
	 * @return
	 */
	Map queryAppointmentExamByAcadeMy(Map<String,Object> searchParams);
	
	/**
	 * 我的考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map myExamDataList(Map<String, Object> searchParams);
	
	/**
	 * 同步考试成绩
	 * @param searchParams
	 * @return
	 */
	int updateExamScore(Map<String, Object> searchParams);

	/**
	 * 我的考试(院校模式)
	 * @param searchParams
	 * @return
	 */
	Map myExamDataListByAcadeMy(Map<String,Object> searchParams);
	
	/**
	 * 查询考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getExamBatchData(Map<String, Object> searchParams);

	/**
	 * 统计考试数据
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map countExamDataInfo(Map<String, Object> searchParams);
	
	
	/**
	 * 查询考点信息列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page queryPointList(Map<String, Object> searchParams,PageRequest pageRequst);
	
	/**
	 * 保存考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	boolean saveExamPointApp(Map<String, Object> searchParams);
	
	/**
	 * 查询个人证件照
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getPersonalZP(Map<String, Object> searchParams);
	
	/**
	 * 更新个人证件照
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map updatePersonalZP(Map<String, Object> searchParams);
	
	/**
	 * 预约考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map makeAppointment(Map<String, Object> searchParams);
	
	/**
	 * 取消预约
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map cancelAppointment(Map<String, Object> searchParams);
	
	/**
	 * 预约待缴费
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map appointPay(Map<String, Object> params);
	
	/**
	 * 下载准考证信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getAdmissionData(Map<String, Object> searchParams);
	
	/**
	 * 获取考试平台科目信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map entryExam(Map<String, Object> searchParams);
	
	/**
	 * 查询报考ID
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getSignUpId(Map<String, Object> searchParams);

	Map queryGjtExamAppointmentNewData(Map searchParams) throws ServiceException;

	/**
	 * 查询往期考试记录
	 * @param searchParams
	 * @return
	 */
	Map getPastLearnRecord(Map searchParams);

	/**
	 * 考试预约数据统计
	 * @param examBatchCode
	 * @param params
	 * @param path
     * @return
     */
	String exportExamAppointmentStudentSituation(final String examBatchCode, Map<String, Object> params, String path);
	
	/**
	 * 新增学员下载准考证
	 * @param searchParams
	 * @return
	 */
	int saveStudentDownToken(Map searchParams);
}
