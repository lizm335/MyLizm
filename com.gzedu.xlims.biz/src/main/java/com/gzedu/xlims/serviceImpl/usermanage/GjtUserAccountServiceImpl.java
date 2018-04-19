/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.usermanage;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.ZipFileUtil;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.SSOUtil;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSetOrgCopyrightDao;
import com.gzedu.xlims.dao.usermanage.GjtEmployeeInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountNativeDao;
import com.gzedu.xlims.dao.usermanage.GjtUserBehaviorDao;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtUserBehavior;
import com.gzedu.xlims.pojo.TblPriLoginLog;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.home.HomeService;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.vo.Todo;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 
 * 功能说明：用户管理实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtUserAccountServiceImpl extends BaseServiceImpl<GjtUserAccount> implements GjtUserAccountService {

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private GjtUserAccountNativeDao gjtUserAccountNativeDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtSetOrgCopyrightDao gjtSetOrgCopyrightDao;

	@Autowired
	private GjtEmployeeInfoDao gjtEmployeeInfoDao;

	@Autowired
	private HomeService homeService;

	@Autowired
	private GjtUserBehaviorDao gjtUserBehaviorDao;

	@Autowired
	private TblPriLoginLogService tblPriLoginLogService;

	@Override
	protected BaseDao<GjtUserAccount, String> getBaseDao() {
		return this.gjtUserAccountDao;
	}

	@Override
	public int updatePwd(String id, String mdPwd, String pwd2) {
		return gjtUserAccountDao.updatePwd(id, mdPwd, pwd2);
	}

	@Override
	public int updateIsEnabled(String id, Integer isEnabled) {
		return gjtUserAccountDao.updateIsEnabled(id, isEnabled);
	}

	/**
	 * 查询帐户是否存在
	 * 
	 * 存在返回ture反之false
	 * 
	 */
	@Override
	public Boolean existsByLoginAccount(String loginAccount) {
		GjtUserAccount user = gjtUserAccountDao.findByLoginAccount(loginAccount);
		if (user != null && "N".equals(user.getIsDeleted())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据用户帐号查询帐号，因为数据库有唯一索引，所以删除了也算存在不能在建
	 */
	@Override
	public GjtUserAccount queryByLoginAccount(String loginAccount) {
		return gjtUserAccountDao.findByLoginAccount(loginAccount);
	}

	@Override
	public GjtUserAccount queryBySfzh(String sfzh) {
		return gjtUserAccountDao.findBySfzh(sfzh);
	}

	@Override
	public GjtUserAccount saveEntity(String xm, String loginAccount, String orgId, Integer userType) {
		GjtUserAccount gjtUserAccount = new GjtUserAccount();
		String userId = UUIDUtils.random();
		gjtUserAccount.setId(userId); // id
		gjtUserAccount.setLoginAccount(loginAccount); // 登陆帐号
		gjtUserAccount.setPassword(Md5Util.encode(Constants.ADMIN_USER_ACCOUNT_PWD_DEFAULT)); // 密码
		gjtUserAccount.setPassword2(Constants.ADMIN_USER_ACCOUNT_PWD_DEFAULT); // 密码
		gjtUserAccount.setRealName(xm); // 姓名
		// gjtUserAccount.setUserType(UserTypeEnum.教师.getNum());// 用户类型
		gjtUserAccount.setUserType(userType);// 用户类型
		GjtOrg org = gjtOrgDao.findOne(orgId);
		if (org != null) {
			gjtUserAccount.setGjtOrg(org);
		}
		gjtUserAccount.setIsSuperMgr(false);
		gjtUserAccount.setIsSync(false);
		gjtUserAccount.setVersion(new BigDecimal(1));// 版本
		// gjtUserAccount.setDataScope("0");// 查看数据范围
		gjtUserAccount.setIsEnabled(true);// 帐号是否启用
		// gjtUserAccount.setEeno("1280859");// EE帐号
		gjtUserAccount.setIsDeleted("N");// 是否删除
		gjtUserAccount.setCreateTime(DateUtils.getDate());// 创建日期
		gjtUserAccount.setCreatedDt(DateUtils.getDate());// 创建时间
		gjtUserAccount.setUpdatedDt(DateUtils.getDate());// 修改时间
		return gjtUserAccountDao.save(gjtUserAccount);
	}

	@Override
	public Boolean deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtUserAccountDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	@Override
	public Boolean deleteById(String id) {
		int i = gjtUserAccountDao.deleteById(id, "Y");
		return i == 1 ? true : false;
	}

	@Override
	public void delete(List<String> ids) {
		gjtUserAccountDao.delete(gjtUserAccountDao.findAll(ids));
	}

	@Override
	public GjtUserAccount findOne(String id) {
		return gjtUserAccountDao.findOne(id);
	}

	@Override
	public boolean insert(GjtUserAccount entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setPassword(Md5Util.encode(entity.getPassword2())); // 密码
		entity.setIsDeleted("N");
		entity.setIsSuperMgr(false);
		entity.setIsSync(false);
		entity.setUserType(0);
		GjtUserAccount userAccount = gjtUserAccountDao.save(entity);
		return userAccount != null;
	}

	@Override
	public void update(GjtUserAccount entity) {
		entity.setUpdatedDt(new Date());
		gjtUserAccountDao.save(entity);
	}

	@Override
	public Page<Map<String, Object>> queryPage(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtUserAccountNativeDao.queryPage(orgId, searchParams, pageRequst);
	}

	@Override
	public List<Map<String, Object>> queryUserRoleByOrg(String orgId, List<String> roleIds) {
		return gjtUserAccountNativeDao.queryUserRoleByOrg(orgId, roleIds);
	}

	@Override
	public Page<Map<String, Object>> queryWorkStatisticsByPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Page<Map<String, Object>> page = gjtUserAccountNativeDao.queryWorkStatisticsByPage(orgId, searchParams,
				pageRequst);
		for (Iterator<Map<String, Object>> iter = page.iterator(); iter.hasNext();) {
			Map<String, Object> info = iter.next();
			String id = (String) info.get("id");
			List<Todo> todoList = homeService.getTodoList(findOne(id));
			int totalTodo = 0;
			for (Todo todo : todoList) {
				totalTodo += todo.getTotal();
			}
			info.put("totalTodo", totalTodo);
		}
		return page;
	}

	@Override
	public List<Map<String, Object>> queryWorkStatisticsBy(String orgId, Map<String, Object> searchParams) {
		List<Map<String, Object>> list = gjtUserAccountNativeDao.queryWorkStatisticsBy(orgId, searchParams);
		return list;
	}

	@Override
	public long countWorkStatisticsBy(String orgId, Map<String, Object> searchParams) {
		return gjtUserAccountNativeDao.countWorkStatisticsBy(orgId, searchParams);
	}

	@Override
	public Map<String, Object> countWorkStatisticsMutualSubjectByAccountId(String accountId) {
		return gjtUserAccountNativeDao.countWorkStatisticsMutualSubjectByAccountId(accountId);
	}

	@Override
	public Map<String, Object> countProLoginLogByAccountId(String accountId, Map<String, Object> searchParams) {
		return gjtUserAccountNativeDao.countProLoginLogByAccountId(accountId, searchParams);
	}

	@Override
	public String exportWorkStatistics(String orgId, Map searchParams, String path) {
		List<Map<String, Object>> resultMap = gjtUserAccountNativeDao.queryWorkStatisticsBy(orgId, searchParams);

		try {
			String[] titles = new String[] { "姓名", "帐号", "手机号", "角色", "院校", "待办事项", "答疑回复率", "答疑回复及时率", "登录总次数",
					"登录总时长", "最近登录日期", "当前在线状态" };
			String[] dbNames = new String[] { "realName", "loginAccount", "sjh", "roleName", "orgName", "totalTodo",
					"reversionRate", "reversionInTimeRate", "loginCount", "totalLoginTime", "lastLoginTime",
					"isOnline" };

			for (Map info : resultMap) {
				String id = (String) info.get("id");
				BigDecimal reversionRate = (BigDecimal) info.get("reversionRate");
				if (reversionRate != null) {
					info.put("reversionRate", reversionRate.multiply(new BigDecimal(100)).intValue() + "%");
				}
				BigDecimal reversionInTimeRate = (BigDecimal) info.get("reversionInTimeRate");
				if (reversionInTimeRate != null) {
					info.put("reversionInTimeRate", reversionInTimeRate.multiply(new BigDecimal(100)).intValue() + "%");
				}
				BigDecimal totalLoginTime = (BigDecimal) info.get("totalLoginTime");
				if (totalLoginTime != null) {
					info.put("totalLoginTime",
							totalLoginTime.divide(new BigDecimal(60), 1, BigDecimal.ROUND_HALF_UP) + "小时");
				}
				Date lastLoginTime = (Date) info.get("lastLoginTime");
				if (lastLoginTime != null) {
					info.put("lastLoginTime", DateFormatUtils.format(lastLoginTime, "yyyy-MM-dd HH:mm:ss"));
				}
				String isOnline = (String) info.get("isOnline");
				BigDecimal leftDay = (BigDecimal) info.get("leftDay");
				if (isOnline != null) {
					info.put("isOnline", Constants.BOOLEAN_YES.equals(isOnline) ? "在线"
							: "离线\r\n" + (leftDay == null ? "（从未登录）" : "（" + leftDay.intValue() + "天未登录）"));
				}
				// 待办事项
				List<Todo> todoList = homeService.getTodoList(this.findOne(id));
				int totalTodo = 0;
				for (Todo todo : todoList) {
					totalTodo += todo.getTotal();
				}
				info.put("totalTodo", totalTodo + "");
			}
			String fileName = ExcelService.renderExcel(resultMap, titles, dbNames, "工作统计表", path,
					"工作统计表_" + Calendar.getInstance().getTimeInMillis());
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String exportWorkStatisticsDetail(String orgId, Map searchParams, String path) {
		String folderName = "工作统计明细_" + Calendar.getInstance().getTimeInMillis();
		String detailFolderPath = path + folderName;
		List<Map<String, Object>> resultMap = gjtUserAccountNativeDao.queryWorkStatisticsBy(orgId, searchParams);

		try {
			String[] titles = new String[] { "姓名", "帐号", "手机号", "角色", "院校", "待办事项", "答疑回复率", "答疑回复及时率", "待解答",
					"超过24小时未解答", "已解答", "超过24小时解答", "答疑总数", "登录总次数", "登录总时长", "最近登录日期", "当前在线状态" };
			String[] dbNames = new String[] { "realName", "loginAccount", "sjh", "roleName", "orgName", "totalTodo",
					"reversionRate", "reversionInTimeRate", "totalNotReversion", "totalNotReversionInTimeOut",
					"totalReversion", "totalReversionInTimeOut", "total", "loginCount", "totalLoginTime",
					"lastLoginTime", "isOnline" };
			String[] fieldsTwo = new String[] { "日期", "时间", "操作栏目", "操作项" };
			String[] dataNamesTwo = new String[] { "ymd", "hms", "operation", "remark" };

			for (Map info : resultMap) {
				String id = (String) info.get("id");
				BigDecimal reversionRate = (BigDecimal) info.get("reversionRate");
				if (reversionRate != null) {
					info.put("reversionRate", reversionRate.multiply(new BigDecimal(100)).intValue() + "%");
				}
				BigDecimal reversionInTimeRate = (BigDecimal) info.get("reversionInTimeRate");
				if (reversionInTimeRate != null) {
					info.put("reversionInTimeRate", reversionInTimeRate.multiply(new BigDecimal(100)).intValue() + "%");
				}
				BigDecimal totalLoginTime = (BigDecimal) info.get("totalLoginTime");
				if (totalLoginTime != null) {
					info.put("totalLoginTime",
							totalLoginTime.divide(new BigDecimal(60), 1, BigDecimal.ROUND_HALF_UP) + "小时");
				}
				Date lastLoginTime = (Date) info.get("lastLoginTime");
				if (lastLoginTime != null) {
					info.put("lastLoginTime", DateFormatUtils.format(lastLoginTime, "yyyy-MM-dd HH:mm:ss"));
				}
				String isOnline = (String) info.get("isOnline");
				BigDecimal leftDay = (BigDecimal) info.get("leftDay");
				if (isOnline != null) {
					info.put("isOnline", Constants.BOOLEAN_YES.equals(isOnline) ? "在线"
							: "离线\r\n" + (leftDay == null ? "（从未登录）" : "（" + leftDay.intValue() + "天未登录）"));
				}
				// 待办事项
				List<Todo> todoList = homeService.getTodoList(this.findOne(id));
				int totalTodo = 0;
				for (Todo todo : todoList) {
					totalTodo += todo.getTotal();
				}
				info.put("totalTodo", totalTodo + "");

				Map<String, Object> countMutualSubject = this.countWorkStatisticsMutualSubjectByAccountId(id);
				info.putAll(countMutualSubject);

				// 用户行为
				Map<String, Object> searchParams2 = new HashMap<String, Object>();
				searchParams2.put("EQ_userId", id);
				String datetime = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
				searchParams2.put("GTE_createDate", datetime);
				searchParams2.put("LTE_createDate", datetime);
				Criteria<GjtUserBehavior> spec = new Criteria();
				spec.addAll(Restrictions.parse(searchParams2));
				List<GjtUserBehavior> userBehaviors = gjtUserBehaviorDao.findAll(spec, new Sort("createDate"));

				// 准备生成excel
				String xm = Objects.toString(info.get("realName"), "").replace("*", "");
				String loginAccount = Objects.toString(info.get("loginAccount"), "").replace("*", "");
				List datas = new ArrayList();
				datas.add(info);
				String[] fields = titles;
				String[] dataNames = dbNames;
				String title = "工作情况统计表";
				String fileName = "工作统计明细表_" + loginAccount + "（" + xm + "）" + ".xls";

				// 写到客户指定的目录文件下
				File file = new File(fileName);
				// 建立新HSSFWorkbook对象
				HSSFWorkbook wb = new HSSFWorkbook();
				// 使用默认的构造方法创建workbook
				FileOutputStream fileout;
				fileout = new FileOutputStream(file);
				// ========================== 第一个sheet
				// ========================== //
				// 建立新的sheet对象
				HSSFSheet sheet = wb.createSheet();
				wb.setSheetName(0, title);

				HSSFRow row1, row2, row3;

				// 设置字体
				HSSFFont workFont1 = getFont(wb, (short) 12, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 属性名字体
				HSSFFont workFont2 = getFont(wb, (short) 8, "微软雅黑", HSSFFont.BOLDWEIGHT_NORMAL);// 数据源字体
				HSSFFont titleFont = getFont(wb, (short) 20, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 标题字体

				// 设置样式
				HSSFCellStyle titleCellStyle = getStyle(wb);
				titleCellStyle.setFont(titleFont);

				HSSFCellStyle fieldStyle = getStyle(wb);
				// 设置背景色 灰色
				fieldStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				fieldStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				fieldStyle.setFont(workFont1);

				HSSFCellStyle datasStyle = getStyle(wb);
				datasStyle.setFont(workFont2);
				datasStyle.setWrapText(true);

				// 标题
				row1 = sheet.createRow((short) 0);
				row1.setHeight((short) 600);

				HSSFCell titleCell = row1.createCell(0);
				// 定义单元格为字符串类型
				titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				titleCell.setCellStyle(titleCellStyle);

				/**
				 * 填充数据
				 */

				titleCell.setCellValue(new HSSFRichTextString(title));

				// 列名称

				row2 = sheet.createRow((short) 1);
				row2.setHeight((short) 500);
				for (int i = 0; i < fields.length; i++) {
					HSSFCell cell2 = row2.createCell((short) i);
					sheet.setColumnWidth(i, (fields[i].getBytes().length + 2) * 512);// 中文宽度自适应
					// sheet.autoSizeColumn(i);// 宽度自适应
					cell2.setCellStyle(fieldStyle);
					cell2.setCellValue(ObjectUtils.toString(fields[i]));

				}

				int index = 1;
				for (int j = 0; j < datas.size(); j++) {
					index++;
					HashMap clsBean = (HashMap) datas.get(j);
					row3 = sheet.createRow(index);
					row3.setHeight((short) 500);
					for (int k = 0; k < dataNames.length; k++) {
						HSSFCell dataCell = row3.createCell((short) k);
						dataCell.setCellStyle(datasStyle);
						dataCell.setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNames[k]))));
					}
				}

				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, dataNames.length - 1)); // 合并标题单元格

				List<Map<String, Object>> datasTwo = new ArrayList<Map<String, Object>>();
				for (GjtUserBehavior ub : userBehaviors) {
					Map<String, Object> ubInfo = new HashMap<String, Object>();
					ubInfo.put("ymd", DateFormatUtils.ISO_DATE_FORMAT.format(ub.getCreateDate()));
					ubInfo.put("hms", DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(ub.getCreateDate()));
					ubInfo.put("operation", ub.getOperation());
					ubInfo.put("remark", ub.getRemark());
					datasTwo.add(ubInfo);
				}
				String titleTwo = "工作动态统计表";
				// ========================== 第二个sheet
				// ========================== //
				// 建立新的sheet对象
				HSSFSheet sheetTwo = wb.createSheet();
				wb.setSheetName(1, titleTwo);

				HSSFRow rowTwo1, rowTwo2, rowTwo3;

				// 标题
				rowTwo1 = sheetTwo.createRow((short) 0);
				rowTwo1.setHeight((short) 600);

				HSSFCell titleCellTwo = rowTwo1.createCell(0);
				// 定义单元格为字符串类型
				titleCellTwo.setCellType(HSSFCell.CELL_TYPE_STRING);
				titleCellTwo.setCellStyle(titleCellStyle);

				/**
				 * 填充数据
				 */

				titleCellTwo.setCellValue(new HSSFRichTextString(titleTwo));

				// 列名称

				rowTwo2 = sheetTwo.createRow((short) 1);
				rowTwo2.setHeight((short) 500);
				for (int i = 0; i < fieldsTwo.length; i++) {
					HSSFCell cell2 = rowTwo2.createCell((short) i);
					sheetTwo.setColumnWidth(i, (fieldsTwo[i].getBytes().length + 2) * 512);// 中文宽度自适应
					// sheet.autoSizeColumn(i);// 宽度自适应
					cell2.setCellStyle(fieldStyle);
					cell2.setCellValue(ObjectUtils.toString(fieldsTwo[i]));

				}

				int indexTwo = 1;
				for (int j = 0; j < datasTwo.size(); j++) {
					indexTwo++;
					HashMap clsBean = (HashMap) datasTwo.get(j);
					rowTwo3 = sheetTwo.createRow(indexTwo);
					rowTwo3.setHeight((short) 500);
					for (int k = 0; k < dataNamesTwo.length; k++) {
						HSSFCell dataCell = rowTwo3.createCell((short) k);
						dataCell.setCellStyle(datasStyle);
						dataCell.setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNamesTwo[k]))));
					}
				}

				sheetTwo.addMergedRegion(new CellRangeAddress(0, 0, 0, dataNamesTwo.length - 1)); // 合并标题单元格

				wb.write(fileout);
				fileout.close();
				FileUtils.copyFile(file, new File(detailFolderPath + File.separator + fileName));
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			String outputFilePath = path + File.separator + folderName + ".zip";
			ZipFileUtil.zipDir(detailFolderPath, outputFilePath);
			FileKit.delFile(detailFolderPath);
			return outputFilePath;
		}
	}

	@Override
	public String exportLoginSituation(String orgId, Map searchParams, String path) {
		String folderName = searchParams.get("GTE_createdDt") + "至" + searchParams.get("LTE_createdDt") + "登录情况明细_"
				+ Calendar.getInstance().getTimeInMillis();
		String detailFolderPath = path + folderName;
		List<Map<String, Object>> resultMap = gjtUserAccountNativeDao.queryWorkStatisticsBy(orgId, searchParams);

		try {
			String[] titles = new String[] { "姓名", "帐号", "手机号", "角色", "院校", "登录总次数", "登录总时长", "最近登录日期", "当前在线状态" };
			String[] dbNames = new String[] { "realName", "loginAccount", "sjh", "roleName", "orgName", "totalLoginNum",
					"totalLoginTime", "recentlyDt", "isOnline" };
			String[] fieldsTwo = new String[] { "登录IP", "登录城市", "登录系统", "登录浏览器", "登录时间", "退出时间", "在线时长" };
			String[] dataNamesTwo = new String[] { "loginIp", "loginAddress", "os", "browser", "createdDt", "updatedDt",
					"loginTime" };

			for (Map info : resultMap) {
				String id = (String) info.get("id");
				Map<String, Object> countLoginLog = countProLoginLogByAccountId(id, searchParams); // 统计时间段内登录情况
				info.putAll(countLoginLog);

				BigDecimal totalLoginTime = (BigDecimal) info.get("totalLoginTime");
				if (totalLoginTime != null) {
					info.put("totalLoginTime",
							totalLoginTime.divide(new BigDecimal(60), 1, BigDecimal.ROUND_HALF_UP) + "小时");
				}
				Date recentlyDt = (Date) info.get("recentlyDt");
				if (recentlyDt != null) {
					info.put("recentlyDt", DateFormatUtils.format(recentlyDt, "yyyy-MM-dd HH:mm:ss"));
				}
				String isOnline = (String) info.get("isOnline");
				BigDecimal leftDay = (BigDecimal) info.get("leftDay");
				if (isOnline != null) {
					info.put("isOnline", Constants.BOOLEAN_YES.equals(isOnline) ? "在线"
							: "离线\r\n" + (leftDay == null ? "（从未登录）" : "（" + leftDay.intValue() + "天未登录）"));
				}

				// 登录日志
				Map<String, Object> searchParams2 = new HashMap<String, Object>();
				searchParams2.put("EQ_createdBy", id);
				searchParams2.put("GTE_createdDt", searchParams.get("GTE_createdDt"));
				searchParams2.put("LTE_createdDt", searchParams.get("LTE_createdDt"));
				List<TblPriLoginLog> loginLogList = tblPriLoginLogService.queryBy(searchParams2, null);

				// 准备生成excel
				String xm = Objects.toString(info.get("realName"), "").replace("*", "");
				String loginAccount = Objects.toString(info.get("loginAccount"), "").replace("*", "");
				List datas = new ArrayList();
				datas.add(info);
				String[] fields = titles;
				String[] dataNames = dbNames;
				String title = searchParams.get("GTE_createdDt") + "至" + searchParams.get("LTE_createdDt") + "登录详情统计";
				String fileName = "登录情况明细表_" + loginAccount + "（" + xm + "）" + ".xls";

				// 写到客户指定的目录文件下
				File file = new File(fileName);
				// 建立新HSSFWorkbook对象
				HSSFWorkbook wb = new HSSFWorkbook();
				// 使用默认的构造方法创建workbook
				FileOutputStream fileout;
				fileout = new FileOutputStream(file);
				// ========================== 第一个sheet
				// ========================== //
				// 建立新的sheet对象
				HSSFSheet sheet = wb.createSheet();
				wb.setSheetName(0, title);

				HSSFRow row1, row2, row3;

				// 设置字体
				HSSFFont workFont1 = getFont(wb, (short) 12, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 属性名字体
				HSSFFont workFont2 = getFont(wb, (short) 8, "微软雅黑", HSSFFont.BOLDWEIGHT_NORMAL);// 数据源字体
				HSSFFont titleFont = getFont(wb, (short) 20, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 标题字体

				// 设置样式
				HSSFCellStyle titleCellStyle = getStyle(wb);
				titleCellStyle.setFont(titleFont);

				HSSFCellStyle fieldStyle = getStyle(wb);
				// 设置背景色 灰色
				fieldStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				fieldStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				fieldStyle.setFont(workFont1);

				HSSFCellStyle datasStyle = getStyle(wb);
				datasStyle.setFont(workFont2);
				datasStyle.setWrapText(true);

				// 标题
				row1 = sheet.createRow((short) 0);
				row1.setHeight((short) 600);

				HSSFCell titleCell = row1.createCell(0);
				// 定义单元格为字符串类型
				titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				titleCell.setCellStyle(titleCellStyle);

				/**
				 * 填充数据
				 */

				titleCell.setCellValue(new HSSFRichTextString(title));

				// 列名称

				row2 = sheet.createRow((short) 1);
				row2.setHeight((short) 500);
				for (int i = 0; i < fields.length; i++) {
					HSSFCell cell2 = row2.createCell((short) i);
					sheet.setColumnWidth(i, (fields[i].getBytes().length + 2) * 512);// 中文宽度自适应
					// sheet.autoSizeColumn(i);// 宽度自适应
					cell2.setCellStyle(fieldStyle);
					cell2.setCellValue(ObjectUtils.toString(fields[i]));

				}

				int index = 1;
				for (int j = 0; j < datas.size(); j++) {
					index++;
					HashMap clsBean = (HashMap) datas.get(j);
					row3 = sheet.createRow(index);
					row3.setHeight((short) 500);
					for (int k = 0; k < dataNames.length; k++) {
						HSSFCell dataCell = row3.createCell((short) k);
						dataCell.setCellStyle(datasStyle);
						dataCell.setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNames[k]))));
					}
				}

				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, dataNames.length - 1)); // 合并标题单元格

				List<Map<String, Object>> datasTwo = new ArrayList<Map<String, Object>>();
				for (TblPriLoginLog ub : loginLogList) {
					Map<String, Object> ubInfo = new HashMap<String, Object>();
					ubInfo.put("loginIp", ub.getLoginIp());
					ubInfo.put("loginAddress", ub.getLoginAddress());
					ubInfo.put("os", ub.getOs());
					ubInfo.put("browser", ub.getBrowser());

					Date createdDt = ub.getCreatedDt();
					if (createdDt != null) {
						ubInfo.put("createdDt", DateFormatUtils.format(createdDt, "yyyy-MM-dd HH:mm:ss"));
					}
					Date updatedDt = ub.getUpdatedDt();
					if (updatedDt != null) {
						ubInfo.put("updatedDt", DateFormatUtils.format(updatedDt, "yyyy-MM-dd HH:mm:ss"));
					}
					BigDecimal loginTime = ub.getLoginTime();
					if (loginTime != null) {
						// 取余 返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
						BigDecimal[] dr = loginTime.divideAndRemainder(new BigDecimal(60));
						ubInfo.put("loginTime", dr[0].intValue() + "小时" + dr[1].intValue() + "分");
					}
					datasTwo.add(ubInfo);
				}
				String titleTwo = searchParams.get("GTE_createdDt") + "至" + searchParams.get("LTE_createdDt")
						+ "登录情况明细";
				// ========================== 第二个sheet
				// ========================== //
				// 建立新的sheet对象
				HSSFSheet sheetTwo = wb.createSheet();
				wb.setSheetName(1, titleTwo);

				HSSFRow rowTwo1, rowTwo2, rowTwo3;

				// 标题
				rowTwo1 = sheetTwo.createRow((short) 0);
				rowTwo1.setHeight((short) 600);

				HSSFCell titleCellTwo = rowTwo1.createCell(0);
				// 定义单元格为字符串类型
				titleCellTwo.setCellType(HSSFCell.CELL_TYPE_STRING);
				titleCellTwo.setCellStyle(titleCellStyle);

				/**
				 * 填充数据
				 */

				titleCellTwo.setCellValue(new HSSFRichTextString(titleTwo));

				// 列名称

				rowTwo2 = sheetTwo.createRow((short) 1);
				rowTwo2.setHeight((short) 500);
				for (int i = 0; i < fieldsTwo.length; i++) {
					HSSFCell cell2 = rowTwo2.createCell((short) i);
					sheetTwo.setColumnWidth(i, (fieldsTwo[i].getBytes().length + 2) * 512);// 中文宽度自适应
					// sheet.autoSizeColumn(i);// 宽度自适应
					cell2.setCellStyle(fieldStyle);
					cell2.setCellValue(ObjectUtils.toString(fieldsTwo[i]));

				}

				int indexTwo = 1;
				for (int j = 0; j < datasTwo.size(); j++) {
					indexTwo++;
					HashMap clsBean = (HashMap) datasTwo.get(j);
					rowTwo3 = sheetTwo.createRow(indexTwo);
					rowTwo3.setHeight((short) 500);
					for (int k = 0; k < dataNamesTwo.length; k++) {
						HSSFCell dataCell = rowTwo3.createCell((short) k);
						dataCell.setCellStyle(datasStyle);
						dataCell.setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNamesTwo[k]))));
					}
				}

				sheetTwo.addMergedRegion(new CellRangeAddress(0, 0, 0, dataNamesTwo.length - 1)); // 合并标题单元格

				wb.write(fileout);
				fileout.close();
				FileUtils.copyFile(file, new File(detailFolderPath + File.separator + fileName));
				file.delete();
			}
			// 登录情况明细总表
			String fileName = ExcelService.renderExcel(resultMap, titles, dbNames, "登录情况明细总表",
					detailFolderPath + File.separator, "登录情况明细总表");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			String outputFilePath = path + File.separator + folderName + ".zip";
			ZipFileUtil.zipDir(detailFolderPath, outputFilePath);
			FileKit.delFile(detailFolderPath);
			return outputFilePath;
		}
	}

	@Override
	public Map<String, String> findUserByRoleId(String roleId, String orgId) {
		String sql = "SELECT id ID,REAL_NAME NAME FROM  GJT_USER_ACCOUNT WHERE IS_DELETED='N' AND ROLE_ID= '" + roleId
				+ "' and ORG_ID IN (select org.ID from GJT_ORG org where org.IS_DELETED='N' start with org.ID = '"
				+ orgId + "' connect by prior org.ID = org.PERENT_ID ) GROUP BY ID,REAL_NAME";
		return commonMapService.getMap(sql);
	}

	@Override
	public boolean updateLoginState(String id, BigDecimal loginCount, String sessionId) {
		// 登陆成功，记录用户是否在线,记录登陆次数
		int loginNum = 0;
		if (loginCount != null) {
			loginNum = loginCount.intValue() + 1;
		} else {
			loginNum = 1;
		}
		int i = gjtUserAccountDao.updateLoginState(id, new BigDecimal(loginNum), sessionId);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateQuitState(String id) {
		int i = gjtUserAccountDao.updateQuitState(id);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateQuitStateBySessionId(String sessionId) {
		int i = gjtUserAccountDao.updateQuitStateBySessionId(sessionId);
		return i >= 1 ? true : false;
	}

	@Override
	public List<String> findUserIdByRoleId(String roleId, String orgId) {
		return gjtUserAccountDao.findUserIdByRoleId(roleId, orgId);
	}

	@Override
	public boolean updateSessionId(String userId, BigDecimal loginCount, String sessionId) {
		// 登陆成功，记录用户是否在线,记录登陆次数
		int loginNum = 0;
		if (loginCount != null) {
			loginNum = loginCount.intValue() + 1;
		} else {
			loginNum = 1;
		}
		int i = gjtUserAccountDao.updateSessionId(userId, new BigDecimal(loginNum), sessionId);
		return i == 1 ? true : false;
	}

	/**
	 * 学员模拟登陆URL生产
	 * 
	 * @param studentId
	 * @param xh
	 * @return
	 */
	public String studentSimulation(String studentId, String xh) {
		String p = SSOUtil.getSignOnParam(xh);
		String domain = getRedirectDomain(xh, PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		domain = domain == null ? AppConfig.getProperty("pcenterServer") : domain;
		String url = domain + AppConfig.getProperty("sso.login.url");
		url += "?p=" + p + "&pdmn=pd&s=" + studentId;
		return url;
	}

	/**
	 * 辅导老师模拟登陆
	 * 
	 * @param employeeId
	 * @return
	 */
	public String coachTeacherSimulation(String employeeId) {
		String teacherId = EncryptUtils.encrypt(employeeId);
		String url = AppConfig.getProperty("oclassHost") + "/book/index/teacher/urlLogin.do?formMap.USER_INFO="
				+ teacherId;
		return url;
	}

	/**
	 * 督导老师模拟登陆
	 * 
	 * @param teachPlanId
	 * @param classId
	 * @param employeeId
	 * @return
	 */
	public String supervisorTeacherSimulation(String teachPlanId, String classId, String employeeId) {
		String teacherParam = EncryptUtils.encrypt(teachPlanId + "," + classId + "," + employeeId);
		String url = AppConfig.getProperty("oclassHost") + "/book/index/ddteacher/urlLogin.do?formMap.USER_INFO="
				+ teacherParam;
		return url;
	}

	/**
	 * 班主任模拟登陆
	 * 
	 * @param loginAccount
	 * @param employeeId
	 * @return
	 */
	public String headTeacherSimulation(String loginAccount, String employeeId, String courseClassId) {
		String teacherParam = EncryptUtils
				.encrypt(loginAccount + "," + employeeId + "," + DateUtils.getDate().getTime());
		String domain = getRedirectDomain(loginAccount, PlatfromTypeEnum.HEADTEACHERPLATFORM.getNum());
		domain = domain == null ? AppConfig.getProperty("pcenterEmployeeServer") : domain;
		String url = domain + AppConfig.getProperty("employee.sso.login.url");
		url += "?userInfo=" + teacherParam + "&cid=" + courseClassId;
		return url;
	}

	@Override
	public Page<GjtUserBehavior> queryUserBehaviorByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createDate"));
		}
		Criteria<GjtUserBehavior> spec = new Criteria();
		spec.addAll(Restrictions.parse(searchParams));
		return gjtUserBehaviorDao.findAll(spec, pageRequest);
	}

	@Override
	public List<String> queryAllDate(String userId) {
		return gjtUserBehaviorDao.queryAllDate(userId);
	}

	@Override
	public boolean saveUserBehavior(GjtUserBehavior entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreateDate(new Date());
		GjtUserBehavior userBehavior = gjtUserBehaviorDao.save(entity);
		return userBehavior != null;
	}

	public String getRedirectDomain(String loginAccount, int platfromType) {
		// TODO 查询院校ID
		GjtUserAccount account = gjtUserAccountDao.findByLoginAccount(loginAccount);
		String orgId = null;
		if (account != null) {
			if (account.getUserType() == 0) { // 管理员
				orgId = account.getGjtOrg().getId();
			}
			if (account.getUserType() == 1) { // 学员
				orgId = account.getGjtStudentInfo().getXxId();
			}
			if (account.getUserType() == 2) { // 教师
				orgId = gjtEmployeeInfoDao.findOneByAccountId(account.getId()).getXxId();
			}
		}

		GjtSetOrgCopyright item = gjtSetOrgCopyrightDao.findByOrgIdAndPlatfromType(orgId, String.valueOf(platfromType));
		if (item != null) {
			return item.getSchoolRealmName();
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param wb
	 * @return 单元格样式(居中 四边框)
	 */
	private static HSSFCellStyle getStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		short size = 1;
		// 边框
		style.setBorderBottom(size);
		style.setBorderLeft(size);
		style.setBorderTop(size);
		style.setBorderRight(size);

		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐

		return style;
	}

	/**
	 *
	 * @param wb
	 * @param size
	 *            大小
	 * @param fontType
	 *            字体
	 * @param fine
	 *            粗细
	 * @return
	 */
	private static HSSFFont getFont(HSSFWorkbook wb, short size, String fontType, short fine) {
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints(size);
		font.setFontName(fontType);
		font.setBoldweight(fine);
		return font;
	}

	@Override
	public GjtUserAccount queryAdminUserByWxOpenId(String wxOpenId) {
		return gjtUserAccountDao.queryAdminUserByWxOpenId(wxOpenId);
	}

	@Override
	public GjtUserAccount queryAdminUserBySjh(String sjh) {
		return gjtUserAccountDao.queryAdminUserBySjh(sjh);
	}

	@Override
	public String studentSimulationNew(String appId, String studentId, String xh, String type) {
		String p = SSOUtil.encrypt(appId, studentId);
		String domain = getRedirectDomain(xh, PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		domain = domain == null ? AppConfig.getProperty("pcenterServer") : domain;
		String url = domain + AppConfig.getProperty("sso.login.url.new");
		url += "?sign=" + p + "&t=" + type;
		return url;
	}

}
