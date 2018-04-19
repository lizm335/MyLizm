
package com.gzedu.xlims.service.edumanage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;

import net.sf.json.JSONObject;

/**
 *
 * 功能说明：教学计划
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
public interface GjtTeachPlanService {

	/**
	 * 根据年级、专业获得教学计划
	 *
	 * @param gradeId
	 *            年级ID
	 * @param specialtyId
	 *            专业ID
	 * @return
	 */
	public List<GjtTeachPlan> queryGjtTeachPlan(String gradeId, String specialtyId);

	/**
	 *
	 * @param xxId
	 *            院校
	 * @param searchParams
	 *            页面查询参数MAP
	 * @param pageRequst
	 *            页面分页对象
	 * @return
	 */
	public Page<GjtTeachPlan> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst);

	public GjtTeachPlan findOne(String teachPlanId);

	public void insert(GjtTeachPlan entity);

	public void update(GjtTeachPlan entity);

	/**
	 * 复制专业计划
	 *
	 * @param grade
	 * @param specialty
	 * @return
	 * @throws Exception
	 */
	boolean createTeachPlan(GjtGradeSpecialty ggs) throws Exception;

	/**
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月15日 下午3:47:33
	 * @param grade
	 *            开设专业的grade
	 * @param copyGradeId
	 *            从某个班级复制
	 * @param gjtSpecialty
	 *            复制教学计划的专业
	 */
	public void createTeachPlan(GjtGradeSpecialty ggs, String specialtyGradeId) throws Exception;

	/**
	 * @param planId
	 */
	public void delete(String planId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月21日 下午7:11:44
	 * @param entity
	 */
	void delete(GjtTeachPlan entity);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月8日 下午4:28:15
	 * @param gradeSpecailtyId
	 * @return
	 */
	public long countGjtTeachPlan(String gradeSpecailtyId);
	
	public List<GjtTeachPlan> findByActualGradeIdAndCourseId(String actualGradeId, String courseId);


	/**
	 * 批量添加教学计划
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月13日 下午4:40:56
	 * @param specialtyId
	 * @param gradeId
	 * @param courseIds
	 * @param term
	 *            学期
	 * @return
	 */
	List<GjtTeachPlan> batchAddPlan(String gradeSpecialtyId, List<String> courseIds, int term);

	GjtTeachPlan findByGradeIdAndKkzyAndCourseId(String gradeId, String kkzy, String courseId);

	void copyTeachPlan(GjtTeachPlan gjtTeachPlan, GjtSpecialtyPlan gjtSpecialtyPlan);

	/**
	 * 更新isdeleted=Y
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月27日 上午10:24:22
	 * @param id
	 */
	void deleteById(String id);

	/**
	 * 获取学期课程ID
	 * @param orgId
	 * @param termId
	 * @param courseId
	 * @return
	 */
	GjtTermCourseinfo queryTermCourseinfoByOrgIdAndTermIdAndCourseId(String orgId, String termId, String courseId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月5日 下午5:51:10
	 * @param gradeSpecialtyId
	 * @return
	 */
	List<GjtTeachPlan> queryGjtTeachPlan(String gradeSpecialtyId);

	/**
	 * 修改专业计划的主教材
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月13日 下午5:50:12
	 * @param planId
	 * @param textbookIds
	 *            主教材的Id
	 */
	public void updateTextbook(String planId, List<String> textbookIds);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月25日 下午7:42:50
	 * @param id
	 * @param plans
	 */
	public void updateTeachPlan(String id, List<JSONObject> plans);
	
	public List<GjtTeachPlan> queryAll(Collection<String> orgIds,Map<String, Object> searchParams);

	/**
	 * 根据产品ID获取教学计划
	 * @param gradeSpecialtyId
	 * @param courseId
	 * @return
	 */
	List<GjtTeachPlan> findView(String gradeSpecialtyId, String courseId);
	/**
	 * 查询学员的所有学期
	 * @param gradeSpecialtyId
	 * @return
	 */
	public List<Map<String, Object>> getGjtTeachPlanInfo(String gradeSpecialtyId);
	
	/**
	 * 导出教学计划
	 * @param formMap
	 * @param request
	 * @param response
	 * @return
	 */
	Workbook expCoursePlan(Map<String, Object> formMap, HttpServletRequest request, HttpServletResponse response);
	/**
	 * 查询学员的专业课程
	 * @param id
	 * @param object
	 * @return
	 */
	public List<GjtTeachPlan> findByGradeSpecialtyIdAndKkxq(String gradeSpecialtyId, int kkxq);
}
