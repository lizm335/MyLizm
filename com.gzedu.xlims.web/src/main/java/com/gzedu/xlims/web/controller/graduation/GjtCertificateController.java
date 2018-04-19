package com.gzedu.xlims.web.controller.graduation;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzedu.xlims.dao.graduation.GjtCertificateRecordDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtCertificateRecord;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtCertificateRecordService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/graduation/certificate")
public class GjtCertificateController {

	private static final Log log = LogFactory.getLog(GjtCertificateController.class);

	@Autowired
	private GjtCertificateRecordService gjtCertificateRecordService;
	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtCertificateRecordDao gjtCertificateRecordDao;

	/**
	 * 毕业证书
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月29日 上午10:39:24
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "graduationList", method = RequestMethod.GET)
	public String graduationList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtCertificateRecord> pageInfo = gjtCertificateRecordService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequest);
		model.addAttribute("pageInfo", pageInfo);

		searchParams.put("EQ_status", 0);// 未发放
		model.addAttribute("unsend", gjtCertificateRecordService.countAll(user.getGjtOrg().getId(), searchParams));
		searchParams.put("EQ_status", 1);// 发放中
		model.addAttribute("sending", gjtCertificateRecordService.countAll(user.getGjtOrg().getId(), searchParams));
		searchParams.put("EQ_status", 2);// 已发放
		model.addAttribute("sended", gjtCertificateRecordService.countAll(user.getGjtOrg().getId(), searchParams));

		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());// 层次
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));// 专业
		model.addAttribute("orgMap", commonMapService.getOrgTree(user.getGjtOrg().getId(), false));
		return "graduation/certificate/graduation_list";
	}

	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void exportGraduationList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		List<GjtCertificateRecord> certificateRecordList = gjtCertificateRecordService.queryAll(user.getGjtOrg().getId(), searchParams);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", certificateRecordList);
		map.put("pyccMap", commonMapService.getPyccMap());
		String fileName = "1".equals(searchParams.get("EQ_certificateType")) ? "学位证书发放记录.xls" : "毕业证书发放记录.xls";
		String path = getClass().getResource(WebConstants.EXCEL_MODEL_URL).getPath() + "证书发放记录导出模板.xls";
		com.gzedu.xlims.common.ExcelUtil.exportExcel(map, path, response, fileName);
	}

	@SysLog("毕业管理-导入毕业证/学位证发放记录")
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importCertificate(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response,
			int certificateType) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "身份证号", "发放人", "发放机构", "发放日期","签收人", "是否代收", "状态", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			int length = heads.length - 1;
			Date now = new Date();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}
					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
					if (studentInfo == null) {
						result[heads.length - 1] = "学号不存在";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if (!datas[1].equals(studentInfo.getXm())) {
						result[heads.length - 1] = "姓名和学号不对应";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[2])) { // 身份证号
						result[heads.length - 1] = "身份证号不能为空";
						failedList.add(result);
						continue;
					}

					if (!datas[2].equals(studentInfo.getSfzh())) {
						result[heads.length - 1] = "姓名和身份证号不对应";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[3])) { // 发放人
						result[heads.length - 1] = "发放人不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[4])) { // 发放机构
						result[heads.length - 1] = "发放机构不能为空";
						failedList.add(result);
						continue;
					}

					GjtOrg gjtOrg = gjtOrgService.queryByName(datas[4]);
					if (gjtOrg == null) {
						result[heads.length - 1] = "发放机构不存在";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[5])) { // 发放日期
						result[heads.length - 1] = "发放日期不能为空";
						failedList.add(result);
						continue;
					}

					Date sendDate = null;
					try {
						sendDate = sdf.parse(datas[5]);
					} catch (Exception e) {
						result[heads.length - 1] = "发放日期格式有误";
						failedList.add(result);
						continue;
					}
					if ("".equals(datas[6])) { // 签收人
						result[heads.length - 1] = "签收人不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[7])) { // 是否代收
						result[heads.length - 1] = "是否代收不能为空";
						failedList.add(result);
						continue;
					}

					if (!"是".equals(datas[7]) && !"否".equals(datas[7])) { // 是否代收
						result[heads.length - 1] = "是否代收数据有误";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[8])) { // 状态
						result[heads.length - 1] = "状态不能为空";
						failedList.add(result);
						continue;
					}

					if (!"未发放".equals(datas[8]) && !"发放中".equals(datas[8]) && !"已发放".equals(datas[8])) { // 状态
						result[heads.length - 1] = "状态数据有误";
						failedList.add(result);
						continue;
					}
					GjtCertificateRecord record = gjtCertificateRecordDao.findByStudentIdAndCertificateType(studentInfo.getStudentId(), certificateType);
					if(record != null){
						record.setCreateBy(user.getId());
						record.setCreateDt(now);
					}else{
						record = new GjtCertificateRecord();
						record.setId(UUIDUtils.random());
						record.setCertificateType(certificateType);
						record.setCreateBy(user.getId());
						record.setCreateDt(now);
						record.setIsDeleted("N");
					}

					record.setIsAgent("否".equals(datas[7]) ? 0 : 1);
					record.setOrgId(user.getGjtOrg().getId());
					record.setSendOrgId(gjtOrg.getId());
					record.setSendPerson(datas[3]);
					record.setSignPerson(datas[6]);
					record.setSendType(0);// 暂时只有现场认领一种方式
					record.setStudentId(studentInfo.getStudentId());
					record.setSendDt(sendDate);
					int status = 0;
					if ("未发放".equals(datas[8])) {
						status = 0;
					} else if ("发放中".equals(datas[8])) {
						status = 1;
					} else if ("已发放".equals(datas[8])) {
						status = 2;
					}
					record.setStatus(status);
//					gjtCertificateRecordService.insert(record);
					gjtCertificateRecordService.save(record);
					result[heads.length - 1] = "导入成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "certificateRecord_success_" + currentTimeMillis + ".xls";
			String failedFileName = "certificateRecord_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "教材导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "教材导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "certificateRecord"
					+ File.separator;
			File f = new File(filePath);
			if (!f.exists()) {
				f.mkdirs();
			}

			File successFile = new File(filePath, successFileName);
			successFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook1, successFile);

			File failedFile = new File(filePath, failedFileName);
			failedFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook2, failedFile);

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName, failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 学位证书
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月29日 上午10:39:24
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "degreeList", method = RequestMethod.GET)
	public String degreeList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Object> pageInfo = new PageImpl<Object>(new ArrayList<Object>(), pageRequst, 0);
		model.addAttribute("pageInfo", pageInfo);
		return "graduation/certificate/degree_list";

	}

}
