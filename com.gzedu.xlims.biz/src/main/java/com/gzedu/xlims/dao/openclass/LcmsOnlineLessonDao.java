package com.gzedu.xlims.dao.openclass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年10月31日
 * @version 3.0
 *
 */
public interface LcmsOnlineLessonDao
		extends BaseDao<LcmsOnlineLesson, String>, JpaRepository<LcmsOnlineLesson, String>, JpaSpecificationExecutor<LcmsOnlineLesson> {
	@Query(value = "select * from (select b.* from LCMS_ONLINETUTOR_INFO a,LCMS_ONLINE_LESSON b where a.ONLINETUTOR_ID=b.ONLINETUTOR_ID and b.NUMBER_=?1 and a.ISDELETED='N' ) temp where rownum=1", nativeQuery = true)
	LcmsOnlineLesson findOneByNumber(String number);

	@Query(value = "from LcmsOnlineLesson where onlinetutorId=?1")
	LcmsOnlineLesson findOneByOnlinetutorId(String onlinetutorId);

	@Modifying
	@Query(value = "delete lcms_onlinetutor_user t where t.onlinetutor_id=?1", nativeQuery = true)
	void deleteStudentByOnlinetutorId(String onlinetutorId);

	@Modifying
	@Transactional
	@Query(value = "insert into lcms_onlinetutor_user (ONLINETUTOR_ID, USER_ID, IS_INTO, ISDELETED, CREATED_BY, CREATED_DT) values (?1, ?2, ?3, 'N', ?2, SYSDATE)", nativeQuery = true)
	void insertOnlinetutorUser(String onlinetutorId, String userId, String isInto);

	@Modifying
	@Transactional
	@Query(value = "insert into lcms_onlinetutor_user (ONLINETUTOR_ID, USER_ID, CREATED_BY) values (?1, ?2, 'system')", nativeQuery = true)
	void insertOnlinetutorUser(String onlinetutorId, String userId);

	@Modifying
	@Transactional
	@Query(value = "update lcms_onlinetutor_user t set t.is_into='Y',t.into_client=?3, t.updated_dt=sysdate where t.onlinetutor_id=?1 and t.user_id=?2", nativeQuery = true)
	void updateOnlinetutorUser(String onlinetutorId, String userId, int client);

	@Query(value = "select user_id from lcms_onlinetutor_user where onlinetutor_id=?1 and isdeleted='N'", nativeQuery = true)
	List<String> queryActivityStudentIds(String onlinetutorId);

	@Query(value = "select * from lcms_onlinetutor_user t where t.onlinetutor_id=?1 and t.user_id=?2", nativeQuery = true)
	Object queryOnlinetutorUser(String onlinetutorId, String studentId);

	@Modifying
	@Transactional
	@Query(value = "insert into GJT_LESSON_VIEWRECORD (ID,STUDENT_ID,ONLINETUTOR_ID,CREATED_DT) values (SYS_GUID(), ?1, ?2,SYSDATE)", nativeQuery = true)
	void insertGjtLessonViewrecord(String studentId, String onlinetutorId);

	@Query("select onlinetutorId from LcmsOnlineLesson where isDeleted='N' and lessonType=1")
	List<String> findActivityOnlintutorId();
}
