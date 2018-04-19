package com.gzedu.xlims.serviceImpl.exam;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.exam.GjtExamBatchApprovalDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamBatchNewRepository;
import com.gzedu.xlims.pojo.exam.GjtExamBatchApproval;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GjtExamBatchNewServiceImpl implements GjtExamBatchNewService {

	private static final Log log = LogFactory.getLog(GjtExamSubjectNewServiceImpl.class);

	@Autowired
	private GjtExamBatchNewDao gjtExamBatchNewDao;

	@Autowired
	private GjtExamBatchNewRepository gjtExamBatchNewRepository;

	@Autowired
	private GjtExamPlanNewDao gjtExamPlanNewDao;
	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private GjtExamBatchApprovalDao gjtExamBatchApprovalDao;

	@Override
	public GjtExamBatchNew insert(GjtExamBatchNew entity) {
		entity.setExamBatchId(UUIDUtils.random());
		entity.setIsDeleted(0);
		if (StringUtils.isEmpty(entity.getExamBatchCode())) {
			entity.setExamBatchCode(codeGeneratorService.codeGenerator(CacheConstants.EXAM_BATCH_CODE));
		}
		GjtExamBatchNew examBatchNew = gjtExamBatchNewDao.save(entity);
		
		//增加审批记录
		GjtExamBatchApproval approval = new GjtExamBatchApproval();
		approval.setApprovalId(UUIDUtils.random());
		approval.setExamBatchId(examBatchNew.getExamBatchId());
		approval.setAuditOperatorRole("1");
		approval.setAuditState("1");
		approval.setUserId(examBatchNew.getCreatedBy());
		approval.setXxId(examBatchNew.getXxId());
		approval.setCreatedBy(examBatchNew.getCreatedBy());
		approval.setIsDeleted("N");
		approval.setCreatedDt(new Date());
		gjtExamBatchApprovalDao.save(approval);
		
		return examBatchNew;
	}
	
	@Override
	public GjtExamBatchNew insertNew(GjtExamBatchNew entity) {
//		entity.setExamBatchId(UUIDUtils.random());
		if(StringUtils.isEmpty(entity.getExamBatchCode())){
			entity.setExamBatchCode(codeGeneratorService.codeGenerator(CacheConstants.EXAM_BATCH_CODE));
		}
		try{
			entity = gjtExamBatchNewDao.save(entity);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			entity = null;
		}
		return entity;
	}

	@Override
	public Page<GjtExamBatchNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtExamBatchNew> spec = new Criteria<GjtExamBatchNew>();
		spec.add(Restrictions.eq("isDeleted", 0, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamBatchNewDao.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtExamBatchNew> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtExamBatchNew> spec = new Criteria<GjtExamBatchNew>();
		spec.add(Restrictions.eq("isDeleted", 0, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamBatchNewRepository.findAll(spec, sort);
	}

	@Override
	public int delete(List<String> ids, String xxid) {
		return gjtExamBatchNewDao.deleteGjtExamBatchNew(ids, xxid);
	}

	@Override
	public Map<String, Object> deleteBatch(String id, String xxid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("successful", false);
		GjtExamBatchNew batch = gjtExamBatchNewDao.findOne(id);
		Date now = new Date();
		if (null != batch) {
			if (xxid.equals(batch.getXxId())) {
				if (now.before(batch.getBookSt())) {
					int rs = gjtExamPlanNewDao.deletePlansByBatchCode(batch.getExamBatchCode());
					resultMap.put("planNum", rs);
					List<String> ids = new ArrayList<String>();
					ids.add(id);
					gjtExamBatchNewDao.deleteGjtExamBatchNew(ids, xxid);
					resultMap.put("message", "成功删除批次与其关联的" + rs + "条考试计划");
					resultMap.put("successful", true);
				} else {
					resultMap.put("message", "该批次已开始, 无法删除");
				}
			} else {
				resultMap.put("message", "不能删除别的院校的数据");
			}
		} else {
			resultMap.put("message", "批次不存在");
		}

		return resultMap;
	}

	@Override
	public GjtExamBatchNew queryBy(String id) {
		return gjtExamBatchNewDao.findOne(id);
	}

	@Override
	public GjtExamBatchNew update(GjtExamBatchNew entity) {
		entity.setUpdatedDt(new Date());
		
		return gjtExamBatchNewDao.save(entity);
	}

	@Override
	public GjtExamBatchNew queryByexamBatchCodeAndXxId(String examBatchCode, String xxid) {
		return gjtExamBatchNewDao.queryByExamBatchCodeAndXxId(examBatchCode, xxid);
	}

	@Override
	public GjtExamBatchNew queryByExamBatchCode(String examBatchCode) {
		return gjtExamBatchNewDao.queryByExamBatchCode(examBatchCode);
	}

	@Override
	public GjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId) {
		return gjtExamBatchNewDao.findByStudyYearIdAndXxIdAndIsDeleted(studyYearId, xxId);
	}

	@Override
	public GjtExamBatchNew findByGradeIdAndXxId(String gradeId, String xxId) {
		return gjtExamBatchNewDao.findByGradeIdAndXxId(gradeId, xxId);
	}
	
	/**
	 * 查询批次列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Page getExamBatchList(Map searchParams, PageRequest pageRequst) {
//		return gjtExamBatchNewDao.getExamBatchList(searchParams, pageRequst);
		return gjtExamBatchNewDao.getExamBatchNewList(searchParams, pageRequst);
	}
	
	/**
	 * 查询批次列表统计项
	 * @return
	 */
	@Override
	public int getExamBatchCount(Map searchParams) {
//		return gjtExamBatchNewDao.getExamBatchCount(searchParams);
		return gjtExamBatchNewDao.getExamBatchNewCount(searchParams);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map queryExamBatchDetail(Map searchParams) {
		Map resultMap = new HashMap();
		try{
			List list = gjtExamBatchNewDao.queryExamBatchDetail(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				resultMap = (Map) list.get(0);
			}
			List approvalList = gjtExamBatchNewDao.getApprovalExamBatchList(searchParams);
			if(EmptyUtils.isNotEmpty(approvalList)){
				resultMap.put("approvalList", approvalList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}
	
}
