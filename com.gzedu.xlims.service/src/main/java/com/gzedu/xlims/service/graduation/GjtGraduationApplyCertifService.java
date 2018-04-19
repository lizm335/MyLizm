package com.gzedu.xlims.service.graduation;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.service.base.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 毕业记录
 * 
 * @author eenet09
 *
 */
public interface GjtGraduationApplyCertifService extends BaseService<GjtGraduationApplyCertif> {

	/**
	 * 查询毕业记录
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<GjtGraduationApplyCertif> queryPage(Map<String, Object> searchParams, PageRequest pageRequest);

	public long queryPageCount(Map<String, Object> searchParams);

	/**
	 * 查询申请毕业证列表，带分页
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtGraduationApplyCertif> queryGraduationApplyCardByPage(Map<String, Object> searchParams,
			PageRequest pageRequest);

	public void update(GjtGraduationApplyCertif entity);

	/**
	 * 查询学生的模块学分获得详情
	 * 
	 * @param studentId
	 * @return
	 */
	public List<Object[]> queryModuleScore(String studentId);

	/**
	 * 查询学生的必修课程平均分
	 * 
	 * @param studentId
	 * @return
	 */
	public float queryCompulsorySumScore(String studentId);

	/**
	 * 查询学生的其它课程平均分
	 * 
	 * @param studentId
	 * @return
	 */
	public float queryOtherSumScore(String studentId);

	/**
	 * 查询学生的学位课程分数
	 * 
	 * @param studentId
	 * @return
	 */
	public float queryDegreeScore(String studentId);

	/**
	 * 查询学生的毕业设计分数
	 * 
	 * @param studentId
	 * @return
	 */
	public float queryDesignScore(String studentId);

	public GjtGraduationApplyCertif queryByStudentIdAndStatus(String studentId, int status);

	public GjtGraduationApplyCertif queryByStudentId(String studentId);

	/**
	 * 查询学生的毕业登记信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> queryStudentRegisterMsg(String studentId);

	// ---------------------------------------- 毕业申请，申请毕业证审核
	// -------------------------------------//

	/**
	 * 根据毕业申请ID获取毕业申请审核记录
	 * 
	 * @param applyId
	 * @return
	 */
	List<GjtGraApplyFlowRecord> queryGraApplyFlowRecordByApplyId(String applyId);

	/**
	 * 初始化毕业申请审核记录
	 * 
	 * @param applyId
	 * @return
	 */
	boolean initAuditGraduationApply(String applyId);

	/**
	 * 导入毕业申请记录
	 * 
	 * @param targetFile
	 * @param path
	 * @return
	 */
	Map importGraduationAudit(File targetFile, String path, String userId, String planId);

	/**
	 * 审核毕业申请
	 * 
	 * @param applyId
	 * @param auditState
	 * @param auditContent
	 * @param operatorRole
	 * @param operatorRealName
	 * @return
	 */
	ResultFeedback auditGraduationApply(String applyId, Integer auditState, String auditContent, int operatorRole,
			String operatorRealName);

	/**
	 * 学员毕业情况
	 * 
	 * @param searchParams
	 * @return
	 */
	Map<String, Object> countStudentApplyCertifSituation(Map<String, Object> searchParams);

	Map<String, Long> countGroupbyAuditState(Map<String, Object> searchParams);

	/**
	 * 根据studentId修改毕业照
	 * 
	 * @param entity
	 */
	public int updatePhotoUrl(String studentId, String photoUrl);

	/**
	 * 获取毕业证申请记录（带分页）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtApplyDegreeCertif> queryGraduationApplyDegreeByPage(Map<String, Object> searchParams, PageRequest pageRequst);

	GjtGraduationApplyCertif queryByStudentIdAndPlanId(String studentId, String planId);

	Workbook exportGraduationApply(Map formMap, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 学生申请毕业
	 * @param gjtGraduationApplyCertif
	 */
    void studentApply(GjtGraduationApplyCertif gjtGraduationApplyCertif, String userId);
}
