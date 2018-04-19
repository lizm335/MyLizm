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
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;

/**
 * 
 * 功能说明：院校管理 实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtSchoolInfoServiceImpl implements GjtSchoolInfoService {

	@Autowired
	private GjtSchoolInfoDao gjtSchoolInfoDao;
	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Override
	public Page<GjtSchoolInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtSchoolInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSchoolInfo.class);
		return gjtSchoolInfoDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtSchoolInfo> queryAll(Iterable<String> ids) {
		return (List<GjtSchoolInfo>) gjtSchoolInfoDao.findAll(ids);
	}

	/**
	 * 添加学院
	 * 
	 * @param employeeInfo
	 */
	@Override
	public void saveEntity(GjtSchoolInfo employeeInfo) {
		String id = UUIDUtils.random();
		employeeInfo.setId(id);

		GjtOrg gjtOrg = new GjtOrg();
		gjtOrg.setId(id);
		// gjtOrg.setOrgName(employeeInfo.getXxmc());
		gjtOrg.setCreatedDt(new Date());
		gjtOrgDao.save(gjtOrg);

		// employeeInfo.setGjtOrg(gjtOrg);
		gjtSchoolInfoDao.save(employeeInfo);
	}

	/**
	 * 修改学院信息
	 */
	@Override
	public void updateEntity(GjtSchoolInfo employeeInfo) {

		employeeInfo.setUpdatedDt(new Date());

		// employeeInfo.getGjtOrg().setOrgName(employeeInfo.getXxmc());
		gjtSchoolInfoDao.save(employeeInfo);
	}

	/**
	 * 查询单个学院信息
	 */
	@Override
	public GjtSchoolInfo queryById(String id) {
		return gjtSchoolInfoDao.findOne(id);
	}

	/**
	 * 查询所有学院
	 */
	@Override
	public List<GjtSchoolInfo> queryAll() {
		return gjtSchoolInfoDao.findAll();
	}

	@Override
	public boolean delete(Iterable<String> ids) {
		for (String id : ids) {
			GjtSchoolInfo schoolInfo = gjtSchoolInfoDao.findOne(id);
			// if (schoolInfo.getGjtEmployeeInfos().size() > 0) {
			// throw new ServiceException("存在职员数量:" +
			// schoolInfo.getGjtEmployeeInfos().size());
			// }
			// if (schoolInfo.getGjtStudentInfos().size() > 0) {
			// throw new ServiceException("存在学生数量:" +
			// schoolInfo.getGjtStudentInfos().size());
			// }

			// if (schoolInfo.getGjtOrg().getChildGjtOrgs().size() > 0) {
			// throw new ServiceException("存在分院数量:" +
			// schoolInfo.getGjtOrg().getChildGjtOrgs().size());
			// }
			gjtSchoolInfoDao.delete(schoolInfo);
		}
		return true;
	}

	@Override
	public GjtSchoolInfo queryByCode(String code) {
		return gjtSchoolInfoDao.findByGjtOrgCode(code);
	}

	@Override
	public GjtSchoolInfo queryByName(String name) {
		List<GjtSchoolInfo> list = gjtSchoolInfoDao.findByXxmc(name);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public GjtSchoolInfo queryAppidByOrgId(String id) {		
		return gjtSchoolInfoDao.queryAppidByOrgId(id);
	}

}
