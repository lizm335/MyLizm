package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;

public interface GjtExamSubjectNewService {
	
	String getSubjectCodeByName(String name);

	public GjtExamSubjectNew insert(GjtExamSubjectNew entity);

	public Page<GjtExamSubjectNew> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public int delete(List<String> ids, String xxid);

	public GjtExamSubjectNew queryBy(String id);

	public GjtExamSubjectNew update(GjtExamSubjectNew entity);

	public String getSubjectCode(int type);

	public List<GjtCourse> noSubjectCourseList(String xxid);

	public HSSFWorkbook exportNoSubjectCourseList(List<GjtCourse> list);

	public Workbook importGjtExamSubjectNew(String path, int examType, GjtUserAccount user);

	public int insertBatch(List<GjtExamSubjectNew> list);

	public Map<String, String> plansCountBySubject(List<String> subjectids);

	public List<GjtCourse> findCourseByXxid(String xxid, String ksfs);

	public List<Map<String, String>> findTeachPlanByXxid(String xxid, String ksfs);

	public List<Map<String, String>> noSubjectPlanList(String xxid, String ksfs);

	public HSSFWorkbook exportNoSubjectPlanList(List<Map<String, String>> list);

	public GjtExamSubjectNew isExamSubjectExist(GjtExamSubjectNew entity);
	
	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, int isDeleted);
//	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, String isDeleted);
	
	public Map<String, String> queryTeachPlanBySubject(List<String> ids);
	
	/**
	 * 导出未创建考试科目的课程
	 * @return
	 */
	public String expCourseSubject(Map formMap, HttpServletRequest request, HttpServletResponse response);
	
	
	
	/**
	 * 导入考试科目
	 * @return
	 */
	public Map importCourseSubject(String filePaths, Map formMap, HttpServletRequest request, HttpServletResponse response);
	
}
