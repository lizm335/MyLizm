package com.gzedu.xlims.dao.openclass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.openClass.LcmsOnlinetutorInfo;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年10月31日
 * @version 3.0
 *
 */
public interface LcmsOnlinetutorInfoDao extends JpaRepository<LcmsOnlinetutorInfo, String>, JpaSpecificationExecutor<LcmsOnlinetutorInfo> {
	@Query("select a.termcourseId from LcmsOnlinetutorInfo a,LcmsOnlineLesson b where a.onlinetutorId=b.onlinetutorId and a.isdeleted='N' and b.number=?1 ")
	List<String> findTermCourseIdByNumber(String number);

	@Query(value = "select sum(a.PARTICIPATE_NUM) ALL_NUM,sum(PARTICIPATE_REAL_NUM) JOIN_NUM from LCMS_ONLINETUTOR_INFO a,LCMS_ONLINE_LESSON b where a.ONLINETUTOR_ID=b.ONLINETUTOR_ID and b.NUMBER_=?1 and a.ISDELETED='N' ", nativeQuery = true)
	Object[] countStudentByNumber(String number);

	@Query(value = "select * from (select a.* from LCMS_ONLINETUTOR_INFO a,LCMS_ONLINE_LESSON b where a.ONLINETUTOR_ID=b.ONLINETUTOR_ID and b.NUMBER_=?1 and a.ISDELETED='N' ) temp where rownum=1", nativeQuery = true)
	LcmsOnlinetutorInfo findOneByNumber(String number);

	@Query(value = "select a.* from LCMS_ONLINETUTOR_INFO a,LCMS_ONLINE_LESSON b where a.ONLINETUTOR_ID=b.ONLINETUTOR_ID and b.NUMBER_=?1 and a.ISDELETED='N' ", nativeQuery = true)
	List<LcmsOnlinetutorInfo> findByNumber(String number);



}
