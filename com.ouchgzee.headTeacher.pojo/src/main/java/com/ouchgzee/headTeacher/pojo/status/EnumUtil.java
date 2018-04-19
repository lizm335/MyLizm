package com.ouchgzee.headTeacher.pojo.status;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举工具类
 * @author eenet09
 *
 */
@Deprecated public class EnumUtil {
	
	/**
	 * 获取毕业论文状态列表
	 * @return
	 */
	public static Map<Integer, String> getThesisApplyStatusMap() {
		Map<Integer, String> applyStatusMap = new HashMap<Integer, String>();
		for (GraduationApplyStatusEnum.ThesisStatusEnum applyStatusEnum : GraduationApplyStatusEnum.ThesisStatusEnum.values()) {
			applyStatusMap.put(applyStatusEnum.getValue(), applyStatusEnum.getName());
		}
		
		return applyStatusMap;
	}
	
	/**
	 * 获取社会实践状态列表
	 * @return
	 */
	public static Map<Integer, String> getPracticeApplyStatusMap() {
		Map<Integer, String> applyStatusMap = new HashMap<Integer, String>();
		for (GraduationApplyStatusEnum.PracticeStatusEnum applyStatusEnum : GraduationApplyStatusEnum.PracticeStatusEnum.values()) {
			applyStatusMap.put(applyStatusEnum.getValue(), applyStatusEnum.getName());
		}
		
		return applyStatusMap;
	}
	
	/**
	 * 获取论文进度码列表
	 * @return
	 */
	public static Map<String, String> getThesisProgressCodeMap() {
		Map<String, String> progressCodeMap = new HashMap<String, String>();
		for (GraduationProgressCodeEnum.ThesisProgressCodeEnum progressCodeEnum : GraduationProgressCodeEnum.ThesisProgressCodeEnum.values()) {
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
		for (GraduationProgressCodeEnum.PracticeProgressCodeEnum progressCodeEnum : GraduationProgressCodeEnum.PracticeProgressCodeEnum.values()) {
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
	
}
