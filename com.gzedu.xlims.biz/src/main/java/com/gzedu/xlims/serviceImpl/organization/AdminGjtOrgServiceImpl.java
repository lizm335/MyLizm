/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;

/**
 * 
 * 功能说明：组织架构 实现接口(废弃）
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class AdminGjtOrgServiceImpl {

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Autowired
	GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private PriRoleInfoService priRoleInfoService;

	public Page<GjtOrg> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtOrg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtOrg.class);
		return gjtOrgDao.findAll(spec, pageRequst);
	}

	public Page<GjtOrg> queryAllByParentId(boolean isChild, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// user = gjtUserAccountDao.findOne(user.getId());
		// if (user.getGjtOrg() != null) {
		// if (isChild) {
		// filters.put("parentGjtOrg.id",
		// new SearchFilter("parentGjtOrg.id", Operator.EQ,
		// user.getGjtOrg().getId()));
		// } else {
		// filters.put("id", new SearchFilter("id", Operator.EQ,
		// user.getGjtOrg().getId()));
		// }
		// } else {
		// filters.put("id", new SearchFilter("id", Operator.EQ, null));
		// }
		// filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ,
		// "N"));

		Specification<GjtOrg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtOrg.class);
		return gjtOrgDao.findAll(spec, pageRequst);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.organization.GjtOrgService#queryAll()
	 */
	public List<GjtOrg> queryAll() {
		return gjtOrgDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.organization.GjtOrgService#queryById(java.lang.
	 * String)
	 */
	public GjtOrg queryById(String id) {
		return gjtOrgDao.findOne(id);
	}

	public void insert(GjtOrg entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.getGjtSchoolInfo().setId(entity.getId());
		entity.getGjtSchoolInfo().setXxmc(entity.getOrgName());
		gjtOrgDao.save(entity);
	}

	public void update(GjtOrg entity) {
		entity.setUpdatedDt(new Date());
		gjtOrgDao.save(entity);
	}

	public void delete(List<String> ids) {
	}

	// 查询管理者角色列表
	public Page<GjtUserAccount> queryUserManagers(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		List<PriRoleInfo> orgMagagers = priRoleInfoService.queryOrgMagagerRoles();
		filters.put("priRoleInfo", new SearchFilter("priRoleInfo", Operator.IN, orgMagagers));
		Specification<GjtUserAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtUserAccount.class);
		return gjtUserAccountDao.findAll(spec, pageRequst);
	}

	public void insertChildOrg(GjtOrg entity) {
		// TODO Auto-generated method stub
	}

	public List<GjtOrg> queryAll(Iterable<String> orgIds) {
		return (List<GjtOrg>) gjtOrgDao.findAll(orgIds);
	}

}
