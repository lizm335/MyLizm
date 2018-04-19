/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtStudyYearAssignment;

/**
 * 
 * 功能说明： 学年度 任务制作
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudyAssignService {
	public Page<GjtStudyYearAssignment> queryAll(Map<String, Object> map, PageRequest pageRequest);

	public List<GjtStudyYearAssignment> queryAlls(Map<String, Object> map);

	public Boolean saveEntity(GjtStudyYearAssignment entity);

	public Boolean updateEntity(GjtStudyYearAssignment entity);

	public GjtStudyYearAssignment queryById(String id);

	/**
	 * 真删除
	 * 
	 * @param list
	 */
	public void delete(String[] list);

	/**
	 * 假删除
	 * 
	 * @param list
	 */
	public void deleteById(String[] list);

	public void updateStutas(String id, String status);
}
