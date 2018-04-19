/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.invoice;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeeInvoice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 出具发票信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtFeeInvoiceDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtFeeInvoiceDao extends BaseDao<BzrGjtFeeInvoice, String> {

	/**
	 * 更改发票的状态
	 * 
	 * @param invoiceId
	 * @param issueStatus
	 * @param updatedBy
	 * @param updatedDt
	 */
	@Modifying
	@Query("UPDATE BzrGjtFeeInvoice i SET i.version=i.version+1,i.issueStatus=:issueStatus,i.updatedBy=:updatedBy,i.updatedDt=:updatedDt WHERE i.invoiceId=:invoiceId")
	@Transactional(value="transactionManagerBzr")
	int updateIssueStatus(@Param("invoiceId") String invoiceId, @Param("issueStatus") String issueStatus,
			@Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

}
