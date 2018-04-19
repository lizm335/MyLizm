package com.gzedu.xlims.serviceImpl.thesis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.gzedu.xlims.dao.thesis.GjtThesisAdviserDao;
import com.gzedu.xlims.dao.thesis.GjtThesisApplyDao;
import com.gzedu.xlims.dao.thesis.GjtThesisArrangeDao;
import com.gzedu.xlims.dao.thesis.GjtThesisStudentProgDao;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.status.ThesisProgressCodeEnum;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisAdviser;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisArrange;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.thesis.GjtThesisArrangeService;

@Service
public class GjtThesisArrangeServiceImpl implements GjtThesisArrangeService {
	
	private static final Log log = LogFactory.getLog(GjtThesisArrangeServiceImpl.class);
	
	@Autowired
	private GjtThesisArrangeDao gjtThesisArrangeDao;
	
	@Autowired
	private GjtThesisAdviserDao gjtThesisAdviserDao;
	
	@Autowired
	private GjtThesisApplyDao gjtThesisApplyDao;
	
	@Autowired
	private GjtThesisStudentProgDao gjtThesisStudentProgDao;

	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Override
	public Page<GjtThesisArrange> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtThesisArrange> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtThesisArrange.class);
		return gjtThesisArrangeDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtThesisArrange insert(GjtThesisArrange entity) {
		log.info("entity:[" + entity + "]");
		entity.setArrangeId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtThesisArrangeDao.save(entity);
	}

	@Override
	public void insert(List<GjtThesisArrange> entities) {
		for (GjtThesisArrange entity : entities) {
			entity.setArrangeId(UUIDUtils.random());
			entity.setCreatedDt(new Date());
			entity.setIsDeleted("N");
		}
		
		gjtThesisArrangeDao.save(entities);
	}

	@Override
	@Transactional
	public void update(GjtThesisArrange entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		
		//gjtThesisAdviserDao.deleteByArrangeId(entity.getArrangeId());
		if (entity.getGjtThesisAdvisers() != null && entity.getGjtThesisAdvisers().size() > 0){
			for (GjtThesisAdviser adviser : entity.getGjtThesisAdvisers()) {
				if (adviser.getAdviserType() == 1) {  // 2017-9-12修改：这里只分配指导老师
					adviser.setAdviserId(UUIDUtils.random());
				}
			}
			
			gjtThesisAdviserDao.save(entity.getGjtThesisAdvisers());
		}
		
		gjtThesisArrangeDao.save(entity);
		
		// 分配指导老师
		List<GjtThesisAdviser> advisers = new ArrayList<GjtThesisAdviser>();
		// 主答辩老师ID
		//List<String> advisers2 = new ArrayList<String>();
		//String advisers2Str = "";
		// 辅答辩老师ID
		//List<String> advisers3 = new ArrayList<String>();
		//String advisers3Str = "";
		if (entity.getGjtThesisAdvisers() != null && entity.getGjtThesisAdvisers().size() > 0){
			for (GjtThesisAdviser adviser : entity.getGjtThesisAdvisers()) {
				if (adviser.getAdviserType() == 1) {
					adviser.setAdviserNum2(adviser.getAdviserNum());//用于标志可分配人数
					advisers.add(adviser);
				} else if (adviser.getAdviserType() == 2) {
					//advisers2.add(adviser.getTeacherId());
				} else if (adviser.getAdviserType() == 3) {
					//advisers3.add(adviser.getTeacherId());
				}
			}
		}
		
		/*if (advisers2.size() > 0) {
			advisers2Str = StringUtils.join(advisers2, ",");
		}
		if (advisers3.size() > 0) {
			advisers3Str = StringUtils.join(advisers3, ",");
		}*/
		
		if (advisers.size() > 0) {
			// 学员申请列表
			List<GjtThesisApply> applys = gjtThesisApplyDao.findByThesisPlanIdAndSpecialtyBaseId(entity.getThesisPlanId(), entity.getSpecialtyBaseId());
			if (applys != null && applys.size() > 0) {
				// 添加进度
				List<GjtThesisStudentProg> progs = new ArrayList<GjtThesisStudentProg>();
				
				// 先对上期未通过的学员分配回原来的指导老师
				List<GjtThesisApply> removeList = new ArrayList<GjtThesisApply>();
				List<GjtGrade> gradeList = gjtGradeDao.findGradesBefore(entity.getGjtThesisPlan().getOrgId(), entity.getGjtThesisPlan().getGradeId());
				GjtGrade grade = gradeList.get(0);
				Map<String, String> preNoPassMap = gjtThesisArrangeDao.getPreNoPassMap(entity.getGjtThesisPlan().getOrgId(), grade.getGradeId(), entity.getSpecialtyBaseId());
				for (GjtThesisApply apply : applys) {
					if (preNoPassMap.containsKey(apply.getStudentId())) {
						String teacherId = preNoPassMap.get(apply.getStudentId());
						for (GjtThesisAdviser adviser : advisers) {
							if (teacherId.equals(adviser.getTeacherId())) {
								// 如果指导老师剩下的指导名额大于0，则可以分配，分配完先保存，再把学生从集合中移除，并且把指导名额减一
								if (adviser.getAdviserNum2() > 0) {
									apply.setGuideTeacher(teacherId);
									//apply.setDefenceTeacher1(advisers2Str);
									//apply.setDefenceTeacher2(advisers3Str);
									// 当状态等于“已申请”的时候，修改状态为“待开题”
									if (apply.getStatus() == ThesisStatusEnum.THESIS_APPLY.getValue()) {
										apply.setStatus(ThesisStatusEnum.THESIS_STAY_OPEN.getValue());
										
										// 添加进度
										GjtThesisStudentProg prog = new GjtThesisStudentProg();
										prog.setProgressId(UUIDUtils.random());
										prog.setStudentId(apply.getStudentId());
										prog.setThesisPlanId(apply.getThesisPlanId());
										prog.setProgressCode(ThesisProgressCodeEnum.THESIS_STAY_OPEN.getCode());
										prog.setCreatedBy(apply.getCreatedBy());
										prog.setCreatedDt(new Date());
										progs.add(prog);
									}
									
									gjtThesisApplyDao.save(apply);
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
				for (GjtThesisAdviser adviser : advisers) {
					for (int i = n; i < n + adviser.getAdviserNum2() && i < applys.size(); i ++) {
						GjtThesisApply apply = applys.get(i);
						apply.setGuideTeacher(adviser.getTeacherId());
						//apply.setDefenceTeacher1(advisers2Str);
						//apply.setDefenceTeacher2(advisers3Str);
						// 当状态等于“已申请”的时候，修改状态为“待开题”
						if (apply.getStatus() == ThesisStatusEnum.THESIS_APPLY.getValue()) {
							apply.setStatus(ThesisStatusEnum.THESIS_STAY_OPEN.getValue());
							
							// 添加进度
							GjtThesisStudentProg prog = new GjtThesisStudentProg();
							prog.setProgressId(UUIDUtils.random());
							prog.setStudentId(apply.getStudentId());
							prog.setThesisPlanId(apply.getThesisPlanId());
							prog.setProgressCode(ThesisProgressCodeEnum.THESIS_STAY_OPEN.getCode());
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
				
				gjtThesisApplyDao.save(applys);
				gjtThesisStudentProgDao.save(progs);
			}
		}
	}

	@Override
	@Transactional
	public void updateDefenceTeacher(GjtThesisArrange entity) {
		log.info("entity:[" + entity + "]");
		//entity.setUpdatedDt(new Date());

		// 主答辩老师ID
		List<String> advisers2 = new ArrayList<String>();
		String advisers2Str = "";
		// 辅答辩老师ID
		List<String> advisers3 = new ArrayList<String>();
		String advisers3Str = "";
		
		// 保存主答辩老师
		if (entity.getGjtThesisAdvisers2() != null && entity.getGjtThesisAdvisers2().size() > 0){
			for (GjtThesisAdviser adviser : entity.getGjtThesisAdvisers2()) {
				adviser.setAdviserId(UUIDUtils.random());
				advisers2.add(adviser.getTeacherId());
			}
		}
		
		// 保存辅答辩老师
		if (entity.getGjtThesisAdvisers3() != null && entity.getGjtThesisAdvisers3().size() > 0){
			for (GjtThesisAdviser adviser : entity.getGjtThesisAdvisers3()) {
				adviser.setAdviserId(UUIDUtils.random());
				advisers3.add(adviser.getTeacherId());
			}
		}
		
		//gjtThesisArrangeDao.save(entity);
		
		if (advisers2.size() > 0) {
			advisers2Str = StringUtils.join(advisers2, ",");
		}
		if (advisers3.size() > 0) {
			advisers3Str = StringUtils.join(advisers3, ",");
		}
		
		// 学员申请列表
		List<GjtThesisApply> applys = gjtThesisApplyDao.findByThesisPlanIdAndSpecialtyBaseId(entity.getThesisPlanId(), entity.getSpecialtyBaseId());
		if (applys != null && applys.size() > 0) {
			for (GjtThesisApply apply : applys) {
				apply.setDefenceTeacher1(advisers2Str);
				apply.setDefenceTeacher2(advisers3Str);
			}
			
			gjtThesisApplyDao.save(applys);
		}
		
		gjtThesisAdviserDao.save(entity.getGjtThesisAdvisers2());
		gjtThesisAdviserDao.save(entity.getGjtThesisAdvisers3());
	}

	@Override
	public GjtThesisArrange findOne(String id) {
		return gjtThesisArrangeDao.findOne(id);
	}

	@Override
	public List<Map<String, Object>> getCanApplySpecialty(String orgId, String gradeId, String thesisPlanId) {
		return gjtThesisArrangeDao.getCanApplySpecialty(orgId, gradeId, thesisPlanId);
	}
	
	public Page<Map<String, Object>> getCanApplyStudent(String orgId, String gradeId, String thesisPlanId, String specialtyBaseId,
			Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtThesisArrangeDao.getCanApplyStudent(orgId, gradeId, thesisPlanId, specialtyBaseId, searchParams, pageRequst);
	}

	@Override
	public Map<String, Object> getGuideNum(String thesisPlanId, String teacherId, String specialtyBaseId) {
		return gjtThesisArrangeDao.getGuideNum(thesisPlanId, teacherId, specialtyBaseId);
	}

	@Override
	public List<Map<String, Object>> getPreNoPass(String orgId, String gradeId, String specialtyBaseId) {
		return gjtThesisArrangeDao.getPreNoPass(orgId, gradeId, specialtyBaseId);
	}

	@Override
	public GjtThesisArrange findByThesisPlanIdAndSpecialtyBaseId(String thesisPlanId, String specialtyBaseId) {
		return gjtThesisArrangeDao.findByThesisPlanIdAndSpecialtyBaseIdAndIsDeleted(thesisPlanId, specialtyBaseId, "N");
	}

}
