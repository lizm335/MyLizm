/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * 
 * 功能说明：学习中心管理
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudyCenterService {

	// 查询该用户下的学习中心
	Page<GjtStudyCenter> queryAll(GjtUserAccount user, Map<String, Object> searchParams, PageRequest pageRequst);

	// 查询该用户下的学习中心
	Page<Map> queryAllOrg(Map<String, Object> searchParams, PageRequest pageRequst);

	long queryOrgCount(Map<String, Object> searchParams);

	// 查询全部学习中心
	public List<GjtStudyCenter> queryAll();

	// 查询单个学习中心
	public GjtStudyCenter queryById(String id);

	public GjtStudyCenter queryByScName(String scName);

	/**
	 * 学习中心关联学院
	 * 
	 * @param gjtStudyCenter
	 * @param gjtSchoolInfo
	 */
	public void setSchoolInfo(GjtStudyCenter gjtStudyCenter, GjtSchoolInfo gjtSchoolInfo);

	public Map<String, Object> queryStudyCenterStudentSize(List<GjtStudyCenter> studyCenterIds);

	/**
	 * 查询当前的登录院校
	 * 
	 * @param id
	 * @return
	 */
	public GjtSchoolInfo queryByOrgId(String id);

	/**
	 * 新增学习中心/招生点
	 * 
	 * @param id
	 * @param entity
	 */
	public boolean insertStudyCenter(GjtUserAccount user, GjtStudyCenter entity, String suoShuXxId,
			String suoShuXxzxId);

	/**
	 * 接口调用
	 * 
	 * @param entity
	 * @param orgType
	 * @return
	 */
	public boolean insert(GjtStudyCenter entity);

	/**
	 * 查询学习中心编码是否存在
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtStudyCenter> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 更新学习中心
	 * 
	 * @param entity
	 * @return
	 */
	boolean update(GjtUserAccount user, GjtStudyCenter entity, String suoShuXxId, String suoShuXxzxId);

	boolean update(GjtStudyCenter entity);

	/**
	 * 删除学习中心
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 学校中心审核
	 * 
	 * @param id
	 * @param audit
	 * @return
	 */
	boolean updateAudit(String id, String audit);

	boolean updateEntity(GjtStudyCenter entity);
}
