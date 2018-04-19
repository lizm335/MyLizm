package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.openClass.GjtOnlineLessonFile;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineObject;
import com.gzedu.xlims.service.base.BaseService;

public interface LcmsOnlineLessonService extends BaseService<LcmsOnlineLesson> {

	/**
	 * 查询直播关联的期课程Id
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月23日 上午10:28:21
	 * @param id
	 * @return
	 */
	List<String> findTermCourseIdByOnlinetutorId(String onlinetutorId);

	/**
	 * 添加参与直播的学生
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月23日 下午2:12:59
	 * @param onlinetutorId
	 * @param termcourseId
	 * @param createBy
	 * @return
	 */
	int insertJoinStudent(String onlinetutorId, String termcourseId, String createBy);


	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月23日 下午4:58:05
	 * @param onlinetutorId
	 * @param joinType
	 *            0.未参与 1.已参与 2.已看录播
	 * @param params
	 * @return
	 */
	long countJoinStudent(String onlinetutorId, String joinType, Map<String, Object> params);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月24日 上午10:14:18
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<Map<String, Object>> findLessonStudent(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月24日 上午11:02:56
	 * @param param
	 * @param pageRequest
	 * @return
	 */
	PageImpl<Map<String, Object>> findOnlineLesson(Map param, PageRequest pageRequest);

	/**
	 * 查询与直播关联的年级
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月24日 下午4:48:04
	 * @param onlinetutorId
	 * @return
	 */
	List<GjtGrade> findGjtGradeByOnlinetutorId(String onlinetutorId);

	/**
	 * 查询与直播关联的课程
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月24日 下午4:52:40
	 * @param onlinetutorId
	 * @return
	 */
	List<GjtCourse> findGjtCourseByOnlinetutorId(String onlinetutorId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月27日 下午3:12:30
	 * @param onlinetutorId
	 * @return
	 */
	List<GjtOnlineLessonFile> findLessonFileByOnlinetutorId(String onlinetutorId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月27日 下午4:28:53
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<? extends LcmsOnlineLesson> findOnlineActivityList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月28日 下午5:33:01
	 * @param onlinetutorId
	 * @return
	 */
	List<LcmsOnlineObject> findOnlineObjectByOnlinetutorId(String onlinetutorId);

	/**
	 * 查询活动直播学生
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月29日 下午4:05:25
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<Map<String, Object>> findActivityStudent(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 查询并保存录播地址
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月30日 上午11:11:04
	 * @param onlineLesson
	 * @return
	 */
	boolean saveVideoUrl(LcmsOnlineLesson onlineLesson);


	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月30日 下午4:47:22
	 * @param orgId
	 * @param pyccIds
	 * @param gradeIds
	 * @param specialtyIds
	 * @param courseIds
	 * @return
	 */
	List<String> queryActivityStudentIds(String orgId, List<String> pyccIds, List<String> gradeIds, List<String> specialtyIds,
			List<String> courseIds);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月1日 下午3:55:15
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryActivityStudent(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 根据直播ID查询参与直播的学生id
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月2日 下午7:35:54
	 * @param onlinetutorId
	 * @return
	 */
	List<String> queryActivityStudentIds(String onlinetutorId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月4日 上午10:07:24
	 * @param lesson
	 * @param onlineObjects
	 * @param studentIds
	 * @param files
	 */
	void updateOnlineActivity(LcmsOnlineLesson lesson, List<LcmsOnlineObject> onlineObjects, List<String> studentIds, List<String> files);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月4日 上午10:07:36
	 * @param lesson
	 * @param onlineObjects
	 * @param studentIds
	 * @param files
	 */
	void saveOnlineActivity(LcmsOnlineLesson lesson, List<LcmsOnlineObject> onlineObjects, List<String> studentIds, List<String> files);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月13日 下午4:53:22
	 * @param onlinetutorId
	 * @param userId
	 * @param client
	 */
	void saveViewOnlineLessionRecord(String onlinetutorId, String studentId, int client);

	/**
	 * 保存学生查看录播记录
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月13日 下午5:05:49
	 * @param onlinetutorId
	 * @param studentId
	 */
	void insertGjtLessonViewrecord(String onlinetutorId, String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月12日 下午5:42:42
	 * @param studentId
	 */
	int initStudentOnlineLesson(String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月16日 下午3:49:45
	 * @param searchParams
	 * @return
	 */
	List<String> queryActivityStudent(Map<String, Object> searchParams);
}
