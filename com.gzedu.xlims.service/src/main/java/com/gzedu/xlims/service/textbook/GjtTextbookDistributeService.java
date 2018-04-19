package com.gzedu.xlims.service.textbook;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;

public interface GjtTextbookDistributeService {

	public void update(GjtTextbookDistribute entity);

	public void update(List<GjtTextbookDistribute> entities);

	public GjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status, String isDeleted);

	public Page<Map<String, Object>> findDistributeSummary(Map<String, Object> searchParams, PageRequest pageRequst);

	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted,
			Collection<Integer> statuses);

	public List<GjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String isDeleted,
			Collection<Integer> statuses);

	/**
	 * 查询当前待发教材总数
	 * 
	 * @param orgId
	 * @param studyYearCode
	 * @return
	 */
	public int queryCurrentDistributeCount(String orgId, int studyYearCode);

	/**
	 * 查询当前待发教材列表
	 * 
	 * @param orgId
	 * @param studyYearCode
	 * @param isEnough：
	 *            true-只查询足够库存的，false或null-查询全部
	 * @return
	 */
	public List<Object[]> queryCurrentDistributeList(String orgId, int studyYearCode, Boolean isEnough);

	/**
	 * 查询学生的当前待发教材列表
	 * 
	 * @param orgId
	 * @param studyYearCode
	 * @return
	 */
	public List<Object[]> queryStudentCurrentDistributeList(String orgId, int studyYearCode);

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 上午11:00:35
	 * @param distributeId
	 */
	public GjtTextbookDistribute queryById(String distributeId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:35:12
	 * @param studentId
	 * @param isDeleted
	 * @param status
	 * @return
	 */
	List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatus(String studentId, String isDeleted, int status);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月24日 下午3:37:37
	 * @param tbd
	 */
	public void updateTextbookDistributeAndOrderDetai(GjtTextbookDistribute tbd);

	/**
	 * 发放时间小于当前时间减7天的配送状态全部改成已签收
	 * 
	 * @param date
	 */
	public void updateStatus(String date);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月23日 下午5:52:49
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryDistributeList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月24日 下午8:32:06
	 * @param searchParams
	 * @return
	 */
	List<Map<String, Object>> queryDistributeDetailList(Map<String, Object> searchParams);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月7日 下午2:56:00
	 * @param distribute
	 * @param details
	 */
	public void saveTextbookDistributeAndDetail(GjtTextbookDistribute distribute, List<GjtTextbookDistributeDetail> details);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月26日 上午11:31:28
	 * @param studentId
	 * @param isDeleted
	 * @param statuses
	 * @return
	 */
	Long countByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted, Collection<Integer> statuses);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月9日 下午4:15:30
	 * @param studentId
	 * @param isDeleted
	 * @param statuses
	 * @return
	 */
	List<GjtTextbookDistribute> findByStudentIdAndStatus(String studentId, String isDeleted, Collection<Integer> statuses);

}
