package com.gzedu.xlims.dao.home;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GjtMessageDataInfoDao extends BaseDaoImpl{
    @Autowired
    private CommonDao commonDao;

    @PersistenceContext(unitName = "entityManagerFactory")
    public EntityManager em;


    /**
     * 根据专业代码查找专业ID
     * @param searchParams
     * @return
     */
    public List getSpecialtyByCode(Map searchParams){
        Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	GS.SPECIALTY_ID,");
        sql.append("  	GS.ZYMC,");
        sql.append("  	GS.PYCC,");
        sql.append("  	GSB.SPECIALTY_CODE");
        sql.append("  FROM");
        sql.append("  	GJT_SPECIALTY GS");
        sql.append("  INNER JOIN GJT_SPECIALTY_BASE GSB ON");
        sql.append("  	GS.SPECIALTY_BASE_ID = GSB.SPECIALTY_BASE_ID");
        sql.append("  WHERE");
        sql.append("  	GS.IS_DELETED = 'N'");
        sql.append("  	AND GSB.IS_DELETED = 'N'");
        sql.append("  	AND GS.XX_ID = :xxId ");
        params.put("xxId", ObjectUtils.toString(searchParams.get("xxId")));

        if (EmptyUtils.isNotEmpty(searchParams.get("specialtyCodes"))){
            sql.append("  	AND GSB.SPECIALTY_CODE IN ("+ObjectUtils.toString(searchParams.get("specialtyCodes"))+")");
        }

        return commonDao.queryForMapListNative(sql.toString(),params);

    }


    /**
     * 查询院校下所有的学生
     * @param searchParams
     * @return
     */
    public List queryStudent(Map searchParams){
        Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	DISTINCT GUA.ID,");
        sql.append("  	GSI.ATID,");
        sql.append("  	GSI.XXZX_ID,");
        sql.append("  	GSI.XX_ID");
        sql.append("  FROM");
        sql.append("  	GJT_USER_ACCOUNT GUA");
        sql.append("  INNER JOIN GJT_STUDENT_INFO GSI ON");
        sql.append("  	GUA.ID = GSI.ACCOUNT_ID");

        if (StringUtils.isNotBlank(ObjectUtils.toString(searchParams.get("gradeIds")))){
            sql.append("  INNER JOIN GJT_GRADE GG ON");
            sql.append("  	GSI.NJ = GG.GRADE_ID");
        }

        if (StringUtils.isNotBlank(ObjectUtils.toString(searchParams.get("specialtyIds")))){
            sql.append("  INNER JOIN GJT_SPECIALTY GS ON");
            sql.append("  	GSI.MAJOR = GS.SPECIALTY_ID ");
        }

        sql.append("  WHERE");
        sql.append("  	GUA.IS_DELETED = 'N'");
        sql.append("  	AND GSI.IS_DELETED = 'N'");

        if (StringUtils.isNotBlank(ObjectUtils.toString(searchParams.get("gradeIds")))){
            sql.append("  	AND GG.IS_DELETED = 'N'");
            sql.append("  	AND GG.GRADE_ID IN ("+ObjectUtils.toString(searchParams.get("gradeIds"))+")");
        }

        if (StringUtils.isNotBlank(ObjectUtils.toString(searchParams.get("specialtyIds")))){
            sql.append("  	AND GS.IS_DELETED = 'N'");
            sql.append("  	AND GS.SPECIALTY_ID IN("+ObjectUtils.toString(searchParams.get("specialtyIds"))+")");
        }

        if (StringUtils.isNotBlank(ObjectUtils.toString(searchParams.get("pyccs")))){
            sql.append("  	AND GSI.PYCC IN ("+ObjectUtils.toString(searchParams.get("pyccs"))+")");
        }

        if (StringUtils.isNotBlank(ObjectUtils.toString(searchParams.get("xxzxIds")))){
            sql.append("  	AND GSI.XXZX_ID IN(");
            sql.append("  		SELECT");
            sql.append("  			org.ID");
            sql.append("  		FROM");
            sql.append("  			GJT_ORG org");
            sql.append("  		WHERE");
            sql.append("  			org.IS_DELETED = 'N' START WITH org.ID IN("+ObjectUtils.toString(searchParams.get("xxzxIds")));
            sql.append("  	CONNECT BY PRIOR org.ID = org.PERENT_ID ");
            sql.append("  	)");
        }else {
            sql.append("  	AND GSI.XX_ID IN(");
            sql.append("  		SELECT");
            sql.append("  			org.ID");
            sql.append("  		FROM");
            sql.append("  			GJT_ORG org");
            sql.append("  		WHERE");
            sql.append("  			org.IS_DELETED = 'N' START WITH org.ID=:xxId");
            sql.append("  	CONNECT BY PRIOR org.ID = org.PERENT_ID ");
            sql.append("  	)");
            params.put("xxId",ObjectUtils.toString(searchParams.get("xxId")));
        }

        sql.append("  ORDER BY");
        sql.append("  	GUA.ID");


        return commonDao.queryForMapListNative(sql.toString(),params);

    }


    public List getXXIdByCode(Map  searchParams){
        Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	DISTINCT org.ID,");
        sql.append("  	org.CODE");
        sql.append("  FROM");
        sql.append("  	GJT_ORG org");
        sql.append("  WHERE");
        sql.append("  	org.IS_DELETED = 'N'");
        sql.append("  	AND org.CODE IN("+ObjectUtils.toString(searchParams.get("learncenterCodes"))+ ")");

        return commonDao.queryForMapListNative(sql.toString(),params);

    }

}
