/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyModuleLimit;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.GjtSpecialtyDto;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：专业管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtSpecialtyService extends BaseService<GjtSpecialty> {
	// 根据条件查询数据源
	public Page<GjtSpecialty> querySource(final GjtSpecialty searchEntity, PageRequest pageRequest);

	// 添加专业信息
	public Boolean saveEntity(GjtSpecialty entity);

	// 修改专业信息
	public Boolean updateEntity(GjtSpecialty entity);

	// 查询所有专业
	public List<GjtSpecialty> queryAll();

	/**
	 * 查询专业对应的课程
	 * 
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> querySpecialtyCourse(String id);

	/**
	 * 假
	 * 
	 * @param ids
	 * @return
	 */
	public Boolean deleteById(String[] ids);

	/**
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtSpecialty> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	public Boolean updateSpecialtyModule(GjtSpecialtyModuleLimit sm);

	public Boolean saveSpecialtyModule(GjtSpecialtyModuleLimit sm);

	public void deleteSpecialtyModule(String id);
	
	public List<GjtSpecialtyModuleLimit> querySpecialtyModuleList(String specialtyId);

	/**
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtSpecialty> queryPage(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @param SpecialtyId
	 * @return
	 */
	GjtSpecialty findBySpecialtyById(String SpecialtyId);

	/**
	 * 共享专业
	 * 
	 * @return
	 */
	Page<GjtSpecialty> queryAllAndShare(String schoolIds, Map<String, Object> searchParams, PageRequest pageRequst,
			List<String> specialtyId);

	/**
	 * @param orgId
	 * @param gradeId
	 * @param type
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtSpecialty> queryGradeSpecialtyAll(String orgId, String gradeId, int type, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public List<GjtSpecialty> findSpecialtyByGradeId(String gradeId);

	List<GjtSpecialty> findSpecialtyBySpecialtyBaseId(String specialtyBaseId);
	
	public List<GjtSpecialty> findAll(Iterable<String> ids);
	
	/**
	 * 拷贝专业
	 * @param specialtyId 原专业id
	 * @param ruleCode 新专业规则号
	 * @param user 创建者
	 * @return
	 */
	public GjtSpecialty copy(String specialtyId, String ruleCode, GjtUserAccount user);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月14日 下午2:02:42
	 * @param gradeId
	 * @return
	 */
	public List<GjtSpecialtyDto> findSpecialtyDtoByGradeId(String gradeId);
	
	/**
	 * 查询院校下的专业
	 * @param xxId
	 * @return
	 */
	public List<GjtSpecialty> findSpecialtyByOrgId(String xxId);

	/**
	 * 根据规则号获取专业
	 * @param ruleCode
	 * @param xxId
	 * @return
	 */
	GjtSpecialty queryByRuleCodeAndXxId(String ruleCode, String xxId);

}
