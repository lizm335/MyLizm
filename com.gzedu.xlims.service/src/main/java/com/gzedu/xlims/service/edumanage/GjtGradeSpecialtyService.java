package com.gzedu.xlims.service.edumanage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.service.base.BaseService;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：年级专业
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月20日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtGradeSpecialtyService extends BaseService<GjtGradeSpecialty> {
	Iterable<GjtGradeSpecialty> save(Iterable<GjtGradeSpecialty> entities);

	public Page<GjtGradeSpecialty> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public GjtGradeSpecialty queryBy(String id);

	public void delete(Iterable<String> ids);

	public void update(GjtGradeSpecialty entity);

	/**
	 * 批量保存年级专业信息
	 * 
	 * @param gradeId
	 * @param realName
	 * @param sradeSpecialties
	 * @param copyGradeId
	 * @throws Exception
	 */
	void saveGradeSpecialty(String gradeId, List<JSONObject> sradeSpecialties, String realName,
			String copyGradeId)
			throws Exception;

	/**
	 * 查询专业相关的班级
	 * 
	 * @param orgId
	 * @return
	 */
	Map<String, String> findSpecialtyGrade(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月21日 下午4:56:09
	 * @param gradeId
	 */
	List<GjtGradeSpecialty> findByGradeId(String gradeId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月26日 下午9:54:09
	 * @param id
	 * @return
	 */
	GjtGradeSpecialty findOne(String id);

	/**
	 * 查询产品
	 * @param gradeId
	 * @param specialtyId
	 * @return
	 */
	GjtGradeSpecialty queryByGradeIdAndSpecialtyId(String gradeId, String specialtyId);
	
	/**
	 * 查询学习中心下的产品
	 * @param gradeId
	 * @param specialtyId
	 * @param studyCenterId
	 * @return
	 */
	GjtGradeSpecialty queryByGradeIdAndSpecialtyIdAndStudyCenterId(String gradeId, String specialtyId, String studyCenterId);
	
	Map<String, String> getSpecialtyMap(String id, String gradeId,String pycc);
	/**
	 * 临时使用
	 * @param newGradeId
	 * @param newSpecialty
	 * @return
	 */
	GjtGradeSpecialty findByGradeIdAndSpecialtyIdAndIsDeletedNew(String newGradeId, String newSpecialty,String xxzxId);

}
