package com.gzedu.xlims.dao.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamSubjectNewRepository;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;

@Repository
public class GjtExamSubjectNewDao {
	
	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Autowired
	GjtExamSubjectNewRepository gjtExamSubjectNewRepository;

	public GjtExamSubjectNew findOne(String id) {
		return gjtExamSubjectNewRepository.findOne(id);
	}

	public List<GjtExamSubjectNew> findAll(Specification<GjtExamSubjectNew> spec) {
		return gjtExamSubjectNewRepository.findAll(spec);
	}

	public Page<GjtExamSubjectNew> findAll(Specification<GjtExamSubjectNew> spec, PageRequest pageRequst) {
		return gjtExamSubjectNewRepository.findAll(spec, pageRequst);
	}

	public GjtExamSubjectNew save(GjtExamSubjectNew entity) {
		return gjtExamSubjectNewRepository.save(entity);
	}

	public GjtExamSubjectNew queryBy(String id) {
		return gjtExamSubjectNewRepository.findOne(id);
	}

	public List<GjtExamSubjectNew> save(List<GjtExamSubjectNew> list) {
		return gjtExamSubjectNewRepository.save(list);
	}

	public List<GjtCourse> noSubjectCourseList(String xxid) {
		return gjtExamSubjectNewRepository.noSubjectCourseList(xxid);
	}

	public GjtExamSubjectNew findByXxidAndKchAndType(String xxid, String kch, int type) {
		return gjtExamSubjectNewRepository.findByXxIdAndKchAndType(xxid, kch, type);
	}

	@Transactional
	public int deleteGjtExamSubjectNew(List<String> ids, String xxid) {
		int rs = 0;
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("update " + "				GJT_EXAM_SUBJECT_NEW " + "			set "
					+ "				IS_DELETED=1" + "			where " + "				STATUS=0 and XX_ID='");
//					+ "				IS_DELETED='N'" + "			where " + "				STATUS=0 and XX_ID='");
			sbuilder.append(xxid);
			sbuilder.append("' ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" and SUBJECT_ID in ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");

				Query query = em.createNativeQuery(sbuilder.toString());
				rs = query.executeUpdate();
			}
		}
		return rs;
	}

	/**
	 * 根据课程号与考试类型获取对应考试科目列表
	 * 
	 * @param khcs
	 * @param type
	 * @return key:courseid, value: GjtExamSubjectNew
	 */
	public Map<String, GjtExamSubjectNew> existList(List<String> khcs, int type) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("select gesn.* from " + "GJT_EXAM_SUBJECT_NEW gesn " + "left join "
				+ "gjt_course gc on gesn.course_id=gc.course_id where gesn.type=");
		sbuilder.append(type);
		if (null != khcs && khcs.size() > 0) {
			sbuilder.append(" and gc.kch in ('");
			sbuilder.append(khcs.get(0));
			sbuilder.append("'");
			for (int i = 1; i < khcs.size(); i++) {
				sbuilder.append(", '");
				sbuilder.append(khcs.get(i));
				sbuilder.append("'");
			}
			sbuilder.append(")");
		}
		Query query = em.createNativeQuery(sbuilder.toString(), GjtExamSubjectNew.class);
		List<GjtExamSubjectNew> resultList = query.getResultList();
		Map<String, GjtExamSubjectNew> map = new HashMap<String, GjtExamSubjectNew>();
		for (GjtExamSubjectNew gjtExamSubjectNew : resultList) {
			// map.put(gjtExamSubjectNew.getCourseId(),
			// gjtExamSubjectNew);//2016-10-25 数据结构改变...
			map.put(gjtExamSubjectNew.getKch(), gjtExamSubjectNew);
		}

		return map;
	}
	
	/**
	 * 根据课程号获取对应考试科目列表
	 * 
	 * @param khcs
	 * @return key:courseid, value: GjtExamSubjectNew
	 */
	public Map<String, GjtExamSubjectNew> existList(List<String> khcs, String xxid) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("select gesn.* from " + "GJT_EXAM_SUBJECT_NEW gesn " + "left join "
				+ "gjt_course gc on gesn.course_id=gc.course_id where gesn.xx_id='");
		sbuilder.append(xxid);
		sbuilder.append("'");
		if (null != khcs && khcs.size() > 0) {
			sbuilder.append(" and gc.kch in ('");
			sbuilder.append(khcs.get(0));
			sbuilder.append("'");
			for (int i = 1; i < khcs.size(); i++) {
				sbuilder.append(", '");
				sbuilder.append(khcs.get(i));
				sbuilder.append("'");
			}
			sbuilder.append(")");
		}
		Query query = em.createNativeQuery(sbuilder.toString(), GjtExamSubjectNew.class);
		List<GjtExamSubjectNew> resultList = query.getResultList();
		Map<String, GjtExamSubjectNew> map = new HashMap<String, GjtExamSubjectNew>();
		for (GjtExamSubjectNew gjtExamSubjectNew : resultList) {
//			map.put(gjtExamSubjectNew.getCourseId(), gjtExamSubjectNew);//2016-10-25 数据结构改变...
			map.put(gjtExamSubjectNew.getKch(), gjtExamSubjectNew);
		}

		return map;
	}
	
	/**
	 * 获取已存在的试卷号
	 * 
	 * @param examNoList
	 * @return
	 */
	public List<String> existExamNoList(String xxid, int type) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("select gesn.exam_no from GJT_EXAM_SUBJECT_NEW gesn where gesn.is_deleted = 0 and gesn.xx_id='");
//		sbuilder.append("select gesn.exam_no from GJT_EXAM_SUBJECT_NEW gesn where gesn.is_deleted = 'N' and gesn.xx_id='");
		sbuilder.append(xxid);
		sbuilder.append("' and gesn.type=");
		sbuilder.append(type);
		
		Query query = em.createNativeQuery(sbuilder.toString());
		return query.getResultList();
	}

	public Map<String, String> plansCountBySubject(List<String> ids) {
		Map<String, String> map = new HashMap<String, String>();
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("select " + "				subject_code, count(subject_code) " + "			from "
					+ "				GJT_EXAM_PLAN_NEW ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" where subject_code in  ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");
			}
			sbuilder.append(" group by subject_code");
			Query query = em.createNativeQuery(sbuilder.toString());
			List<Object[]> resultList = query.getResultList();

			for (Object[] obj : resultList) {
				map.put(obj[0].toString(), obj[1].toString());
			}
		}
		return map;
	}
	
	public List<GjtCourse> findCourseByXxid(String xxid, String ksfs) {
		String sql = "select gplan.COURSE_ID, gplan.COURSE_CODE as KCH, gcourse.KCMC, gplan.XX_ID "
				+ "		from VIEW_TEACH_PLAN gplan "
				+ "		left join gjt_course gcourse on gplan.course_id=gcourse.course_id "
				+ "		where gplan.is_deleted = 'N' and gplan.xx_id=:xxid and gplan.ksfs=:ksfs and gplan.COURSE_CODE is not null";
		Query query = em.createNativeQuery(sql);
		query.setParameter("xxid", xxid);
		query.setParameter("ksfs", ksfs);
		List<Object[]> resultList = query.getResultList();
		List<GjtCourse> courseList = new ArrayList<GjtCourse>();
		for (int i = 0; i < resultList.size(); i++) {
			Object[] arr = resultList.get(i);
			GjtCourse course = new GjtCourse();
			course.setCourseId((String) arr[0]);
			course.setKch((String) arr[1]);
			course.setKcmc((String) arr[2]);
			GjtOrg org = new GjtOrg();
			org.setId((String) arr[3]);
			course.setGjtOrg(org);
			courseList.add(course);
		}
		return courseList;
	}

	public GjtExamSubjectNew getEaxmSubjectByKch(String kch) {
		String sql = "select * from gjt_exam_subject_new where kch=:kch";
		Query query = em.createNativeQuery(sql, GjtExamSubjectNew.class);
		query.setParameter("kch", kch);
		List<GjtExamSubjectNew> list = query.getResultList();
		GjtExamSubjectNew subject = null;
		if (null != list && list.size() > 0) {
			subject = list.get(0);
		}
		return subject;
	}
	
	public List<Map<String, String>> findTeachPlanByXxid(String xxid, String ksfs) {
		String sql = "	select "
				+ "			gplan.COURSE_CODE as COURSE_CODE, gplan.COURSE_ID as COURSE_ID, gcourse.kcmc as KCMC"
				+ "		from "
				+ "			VIEW_TEACH_PLAN gplan "
				+ "		left join gjt_course gcourse on gplan.course_id=gcourse.course_id "
				+ "		where "
				+ "			gplan.is_deleted='N' and gplan.xx_id=:xxid and gplan.ksfs=:ksfs and gplan.course_code is not null"
				+ "			group by gplan.COURSE_CODE, gplan.COURSE_ID, gcourse.kcmc";
		Query query = em.createNativeQuery(sql);
		query.setParameter("xxid", xxid);
		query.setParameter("ksfs", ksfs);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();
	}
	
	public List<Map<String, String>> noSubjectPlanList(String xxid, String ksfs) {
		String sql = "	select "
				+ "			gplan.COURSE_CODE as COURSE_CODE, gplan.COURSE_ID as COURSE_ID, gcourse.kcmc as KCMC"
				+ "		from " + "			VIEW_TEACH_PLAN gplan "
				+ "		left join gjt_course gcourse on gplan.course_id=gcourse.course_id "
				+ "		where "
				+ "			gplan.is_deleted='N' and gplan.xx_id=:xxid and gplan.course_code is not null and gplan.ksfs=:ksfs and gplan.course_code not in "
				+ "			(select gsubject.kch from gjt_exam_subject_new gsubject where gsubject.is_deleted=0 and gsubject.xx_id=:xxid) "
//				+ "			(select gsubject.kch from gjt_exam_subject_new gsubject where gsubject.is_deleted='N' and gsubject.xx_id=:xxid) "
				+ "			group by gplan.COURSE_CODE, gplan.COURSE_ID, gcourse.kcmc";
		Query query = em.createNativeQuery(sql);
		query.setParameter("xxid", xxid);
		query.setParameter("ksfs", ksfs);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();
	}

	public GjtExamSubjectNew isExamSubjectExist(GjtExamSubjectNew entity) {
		String sql = "	select "
				+ "			* "
				+ "		from "
				+ "			gjt_exam_subject_new "
				+ "		where "
				+ "			exam_no=:examNo and type=:type and xx_id=:xxid and is_deleted=0";
//				+ "			exam_no=:examNo and type=:type and xx_id=:xxid and is_deleted='N'";
		Query query = em.createNativeQuery(sql, GjtExamSubjectNew.class);
		query.setParameter("examNo", entity.getExamNo());
		query.setParameter("type", entity.getType());
		query.setParameter("xxid", entity.getXxId());
		List<GjtExamSubjectNew> list = query.getResultList();
		GjtExamSubjectNew subject = null;
		if (null != list && list.size() > 0) {
			subject = list.get(0);
		}
		return subject;
	}
	
	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, int isDeleted) {
		return gjtExamSubjectNewRepository.findByTypeAndXxIdAndIsDeleted(type, xxId, isDeleted);
	}
//	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, String isDeleted) {
//		return gjtExamSubjectNewRepository.findByTypeAndXxIdAndIsDeleted(type, xxId, isDeleted);
//	}
	
	public Map<String, String> queryTeachPlanBySubject(List<String> ids) {
		Map<String, String> map = new HashMap<String, String>();
		if (ids != null && ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("select distinct SUBJECT_ID, COURSE_CODE	from  VIEW_TEACH_PLAN ");
			sbuilder.append(" where SUBJECT_ID in  ('");
			sbuilder.append(ids.get(0));
			sbuilder.append("'");
			for (int i = 1; i < ids.size(); i++) {
				sbuilder.append(", '");
				sbuilder.append(ids.get(i));
				sbuilder.append("'");
			}
			sbuilder.append(")");
			Query query = em.createNativeQuery(sbuilder.toString());
			List<Object[]> resultList = query.getResultList();

			for (Object[] obj : resultList) {
				if (obj[1] != null) {
					if (map.containsKey(obj[0].toString())) {
						map.put(obj[0].toString(), map.get(obj[0].toString()) + ", " + obj[1].toString());
					} else {
						map.put(obj[0].toString(), obj[1].toString());
					}
				}
			}
		}
		return map;
	} 
	
	/**
	 * 获得课程是否存在
	 * @return
	 */
	public List getCourseExamNo(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GCE.COURSE_ID,");
		sql.append("  (SELECT COUNT(GES.SUBJECT_ID)");
		sql.append("  FROM GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  WHERE GES.IS_DELETED = 0");
		sql.append("  AND GES.TYPE = :TYPE");
		sql.append("  AND GES.COURSE_ID = GCE.COURSE_ID) SUBJECT_COUNT,");
		sql.append("  (SELECT GES.SUBJECT_CODE");
		sql.append("  FROM GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  WHERE GES.IS_DELETED = 0");
		sql.append("  AND GES.EXAM_NO = :EXAM_NO");
		sql.append("  AND ROWNUM = 1) SUBJECT_CODE");
		sql.append("  FROM GJT_COURSE GCE");
		sql.append("  WHERE GCE.IS_DELETED = 'N'");
		sql.append("  AND GCE.XX_ID = :XX_ID");
		sql.append("  AND GCE.KCH = :KCH");
		
		param.put("TYPE", ObjectUtils.toString(searchParams.get("examType")));
		param.put("EXAM_NO", ObjectUtils.toString(searchParams.get("examNo")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("xxId")));
		param.put("KCH", ObjectUtils.toString(searchParams.get("kch")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
}
