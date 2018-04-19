package com.gzedu.xlims.serviceImpl.exam;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzdec.framework.util.BeanConvertUtils;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointNewRepository;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.service.exam.GjtExamPointNewService;

@Service
public class GjtExamPointNewServiceImpl implements GjtExamPointNewService {

	@Autowired
	GjtExamPointNewDao gjtExamPointDao;
	
	@Autowired
	GjtExamPointNewRepository gjtExamPointRepository;
	
	@Autowired
	GjtExamBatchNewDao gjtExamBatchNewDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.exam.GjtExamPointService#queryAll(java.lang.
	 * String, java.util.Map, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<GjtExamPointNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtExamPointDao.queryAll(searchParams, pageRequst);
	}

	@Override
	public boolean existsCode(String code) {
		List<GjtExamPointNew> gjtExamPointNew = gjtExamPointRepository.findByCodeAndIsDeleted(code, Constants.BOOLEAN_NO);
		if (gjtExamPointNew == null || gjtExamPointNew.size() == 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public GjtExamPointNew findByExamBatchCodeAndCode(String examBatchCode, String code) {
		return gjtExamPointRepository.findByExamBatchCodeAndCodeAndIsDeleted(examBatchCode, code, Constants.BOOLEAN_NO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.exam.GjtExamPointNewService#save(com.gzedu.xlims.
	 * pojo.exam.GjtExamPointNew)
	 */
	@Override
	public void save(GjtExamPointNew entity) throws ServerException {
		if(StringUtils.isNotBlank(entity.getExamBatchId())) {
			GjtExamBatchNew examBatchNew = gjtExamBatchNewDao.findOne(entity.getExamBatchId());
			if (examBatchNew == null) {
				throw new ServerException("批次不存在");
			}
			entity.setExamBatchCode(examBatchNew.getExamBatchCode());
		}
		if (StringUtils.isNotBlank(entity.getExamPointId())) {
			entity.setUpdatedDt(new Date());
		} else {
			entity.setExamPointId(UUIDUtils.random());
			entity.setCreatedDt(new Date());
		}
		gjtExamPointDao.save(entity);
	}
	

	@Override
	public boolean batchAddPoint(String examBatchId, List<String> pointIds) throws ServerException {
		GjtExamBatchNew examBatchNew = gjtExamBatchNewDao.findOne(examBatchId);
		if (examBatchNew == null) {
			throw new ServerException("批次不存在");
		}
		for (String examPointId : pointIds) {
			GjtExamPointNew basePoint = gjtExamPointRepository.findOne(examPointId);
			GjtExamPointNew point = gjtExamPointRepository.findByExamBatchCodeAndCodeAndIsDeleted(examBatchNew.getExamBatchCode(), basePoint.getCode(), Constants.BOOLEAN_NO);
			// 忽略掉已存在的
			if(point != null) {
				continue;
			}
			GjtExamPointNew newPoint = BeanConvertUtils.convert(basePoint, GjtExamPointNew.class);
			newPoint.setExamPointId(UUIDUtils.random());
			newPoint.setExamBatchId(examBatchNew.getExamBatchId());
			newPoint.setExamBatchCode(examBatchNew.getExamBatchCode());
			newPoint.setCreatedDt(new Date());
			newPoint.setUpdatedDt(new Date());
			List<GjtStudyCenter> studyCenters = new ArrayList<GjtStudyCenter>();
			for (GjtStudyCenter s : basePoint.getGjtStudyCenters()) {
				studyCenters.add(s);
			}
			newPoint.setGjtStudyCenters(studyCenters);
			gjtExamPointDao.save(newPoint);
			this.insertExamPointStudyCenter(newPoint.getExamPointId());
		}
		return true;
	}

	@Override
	public boolean insertExamPointStudyCenter(String examPointId) {
		// 如果没有指定学习中心，则插入一条-1的记录
		if(!gjtExamPointDao.existsExamPointStudyCenter(examPointId)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("EXAM_POINT_ID", examPointId);
			params.put("STUDY_CENTER_ID", "-1");
			gjtExamPointDao.insertExamPointStudyCenter(params);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.exam.GjtExamPointNewService#delete(java.util.
	 * List)
	 */
	@Override
	public void delete(List<String> ids) {
		gjtExamPointDao.delete(ids);
	}

	@Override
	public GjtExamPointNew queryById(String id) {
		return gjtExamPointDao.queryById(id);

	}

	public static void main(String[] args) {
		System.out.println(UUIDUtils.random());
	}
}
