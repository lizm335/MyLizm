/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.graduate;

import com.ouchgzee.headTeacher.pojo.BzrGjtGraduateStandard;
import com.ouchgzee.headTeacher.pojo.BzrGjtGraduationStu;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 毕业业务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtGraduateStandardService extends BaseService<BzrGjtGraduateStandard> {

    /**
     * 获取学员的毕业信息
     * @param studentId
     * @return
     */
    BzrGjtGraduationStu queryGraduationStuByStuId(String studentId);

}
