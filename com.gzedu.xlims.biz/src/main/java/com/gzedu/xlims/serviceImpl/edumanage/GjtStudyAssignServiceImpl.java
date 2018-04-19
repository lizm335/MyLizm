/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

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
import com.gzedu.xlims.dao.edumanage.GjtStudyyearAssignmentDao;
import com.gzedu.xlims.pojo.GjtStudyYearAssignment;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyAssignService;

/**
 * 
 * 功能说明：学年度 任务制作
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service
public class GjtStudyAssignServiceImpl implements GjtStudyAssignService {

	@Autowired
	CommonMapService commonMapService;
	@Autowired
	GjtStudyyearAssignmentDao gjtStudyAssignDao;

	@Override
	public Page<GjtStudyYearAssignment> queryAll(Map<String, Object> map, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtStudyYearAssignment> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudyYearAssignment.class);
		return gjtStudyAssignDao.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtStudyYearAssignment> queryAlls(Map<String, Object> map) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtStudyYearAssignment> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudyYearAssignment.class);
		return gjtStudyAssignDao.findAll(spec);
	}

	@Override
	public Boolean saveEntity(GjtStudyYearAssignment entity) {
		GjtStudyYearAssignment assignment = gjtStudyAssignDao.save(entity);
		if (assignment != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updateEntity(GjtStudyYearAssignment entity) {
		GjtStudyYearAssignment assignment = gjtStudyAssignDao.save(entity);
		if (assignment != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GjtStudyYearAssignment queryById(String id) {
		return gjtStudyAssignDao.findOne(id);
	}

	@Override
	public void deleteById(String[] list) {
		if (list != null) {
			for (String id : list) {
				gjtStudyAssignDao.deleteById(id, "Y");
			}
		}
	}

	@Override
	public void delete(String[] list) {
		if (list != null) {
			for (String id : list) {
				gjtStudyAssignDao.delete(id);
			}
		}
	}

	@Override
	public void updateStutas(String id, String status) {
		gjtStudyAssignDao.updateStatus(id, status);
	}

}
