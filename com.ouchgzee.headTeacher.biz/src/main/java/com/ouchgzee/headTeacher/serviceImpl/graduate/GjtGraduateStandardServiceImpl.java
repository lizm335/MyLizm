/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.graduate;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.graduate.GjtGraduateStandardDao;
import com.ouchgzee.headTeacher.dao.graduate.GjtGraduationStuDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtGraduateStandard;
import com.ouchgzee.headTeacher.pojo.BzrGjtGraduationStu;
import com.ouchgzee.headTeacher.service.graduate.BzrGjtGraduateStandardService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtGraduateStandardServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduateStandardServiceImpl extends BaseServiceImpl<BzrGjtGraduateStandard> implements BzrGjtGraduateStandardService {

    @Autowired
    private GjtGraduateStandardDao gjtGraduateStandardDao;

    @Autowired
    private GjtGraduationStuDao gjtGraduationStuDao;

    @Override
    protected BaseDao<BzrGjtGraduateStandard, String> getBaseDao() {
        return gjtGraduateStandardDao;
    }

    @Override
    public BzrGjtGraduationStu queryGraduationStuByStuId(String studentId) {
        Criteria<BzrGjtGraduationStu> spec = new Criteria();
        spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
        spec.add(Restrictions.eq("gjtStudentInfo.studentId", studentId, true));
        List<BzrGjtGraduationStu> graduationStuList = gjtGraduationStuDao.findAll(spec);
        return (graduationStuList != null && graduationStuList.size() > 0) ? graduationStuList.get(0) : null;
    }
}
