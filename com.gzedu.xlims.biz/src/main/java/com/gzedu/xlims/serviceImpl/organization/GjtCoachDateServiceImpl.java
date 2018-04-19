/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtCoachDateDao;
import com.gzedu.xlims.pojo.openClass.GjtCoachDate;
import com.gzedu.xlims.service.organization.GjtCoachDateService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月24日
 * @version 3.0
 *
 */

@Service
public class GjtCoachDateServiceImpl extends BaseServiceImpl<GjtCoachDate> implements GjtCoachDateService {

	@Autowired
	private GjtCoachDateDao gjtCoachDateDao;

	@Override
	protected BaseDao<GjtCoachDate, String> getBaseDao() {
		return gjtCoachDateDao;
	}

	@Autowired
	private CommonDao commonDao;

	@Override
	public Page<Map<String, Object>> findCoachDateList(Map<String, Object> searchParams, PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> param=new HashMap<String, Object>();
		sql.append("  select gcd.coach_data_id \"dataId\",");
		sql.append("  gcd.data_name     \"dataName\",");
		sql.append("  gcd.data_type     \"dataType\",");
		sql.append("  gcd.data_path     \"dataPath\",");
		sql.append("  gcd.data_label     \"dataLabel\",");
		sql.append("  gcd.download_num  \"downloadNum\",");
		sql.append("  gcd.created_dt \"shareTime\", ");
		sql.append("  gua.real_name \"sharePerson\",");
		sql.append("  gg.grade_name \"term\",");
		sql.append("  gc.kcmc \"courseName\",");
		sql.append("  gc.kch \"courseCode\",");
		sql.append("  (select count(1) from gjt_rec_result grr where grr.is_deleted='N' and grr.termcourse_id=gcd.term_course_id) \"empCount\"");
		sql.append("  from GJT_COACH_DATE      gcd,");
		sql.append("  gjt_term_courseinfo gtc,");
		sql.append("  gjt_user_account    gua,");
		sql.append("  gjt_grade           gg,");
		sql.append("  gjt_course          gc");
		sql.append("  where gcd.term_course_id = gtc.termcourse_id");
		sql.append("  and gcd.created_by = gua.id");
		sql.append("  and gtc.term_id = gg.grade_id");
		sql.append("  and gtc.course_id = gc.course_id");
		sql.append("  and gcd.is_deleted = 'N'");
		sql.append("  and gtc.is_deleted = 'N'");
		String orgId = (String) searchParams.get("EQ_orgId");
		sql.append(" and gcd.org_id=:orgId");
		param.put("orgId", orgId);
		String createdBy = (String) searchParams.get("EQ_createdBy");
		if (StringUtils.isNoneBlank(createdBy)) {
			sql.append(" and gcd.created_by=:createdBy");
			param.put("createdBy", createdBy);
		}
		String gradeId = (String) searchParams.get("EQ_gradeId");
		if (StringUtils.isNoneBlank(gradeId)) {
			sql.append(" and gtc.term_id=:gradeId");
			param.put("gradeId", gradeId);
		}

		String course = (String) searchParams.get("LIKE_course");
		if (StringUtils.isNotBlank(course)) {
			sql.append(" and (gc.kch = :kch OR gc.kcmc like :course) ");
			param.put("kch", course);
			param.put("course", "%" + course + "%");
		}

		String dataType = (String) searchParams.get("EQ_dataType");
		if (StringUtils.isNotBlank(dataType)) {
			sql.append(" and gcd.data_type=:dataType ");
			param.put("dataType", dataType);
		}
		
		String dataName = (String) searchParams.get("LIKE_dataName");
		if (StringUtils.isNotBlank(dataName)) {
			sql.append(" and gcd.data_name like :dataName ");
			param.put("dataName", "%" + dataName + "%");
		}
		sql.append(" order by gcd.created_dt desc ");
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	@Override
	@Transactional
	public void createCoachDate(GjtCoachDate gjtCoachDate, List<String> termCourseIds) {
		gjtCoachDate.setIsDeleted("N");
		gjtCoachDate.setCreatedDt(new Date());
		String dataPath = gjtCoachDate.getDataPath();
		int index = dataPath.lastIndexOf(".");
		gjtCoachDate.setDataType(dataPath.substring(index + 1));
		
		for (String termCourseId : termCourseIds) {
			GjtCoachDate data = new GjtCoachDate();
			BeanUtils.copyProperties(gjtCoachDate, data);
			data.setCoachDataId(UUIDUtils.random());
			data.setTermCourseId(termCourseId);
			save(data);
		}

	}

	@Override
	public List<GjtCoachDate> queryByTermCourseId(String termCourseId) {
		return gjtCoachDateDao.queryByTermCourseId(termCourseId);
	}

}
