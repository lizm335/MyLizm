package com.gzedu.xlims.service.feedback;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualSubject;

public interface LcmsMutualSubjectService {

	public Page<LcmsMutualSubject> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public Page<LcmsMutualSubject> queryAllPageInfo(String userId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public List<LcmsMutualSubject> queryAllInfo(String title, Map<String, Object> searchParams);

	public Map<String, Object> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public LcmsMutualSubject save(LcmsMutualSubject item);

	public LcmsMutualSubject queryById(String id);

	public boolean update(LcmsMutualSubject item);

	/**
	 * 把问题转发给其他人员，并且发短信告知
	 * 
	 * @param id
	 * @param classId
	 * @param teachId
	 * @param zhuanfaId
	 * @param roleId
	 * @return
	 */
	public LcmsMutualSubject forwardOthers(String id, String classId, String teachId, String zhuanfaId, String roleId);

	public boolean delete(String id);

	/**
	 * 更改常见问题状态
	 * 
	 * @param id
	 * @param state
	 * @param oftenType
	 * @return
	 */
	public boolean updateIsComm(String id, String state, String oftenType, String isCommendType);

	/**
	 * 问题转回初始的地方，并且发短信通知对方
	 * 
	 * @param id
	 * @param chushiId
	 * @param studentName
	 * @param teacherName
	 * @param content
	 * @return
	 */
	public boolean updateForward(String id, String chushiId, String studentName, String teacherName, String content);

	/**
	 * 学员新增答疑
	 * 
	 * @param source
	 * @param classInfo
	 * @param user
	 * @param imgUrls
	 * @param content
	 * @param title
	 * @return
	 */
	public LcmsMutualSubject save(String source, GjtClassInfo classInfo, GjtUserAccount user, String[] imgUrls,
			String content, String title);

	/**
	 * 老师新增答疑
	 * 
	 * @param source
	 * @param user
	 * @param imgUrls
	 * @param content
	 * @param title
	 * @param adminUserId
	 * @return
	 */
	public LcmsMutualSubject teachSave(String source, GjtUserAccount user, String[] imgUrls, String content,
			String title, String adminUserId);

	/**
	 * 新增常见问题（提问端），自问自答形式
	 * 
	 * @param oftenType
	 * @param imgUrls1
	 * @param user
	 * @param content1
	 * @param title
	 * @return
	 */
	public LcmsMutualSubject saveOften(String oftenType, String[] imgUrls1, GjtUserAccount user, String content1,
			String title, String classId, String sourceType, String isCommendType);

	/**
	 * 老师自问自答，已学生的名义把问题提给某个学员
	 * 
	 * @param teacherClassId
	 * @param studentUser
	 * @param userId
	 * @param imgUrls
	 * @param content
	 * @param title
	 * @param answerUserId
	 * @param termId
	 * @return
	 */
	public LcmsMutualSubject teachHelpStudentSave(String teacherClassId, GjtUserAccount studentUser, String userId,
			String[] imgUrls, String content, String title, String answerUserId, String termId);

	public LcmsMutualSubject updateOften(String subjectId, String title, String[] imgUrls1, String content1,
			String oftenType, String isCommendType);

}