package com.gzedu.xlims.serviceImpl.transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.transaction.GjtExemptExamInfoAuditDao;
import com.gzedu.xlims.dao.transaction.GjtExemptExamInfoDao;
import com.gzedu.xlims.dao.transaction.GjtExemptExamProveDao;
import com.gzedu.xlims.pojo.GjtExemptExamInfo;
import com.gzedu.xlims.pojo.GjtExemptExamInfoAudit;
import com.gzedu.xlims.pojo.GjtExemptExamProve;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.service.transaction.GjtExemptExamInfoService;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月28日
 * @version 2.5
 */
@Service
public class GjtExemptExamInfoServiceImpl implements GjtExemptExamInfoService{
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamInfoServiceImpl.class);
	
	@Autowired
	private GjtExemptExamInfoDao gjtExemptExamInfoDao;
	
	@Autowired
	private GjtExemptExamInfoAuditDao gjtExemptExamInfoAuditDao;
	
	@Autowired
	private GjtExemptExamProveDao gjtExemptExamProveDao;
	
	@Autowired
	private GjtOrgDao gjtOrgDao;
	
	@Autowired
	private GjtRecResultDao gjtRecResultDao;
	
	@Autowired
	private CommonDao commonDao;

	@Override
	@Transactional
	public boolean insertExemptExamInfo(Map<String, Object> applyExemptExamMap, String[] materialId, String[] awardDate, String[] awardUnit, String[] url) {
		try {
			//新增学员申请免修免考明细
			GjtExemptExamInfo exemptExamInfo = new GjtExemptExamInfo();
			String exemptExamId=UUIDUtils.random();
			exemptExamInfo.setExemptExamId(exemptExamId);
			exemptExamInfo.setCourseId(ObjectUtils.toString(applyExemptExamMap.get("courseId")));
			exemptExamInfo.setStudentId(applyExemptExamMap.get("studentId").toString());
			exemptExamInfo.setSign(ObjectUtils.toString(applyExemptExamMap.get("sign")));
			exemptExamInfo.setAuditStatus("0");	
			exemptExamInfo.setIsApplay("1");
			exemptExamInfo.setAuditOperatorRole("5");//学籍科
			gjtExemptExamInfoDao.save(exemptExamInfo);
			//为学员新增一条审核记录，默认为已通过
			GjtExemptExamInfoAudit audit1=new GjtExemptExamInfoAudit();
			audit1.setId(UUIDUtils.random());
			audit1.setExemptExamId(exemptExamId);
			audit1.setStudentId(applyExemptExamMap.get("studentId").toString());
			audit1.setAuditState("1");
			audit1.setAuditOperatorRole("1");
			audit1.setAuditDt(new Date());
			gjtExemptExamInfoAuditDao.save(audit1);
			//为学员新增一条审核记录，默认为待审核
			GjtExemptExamInfoAudit audit2=new GjtExemptExamInfoAudit();
			audit2.setId(UUIDUtils.random());
			audit2.setExemptExamId(exemptExamId);
			audit2.setStudentId(applyExemptExamMap.get("studentId").toString());
			audit2.setAuditState("0");
			audit2.setAuditOperatorRole("5");
			gjtExemptExamInfoAuditDao.save(audit2);
			//新增学员申请免修免考所提交的材料证明信息
			GjtExemptExamProve prove=new GjtExemptExamProve();
			for(int i=0;i<materialId.length;i++){
				prove.setApplyId(UUIDUtils.random());
				prove.setExemptExamId(exemptExamId);
				prove.setMaterialId(materialId[i]);
				prove.setStudentId(applyExemptExamMap.get("studentId").toString());
				prove.setAwardDate(awardDate[i]);
				prove.setAwardUnit(awardUnit[i]);
				prove.setUrl(url[i]);
				gjtExemptExamProveDao.save(prove);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public Page<GjtExemptExamInfo> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted",new SearchFilter("isDeleted", Operator.EQ, "N"));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
		filters.put("gjtStudentInfo.gjtStudyCenter.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtStudyCenter.gjtOrg.id", SearchFilter.Operator.IN, orgList));
		Specification<GjtExemptExamInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),GjtExemptExamInfo.class);
		return gjtExemptExamInfoDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtExemptExamInfo> findByStudentIdAndCourseIdOrderByCreatedDtDesc(String studentId, String courseId) {
		return gjtExemptExamInfoDao.findByStudentIdAndCourseIdOrderByCreatedDtDesc(studentId,courseId);
	}

	@Override
	public boolean exemptExamAudit(Map<String, Object> data) {
		try {
			Date now=new Date();
			//查询学员申请免修免考信息
			GjtExemptExamInfo info =gjtExemptExamInfoDao.findOne(ObjectUtils.toString(data.get("exemptExamId")));
			info.setAuditStatus(ObjectUtils.toString(data.get("auditState")));
			info.setUpdatedDt(now);
			gjtExemptExamInfoDao.save(info);
			List<GjtExemptExamInfoAudit> auditList=gjtExemptExamInfoAuditDao.findByExemptExamIdOrderByAuditDtAsc(ObjectUtils.toString(data.get("exemptExamId")));
			GjtExemptExamInfoAudit audit=auditList.get(auditList.size()-1);
			audit.setAuditState(ObjectUtils.toString(data.get("auditState")));
			audit.setAuditOperator(ObjectUtils.toString(data.get("roleName")));
			audit.setAuditDt(now);
			audit.setAuditContent(ObjectUtils.toString(data.get("auditContent")));
			gjtExemptExamInfoAuditDao.save(audit);
			if("1".equals(ObjectUtils.toString(data.get("auditState")))){
				GjtRecResult gjtRecResult=gjtRecResultDao.
						findByCourseIdAndStudentIdAndIsDeleted(ObjectUtils.toString(data.get("courseId")), ObjectUtils.toString(data.get("studentId")),Constants.BOOLEAN_NO);
				if(gjtRecResult!=null){
					gjtRecResult.setIsOver("3");
					gjtRecResultDao.save(gjtRecResult);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public long queryTotalNum(String xxId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id", SearchFilter.Operator.EQ, xxId));
		Specification<GjtExemptExamInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtExemptExamInfo.class);
		return gjtExemptExamInfoDao.count(spec);
	}

	@Override
	public long queryByStatusTotalNum(String xxId, int auditState, String auditOperatorRole) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("auditStatus", new SearchFilter("auditStatus", Operator.EQ, auditState));//审核状态
		filters.put("auditOperatorRole", new SearchFilter("auditOperatorRole", Operator.EQ, auditOperatorRole));//审核人角色
		filters.put("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id", SearchFilter.Operator.EQ, xxId));
		Specification<GjtExemptExamInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtExemptExamInfo.class);
		return gjtExemptExamInfoDao.count(spec);
	}

	@Override
	public Map<String, Object> queryExemptExamInfo(String exemptExamId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gsi.xm as \"xm\", ");
		sql.append("  gsi.xh as \"xh\", ");
		sql.append("  go.org_name as \"orgName\", ");
		sql.append("  nvl(gsi.exedumajor,' ') as \"exedumajor\", ");
		sql.append("  nvl(gsi.exedulevel,' ') as \"exedulevel\", ");
		sql.append("  geep.award_unit as \"awardUnit\", ");
		sql.append("  geep.award_date as \"awardDate\", ");
		sql.append("  gs.zymc as \"zymc\", ");
		sql.append("  gg.grade_name as \"gradeName\", ");
		sql.append("  geem.material_name as \"materialName\", ");
		sql.append("  (select tsd.name");
		sql.append("  from tbl_sys_data tsd");
		sql.append("  where tsd.type_code = 'TrainingLevel'");
		sql.append("  and tsd.is_deleted = 'N'");
		sql.append("  and tsd.code = gsi.pycc) as \"studentPycc\", ");
		sql.append("  gc.credit as \"credit\", ");
		sql.append("  gc.kcmc as \"courseName\",");
		sql.append("  (select tsd.name");
		sql.append("  from tbl_sys_data tsd");
		sql.append("  where tsd.type_code = 'TrainingLevel'");
		sql.append("  and tsd.is_deleted = 'N'");
		sql.append("  and tsd.code = gc.pycc) as \"coursePycc\" ");
		sql.append("  from gjt_exempt_exam_info geei");
		sql.append("  left join gjt_student_info gsi");
		sql.append("  on gsi.student_id = geei.student_id");
		sql.append("  left join gjt_exempt_exam_prove geep");
		sql.append("  on geep.exempt_exam_id = geei.exempt_exam_id");
		sql.append("  left join gjt_exempt_exam_material geem");
		sql.append("  on geep.material_id = geem.id");
		sql.append("  and geem.is_deleted = 'N'");
		sql.append("  left join gjt_specialty gs");
		sql.append("  on gs.specialty_id = gsi.major");
		sql.append("  and gs.is_deleted = 'N'");
		sql.append("  left join gjt_grade gg");
		sql.append("  on gg.grade_id = gsi.nj");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  left join gjt_course gc");
		sql.append("  on gc.course_id = geei.course_id");
		sql.append("  and gc.is_deleted = 'N'");
		sql.append("  left join gjt_org go");
		sql.append("  on go.id = gsi.xx_id");
		sql.append("  and go.is_deleted = 'N'");
		sql.append("  where geei.exempt_exam_id = :exempt_exam_id");

		map.put("exempt_exam_id", exemptExamId);		
		return commonDao.queryObjectToMapNative(sql.toString(), map);
	}
}
