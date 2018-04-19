package com.gzedu.xlims.service.exam;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.service.base.BaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface GjtExamPlanNewService extends BaseService<GjtExamPlanNew> {

	public Page<GjtExamPlanNew> queryAll(Map<String, Object> searchParams,
			PageRequest pageRequst);

	public void delete(List<String> ids);

	public GjtExamPlanNew queryBy(String id);

	public GjtExamPlanNew update(GjtExamPlanNew entity);

	/**
	 * 添加通用专业
	 * @param examPlanId
	 * @return
     */
	boolean insertExamPlanToTongyongSpecialty(String examPlanId);

	public Map<String, Object> dataFilter(List<GjtExamPlanNew> list);

	public int insertBatch(List<GjtExamPlanNew> list);

	public int updateBatch(List<GjtExamPlanNew> list);

	public int planValid(GjtExamPlanNew entity);

	public String examPlanIdGenerator(String subjectCode, String batchCode);

	public Map<String, Object> autoCreatePlans(GjtExamBatchNew batch);

	public List<GjtExamPlanNew> findByExamBatchCode(String batchCode);

	public Map exportByList(Map searchParams, PageRequest pageRequst, HttpServletResponse response);

	public HSSFWorkbook exportByList(List<GjtExamPlanNew> list);
	
	
	public HSSFWorkbook importPlansSetting(String path, GjtUserAccount user);

	public GjtExamPlanNew isPlanExist(String examPlanid);
	
	/**
	 * 查询考试计划列表页
	 */
	public Page getExamPlanList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 查询考试计划列表页(统计项)
	 */
	public int getExamPlanCount(Map searchParams);
	
	/**
	 * 导出未设置考试时间的考试科目表
	 * @return
	 */
	public String expExamPlanTime(Map formMap, HttpServletRequest request, HttpServletResponse response, PageRequest pageRequst);
	
	/**
	 * 导入考试计划
	 * @param filePaths
	 * @param formMap
	 * @param request
	 * @param response
	 * @return
	 */
	public Map importExamPlan(String filePaths, Map formMap, HttpServletRequest request, HttpServletResponse response);
	
	public String getSubjectCode(int type);
	
	public Page<Map> queryExamPlan(Map formMap,PageRequest pageRequst);
	
	public List queryByCourseIdAndBatchCodeAndExamType(String courseId ,String batchCode,String examTYpe);
	
	
	public String importUpdatePlans(String path, GjtUserAccount user,String savePath);
	
	HSSFWorkbook importBlankPlans(String path, GjtUserAccount user, String pcId);
	
	int insertBatchSubject(List<GjtExamSubjectNew> list);
	
	String getSubjectCodeByName(String name);
	
	int getExamTypeByName(String name);
	
	GjtExamPlanNew findByExamBatchCodeAndExamNoAndType(String examBatchCode, String examNo, int type);

	/**
	 * 根据课程号获取唯一的开考科目 改为 findByExamBatchCodeAndExamNoAndType
	 * @param examBatchCode
	 * @param examNo
	 * @param type
	 * @param courseId
     * @return
     */
	@Deprecated
	GjtExamPlanNew findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseId(String examBatchCode, String examNo, int type, String courseId);

	/**
	 * 保存开考科目与专业对应信息
	 * @param params
	 * @return
	 */
	public int insertExamPlanSpecialty(Map<String, Object> params);
	/**
	 * 企业大学接口--查询考试管理列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getExamManagmentList(Map<String, Object> searchParams, PageRequest pageRequst);
	/**
	 * 企业大学接口--查看排考记录
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> getArrangeExamRecordList(Map<String, Object> searchParams, PageRequest pageRequst);
	/**
	 * 企业大学接口--查看学员的准考证信息
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getStudentAdmissionTicket(Map<String, Object> searchParams);

	/**
	 * 企业大学接口--考试计划接口
	 * @param searchParams
	 * @return
	 */
	Map getExamBatchList(Map<String,Object> searchParams);

	/**
	 * 考试科目是否有被修改过
	 * @param examBatchCode
	 * @return
	 */
	boolean isUpdate(String examBatchCode);

	/**
	 * 自动生成开考科目
	 * @param xxId
	 * @param examBatchCode
	 * @return
	 */
	boolean initAutoExamPlan(String xxId, String examBatchCode);
}
