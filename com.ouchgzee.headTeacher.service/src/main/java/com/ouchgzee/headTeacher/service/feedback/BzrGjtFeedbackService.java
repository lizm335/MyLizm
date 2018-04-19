/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.feedback;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.dto.FeedbackSolvedDto;
import com.ouchgzee.headTeacher.dto.FeedbackUnsolvedDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeedback;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 留言反馈业务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月31日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtFeedbackService extends BaseService<BzrGjtFeedback> {

	/**
	 * 分页根据条件查询班级学员未解决的留言反馈
	 * 
	 * @param searchParams
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<FeedbackUnsolvedDto> queryUnsolvedFeedBackByBzrIdPage(Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 分页根据条件查询班级学员已解决的的留言反馈
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<FeedbackSolvedDto> querySolvedFeedBackByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 统计班级未解决的留言反馈
	 * 
	 * @param searchParams
	 * @return
	 */
	long countUnsolvedFeedBack(Map<String, Object> searchParams);

	/**
	 * 统计已解决的留言反馈
	 * 
	 * @return
	 */
	long countSolvedFeedBack(Map<String, Object> searchParams);

	/**
	 * 留言反馈信息转发给其他老师
	 * 
	 * @param id
	 * @param shareEmployeeId
	 * @param updatedBy
	 * @return
	 */
	boolean shareTeacher(String id, String shareEmployeeId, String updatedBy);

}
