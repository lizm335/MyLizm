package com.gzedu.xlims.serviceImpl.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.exam.GjtExamBatchApprovalDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.pojo.exam.GjtExamBatchApproval;
import com.gzedu.xlims.service.exam.GjtExamBatchApprovalService;
@Service
public class GjtExamBatchApprovalServiceImpl implements GjtExamBatchApprovalService {
	
	@Autowired
	private GjtExamBatchNewDao gjtExamBatchNewDao;

	@Autowired
	private GjtExamBatchApprovalDao gjtExamBatchApprovalDao;

	@SuppressWarnings("rawtypes")
	@Transactional
	public Map saveExamApproval(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			searchParams.put("APPROVAL_ID", UUIDUtils.random());
			gjtExamBatchNewDao.saveExamApproval(searchParams);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map delExamBatch(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		int result = 0;
		try{
			result = gjtExamBatchNewDao.delExamBatch(searchParams);
			resultMap.put("result", result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Map saveApprovalData(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		int result = 0;
		try{
			searchParams.put("APPROVAL_ID", UUIDUtils.random());
			int i = gjtExamBatchNewDao.saveExamApproval(searchParams);
			if(i==1){
				if("1".equals(ObjectUtils.toString(searchParams.get("AUDIT_STATE")))){
					searchParams.put("PLAN_STATUS", "3");
				}else if("2".equals(ObjectUtils.toString(searchParams.get("AUDIT_STATE")))){
					searchParams.put("PLAN_STATUS", "2");
				}
				result = gjtExamBatchNewDao.updateExamBatchStatus(searchParams);
			}
			resultMap.put("result", result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map againApprovalData(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		int result = 0;
		try{
			searchParams.put("APPROVAL_ID", UUIDUtils.random());
			result = gjtExamBatchNewDao.saveExamApproval(searchParams);
			resultMap.put("result", result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public GjtExamBatchApproval insert(GjtExamBatchApproval entity) {
		entity.setApprovalId(UUIDUtils.random());
		entity.setIsDeleted("N");
		entity.setCreatedDt(new Date());
		
		return gjtExamBatchApprovalDao.save(entity);
	}

}
