package com.gzedu.xlims.dao.textbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookDistributeDetailRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;

@Repository
public class GjtTextbookDistributeDetailDao {

	@Autowired
	private GjtTextbookDistributeDetailRepository gjtTextbookDistributeDetailRepository;

	@Autowired
	private CommonDao commonDao;

	public GjtTextbookDistributeDetail save(GjtTextbookDistributeDetail entity) {
		return gjtTextbookDistributeDetailRepository.save(entity);
	}

	public List<GjtTextbookDistributeDetail> save(List<GjtTextbookDistributeDetail> entities) {
		return gjtTextbookDistributeDetailRepository.save(entities);
	}

	public Page<GjtTextbookDistributeDetail> findAll(Specification<GjtTextbookDistributeDetail> spec,
			PageRequest pageRequst) {
		return gjtTextbookDistributeDetailRepository.findAll(spec, pageRequst);
	}

	public List<GjtTextbookDistributeDetail> findAll(Specification<GjtTextbookDistributeDetail> spec) {
		return gjtTextbookDistributeDetailRepository.findAll(spec);
	}

	@Transactional
	public void updateStatus(String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  update GJT_TEXTBOOK_DISTRIBUTE_DETAIL");
		sql.append("  set status = '3'");
		sql.append("  where status = '2'");
		sql.append("  and DISTRIBUTE_ID in");
		sql.append("  (select DISTRIBUTE_ID");
		sql.append("  from GJT_TEXTBOOK_DISTRIBUTE a");
		sql.append("  where a.status = '2'");
		sql.append("  and a.distribution_dt < to_date(:date, 'yyyy-mm-dd'))");
		sql.append("  ");

		map.put("date", date);

		commonDao.updateForMapNative(sql.toString(), map);
	}
}
