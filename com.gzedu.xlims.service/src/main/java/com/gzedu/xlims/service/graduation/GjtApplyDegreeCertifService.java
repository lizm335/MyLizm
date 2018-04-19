package com.gzedu.xlims.service.graduation;

import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyDegree;
import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 
 * 功能说明：学位申请
 * 
 *
 */
public interface GjtApplyDegreeCertifService extends BaseService<GjtApplyDegreeCertif> {

	GjtApplyDegreeCertif queryApplyDegreeCertifByStudentIdAndPlanId(String studntId, String planId);

	Map<String,Long> countGroupbyAuditState(Map<String, Object> searchParams);

	/**
	 * 下载学位申请资料
	 * @param searchParams
	 * @param realPath
	 * @return
	 * @throws Exception
	 */
	String downloadReqFile(Map<String, Object> searchParams, String realPath) throws Exception;

	/**
	 * 创建单条审核记录
	 * @param entity
	 * @param auditState
	 * @param operatorRole
	 * @param auditContent
	 */
	void createRecord(GjtApplyDegreeCertif entity, int auditState, int operatorRole, String auditContent);

	/**
	 * 记录学生的申请记录
	 * @param certif
	 * @param id
	 */
	void studentApplyDegree(GjtApplyDegreeCertif certif, String id);
}
