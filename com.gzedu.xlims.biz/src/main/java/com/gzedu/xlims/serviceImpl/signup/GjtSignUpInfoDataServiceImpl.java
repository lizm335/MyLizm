package com.gzedu.xlims.serviceImpl.signup;

import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.signup.GjtSignUpInfoDataDao;
import com.gzedu.xlims.service.signup.GjtSignUpInfoDataService;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
@Service
public class GjtSignUpInfoDataServiceImpl implements GjtSignUpInfoDataService {
	
	@Autowired
	GjtSignUpInfoDataDao gjtSignUpInfoDataDao;
	
	@SuppressWarnings("rawtypes")
	public Page getSignUpList(Map searchParams, PageRequest pageRequst) {
		return gjtSignUpInfoDataDao.getSignUpList(searchParams, pageRequst);
	}



	
	@SuppressWarnings("rawtypes")
	public Map querySignUpDetail(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			List list = gjtSignUpInfoDataDao.querySignUpDetail(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				resultMap = (Map) list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@SuppressWarnings("rawtypes")
	public String exportSignUpData(Map<String, Object> searchParams){
		List datas = gjtSignUpInfoDataDao.exportSignUpList(searchParams);
		String [] ts = {"姓名","性别","学号","身份证号","手机号","年级","层次","专业","报名时间","报读来源","资料提交","学籍状态","缴费状态","学习中心"};
		String [] fields = {"XM","XBM","XH","SFZH","SJH","GRADE_NAME","PYCC_NAME","ZYMC","CREATED_DT","AUDIT_SOURCE","AUDIT_STATE","XJZT","CHARGE","SC_NAME"};
		return ExcelService.renderExcel(datas, Arrays.asList(ts), Arrays.asList(fields));
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map userAttributeCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			Map generationMap = new HashMap();
			List generationSum = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generationSum)){
				generationMap = (Map) generationSum.get(0);
				resultMap.put("GENERATIONSUM", generationMap.get("STUDENT_COUNT"));
			}
			searchParams.put("AGE_FLAG", "0"); //00后
			List generation_00s = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generation_00s)){
				Map ageFlag_00s = (Map) generation_00s.get(0);
				resultMap.put("AGE_FLAG_00s", ageFlag_00s.get("STUDENT_COUNT"));
			}
			searchParams.put("AGE_FLAG", "1"); //90后
			List generation_90s = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generation_90s)){
				Map ageFlag_90s = (Map) generation_90s.get(0);
				resultMap.put("AGE_FLAG_90s", ageFlag_90s.get("STUDENT_COUNT"));
			}
			searchParams.put("AGE_FLAG", "2"); //80后
			List generation_80s = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generation_80s)){
				Map ageFlag_80s = (Map) generation_80s.get(0);
				resultMap.put("AGE_FLAG_80s", ageFlag_80s.get("STUDENT_COUNT"));
			}
			searchParams.put("AGE_FLAG", "3"); //70后
			List generation_70s = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generation_70s)){
				Map ageFlag_70s = (Map) generation_70s.get(0);
				resultMap.put("AGE_FLAG_70s", ageFlag_70s.get("STUDENT_COUNT"));
			}
			searchParams.put("AGE_FLAG", "4"); //60后
			List generation_60s = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generation_60s)){
				Map ageFlag_60s = (Map) generation_60s.get(0); 
				resultMap.put("AGE_FLAG_60s", ageFlag_60s.get("STUDENT_COUNT"));
			}
			searchParams.put("AGE_FLAG", "5"); //60前
			List generation_60s_ago = gjtSignUpInfoDataDao.userAttributeCount_AGE(searchParams);
			if(EmptyUtils.isNotEmpty(generation_60s_ago)){
				Map ageFlag_60s_ago = (Map) generation_60s_ago.get(0);
				resultMap.put("AGE_FLAG_60s_ago", ageFlag_60s_ago.get("STUDENT_COUNT"));
			}
			
			searchParams.remove("AGE_FLAG");
			List sumSex = gjtSignUpInfoDataDao.userAttributeCount_SEX(searchParams);
			searchParams.put("SEX_FLAG", "1"); //男
			List maleList = gjtSignUpInfoDataDao.userAttributeCount_SEX(searchParams);
			searchParams.put("SEX_FLAG", "2"); //女
			List femaleList = gjtSignUpInfoDataDao.userAttributeCount_SEX(searchParams);
			Map sumMap = new HashMap();
			Map maleMap = new HashMap();
			Map femaleMap = new HashMap();
			if(EmptyUtils.isNotEmpty(sumSex)){
				sumMap = (Map) sumSex.get(0);
			}
			if(EmptyUtils.isNotEmpty(maleList)){
				maleMap = (Map) maleList.get(0);
			}
			if(EmptyUtils.isNotEmpty(femaleList)){
				femaleMap = (Map) femaleList.get(0);
			}
			
			DecimalFormat df = new DecimalFormat("0.00");
			int generSum = Integer.parseInt(ObjectUtils.toString(resultMap.get("GENERATIONSUM")));
			
			if(generSum !=0){
				String gener_00s = df.format((float)(Integer.parseInt(ObjectUtils.toString(resultMap.get("AGE_FLAG_00s"))))/generSum);
				String gener_90s = df.format((float)(Integer.parseInt(ObjectUtils.toString(resultMap.get("AGE_FLAG_90s"))))/generSum);
				String gener_80s = df.format((float)(Integer.parseInt(ObjectUtils.toString(resultMap.get("AGE_FLAG_80s"))))/generSum);
				String gener_70s = df.format((float)(Integer.parseInt(ObjectUtils.toString(resultMap.get("AGE_FLAG_70s"))))/generSum);
				String gener_60s = df.format((float)(Integer.parseInt(ObjectUtils.toString(resultMap.get("AGE_FLAG_60s"))))/generSum);
				String gener_60s_ago = df.format((float)(Integer.parseInt(ObjectUtils.toString(resultMap.get("AGE_FLAG_60s_ago"))))/generSum);
				
				resultMap.put("AGE_FLAG_00s", ObjectUtils.toString(new BigDecimal(gener_00s).multiply(new BigDecimal(100)).intValue()));
				resultMap.put("AGE_FLAG_90s", ObjectUtils.toString(new BigDecimal(gener_90s).multiply(new BigDecimal(100)).intValue()));
				resultMap.put("AGE_FLAG_80s", ObjectUtils.toString(new BigDecimal(gener_80s).multiply(new BigDecimal(100)).intValue()));
				resultMap.put("AGE_FLAG_70s", ObjectUtils.toString(new BigDecimal(gener_70s).multiply(new BigDecimal(100)).intValue()));
				resultMap.put("AGE_FLAG_60s", ObjectUtils.toString(new BigDecimal(gener_60s).multiply(new BigDecimal(100)).intValue()));
				resultMap.put("AGE_FLAG_60s_ago", ObjectUtils.toString(new BigDecimal(gener_60s_ago).multiply(new BigDecimal(100)).intValue()));
			}else{
				resultMap.put("AGE_FLAG_00s", "0");
				resultMap.put("AGE_FLAG_90s", "0");
				resultMap.put("AGE_FLAG_80s", "0");
				resultMap.put("AGE_FLAG_70s", "0");
				resultMap.put("AGE_FLAG_60s", "0");
				resultMap.put("AGE_FLAG_60s_ago", "0");
			}
			
			int sum = Integer.parseInt(ObjectUtils.toString(sumMap.get("SEX_COUNT")));
			if(sum!=0){
				String male_result = df.format((float)(Integer.parseInt(ObjectUtils.toString(maleMap.get("SEX_COUNT"))))/sum);
				String female_result = df.format((float)(Integer.parseInt(ObjectUtils.toString(femaleMap.get("SEX_COUNT"))))/sum);
				resultMap.put("MALE_COUNT", ObjectUtils.toString(maleMap.get("SEX_COUNT")));
				resultMap.put("FEMALE_COUNT", ObjectUtils.toString(femaleMap.get("SEX_COUNT")));
				
				int test1 = new BigDecimal(male_result).multiply(new BigDecimal(100)).intValue();
				int test2 = new BigDecimal(female_result).multiply(new BigDecimal(100)).intValue();

//				resultMap.put("MALE_PERCENT", ObjectUtils.toString(Math.round(test1/10D)*10));
//				resultMap.put("FEMALE_PERCENT", ObjectUtils.toString(Math.round(test2/10D)*10));
				resultMap.put("MALE_PERCENT", test1);
				resultMap.put("FEMALE_PERCENT", test2);
			}else{
				resultMap.put("MALE_COUNT", "0");
				resultMap.put("FEMALE_COUNT", "0");
				resultMap.put("MALE_PERCENT", "0");
				resultMap.put("FEMALE_PERCENT", "0");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map userPyccCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			searchParams.put("PYCC_FLAG", "0"); //专科
			List highSpin = gjtSignUpInfoDataDao.userPyccCount(searchParams);
			if(EmptyUtils.isNotEmpty(highSpin)){
				Map spinMap = (Map) highSpin.get(0);
				resultMap.put("HIGHSPIN", spinMap.get("PYCC_COUNT"));
			}
			searchParams.put("PYCC_FLAG", "2"); //本科
			List specilaEdition = gjtSignUpInfoDataDao.userPyccCount(searchParams);
			if(EmptyUtils.isNotEmpty(specilaEdition)){
				Map editionMap = (Map) specilaEdition.get(0);
				resultMap.put("SPECIALE_DITION", editionMap.get("PYCC_COUNT"));
			}
//			searchParams.put("PYCC_FLAG", "4"); //中专
//			List juniorList = gjtSignUpInfoDataDao.userPyccCount(searchParams);
//			if(EmptyUtils.isNotEmpty(juniorList)){
//				Map juniorMap = (Map) juniorList.get(0);
//				resultMap.put("JUNIOR", juniorMap.get("PYCC_COUNT"));
//			}
//			searchParams.put("PYCC_FLAG", "6"); //高起专_助力计划
//			List high_powerPlan = gjtSignUpInfoDataDao.userPyccCount(searchParams);
//			if(EmptyUtils.isNotEmpty(high_powerPlan)){
//				Map highPlanMap = (Map) high_powerPlan.get(0);
//				resultMap.put("HIGH_POWER_PLAN", highPlanMap.get("PYCC_COUNT"));
//			}
//			searchParams.put("PYCC_FLAG", "8"); //专升本_助力计划
//			List specialPlan = gjtSignUpInfoDataDao.userPyccCount(searchParams);
//			if(EmptyUtils.isNotEmpty(specialPlan)){
//				Map specialPlanMap = (Map) specialPlan.get(0);
//				resultMap.put("SPECIAL_PLAN", specialPlanMap.get("PYCC_COUNT"));
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map userCountBySpecial(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			List<Map<String, Object>> list = gjtSignUpInfoDataDao.querySpecialCount(searchParams);
			resultMap.put("SPECIALLIST", list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map queryEnrolmentCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		Map temp = null;
		try{
			searchParams.put("AUDIT_STATE", "4"); //未提交
			List list_4 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_4)){
				temp = (Map) list_4.get(0);
				resultMap.put("AUDIT_STATE_4", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "3"); //待审核
			List list_3 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_3)){
				temp = (Map) list_3.get(0);
				resultMap.put("AUDIT_STATE_3", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "1"); //审核通过
			List list_1 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_1)){
				temp = (Map) list_1.get(0);
				resultMap.put("AUDIT_STATE_1", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "2"); //审核中
			List list_2 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_2)){
				temp = (Map) list_2.get(0);
				resultMap.put("AUDIT_STATE_2", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "0"); //审核不通过
			List list_0 = gjtSignUpInfoDataDao.queryEnrolmentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_0)){
				temp = (Map) list_0.get(0);
				resultMap.put("AUDIT_STATE_0", temp.get("STUDENT_COUNT"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map queryPaymentCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		Map temp = null;
		try{
			/*
			searchParams.put("CHARGE", "1"); //已缴费
			List list_1 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_1)){
				temp = (Map) list_1.get(0);
				resultMap.put("CHARGE_1", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("CHARGE", "0"); //未缴费
			List list_0 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_0)){
				temp = (Map) list_0.get(0);
				resultMap.put("CHARGE_0", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("CHARGE","2"); //已退费
			List list_2 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_2)){
				temp = (Map) list_2.get(0);
				resultMap.put("CHARGE_2", temp.get("STUDENT_COUNT"));
			} */

			searchParams.put("CHARGE", "0"); //已全额缴费
			List list_1 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_1)){
				temp = (Map) list_1.get(0);
				resultMap.put("CHARGE_0", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("CHARGE", "1"); //已部分缴费
			List list_0 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_0)){
				temp = (Map) list_0.get(0);
				resultMap.put("CHARGE_1", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("CHARGE","2"); //待缴费
			List list_2 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_2)){
				temp = (Map) list_2.get(0);
				resultMap.put("CHARGE_2", temp.get("STUDENT_COUNT"));
			}
			searchParams.put("CHARGE","3"); //已欠费
			List list_3 = gjtSignUpInfoDataDao.queryPaymentCount(searchParams);
			if(EmptyUtils.isNotEmpty(list_3)){
				temp = (Map) list_3.get(0);
				resultMap.put("CHARGE_3", temp.get("STUDENT_COUNT"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map queryStudyCenter(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			List list = gjtSignUpInfoDataDao.statisticsByStudyCenter(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				Map listMap_1 = (Map) list.get(0);
				Map listMap_2 = null;
				if (list.size()>1){
					listMap_2 = (Map) list.get(1);
				}
				Map listMap_3 = null;
				if (list.size()>2){
					listMap_3 = (Map) list.get(2);
				}
				Map listMap_4 = null;
				if (list.size()>3){
					listMap_4 = (Map) list.get(3);
				}
				Map listMap_5 = null;
				if (list.size()>4){
					listMap_5 = (Map) list.get(4);
				}

				if(EmptyUtils.isNotEmpty(listMap_1)){
					resultMap.put("SC_NAME_1", ObjectUtils.toString(listMap_1.get("SC_NAME")));
					resultMap.put("STUDY_COUNT_1", ObjectUtils.toString(listMap_1.get("STUDY_COUNT")));
				}else{
					resultMap.put("SC_NAME_1", "");
					resultMap.put("STUDY_COUNT_1", "0");
				}
				
				if(EmptyUtils.isNotEmpty(listMap_2)){
					resultMap.put("SC_NAME_2", ObjectUtils.toString(listMap_2.get("SC_NAME")));
					resultMap.put("STUDY_COUNT_2", ObjectUtils.toString(listMap_2.get("STUDY_COUNT")));
				}else{
					resultMap.put("SC_NAME_2", "");
					resultMap.put("STUDY_COUNT_2", "0");
				}
				
				if(EmptyUtils.isNotEmpty(listMap_3)){
					resultMap.put("SC_NAME_3", ObjectUtils.toString(listMap_3.get("SC_NAME")));
					resultMap.put("STUDY_COUNT_3", ObjectUtils.toString(listMap_3.get("STUDY_COUNT")));
				}else{
					resultMap.put("SC_NAME_3", "");
					resultMap.put("STUDY_COUNT_3","0");
				}
				
				if(EmptyUtils.isNotEmpty(listMap_4)){
					resultMap.put("SC_NAME_4", ObjectUtils.toString(listMap_4.get("SC_NAME")));
					resultMap.put("STUDY_COUNT_4", ObjectUtils.toString(listMap_4.get("STUDY_COUNT")));
				}else{
					resultMap.put("SC_NAME_4", "");
					resultMap.put("STUDY_COUNT_4", "0");
				}
				
				if(EmptyUtils.isNotEmpty(listMap_5)){
					resultMap.put("SC_NAME_5", ObjectUtils.toString(listMap_5.get("SC_NAME")));
					resultMap.put("STUDY_COUNT_5", ObjectUtils.toString(listMap_5.get("STUDY_COUNT")));
				}else{
					resultMap.put("SC_NAME_5", "");
					resultMap.put("STUDY_COUNT_5", "0");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map queryAreaCount(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try {
			List list = gjtSignUpInfoDataDao.queryAreaCount(searchParams);
			if (EmptyUtils.isNotEmpty(list)){
				resultMap.put("AREA_LIST",list);
			}else {
				resultMap.put("AREA_LIST",new ArrayList());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map countSignupInfo(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		Map temp = null;
		try{
			//资料提交
			searchParams.put("AUDIT_STATE", "0"); //审核不通过
			List list_0 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list_0)){
				temp = (Map) list_0.get(0);
				resultMap.put("AUDIT_STATE_0", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "1"); //审核通过
			List list_1 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list_1)){
				temp = (Map) list_1.get(0);
				resultMap.put("AUDIT_STATE_1", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "2"); //审核中
			List list_2 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list_2)){
				temp = (Map) list_2.get(0);
				resultMap.put("AUDIT_STATE_2", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "3");  //待审核
			List list_3 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list_3)){
				temp = (Map) list_3.get(0);
				resultMap.put("AUDIT_STATE_3", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("AUDIT_STATE", "4"); //未提交
			List list_4 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list_4)){
				temp = (Map) list_4.get(0);
				resultMap.put("AUDIT_STATE_4", temp.get("SIGNUP_COUNT"));
			}
			
			//学籍状态
			searchParams.remove("AUDIT_STATE");
			searchParams.put("XJZT_STATE", "3"); //待注册
			List xjzt_list_1 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_1)){
				temp = (Map) xjzt_list_1.get(0);
				resultMap.put("XJZT_STATE_1", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("XJZT_STATE", "0"); //注册中
			List xjzt_list_2 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_2)){
				temp = (Map) xjzt_list_2.get(0);
				resultMap.put("XJZT_STATE_2", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("XJZT_STATE", "2"); //在籍
			List xjzt_list_3 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_3)){
				temp = (Map) xjzt_list_3.get(0);
				resultMap.put("XJZT_STATE_3", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("XJZT_STATE", "5"); //退学
			List xjzt_list_4 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_4)){
				temp = (Map) xjzt_list_4.get(0);
				resultMap.put("XJZT_STATE_4", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("XJZT_STATE", "4"); //休学
			List xjzt_list_5 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_5)){
				temp = (Map) xjzt_list_5.get(0);
				resultMap.put("XJZT_STATE_5", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("XJZT_STATE", "10"); //转学
			List xjzt_list_6 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_6)){
				temp = (Map) xjzt_list_6.get(0);
				resultMap.put("XJZT_STATE_6", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("XJZT_STATE", "8"); //毕业
			List xjzt_list_7 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(xjzt_list_7)){
				temp = (Map) xjzt_list_7.get(0);
				resultMap.put("XJZT_STATE_7", temp.get("SIGNUP_COUNT"));
			}

			//缴费状态
			/*
			searchParams.remove("XJZT_STATE");
			searchParams.put("CHARGE", "0"); //未缴费
			List charge_list_0 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(charge_list_0)){
				temp = (Map)  charge_list_0.get(0);
				resultMap.put("CHARGE_0", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("CHARGE", "1"); //已缴费
			List charge_list_1 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(charge_list_1)){
				temp = (Map) charge_list_1.get(0);
				resultMap.put("CHARGE_1", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("CHARGE","2"); //已退费
			List charge_list_2 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if(EmptyUtils.isNotEmpty(charge_list_2)){
				temp = (Map) charge_list_2.get(0);
				resultMap.put("CHARGE_2", temp.get("SIGNUP_COUNT"));
			}
			*/

			//缴费状态
			searchParams.remove("XJZT_STATE");
			searchParams.put("CHARGE","0"); //已全额缴费
			List charge_list_0 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if (EmptyUtils.isNotEmpty(charge_list_0)){
				temp = (Map)  charge_list_0.get(0);
				resultMap.put("CHARGE_0", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("CHARGE","1"); //已部分缴费
			List charge_list_1 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if (EmptyUtils.isNotEmpty(charge_list_1)){
				temp = (Map)  charge_list_1.get(0);
				resultMap.put("CHARGE_1", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("CHARGE","2"); //待缴费
			List charge_list_2 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if (EmptyUtils.isNotEmpty(charge_list_2)){
				temp = (Map)  charge_list_2.get(0);
				resultMap.put("CHARGE_2", temp.get("SIGNUP_COUNT"));
			}
			searchParams.put("CHARGE","3"); //已欠费
			List charge_list_3 = gjtSignUpInfoDataDao.countSignupInfo(searchParams);
			if (EmptyUtils.isNotEmpty(charge_list_3)){
				temp = (Map)  charge_list_3.get(0);
				resultMap.put("CHARGE_3", temp.get("SIGNUP_COUNT"));
			}


			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map countStudentRollSituationBy(Map<String, Object> searchParams) {
		return gjtSignUpInfoDataDao.countStudentRollSituationBy(searchParams);
	}

	@Transactional
	public Map chargeSignupNew(Map<String, Object> searchParams) {
		Map resultMap = new LinkedHashMap();
		try {
			String studentId = ObjectUtils.toString(searchParams.get("studentId"),"").trim();
			if (EmptyUtils.isEmpty(studentId)){
				resultMap.put("success","false");
				resultMap.put("message","idCard不能为空！");
				return resultMap;
			}
			String charge = ObjectUtils.toString(searchParams.get("charge"),"").trim();
            if (EmptyUtils.isEmpty(charge)){
                resultMap.put("success","false");
                resultMap.put("message","缴费状态不能为空！");
                return resultMap;
            }

			int i = gjtSignUpInfoDataDao.chargeSignupNew(searchParams);
			if (i > 0){
				resultMap.put("success","true");
				resultMap.put("message","缴费确认成功");
				resultMap.put("result",ObjectUtils.toString(i));
			}else {
				resultMap.put("success","false");
				resultMap.put("message","缴费确认失败");
				resultMap.put("result",ObjectUtils.toString(i));
			}
		}catch (Exception e){
			e.printStackTrace();
			resultMap.put("success","false");
			resultMap.put("message",e.getMessage());
		}
		return resultMap;
	}


	/**
	 * 查询报读学员Signup
	 * @param studentId
	 * @return
	 */
	@Override
	public Map getSignUpByStudentId(String studentId) {
		Map resultMap = new LinkedHashMap();
		try {
			List list = gjtSignUpInfoDataDao.getSignUpByStudentId(studentId);
			if (EmptyUtils.isNotEmpty(list)){
				resultMap = (Map) list.get(0);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}
}
