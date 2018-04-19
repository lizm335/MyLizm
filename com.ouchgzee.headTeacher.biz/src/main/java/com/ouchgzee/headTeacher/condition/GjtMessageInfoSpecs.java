/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.condition;

import com.gzedu.xlims.common.Constants;
import com.ouchgzee.headTeacher.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息查询条件拼接<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月9日
 * @version 2.5
 * @since JDK 1.7
 */
public class GjtMessageInfoSpecs {

	private static Logger log = LoggerFactory.getLogger(GjtMessageInfoSpecs.class);

	/**
	 * @param messageInfo
	 * @return
	 */
	public static Specification<BzrGjtMessageInfo> find(final BzrGjtMessageInfo messageInfo) {
		return new Specification<BzrGjtMessageInfo>() {
			@Override
			public Predicate toPredicate(Root<BzrGjtMessageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				expressions.add(cb.equal(root.get("isDeleted"), Constants.BOOLEAN_NO));
				// 学校ID
				if (StringUtils.isNotBlank(messageInfo.getXxId())) {
					expressions.add(cb.equal(root.get("xxId"), messageInfo.getXxId()));
				}
				// // in和or的用法
				// List<String> array = new ArrayList<String>();
				// array.add(userAccount.getLoginAccount());
				// array.add("88901500000101");
				// array.add("100040");
				// expressions.add(cb.or(cb.in(joinSUA.get("loginAccount")).value(array),
				// cb.equal(joinSC.get("classId"), classInfo.getClassId())));
				return predicate;
			}
		};
	}

	/**
	 * 
	 * @param messageInfo
	 * @return
	 */
	public static Specification<BzrGjtMessageInfo> findHeadTeacherMessageInfo(final BzrGjtMessageInfo messageInfo) {
		return new Specification<BzrGjtMessageInfo>() {
			@Override
			public Predicate toPredicate(Root<BzrGjtMessageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				expressions.add(cb.equal(root.get("isDeleted"), Constants.BOOLEAN_NO));
				expressions.add(cb.equal(root.get("infoType"), "2"));
				expressions.add(cb.equal(root.get("infoTool"), "1"));
				// 学校ID
				if (StringUtils.isNotBlank(messageInfo.getXxId())) {
					expressions.add(cb.equal(root.get("xxId"), messageInfo.getXxId()));
				}
				// 标题
				if (StringUtils.isNotBlank(messageInfo.getInfoTheme())) {
					expressions.add(cb.like(root.<String> get("infoTheme"), "%" + messageInfo.getInfoTheme() + "%"));
				}
				// 发布时间
				try {
					if (StringUtils.isNotBlank(messageInfo.getCondCreatedDtBegin())) {
						Date createdDtBegin = DateUtils.parseDate(messageInfo.getCondCreatedDtBegin() + " 00:00:00",
								"yyyy-MM-dd HH:mm:ss");
						expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("createdDt"), createdDtBegin));
					}
					if (StringUtils.isNotBlank(messageInfo.getCondCreatedDtEnd())) {
						Date createdDtEnd = DateUtils.parseDate(messageInfo.getCondCreatedDtEnd() + " 23:59:59",
								"yyyy-MM-dd HH:mm:ss");
						expressions.add(cb.lessThanOrEqualTo(root.<Date> get("createdDt"), createdDtEnd));
					}
				} catch (ParseException e) {
					log.error("时间类型转化异常", e);
				}
				return predicate;
			}
		};
	}

	/**
	 * 
	 * @param messageInfo
	 * @param classId
	 * @param putUser
	 * @return
	 */
	public static Specification<BzrGjtMessageInfo> findClassMessageInfoByClassId(final BzrGjtMessageInfo messageInfo,
																				 final String classId, final String putUser) {
		return new Specification<BzrGjtMessageInfo>() {
			@Override
			public Predicate toPredicate(Root<BzrGjtMessageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				expressions.add(cb.equal(root.get("isDeleted"), Constants.BOOLEAN_NO));
				expressions.add(cb.equal(root.get("infoTool"), "1"));
				// 学校ID
				if (StringUtils.isNotBlank(messageInfo.getXxId())) {
					expressions.add(cb.equal(root.get("xxId"), messageInfo.getXxId()));
				}
				// 班级
				if (StringUtils.isNotBlank(classId)) {
					// 子查询
					Subquery<BzrGjtStudentInfo> sq = query.subquery(BzrGjtStudentInfo.class);

					Root<BzrGjtStudentInfo> uaRoot = sq.from(BzrGjtStudentInfo.class);
					Join<BzrGjtStudentInfo, BzrGjtClassStudent> joinSCS = uaRoot.join("gjtClassStudentList");
					Join<BzrGjtClassStudent, BzrGjtClassInfo> joinCSC = joinSCS.join("gjtClassInfo");
					Join<BzrGjtStudentInfo, BzrGjtUserAccount> joinSUA = uaRoot.join("gjtUserAccount");

					sq.where(cb.equal(uaRoot.get("isDeleted"), Constants.BOOLEAN_NO),
							cb.equal(joinSCS.get("isDeleted"), Constants.BOOLEAN_NO),
							cb.equal(joinCSC.get("isDeleted"), Constants.BOOLEAN_NO),
							cb.equal(joinSUA.get("isDeleted"), Constants.BOOLEAN_NO),
							cb.equal(joinCSC.get("classId"), classId),
							cb.gt(cb.locate(root.<String> get("getUser"), joinSUA.<String> get("loginAccount")), 0));
					sq.select(uaRoot);

					Predicate predicate1 = cb.and(cb.equal(root.get("getUserMethod"), 1), cb.exists(sq));
					Predicate predicate2 = cb.and(cb.equal(root.get("getUserMethod"), 2),
							cb.like(root.<String> get("getUser"), "%" + classId + "%"));
					expressions.add(cb.or(predicate1, predicate2));
				}
				// 发送者
				if (StringUtils.isNotBlank(putUser)) {
					Join<BzrGjtMessageInfo, BzrGjtUserAccount> joinMUA = root.join("gjtUserAccount");
					expressions.add(cb.equal(joinMUA.get("isDeleted"), Constants.BOOLEAN_NO));

					expressions.add(cb.equal(joinMUA.get("id"), putUser));
				}
				// 标题
				if (StringUtils.isNotBlank(messageInfo.getInfoTheme())) {
					expressions.add(cb.like(root.<String> get("infoTheme"), "%" + messageInfo.getInfoTheme() + "%"));
				}
				// 消息类型 1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒
				if (StringUtils.isNotBlank(messageInfo.getInfoType())) {
					expressions.add(cb.equal(root.get("infoType"), messageInfo.getInfoType()));
				} else {
					List<String> array = new ArrayList<String>();
					array.add("11");
					array.add("12");
					array.add("13");
					expressions.add(cb.in(root.get("infoType")).value(array));
				}
				// 发布时间
				try {
					if (StringUtils.isNotBlank(messageInfo.getCondCreatedDtBegin())) {
						Date createdDtBegin = DateUtils.parseDate(messageInfo.getCondCreatedDtBegin() + " 00:00:00",
								"yyyy-MM-dd HH:mm:ss");
						expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("createdDt"), createdDtBegin));
					}
					if (StringUtils.isNotBlank(messageInfo.getCondCreatedDtEnd())) {
						Date createdDtEnd = DateUtils.parseDate(messageInfo.getCondCreatedDtEnd() + " 23:59:59",
								"yyyy-MM-dd HH:mm:ss");
						expressions.add(cb.lessThanOrEqualTo(root.<Date> get("createdDt"), createdDtEnd));
					}
				} catch (ParseException e) {
					log.error("时间类型转化异常", e);
				}
				return predicate;
			}
		};
	}

}
