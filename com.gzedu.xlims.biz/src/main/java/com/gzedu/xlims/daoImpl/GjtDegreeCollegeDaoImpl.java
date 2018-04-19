/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.daoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.dto.DegreeCollegeSpecialtyDto;

/**
 * 学位院校信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月21日
 * @version 2.5
 * @since JDK 1.7
 */
@Repository
@Transactional(readOnly = true)
public class GjtDegreeCollegeDaoImpl extends BaseDaoImpl {

    /**
     * 分页条件查询学位院校信息，HQL语句
     * @param orgId
     * @param searchParams
     * @param pageRequest
     * @return
     */
    public Page<DegreeCollegeSpecialtyDto> findDegreeCollegeSpecialtyByPage(String orgId, Map<String, Object> searchParams, PageRequest pageRequest) {
        Map<String, Object> parameters = new HashMap();
        StringBuilder queryHql = createHqlToFindDegreeCollegeSpecial(orgId, searchParams, parameters);
        return super.findByPageHql(queryHql, parameters, pageRequest, DegreeCollegeSpecialtyDto.class);
    }

    /**
     * 根据条件查询学位院校信息，HQL语句
     * @param orgId
     * @param searchParams
     * @param sort
     * @return
     */
    public List<DegreeCollegeSpecialtyDto> findDegreeCollegeSpecialtyBy(String orgId, Map<String, Object> searchParams, Sort sort) {
        Map<String, Object> parameters = new HashMap();
		StringBuilder queryHql = createHqlToFindDegreeCollegeSpecial(orgId, searchParams, parameters);
        return super.findAllByHql(queryHql, parameters, sort, DegreeCollegeSpecialtyDto.class);
    }

	public long countDegreeCollegeSpecialty(String orgId, Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder queryHql = new StringBuilder("select count(t)");
		queryHql.append(getFromHql(orgId, searchParams, parameters));
		return super.countByHql(queryHql, parameters);
	}

    /**
     * 拼接HQL，查询学员状态信息
     * @param orgId
     * @param searchParams
     * @param parameters
     * @return
     */
	private StringBuilder createHqlToFindDegreeCollegeSpecial(String orgId, final Map<String, Object> searchParams, Map<String, Object> parameters) {
		StringBuilder queryHql = new StringBuilder(
				"select new com.gzedu.xlims.pojo.dto.DegreeCollegeSpecialtyDto(b.id,t.collegeId,c.specialtyBaseId,t.collegeName,c.specialtyName,(SELECT CASE WHEN COUNT(*)>0 THEN TRUE ELSE FALSE END FROM GjtDegreeRequirement x WHERE x.collegeId=t.collegeId AND x.specialtyId=c.specialtyBaseId))");
		queryHql.append(getFromHql(orgId, searchParams, parameters));
		return queryHql;
	}

	private String getFromHql(String orgId, final Map<String, Object> searchParams, Map<String, Object> parameters) {
		StringBuilder queryHql = new StringBuilder();
    	 queryHql.append(" FROM GjtDegreeCollege t inner join t.gjtSpecialtyDegreeColleges b inner join b.gjtSpecialty c");
		queryHql.append(" WHERE t.isDeleted=:isDeleted and b.isDeleted=:isDeleted and c.isDeleted=:isDeleted");
         queryHql.append(" and t.orgId=:orgId");
         parameters.put("isDeleted", Constants.BOOLEAN_NO);
         parameters.put("orgId", orgId);

         if (StringUtils.isNotBlank(searchParams.get("collegeName"))) {
             queryHql.append(" AND t.collegeName LIKE :collegeName");
             parameters.put("collegeName", MatchMode.ANYWHERE.toMatchString(searchParams.get("collegeName").toString()));
         }
         if (StringUtils.isNotBlank(searchParams.get("collegeId"))) {
             queryHql.append(" AND t.collegeId = :collegeId");
             parameters.put("collegeId", searchParams.get("collegeId"));
         }
         if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
 			queryHql.append(" AND c.specialtyBaseId = :specialtyId");
             parameters.put("specialtyId", searchParams.get("specialtyId"));
         }
		if (StringUtils.isNotBlank(searchParams.get("isEnabled"))) {
 			queryHql.append(" AND b.isEnabled = :isEnabled");
			parameters.put("isEnabled", Integer.valueOf(searchParams.get("isEnabled") + ""));
 		}
 		if (StringUtils.isNotBlank(searchParams.get("notSet"))) {// 未设置
 			queryHql.append(
 					" AND not exists (select 1 FROM GjtDegreeRequirement x WHERE x.collegeId=t.collegeId AND x.specialtyId=c.specialtyBaseId)");
 		}
		return queryHql.toString();
    }

}
