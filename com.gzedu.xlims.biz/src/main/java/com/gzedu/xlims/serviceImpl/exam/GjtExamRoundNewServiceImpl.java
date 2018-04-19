package com.gzedu.xlims.serviceImpl.exam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.exam.GjtExamRoundNewDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoundNew;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamRoundNewService;

@Service
public class GjtExamRoundNewServiceImpl implements GjtExamRoundNewService {

	private static final Log log = LogFactory.getLog(GjtExamRoundNewServiceImpl.class);

	@Autowired
	private GjtExamRoundNewDao gjtExamRoundNewDao;
	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Override
	public Page<GjtExamRoundNew> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));
		Specification<GjtExamRoundNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamRoundNew.class);
		return gjtExamRoundNewDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtExamRoundNew queryBy(String id) {
		return gjtExamRoundNewDao.queryBy(id);
	}

	@Override
	public int delete(List<String> ids, String xxid) {
		return gjtExamRoundNewDao.deleteGjtExamRoundNew(ids, xxid);
	}

	@Override
	public int createExamRounds(GjtExamBatchNew batch, String roomids, GjtUserAccount user) {
		int rs = 0;
		List<GjtExamRoundNew> list = new ArrayList<GjtExamRoundNew>();
		GjtExamRoundNew round;
		Date now = new Date();
		String[] idArr = roomids.split(",");
		if (idArr.length > 0) {
			for (int i = 0; i < idArr.length; i++) {
				round = new GjtExamRoundNew();
				round.setExamRoundId(UUIDUtils.random());
				round.setCreatedBy(user.getId());
				round.setCreatedDt(now);
				round.setUpdatedBy(user.getId());
				round.setUpdatedDt(now);
				round.setXxId(user.getGjtOrg().getId());
				round.setRoundIndex((int) codeGeneratorService.incrRoundIndex(batch.getExamBatchCode()));
				round.setExamRoomId(idArr[i]);
				// 开始&结束时间
				if (null != batch.getOnlineSt() && null != batch.getOnlineEnd()) {
					round.setStartTime(batch.getOnlineSt());
					round.setEndTime(batch.getOnlineEnd());
				}
				if (null != batch.getOfflineSt() && null != batch.getOfflineEnd()) {
					if (batch.getOfflineSt().before(round.getStartTime())) {
						round.setStartTime(batch.getOfflineSt());
					}
					if (batch.getOfflineEnd().after(round.getEndTime())) {
						round.setEndTime(batch.getOfflineEnd());
					}
				}

				list.add(round);
			}
			try {
				gjtExamRoundNewDao.save(list);
				rs = list.size();
			} catch (Exception e) {
				log.error("rounds batch insert error.");
				log.error(e.getMessage(), e);
			}
		}
		return rs;
	}

	@Override
	public List<GjtExamRoundNew> queryGjtExamRoundNewsBy(String batchCode, String pointId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("examBatchCode", new SearchFilter("examBatchCode", Operator.EQ, batchCode));
		filters.put("examPointId", new SearchFilter("examPointId", Operator.EQ, pointId));
		Specification<GjtExamRoundNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamRoundNew.class);
		return gjtExamRoundNewDao.findAll(spec);
	}

	@Override
	public void insert(GjtExamRoundNew entity) {
		entity.setExamRoundId(UUIDUtils.random().toString());
		gjtExamRoundNewDao.save(entity);
	}

}
