package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtTermCourseinfoDao;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.service.organization.GjtTermCourseinfoService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtTermCourseinfoServiceImpl extends BaseServiceImpl<GjtTermCourseinfo>
		implements GjtTermCourseinfoService {

	@Autowired
	private GjtTermCourseinfoDao gjtTermCourseinfoDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<GjtTermCourseinfo, String> getBaseDao() {
		return gjtTermCourseinfoDao;
	}

	@Override
	public List<String> findTermIdsBytermCourseIds(List<String> termCourseIds) {
		List<String> termIds = new ArrayList<String>();
		for (String tc : termCourseIds) {
			termIds.add(getBaseDao().findOne(tc).getTermId());
		}
		return  termIds;
	}

	@Override
	public List<Map<String, Object>> queryCourseByTeacherId(String teachId) {
		String sql = "select distinct a.course_id from gjt_term_courseinfo a where a.class_teacher=:teachId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("teachId", teachId);
		return commonDao.queryForMapList(sql, params);
	}

}
