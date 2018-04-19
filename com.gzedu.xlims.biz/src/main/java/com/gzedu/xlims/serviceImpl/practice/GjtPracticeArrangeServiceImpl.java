package com.gzedu.xlims.serviceImpl.practice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.practice.GjtPracticeAdviserDao;
import com.gzedu.xlims.dao.practice.GjtPracticeApplyDao;
import com.gzedu.xlims.dao.practice.GjtPracticeArrangeDao;
import com.gzedu.xlims.dao.practice.GjtPracticeStudentProgDao;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.practice.GjtPracticeAdviser;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeArrange;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;
import com.gzedu.xlims.pojo.status.PracticeProgressCodeEnum;
import com.gzedu.xlims.pojo.status.PracticeStatusEnum;
import com.gzedu.xlims.service.practice.GjtPracticeArrangeService;

@Service
public class GjtPracticeArrangeServiceImpl implements GjtPracticeArrangeService {
	
	private static final Log log = LogFactory.getLog(GjtPracticeArrangeServiceImpl.class);
	
	@Autowired
	private GjtPracticeArrangeDao gjtPracticeArrangeDao;
	
	@Autowired
	private GjtPracticeAdviserDao gjtPracticeAdviserDao;
	
	@Autowired
	private GjtPracticeApplyDao gjtPracticeApplyDao;
	
	@Autowired
	private GjtPracticeStudentProgDao gjtPracticeStudentProgDao;

	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Override
	public Page<GjtPracticeArrange> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtPracticeArrange> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtPracticeArrange.class);
		return gjtPracticeArrangeDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtPracticeArrange insert(GjtPracticeArrange entity) {
		log.info("entity:[" + entity + "]");
		entity.setArrangeId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtPracticeArrangeDao.save(entity);
	}

	@Override
	public void insert(List<GjtPracticeArrange> entities) {
		for (GjtPracticeArrange entity : entities) {
			entity.setArrangeId(UUIDUtils.random());
			entity.setCreatedDt(new Date());
			entity.setIsDeleted("N");
		}
		
		gjtPracticeArrangeDao.save(entities);
	}

	@Override
	@Transactional
	public void update(GjtPracticeArrange entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		
		if (entity.getGjtPracticeAdvisers() != null && entity.getGjtPracticeAdvisers().size() > 0){
			for (GjtPracticeAdviser adviser : entity.getGjtPracticeAdvisers()) {
				adviser.setAdviserId(UUIDUtils.random());
			}
			
			gjtPracticeAdviserDao.save(entity.getGjtPracticeAdvisers());
		}
		
		gjtPracticeArrangeDao.save(entity);
		
		// 分配指导老师
		List<GjtPracticeAdviser> advisers = new ArrayList<GjtPracticeAdviser>();
		if (entity.getGjtPracticeAdvisers() != null && entity.getGjtPracticeAdvisers().size() > 0){
			for (GjtPracticeAdviser adviser : entity.getGjtPracticeAdvisers()) {
				if (adviser.getAdviserType() == 1) {
					adviser.setAdviserNum2(adviser.getAdviserNum()); //用于标志可分配人数
					advisers.add(adviser);
				}
			}
		}
		if (advisers.size() > 0) {
			// 学员申请列表
			List<GjtPracticeApply> applys = gjtPracticeApplyDao.findByPracticePlanIdAndSpecialtyBaseId(entity.getPracticePlanId(), entity.getSpecialtyBaseId());
			if (applys != null && applys.size() > 0) {
				// 添加进度
				List<GjtPracticeStudentProg> progs = new ArrayList<GjtPracticeStudentProg>();
				
				// 先对上期未通过的学员分配回原来的指导老师
				List<GjtPracticeApply> removeList = new ArrayList<GjtPracticeApply>();
				List<GjtGrade> gradeList = gjtGradeDao.findGradesBefore(entity.getGjtPracticePlan().getOrgId(), entity.getGjtPracticePlan().getGradeId());
				GjtGrade grade = gradeList.get(0);
				Map<String, String> preNoPassMap = gjtPracticeArrangeDao.getPreNoPassMap(entity.getGjtPracticePlan().getOrgId(), grade.getGradeId(), entity.getSpecialtyBaseId());
				for (GjtPracticeApply apply : applys) {
					if (preNoPassMap.containsKey(apply.getStudentId())) {
						String teacherId = preNoPassMap.get(apply.getStudentId());
						for (GjtPracticeAdviser adviser : advisers) {
							if (teacherId.equals(adviser.getTeacherId())) {
								// 如果指导老师剩下的指导名额大于0，则可以分配，分配完先保存，再把学生从集合中移除，并且把指导名额减一
								if (adviser.getAdviserNum2() > 0) {
									apply.setGuideTeacher(teacherId);
									// 当状态等于“已申请”的时候，修改状态为“待提交初稿”
									if (apply.getStatus() == PracticeStatusEnum.PRACTICE_APPLY.getValue()) {
										apply.setStatus(PracticeStatusEnum.PRACTICE_STAY_OPEN.getValue());
										
										// 添加进度
										GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
										prog.setProgressId(UUIDUtils.random());
										prog.setStudentId(apply.getStudentId());
										prog.setPracticePlanId(apply.getPracticePlanId());
										prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_STAY_OPEN.getCode());
										prog.setCreatedBy(apply.getCreatedBy());
										prog.setCreatedDt(new Date());
										progs.add(prog);
									}
									
									gjtPracticeApplyDao.save(apply);
									removeList.add(apply);
									adviser.setAdviserNum2(adviser.getAdviserNum2() - 1);

									break;
								}
							}
						}
					}
				}
				applys.removeAll(removeList);
				
				// 再分配剩下的学员
				int n = 0;
				for (GjtPracticeAdviser adviser : advisers) {
					for (int i = n; i < n + adviser.getAdviserNum2() && i < applys.size(); i ++) {
						GjtPracticeApply apply = applys.get(i);
						apply.setGuideTeacher(adviser.getTeacherId());
						// 当状态等于“已申请”的时候，修改状态为“待提交初稿”
						if (apply.getStatus() == PracticeStatusEnum.PRACTICE_APPLY.getValue()) {
							apply.setStatus(PracticeStatusEnum.PRACTICE_STAY_OPEN.getValue());
							
							// 添加进度
							GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
							prog.setProgressId(UUIDUtils.random());
							prog.setStudentId(apply.getStudentId());
							prog.setPracticePlanId(apply.getPracticePlanId());
							prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_STAY_OPEN.getCode());
							prog.setCreatedBy(apply.getCreatedBy());
							prog.setCreatedDt(new Date());
							progs.add(prog);
						}
					}
					
					n += adviser.getAdviserNum2();
					if (n >= applys.size()) {
						break;
					}
				}
				
				gjtPracticeApplyDao.save(applys);
				gjtPracticeStudentProgDao.save(progs);
			}
		}
	}

	@Override
	public GjtPracticeArrange findOne(String id) {
		return gjtPracticeArrangeDao.findOne(id);
	}

	@Override
	public List<Map<String, Object>> getCanApplySpecialty(String orgId, String gradeId, String practicePlanId) {
		return gjtPracticeArrangeDao.getCanApplySpecialty(orgId, gradeId, practicePlanId);
	}
	
	public Page<Map<String, Object>> getCanApplyStudent(String orgId, String gradeId, String practicePlanId, String specialtyBaseId,
			Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtPracticeArrangeDao.getCanApplyStudent(orgId, gradeId, practicePlanId, specialtyBaseId, searchParams, pageRequst);
	}

	@Override
	public Map<String, Object> getGuideNum(String practicePlanId, String teacherId, String specialtyBaseId) {
		return gjtPracticeArrangeDao.getGuideNum(practicePlanId, teacherId, specialtyBaseId);
	}

	@Override
	public List<Map<String, Object>> getPreNoPass(String orgId, String gradeId, String specialtyBaseId) {
		return gjtPracticeArrangeDao.getPreNoPass(orgId, gradeId, specialtyBaseId);
	}

}
