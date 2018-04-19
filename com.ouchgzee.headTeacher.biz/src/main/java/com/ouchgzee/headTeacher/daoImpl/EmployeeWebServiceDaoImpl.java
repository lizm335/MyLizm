package com.ouchgzee.headTeacher.daoImpl;

import com.ouchgzee.headTeacher.dao.EmployeeWebServiceDao;
import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paul on 2017/2/14.
 * 班主任外部接口实现DAO
 *
 */
@Deprecated @Repository("bzrEmployeeWebServiceDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class EmployeeWebServiceDaoImpl extends BaseDaoImpl implements EmployeeWebServiceDao{



    @Override
    public List classList(BzrGjtEmployeeInfo employeeInfo) {
        StringBuilder querySql = new StringBuilder();
        querySql.append("  SELECT ");
        querySql.append("    GCI.BJMC AS className , GCI.CLASS_ID AS classId ");
        querySql.append("  FROM ");
        querySql.append("    GJT_CLASS_INFO GCI ");
        querySql.append("  WHERE GCI.BZR_ID = :EMPLOYEE_ID AND GCI.IS_DELETED = 'N'");
        Map param = new HashMap();
        param.put("EMPLOYEE_ID",employeeInfo.getEmployeeId());
        return  super.findAllBySql(querySql,param,new Sort("GCI.CREATED_DT"),Map.class);
    }

    @Override
    public List stuList(String classId) {
        StringBuilder querySql = new StringBuilder();
        querySql.append( "select t.student_id stuId, t.xm stuName , t.sjh stuPhone from gjt_student_info t inner join gjt_class_student b on t.student_id=b.student_id inner join gjt_class_info c on b.class_id=c.class_id");
        querySql.append(" where t.is_deleted='N' and b.is_deleted='N' and c.is_deleted='N' and c.class_id= :classId ");
        Map param = new HashMap();
        param.put("classId",classId);
        return super.findAllBySql(querySql,param,new Sort(Sort.Direction.DESC,"t.created_dt"),Map.class);
    }
}
