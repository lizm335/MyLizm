package com.gzedu.xlims.service.graduation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.gzedu.xlims.pojo.graduation.GjtGraduationRegister;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegisterEdu;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 毕业登记业务逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月15日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtGraduationRegisterService extends BaseService<GjtGraduationRegister> {

	/**
	 * 查询毕业生登记表，带分页
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtGraduationRegister> queryGraduationRegisterByPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 统计毕业生登记表
	 * 
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	long count(String orgId, Map<String, Object> searchParams);

	/**
	 * 获取学员的毕业登记信息
	 * 
	 * @param graduationPlanId
	 * @param studentId
	 * @return
	 */
	GjtGraduationRegister queryByStudentId(String graduationPlanId, String studentId);

	/**
	 * 获取学员的学历信息
	 * 
	 * @param studentId
	 * @return
	 */
	List<GjtGraduationRegisterEdu> queryEduByStudentId(String studentId);

	void deleteGraduationRegisterEdu(String id);

	boolean saveGraduationRegisterEdu(GjtGraduationRegisterEdu entity);

	GjtGraduationRegisterEdu findGraduationRegisterEdu(String eduId);

	/**
	 * 批量下载毕业生登记表
	 * 
	 * @param searchParams
	 * @param object
	 * @param realPath
	 * @return
	 */
	String batchDownloadRegister(Map<String, Object> searchParams, Sort sort, String realPath);

	/**
	 * 修改注册登记表
	 * 
	 * @param info
	 * @return
	 */
	boolean update(GjtGraduationRegister info);

	/**
	 * 初始化入学学期时间之前的所有学员的毕业生登记数据
	 * 
	 * @param xxId
	 * @param graduationPlanId
	 * @param enterSchoolDate
	 *            入学学期时间
	 */
	boolean initCurrentTermGraduationStudent(String xxId, String graduationPlanId, Date enterSchoolDate);

	/**
	 * 根据用户Id与计划Id查找毕业省登记表
	 * 
	 * @return
	 */
	GjtGraduationRegister queryOneByStudentIdAndPlanId(String studentId, String planId);

	/**
	 * 根据studentId修改毕业照
	 * 
	 * @param entity
	 */
	public int updatePhoto(String studentId, String photo);
}
