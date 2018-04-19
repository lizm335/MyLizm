package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyDegree;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：学位申请
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年9月18日
 * @version 3.0
 *
 */
public interface GjtGraduationApplyDegreeService extends BaseService<GjtGraduationApplyDegree> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月19日 下午7:42:26
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtGraduationApplyDegree> queryGraduationApplyCardByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月19日 下午7:45:27
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	long countGraduationApplyCardByPage(Map<String, Object> searchParams);

	/**
	 * 查询审核记录
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月22日 上午10:04:55
	 * @param recordId
	 * @return
	 */
	GjtGraApplyFlowRecord queryFlowRecordById(String recordId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月22日 上午10:16:09
	 * @param record
	 * @return
	 */
	GjtGraApplyFlowRecord saveFlowRecord(GjtGraApplyFlowRecord record);

	/**
	 * 查询学生成绩
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月22日 下午5:32:03
	 * @param studentId
	 * @return
	 */
	List<Map<String, Object>> queryAchievementByStudentId(String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月25日 上午12:57:42
	 * @param applyId
	 * @param roleId
	 * @return
	 */
	GjtGraApplyFlowRecord queryFlowRecordByApplyId(String applyId, int roleId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月25日 上午1:06:46
	 * @param studentId
	 * @return
	 */
	GjtGraduationApplyDegree queryDegreeApplyByStudentId(String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月26日 上午1:47:29
	 * @param searchParams
	 * @param realPath
	 * @return
	 * @throws Exception
	 */
	String downloadReqFile(Map<String, Object> searchParams, String realPath) throws Exception;

	/**
	 * 学员学位情况
	 * @param searchParams
	 * @return
	 */
    Map<String,Object> countStudentApplyDegreeSituation(Map<String, Object> searchParams);
}
