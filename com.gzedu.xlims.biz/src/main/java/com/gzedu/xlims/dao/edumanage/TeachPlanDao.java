package com.gzedu.xlims.dao.edumanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtTeachPlan;

/**
 * Created by paul on 2017/8/8.
 */
@Repository
public class TeachPlanDao extends BaseDaoImpl {
    
    @Autowired
	private CommonDao commonDao;

    /** 视图获取教学计划*/
    public List<GjtTeachPlan> findView(String gradeSpecialtyId, String courseId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuilder sql = new StringBuilder("select GTP.* FROM VIEW_TEACH_PLAN VEP, GJT_TEACH_PLAN GTP WHERE GTP.IS_DELETED = 'N' AND GTP.TEACH_PLAN_ID = VEP.TEACH_PLAN_ID ");
    	sql.append(" AND VEP.GRADE_SPECIALTY_ID = :gradeSpecialtyId AND VEP.COURSE_ID = :courseId");
        params.put("gradeSpecialtyId", gradeSpecialtyId);
        params.put("courseId", courseId);
        List<GjtTeachPlan> list = super.findAllBySql(sql, params, null, GjtTeachPlan.class);
        return list;
    }

    /** 视图获取教学计划*/
    public Map findOne(String teachPlanId) {
    	Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("SELECT VEP.* FROM VIEW_TEACH_PLAN VEP WHERE VEP.TEACH_PLAN_ID=:teachPlanId");
        params.put("teachPlanId", teachPlanId);
        List<Map> list = super.findAllByToMap(sql, params, null);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**根据课程号跟学生 在选课获取课程数据 */
    public List<GjtCourse> findCourseByStuAndCourseNo(String stuId, String courseNo) {

        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT ");
        sql.append("  	GC.*  ");
        sql.append("  FROM ");
        sql.append("  	GJT_REC_RESULT GRR,");
        sql.append("  	GJT_COURSE GC");
        sql.append("  WHERE");
        sql.append("  	GC.IS_DELETED = 'N'");
        sql.append("  AND GRR.IS_DELETED = 'N'");
        sql.append("  AND GC.COURSE_ID = GRR.COURSE_ID");
        sql.append("  AND GC.KCH = '"+ courseNo +"'");
        sql.append("  AND GRR.STUDENT_ID = '"+   stuId +"'");

        Query query = em.createNativeQuery(sql.toString(), GjtCourse.class);
        return  query.getResultList();
    }
    
    /**根据课程号跟学生 在选课获取课程数据 */
    public List  getCourseByStuAndCourseNo(String stuId, String courseNo) {

        StringBuffer sql = new StringBuffer();
        Map param = new HashMap();
        
        sql.append("  SELECT VTP.COURSE_ID, VTP.SOURCE_COURSE_ID,GRR.EXAM_SCORE");
        sql.append("  FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN VTP");
        sql.append("  WHERE GRR.IS_DELETED = 'N'");
        sql.append("  AND VTP.IS_DELETED = 'N'");
        sql.append("  AND GRR.TEACH_PLAN_ID = VTP.TEACH_PLAN_ID");
        sql.append("  AND (VTP.KCH =:courseNo OR VTP.SOURCE_KCH =:courseNo)");
        sql.append("  AND GRR.STUDENT_ID =:STUDENT_ID");
        param.put("courseNo", courseNo);
        param.put("STUDENT_ID", stuId);
//        Query query = em.createNativeQuery(sql.toString(), GjtCourse.class);
//        return  query.getResultList();
        return commonDao.queryForMapListNative(sql.toString(), param);
    }
}
