/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.invoice;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtBillApplyRecord;
import org.springframework.stereotype.Repository;

/**
 * 发票申请记录信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月21日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtBillApplyRecordDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtBillApplyRecordDao extends BaseDao<BzrGjtBillApplyRecord, String> {

}