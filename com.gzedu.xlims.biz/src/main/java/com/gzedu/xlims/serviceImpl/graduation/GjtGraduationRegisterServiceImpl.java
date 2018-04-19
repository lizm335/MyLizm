package com.gzedu.xlims.serviceImpl.graduation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.dao.graduation.GjtGraduationApplyCertifDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.ZipFileUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationNativeDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationRegisterDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationRegisterEduDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegister;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegisterEdu;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月15日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class GjtGraduationRegisterServiceImpl extends BaseServiceImpl<GjtGraduationRegister>
		implements GjtGraduationRegisterService {

	private static final Logger log = LoggerFactory.getLogger(GjtGraduationRegisterServiceImpl.class);

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtGraduationRegisterDao gjtGraduationRegisterDao;

	@Autowired
	private GjtGraduationRegisterEduDao gjtGraduationRegisterEduDao;

	@Autowired
	private GjtGraduationApplyCertifDao gjtGraduationApplyCertifDao;

	@Override
	protected BaseDao<GjtGraduationRegister, String> getBaseDao() {
		return gjtGraduationRegisterDao;
	}

	@Override
	public Page<GjtGraduationRegister> queryGraduationRegisterByPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtGraduationRegister> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		String xxzxId = (String) searchParams.remove("EQ_gjtStudentInfo.gjtStudyCenter.id");
		if (StringUtils.isNotBlank(xxzxId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(xxzxId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		} else if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));
		searchParams.put("EQ_gjtStudentInfo.gjtStudyCenter.id", xxzxId);

		Page<GjtGraduationRegister> pageInfos = gjtGraduationRegisterDao.findAll(spec, pageRequest);
		return pageInfos;
	}

	@Override
	public long count(String orgId, Map<String, Object> searchParams) {
		Criteria<GjtGraduationRegister> spec = new Criteria<GjtGraduationRegister>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		String xxzxId = (String) searchParams.remove("EQ_gjtStudentInfo.gjtStudyCenter.id");
		if (StringUtils.isNotBlank(xxzxId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(xxzxId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		} else if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));
		searchParams.put("EQ_gjtStudentInfo.gjtStudyCenter.id", xxzxId);
		return gjtGraduationRegisterDao.count(spec);
	}

	@Override
	public GjtGraduationRegister queryByStudentId(String graduationPlanId, String studentId) {
		Criteria<GjtGraduationRegister> spec = new Criteria();
		spec.add(Restrictions.eq("graduationPlanId", graduationPlanId, true));
		spec.add(Restrictions.eq("studentId", studentId, true));
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		return gjtGraduationRegisterDao.findOne(spec);
	}

	@Override
	public List<GjtGraduationRegisterEdu> queryEduByStudentId(String studentId) {
		Criteria<GjtGraduationRegisterEdu> spec = new Criteria();
		spec.add(Restrictions.eq("studentId", studentId, true));
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		return gjtGraduationRegisterEduDao.findAll(spec, new Sort(Direction.DESC, "beginTime"));
	}

	@Override
	public void deleteGraduationRegisterEdu(String id) {
		gjtGraduationRegisterEduDao.delete(id);
	}

	@Override
	public boolean saveGraduationRegisterEdu(GjtGraduationRegisterEdu entity) {
		GjtGraduationRegisterEdu t = gjtGraduationRegisterEduDao.save(entity);
		return t != null ? true : false;
	}

	@Override
	public GjtGraduationRegisterEdu findGraduationRegisterEdu(String eduId) {
		return gjtGraduationRegisterEduDao.findOne(eduId);
	}

	@Override
	public String batchDownloadRegister(Map<String, Object> searchParams, Sort sort, String realPath) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		// 查询当前毕业计划的开始时间
		Date currentGradeDate = gjtGradeDao.findOne(ObjectUtils.toString(searchParams.get("gradeId"))).getStartDate();
		List<GjtGrade> gjtGradeList = gjtGradeDao.getGradeList(currentGradeDate,
				ObjectUtils.toString(searchParams.get("xxId")));
		GjtGrade gjtGrade = null;
		if (gjtGradeList != null && gjtGradeList.size() > 0) {
			gjtGrade = gjtGradeList.get(3);// 当前学期往后推2年,即往后推4个学期
		}
		searchParams.put("afterGradeId", gjtGrade.getGradeId());
		List<Map<String, Object>> registerMsg = gjtGraduationNativeDao.queryStudentRegisterMsgNew(searchParams);
		String folderName = null;
		try {
			Calendar cal = Calendar.getInstance();
			folderName = "国家开放大学毕业生登记表_" + cal.getTimeInMillis();
			String outputPath = realPath + WebConstants.EXCEL_DOWNLOAD_URL + "register" + File.separator
					+ DateFormatUtils.ISO_DATE_FORMAT.format(cal) + File.separator;
			String outputFolderPath = outputPath + File.separator + folderName;
			File folder = new File(outputFolderPath);
			if (!folder.exists()) {
				folder.mkdirs();
				log.info("创建文件 " + outputFolderPath);
			}
			HSSFWorkbook book = null;
			InputStream in = this.getClass().getResourceAsStream("/excel/graduation/Template_Graduation.xlt");
			book = new HSSFWorkbook(in);
			HSSFSheet sheet = book.getSheetAt(0);
			HSSFRow row = null;
			int rowIdx = 1, idx = 1;
			int colIdx = 0;
			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			DateFormat df = new SimpleDateFormat("yyyy.MM");
			List<Map<String, String>> eduList = null;
			if (registerMsg != null && registerMsg.size() > 0) {
				for (int i = 0; i < registerMsg.size(); i++) {
					Map<String, Object> register = registerMsg.get(i);
					List<GjtGraduationRegisterEdu> registerEduList = this
							.queryEduByStudentId(register.get("studentId").toString());
					eduList = new ArrayList<Map<String, String>>();
					if (registerEduList != null && registerEduList.size() > 0) {

						int size = registerEduList.size();
						if (size < 4) {
							int n = 0;
							for (GjtGraduationRegisterEdu registerEdu : registerEduList) {
								Map<String, String> edu = new HashMap<String, String>();
								String time = "";
								if (registerEdu.getBeginTime() != null) {
									time += df.format(registerEdu.getBeginTime());
								}
								if (registerEdu.getEndTime() != null) {
									time += "--" + df.format(registerEdu.getEndTime());
								}
								time += " ";
								edu.put("time", time);
								if (registerEdu.getRegion() != null) {
									edu.put("region", registerEdu.getRegion());
								} else {
									edu.put("region", " ");
								}
								if (registerEdu.getSchool() != null) {
									edu.put("school", registerEdu.getSchool());
								} else {
									edu.put("school", " ");
								}

								eduList.add(edu);
								n++;
							}
							for (; n < 5; n++) { // 凑够4行
								Map<String, String> edu = new HashMap<String, String>();
								edu.put("time", " ");
								edu.put("region", " ");
								edu.put("school", " ");

								eduList.add(edu);
							}
						} else { // 只取前3行
							for (int j = 0; j < 4; j++) {
								GjtGraduationRegisterEdu registerEdu = registerEduList.get(i);
								Map<String, String> edu = new HashMap<String, String>();
								String time = "";
								if (registerEdu.getBeginTime() != null) {
									time += df.format(registerEdu.getBeginTime());
								}
								if (registerEdu.getEndTime() != null) {
									time += "--" + df.format(registerEdu.getEndTime());
								}
								time += " ";
								edu.put("time", time);
								if (registerEdu.getRegion() != null) {
									edu.put("region", registerEdu.getRegion());
								} else {
									edu.put("region", " ");
								}
								if (registerEdu.getSchool() != null) {
									edu.put("school", registerEdu.getSchool());
								} else {
									edu.put("school", " ");
								}
								eduList.add(edu);
							}
						}
					} else { // 放5个空行
						for (int k = 0; k < 5; k++) {
							Map<String, String> edu = new HashMap<String, String>();
							edu.put("time", " ");
							edu.put("region", " ");
							edu.put("school", " ");
							eduList.add(edu);
						}
					}
					register.put("eduList", eduList);
					Object photo = register.get("photo");
					if (photo != null && !"".equals(photo.toString().trim())) {
						try {
							String encode = WordTemplateUtil.getRemoteSourceEncode(photo.toString().trim(), realPath);
							register.put("photo", encode);
						} catch (Exception e) {
							register.put("photo", " ");
							log.error(e.getMessage(), e);
						}
					} else {
						register.put("photo", " ");
					}
					String certificateFolderPath = outputFolderPath + File.separator;
					WordTemplateUtil.createWord(register, "国家开放大学毕业生登记表.ftl",
							new FileOutputStream(certificateFolderPath + "国家开放大学毕业生登记表_" + register.get("studentName")
									+ "(" + register.get("sfzh") + ")" + ".doc"));
				}
			}

			// 查询未填写和已填写毕业登记表的学员
			List<Map<String, Object>> studentList = gjtGraduationNativeDao
					.queryStudentRegisterProgress(gjtGrade.getGradeId());
			if (studentList != null && studentList.size() > 0) {
				for (int i = 0; i < studentList.size(); i++) {
					Map<String, Object> studentInfo = studentList.get(i);
					row = sheet.createRow(rowIdx++);
					colIdx = 0;
					row.createCell(colIdx++).setCellValue(idx++);
					row.createCell(colIdx++).setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("XM"))));
					row.createCell(colIdx++).setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("XH"))));
					row.createCell(colIdx++).setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("ZYMC"))));
					row.createCell(colIdx++).setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("NAME"))));
					row.createCell(colIdx++)
							.setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("GRADE_NAME"))));
					row.createCell(colIdx++).setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("SJH"))));
					row.createCell(colIdx++)
							.setCellValue(getCellVal(ObjectUtils.toString(studentInfo.get("PROGRESS"))));
				}
			}
			// excel
			String excelName = "学员毕业登记表登记信息" + "(" + gjtGrade.getGradeName() + ")_" + cal.getTimeInMillis();
			String excelFilePath = outputFolderPath + File.separator + excelName + ".xls";
			OutputStream out = new FileOutputStream(excelFilePath);
			book.write(out);
			out.flush();
			out.close();

			String outputFilePath = outputPath + File.separator + folderName + ".zip";
			ZipFileUtil.zipDir(outputFolderPath, outputFilePath);
			FileKit.delFile(outputFolderPath);
			return outputFilePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean update(GjtGraduationRegister info) {
		info.setUpdatedDt(new Date());
		return gjtGraduationRegisterDao.save(info) != null;
	}

	@Override
	public boolean initCurrentTermGraduationStudent(String xxId,String graduationPlanId, Date enterSchoolDate) {
		gjtGraduationRegisterDao.initCurrentTermGraduationStudent(xxId, graduationPlanId, enterSchoolDate);
		gjtGraduationApplyCertifDao.initCurrentTermGraduationStudent(xxId, graduationPlanId, enterSchoolDate);
		return true;
	}

	@Override
	public GjtGraduationRegister queryOneByStudentIdAndPlanId(String studentId, String planId) {
		return gjtGraduationRegisterDao.queryOneByStudentIdAndPlanId(studentId, planId);
	}

	/**
	 * 安全输出表格单元格的值
	 * 
	 * @param val
	 * @return
	 */
	private HSSFRichTextString getCellVal(String val) {
		HSSFRichTextString result = new HSSFRichTextString("");
		try {
			val = ObjectUtils.toString(val, "");
			result = new HSSFRichTextString(val);
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public int updatePhoto(String studentId, String photo) {
		return gjtGraduationRegisterDao.updatePhoto(studentId, photo);
	}

}
