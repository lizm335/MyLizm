package com.gzedu.xlims.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.GjtRulesSettingDao;
import com.gzedu.xlims.pojo.GjtRulesSetting;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtRulesSettingService;

/**
 * 
 * 功能说明：规则设置
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service
public class GjtRulesSettingServiceImpl implements GjtRulesSettingService {

	@Autowired
	GjtRulesSettingDao gjtRulesSettingDao;

	@Autowired
	CommonMapService commonMapService;

	@Override
	public Page<GjtRulesSetting> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequest) {

		Map<String, String> orgId = commonMapService.getOrgMapByOrgId(schoolId);

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		// filters.put("gjtGrade.isDeleted", new
		// SearchFilter("gjtGrade.isDeleted", Operator.EQ, "N"));
		// filters.put("gjtSpecialty.isDeleted", new
		// SearchFilter("gjtSpecialty.isDeleted", Operator.EQ, "N"));
		filters.put("gjtSchoolInfo.id", new SearchFilter("gjtSchoolInfo.id", Operator.IN, orgId.keySet()));
		Specification<GjtRulesSetting> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtRulesSetting.class);
		return gjtRulesSettingDao.findAll(spec, pageRequest);
	}

	@Override
	public GjtRulesSetting queryBy(String id) {
		return gjtRulesSettingDao.findOne(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtRulesSettingDao.delete(id);
			}
		}

	}

	@Override
	public void delete(String id) {
		gjtRulesSettingDao.delete(id);

	}

	@Override
	public void insert(GjtRulesSetting entity) {
		gjtRulesSettingDao.save(entity);

	}

	@Override
	public void update(GjtRulesSetting entity) {
		gjtRulesSettingDao.save(entity);
	}

	@Override
	public void deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtRulesSettingDao.delete(id);
			}
		}

	}

}
