/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.GjtCertificateRecordDao;
import com.gzedu.xlims.pojo.graduation.GjtCertificateRecord;
import com.gzedu.xlims.service.graduation.GjtCertificateRecordService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：证书发放记录
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月31日
 * @version 3.0
 *
 */
@Service
public class GjtCertificateRecordServiceImpl extends BaseServiceImpl<GjtCertificateRecord> implements GjtCertificateRecordService {

	@Autowired
	GjtCertificateRecordDao gjtCertificateRecordDao;

	@Override
	protected BaseDao<GjtCertificateRecord, String> getBaseDao() {
		return gjtCertificateRecordDao;
	}

	@Override
	public Page<GjtCertificateRecord> queryAll(final String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("orgId", new SearchFilter("orgId", Operator.EQ, orgId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		if (! searchParams.containsKey("EQ_certificateType")){
			filters.put("certificateType", new SearchFilter("certificateType", Operator.EQ, 0));
		}
		Specification<GjtCertificateRecord> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtCertificateRecord.class);
		return gjtCertificateRecordDao.findAll(spec, pageRequst);
	}

	@Override
	public long countAll(final String orgId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("orgId", new SearchFilter("orgId", Operator.EQ, orgId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtCertificateRecord> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtCertificateRecord.class);
		return gjtCertificateRecordDao.count(spec);
	}

	@Override
	public List<GjtCertificateRecord> queryAll(final String orgId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("orgId", new SearchFilter("orgId", Operator.EQ, orgId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtCertificateRecord> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtCertificateRecord.class);
		return gjtCertificateRecordDao.findAll(spec);
	}

	@Override
	public long count(Map<String, Object> searchParams) {
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtCertificateRecord> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtCertificateRecord.class);
		return gjtCertificateRecordDao.count(spec);
	}

}
