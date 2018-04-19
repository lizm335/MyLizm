/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.graduation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.graduation.GjtDegreeCollegeDao;
import com.gzedu.xlims.dao.graduation.GjtDegreeRequirementBaseDao;
import com.gzedu.xlims.dao.graduation.GjtDegreeRequirementDao;
import com.gzedu.xlims.dao.graduation.GjtSpecialtyDegreeCollegeDao;
import com.gzedu.xlims.daoImpl.GjtDegreeCollegeDaoImpl;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.dto.DegreeCollegeSpecialtyDto;
import com.gzedu.xlims.pojo.graduation.GjtDegreeCollege;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirementBase;
import com.gzedu.xlims.pojo.graduation.GjtSpecialtyDegreeCollege;
import com.gzedu.xlims.service.graduation.GjtDegreeCollegeService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 学位院校、申请条件业务逻辑实现类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2016年11月20日
 * @EditDate 2016年11月21日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class GjtDegreeCollegeServiceImpl extends BaseServiceImpl<GjtDegreeCollege> implements GjtDegreeCollegeService {

    @Autowired
    private GjtDegreeCollegeDao gjtDegreeCollegeDao;

    @Autowired
    private GjtDegreeRequirementDao gjtDegreeRequirementDao;

    @Autowired
    private GjtDegreeRequirementBaseDao gjtDegreeRequirementBaseDao;

    @Autowired
    private GjtSpecialtyDegreeCollegeDao gjtSpecialtyDegreeCollegeDao;

    @Autowired
    private GjtDegreeCollegeDaoImpl degreeCollegeDao;

	@Autowired
	private CommonDao commonDao;

    @Override
    protected BaseDao<GjtDegreeCollege, String> getBaseDao() {
        return gjtDegreeCollegeDao;
    }

    @Override
	public Page<DegreeCollegeSpecialtyDto> queryDegreeCollegeSpecialtyByPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), new Sort(Sort.Direction.DESC, "t.createdDt"));
		}

		Page<DegreeCollegeSpecialtyDto> result = degreeCollegeDao.findDegreeCollegeSpecialtyByPage(orgId, searchParams, pageRequest);
		return result;
	}

	@Override
	public long countDegreeCollegeSpecialty(String orgId, Map<String, Object> searchParams) {
		return degreeCollegeDao.countDegreeCollegeSpecialty(orgId, searchParams);
	}

    @Override
    public List<GjtDegreeRequirement> queryGjtDegreeRequirementBySpecialty(String collegeId, String specialtyId) {
        Criteria<GjtDegreeRequirement> spec = new Criteria();
        spec.add(Restrictions.eq("collegeId", collegeId, true));
        spec.add(Restrictions.eq("specialtyId", specialtyId, true));
        return gjtDegreeRequirementDao.findAll(spec, new Sort("requirementType"));
    }

	@Override
	public List<GjtDegreeRequirement> queryReqByCollegeSpecialtyId(String collegeSpecialtyId) {
		Criteria<GjtDegreeRequirement> spec = new Criteria();
		spec.add(Restrictions.eq("collegeSpecialtyId", collegeSpecialtyId, true));
		return gjtDegreeRequirementDao.findAll(spec, new Sort("createdDt"));
	}

    @Override
    public GjtDegreeRequirement queryGjtDegreeRequirementById(String requirementId) {
        return gjtDegreeRequirementDao.findOne(requirementId);
    }

    @Override
    public List<GjtDegreeRequirementBase> queryGjtDegreeRequirementBaseAll() {
        Criteria<GjtDegreeRequirementBase> spec = new Criteria();
        spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
        return gjtDegreeRequirementBaseDao.findAll(spec, new Sort("baseType"));
    }

    @Override
    public GjtDegreeRequirementBase queryGjtDegreeRequirementBaseById(String baseId) {
        return gjtDegreeRequirementBaseDao.findOne(baseId);
    }

    @Override
    public boolean insertDegreeCollegeSpecialtys(GjtDegreeCollege entity, String[] specialtyIds) {
        List<GjtSpecialtyDegreeCollege> newList = new ArrayList<GjtSpecialtyDegreeCollege>(specialtyIds.length);
        entity.setCreatedDt(new Date());
        if(specialtyIds != null && specialtyIds.length > 0) {
            for (int i = 0; i < specialtyIds.length; i++) {
                GjtSpecialtyDegreeCollege item = new GjtSpecialtyDegreeCollege();
                item.setId(UUIDUtils.random());
                item.setGjtDegreeCollege(entity);
				item.setGjtSpecialty(new GjtSpecialtyBase(specialtyIds[i]));
                newList.add(item);
            }
        }
        GjtDegreeCollege info = gjtDegreeCollegeDao.save(entity);
        gjtSpecialtyDegreeCollegeDao.save(newList);
        return info != null;
    }

    @Override
    public boolean updateGjtDegreeCollegeSpecialtys(GjtDegreeCollege entity, String[] specialtyIds) {
        if (StringUtils.isNoneBlank(entity.getCollegeId())) {
            entity.setUpdatedDt(new Date());
            // 删除不存在的
            List<GjtSpecialtyDegreeCollege> removeList = new ArrayList<GjtSpecialtyDegreeCollege>();
            for (Iterator<GjtSpecialtyDegreeCollege> iter = entity.getGjtSpecialtyDegreeColleges().iterator(); iter.hasNext();) {
                GjtSpecialtyDegreeCollege info = iter.next();
                boolean exist = false;
                for (int i = 0; i < specialtyIds.length; i++) {
					if (info.getGjtSpecialty().getSpecialtyBaseId().equals(specialtyIds[i])) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) {
                    removeList.add(info);
                }
            }
            // 添加新的
            List<GjtSpecialtyDegreeCollege> newList = new ArrayList<GjtSpecialtyDegreeCollege>();
            for (int i = 0; i < specialtyIds.length; i++) {
                boolean exist = false;
                for (Iterator<GjtSpecialtyDegreeCollege> iter = entity.getGjtSpecialtyDegreeColleges().iterator(); iter.hasNext();) {
                    GjtSpecialtyDegreeCollege info = iter.next();
					if (info.getGjtSpecialty().getSpecialtyBaseId().equals(specialtyIds[i])) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) {
                    GjtSpecialtyDegreeCollege item = new GjtSpecialtyDegreeCollege();
                    item.setId(UUIDUtils.random());
                    item.setGjtDegreeCollege(entity);
					item.setGjtSpecialty(new GjtSpecialtyBase(specialtyIds[i]));
                    newList.add(item);
                }
            }
            GjtDegreeCollege info = gjtDegreeCollegeDao.save(entity);
            gjtSpecialtyDegreeCollegeDao.delete(removeList);
            gjtSpecialtyDegreeCollegeDao.save(newList);
            return info != null;
        }
        return false;
    }

    @Override
    public boolean insertGjtDegreeRequirement(GjtDegreeRequirement entity) {
        entity.setCreatedDt(new Date());
        GjtDegreeRequirement info = gjtDegreeRequirementDao.save(entity);
        return info != null;
    }

    @Override
    public boolean insertGjtDegreeRequirement(List<GjtDegreeRequirement> entityList) {
        for (GjtDegreeRequirement entity : entityList) {
            entity.setCreatedDt(new Date());
        }
        List<GjtDegreeRequirement> infoList = gjtDegreeRequirementDao.save(entityList);
        return infoList != null;
    }

    @Override
    public boolean updateGjtDegreeRequirement(GjtDegreeRequirement entity) {
        if (StringUtils.isNoneBlank(entity.getRequirementId())) {
            GjtDegreeRequirement info = gjtDegreeRequirementDao.save(entity);
            return info != null;
        }
        return false;
    }

    @Override
    public boolean updateGjtDegreeRequirement(List<GjtDegreeRequirement> entityList) {
        List<String> ids = new ArrayList<String>();
        for (GjtDegreeRequirement entity : entityList) {
            ids.add(entity.getRequirementId());
        }
        // 先删除不存在的数据再更新
		gjtDegreeRequirementDao.deleteNotExistInBatch(entityList.get(0).getCollegeSpecialtyId(), ids);
        List<GjtDegreeRequirement> infoList = gjtDegreeRequirementDao.save(entityList);
        return infoList != null;
    }

    @Override
    public boolean insertGjtDegreeRequirementBase(GjtDegreeRequirementBase entity) {
        entity.setCreatedDt(new Date());
        GjtDegreeRequirementBase info = gjtDegreeRequirementBaseDao.save(entity);
        return info != null;
    }

    @Override
    public boolean updateGjtDegreeRequirementBase(GjtDegreeRequirementBase entity) {
        if (StringUtils.isNoneBlank(entity.getBaseId())) {
            entity.setUpdatedDt(new Date());
            GjtDegreeRequirementBase info = gjtDegreeRequirementBaseDao.save(entity);
            return info != null;
        }
        return false;
    }

    @Override
    public boolean deleteGjtDegreeRequirementBase(String id) {
        gjtDegreeRequirementBaseDao.delete(id);
        return true;
    }

    @Override
    public boolean deleteGjtDegreeRequirementBaseInBatch(String[] ids) {
        if (ids != null) {
            for (String id : ids) {
                deleteGjtDegreeRequirementBase(id);
            }
        }
        return true;
    }

    @Override
    public boolean deleteGjtSpecialtyDegreeCollegeReq(String collegeSpecialtyId) {
        GjtSpecialtyDegreeCollege info = gjtSpecialtyDegreeCollegeDao.findOne(collegeSpecialtyId);
        if(info != null) {
			info.setIsDeleted("Y");
			gjtSpecialtyDegreeCollegeDao.save(info);
            // 删除不存在的数据
        }
        return true;
    }

    @Override
    public boolean deleteGjtSpecialtyDegreeCollegeReqInBatch(String[] collegeSpecialtyIds) {
        if (collegeSpecialtyIds != null) {
            for (String id : collegeSpecialtyIds) {
                deleteGjtSpecialtyDegreeCollegeReq(id);
            }
        }
        return true;
    }


	@Override
	public void CreateSpecialtyDegreeCollege(String collegeId, String gradeId, String degreeName, List<String> specialtyIds,
			List<GjtDegreeRequirement> requirementList, String orgId) {
		Date date = new Date();
		for (String specialtyId : specialtyIds) {
			GjtSpecialtyDegreeCollege gsdc = new GjtSpecialtyDegreeCollege();
			gsdc.setDegreeName(degreeName);
			gsdc.setGjtDegreeCollege(new GjtDegreeCollege(collegeId));
			gsdc.setGjtSpecialty(new GjtSpecialtyBase(specialtyId));
			gsdc.setGradeId(gradeId);
			gsdc.setId(UUIDUtils.random());
			gsdc.setIsEnabled(0);
			gsdc.setIsDeleted("N");
			gsdc.setOrgId(orgId);
			gjtSpecialtyDegreeCollegeDao.save(gsdc);
			for (GjtDegreeRequirement requirement : requirementList) {
				GjtDegreeRequirement entity = new GjtDegreeRequirement();
				BeanUtils.copyProperties(requirement, entity);
				entity.setCollegeSpecialtyId(gsdc.getId());
				entity.setCollegeId(collegeId);
				entity.setSpecialtyId(specialtyId);
				entity.setCreatedDt(date);
				entity.setRequirementId(UUIDUtils.random());
				gjtDegreeRequirementDao.save(entity);
			}
		}

	}

	@Override
	public GjtSpecialtyDegreeCollege querySpecialtyDegreeCollegeById(String id) {
		return gjtSpecialtyDegreeCollegeDao.findOne(id);
	}

	@Override
	public void saveCollegeSpecialty(GjtSpecialtyDegreeCollege specialytCollge) {
		gjtSpecialtyDegreeCollegeDao.save(specialytCollge);
	}

	@Override
	public List<GjtSpecialtyDegreeCollege> queryCollegeSpecialtyByOrgId(String orgId) {
		Criteria<GjtSpecialtyDegreeCollege> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("orgId", orgId, true));
		return gjtSpecialtyDegreeCollegeDao.findAll(spec);
	}

	@Override
	public List<GjtDegreeCollege> queryByStudentInfo(GjtStudentInfo student) {
		String hql = "select t from GjtDegreeCollege t inner join t.gjtSpecialtyDegreeColleges s where 1=1"
				+" and t.orgId=:orgId"
				+" and t.status=0"
				+" and t.isDeleted='N'"
				+ " and s.gjtGrade.startDate<=:startDate"
				+" and s.gjtSpecialty.specialtyBaseId=:specialtyBaseId";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("orgId", student.getXxId());
		params.put("startDate", student.getGjtGrade().getStartDate());
		params.put("specialtyBaseId", student.getGjtSpecialty().getGjtSpecialtyBase().getSpecialtyBaseId());
		return commonDao.queryForList(hql, params, GjtDegreeCollege.class);
	}
}
