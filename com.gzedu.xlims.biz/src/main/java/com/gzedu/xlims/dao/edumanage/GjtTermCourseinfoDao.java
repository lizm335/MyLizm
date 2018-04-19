/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;

/**
 * 学期课程信息操作类
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年7月5日
 * @version 1.0
 *
 */
public interface GjtTermCourseinfoDao extends BaseDao<GjtTermCourseinfo, String> {

    /**
     * 获取院校下的年级课程期课程信息
     * @param xxId
     * @param courseId
     * @param termId
     * @return
     */
    GjtTermCourseinfo findByXxIdAndTermIdAndCourseId(String xxId, String termId, String courseId);

}
