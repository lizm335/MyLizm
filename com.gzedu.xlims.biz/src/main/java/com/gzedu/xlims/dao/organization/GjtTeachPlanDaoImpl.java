package com.gzedu.xlims.dao.organization;



import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 2017/4/27.
 */

@Repository
public class GjtTeachPlanDaoImpl extends BaseDaoImpl {

    public List<Map> findTeach(Map formMap){
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append(" SELECT  gtp.SUBJECT_ID,GTP.COURSE_ID,GTP.KSFS FROM VIEW_TEACH_PLAN GTP, GJT_COURSE GC  ");
        sbuilder.append(" WHERE GTP.GRADE_ID =:gradeId  AND GC.IS_DELETED = 'N' AND GTP.IS_DELETED ='N'  ");
        sbuilder.append(" AND GTP.KKZY =:kkzy ");
        sbuilder.append(" AND GTP.COURSE_ID = GC.COURSE_ID AND GC.KCMC =:kcmc  ");
        return super.findAllBySql(sbuilder , formMap, null, Map.class);
    }

}
