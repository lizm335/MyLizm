package com.gzedu.xlims.service.transaction;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtExemptExamInfo;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月28日
 * @version 2.5
 */
public interface GjtExemptExamInfoService {

	boolean insertExemptExamInfo(Map<String, Object> applyExemptExamMap, String[] materialId, String[] awardDate, String[] awardUnit, String[] url);

	Page<GjtExemptExamInfo> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst);

	List <GjtExemptExamInfo> findByStudentIdAndCourseIdOrderByCreatedDtDesc(String studentId, String courseId);

	boolean exemptExamAudit(Map<String, Object> data);

	long queryTotalNum(String xxId);

	long queryByStatusTotalNum(String xxId, int auditState, String auditOperatorRole);

	Map<String, Object> queryExemptExamInfo(String exemptExamId);
	
}
