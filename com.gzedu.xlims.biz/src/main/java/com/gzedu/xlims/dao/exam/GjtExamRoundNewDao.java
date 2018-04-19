package com.gzedu.xlims.dao.exam;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.exam.repository.GjtExamRoundNewRepository;
import com.gzedu.xlims.pojo.exam.GjtExamRoundNew;

@Repository
public class GjtExamRoundNewDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Autowired
	private GjtExamRoundNewRepository gjtExamRoundNewRepository;

	public Page<GjtExamRoundNew> findAll(Specification<GjtExamRoundNew> spec, PageRequest pageRequst) {
		return gjtExamRoundNewRepository.findAll(spec, pageRequst);
	}

	public List<GjtExamRoundNew> findAll(Specification<GjtExamRoundNew> spec) {
		return gjtExamRoundNewRepository.findAll(spec);
	}

	public GjtExamRoundNew queryBy(String id) {
		return gjtExamRoundNewRepository.findOne(id);
	}

	public List<GjtExamRoundNew> save(List<GjtExamRoundNew> list) {
		return gjtExamRoundNewRepository.save(list);
	}

	public void save(GjtExamRoundNew entity) {
		gjtExamRoundNewRepository.save(entity);
	}

	public void deleteByExamBatchCodeAndExamPointId(String examBatchCode, String examPointId) {
		List<GjtExamRoundNew> list = gjtExamRoundNewRepository.findByExamBatchCodeAndExamPointId(examBatchCode,
				examPointId);
		gjtExamRoundNewRepository.delete(list);
	}

	@Transactional
	public int deleteGjtExamRoundNew(List<String> ids, String xxid) {
		int rs = 0;
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("delete from " + "				GJT_EXAM_ROUND_NEW " + "			where "
					+ "				XX_ID='");
			sbuilder.append(xxid);
			sbuilder.append("' ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" and EXAM_ROUND_ID in ('");
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
}
