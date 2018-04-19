package com.gzedu.xlims.dao.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointNewRepository;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;

@Component
public class GjtExamPointNewDao extends BaseDaoImpl {

	@Autowired
	GjtExamPointNewRepository gjtExamPointRepository;

	@Autowired
	CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;
	
	public List<GjtExamPointNew> queryAll(){
		return gjtExamPointRepository.findAll();
	}

	/**
	 * 
	 * @param searchParams中key的格式为OPERATOR_FIELDNAME
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtExamPointNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {

		List<SearchFilter> filters = SearchFilter.parse2(searchParams);
		Specification<GjtExamPointNew> spec = DynamicSpecifications.bySearchFilter(filters, GjtExamPointNew.class);

		return gjtExamPointRepository.findAll(spec, pageRequst);
	}

	public List<GjtExamPointNew> findByExamBatchCode(String examBatchCode) {
		return gjtExamPointRepository.findByExamBatchCodeAndIsDeleted(examBatchCode, Constants.BOOLEAN_NO);
	}

	public GjtExamPointNew queryById(String id) {
		return gjtExamPointRepository.findOne(id);
	}

	public GjtExamPointNew save(GjtExamPointNew entity) {
		return gjtExamPointRepository.save(entity);
	}

	public void delete(List<String> ids) {
		gjtExamPointRepository.delete(gjtExamPointRepository.findAll(ids));
	}

	public List<String> appointStudentidList(String pointid, String studyyearid) {
		String sql = "select student_id from GJT_EXAM_POINT_APPOINTMENT_NEW where study_year_id=:studyyearid and exam_point_id=:pointid";
		Query query = em.createNativeQuery(sql);
		query.setParameter("studyyearid", studyyearid);
		query.setParameter("pointid", pointid);
		List<String> resultList = query.getResultList();
		return resultList;
	}

	public int totalSeatsByPoint(String pointid) {
		String sql = "select sum(seats) from gjt_exam_room_new where status=1 and is_deleted=0 and exam_point_id=:pointid";
		Query query = em.createNativeQuery(sql);
		query.setParameter("pointid", pointid);
		return query.getFirstResult();
	}

	/**
	 * 保存考点与学习中心对应信息
	 * @param examPlanId
	 * @return
	 */
	public boolean existsExamPointStudyCenter(String examPointId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM GJT_EXAM_POINT_NEW_STUDYCENTER WHERE EXAM_POINT_ID=:EXAM_POINT_ID");

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("EXAM_POINT_ID", examPointId);
			return commonDao.queryForCountNative(sql.toString(), params) > 0;
		}catch (TransactionSystemException e) {

		}
		return true;
	}
	
	/**
	 * 保存考点与学习中心对应信息
	 * @param params
	 * @return
	 */
	@Transactional
	public int insertExamPointStudyCenter(Map<String, Object> params){
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_POINT_NEW_STUDYCENTER(");
		sql.append("  			EXAM_POINT_ID,");
		sql.append("  			STUDY_CENTER_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:EXAM_POINT_ID,");
		sql.append("  		:STUDY_CENTER_ID");
		sql.append("  	)");

		int  re =0 ;
		try {
			re = commonDao.insertForMapNative(sql.toString(), params);
		}catch (TransactionSystemException e){
			e.printStackTrace();
		}
		return re;
	}
}
