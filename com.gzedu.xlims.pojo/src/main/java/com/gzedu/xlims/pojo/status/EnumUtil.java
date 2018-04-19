package com.gzedu.xlims.pojo.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举工具类
 * @author eenet09
 *
 */
public class EnumUtil {
	
	/**
	 * 获取毕业论文状态列表
	 * @return
	 */
	public static Map<Integer, String> getThesisApplyStatusMap() {
		Map<Integer, String> applyStatusMap = new HashMap<Integer, String>();
		for (ThesisStatusEnum applyStatusEnum : ThesisStatusEnum.values()) {
			applyStatusMap.put(applyStatusEnum.getValue(), applyStatusEnum.getName());
		}
		
		return applyStatusMap;
	}
	
	/**
	 * 获取毕业论文状态列表
	 * @return
	 */
	public static Map<String, String> getThesisApplyStatusStringMap() {
		Map<String, String> applyStatusMap = new HashMap<String, String>();
		for (ThesisStatusEnum applyStatusEnum : ThesisStatusEnum.values()) {
			applyStatusMap.put(String.valueOf(applyStatusEnum.getValue()), applyStatusEnum.getName());
		}
		
		return applyStatusMap;
	}
	
	/**
	 * 获取社会实践状态列表
	 * @return
	 */
	public static Map<Integer, String> getPracticeApplyStatusMap() {
		Map<Integer, String> applyStatusMap = new HashMap<Integer, String>();
		for (PracticeStatusEnum applyStatusEnum : PracticeStatusEnum.values()) {
			applyStatusMap.put(applyStatusEnum.getValue(), applyStatusEnum.getName());
		}
		
		return applyStatusMap;
	}
	
	/**
	 * 获取社会实践状态列表
	 * @return
	 */
	public static Map<String, String> getPracticeApplyStatusStringMap() {
		Map<String, String> applyStatusMap = new HashMap<String, String>();
		for (PracticeStatusEnum applyStatusEnum : PracticeStatusEnum.values()) {
			applyStatusMap.put(String.valueOf(applyStatusEnum.getValue()), applyStatusEnum.getName());
		}
		
		return applyStatusMap;
	}
	
	/**
	 * 获取论文进度码列表
	 * @return
	 */
	public static Map<String, String> getThesisProgressCodeMap() {
		Map<String, String> progressCodeMap = new HashMap<String, String>();
		for (ThesisProgressCodeEnum progressCodeEnum : ThesisProgressCodeEnum.values()) {
			progressCodeMap.put(progressCodeEnum.getCode(), progressCodeEnum.getName());
		}
		
		return progressCodeMap;
	}
	
	/**
	 * 获取社会实践进度码列表
	 * @return
	 */
	public static Map<String, String> getPracticeProgressCodeMap() {
		Map<String, String> progressCodeMap = new HashMap<String, String>();
		for (PracticeProgressCodeEnum progressCodeEnum : PracticeProgressCodeEnum.values()) {
			progressCodeMap.put(progressCodeEnum.getCode(), progressCodeEnum.getName());
		}
		
		return progressCodeMap;
	}
	
	/**
	 * 获取毕业记录状态列表
	 * @return
	 */
	public static Map<Integer, String> getGraduationRecordStatusMap() {
		Map<Integer, String> recordStatusMap = new HashMap<Integer, String>();
		for (GraduationRecordStatusEnum recordStatusEnum : GraduationRecordStatusEnum.values()) {
			recordStatusMap.put(recordStatusEnum.getValue(), recordStatusEnum.getName());
		}
		
		return recordStatusMap;
	}
	
	/**
	 * 获取学位基础条件类型列表
	 * @return
	 */
	public static Map<Integer, String> getBaseDegreeRequirementTypeMap() {
		Map<Integer, String> baseTypeMap = getDegreeRequirementTypeMap();
		
		return baseTypeMap;
	}
	
	/**
	 * 获取学位条件类型列表
	 * @return
	 */
	public static Map<Integer, String> getDegreeRequirementTypeMap() {
		Map<Integer, String> baseTypeMap = new HashMap<Integer, String>();
		for (DegreeRequirementTypeEnum typeEnum : DegreeRequirementTypeEnum.values()) {
			baseTypeMap.put(typeEnum.getValue(), typeEnum.getName());
		}
		
		return baseTypeMap;
	}
	
	/**
	 * 获取快递公司列表
	 * @return
	 */
	public static Map<String, String> getExpressMap() {
		Map<String, String> expressMap = new LinkedHashMap<String, String>();
		for (ExpressEnum expressEnum : ExpressEnum.values()) {
			expressMap.put(expressEnum.getValue(), expressEnum.getName());
		}
		
		return expressMap;
	}
	
	public static List<Map<String, String>> getExpressList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (ExpressEnum expressEnum : ExpressEnum.values()) {
			Map<String, String> expressMap = new HashMap<String, String>();
			expressMap.put("name", expressEnum.getName());
			expressMap.put("value", expressEnum.getValue());
			
			list.add(expressMap);
		}
		
		return list;
	}
	public static Map<String, String> getBankMap(){
		Map<String, String> bankMap = new LinkedHashMap<String, String>();
		for (BankEnum bankEnum : BankEnum.values()) {
			bankMap.put(bankEnum.getValue(), bankEnum.getName());
		}		
		return bankMap;
	}
}
