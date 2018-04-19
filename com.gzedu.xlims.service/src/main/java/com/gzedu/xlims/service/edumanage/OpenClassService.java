package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.CanCourseDto;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.pojo.openClass.LcmsOnlinetutorInfo;

public interface OpenClassService {
	
	Page<Map<String, Object>> queryGraduationSpecialtyList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询可选择的课程
	 * @param termId
	 * @param orgId
     * @return
     */
	List<CanCourseDto> queryCanCourseBy(String termId, String orgId);

	/**
	 * 开设课程，生成教学计划
	 * @param termId
	 * @param courseIds
	 * @param orgId
	 * @param operatorId
     * @return
     */
	boolean updateOpenCourse(String termId, String courseIds, String orgId, String operatorId);
	
	/**
	 * 查询期课程列表
	 */
	Page<Map<String, Object>> queryTermCoureList(Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 查询开课课程
	 * @param termId
	 * @param orgId
     * @return
     */
	List<CanCourseDto> getCourseList(String termId, String orgId);
	
	/**
	 * 开设课程
	 * @param termId
	 * @param courseIds
	 * @param orgId
	 * @param operatorId
     * @return
     */
	boolean saveOpenCourse(String termId, String courseIds, GjtUserAccount user);
	
	/**
	 * 是否复制个数
	 * @param searchParams
	 * @return
	 */
	int getCopyCount(Map searchParams);
	
	List getCourseChooseCount(Map searchParams);
	
	/**
	 * 导出数据
	 */
	public Workbook expTermCourse(Map formMap, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 初始化期课程的复制状态
	 * @param searchParams
	 * @return
	 */
	Map updTermCopyFlg(Map searchParams);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月17日 下午6:03:42
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryTermCoureListByTeacher(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月17日 下午8:36:59
	 * @param searchParams
	 * @return
	 */
	long getStatusCount(Map searchParams);

	/**
	 *
	 * @param formMap
	 * @param type
	 *            1 新增 2 修改 3 删除
	 * @return
	 * @throws Exception
	 */
	int updateOnlineLesson(Map formMap, int type) throws Exception;



	/**
	 * 列表直播数据统计项
	 * @param param
	 * @return
	 */
	long getOnlineLessonCount(Map param);


	LcmsOnlinetutorInfo getOnlineLesson(String id);

	List<Map> getTeachPlan(List<String> termCourseIds, PageRequest pageRequest);

	Page<Map> getLessonStu(Map param, PageRequest pageRequest);

	Long[] getLessonStuCount(String number);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月26日 下午7:01:48
	 * @param orgId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	List<GjtTermCourseinfo> getByCourseCodeAndOrg(String code, String orgId) throws Exception;

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月2日 下午3:00:16
	 * @param number
	 * @return
	 */
	List<String> findTermCourseIdByNumber(String number);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月9日 上午11:56:02
	 * @param id
	 * @return
	 */
	LcmsOnlineLesson findOnlineLessonById(String id);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月9日 上午11:56:09
	 * @param id
	 * @return
	 */
	LcmsOnlinetutorInfo findOnlinetutorInfoById(String id);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月13日 上午10:58:18
	 * @param onlinetutorId
	 * @return
	 */
	LcmsOnlineLesson findOnlineLessonByTutorId(String onlinetutorId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月14日 上午11:21:16
	 * @param onlineLesson
	 */
	void saveLcmsOnlineLesson(LcmsOnlineLesson onlineLesson);
}
