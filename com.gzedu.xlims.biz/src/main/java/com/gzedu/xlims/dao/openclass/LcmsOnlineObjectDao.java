package com.gzedu.xlims.dao.openclass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineObject;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年10月31日
 * @version 3.0
 *
 */
public interface LcmsOnlineObjectDao
		extends BaseDao<LcmsOnlineObject, String>, JpaRepository<LcmsOnlineObject, String>, JpaSpecificationExecutor<LcmsOnlineObject> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月22日 下午2:26:46
	 * @param onlinetutorId
	 * @return
	 */
	@Query("from LcmsOnlineObject where onlinetutorId=?1 and isDeleted='N'")
	List<LcmsOnlineObject> findByOnlinetutorId(String onlinetutorId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月23日 上午10:29:41
	 * @param id
	 * @return
	 */
	@Query("select objectId from LcmsOnlineObject b where onlinetutorId=?1 and isDeleted='N' and objectType='3'")
	List<String> findTermCourseIdByOnlinetutorId(String onlinetutorId);

	@Query("select distinct(c) from LcmsOnlineObject a,GjtTermCourseinfo b,GjtGrade c where a.onlinetutorId=?1 and a.isDeleted='N' and a.objectType='3' and a.objectId=b.termcourseId and b.termId=c.gradeId")
	List<GjtGrade> findGjtGradeByOnlinetutorId(String onlinetutorId);

	@Query("select c from LcmsOnlineObject a,GjtTermCourseinfo b,GjtCourse c where a.onlinetutorId=?1 and a.isDeleted='N' and a.objectType='3' and a.objectId=b.termcourseId and b.courseId=c.courseId")
	List<GjtCourse> findGjtCourseByOnlinetutorId(String onlinetutorId);

	@Query("select objectType  from LcmsOnlineObject where onlinetutorId=?1 and isDeleted='N' group by objectType")
	List<String> findObjectTypeByOnlinetutorId(String onlinetutorId);
}