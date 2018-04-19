/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.recruitmanage;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.recruitmanage.GjtEnrollBatchDao;
import com.gzedu.xlims.pojo.GjtEnrollBatch;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.recruitmanage.GjtEnrollBatchService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月21日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtEnrollBatchServiceImpl implements GjtEnrollBatchService {

	private static final Log log = LogFactory.getLog(GjtEnrollBatchServiceImpl.class);
	@Autowired
	GjtEnrollBatchDao gjtEnrollBatchDao;

	@Autowired
	CommonMapService commonMapService;

	@Override
	public Page<GjtEnrollBatch> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, String> orgId = commonMapService.getOrgMapByOrgId(schoolId);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtGrade.isDeleted", new SearchFilter("gjtGrade.isDeleted", Operator.EQ, "N"));
		filters.put("gjtSchoolInfo.id", new SearchFilter("gjtSchoolInfo.id", Operator.IN, orgId.keySet()));
		Specification<GjtEnrollBatch> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtEnrollBatch.class);
		return gjtEnrollBatchDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtEnrollBatch queryBy(String id) {
		return gjtEnrollBatchDao.findOne(id);
	}

	/**
	 * 假删除
	 */
	@Override
	public void delete(Iterable<String> ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtEnrollBatchDao.deleteById(id, "Y");
			}
		}

	}

	@Override
	public void delete(String id) {
		gjtEnrollBatchDao.delete(id);

	}

	@Override
	public void insert(GjtEnrollBatch entity) {
		gjtEnrollBatchDao.save(entity);
	}

	@Override
	public void update(GjtEnrollBatch entity) {
		gjtEnrollBatchDao.save(entity);
	}

}
