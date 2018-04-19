/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.message.GjtMessageInfo;

/**
 * 功能说明：教务管理-通知公告接口
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 *
 */
public interface GjtMessageInfoService {

	// 根据条件查询,通知公告信息
	public Page<GjtMessageInfo> querySource(GjtMessageInfo entity, PageRequest pageRequest);

	// 添加查询通知公告
	public GjtMessageInfo saveEntity(GjtMessageInfo entity);

	// 删除查询通知公告
	public void delete(String id);

	// 通过id查询通知公告
	public GjtMessageInfo queryById(String id);

	// 修改通知公告
	public Boolean updateEntity(GjtMessageInfo entity);

	public Boolean updateIsRead(String messageId, String userId, String platform);

	// 查询全部数据
	public List<GjtMessageInfo> queryAll();

	// 带分页的查询
	Page<GjtMessageInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public List<Object> queryStudent(String specialtyIds, String gradeIds, String pyccs, String orgId);

	public List<Object> queryEmployee(String orgId, String specialtyIds, String gradeIds, String pyccs);

	List getSpecialtyByCode(Map searchParams);

	List getXXIdByCode(Map searchParams);

	List<Object> queryStudentInfo(Map searchParams);

	public List<Map<String, Object>> queryStudentByGradeIdCourseId(Map<String, Object> params);

	/**
	 * 查看分类是否有引用
	 * 
	 * @param id
	 */
	public long findByTypeClassify(String id);

}
