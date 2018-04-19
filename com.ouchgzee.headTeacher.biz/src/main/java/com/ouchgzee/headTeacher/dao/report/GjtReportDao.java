/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.dao.report;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtReport;
import org.springframework.stereotype.Repository;

/**
 * 日报操作类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月27日
 * @version 1.0
 *
 */
@Deprecated @Repository("bzrGjtReportDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtReportDao extends BaseDao<BzrGjtReport, String> {

}
