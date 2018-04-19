/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;

/**
 * 学位条件操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月20日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtDegreeRequirementDao extends BaseDao<GjtDegreeRequirement, String> {

    /**
     * 删除院校的某专业学位条件
     * @param collegeId
     * @param specialtyId
     * @return
     */
    @Modifying
    @Query("DELETE FROM GjtDegreeRequirement t WHERE t.collegeId=:collegeId AND t.specialtyId=:specialtyId")
    @Transactional
    int deleteDegreeReqBySpecialty(@Param("collegeId") String collegeId, @Param("specialtyId") String specialtyId);

    /**
     * 删除不存在的学位条件
     * @param collegeId
     * @param specialtyId
     * @param ids
     * @return
     */
    @Modifying
	@Query("DELETE FROM GjtDegreeRequirement t WHERE t.collegeSpecialtyId=:collegeSpecialtyId AND t.requirementId NOT IN :ids")
    @Transactional
	int deleteNotExistInBatch(@Param("collegeSpecialtyId") String collegeSpecialtyId, @Param("ids") List<String> ids);

}
