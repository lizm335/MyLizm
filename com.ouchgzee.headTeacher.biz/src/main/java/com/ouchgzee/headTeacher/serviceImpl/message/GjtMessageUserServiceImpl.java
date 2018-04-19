package com.ouchgzee.headTeacher.serviceImpl.message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.message.GjtMessageUserDao;
import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageUserService;

@Deprecated @Service("bzrGjtMessageUserServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtMessageUserServiceImpl extends BaseDaoImpl implements BzrGjtMessageUserService {

	@Autowired
	GjtMessageUserDao gjtMessageUserDao;

	@Override
	public Page<BzrGjtMessageUser> queryAll(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), new Sort(
					new Sort.Order("isEnabled"), new Sort.Order(Sort.Direction.DESC, "gjtMessageInfo.effectiveTime")));
		}
		Criteria<BzrGjtMessageUser> spec = new Criteria();
		// // 设置左连接
		// spec.setJoinType("gjtMessageInfo", JoinType.LEFT);

		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtMessageInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtMessageUserDao.findAll(spec, pageRequest);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Criteria<BzrGjtMessageUser> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtMessageInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtMessageUserDao.count(spec);
	}

	@Override
	public boolean updateRead(String messageId, String userId) {
		gjtMessageUserDao.updateRead(messageId, userId, new Date());
		return true;
	}

	@Override
	public BzrGjtMessageUser queryById(String id) {
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
	public void save(Set<String> lists, String CreatedName, String messageId) {
		List<BzrGjtMessageUser> listAdd = new ArrayList<BzrGjtMessageUser>(); // save的list
		for (String id : lists) {
			if (StringUtils.isNotBlank(id)) {
				BzrGjtMessageUser gmu = new BzrGjtMessageUser();
				gmu.setId(UUIDUtils.random());
				gmu.setCreatedBy(CreatedName);
				gmu.setCreatedDt(DateUtils.getNowTime());
				gmu.setMessageId(messageId);
				gmu.setUserId(id);
				gmu.setIsDeleted("N");
				gmu.setIsEnabled("0");
				gmu.setVersion(new BigDecimal(1.0));
				listAdd.add(gmu);
			}
		}
		if (listAdd.size() > 0) {
			gjtMessageUserDao.save(listAdd);
		}
	}

	@Override
	public void insert(BzrGjtMessageUser entity) {
		gjtMessageUserDao.save(entity);
	}

	@Override
	public void insert(List<BzrGjtMessageUser> list) {
		gjtMessageUserDao.save(list);
	}

	@Override
	public void update(BzrGjtMessageUser entity) {
		gjtMessageUserDao.save(entity);
	}

	@Override
	public List<Object[]> queryPutMessageIds(String... messageIds) {
		return gjtMessageUserDao.queryPutMessageIds(Lists.newArrayList(messageIds));
	}

	@Override
	public List<Object[]> queryReadMessageIds(String... messageIds) {
		return gjtMessageUserDao.queryReadMessageIds(Lists.newArrayList(messageIds));
	}

	@Override
	public Page<Map> queryAllByMessageId(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.login_Account,b.real_Name,a.is_Enabled,a.updated_Dt from gjt_message_user a ");
		sql.append(" inner join gjt_user_account b on a.user_id=b.id");
		sql.append(" where a.message_id  =:messageId");
		String userName = (String) searchParams.get("userName");
		if (StringUtils.isNotBlank(userName)) {
			sql.append(" and (b.real_Name like :userName or b.login_Account like :userName)");
			params.put("userName", "%" + userName + "%");
		}
		String isRead = (String) searchParams.get("isRead");
		if (StringUtils.isNotBlank(isRead)) {
			sql.append(" and a.is_Enabled=:isRead ");
			params.put("isRead", isRead);
		}
		params.put("messageId", searchParams.get("messageId"));
		Page<Map> pageInfo = super.findByPageToMap(sql, params, pageRequst);
		return pageInfo;
	}

	@Override
	public List<BzrGjtMessageUser> queryByMessageId(String messageId) {
		return gjtMessageUserDao.findByMessageId(messageId);
	}

}