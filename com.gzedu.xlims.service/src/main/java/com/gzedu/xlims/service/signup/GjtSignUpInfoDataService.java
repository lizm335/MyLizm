package com.gzedu.xlims.service.signup;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface GjtSignUpInfoDataService {
	
	
	/**
	 * 招生管理->报读信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page getSignUpList(Map searchParams,PageRequest pageRequst);
	
	/**
	 * 报读信息详情
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map querySignUpDetail(Map<String, Object> searchParams);


	/**
	 * 导出报读信息统计表
	 * @param xxId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
//	HSSFWorkbook exportSignUpList(String xxId,Map<String, Object> searchParams,Sort sort);
	String exportSignUpData(Map<String, Object> searchParams);
	
	/**
	 * 用户属性统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map userAttributeCount(Map<String, Object> searchParams);
	
	/**
	 * 学历层次统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map userPyccCount(Map<String, Object> searchParams);
	
	/**
	 * 报读专业统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map userCountBySpecial(Map<String, Object> searchParams);
	
	/**
	 * 报读资料统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map queryEnrolmentCount(Map<String, Object> searchParams);
	
	/**
	 * 报读缴费统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map queryPaymentCount(Map<String, Object> searchParams);

	/**
	 * 学习中心统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map queryStudyCenter(Map<String, Object> searchParams);

	/**
	 * 区域统计
	 * @param searchParams
	 * @return
	 */
	Map queryAreaCount(Map<String,Object> searchParams);
	
	/**
	 * 统计学生报读信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map countSignupInfo(Map<String, Object> searchParams);

	/**
	 * 学籍统计
	 * @param searchParams
	 * @return
     */
	Map countStudentRollSituationBy(Map<String, Object> searchParams);

	/**
	 * 根据身份证来修改收费状态(0：已全额缴费，1：已部分缴费，2：待缴费，3：已欠费)
	 * @param searchParams
	 * @return
	 */
	Map chargeSignupNew(Map<String,Object> searchParams);

	Map getSignUpByStudentId(String studentId);

}
