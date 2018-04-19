package com.gzedu.xlims.serviceImpl.textbook;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.dao.textbook.GjtTextbookDistributeDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookDistributeDetailDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDetailDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookDistributeRepository;
import com.gzedu.xlims.pojo.status.DistributeStatusEnum;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeService;

@Service
public class GjtTextbookDistributeServiceImpl implements GjtTextbookDistributeService {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookDistributeServiceImpl.class);

	@Autowired
	private GjtTextbookDistributeDao gjtTextbookDistributeDao;

	@Autowired
	private GjtTextbookDistributeRepository gjtTextbookDistributeRepository;

	@Autowired
	private GjtTextbookOrderDetailDao gjtTextbookOrderDetailDao;

	@Autowired
	private GjtTextbookDistributeDetailDao gjtTextbookDistributeDetailDao;

	@Override
	public void update(GjtTextbookDistribute entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookDistributeDao.save(entity);
	}

	@Override
	public void update(List<GjtTextbookDistribute> entities) {
		log.info("entities:[" + entities + "]");

		for (GjtTextbookDistribute entity : entities) {
			entity.setUpdatedDt(new Date());
		}

		gjtTextbookDistributeDao.save(entities);
	}

	@Override
	public GjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status, String isDeleted) {
		log.info("orderCode:" + orderCode + ", status:" + status + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.findByOrderCodeAndStatusAndIsDeleted(orderCode, status, isDeleted);
	}

	@Override
	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatus(String studentId, String isDeleted,
			int status) {
		log.info("studentId:" + studentId + ", status:" + status + ", isDeleted:" + isDeleted);
		if (status == 0) {
			return gjtTextbookDistributeDao.findByStudentIdAndIsDeleted(studentId, isDeleted);
		}
		return gjtTextbookDistributeDao.findByStudentIdAndIsDeletedAndStatus(studentId, isDeleted, status);
	}

	@Override
	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted,
			Collection<Integer> statuses) {
		log.info("studentId:" + studentId + ", statuses:" + statuses + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.findByStudentIdAndIsDeletedAndStatusIn(studentId, isDeleted, statuses);
	}

	@Override
	public List<GjtTextbookDistribute> findByStudentIdAndStatus(String studentId, String isDeleted, Collection<Integer> statuses) {
		return gjtTextbookDistributeDao.findByStudentIdAndStatus(studentId, isDeleted, statuses);
	}

	@Override
	public Long countByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted, Collection<Integer> statuses) {
		log.info("studentId:" + studentId + ", statuses:" + statuses + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.countByStudentIdAndIsDeletedAndStatusIn(studentId, isDeleted, statuses);
	}

	@Override
	public List<GjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String isDeleted,
			Collection<Integer> statuses) {
		log.info("orgId:" + orgId + ", statuses:" + statuses + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.findByIsDeletedAndStatusIn(orgId, isDeleted, statuses);
	}

	@Override
	public Page<Map<String, Object>> findDistributeSummary(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "]");
		return gjtTextbookDistributeDao.findDistributeSummary(searchParams, pageRequst);
	}

	@Override
	public int queryCurrentDistributeCount(String orgId, int studyYearCode) {
		log.info("orgId:" + orgId + ", studyYearCode:" + studyYearCode);
		return gjtTextbookDistributeDao.queryCurrentDistributeCount(orgId, studyYearCode);
	}

	@Override
	public List<Object[]> queryCurrentDistributeList(String orgId, int studyYearCode, Boolean isEnough) {
		log.info("orgId:" + orgId + ", studyYearCode:" + studyYearCode);
		return gjtTextbookDistributeDao.queryCurrentDistributeList(orgId, studyYearCode, isEnough);
	}

	@Override
	public List<Object[]> queryStudentCurrentDistributeList(String orgId, int studyYearCode) {
		log.info("orgId:" + orgId + ", studyYearCode:" + studyYearCode);
		return gjtTextbookDistributeDao.queryStudentCurrentDistributeList(orgId, studyYearCode);
	}

	@Override
	public GjtTextbookDistribute queryById(String distributeId) {
		log.info("distributeId:" + distributeId);
		return gjtTextbookDistributeRepository.findOne(distributeId);
	}

	@Override
	public void updateTextbookDistributeAndOrderDetai(GjtTextbookDistribute tbd) {
		Date now = DateUtil.getDate();
		tbd.setStatus(DistributeStatusEnum.已签收.getCode());
		tbd.setUpdatedDt(now);
		tbd.setSignDt(now);
		List<GjtTextbookDistributeDetail> distributeDetailsList = tbd.getGjtTextbookDistributeDetails();
		if (CollectionUtils.isNotEmpty(distributeDetailsList)) {
			for (GjtTextbookDistributeDetail detail : distributeDetailsList) {
				detail.setStatus(DistributeStatusEnum.已签收.getCode());
				// List<GjtTextbookOrderDetail> orderDetails =
				// gjtTextbookOrderDetailDao
				// .findByStudentIdAndTextbookIdAndStatus(tbd.getStudentId(),
				// detail.getTextbookId(), 6);
				// if (CollectionUtils.isEmpty(orderDetails))
				// continue;
				// for (GjtTextbookOrderDetail orderDetail : orderDetails) {
				// orderDetail.setStatus(7);
				// orderDetail.setUpdatedBy(tbd.getUpdatedBy());
				// orderDetail.setUpdatedDt(now);
				// gjtTextbookOrderDetailDao.save(orderDetail);
				// }
			}
		}
		this.update(tbd);
	}

	@Override
	public void updateStatus(String date) {
		// 先改教材订购明细表
		gjtTextbookOrderDetailDao.updateStatus(date);

		// 在改教材发放明细表
		gjtTextbookDistributeDetailDao.updateStatus(date);

		// 最后修改教材发放表
		gjtTextbookDistributeDao.updateStatus(date);
	}

	@Override
	public Page<Map<String, Object>> queryDistributeList(Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtTextbookDistributeDao.queryDistributeList(searchParams, pageRequst);

	}

	@Override
	public List<Map<String, Object>> queryDistributeDetailList(Map<String, Object> searchParams) {
		return gjtTextbookDistributeDao.queryDistributeDetailList(searchParams);
	}

	@Override
	@Transactional
	public void saveTextbookDistributeAndDetail(GjtTextbookDistribute distribute, List<GjtTextbookDistributeDetail> details) {
		gjtTextbookDistributeDao.save(distribute);
		gjtTextbookDistributeDetailDao.save(details);
		
	}

}
