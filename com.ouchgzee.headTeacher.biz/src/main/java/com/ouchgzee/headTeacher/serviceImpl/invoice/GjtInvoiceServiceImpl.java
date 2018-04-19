/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.invoice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.ExcelReader;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.HttpClientUtils;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.invoice.GjtBillApplyRecordDao;
import com.ouchgzee.headTeacher.dao.invoice.GjtFeeInvoiceDao;
import com.ouchgzee.headTeacher.dao.signup.GjtSignupDao;
import com.ouchgzee.headTeacher.dao.student.GjtStudentInfoDao;
import com.ouchgzee.headTeacher.dto.StudentInvoiceDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtBillApplyRecord;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeeInvoice;
import com.ouchgzee.headTeacher.pojo.BzrGjtSignup;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.status.IssueStatus;
import com.ouchgzee.headTeacher.service.invoice.BzrGjtInvoiceService;
import com.ouchgzee.headTeacher.service.remote.BillRemoteService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtInvoiceServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtInvoiceServiceImpl extends BaseServiceImpl<BzrGjtFeeInvoice> implements BzrGjtInvoiceService {

	private static Log log = LogFactory.getLog(GjtInvoiceServiceImpl.class);

	@Autowired
	private BillRemoteService billRemoteService;

	@Autowired
	private GjtFeeInvoiceDao gjtFeeInvoiceDao;

	@Autowired
	private GjtBillApplyRecordDao gjtBillApplyRecordDao;

	@Autowired
	private GjtSignupDao gjtSignupDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Override
	protected BaseDao<BzrGjtFeeInvoice, String> getBaseDao() {
		return gjtFeeInvoiceDao;
	}

	@Override
	public Page<BzrGjtFeeInvoice> queryByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(new Sort.Order("issueStatus"), new Sort.Order(Direction.DESC, "createdDt")));
		}
		return super.queryByPage(searchParams, pageRequest);
	}

	@Override
	public List<BzrGjtFeeInvoice> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(new Sort.Order("issueStatus"), new Sort.Order(Direction.DESC, "createdDt"));
		}
		return super.queryBy(searchParams, sort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ouchgzee.headTeacher.service.invoice.GjtInvoiceService#update(com.
	 * ouchgzee.headTeacher.pojo.GjtFeeInvoice)
	 */
	@Override
	public boolean update(BzrGjtFeeInvoice feeInvoice) {
		if (StringUtils.isNoneBlank(feeInvoice.getInvoiceId())) {
			feeInvoice.setUpdatedDt(new Date());
			BzrGjtFeeInvoice invoice = gjtFeeInvoiceDao.save(feeInvoice);
			return invoice != null;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ouchgzee.headTeacher.service.invoice.GjtInvoiceService#
	 * updateInvoiceIssue(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateInvoiceIssue(String invoiceId, String updatedBy) {
		gjtFeeInvoiceDao.updateIssueStatus(invoiceId, IssueStatus.FINISH.getValue() + "", updatedBy, new Date());
		return true;
	}

	@Override
	public Page<BzrGjtFeeInvoice> queryFeeInvoiceByClassIdPage(String classId, Map<String, Object> searchParams,
															   PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(new Sort.Order("issueStatus"), new Sort.Order(Direction.DESC, "createdDt")));
		}
		Criteria<BzrGjtFeeInvoice> spec = new Criteria();
		spec.distinct(true);
		// 设置连接方式，如果是内连接可省略掉
		spec.setJoinType("gjtSignupFee", JoinType.LEFT);

		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.or(Restrictions.isNull("gjtSignupFee.feeId"), Restrictions.eq("gjtSignupFee.isDeleted", Constants.BOOLEAN_NO, true)));
		spec.add(Restrictions.eq("gjtBillApplyRecord.gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtBillApplyRecord.gjtStudentInfo.gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtBillApplyRecord.gjtStudentInfo.gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtBillApplyRecord.gjtStudentInfo.gjtUserAccount.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtBillApplyRecord.gjtStudentInfo.gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		Page<BzrGjtFeeInvoice> result = gjtFeeInvoiceDao.findAll(spec, pageRequest);
		return result;
	}

	@Override
	public HSSFWorkbook exportFeeInvoiceTemplate() {
		try {
			HSSFWorkbook book = null;
			InputStream in = this.getClass().getResourceAsStream("/template/FeeInvoice.xlt");
			book = new HSSFWorkbook(in);
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ouchgzee.headTeacher.service.invoice.GjtInvoiceService#
	 * updateSimpleImportFeeInvoice(java.util.Map)
	 */
	public boolean updateSimpleImportFeeInvoice(Map<String, Object> info, String operUser) {
		String studentId = (String) info.get("studentId");
		BzrGjtStudentInfo gjtStudentInfo = gjtStudentInfoDao.findOne(studentId);

		Criteria<BzrGjtSignup> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.studentId", studentId, true));
		BzrGjtSignup signup = gjtSignupDao.findOne(spec);
		// 订单号
		String orderSn = signup.getOrderSn();
		if(StringUtils.isBlank(orderSn))
			return false;

		String delivery = null;
		String deliveryStr = (String) info.get("delivery");
		if(StringUtils.isNotBlank(deliveryStr)) {
			String[] delArr = deliveryStr.split("-");
			if(delArr.length >= 2)
				delivery = delArr[0];
		}
		String mobile = null;
		String mobileStr = (String) info.get("mobile");
		if(StringUtils.isNotBlank(mobileStr)) {
			mobile = mobileStr;
		} else {
			mobile = gjtStudentInfo.getSjh();
		}
		Map<String, Object> args = new HashMap();
		args.put("formMap.ORDER_ID", orderSn);
		args.put("formMap.INVOICE_NAME", info.get("title"));// 发票抬头
		args.put("formMap.INVOICE_DESC", info.get("remark"));// 发票用途说明
		args.put("formMap.LOGISTICS_WAY", delivery);// 配送方式
		args.put("formMap.RECEIVER_USER", info.get("receiver"));// 收件人姓名
		args.put("formMap.RECEIVER_PHONE", mobile);// 手机号码
		args.put("formMap.RECEIVER_ADDRESS", info.get("addr"));// 快递地址
		args.put("formMap.RECEIVER_ZIPCODE", info.get("zip"));// 邮政编码

		String INVOICE_SYNC_URI = AppConfig.getProperty("api.delivery.sync");
		String rsp = HttpClientUtils.doHttpPost(INVOICE_SYNC_URI, args, Constants.TIMEOUT, Constants.CHARSET_GBK);

		if (StringUtils.isNotBlank(rsp)) {
			Gson gson = new GsonBuilder().create();
			Map json = gson.fromJson(rsp, HashMap.class);
			String result = ObjectUtils.toString(json.get("result"));
			if (!StringUtils.equals("1", result)) {
				String msg = ObjectUtils.toString(json.get("msg"));
				log.error(msg);
				return false;
			}
		}
		String syncUrl = this.getURL(INVOICE_SYNC_URI, args);

		Map<String, Object> queryArgs = new HashMap();
		queryArgs.put("formMap.PROJECT_CODE", Constants.PROJECT_CODE);
		queryArgs.put("formMap.USER_ACCOUNT", gjtStudentInfo.getSfzh());
		queryArgs.put("formMap.USER_NAME", gjtStudentInfo.getXm());
		String queryUrl = this.getURL(AppConfig.getProperty("api.delivery.query"), queryArgs);

		Date now = new Date();
		if(info.get("applyDt") != null) {
			try {
				now = DateUtils.parseDate((String) info.get("applyDt"), "yyyy-MM-dd");
			} catch (ParseException e) {
				log.error("时间类型转化异常", e);
			}
		}
		BzrGjtBillApplyRecord gjtBillApplyRecord = new BzrGjtBillApplyRecord();
		gjtBillApplyRecord.setId(UUIDUtils.random());
		gjtBillApplyRecord.setGjtStudentInfo(new BzrGjtStudentInfo(studentId));
		gjtBillApplyRecord.setTitle((String) info.get("title"));
		gjtBillApplyRecord.setRemark((String) info.get("remark"));
		gjtBillApplyRecord.setDelivery(delivery);
		gjtBillApplyRecord.setReceiver((String) info.get("receiver"));
		gjtBillApplyRecord.setMobile(mobile);
		gjtBillApplyRecord.setAddr((String) info.get("addr"));
		gjtBillApplyRecord.setProvince(null);
		gjtBillApplyRecord.setCity(null);
		gjtBillApplyRecord.setArea(null);
		gjtBillApplyRecord.setZip((String) info.get("zip"));
		gjtBillApplyRecord.setSyncUrl(syncUrl);
		gjtBillApplyRecord.setQueryUrl(queryUrl);
		gjtBillApplyRecord.setCreatedBy(operUser);
		gjtBillApplyRecord.setCreatedDt(now);
		gjtBillApplyRecordDao.save(gjtBillApplyRecord);
		return true;
	}

	/**
	 * 请求链接拼接
	 * @param url
	 * @param args
     * @return
     */
	private String getURL(String url, Map<String, Object> args) {
		StringBuffer urlBuff = new StringBuffer(500);
		urlBuff.append(url);
		boolean flag = false;
		for (String key : args.keySet()) {
			if (flag) {
				urlBuff.append("&");
			} else {
				urlBuff.append("?");
				flag = true;
			}
			urlBuff.append(key).append("=").append(args.get(key));
		}
		return urlBuff.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ouchgzee.headTeacher.service.invoice.GjtInvoiceService#
	 * updateBatchImportFeeInvoice(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> updateBatchImportFeeInvoice(String url, String operUser) {
		File file = new File(url);
		Map<String, Object> result = new HashMap<String, Object>();

		int startRowNum = 2;
		ExcelReader reader = new ExcelReader(file, TITLES, startRowNum);
		List invoiceList = reader.readAsList();

		List<Map> succList = new ArrayList();
		List<Map> failList = new ArrayList();
		for (Object obj : invoiceList) {
			Map<String, Object> info = (Map<String, Object>) obj;

			BzrGjtStudentInfo gjtStudentInfo = gjtStudentInfoDao.findByLoginAccount(info.get(TITLES[0]).toString());
			if(gjtStudentInfo != null) {
				info.put("studentId", gjtStudentInfo.getStudentId());
				boolean flag = this.updateSimpleImportFeeInvoice(info, operUser);
				if(flag) {
					succList.add(info);
					log.debug("==> 发票申请信息 [Success]：" + info);
				} else {
					failList.add(info);
					log.debug("==> 发票申请信息 [Fail]：" + info);
				}
			} else {
				failList.add(info);
				log.debug("==> 发票申请信息 [Fail]：" + info);
			}
		}
		result.put("succList", succList);
		result.put("failList", failList);
		return result;
	}

	@Override
	public Page<StudentInvoiceDto> queryRemoteFeeInvoiceByClassIdPage(String classId, Map<String, Object> searchParams,
																		  PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtUserAccount.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSpecialty.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSignup.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		Page<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, pageRequest);

		List<StudentInvoiceDto> stuInvoiceList = this.encapsulationInvoiceDto(result.getContent());
		return new PageImpl(stuInvoiceList, pageRequest, result.getTotalElements());
	}

	/**
	 * 共用方法 - 封装发票信息
	 * @param result
	 * @return
     */
	private List<StudentInvoiceDto> encapsulationInvoiceDto(List<BzrGjtStudentInfo> result) {
		List<StudentInvoiceDto> stuInvoiceList = new ArrayList();
		for (Iterator<BzrGjtStudentInfo> iter = result.iterator(); iter.hasNext();) {
			BzrGjtStudentInfo info = iter.next();

			StudentInvoiceDto invoiceDto = new StudentInvoiceDto();
			invoiceDto.setStudentId(info.getStudentId());
			invoiceDto.setXm(info.getXm());
			invoiceDto.setXbm(info.getXbm()); //性别
			invoiceDto.setXh(info.getXh()); //学号
			invoiceDto.setSjh(info.getSjh()); //手机号
			invoiceDto.setPycc(info.getPycc()); //层次
			invoiceDto.setSfzh(info.getSfzh());
			invoiceDto.setZymc(info.getGjtSpecialty().getZymc());
			invoiceDto.setGradeName(info.getGjtSignup().getGjtEnrollBatch().getGjtGrade().getGradeName());
			Map tempMap = this.getPaymentInfo(info.getSfzh());
			invoiceDto.setGkxlPaymentTpye((String) tempMap.get("GKXL_PAYMENT_TPYE"));
			List<Map> invoiceList = this.getInvoice(info.getSfzh(), info.getXm());
			if(invoiceList != null && invoiceList.size() > 0) {
				// 已发放次数
				int issueNum = 0;
				// 总申请次数
				int totalApplyNum = invoiceList.size();
				for (Map i : invoiceList) {
					String state = (String) i.get("STATE");
					if(!"A".equals(state)) {
						issueNum++;
					}
				}
				invoiceDto.setIssueNum(issueNum);
				invoiceDto.setTotalApplyNum(totalApplyNum);
				invoiceDto.setState((String) invoiceList.get(0).get("STATE"));
			}
			invoiceDto.setInvoiceList(invoiceList);

			stuInvoiceList.add(invoiceDto);
		}
		return stuInvoiceList;
	}

	@Override
	public HSSFWorkbook exportFeeInvoiceToExcel(String classId, Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtUserAccount.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSpecialty.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSignup.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		List<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, sort);

		List<StudentInvoiceDto> stuInvoiceList = this.encapsulationInvoiceDto(result);
		try {
			HSSFWorkbook book = null;
			InputStream in = this.getClass().getResourceAsStream("/template/Export_FeeInvoice.xls");
			book = new HSSFWorkbook(in);
			HSSFSheet sheet = book.getSheetAt(0);
			HSSFRow row = null;

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			sheet.createFreezePane(0, 1); //冻结列：冻结0列1行

			for (Iterator<StudentInvoiceDto> iter = stuInvoiceList.iterator(); iter.hasNext();) {
				StudentInvoiceDto info = iter.next();

				if(info.getInvoiceList() != null && info.getInvoiceList().size() > 0) {
					for(Map i : info.getInvoiceList()) {
						row = sheet.createRow(rowIdx++);
						colIdx = 0;

						row.createCell(colIdx++).setCellValue(idx++);
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getXm())));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getGradeName())));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getZymc())));
						String gkxlPaymentTpye = "";
						if("A".equals(info.getGkxlPaymentTpye())) {
							gkxlPaymentTpye = "全额缴费";
						} else if("B".equals(info.getGkxlPaymentTpye())) {
							gkxlPaymentTpye = "首年缴费";
						} else if("C".equals(info.getGkxlPaymentTpye())) {
							gkxlPaymentTpye = "分期付款";
						}
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(gkxlPaymentTpye)));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getIssueNum()+"/"+info.getTotalApplyNum())));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(i.get("INVOICE_NAME"))));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(i.get("INVOICE_AMOUNT"))));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(null)));
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(i.get("INVOICE_TIME"))));
						String invoiceState = "";
						if("A".equals(i.get("STATE"))) {
							invoiceState = "已申请";
						} else if("B".equals(i.get("STATE"))) {
							invoiceState = "已开具";
						} else if("C".equals(i.get("STATE"))) {
							invoiceState = "已发放";
						} else if("D".equals(i.get("STATE"))) {
							invoiceState = "已领取";
						}
						row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(invoiceState)));
					}
				} else {
					row = sheet.createRow(rowIdx++);
					colIdx = 0;

					row.createCell(colIdx++).setCellValue(idx++);
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getXm())));
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getGradeName())));
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getZymc())));
					String gkxlPaymentTpye = "";
					if("A".equals(info.getGkxlPaymentTpye())) {
						gkxlPaymentTpye = "全额缴费";
					} else if("B".equals(info.getGkxlPaymentTpye())) {
						gkxlPaymentTpye = "首年缴费";
					} else if("C".equals(info.getGkxlPaymentTpye())) {
						gkxlPaymentTpye = "分期付款";
					}
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(gkxlPaymentTpye)));
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString("0/0")));
				}
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 远程获取学员发票信息
	 * @param userAccount
	 * @param userName
     * @return
     */
	private List<Map> getInvoice(String userAccount, String userName) {
		Map params = new HashMap();
		params.put("formMap.PROJECT_CODE", Constants.PROJECT_CODE);
		params.put("formMap.USER_ACCOUNT", userAccount);
		params.put("formMap.USER_NAME", userName);

		String rsp = HttpClientUtils.doHttpGet(AppConfig.getProperty("api.delivery.query"), params, Constants.TIMEOUT, Constants.CHARSET_GBK);

		if (StringUtils.isNotBlank(rsp)) {
			Gson gson = new GsonBuilder().create();
			Map map = gson.fromJson(rsp, HashMap.class);
			String result = ObjectUtils.toString(map.get("result"));
			if (StringUtils.equals("1", result)) {
				return (List<Map>) map.get("dataList");
			} else {
				log.warn("==> 发票查询失败:"+ map.get("msg"));
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 远程获取用户订单费用缴纳明细
	 * @param sfzh
	 * @return
	 */
	private Map getPaymentInfo(String sfzh) {
		List<Map> list = billRemoteService.getBillList(sfzh, BillRemoteService.BillType.XF.name());
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return Collections.emptyMap();
	}

}
