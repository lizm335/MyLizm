/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.home.GjtMessageDataInfoDao;
import com.gzedu.xlims.dao.home.GjtMessageInfoSpecs;
import com.gzedu.xlims.dao.home.message.GjtMessageInfoDao;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.service.home.message.GjtMessageInfoService;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
@Service
public class GjtMessageInfoServiceImpl implements GjtMessageInfoService {

	private final static Logger log = LoggerFactory.getLogger(GjtMessageInfoServiceImpl.class);

	@Autowired
	private GjtMessageInfoDao gjtMessageInfoDao;

	@Autowired
	private GjtMessageDataInfoDao gjtMessageDataInfoDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager em;

	@Autowired
	private CommonDao commonDao;

	@Override
	public Page<GjtMessageInfo> querySource(GjtMessageInfo entity, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return gjtMessageInfoDao.findAll(GjtMessageInfoSpecs.findAllByMessageInfo(entity), pageRequest);
	}

	@Override
	public GjtMessageInfo saveEntity(GjtMessageInfo entity) {
		if (StringUtils.isBlank(entity.getAppSend())) {
			entity.setAppSend("0");
		}

		if (StringUtils.isBlank(entity.getPublicSend())) {
			entity.setPublicSend("0");
		}

		if (StringUtils.isBlank(entity.getIsComment())) {
			entity.setIsComment("0");
		}

		if (StringUtils.isBlank(entity.getIsLike())) {
			entity.setIsLike("0");
		}

		if (StringUtils.isBlank(entity.getIsFeedback())) {
			entity.setIsFeedback("0");
		}
		return gjtMessageInfoDao.save(entity);
	}

	@Override
	public GjtMessageInfo queryById(String id) {
		// TODO Auto-generated method stub
		return gjtMessageInfoDao.findOne(id);
	}

	@Override
	public Boolean updateEntity(GjtMessageInfo entity) {
		GjtMessageInfo gjtMessageInfo = gjtMessageInfoDao.save(entity);
		return gjtMessageInfo != null ? true : false;
	}

	@Override
	public void delete(String id) {
		gjtMessageInfoDao.updateIsDelete(id);
	}

	@Override
	public List<GjtMessageInfo> queryAll() {
		return gjtMessageInfoDao.findAll();
	}

	@Override
	public Page<GjtMessageInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		// filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ,
		// orgId));
		Specification<GjtMessageInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMessageInfo.class);
		// Order or = new Order(Direction.DESC, "createdDt");
		Pageable pageable = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		return gjtMessageInfoDao.findAll(spec, pageable);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMessageInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMessageInfo.class);
		return gjtMessageInfoDao.count(spec);
	}

	@Override
	public List<Object> queryStudent(String specialtyIds, String gradeIds, String pyccs, String orgId) {
		String sql = "select distinct gua.id ,gsi.atid ,gsi.xxzx_id,gsi.xx_id,gsi.sfzh from "
				+ " gjt_user_account  gua  inner join gjt_student_info gsi on gua.id=gsi.account_id ";

		if (StringUtils.isNotBlank(gradeIds)) {
			sql += " inner join gjt_grade gg on gsi.nj=gg.grade_id ";
		}

		if (StringUtils.isNotBlank(specialtyIds)) {
			sql += " inner join gjt_specialty gs on gs.specialty_id =gsi.major ";
		}

		sql += " where gsi.xxzx_id in (select id from gjt_org where is_deleted='N' START WITH id = :orgId CONNECT BY PRIOR id = perent_id)  ";

		if (StringUtils.isNotBlank(gradeIds)) {
			sql += " and gg.GRADE_ID in(" + gradeIds + ")";
		}
		if (StringUtils.isNotBlank(specialtyIds)) {
			sql += "  and  gs.specialty_id in(" + specialtyIds + ") ";
		}

		if (StringUtils.isNotBlank(pyccs)) {
			sql += "  and  gsi.pycc =" + pyccs;
		}

		sql += " and gsi.is_deleted = 'N' ";
		sql += " order by gua.id";
		Query query = em.createNativeQuery(sql);
		query.setParameter("orgId", orgId);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		log.info("通知公告查询学生SQL:{};参数orgId={}", sql, orgId);
		return rows;
	}

	@Override
	public List<Object> queryEmployee(String orgId, String specialtyIds, String gradeIds, String pyccs) {
		String sql = "select distinct a.id from gjt_user_account a inner join gjt_employee_info b on a.id=b.account_id"
				+ " inner join gjt_class_info c on c.bzr_id=b.employee_id ";

		if (StringUtils.isNotBlank(pyccs)) {
			sql += " inner join gjt_specialty d on d.specialty_id=c.specialty_id ";
		}

		sql += "  where a.is_deleted='N' and b.is_deleted='N' and c.is_deleted='N' and c.class_type = 'teach'";

		if (StringUtils.isNotBlank(orgId)) {
			sql += "  and b.xxzx_id in (select id from gjt_org where is_deleted='N' START WITH id = '" + orgId
					+ "' CONNECT BY PRIOR id = perent_id)  ";
		}

		if (StringUtils.isNotBlank(gradeIds)) {
			sql += "  and c.grade_id in (" + gradeIds + ") ";
		}

		if (StringUtils.isNotBlank(specialtyIds)) {
			sql += " and c.specialty_id in(" + specialtyIds + ") ";
		}
		if (StringUtils.isNotBlank(pyccs)) {
			sql += " and d.is_deleted='N' and d.pycc in (" + pyccs + ")";
		}
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		return rows;
	}

	@Override
	public Boolean updateIsRead(String messageId, String userId, String platform) {
		int i = gjtMessageInfoDao.updateIsRead(messageId, userId, platform);
		return i == 1 ? true : false;
	}

	@Override
	public List getSpecialtyByCode(Map searchParams) {
		return gjtMessageDataInfoDao.getSpecialtyByCode(searchParams);
	}

	@Override
	public List<Object> queryStudentInfo(Map searchParams) {
		return gjtMessageDataInfoDao.queryStudent(searchParams);
	}

	@Override
	public List getXXIdByCode(Map searchParams) {
		return gjtMessageDataInfoDao.getXXIdByCode(searchParams);
	}

	@Override
	public List<Map<String, Object>> queryStudentByGradeIdCourseId(Map<String, Object> params) {

		StringBuffer sql = new StringBuffer();
		sql.append("  select distinct b.account_id id, b.atid, b.xxzx_id, b.xx_id, b.sfzh");
		sql.append("  from gjt_rec_result a");
		sql.append("  inner join gjt_student_info b");
		sql.append("  on a.student_id = b.student_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  and a.course_id in(:courseIds)");
		sql.append("  and a.term_id in(:gradeIds)");

		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public long findByTypeClassify(String typeClassify) {
		long count = gjtMessageInfoDao.findByTypeClassifyCount(typeClassify);
		return count;
	}
}
