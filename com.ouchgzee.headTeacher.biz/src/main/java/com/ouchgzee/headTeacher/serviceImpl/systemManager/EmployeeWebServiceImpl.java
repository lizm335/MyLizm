package com.ouchgzee.headTeacher.serviceImpl.systemManager;

import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.ouchgzee.headTeacher.dao.EmployeeWebServiceDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.BzrEmployeeWebService;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paul on 2017/2/14.
 * 班主任对外接口服务
 */
@Deprecated @Service("bzrEmployeeWebServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class EmployeeWebServiceImpl implements BzrEmployeeWebService {

    @Autowired
    private EmployeeWebServiceDao employeeWebServiceDao;

    @Override
    public List classList(BzrGjtEmployeeInfo employeeInfo) {
        return employeeWebServiceDao.classList(employeeInfo);
    }

    @Override
    public Map stuList(String classId,String oldStus ) {
        Map re = new HashMap<String, List>();
        List<Map> allStus = employeeWebServiceDao.stuList(classId);
        List<Map> chooseStus = new ArrayList();
        String[] oldStusList = oldStus.split(",");
        for (Map stu : allStus) {
            for (String old : oldStusList) {
                if (StringUtils.isNotEmpty(old) && ObjectUtils.toString(stu.get("STUID")).equals(old)) {
                    chooseStus.add(stu);
                }
            }
        }
        allStus.removeAll(chooseStus);
        re.put("unchooseStus", allStus);
        re.put("chooseStus", chooseStus);
        return re;
    }

}
