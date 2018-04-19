/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.graduation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.graduation.GjtFirstCourseDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtFirstCourse;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtSpecialtyVideo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.graduation.GjtFirstCourseService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

import net.sf.json.JSONObject;

/**
 * 功能说明：证书发放记录
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月31日
 * @version 3.0
 *
 */
@Service
public class GjtFirstCourseServiceImpl extends BaseServiceImpl<GjtFirstCourse> implements GjtFirstCourseService {
	@Autowired
	private GjtFirstCourseDao gjtFirstCourseDao;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Override
	protected BaseDao<GjtFirstCourse, String> getBaseDao() {
		return gjtFirstCourseDao;
	}

	@Override
	public Page<GjtFirstCourse> queryFirstCoursesListByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder("select c.* from GJT_FIRST_COURSE c where c.IS_DELETED='N'");
		Map<String, Object> params = new HashMap<String, Object>();
		String orgId = (String) searchParams.get("orgId");
		if (StringUtils.isNotBlank(orgId)) {
			sql.append(" and c.ORG_ID=:orgId");
			params.put("orgId", orgId);
		}

		String title = (String) searchParams.get("LIKE_title");
		if (StringUtils.isNotBlank(title)) {
			sql.append(" and c.TITLE like :title");
			params.put("title", "%" + title + "%");
		}

		String specialtyBaseId = (String) searchParams.get("EQ_specialtyBaseId");
		if (StringUtils.isNotBlank(specialtyBaseId)) {
			sql.append(
					" and exists(select 1 from gjt_course_specialty gcs where gcs.first_course_id=c.id and gcs.specialty_base_id=:specialtyBaseId)");
			params.put("specialtyBaseId", specialtyBaseId);
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest, GjtFirstCourse.class);
	}

	@Override
	public long countFirstCourseStudentNum(String firstCourseId) {
		String sql = "select 1 from gjt_fcourse_student t where t.first_course_id=:firstCourseId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstCourseId", firstCourseId);
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	@Override
	public long countAllFirstCourseStudentNum(String firstCourseId) {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT 1");
		sql.append("  FROM GJT_STUDENT_INFO GSI");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND EXISTS");
		sql.append("  (SELECT 1");
		sql.append("  FROM GJT_FCOURSE_SPECIALTY GFS,");
		sql.append("  GJT_SPECIALTY_BASE    GSB,");
		sql.append("  GJT_SPECIALTY         GS");
		sql.append("  WHERE GFS.SPECIALTY_BASE_ID = GSB.SPECIALTY_BASE_ID");
		sql.append("  AND GSB.SPECIALTY_BASE_ID = GS.SPECIALTY_BASE_ID");
		sql.append("  AND GS.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  AND GS.IS_DELETED='N'");
		sql.append("  AND GFS.FIRST_COURSE_ID = :firstCourseId)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstCourseId", firstCourseId);
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	@Override
	public long countStudentByOrgId(String orgId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, orgId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtStudentInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtStudentInfo.class);
		return gjtStudentInfoDao.count(spec);
	}

	@Override
	public GjtFirstCourse queryBySpecialtyBaseId(String specialtyBaseId) {
		List<GjtFirstCourse> firstCourses = gjtFirstCourseDao.queryBySpecialtyBaseId(specialtyBaseId);
		if (CollectionUtils.isNotEmpty(firstCourses)) {
			return firstCourses.get(0);
		}
		return null;
	}

	@Override
	public Object queryByStudentId(String studentId) {
		return gjtFirstCourseDao.queryByStudentId(studentId);
	}

	@Override
	public List<GjtFirstCourse> queryByType(String orgId, int type) {
		return gjtFirstCourseDao.queryByType(orgId, type);
	}

	@Override
	public void saveFirstCourseStudent(String firstCourseId, String studentId) {
		gjtFirstCourseDao.saveFirstCourseStudent(firstCourseId, studentId, new Date());

	}

	@Override
	public Object queryByFourseCourseIdAndStduentId(String courseId, String studentId) {
		return gjtFirstCourseDao.queryByFourseCourseIdAndStduentId(courseId, studentId);
	}

	@Override
	public void saveFirstCourse(GjtFirstCourse gjtFirstCourse, List<String> videos, List<String> specialtyIds) {
		List<GjtSpecialtyVideo> videoList = new ArrayList<GjtSpecialtyVideo>();
		for (String video : videos) {
			JSONObject json = JSONObject.fromObject(video);
			GjtSpecialtyVideo sv = new GjtSpecialtyVideo();
			sv.setVideoId(json.get("id") == null ? UUIDUtils.random() : json.getString("id"));
			sv.setTitle(json.getString("title"));
			sv.setVideoUrl(json.getString("videoUrl"));
			videoList.add(sv);
		}
		gjtFirstCourse.setGjtSpecialtyVideoList(videoList);

		if (CollectionUtils.isEmpty(specialtyIds)) {
			gjtFirstCourse.setType(1);
		} else {
			List<GjtSpecialtyBase> specialtyList = new ArrayList<GjtSpecialtyBase>();
			for (String sid : specialtyIds) {
				GjtSpecialtyBase sb = new GjtSpecialtyBase(sid);
				specialtyList.add(sb);
			}
			gjtFirstCourse.setGjtSpecialtyBaseList(specialtyList);
		}
		save(gjtFirstCourse);
	}

	@Override
	public Page<GjtSpecialtyBase> querySpecialtyBasePage(Map<String, Object> searchParams, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select *");
		sql.append("  from GJT_SPECIALTY_BASE gsb");
		sql.append("  where gsb.is_deleted = 'N'");
		sql.append("  and gsb.XX_ID = (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  AND org.ORG_TYPE = '1'");
		sql.append("  START WITH org.ID = :orgId");
		sql.append("  CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID)");
		sql.append("  and not exists(select 1 from gjt_fcourse_specialty gfs where gfs.specialty_base_id=gsb.specialty_base_id)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", searchParams.get("orgId"));
		String specialtyLayer = (String) searchParams.get("EQ_specialtyLayer");
		if (StringUtils.isNotBlank(specialtyLayer)) {
			sql.append(" and gsb.specialtyLayer=:specialtyLayer");
			params.put("specialtyLayer", specialtyLayer);
		}
		String specialtyName = (String) searchParams.get("LIKE_specialtyName");
		if (StringUtils.isNotBlank(specialtyName)) {
			sql.append(" and gsb.specialtyName LIKE :specialtyName");
			params.put("specialtyName", "%" + specialtyName + "%");
		}
		Page<GjtSpecialtyBase> gjtSpecialtyBases = commonDao.queryForPageNative(sql.toString(), params, pageRequest, GjtSpecialtyBase.class);
		return gjtSpecialtyBases;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.graduation.GjtFirstCourseService#
	 * queryFirstCourseStduentPage(java.util.Map,
	 * org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<Map<String, Object>> queryFirstCourseStduentPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select gsi.zp              \"zp\",");
		sql.append("  gsi.xm              \"studentName\",");
		sql.append("  gsi.sjh             \"mobile\",");
		sql.append("  gs.pycc             \"pycc\",");
		sql.append("  gg.grade_name       \"gradeName\",");
		sql.append("  gy.name             \"yearName\",");
		sql.append("  gs.zymc             \"specialtyName\",");
		sql.append("  gcs.first_course_id \"fcourseId\",");
		sql.append("  gcs.crated_dt       \"viewTime\"");
		sql.append("  from gjt_student_info gsi");
		sql.append("  inner join gjt_specialty gs");
		sql.append("  on gsi.major = gs.specialty_id and gs.is_deleted='N'");
		sql.append("  inner join gjt_specialty_base gsb");
		sql.append("  on gs.specialty_base_id = gsb.specialty_base_id and gsb.is_deleted='N'");
		sql.append("  inner join gjt_fcourse_specialty gfs");
		sql.append("  on gsb.specialty_base_id = gfs.specialty_base_id");
		sql.append("  and gfs.first_course_id = :firstCourseId");
		sql.append("  inner join gjt_grade gg ");
		sql.append("  on gg.grade_id = gsi.nj and gg.is_deleted='N'");
		sql.append("  inner join gjt_year gy");
		sql.append("  on gy.grade_id = gg.year_id");
		sql.append("  left join gjt_fcourse_student gcs");
		sql.append("  on gcs.student_id = gsi.student_id");
		sql.append("  where gsi.is_deleted='N'");
		sql.append("  ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstCourseId", searchParams.get("firstCourseId"));

		return null;
	}

}
