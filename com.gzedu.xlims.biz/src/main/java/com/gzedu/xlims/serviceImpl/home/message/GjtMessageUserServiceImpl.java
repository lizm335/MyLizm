/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.home.message.GjtMessageUserDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.message.GjtMessageUser;
import com.gzedu.xlims.service.home.message.GjtMessageUserService;

/**
 * 
 * 功能说明：用户信息实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月11日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service
public class GjtMessageUserServiceImpl extends BaseDaoImpl implements GjtMessageUserService {

	@Autowired
	GjtMessageUserDao gjtMessageUserDao;

	@Autowired
	CommonDao commonDao;

	@Override
	public Page<GjtMessageUser> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMessageUser> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMessageUser.class);

		PageRequest pageRequest = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "gjtMessageInfo.effectiveTime"));
		return gjtMessageUserDao.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtMessageUser> queryAll(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMessageUser> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMessageUser.class);
		return gjtMessageUserDao.findAll(spec);
	}

	@Override
	public Page<Map> queryAllByMessageId(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder();
		sql.append("  select to_char(e.feedback_dt,'yyyy-mm-dd hh24:mi:ss') feedback_dt,");
		sql.append(" e.is_tickling,e.feedback_type,e.id,e.is_constraint,e.platform,s.xh login_Account,");
		sql.append("  s.xm real_Name,");
		sql.append("  e.is_Enabled,");
		sql.append("  to_char(e.updated_Dt,'yyyy-mm-dd hh24:mi:ss') updated_Dt,");
		sql.append("  '学员' role_name,");
		sql.append("  g.grade_name,");
		sql.append("  case s.pycc");
		sql.append("  when '0' then");
		sql.append("  '专科'");
		sql.append("  when '2' then");
		sql.append("  '本科'");
		sql.append("  else");
		sql.append("  '--'");
		sql.append("  end pycc,");
		sql.append("  gs.zymc");
		sql.append("  from gjt_message_user e");
		sql.append("  inner join gjt_student_info s");
		sql.append("  on e.user_id = s.account_id");
		sql.append("  inner join gjt_grade g");
		sql.append("  on s.nj = g.grade_id");
		sql.append("  inner join gjt_specialty gs");
		sql.append("  on gs.specialty_id = s.major");
		sql.append("  where e.message_id = :messageId");

		String gradeId = (String) searchParams.get("EQ_gradeId");
		if (StringUtils.isNotBlank(gradeId)) {
			sql.append("  and g.grade_id = :gradeId");
			params.put("gradeId", gradeId);
		}

		String pycc = (String) searchParams.get("EQ_pycc");
		if (StringUtils.isNotBlank(pycc)) {
			sql.append("  and s.pycc = :pycc");
			params.put("pycc", pycc);
		}

		String specialtyId = (String) searchParams.get("EQ_specialtyId");
		if (StringUtils.isNotBlank(specialtyId)) {
			sql.append("  and s.major = :specialtyId");
			params.put("specialtyId", specialtyId);
		}

		String userName = (String) searchParams.get("LIKE_userName");
		if (StringUtils.isNotBlank(userName)) {
			sql.append("  and s.xm like :userName ");
			params.put("userName", "%" + userName + "%");
		}

		String EQ_loginAccount = (String) searchParams.get("EQ_loginAccount");
		if (StringUtils.isNotBlank(EQ_loginAccount)) {
			sql.append("  and s.xh=:xh ");
			params.put("xh", EQ_loginAccount);
		}

		String isRead = (String) searchParams.get("isRead");
		if (StringUtils.isNotBlank(isRead)) {
			sql.append("  and e.is_enabled = :isRead");
			params.put("isRead", isRead);
		}

		String platform = (String) searchParams.get("platform");
		if (StringUtils.isNotBlank(platform)) {
			sql.append("  and e.platform = :platform");
			params.put("platform", platform);
		}
		String isTickling = (String) searchParams.get("EQ_isTickling");
		if (StringUtils.isNotBlank(isTickling)) {
			sql.append("  and e.is_Tickling = :isTickling");
			params.put("isTickling", isTickling);
		}

		sql.append("  and e.is_deleted = 'N'");
		sql.append("  union all");
		sql.append("  select  to_char(a.feedback_dt,'yyyy-mm-dd hh24:mi:ss') FEEDBACK_DT,");
		sql.append("  a.is_tickling,a.feedback_type,a.id,a.is_constraint,a.platform,b.login_Account,");
		sql.append("  b.real_Name,");
		sql.append("  a.is_Enabled,");
		sql.append("  to_char(a.updated_Dt,'yyyy-mm-dd hh24:mi:ss') updated_Dt,");
		sql.append("  c.role_name,");
		sql.append("  '--' grade_name,");
		sql.append("  '--' pycc,");
		sql.append("  '--' zymc");
		sql.append("  from gjt_message_user a");
		sql.append("  inner join gjt_user_account b");
		sql.append("  on a.user_id = b.id");
		sql.append("  inner join pri_role_info c");
		sql.append("  on b.role_id = c.role_id");
		sql.append("  where a.message_id = :messageId");

		if (StringUtils.isNotBlank(userName)) {
			sql.append(" and b.real_Name like :userName");
			params.put("userName", "%" + userName + "%");
		}

		if (StringUtils.isNotBlank(EQ_loginAccount)) {
			sql.append(" and  b.login_Account like :loginAccount");
			params.put("loginAccount", EQ_loginAccount);
		}

		if (StringUtils.isNotBlank(isRead)) {
			sql.append(" and a.is_Enabled=:isRead ");
			params.put("isRead", isRead);
		}

		if (StringUtils.isNotBlank(platform)) {
			sql.append(" and a.platform=:platform ");
			params.put("platform", platform);
		}

		if (StringUtils.isNotBlank(isTickling)) {
			sql.append(" and a.is_Tickling=:isTickling ");
			params.put("isTickling", isTickling);
		}

		sql.append("  and a.is_deleted = 'N'");

		params.put("messageId", searchParams.get("messageId"));
		Page<Map> pageInfo = super.findByPageToMap(sql, params, pageRequst);
		return pageInfo;
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMessageUser> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMessageUser.class);
		return gjtMessageUserDao.count(spec);
	}

	@Override
	public GjtMessageUser queryById(String id) {
		return gjtMessageUserDao.findOne(id);
	}

	@Override
	public void delete(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtMessageUserDao.delete(id);
			}
		}
	}

	@Override
	public void delete(String id) {
		gjtMessageUserDao.delete(id);
	}

	@Override
	public void save(List<String> userLists, String userName, String messageId) {
		List<GjtMessageUser> listAdd = new ArrayList<GjtMessageUser>(); // save的list
		if (userLists != null && userLists.size() > 0) {
			for (String userId : userLists) {
				GjtMessageUser gmu = new GjtMessageUser();
				gmu.setId(UUIDUtils.random());
				gmu.setCreatedBy(userName);
				gmu.setCreatedDt(DateUtils.getNowTime());
				gmu.setMessageId(messageId);
				gmu.setUserId(userId);
				gmu.setIsDeleted("N");
				gmu.setIsEnabled("0");
				gmu.setVersion(new BigDecimal(1.0));
				listAdd.add(gmu);
			}
			gjtMessageUserDao.save(listAdd);
		}
	}

	@Override
	public void insert(GjtMessageUser entity) {
		gjtMessageUserDao.save(entity);
	}

	@Override
	public void insert(List<GjtMessageUser> list) {
		gjtMessageUserDao.save(list);
	}

	@Override
	public void update(GjtMessageUser entity) {
		gjtMessageUserDao.save(entity);
	}

	@Override
	public GjtMessageUser queryByUserIdAndMessageId(String userId, String messageId) {
		return gjtMessageUserDao.findByUserIdAndMessageId(userId, messageId);
	}

	@Override
	public List<Object[]> queryPutMessageIds(List<String> messageIds) {
		return gjtMessageUserDao.queryPutMessageIds(messageIds);
	}

	@Override
	public List<Object[]> findReadMessageIds(List<String> messageIds) {
		return gjtMessageUserDao.findReadMessageIds(messageIds);
	}

	@Override
	public List<Object[]> queryReadMessageIds(List<String> messageIds) {
		return gjtMessageUserDao.queryReadMessageIds(messageIds);
	}

	@Override
	public List<Object[]> queryLikeMessageIds(List<String> messageIds) {
		return gjtMessageUserDao.queryLikeMessageIds(messageIds);
	}

	@Override
	public List<Object[]> queryTicklingMessageIds(List<String> messageIds) {
		return gjtMessageUserDao.queryTicklingMessageIds(messageIds);
	}

	@Override
	public List<Object[]> queryCommMessageIds(List<String> messageIds) {
		return gjtMessageUserDao.queryCommMessageIds(messageIds);
	}

	@Override
	public long updateConstraint(String messageId) {
		return gjtMessageUserDao.updateConstraint(messageId);
	}

	@Override
	public long settingsIsConstraint(String id, String isConstraint) {
		return gjtMessageUserDao.settingsIsConstraint(id, isConstraint);
	}

	@Override
	public List<Map<String, Object>> findEntityByMessageId(String messageId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select b.sjh,b.real_name");
		sql.append("  from gjt_message_user a");
		sql.append("  inner join gjt_user_account b");
		sql.append("  on a.user_id = b.id");
		sql.append("  where a.message_id = :messageId");
		sql.append("  and a.is_enabled = '0'");
		params.put("messageId", messageId);
		return commonDao.queryForMapList(sql.toString(), params);// 未阅读的
	}

	@Override
	public List<Map<String, Object>> queryTicktingList(String messageId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select count(*) num,  m.feedback_type");
		sql.append("  from gjt_message_user m");
		sql.append("  where m.message_id =:messageId ");
		sql.append("  and m.feedback_type is not null");
		sql.append("  group by  m.feedback_type ");
		params.put("messageId", messageId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> findTypeCount(String userId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select count(0) nums, b.info_type");
		sql.append("  from gjt_message_user a");
		sql.append("  inner join Gjt_Message_Info b");
		sql.append("  on a.message_id = b.message_id");
		sql.append("  where a.user_id = :userId");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  group by b.info_type");
		params.put("userId", userId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> findImportanceList(String userId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select m.message_id \"messageId\" ");
		sql.append("  from gjt_message_user u");
		sql.append("  inner join gjt_message_info m");
		sql.append("  on u.message_id = m.message_id");
		sql.append("  where u.user_id = :userId");
		sql.append("  and m.is_deleted = 'N'");
		sql.append("  and m.degree = '1'");
		sql.append("  and u.is_enabled = '0'");
		params.put("userId", userId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

}
