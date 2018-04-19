/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.invoice;

import com.ouchgzee.headTeacher.dto.StudentInvoiceDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeeInvoice;
import com.ouchgzee.headTeacher.service.base.BaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * 发票业务接口<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtInvoiceService extends BaseService<BzrGjtFeeInvoice> {

	String[] TITLES = { "loginAccount", "receiver", "gradeName", "pycc", "zymc", "feeMethodType", "title", "invoiceFee",
			"delivery", "addr", "mobile", "zip", "remark", "applyDt" };

	/**
	 * 更新发票信息
	 * 
	 * @param feeInvoice
	 * @return
	 */
	boolean update(BzrGjtFeeInvoice feeInvoice);

	/**
	 * 发放发票
	 * 
	 * @param invoiceId
	 *            发票ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateInvoiceIssue(String invoiceId, String updatedBy);

	/**
	 * 分页根据条件查询班级学员的发票信息
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
     * @return
     */
	Page<BzrGjtFeeInvoice> queryFeeInvoiceByClassIdPage(String classId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 导出发票信息模板
	 * @return
	 */
	HSSFWorkbook exportFeeInvoiceTemplate();

	/**
	 * 发票申请 - 远程调接口
	 * @param info
	 * 			  发票信息
	 * @param operUser
	 *            操作人ID
	 * @return
     */
	boolean updateSimpleImportFeeInvoice(Map<String, Object> info, String operUser);

	/**
	 * 导入发票申请信息<br>
	 * 1.读取Exdcel文件流<br>
	 * 2.再解析<br>
	 * 3.最后批量保存发票申请信息<br>
	 * 
	 * @param url
	 *            Exdcel文件路径
	 * @param operUser
	 *            操作人ID
	 * @return 导入结果
	 */
	Map<String, Object> updateBatchImportFeeInvoice(String url, String operUser);

	/**
	 * 分页根据条件查询班级学员的发票信息 - 远程调接口
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentInvoiceDto> queryRemoteFeeInvoiceByClassIdPage(String classId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 导出发票信息<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	HSSFWorkbook exportFeeInvoiceToExcel(String classId, Map<String, Object> searchParams, Sort sort);
}
