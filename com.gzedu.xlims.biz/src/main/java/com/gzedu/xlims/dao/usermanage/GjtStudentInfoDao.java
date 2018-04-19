/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.usermanage;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 *
 * 功能说明：学员管理
 *
 * @author 张伟文 zhangweiwen@eenet.com
 * @CreateDate 2016年4月26日
 * @EditDate 2017年02月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudentInfoDao extends BaseDao<GjtStudentInfo, String> {

	@Query(value = "select decode(t.audit_state,null,' ','1','1','0','0','3') audit_state,count(t.student_id) student_num from gjt_signup t " +
			" inner join gjt_student_info b on t.student_id=b.student_id" +
			" where t.is_deleted='N' and b.is_deleted='N' and b.xx_id=:xxId" +
			" group by decode(t.audit_state,null,' ','1','1'，'0','0','3')", nativeQuery = true)
	List<Object[]> countGroupbyAuditState(@Param("xxId") String xxId);

	@Query(value = "select decode(b.perfect_status,null,' ',1,'1','0') perfect_status,count(t.student_id) student_num from gjt_signup t " +
			" inner join gjt_student_info b on t.student_id=b.student_id" +
			" where t.is_deleted='N' and b.is_deleted='N' and b.xx_id=:xxId" +
			" group by decode(b.perfect_status,null,' ',1,'1','0')", nativeQuery = true)
	List<Object[]> countGroupbyPerfectStatus(@Param("xxId") String xxId);

	@Query(value = "select decode(b.xjzt,null,' ',b.xjzt) xjzt,count(t.student_id) student_num from gjt_signup t " +
			" inner join gjt_student_info b on t.student_id=b.student_id" +
			" where t.is_deleted='N' and b.is_deleted='N' and b.xx_id=:xxId" +
			" and t.audit_state='1'" +
			" group by b.xjzt", nativeQuery = true)
	List<Object[]> countGroupbyXjzt(@Param("xxId") String xxId);

	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudentInfo g set g.isDeleted=?2 where g.studentId=?1 ")
	int deleteById(String id, String str);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudentInfo g set g.isEnabled=?2 where g.studentId=?1 ")
	int updateIsEnabled(String id, String str);

	/**
	 * 根据xh获取学员信息
	 * @param xh
	 * @return
	 */
	@Query("select t from GjtStudentInfo t where t.isDeleted='N' and t.xh=:xh and t.xjzt!='5'")
	GjtStudentInfo findByXhAndIsDeleted(@Param("xh") String xh);

	/**
	 * 根据xh获取学员信息
	 * @param xh
	 * @return
	 */
	@Query("select t.studentId from GjtStudentInfo t where t.isDeleted='N' and t.xh=:xh and t.xjzt!='5'")
	String findStudentIdByXhAndIsDeleted(@Param("xh") String xh);

	@Query("select t from GjtStudentInfo t where t.isDeleted='N' and (t.xh=:xh or t.sfzh=:xh) and t.xjzt!='5' order by t.createdDt desc")
	List<GjtStudentInfo> findByXhOrSfzhAndIsDeletedOrderByCreatedDtDesc(@Param("xh") String sfzh);

	@Query("select t from GjtStudentInfo t where t.isDeleted='N' and (t.xh=:xh or t.sfzh=:xh or t.sjh=:xh) and t.xjzt!='5' order by t.createdDt desc")
	List<GjtStudentInfo> findByXhOrSfzhOrSjhAndIsDeletedOrderByCreatedDtDesc(@Param("xh") String sfzh);

	/**
	 * 根据微信OpenId获取学员信息
	 * @param wxOpenId
	 * @return
	 */
	@Query(value = "select t from GjtStudentInfo t where t.isDeleted='N' and t.gjtUserAccount.wxOpenId = ?1 and t.xjzt!='5' order by t.createdDt desc")
	List<GjtStudentInfo> queryByWxOpenId(String wxOpenId);

	@Modifying
	@Transactional
	@Query("update GjtSignup g set g.gjtStudentInfo.nj=?2 where g.gjtStudentInfo.studentId=?1 ")
	int updateStudentGrade(String studentId, String str);

	/**
	 * 录入学籍
	 * @param studentId
	 * @param updatedBy
	 * @param updatedDt
	 */
	@Modifying
	@Query("UPDATE GjtStudentInfo t SET t.xjzt='2',t.version=t.version+1,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.isDeleted='N' AND t.xjzt='3' AND t.studentId=:studentId")
	@Transactional
	int enteringSignup(@Param("studentId") String studentId, @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

	/**
	 * 根据xh、xm、sfzh、pycc获取学员信息
	 * @param xh
	 * @param xm
	 * @param sfzh
	 * @param pycc
	 * @return
	 */
	@Query(value = "SELECT T FROM GjtStudentInfo T WHERE T.isDeleted='N' AND T.xjzt!='5' AND T.xh=?1 AND T.xm=?2 AND T.sfzh=?3 AND T.pycc=?4")
	GjtStudentInfo findByXhAndXmAndSfzhAndPycc(String xh, String xm, String sfzh, String pycc);

	/**
	 * 根据sfzh、pycc获取学员信息
	 * @param sfzh
	 * @param pycc
	 * @param isDeleted
	 * @return
	 */
	GjtStudentInfo findBySfzhAndPyccAndIsDeleted(String sfzh, String pycc, String isDeleted);

	/**
	 * 根据学号姓名查找
	 * @param xh
	 * @param xm
	 * @param isDeleted
     * @return
     */
	GjtStudentInfo findByXhAndXmAndIsDeleted(String xh, String xm, String isDeleted);

	/**修改学生信息*/
	@Modifying
	@Transactional
	@Query(" UPDATE GjtStudentInfo SET updatedDt = sysdate ,userType =:userType , xjzt = :studentNumberStatus WHERE isDeleted='N' AND xh =:xh AND xm=:xm AND sfzh=:sfzh AND pycc=:pycc")
	int updateStuInfo(@Param("userType")String userType,@Param("studentNumberStatus") String studentNumberStatus,@Param("xh")String xh,@Param("xm") String xm,@Param("sfzh")String sfzh,@Param("pycc") String pycc);


	/**修改学生信息*/
	@Modifying
	@Transactional
	@Query(" UPDATE GjtStudentInfo SET updatedDt = sysdate ,userType =:userType  WHERE isDeleted='N' AND xh =:xh AND xm=:xm AND sfzh=:sfzh AND pycc=:pycc")
	int updateStuInfoUserType(@Param("userType")String userType,@Param("xh")String xh,@Param("xm") String xm,@Param("sfzh")String sfzh,@Param("pycc") String pycc);

	/**修改学生信息*/
	@Modifying
	@Transactional
	@Query(" UPDATE GjtStudentInfo SET updatedDt = sysdate ,xjzt =:studentNumberStatus  WHERE isDeleted='N' AND xh =:xh AND xm=:xm AND sfzh=:sfzh AND pycc=:pycc")
	int updateStuInfoStudentNumberStatus(@Param("studentNumberStatus")String studentNumberStatus,@Param("xh")String xh,@Param("xm") String xm,@Param("sfzh")String sfzh,@Param("pycc") String pycc);

	/**修改学生信息*/
	@Modifying
	@Transactional
	@Query(" UPDATE GjtStudentInfo SET updatedDt = sysdate ,xjzt =:studentNumberStatus  WHERE isDeleted='N' AND studentId =:studentId ")
	int updateStuInfoStudentNumberStatus(@Param("studentNumberStatus")String studentNumberStatus,@Param("studentId")String studentId);

	/**
	 * 学员学籍资料是否完善
	 * @param studentId
	 * @return
	 */
	@Query(value = "SELECT T.PERFECT_STATUS FROM GJT_STUDENT_INFO T WHERE T.IS_DELETED='N' AND T.STUDENT_ID=:studentId", nativeQuery = true)
	int isPerfect(@Param("studentId") String studentId);

	/**
	 * 学员学籍资料完善了学籍资料
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GjtStudentInfo T SET T.perfectStatus=DECODE(T.perfectStatus,0,2,T.perfectStatus) WHERE T.isDeleted='N' AND T.studentId=:studentId")
	int perfectSignup(@Param("studentId") String studentId);

	/**
	 * 学员学籍资料完善了通讯信息
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GjtStudentInfo T SET T.perfectStatus=DECODE(T.perfectStatus,2,3,T.perfectStatus) WHERE T.isDeleted='N' AND T.studentId=:studentId")
	int perfectCommunication(@Param("studentId") String studentId);

	/**
	 * 学员学籍资料完善了报读信息
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GjtStudentInfo T SET T.perfectStatus=DECODE(T.perfectStatus,3,4,T.perfectStatus) WHERE T.isDeleted='N' AND T.studentId=:studentId")
	int perfectRead(@Param("studentId") String studentId);

	/**
	 * 学员学籍资料完善了原最高学历
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GjtStudentInfo T SET T.perfectStatus=DECODE(T.perfectStatus,4,5,T.perfectStatus) WHERE T.isDeleted='N' AND T.studentId=:studentId")
	int perfectEducation(@Param("studentId") String studentId);

	/**
	 * 学员学籍资料完善了证件资料
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GjtStudentInfo T SET T.perfectStatus=DECODE(T.perfectStatus,5,6,T.perfectStatus) WHERE T.isDeleted='N' AND T.studentId=:studentId")
	int perfectCertificate(@Param("studentId") String studentId);

	/**
	 * 学员学籍资料确认签名
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GjtStudentInfo T SET T.perfectStatus=DECODE(T.perfectStatus,6,1,T.perfectStatus) WHERE T.isDeleted='N' AND T.studentId=:studentId")
	int perfectSign(@Param("studentId") String studentId);

	/**
	 * 更新学籍资料是否缓存到下载列表 1-已缓存 0-未缓存
	 * @param studentId
	 * @param rollCacheStatus
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudentInfo g set g.rollCacheStatus=?2 where g.studentId=?1 ")
	int updateRollSyncStatus(String studentId, Integer rollCacheStatus);

	GjtStudentInfo queryByAtid(String atid);

	@Modifying
	@Transactional
	@Query(value = " UPDATE GJT_SIGNUP_DATA SET URL_NEW = :url  WHERE ID = :id", nativeQuery = true)
	int upPic(@Param("url") String url,@Param("id")String id);

	/**
	 * 根据账号Id获取学员信息
	 * @param accountId
	 * @return
	 */
	@Query(value = "select s from GjtStudentInfo s where s.isDeleted='N' and s.gjtUserAccount.id = ?1")
	GjtStudentInfo queryByAccountId(String accountId);

	/**
	 * 入学通知书确认
	 * @param studentId
     */
	@Modifying
	@Transactional
	@Query(value = "UPDATE GJT_STUDENT_INFO SET STATUS='1' WHERE STUDENT_ID=:studentId AND (STATUS IS NULL OR STATUS='0')", nativeQuery = true)
	int confirm(@Param("studentId") String studentId);
	
	List<GjtStudentInfo> findByXxIdAndXjztAndIsDeleted(String xxId, String xjzt, String isDeleted);
	
	/**
	 * 获取某时间之后的学员ID
	 * @param data
	 * @return
	 */
	@Query(value="select t.studentId from GjtStudentInfo t inner join t.gjtSignup b where t.isDeleted='N' and t.createdDt>=?1 and b.auditState<>'0' and b.auditState<>'1' and not exists (select x from GjtFlowRecord x where x.studentId=t.studentId)")
	List<String> findSidByCreatedDtGreaterThanEqual(Date data);

	@Query(value="select * from gjt_student_info where xx_id=?1 and nj=?2 and major=?3 and xxzx_id<>'a75ac723e412465a9d73799a10e1b255' and xxzx_id<>'4a204875872347189478fd59be2d886c' and is_deleted = 'N' ",nativeQuery=true)
	List<GjtStudentInfo> findStudentInfo(String xxId, String gradeId, String specialtyId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月1日 下午5:59:05
	 * @param sfzh
	 * @return
	 */
	@Query("select t from GjtStudentInfo t where t.isDeleted='N' and t.sfzh=?1 and t.xjzt!='5' order by t.createdDt desc")
	List<GjtStudentInfo> queryBySfzh(String sfzh);

	List<GjtStudentInfo> findByXxIdAndIsDeleted(String xxId, String booleanNo);

	@Modifying
	@Transactional
	@Query("update GjtStudentInfo t set t.graduatedAvatar=:graduatedAvatar where t.studentId=:studentId ")
	public int updatePhoto(@Param("studentId") String studentId, @Param("graduatedAvatar") String graduatedAvatar);
}

