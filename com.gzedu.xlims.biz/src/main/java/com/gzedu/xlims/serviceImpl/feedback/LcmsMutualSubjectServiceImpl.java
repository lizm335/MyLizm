package com.gzedu.xlims.serviceImpl.feedback;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.dao.feedback.LcmsMutualSubjectDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.status.SubjectSourceEnum;
import com.gzedu.xlims.service.feedback.LcmsMutualSubjectService;

@Service
public class LcmsMutualSubjectServiceImpl implements LcmsMutualSubjectService {

	private static final Logger log = LoggerFactory.getLogger(LcmsMutualSubjectServiceImpl.class);

	@Autowired
	LcmsMutualSubjectDao lcmsMutualSubjectDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Override
	public Page<LcmsMutualSubject> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<LcmsMutualSubject> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				LcmsMutualSubject.class);
		return lcmsMutualSubjectDao.findAll(spec, page);
	}

	@Override
	public Page<LcmsMutualSubject> queryAllPageInfo(String userId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Criteria<LcmsMutualSubject> spec = new Criteria();

		if (StringUtils.isNotBlank(userId)) {
			spec.add(Restrictions.or(Restrictions.eq("createAccountId", userId, true),
					Restrictions.eq("initialAccountId", userId, true)));
		}

		spec.addAll(Restrictions.parse(searchParams));

		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));

		return lcmsMutualSubjectDao.findAll(spec, page);
	}

	@Override
	public List<LcmsMutualSubject> queryAllInfo(String title, Map<String, Object> searchParams) {

		Criteria<LcmsMutualSubject> spec = new Criteria();
		if (StringUtils.isNotBlank(title)) {
			spec.add(Restrictions.or(Restrictions.like("subjectTitle", title, true),
					Restrictions.like("subjectContent", title, true)));
		}

		spec.addAll(Restrictions.parse(searchParams));
		return lcmsMutualSubjectDao.findAll(spec);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<LcmsMutualSubject> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				LcmsMutualSubject.class);
		return lcmsMutualSubjectDao.count(spec);
	}

	@Override
	public LcmsMutualSubject save(LcmsMutualSubject item) {
		item.setSubjectId(UUIDUtils.random().toString());
		if (!"Y".equals(item.getIsCommend())) {// 常见问题没有解答这个状态
			item.setSubjectStatus("N");// 未解决
		}
		if (StringUtils.isBlank(item.getIsCommend())) {
			item.setIsCommend("N");// 是否常见问题
		}
		if (StringUtils.isBlank(item.getIsTranspond())) {
			item.setIsTranspond("0");// 否转发
		}
		if (StringUtils.isBlank(item.getSoleType())) {
			item.setSoleType("0");// 学生提问的
		}

		if (StringUtils.isBlank(item.getUserType())) {
			item.setUserType("stud");// 兼容旧平台
		}
		item.setIsdeleted("N");
		item.setReplyCount(new BigDecimal("0"));// 回复数
		item.setSubjectType("1");// 旧平台 1答疑，2意见
		item.setVersion(new BigDecimal("1"));
		item.setIsSolve("0");// 是否已解决
		item.setUpdatedDt(DateUtils.getNowTime());// 记录问题超时
		item.setCreatedDt(DateUtils.getNowTime());
		return lcmsMutualSubjectDao.save(item);
	}

	@Override
	public LcmsMutualSubject queryById(String id) {
		return lcmsMutualSubjectDao.findOne(id);
	}

	@Override
	public boolean update(LcmsMutualSubject item) {
		LcmsMutualSubject entity = lcmsMutualSubjectDao.save(item);
		return entity == null ? false : true;
	}

	@Override
	public boolean updateIsComm(String id, String state, String oftenType, String isCommendType) {
		int i = lcmsMutualSubjectDao.updateIsComm(id, state, oftenType, isCommendType);
		return i == 1 ? true : false;
	}

	@Override
	public boolean delete(String id) {
		int i = lcmsMutualSubjectDao.updateIsDelete(id);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateForward(String id, String chushiId, String studentName, String teacherName, String content) {
		int i = lcmsMutualSubjectDao.updateForward(id, chushiId);

		// 发送短信提醒
		GjtUserAccount account = gjtUserAccountDao.findOne(chushiId);
		String remark = "你转发" + studentName + "(学员)的问题" + teacherName + "因" + content + "解答不了，请尽快登录平台查看并解答问题！";
		int smsResult = SMSUtil.sendMessage(account.getTelephone(), remark, "gk");
		log.info("问题返回原处短信提醒参数remark={},result={}", remark, smsResult);

		return i == 1 ? true : false;
	}

	@Override
	public Map<String, Object> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LcmsMutualSubject save(String source, GjtClassInfo classInfo, GjtUserAccount user, String[] imgUrls,
			String content, String title) {

		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		LcmsMutualSubject item = new LcmsMutualSubject();

		if ("androidPhone".equals(source)) {
			source = SubjectSourceEnum.ANDROID_APP.getNum();
		}
		if ("iosPhone".equals(source)) {
			source = SubjectSourceEnum.IOS_APP.getNum();
		}

		item.setQuestionSource(source);
		item.setQuestionSourcePath(SubjectSourceEnum.getCode(source));
		item.setClassId(classInfo.getClassId());
		String teachUserId = classInfo.getGjtBzr().getGjtUserAccount().getId();
		item.setCreateAccount(user);
		item.setForwardAccountId(teachUserId);// 发送给班主任
		item.setInitialAccountId(teachUserId);// 问题初始人
		item.setResPath(imgUrl.toString());
		item.setSubjectContent(content);
		item.setSubjectTitle(title);
		item.setUserId(user.getGjtStudentInfo().getStudentId());// 兼容旧的userId字段和createBy字段，取studentId
		item.setCreatedBy(user.getGjtStudentInfo().getStudentId());// 兼容旧的userId字段和createBy字段，取studentId
		item.setOrgId(user.getGjtStudentInfo().getGjtSchoolInfo().getId());
		return this.save(item);
	}

	@Override
	public LcmsMutualSubject teachSave(String source, GjtUserAccount user, String[] imgUrls, String content,
			String title, String adminUserId) {

		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		LcmsMutualSubject item = new LcmsMutualSubject();

		item.setQuestionSource(source);
		item.setQuestionSourcePath(SubjectSourceEnum.getCode(source));
		item.setCreateAccount(user);
		item.setCreatedDt(DateUtils.getNowTime());
		item.setForwardAccountId(adminUserId);// 发送给后台管理员
		item.setInitialAccountId(adminUserId);// 发送给后台管理员
		item.setResPath(imgUrl.toString());
		item.setSubjectContent(content);
		item.setSubjectTitle(title);
		item.setUserId(user.getId());// 老师向后台管理员提问 老师的accountId
		item.setCreatedBy(user.getId());// 老师向后台管理员提问 老师的accountId
		item.setOrgId(user.getGjtOrg().getId());
		item.setSoleType("1");// 老师提问的
		return this.save(item);
	}

	@Override
	public LcmsMutualSubject saveOften(String oftenType, String[] imgUrls1, GjtUserAccount user, String content1,
			String title, String classId, String sourceType, String isCommendType) {

		StringBuffer imgUrl1 = new StringBuffer("");
		if (imgUrls1 != null && imgUrls1.length > 0) {
			for (int i = 0; i < imgUrls1.length; i++) {
				imgUrl1.append(imgUrls1[i]);
				if (i < imgUrls1.length - 1) {
					imgUrl1.append(",");
				}
			}
		}

		LcmsMutualSubject item = new LcmsMutualSubject();
		item.setClassId(classId);
		item.setCreateAccount(user);// 常见问题创建人
		item.setCreatedDt(DateUtils.getNowTime());
		item.setForwardAccountId(user.getId());// 发送给班主任
		item.setInitialAccountId(user.getId());// 问题初始人
		item.setIsCommend("Y");// 是否常见问题
		item.setResPath(imgUrl1.toString());
		item.setSubjectContent(content1);
		item.setSubjectTitle(title);
		item.setUserType("tchr");
		item.setUserId("");// 兼容旧的userId字段和createBy字段，取studentId
		item.setCreatedBy("");// 兼容旧的userId字段和createBy字段，取studentId
		item.setOftenType(oftenType);
		item.setOrgId(user.getGjtOrg().getId());
		item.setQuestionSource(sourceType);
		item.setQuestionSourcePath(SubjectSourceEnum.getCode(sourceType));
		item.setIsCommendType(isCommendType);
		return save(item);
	}

	@Override
	public LcmsMutualSubject teachHelpStudentSave(String teacherClassId, GjtUserAccount studentUser, String userId,
			String[] imgUrls, String content, String title, String answerUserId, String termId) {
		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}
		GjtStudentInfo studentInfo = studentUser.getGjtStudentInfo();
		LcmsMutualSubject item = new LcmsMutualSubject();
		item.setClassId(teacherClassId);
		item.setCreateAccount(studentUser);
		item.setForwardAccountId(answerUserId);// 发送给班主任
		item.setInitialAccountId(userId);// 问题初始人
		item.setResPath(imgUrl.toString());
		item.setSubjectContent(content);
		item.setSubjectTitle(title);
		item.setUserId(studentInfo.getStudentId());// 兼容旧的userId字段和createBy字段，取studentId
		item.setCreatedBy(studentInfo.getStudentId());// 兼容旧的userId字段和createBy字段，取studentId
		item.setOrgId(studentInfo.getGjtSchoolInfo().getId());
		item.setTermcourseId(termId);
		item.setIsTranspond("1");// 是转发
		return this.save(item);
	}

	public LcmsMutualSubject forwardOthers(String id, String classId, String teachId, String zhuanfaId, String roleId) {
		log.info("转发人员参数：id:" + id + "--zhuanfaId：" + zhuanfaId + "--classId:" + classId + "--teachId:" + teachId);
		LcmsMutualSubject item = lcmsMutualSubjectDao.findOne(id);
		if (item != null) {
			item.setForwardAccountId(zhuanfaId);
			item.setClassId(classId);
			item.setTermcourseId(teachId);
			item.setTranspondDt(DateUtils.getNowTime());
			item.setIsTranspond("1");

			if ("be60d336bc1946a5a24f88d5ae594b17".equals(roleId)) { // 辅导老师兼容辅导平台
				item.setReplyer("1");
			}
			if ("699f6f83acf54548bfae7794915a3cf3".equals(roleId)) { // 督导老师兼容辅导平台
				item.setReplyer("2");
			}

			GjtUserAccount zhuanfaUser = gjtUserAccountDao.findOne(zhuanfaId);
			GjtUserAccount initial = gjtUserAccountDao.findOne(item.getInitialAccountId());
			GjtUserAccount student = item.getCreateAccount();
			String remark = initial.getRealName() + "(" + initial.getPriRoleInfo().getRoleName() + ")向你转发了一条"
					+ student.getRealName() + "(学员)提出的答疑，请尽快登录平台查看并解答问题！";

			int smsResult = SMSUtil.sendMessage(zhuanfaUser.getTelephone(), remark, "gk");
			log.info("问题返回原处短信提醒参数remark={},result={}", remark, smsResult);
			return lcmsMutualSubjectDao.save(item);
		} else {
			return null;
		}
	}

	@Override
	public LcmsMutualSubject updateOften(String subjectId, String title, String[] imgUrls1, String content1,
			String oftenType, String isCommendType) {
		LcmsMutualSubject modify = lcmsMutualSubjectDao.findOne(subjectId);
		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls1 != null && imgUrls1.length > 0) {
			for (int i = 0; i < imgUrls1.length; i++) {
				imgUrl.append(imgUrls1[i]);
				if (i < imgUrls1.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		modify.setSubjectTitle(title);
		modify.setSubjectContent(content1);
		modify.setResPath(imgUrl.toString());
		modify.setOftenType(oftenType);
		modify.setIsCommendType(isCommendType);
		modify.setUpdatedDt(DateUtils.getNowTime());

		return lcmsMutualSubjectDao.save(modify);
	}
}