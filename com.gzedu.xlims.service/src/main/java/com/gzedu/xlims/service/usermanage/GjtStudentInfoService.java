/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.usermanage;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.ViewStudentInfo;
import com.gzedu.xlims.pojo.dto.StatisticsDto;
import com.gzedu.xlims.pojo.dto.StudentCourseDto;
import com.gzedu.xlims.pojo.dto.StudentSignupInfoDto;
import com.gzedu.xlims.pojo.dto.StudentSynthesizeInfoDto;
import com.gzedu.xlims.pojo.export.StudentExport;

/**
 * 功能说明：学员管理接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudentInfoService {

	List<GjtStudentInfo> queryBy(Map<String, Object> searchParams, Sort sort);

	GjtStudentInfo queryById(String id);

	boolean delete(String id);

	String outStudentSignupInfo(Map<String, Object> searchParams, String path);

	/**
	 * 导出学员报名表
	 * 
	 * @param outputTmpPath
	 *            导出路径
	 * @param id
	 *            学员id
	 */
	String outStuWord(String outputTmpPath, String id);

	/**
	 * 获取学员的报读信息及相关信息
	 * 
	 * @param studentId
	 * @return
	 */
	StudentSignupInfoDto getStudentSignupInfoById(String studentId);

	/**
	 * 分页条件查询学员的学籍资料
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentSignupInfoDto> queryStudentSignupInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 统计院校的学员资料审核状态人数
	 * 
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyAuditState(Map<String, Object> searchParams);

	/**
	 * 统计院校的学员资料完善状态人数
	 * 
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyPerfectStatus(Map<String, Object> searchParams);

	/**
	 * 统计院校的学员学籍状态人数
	 * 
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyXjzt(Map<String, Object> searchParams);

	Page<GjtStudentInfo> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	// 添加学员
	Boolean saveEntity(GjtStudentInfo gjtStudentInfo);

	/**
	 * 根据账号Id获取学员信息
	 * 
	 * @param accountId
	 * @return
	 */
	GjtStudentInfo queryByAccountId(String accountId);

	/**
	 * 根据学号获取学员信息
	 * 
	 * @param xh
	 * @return
	 */
	GjtStudentInfo queryByXh(String xh);

	/**
	 * 根据学号获取学员ID
	 * 
	 * @param xh
	 * @return
	 */
	String queryStudentIdByXh(String xh);

	int updateStudentGrade(String id, String ebid);

	/**
	 * 修改学员信息
	 * 
	 * @param entity
	 * @return
	 */
	boolean updateEntity(GjtStudentInfo entity);

	/**
	 * 修改学员信息，并且刷新学籍资料缓存
	 * 
	 * @param entity
	 * @return
	 */
	boolean updateEntityAndFlushCache(GjtStudentInfo entity);

	HSSFWorkbook outInfoByExcel(List<StudentExport> list);

	/**
	 * 删除（假）
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteById(String[] ids);

	// 是否启用停用
	Boolean updateIsEnabled(String id, String str);

	Page<GjtStudentInfo> queryNoBrvbar(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 根据课程和学年度查询学员
	 * 
	 * @param courseId
	 * @param studyYearId
	 * @return
	 */
	Page<GjtStudentInfo> queryByCourseIdAndStudyYearId(String courseId, int studyYearId, boolean exists,
			Map<String, Object> searchParams, Pageable pageable);

	/**
	 * 根据条件统计专业学员数
	 * 
	 * @param searchParams
	 * @return
	 */
	List<StatisticsDto<Integer>> querySpecialtyRecruitstatisticsBy(Map<String, Object> searchParams);

	/**
	 * 根据条件统计学校的专业学员数
	 * 
	 * @param xxId
	 * @param searchParams
	 * @return
	 */
	List<StatisticsDto<Integer>> querySpecialtyRecruitstatisticsBy(String xxId, Map<String, Object> searchParams);

	/**
	 * @param xhs
	 * @return
	 */
	Map<String, ViewStudentInfo> getViewStudentInfos(Set<String> xhs);

	/**
	 * 导出未注册学员学籍资料信息<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置文件流<br>
	 * 3.写入数据<br>
	 * 4.压缩打包<br>
	 *
	 * @param searchParams
	 * @param sort
	 * @param outputTmpPath
	 * @return 文件输出路径
	 */
	String exportNotRegInfoToPath(Map<String, Object> searchParams, Sort sort, String outputTmpPath);

	/**
	 * 录入学籍
	 * 
	 * @param studentId
	 * @param updatedBy
	 * @return
	 */
	boolean enteringSignup(String studentId, String updatedBy);

	/**
	 * 获取学员的年级信息
	 * 
	 * @param studentId
	 * @return
	 */
	GjtGrade getGjtGradeByStudentId(String studentId);

	Map importStuInfo(File targetFile, String path);

	/**
	 * 校验学员学籍资料完善程度
	 * 
	 * @param studentId
	 */
	boolean perfectSignupAndCertificateInfo(String studentId);

	/**
	 * 判断是否为本科
	 * 
	 * @param pycc
	 * @return
	 */
	boolean isUndergraduateCourse(String pycc);

	/**
	 * 判断学员是否是本科层次
	 * 
	 * @param studentId
	 * @return
	 */
	boolean isUndergraduateCourseById(String studentId);

	GjtStudentInfo queryByAtid(String atid);

	/**
	 * 学员的学籍资料每个流程审核状态
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<Map> queryStudentSignupInfoEveryAuditState(Map<String, Object> searchParams, Sort sort);

	/**
	 * 导出学员的学籍资料每个流程审核状态<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	String exportAuditState(Map<String, Object> searchParams, Sort sort, String path);

	Map upPic(Map formMap);

	/**
	 * 学员学籍资料完善了证件资料
	 * 
	 * @param studentId
	 * @return
	 */
	Map<String, Object> getAuditInfo(String studentId);

	/**
	 * 获取学员缴费逾期状态
	 * 
	 * @param studentId
	 * @return
	 */
	Map<String, Object> getPayOverdueStatus(String studentId);

	/**
	 * 重交资料
	 * 
	 * @param studentId
	 * @return
	 */
	boolean againSignupInfo(String studentId);

	/**
	 * 入学通知书确认
	 * 
	 * @param studentId
	 */
	boolean confirm(String studentId);

	/**
	 * 同步微信信息
	 * 
	 * @param sfzh
	 * @param wxOpenId
	 * @param wxInfos
	 * @param updatedBy
	 * @return
	 */
	boolean updateWxInfo(String sfzh, String wxOpenId, Map<String, Object> wxInfos, String updatedBy);

	/**
	 * 单点登录获取最新的学员信息
	 * 
	 * @param sfzh
	 *            学号或身份证号
	 * @return
	 */
	GjtStudentInfo querySSOByXhOrSfzh(String sfzh);

	/**
	 * 单点登录获取最新的学员信息
	 * 
	 * @param sfzh
	 *            学号或身份证号或手机号
	 * @return
	 */
	GjtStudentInfo querySSOByXhOrSfzhOrSjh(String sfzh);

	/**
	 * 根据微信OpenId获取学员信息
	 * 
	 * @param wxOpenId
	 * @return
	 */
	GjtStudentInfo queryByWxOpenId(String wxOpenId);

	/**
	 * 分页条件查询学员的资料
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentSignupInfoDto> queryStudentInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 院校模式-导入学员信息
	 * 
	 * @param targetFile
	 * @param path
	 * @param orgId
	 * @param operatorId
	 * @return
	 */
	Map importCollegeStudentInfo(File targetFile, String path, String orgId, String operatorId);

	/**
	 * 学员选课信息
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentCourseDto> queryStudentCourseByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 院校模式-导入学员选课
	 * 
	 * @param targetFile
	 * @param path
	 * @param orgId
	 * @param operatorId
	 * @return
	 */
	Map importCollegeStudentTakeCourse(File targetFile, String path, String orgId, String operatorId);

	/**
	 * 统计学员学籍信息 - 学籍状态(0:注册中,10:转学,2:在籍,3:待注册,4:休学,5:退学,8:毕业)
	 * 
	 * @param orgId
	 * @return
	 */
	List<Map<String, String>> countStudentInfoByOrgId(Collection<String> orgId);

	/**
	 * 获取某时间之后的学员ID
	 * 
	 * @param data
	 * @return
	 */
	List<String> findSidByCreatedDtGreaterThanEqual(Date data);

	/**
	 * 查询学员所在的教务班级
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryStudentTeachClassInfo(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	List<Map<String, Object>> exportStudentTeachClassInfo(String orgId, Map<String, Object> searchParams);

	/**
	 * 根据atid或userId查询studentId
	 * 
	 * @param searchParams
	 * @return
	 */
	List<Map> queryStudentIdsBy(Map<String, Object> searchParams);

	/**
	 * 
	 * @param xh
	 * @param xm
	 * @param xxId
	 * @return
	 */
	public Page<Map> findStudent(String xhOrxm, String xxId, PageRequest pageRequst);

	/**
	 * 统计身份证识别有误数据
	 * 
	 * @param searchParams
	 * @param path
	 * @return
	 */
	public String exportNotValidStudentName(Map<String, Object> searchParams, String path);

	/**
	 * 根据学期、专业查询学员信息
	 * 
	 * @param xxId
	 * @param gradeId
	 * @param specialtyId
	 * @return
	 */
	public List<GjtStudentInfo> findStudent(String xxId, String gradeId, String specialtyId);

	/**
	 * 查询活动组织要发送的学员
	 * 
	 * @param map
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryPutStudent(Map<String, Object> map, PageRequest pageRequst);

	public List<Map<String, Object>> queryPutStudent(Map<String, Object> map);

	/**
	 * 查询活动组织要发送的统计
	 * 
	 * @param map
	 * @return
	 */
	public long queryPutStudentCount(Map<String, Object> map);

	/**
	 * 活动组织导入学员，校验学员是否正确,返回userId
	 */

	public String checkImportStudent(String xm, String xh, String specialtyName);

	/**
	 * 查询在线学员
	 * 
	 */
	public List<Map<String, Object>> findOline(String orgId, Map<String, Object> searchParams);

	/**
	 * 查询Excel导入的学员和选择的在线学员
	 */
	public Page<Map<String, Object>> findImportStudentPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 学员综合信息，分页
	 *
	 * @return
	 */
	Page<StudentSynthesizeInfoDto> queryStudentSynthesizeListByPage(Map searchParams, PageRequest pageRequst);

	/**
	 * 学员综合信息，导出
	 *
	 * @param searchParams
	 * @return
	 */
	List<Map<String, String>> downLoadStudentSynthesizeListExportXls(Map searchParams);

	/**
	 * 同步广州国开学员至运营部门(临时使用)
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月1日 下午6:00:43
	 * @param sfzh
	 * @return
	 */
	GjtStudentInfo queryBySfzh(String sfzh);

	List<GjtStudentInfo> findByXxIdAndIsDeleted(String string, String booleanNo);

	List<Map<String, Object>> queryStudentInfo();

	/**
	 * 根据身份证号批量获取studentId
	 * 
	 * @param cardNos
	 * @return
	 */
	List<Map<String, String>> queryBySfzh(List<String> cardNos);

    void updatePhoto(String studentId, String photoUrl);
}
