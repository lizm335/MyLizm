package com.gzedu.xlims.dao.organization;

import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 学期Dao层操作类
 * Created by paul on 2017/7/6.
 */

@Repository
public class GradeDaoImpl extends BaseDaoImpl {

    /**
     * 分页条件查询学期信息，SQL语句
     * @param xxId
     * @param termId
     * @param pageRequest
     * @return
     */
    public Page<Map> findGradeByPage(String xxId, String termId, PageRequest pageRequest) {
        Map<String, Object> parameters = new HashMap();
        StringBuilder querySql = new StringBuilder();
        querySql.append(" select * from gjt_grade t where t.is_deleted='N' and t.is_enabled='1'");
        querySql.append(" and t.xx_id=:xxId ");
        querySql.append(" and t.start_date >= (select x.start_date from gjt_grade x where x.grade_id = :termId)");
        querySql.append(" order by t.start_date");
        parameters.put("xxId", xxId);
        parameters.put("termId", termId);
        return super.findByPageToMap(querySql, parameters, pageRequest);
    }

}
