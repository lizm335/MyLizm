/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.dto.DegreeCollegeSpecialtyDto;
import com.gzedu.xlims.pojo.graduation.GjtDegreeCollege;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirementBase;
import com.gzedu.xlims.pojo.graduation.GjtSpecialtyDegreeCollege;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 学位院校、申请条件业务逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2016年11月20日
 * @EditDate 2016年11月21日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtDegreeCollegeService extends BaseService<GjtDegreeCollege> {

	/**
	 * 分页根据条件查询学位院校及专业信息
	 * @param orgId
	 * @param searchParams
	 * @param pageRequest
     * @return
     */
	Page<DegreeCollegeSpecialtyDto> queryDegreeCollegeSpecialtyByPage(String orgId, Map<String, Object> searchParams,
																	  PageRequest pageRequest);

	/**
	 * 获取院校的学位条件信息
	 * @param collegeId
	 * @return
	 */
	List<GjtDegreeRequirement> queryGjtDegreeRequirementBySpecialty(String collegeId, String specialtyId);

	/**
	 * 根据ID获取院校的学位条件信息
	 * @param requirementId
	 * @return
	 */
	GjtDegreeRequirement queryGjtDegreeRequirementById(String requirementId);

	/**
	 * 获取学位条件基础信息
	 * @return
	 */
	List<GjtDegreeRequirementBase> queryGjtDegreeRequirementBaseAll();

	/**
	 * 根据ID获取学位条件基础信息
	 * @param baseId
	 * @return
     */
	GjtDegreeRequirementBase queryGjtDegreeRequirementBaseById(String baseId);

	/**
	 * 添加院校并为院校设置专业
	 * @param entity
	 * @param specialtyIds
     * @return
     */
	boolean insertDegreeCollegeSpecialtys(GjtDegreeCollege entity, String[] specialtyIds);

	/**
	 * 更新学位院校
	 * @param entity
	 * @return
	 */
	boolean updateGjtDegreeCollegeSpecialtys(GjtDegreeCollege entity, String[] specialtyIds);

	/**
	 * 添加学位条件
	 * @param entity
	 * @return
	 */
	boolean insertGjtDegreeRequirement(GjtDegreeRequirement entity);

	/**
	 * 添加学位条件
	 * @param entityList
	 * @return
	 */
	boolean insertGjtDegreeRequirement(List<GjtDegreeRequirement> entityList);

	/**
	 * 更新学位条件
	 * @param entity
	 * @return
	 */
	boolean updateGjtDegreeRequirement(GjtDegreeRequirement entity);

	/**
	 * 更新学位条件
	 * @param entityList
	 * @return
	 */
	boolean updateGjtDegreeRequirement(List<GjtDegreeRequirement> entityList);

	/**
	 * 添加学位条件基础信息
	 * @param entity
	 * @return
	 */
	boolean insertGjtDegreeRequirementBase(GjtDegreeRequirementBase entity);

	/**
	 * 更新学位条件基础信息
	 * @param entity
	 * @return
	 */
	boolean updateGjtDegreeRequirementBase(GjtDegreeRequirementBase entity);

	/**
	 * 删除学位条件基础信息
	 * @param id
	 * @return
	 */
	boolean deleteGjtDegreeRequirementBase(String id);

	/**
	 * 批量删除学位条件基础信息
	 * @param ids
	 * @return
	 */
	boolean deleteGjtDegreeRequirementBaseInBatch(String[] ids);

	/**
	 * 删除学位专业及申请条件
	 * @param collegeSpecialtyId
	 * @return
	 */
	boolean deleteGjtSpecialtyDegreeCollegeReq(String collegeSpecialtyId);

	/**
	 * 批量删除学位专业及申请条件
	 * @param collegeSpecialtyIds
	 * @return
	 */
	boolean deleteGjtSpecialtyDegreeCollegeReqInBatch(String[] collegeSpecialtyIds);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月7日 下午2:29:54
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	long countDegreeCollegeSpecialty(String orgId, Map<String, Object> searchParams);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月8日 下午5:54:00
	 * @param collegeId
	 * @param gradeId
	 * @param degreeName
	 * @param specialtyIds
	 * @param requirementList
	 * @param orgId
	 */
	void CreateSpecialtyDegreeCollege(String collegeId, String gradeId, String degreeName, List<String> specialtyIds,
			List<GjtDegreeRequirement> requirementList, String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月9日 下午5:04:29
	 * @param id
	 * @return
	 */
	GjtSpecialtyDegreeCollege querySpecialtyDegreeCollegeById(String id);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月9日 下午5:35:35
	 * @param collegeSpecialtyId
	 * @return
	 */
	List<GjtDegreeRequirement> queryReqByCollegeSpecialtyId(String collegeSpecialtyId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月9日 下午6:36:27
	 * @param specialytCollge
	 */
	void saveCollegeSpecialty(GjtSpecialtyDegreeCollege specialytCollge);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月14日 上午10:40:19
	 * @param orgId
	 * @return
	 */
	List<GjtSpecialtyDegreeCollege> queryCollegeSpecialtyByOrgId(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月20日 下午4:49:00
	 * @param student
	 * @return
	 */
	List<GjtDegreeCollege> queryByStudentInfo(GjtStudentInfo student);

}
