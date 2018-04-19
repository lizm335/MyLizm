/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtChangeSpecialtyDao;
import com.gzedu.xlims.pojo.GjtChangeSpecialty;
import com.gzedu.xlims.service.organization.GjtChangeSpecialtyService;

/**
 * 
 * 功能说明：学籍异动实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月6日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtChangeSpecialtyServiceImpl implements GjtChangeSpecialtyService {

	@Autowired
	private GjtChangeSpecialtyDao gjtChangeSpecialtyDao;

	@Override
	public Boolean saveEntity(GjtChangeSpecialty entity) {
		GjtChangeSpecialty save = gjtChangeSpecialtyDao.save(entity);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 真删除
	 */
	@Override
	public void delete(String id) {
		gjtChangeSpecialtyDao.delete(id);
	}

	@Override
	public Page<GjtChangeSpecialty> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		filters.put("gjtStudentInfo.gjtSchoolInfo.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.id", Operator.EQ, orgId));

		Specification<GjtChangeSpecialty> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtChangeSpecialty.class);
		return gjtChangeSpecialtyDao.findAll(spec, pageRequst);
	}
}
