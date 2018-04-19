/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.mobileMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.home.mobileMessage.GjtMoblieMessageUserDao;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageUser;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageUserService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
@Service
public class GjtMoblieMessageUserServiceImpl implements GjtMoblieMessageUserService {

	@Autowired
	GjtMoblieMessageUserDao gjtMoblieMessageUserDao;

	@Autowired
	CommonDao commonDao;

	@Override
	public void insert(List<GjtMoblieMessageUser> entities) {
		gjtMoblieMessageUserDao.save(entities);
	}

	@Override
	public List<Map<String, Object>> findMobileMessageUser(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select b.login_account, b.sjh,b.real_name");
		sql.append("  from gjt_moblie_message_user a");
		sql.append("  inner join gjt_user_account b");
		sql.append("  on a.user_id = b.id");
		sql.append("  where a.mobile_message_id = :id");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> findSendCount(List<String> ids) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select count(0) \"num\",u.mobile_message_id \"id\"");
		sql.append("  from gjt_moblie_message_user u");
		sql.append("  where u.mobile_message_id in (:ids) group by u.mobile_message_id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public Page<GjtMoblieMessageUser> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMoblieMessageUser> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMoblieMessageUser.class);
		PageRequest pageRequest = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "sendTime"));
		return gjtMoblieMessageUserDao.findAll(spec, pageRequest);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMoblieMessageUser> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMoblieMessageUser.class);
		return gjtMoblieMessageUserDao.count(spec);
	}

}
