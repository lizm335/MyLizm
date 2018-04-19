package com.gzedu.xlims.serviceImpl.exam;

import com.gzedu.xlims.common.*;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.exam.GjtExamRecordNewDao;
import com.gzedu.xlims.dao.studymanage.StudyManageDao;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.exam.GjtExamRecordNewService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class GjtExamRecordNewServiceImpl implements GjtExamRecordNewService {
	
	@Autowired
	GjtExamRecordNewDao gjtExamRecordNewDao;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	StudyManageDao studyManageDao;
	
	@Autowired
	private CacheService cacheService;
	
	/**
	 * 考试管理=》登记成绩查询列表
	 * @return
	 */
	public Page getExamRegisterList(Map searchParams, PageRequest pageRequst) {
		return gjtExamRecordNewDao.getExamRegisterList(searchParams, pageRequst);
	}
	
	/**
	 * 考试管理=》登记成绩(统计)
	 * @return
	 */
	public int getRegisterCount(Map searchParams) {
		return gjtExamRecordNewDao.getRegisterCount(searchParams);
	}
	
	/**
	 * 考试管理=》考试成绩查看详情
	 * @return
	 */
	public Map getExamRecordDetail(Map searchParams) {
		Map resultMap = new HashMap();
		try {
			List detailList =  gjtExamRecordNewDao.getExamRecordDetail(searchParams);
			if (EmptyUtils.isNotEmpty(detailList)) {
				resultMap = (Map)detailList.get(0);
				
				List scoreList = gjtExamRecordNewDao.getScoreList(searchParams);
				resultMap.put("SCORELIST", scoreList);
				
				List moduleList = gjtRecResultService.getCreditInfoAnd(searchParams);
				resultMap.put("MODULELIST", moduleList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 导入登记成绩
	 * @return
	 */
	public Map importRegisterRecord(String filePaths, Map formMap, String basePath) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try {
			String userId = ObjectUtils.toString(formMap.get("USER_ID"));
			String xxId = ObjectUtils.toString(formMap.get("XX_ID"));
			String exam_batch_code = ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE"));
			String[] titles = { "xh", "xm", "pycc", "major", "examNo", "subjectName", "examScore", "studyScore", "kcbz", "sumScore" };
			File file = new File(filePaths);
			ExcelReader reader = new ExcelReader(file, titles, 2);
			List list = reader.readAsList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i=0; i<list.size(); i++) {
					Map errorInfo = new HashMap();
					String errorstr = "";
					int errorNum = 1;
					
					Map tempMap = (Map) list.get(i);
					tempMap.put("examBatchCode", exam_batch_code);
					tempMap.put("xxId", xxId);
					
					String xh = ObjectUtils.toString(tempMap.get("xh")).trim();
					String xm = ObjectUtils.toString(tempMap.get("xm")).trim();
					String pycc = ObjectUtils.toString(tempMap.get("pycc")).trim();
					String major = ObjectUtils.toString(tempMap.get("major")).trim();
					String examNo = ObjectUtils.toString(tempMap.get("examNo")).trim();
					String subjectName = ObjectUtils.toString(tempMap.get("subjectName")).trim();
					String impExamScore = ObjectUtils.toString(tempMap.get("examScore")).trim();
					String impStudyScore = ObjectUtils.toString(tempMap.get("studyScore")).trim();
					String kcbz = ObjectUtils.toString(tempMap.get("kcbz")).trim();
					String impSumScore = ObjectUtils.toString(tempMap.get("sumScore")).trim();
					
					if (i==0) {
						if (!("学号".equals(xh) && "姓名".equals(xm) && "层次".equals(pycc) && "专业".equals(major) && "试卷号".equals(examNo) 
								&& "科目名称".equals(subjectName) && "卷面".equals(impExamScore) && "平时".equals(impStudyScore) && "比例".equals(kcbz) && "综合".equals(impSumScore))) {
							errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
							break;
						} else {
							continue;
						}
					}
					
					if (EmptyUtils.isNotEmpty(xh)) {
						if (EmptyUtils.isNotEmpty(xh) || EmptyUtils.isNotEmpty(xm)) {
							if (EmptyUtils.isEmpty(xh)) {
								errorstr += "学号不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(xm)) {
								errorstr += "姓名不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(examNo)) {
								errorstr += "试卷号不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isNotEmpty(impExamScore) && !ToolUtil.isNumeric(impExamScore)) {
								errorstr += "卷面成绩必须是数字，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isNotEmpty(impStudyScore) && !ToolUtil.isNumeric(impStudyScore)) {
								errorstr += "平时成绩必须是数字，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(impSumScore)) {
								errorstr += "综合成绩不能为空，";
		                        errorNum++;
							} else if (!ToolUtil.isNumeric(impSumScore)) {
								errorstr += "综合成绩必须是数字，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(kcbz)) {
								errorstr += "比例不能为空，";
		                        errorNum++;
							} else if (!ToolUtil.isNumeric(kcbz)) {
								errorstr += "比例必须是数字，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(errorstr)) {
								List stuList = gjtExamRecordNewDao.getStuExamNoInfo(tempMap);
								if (EmptyUtils.isNotEmpty(stuList)) {
									Map stuMap = (Map)stuList.get(0);
									if (EmptyUtils.isNotEmpty(ObjectUtils.toString(stuMap.get("STUDENT_ID")))) {
										if (EmptyUtils.isNotEmpty(ObjectUtils.toString(stuMap.get("APPOINTMENT_ID")))) {
											List recordList = gjtExamRecordNewDao.getExemptRecord(tempMap);
											if (EmptyUtils.isNotEmpty(recordList)) {
												Map subMap = (Map)recordList.get(0);
												String student_id = ObjectUtils.toString(subMap.get("STUDENT_ID"));
												String xk_percent = ObjectUtils.toString(subMap.get("XK_PERCENT"));
												String teach_plan_id = ObjectUtils.toString(subMap.get("TEACH_PLAN_ID"));
												String rec_id = ObjectUtils.toString(subMap.get("REC_ID"));
												String exam_score = ObjectUtils.toString(subMap.get("EXAM_SCORE"));    // 平时成绩
												String exam_score1 = ObjectUtils.toString(subMap.get("EXAM_SCORE1"));  // 考试成绩
												String exam_score2 = ObjectUtils.toString(subMap.get("EXAM_SCORE2"));  // 综合成绩
												String exam_state = ObjectUtils.toString(subMap.get("EXAM_STATE"));    // 成绩状态
												String xf = ObjectUtils.toString(subMap.get("XF"));
												String examType = ObjectUtils.toString(subMap.get("EXAM_TYPE"));
												String kch = ObjectUtils.toString(subMap.get("KCH"));
												String progress = ObjectUtils.toString(subMap.get("PROGRESS"));
												String examState = "";
												String status = "";

												/*boolean scoreFlag = true;
												if (EmptyUtils.isNotEmpty(exam_score2)) {
													if (Double.parseDouble(exam_score2) < Double.parseDouble(impSumScore)) {
														scoreFlag = false;
													}
												}*/
												
												if (EmptyUtils.isEmpty(exam_score)) {
													exam_score = "0";
												}
												
												if (EmptyUtils.isEmpty(exam_score1)) {
													exam_score1 = "0";
												}
												
												if ("0".equals(exam_state)) {
													errorstr += "成绩已登记为未通过，请勿重复登记，";
							                        errorNum++;
												} else if ("1".equals(exam_state)) {
													errorstr += "成绩已登记为通过，请勿重复登记，";
							                        errorNum++;
												} else if (!"3".equals(exam_state)) {
													errorstr += "该成绩未锁定登记中，暂时不可以登记成绩，";
							                        errorNum++;
												} else if (EmptyUtils.isEmpty(examType)) {
													errorstr += "开课科目中的考试方式未设置，请先设置，";
							                        errorNum++;
												} else if (EmptyUtils.isEmpty(xk_percent)) {
													errorstr += "开课科目中的形考比例未设置，请先设置，";
							                        errorNum++;
												} else {
													double examScore_imp = 0; //导入文档中的卷面成绩
													if (EmptyUtils.isEmpty(impExamScore)) {
														examScore_imp = 0;
													} else {
														examScore_imp = Double.parseDouble(impExamScore);
													}
													
													double studyScore_imp =0; //导入文档中的平时成绩
													if (EmptyUtils.isEmpty(impStudyScore)) {
														studyScore_imp = 0;
													} else {
														studyScore_imp = Double.parseDouble(impStudyScore);
													}
													
													double exam_score_d = Double.parseDouble(exam_score);   // 平台中的平时成绩
													double exam_score1_d = Double.parseDouble(exam_score1); // 平台中的卷面成绩
													double sumPoint = 0;
													
													String get_credits = "0";
													if(NumberUtils.toDouble(xk_percent) == 0 && studyScore_imp == 0) { // 如果形考比例为0，且导入的平时成绩为0，则使用原来的平时成绩
														studyScore_imp = ToolUtil.getIntRound(exam_score_d);
														impStudyScore = exam_score;
													}
													boolean isXingkao = "13".equals(examType); // 如果考试形式是形考，则平时成绩、综合成绩以导入为准
													if(!isXingkao) {
														if (NumberUtils.toDouble(kcbz) != NumberUtils.toDouble(xk_percent)) {
															errorstr += "形考比例跟平台设置的不一致，";
															errorNum++;
														} else if (exam_score_d > 0 && studyScore_imp != ToolUtil.getIntRound(exam_score_d)) {
															errorstr += "平时成绩跟平台锁定的成绩不一致，";
															errorNum++;
														} else if ("4".equals(examType) && exam_score1_d >= 0 && examScore_imp != ToolUtil.getIntRound(exam_score1_d)) {
															errorstr += "网考成绩登记时卷面成绩跟平台锁定的成绩不一致";
															errorNum++;
														/* 有时候综合成绩还是按照形考比例计算，所以这个不做判断了
														} else if (examScore_imp == 0 && Double.parseDouble(xk_percent) != 100) {
															if (Double.parseDouble(impSumScore) != 0) {
																errorstr += "卷面成绩为0、形考比例不是100%、综合成绩应该为0分，";
																errorNum++;
															}
														*/
														} else if (examScore_imp >= 0 && Double.parseDouble(impSumScore) >= 0) {
															sumPoint = studyScore_imp * Double.parseDouble(xk_percent) / 100 + examScore_imp * (100 - Double.parseDouble(xk_percent)) / 100;
															// 并不喜欢那种不按程序来的业务，让他们想导多少成绩就多少吧，比如成绩不四舍五入等
															/*if (Double.parseDouble(impSumScore) != ToolUtil.getIntRound(sumPoint)) {
																errorstr += "根据形考比例计算综合成绩应该为：" + ToolUtil.getIntRound(sumPoint) + "分(" + ToolUtil.getNumberFormat("0.00", sumPoint) + ")，";
																errorNum++;
															}*/
														} else if (Double.parseDouble(xk_percent) == 0 && examScore_imp != Double.parseDouble(impSumScore)) {
															errorstr += "形考比例为0的情况下、综合成绩跟卷面成绩应保持一致， ";
															errorNum++;
														} else {
															if (Double.parseDouble(xk_percent) > 0 && Double.parseDouble(impSumScore) > 0) {
																if (examScore_imp >= 0) {
																	sumPoint = studyScore_imp * Double.parseDouble(xk_percent) / 100 + examScore_imp * (100 - Double.parseDouble(xk_percent)) / 100;
																	// 并不喜欢那种不按程序来的业务，让他们想导多少成绩就多少吧，比如成绩不四舍五入等
																	/*if (Double.parseDouble(impSumScore) != ToolUtil.getIntRound(sumPoint)) {
																		errorstr += "根据形考比例计算综合成绩应该为：" + ToolUtil.getIntRound(sumPoint) + "分(" + ToolUtil.getNumberFormat("0.00", sumPoint) + ")，";
																		errorNum++;
																	}*/
																} else {
																	sumPoint = studyScore_imp * Double.parseDouble(xk_percent) / 100;
																	// 并不喜欢那种不按程序来的业务，让他们想导多少成绩就多少吧，比如成绩不四舍五入等
																	/*if (sumPoint > 0 && Double.parseDouble(impSumScore) != ToolUtil.getIntRound(sumPoint)) {
																		errorstr += "根据形考比例计算综合成绩应该为：" + ToolUtil.getIntRound(sumPoint) + "分(" + ToolUtil.getNumberFormat("0.00", sumPoint) + ")，";
																		errorNum++;
																	}*/
																}
															}
														}
													}
													if (EmptyUtils.isEmpty(errorstr)) {
														// 大于60分已通过
														if (Double.parseDouble(impSumScore)>=60) {
															examState = "1";
															status = "0";
															get_credits = xf;
														} else {
															examState = "0";
															status = "1";
														}
														
														Map searchParams = new HashMap();
														searchParams.put("EXAM_SCORE", impStudyScore);
														searchParams.put("EXAM_SCORE1", impExamScore);
														searchParams.put("EXAM_SCORE2", impSumScore);
														searchParams.put("EXAM_STATE", examState);
														searchParams.put("REC_ID", rec_id);
														searchParams.put("GET_CREDITS", get_credits);
														searchParams.put("COURSE_SCHEDULE", xk_percent);
														
														int num = gjtExamRecordNewDao.updRecRegister(searchParams);
														if (num>0) {
															searchParams.put("REPAIR_ID", SequenceUUID.getSequence());
															searchParams.put("TEACH_PLAN_ID", teach_plan_id);
															searchParams.put("XCX_SCORE", impStudyScore);
															searchParams.put("ZJX_SCORE", impExamScore);
															searchParams.put("ZCJ_SCORE", impSumScore);
															searchParams.put("STATUS", status);
															searchParams.put("STUDENT_ID", student_id);
															searchParams.put("XH", xh);
															searchParams.put("COURSE_CODE", kch);
															searchParams.put("RATIO", xk_percent);
															searchParams.put("EXAM_CODE", examNo);
															searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
															searchParams.put("PROGRESS", progress);
															searchParams.put("SUBJECT_NAME", subjectName);
															if (EmptyUtils.isNotEmpty(gjtExamRecordNewDao.queryLearnRepair(searchParams))) {
																gjtExamRecordNewDao.updLearnRepair(searchParams);
															} else {
																gjtExamRecordNewDao.addRecRegister(searchParams);
															}
															if(Double.parseDouble(impExamScore)>=60){
																searchParams.put("EXAM_STATUS", "1");
															}else{
																searchParams.put("EXAM_STATUS", "2");
															}
															gjtExamRecordNewDao.updateExamScore(searchParams);
														} else {
															errorstr += "成绩登记失败，";
									                        errorNum++;
														}
													}
												}
											} else {
												errorstr += "根据学号和试卷号查询不到学员的报读信息，请确认填写是否正确，";
						                        errorNum++;
											}
										} else {
											errorstr += "根据学号和试卷号在考试计划"+exam_batch_code+"中查询不到学员的考试信息，请确认填写是否正确，";
					                        errorNum++;
										}
									} else {
										errorstr += "根据学号查询不到学员信息，请确认填写是否正确，";
				                        errorNum++;
									}
								} else {
									errorstr += "根据学号查询不到学员信息，请确认填写是否正确，";
			                        errorNum++;
								}
							}
						}
						if (EmptyUtils.isNotEmpty(errorstr)) {
							tempMap.put("errorstr", errorstr);
							errorList.add(tempMap);
						} else {
							rightList.add(tempMap);
						}
					}
				}
			}
			
			String timeStr = ObjectUtils.toString(new Date().getTime());
			String root = basePath;
			// 导出成功记录
			String tempPath = GjtExamRecordNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/成绩登记导入成功表.xls";
			String downPath = root+"/tmp/成绩登记导入成功表"+timeStr+".xls";
			Map beans = new HashMap();  
		    beans.put("dataList", rightList);
		    XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("RIGHT_DOWNPATH", downPath);
			
            // 导出失败记录
            tempPath = GjtExamRecordNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/成绩登记导入失败表.xls";
			downPath = root+"/tmp/成绩登记导入失败表"+timeStr+".xls";
            beans = new HashMap();
            beans.put("dataList", errorList);
            transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("ERROR_DOWNPATH", downPath);
            
		} catch (Exception e) {
			errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
			e.printStackTrace();
		}
		resultMap.put("errorMessage", errorMessage);
		resultMap.put("errorNum", errorList.size());
		resultMap.put("rightNum", rightList.size());
		resultMap.put("allNum", errorList.size()+rightList.size());
		return resultMap;
	}
	
	/**
	 * 导入登记成绩统考学位英语
	 * @return
	 */
	public Map importRegisterTkXwRecord(String filePaths, Map formMap, String basePath) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		String exam_batch_code = ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE"));
		String xxId = ObjectUtils.toString(formMap.get("XX_ID"));
		try {
			String[] titles = { "xh", "xm", "sfzh", "kch", "kcmc", "examNo", "examScore", "examState", "remark"};
			File file = new File(filePaths);
			ExcelReader reader = new ExcelReader(file, titles, 2);
			List list = reader.readAsList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i=0; i<list.size(); i++) {
					Map errorInfo = new HashMap();
					String errorstr = "";
					int errorNum = 1;
					
					Map tempMap = (Map) list.get(i);
					tempMap.put("examBatchCode", exam_batch_code);
					tempMap.put("xxId", xxId);
					String xh = ObjectUtils.toString(tempMap.get("xh")).trim();
					String xm = ObjectUtils.toString(tempMap.get("xm")).trim();
					String sfzh = ObjectUtils.toString(tempMap.get("sfzh")).trim();
					String kch = ObjectUtils.toString(tempMap.get("kch")).trim();
					String kcmc = ObjectUtils.toString(tempMap.get("kcmc")).trim();
					String examNo = ObjectUtils.toString(tempMap.get("examNo")).trim();
					String examScore = ObjectUtils.toString(tempMap.get("examScore")).trim();
					String examState = ObjectUtils.toString(tempMap.get("examState")).trim();
					String remark = ObjectUtils.toString(tempMap.get("remark")).trim();
					
					if (i==0) {
						if (!("学号".equals(xh) && "姓名".equals(xm) && "身份证号".equals(sfzh) && "课程ID".equals(kch) && "课程名称".equals(kcmc) 
								&& "试卷号".equals(examNo) && "成绩".equals(examScore) && "成绩状态".equals(examState) && "备注".equals(remark))) {
							errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
							break;
						} else {
							continue;
						}
					}
					
					if (EmptyUtils.isNotEmpty(xh)) {
						if (EmptyUtils.isNotEmpty(xh) || EmptyUtils.isNotEmpty(xm)) {
							if (EmptyUtils.isEmpty(xh)) {
								errorstr += "学号不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(xm)) {
								errorstr += "姓名不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(kch)) {
								errorstr += "课程ID不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(kcmc)) {
								errorstr += "课程名称不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(examNo)) {
								errorstr += "试卷号不能为空，";
		                        errorNum++;
							}
							
							if (EmptyUtils.isNotEmpty(examScore) && !ToolUtil.isNumeric(examScore)) {
								errorstr += "成绩必须是数字，";
		                        errorNum++;
							}
							
							Map examStateMap = cacheService.getMapCachedCode("EXAM_SCORE");
							if (EmptyUtils.isEmpty(examState)) {
								errorstr += "成绩状态不能为空，";
		                        errorNum++;
							} else if (EmptyUtils.isEmpty(examStateMap.get(examState))) {
								errorstr += "成绩状态填写错误，请按模板提示填写";
		                        errorNum++;
							}
							
							if (EmptyUtils.isEmpty(errorstr)) {
								List stuList = gjtExamRecordNewDao.getStuExamNoInfo(tempMap);
								if (EmptyUtils.isNotEmpty(stuList)) {
									Map stuMap = (Map)stuList.get(0);
									if (EmptyUtils.isNotEmpty(ObjectUtils.toString(stuMap.get("STUDENT_ID")))) {
										if (EmptyUtils.isNotEmpty(ObjectUtils.toString(stuMap.get("APPOINTMENT_ID")))) {
											List recordList = gjtExamRecordNewDao.getExemptRecord(tempMap);
											if (EmptyUtils.isNotEmpty(recordList)) {
												
												Map subMap = (Map)recordList.get(0);
												String student_id = ObjectUtils.toString(subMap.get("STUDENT_ID"));
												String xk_percent = ObjectUtils.toString(subMap.get("XK_PERCENT"));
												String teach_plan_id = ObjectUtils.toString(subMap.get("TEACH_PLAN_ID"));
												String rec_id = ObjectUtils.toString(subMap.get("REC_ID"));
												String exam_score = ObjectUtils.toString(subMap.get("EXAM_SCORE"));    // 平时成绩
												String exam_score1 = ObjectUtils.toString(subMap.get("EXAM_SCORE1"));  // 考试成绩
												String exam_score2 = ObjectUtils.toString(subMap.get("EXAM_SCORE2"));  // 综合成绩
												String exam_state = ObjectUtils.toString(subMap.get("EXAM_STATE"));    // 成绩状态
												String xf = ObjectUtils.toString(subMap.get("XF"));
												String examType = ObjectUtils.toString(subMap.get("EXAM_TYPE"));
												String progress = ObjectUtils.toString(subMap.get("PROGRESS"));

												Map searchParams = new HashMap();
												searchParams.put("REPAIR_ID", SequenceUUID.getSequence());
												searchParams.put("STUDENT_ID", student_id);
												searchParams.put("TEACH_PLAN_ID", teach_plan_id);
												searchParams.put("XH", xh);
												searchParams.put("COURSE_CODE", kch);
												searchParams.put("RATIO", xk_percent);
												searchParams.put("EXAM_SCORE", exam_score);
												searchParams.put("REC_ID", rec_id);
												searchParams.put("COURSE_SCHEDULE", xk_percent);
												searchParams.put("EXAM_CODE", examNo);
												searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
												searchParams.put("PROGRESS", progress);
												searchParams.put("SUBJECT_NAME", kcmc);
												
												if ("通过".equals(examState) || "良好".equals(examState) || "继承".equals(examState) || "合格".equals(examState)
														|| "及格".equals(examState) || "免考".equals(examState) || "免修".equals(examState) || "优秀".equals(examState)
														|| "中等".equals(examState) ) {
													searchParams.put("EXAM_STATE", "1");
													searchParams.put("STATUS", "0");
													searchParams.put("GET_CREDITS", ObjectUtils.toString(searchParams.get("XF")));
												} else {
													searchParams.put("EXAM_STATE", "0");
													searchParams.put("STATUS", "1");
													searchParams.put("GET_CREDITS", "0");
												}
												if (EmptyUtils.isNotEmpty(examScore)) {
													searchParams.put("EXAM_SCORE2", examScore);
													searchParams.put("EXAM_SCORE1", examScore);
													searchParams.put("ZCJ_SCORE", examScore);
													searchParams.put("ZJX_SCORE", examScore);
												} else {
													searchParams.put("EXAM_SCORE2", ObjectUtils.toString(examStateMap.get(examState)));
													searchParams.put("EXAM_SCORE1", ObjectUtils.toString(examStateMap.get(examState)));
													searchParams.put("ZCJ_SCORE", ObjectUtils.toString(examStateMap.get(examState)));
													searchParams.put("ZJX_SCORE", ObjectUtils.toString(examStateMap.get(examState)));
												}
												searchParams.put("REMARK", remark);
												
												int num = gjtExamRecordNewDao.updRecRegister(searchParams);
												if (num>0) {
													if (EmptyUtils.isNotEmpty(gjtExamRecordNewDao.queryLearnRepair(searchParams))) {
														gjtExamRecordNewDao.updLearnRepair(searchParams);
													} else {
														gjtExamRecordNewDao.addRecRegister(searchParams);
													}
													if("1".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))){
														searchParams.put("EXAM_STATUS", "1");
													} else {
														searchParams.put("EXAM_STATUS", "2");
													}
													gjtExamRecordNewDao.updateExamScore(searchParams);
												} else {
													errorstr += "成绩登记失败，";
							                        errorNum++;
												}
											} else {
												errorstr += "根据学号和试卷号查询不到学员的报读信息，请确认填写是否正确，";
						                        errorNum++;
											}
										} else {
											errorstr += "根据学号和试卷号在考试计划"+exam_batch_code+"中查询不到学员的考试信息，请确认填写是否正确，";
					                        errorNum++;
										}
									} else {
										errorstr += "根据学号查询不到学员信息，请确认填写是否正确，";
				                        errorNum++;
									}
								} else {
									errorstr += "根据学号查询不到学员信息，请确认填写是否正确，";
			                        errorNum++;
								}
							}
						}
						if (EmptyUtils.isNotEmpty(errorstr)) {
							tempMap.put("errorstr", errorstr);
							errorList.add(tempMap);
						} else {
							rightList.add(tempMap);
						}
					}
				}
			}
			
			String timeStr = ObjectUtils.toString(new Date().getTime());
			String root = basePath;
			// 导出成功记录
			String tempPath = GjtExamRecordNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/统考学位英语成绩登记导入成功表.xls";
			String downPath = root+"/tmp/统考学位英语成绩登记导入成功表"+timeStr+".xls";
			Map beans = new HashMap();  
		    beans.put("dataList", rightList);
		    XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("RIGHT_DOWNPATH", downPath);
			
            // 导出失败记录
            tempPath = GjtExamRecordNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/统考学位英语成绩登记导入失败表.xls";
			downPath = root+"/tmp/统考学位英语成绩登记导入失败表"+timeStr+".xls";
            beans = new HashMap();
            beans.put("dataList", errorList);
            transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("ERROR_DOWNPATH", downPath);
            
		} catch (Exception e) {
			errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
			e.printStackTrace();
		}
		resultMap.put("errorMessage", errorMessage);
		resultMap.put("errorNum", errorList.size());
		resultMap.put("rightNum", rightList.size());
		resultMap.put("allNum", errorList.size()+rightList.size());
		return resultMap;
	}


	/**
	 * 导入登记成绩-特殊处理  按照学号课程代码对应得上就是干
	 * @return
	 */
	public Map importRegisterRecordTeshuchuli(String filePaths, Map formMap, String basePath) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try {
			String userId = ObjectUtils.toString(formMap.get("USER_ID"));
			String xxId = ObjectUtils.toString(formMap.get("XX_ID"));
			String exam_batch_code = ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE"));
			String[] titles = { "xh", "xm", "pycc", "major", "examNo", "subjectName", "examScore", "studyScore", "kcbz", "sumScore" };
			File file = new File(filePaths);
			ExcelReader reader = new ExcelReader(file, titles, 2);
			List list = reader.readAsList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i=0; i<list.size(); i++) {
					Map errorInfo = new HashMap();
					String errorstr = "";
					int errorNum = 1;

					Map tempMap = (Map) list.get(i);
					tempMap.put("examBatchCode", exam_batch_code);
					tempMap.put("xxId", xxId);

					String xh = ObjectUtils.toString(tempMap.get("xh")).trim();
					String xm = ObjectUtils.toString(tempMap.get("xm")).trim();
					String pycc = ObjectUtils.toString(tempMap.get("pycc")).trim();
					String major = ObjectUtils.toString(tempMap.get("major")).trim();
					String examNo = ObjectUtils.toString(tempMap.get("examNo")).trim();
					String subjectName = ObjectUtils.toString(tempMap.get("subjectName")).trim();
					String impExamScore = ObjectUtils.toString(tempMap.get("examScore")).trim();
					String impStudyScore = ObjectUtils.toString(tempMap.get("studyScore")).trim();
					String kcbz = ObjectUtils.toString(tempMap.get("kcbz")).trim();
					String impSumScore = ObjectUtils.toString(tempMap.get("sumScore")).trim();

					if (i==0) {
						if (!("学号".equals(xh) && "姓名".equals(xm) && "层次".equals(pycc) && "专业".equals(major) && "试卷号".equals(examNo)
								&& "科目名称".equals(subjectName) && "卷面".equals(impExamScore) && "平时".equals(impStudyScore) && "比例".equals(kcbz) && "综合".equals(impSumScore))) {
							errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
							break;
						} else {
							continue;
						}
					}

					if (EmptyUtils.isNotEmpty(xh)) {
						if (EmptyUtils.isNotEmpty(xh) || EmptyUtils.isNotEmpty(xm)) {
							if (EmptyUtils.isEmpty(xh)) {
								errorstr += "学号不能为空，";
								errorNum++;
							}

							if (EmptyUtils.isEmpty(xm)) {
								errorstr += "姓名不能为空，";
								errorNum++;
							}

							if (EmptyUtils.isEmpty(examNo)) {
								errorstr += "试卷号不能为空，";
								errorNum++;
							}

							if (EmptyUtils.isNotEmpty(impExamScore) && !ToolUtil.isNumeric(impExamScore)) {
								errorstr += "卷面成绩必须是数字，";
								errorNum++;
							}

							if (EmptyUtils.isNotEmpty(impStudyScore) && !ToolUtil.isNumeric(impStudyScore)) {
								errorstr += "平时成绩必须是数字，";
								errorNum++;
							}

							if (EmptyUtils.isEmpty(impSumScore)) {
								errorstr += "综合成绩不能为空，";
								errorNum++;
							} else if (!ToolUtil.isNumeric(impSumScore)) {
								errorstr += "综合成绩必须是数字，";
								errorNum++;
							}

							if (EmptyUtils.isEmpty(kcbz)) {
								errorstr += "比例不能为空，";
								errorNum++;
							} else if (!ToolUtil.isNumeric(kcbz)) {
								errorstr += "比例必须是数字，";
								errorNum++;
							}

							if (EmptyUtils.isEmpty(errorstr)) {
								List recordList = gjtExamRecordNewDao.getExemptRecordTeshuchuli(tempMap);
								if (EmptyUtils.isNotEmpty(recordList)) {
									Map subMap = (Map) recordList.get(0);
									String student_id = ObjectUtils.toString(subMap.get("STUDENT_ID"));
									String teach_plan_id = ObjectUtils.toString(subMap.get("TEACH_PLAN_ID"));
									String rec_id = ObjectUtils.toString(subMap.get("REC_ID"));
									String exam_score = ObjectUtils.toString(subMap.get("EXAM_SCORE"));    // 平时成绩
									String exam_score1 = ObjectUtils.toString(subMap.get("EXAM_SCORE1"));  // 考试成绩
									String exam_score2 = ObjectUtils.toString(subMap.get("EXAM_SCORE2"));  // 综合成绩
									String exam_state = ObjectUtils.toString(subMap.get("EXAM_STATE"));    // 成绩状态
									String xf = ObjectUtils.toString(subMap.get("XF"));
									String kch = ObjectUtils.toString(subMap.get("KCH"));
									String progress = ObjectUtils.toString(subMap.get("PROGRESS"));
									String examState = "";
									String status = "";

									/*boolean scoreFlag = true;
									if (EmptyUtils.isNotEmpty(exam_score2)) {
										if (Double.parseDouble(exam_score2) < Double.parseDouble(impSumScore)) {
											scoreFlag = false;
										}
									}*/

									if (EmptyUtils.isEmpty(exam_score)) {
										exam_score = "0";
									}

									if (EmptyUtils.isEmpty(exam_score1)) {
										exam_score1 = "0";
									}

									/*if ("0".equals(exam_state)) {
										errorstr += "成绩已登记为未通过，请勿重复登记，";
										errorNum++;
									} else if ("1".equals(exam_state)) {
										errorstr += "成绩已登记为通过，请勿重复登记，";
										errorNum++;
									} else {*/
										double examScore_imp = 0; //导入文档中的卷面成绩
										if (EmptyUtils.isEmpty(impExamScore)) {
											examScore_imp = 0;
										} else {
											examScore_imp = Double.parseDouble(impExamScore);
										}

										double studyScore_imp =0; //导入文档中的平时成绩
										if (EmptyUtils.isEmpty(impStudyScore)) {
											studyScore_imp = 0;
										} else {
											studyScore_imp = Double.parseDouble(impStudyScore);
										}

										double exam_score_d = Double.parseDouble(exam_score);   // 平台中的平时成绩
										double exam_score1_d = Double.parseDouble(exam_score1); // 平台中的卷面成绩
										double sumPoint = 0;

										String get_credits = "0";
										String xk_percent = kcbz; // 特殊处理  按照学号课程代码对应得上就是干
										if(NumberUtils.toDouble(xk_percent) == 0 && studyScore_imp == 0) { // 如果形考比例为0，且导入的平时成绩为0，则使用原来的平时成绩
											studyScore_imp = ToolUtil.getIntRound(exam_score_d);
											impStudyScore = exam_score;
										}
										if (EmptyUtils.isEmpty(errorstr)) {
											// 大于60分已通过
											if (Double.parseDouble(impSumScore)>=60) {
												examState = "1";
												status = "0";
												get_credits = xf;
											} else {
												examState = "0";
												status = "1";
											}

											Map searchParams = new HashMap();
											searchParams.put("EXAM_SCORE", impStudyScore);
											searchParams.put("EXAM_SCORE1", impExamScore);
											searchParams.put("EXAM_SCORE2", impSumScore);
											searchParams.put("EXAM_STATE", examState);
											searchParams.put("REC_ID", rec_id);
											searchParams.put("GET_CREDITS", get_credits);
											searchParams.put("COURSE_SCHEDULE", xk_percent);

											int num = gjtExamRecordNewDao.updRecRegister(searchParams);
											if (num>0) {
												searchParams.put("REPAIR_ID", SequenceUUID.getSequence());
												searchParams.put("TEACH_PLAN_ID", teach_plan_id);
												searchParams.put("XCX_SCORE", impStudyScore);
												searchParams.put("ZJX_SCORE", impExamScore);
												searchParams.put("ZCJ_SCORE", impSumScore);
												searchParams.put("STATUS", status);
												searchParams.put("STUDENT_ID", student_id);
												searchParams.put("XH", xh);
												searchParams.put("COURSE_CODE", kch);
												searchParams.put("RATIO", xk_percent);
												searchParams.put("EXAM_CODE", examNo);
												searchParams.put("EXAM_BATCH_CODE", exam_batch_code);
												searchParams.put("PROGRESS", progress);
												searchParams.put("SUBJECT_NAME", subjectName);
												if (EmptyUtils.isNotEmpty(gjtExamRecordNewDao.queryLearnRepair(searchParams))) {
													gjtExamRecordNewDao.updLearnRepair(searchParams);
												} else {
													gjtExamRecordNewDao.addRecRegister(searchParams);
												}
												if(Double.parseDouble(impExamScore)>=60){
													searchParams.put("EXAM_STATUS", "1");
												}else{
													searchParams.put("EXAM_STATUS", "2");
												}
												gjtExamRecordNewDao.updateExamScore(searchParams);
											} else {
												errorstr += "成绩登记失败，";
												errorNum++;
											}
										}
									/*}*/
								} else {
									errorstr += "根据学号和课程代码查询不到学员的报读信息，请确认填写是否正确，";
									errorNum++;
								}
							}
						}
						if (EmptyUtils.isNotEmpty(errorstr)) {
							tempMap.put("errorstr", errorstr);
							errorList.add(tempMap);
						} else {
							rightList.add(tempMap);
						}
					}
				}
			}

			String timeStr = ObjectUtils.toString(new Date().getTime());
			String root = basePath;
			// 导出成功记录
			String tempPath = GjtExamRecordNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/成绩登记导入成功表.xls";
			String downPath = root+"/tmp/特殊处理-成绩登记导入成功表"+timeStr+".xls";
			Map beans = new HashMap();
			beans.put("dataList", rightList);
			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS(tempPath, beans, downPath);
			resultMap.put("RIGHT_DOWNPATH", downPath);

			// 导出失败记录
			tempPath = GjtExamRecordNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/成绩登记导入失败表.xls";
			downPath = root+"/tmp/特殊处理-成绩登记导入失败表"+timeStr+".xls";
			beans = new HashMap();
			beans.put("dataList", errorList);
			transformer = new XLSTransformer();
			transformer.transformXLS(tempPath, beans, downPath);
			resultMap.put("ERROR_DOWNPATH", downPath);

		} catch (Exception e) {
			errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
			e.printStackTrace();
		}
		resultMap.put("errorMessage", errorMessage);
		resultMap.put("errorNum", errorList.size());
		resultMap.put("rightNum", rightList.size());
		resultMap.put("allNum", errorList.size()+rightList.size());
		return resultMap;
	}
	
	/**
	 * 导出成绩
	 */
	public Workbook expExamRecord(Map formMap) {
		List<Map<String, Object>> recordlist = studyManageDao.getScoreList(formMap);
		int page = 1, pageSize = 65500;
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			while(((page - 1)*pageSize) < recordlist.size()) { // 是否有下一页
				//  插入工作表
				HSSFSheet sheet = wb.createSheet();
				HSSFRow row;
				HSSFCell cell;
				int rowIndex = 0;
				int cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				//标题栏
				cell = row.createCell(cellIndex++);
				cell.setCellValue("姓名");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学号");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("身份证");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学员类型");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("手机");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("专业层次");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("入学年级");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("入学学期");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("报读专业");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学期");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("课程ID");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("课程");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("中央课程ID");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("中央课程名称");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("试卷号");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("考试方式");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("考试单位");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("形考比例(%)");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习成绩");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("考试成绩");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("总成绩");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("成绩状态");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学习中心");
				for(int i = (page - 1)*pageSize; i < page*pageSize && i < recordlist.size(); i++) { // 当前页数据
					Map<String, Object> info = recordlist.get(i);
					// 填充数据
					String exam_state = ObjectUtils.toString(info.get("EXAM_STATE"));

					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("XM")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("XH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("SFZH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, ObjectUtils.toString(info.get("USER_TYPE"))));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("SJH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("PYCC_NAME")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("YEAR_NAME")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("GRADE_NAME")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("ZYMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("KKXQ")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("KCH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("KCMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("SOURCE_KCH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("SOURCE_KCMC")));

					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
						cell.setCellValue(ObjectUtils.toString(info.get("EXAM_NO")));
					} else {
						cell.setCellValue("--");
					}

					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
						if(StringUtils.isNotBlank(info.get("EXAM_PLAN_KSFS_NAME"))) {
							cell.setCellValue(ObjectUtils.toString(info.get("EXAM_PLAN_KSFS_NAME")));
						} else {
							cell.setCellValue(ObjectUtils.toString(info.get("KSFS_NAME")));
						}
					} else {
						cell.setCellValue(ObjectUtils.toString(info.get("KSFS_NAME")));
					}

					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
						if ("1".equals(ObjectUtils.toString(info.get("KSDW")))) {
							cell.setCellValue("省");
						} else if ("2".equals(ObjectUtils.toString(info.get("KSDW")))) {
							cell.setCellValue("中央");
						} else if ("3".equals(ObjectUtils.toString(info.get("KSDW")))) {
							cell.setCellValue("分校");
						} else {
							cell.setCellValue("--");
						}
					} else {
						cell.setCellValue("--");
					}

					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
						cell.setCellValue(ObjectUtils.toString(info.get("KCXXBZ")));
					} else {
						cell.setCellValue("--");
					}

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("EXAM_SCORE")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("EXAM_SCORE1")));

					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state)) {
						cell.setCellValue(ObjectUtils.toString(info.get("EXAM_SCORE2")));
					} else {
						cell.setCellValue("--");
					}

					cell = row.createCell(cellIndex++);
					if ("0".equals(ObjectUtils.toString(info.get("STUDY_FLG")))) {
						cell.setCellValue("未学习");
					} else if ("0".equals(exam_state)) {
						cell.setCellValue("未通过");
					} else if ("1".equals(exam_state)) {
						cell.setCellValue("已通过");
					} else if ("2".equals(exam_state)) {
						cell.setCellValue("学习中");
					} else if ("3".equals(exam_state)) {
						cell.setCellValue("登记中");
					} else {
						cell.setCellValue("学习中");
					}

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(info.get("SC_NAME")));
				}
				page++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public String expExamRecordToCsvContent(Map formMap) {
		//csv表头
		String[] header = new String[] {"姓名","学号","身份证","学员类型","手机","专业层次","入学年级","入学学期","报读专业","学期",
				"课程ID","课程","中央课程ID","中央课程名称","试卷号","考试方式","考试单位","形考比例(%)",
				"学习成绩","考试成绩","总成绩","成绩状态","学习中心"};
		//下面 data里的key，可以说是数据库字段了
		String[] key = new String[] {"XM","XH","SFZH","USER_TYPE_NAME","SJH","PYCC_NAME","YEAR_NAME","GRADE_NAME","ZYMC","KKXQ",
				"KCH","KCMC","SOURCE_KCH","SOURCE_KCMC","EXAM_NO_NAME","KSFS_NAME2","KSDW_NAME","KCXXBZ_NAME",
				"EXAM_SCORE","EXAM_SCORE1","EXAM_SCORE2_NAME","STUDY_FLG_NAME","SC_NAME"};


		//从数据库加载 你的数据
		List<Map<String, Object>> data = studyManageDao.getScoreList(formMap);
		for (Iterator<Map<String, Object>> iter = data.iterator(); iter.hasNext(); ) {
			Map<String, Object> info = iter.next();
			info.put("SFZH", ObjectUtils.toString(info.get("SFZH")) + "\t");
			info.put("USER_TYPE_NAME", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, ObjectUtils.toString(info.get("USER_TYPE"))));

			String exam_state = ObjectUtils.toString(info.get("EXAM_STATE"));
			if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
				info.put("EXAM_NO_NAME", ObjectUtils.toString(info.get("EXAM_NO")));
			} else {
				info.put("EXAM_NO_NAME", "--");
			}
			if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
				if(StringUtils.isNotBlank(info.get("EXAM_PLAN_KSFS_NAME"))) {
					info.put("KSFS_NAME2", ObjectUtils.toString(info.get("EXAM_PLAN_KSFS_NAME")));
				} else {
					info.put("KSFS_NAME2", ObjectUtils.toString(info.get("KSFS_NAME")));
				}
			} else {
				info.put("KSFS_NAME2", ObjectUtils.toString(info.get("KSFS_NAME")));
			}
			if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
				if ("1".equals(ObjectUtils.toString(info.get("KSDW")))) {
					info.put("KSDW_NAME", "省");
				} else if ("2".equals(ObjectUtils.toString(info.get("KSDW")))) {
					info.put("KSDW_NAME", "中央");
				} else if ("3".equals(ObjectUtils.toString(info.get("KSDW")))) {
					info.put("KSDW_NAME", "分校");
				} else {
					info.put("KSDW_NAME", "--");
				}
			} else {
				info.put("KSDW_NAME", "--");
			}
			if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
				info.put("KCXXBZ_NAME", ObjectUtils.toString(info.get("KCXXBZ")));
			} else {
				info.put("KCXXBZ_NAME", "--");
			}
			if ("0".equals(exam_state) || "1".equals(exam_state)) {
				info.put("EXAM_SCORE2_NAME", ObjectUtils.toString(info.get("EXAM_SCORE2")));
			} else {
				info.put("EXAM_SCORE2_NAME", "--");
			}
			if ("0".equals(ObjectUtils.toString(info.get("STUDY_FLG")))) {
				info.put("STUDY_FLG_NAME", "未学习");
			} else if ("0".equals(exam_state)) {
				info.put("STUDY_FLG_NAME", "未通过");
			} else if ("1".equals(exam_state)) {
				info.put("STUDY_FLG_NAME", "已通过");
			} else if ("2".equals(exam_state)) {
				info.put("STUDY_FLG_NAME", "学习中");
			} else if ("3".equals(exam_state)) {
				info.put("STUDY_FLG_NAME", "登记中");
			} else {
				info.put("STUDY_FLG_NAME", "学习中");
			}
		}

		String content =  CSVUtils.formatCsvData(data, header, key);
		return content;
	}
	
	/**
	 * 导出登记成绩
	 */
	public Workbook expExamRegisterRecord(Map formMap) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			List recordlist = gjtExamRecordNewDao.getExamRegisterList(formMap);
			
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			
			//标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("身份证");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学员类型");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("入学学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("报读专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程ID");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("中央课程ID");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("中央课程名称");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("试卷号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考试方式");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考试单位");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("形考比例(%)");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考试成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("总成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("成绩状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			
			if(EmptyUtils.isNotEmpty(recordlist)){
				for (int i=0; i<recordlist.size(); i++){
					Map e = (Map)recordlist.get(i);
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("XM")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("XH")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SFZH")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("USER_TYPE_NAM")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KKXQ")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCH")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SOURCE_KCH")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SOURCE_KCMC")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("EXAM_NO")));
				
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KSFS_NAME")));
					
					String exam_state = ObjectUtils.toString(e.get("EXAM_STATE"));
					
					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
						if ("1".equals(ObjectUtils.toString(e.get("KSDW")))) {
							cell.setCellValue("省");
						} else if ("2".equals(ObjectUtils.toString(e.get("KSDW")))) {
							cell.setCellValue("中央");
						} else if ("3".equals(ObjectUtils.toString(e.get("KSDW")))) {
							cell.setCellValue("分校");
						} else {
							cell.setCellValue("--");
						}
					} else {
						cell.setCellValue("--");
					}
					
					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state) || "3".equals(exam_state)) {
						cell.setCellValue(ObjectUtils.toString(e.get("XK_PERCENT")));
					} else {
						cell.setCellValue("--");
					}
						
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE")));
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE1")));
					
					cell = row.createCell(cellIndex++);
					if ("0".equals(exam_state) || "1".equals(exam_state)) {
						cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE2")));
					} else {
						cell.setCellValue("--");
					}
					
					cell = row.createCell(cellIndex++);
					if ("0".equals(ObjectUtils.toString(e.get("STUDY_FLG")))) {
						cell.setCellValue("未学习");
					} else if ("0".equals(exam_state)) {
						cell.setCellValue("未通过");
					} else if ("1".equals(exam_state)) {
						cell.setCellValue("已通过");
					} else if ("2".equals(exam_state)) {
						cell.setCellValue("学习中");
					} else if ("3".equals(exam_state)) {
						cell.setCellValue("登记中");
					} else {
						cell.setCellValue("学习中");
					}
					
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("ORG_NAME")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 获取学员历史成绩
	 *
	 * @param formMap
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getHistoryScore(Map<String, Object> formMap) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();

		Map<String,String> tm = new HashMap<String,String>();

		List<Map<String, String>> list = gjtExamRecordNewDao.getHistoryScore(formMap);
		if(list!=null &&list.size()>0){
			for (Map temp:list) {
				tm.put(org.apache.commons.lang3.ObjectUtils.toString(temp.get("TERM_ID")), org.apache.commons.lang3.ObjectUtils.toString(temp.get("TERM_ID")));
			}
			for (String string:tm.keySet()) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("HISTORY_MSG",new ArrayList());
				for (Map temp:list){
					if(string.equals(org.apache.commons.lang3.ObjectUtils.toString(temp.get("TERM_ID")))){
						map.put("TERM_ID", org.apache.commons.lang3.ObjectUtils.toString(temp.get("TERM_ID")));
						map.put("TERM_NAME", org.apache.commons.lang3.ObjectUtils.toString(temp.get("TERM_NAME")));
						if(EmptyUtils.isNotEmpty(org.apache.commons.lang3.ObjectUtils.toString(temp.get("XCX_PERCENT"),""))){
							temp.put("ZJX_PERCENT",100-Integer.parseInt(org.apache.commons.lang3.ObjectUtils.toString(temp.get("XCX_PERCENT"),"0")));
						}else {
							temp.put("ZJX_PERCENT","0");
						}
						((List)map.get("HISTORY_MSG")).add(temp);
					}
				}
				resultList.add(map);
			}
		}
		return resultList;
	}
	
	/**
	 * 定时锁定登记成绩
	 */
	public int registerExamState(Map formMap) {
		int num = 0;
		try {
			List recordList = gjtExamRecordNewDao.getRegisterExamList(formMap);
			if (EmptyUtils.isNotEmpty(recordList)) {
				for (int i=0; i<recordList.size(); i++) {
					Map recordMap = (Map)recordList.get(i);
					recordMap.put("EXAM_SCORE", ObjectUtils.toString(recordMap.get("MY_POINT")));
					recordMap.put("EXAM_SCORE1", ObjectUtils.toString(recordMap.get("TEST_POINT")));
					recordMap.put("PROGRESS", ObjectUtils.toString(recordMap.get("PROGRESS")));
					num += gjtExamRecordNewDao.registerExamState(recordMap);
					// gjtExamRecordNewDao.updateExamState(recordMap);
				}
			}
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 考试管理=》考情分析(考试预约明细)
	 * @return
	 */
	public Page getRecordAppointmentList(Map searchParams, PageRequest pageRequst) {
		return gjtExamRecordNewDao.getRecordAppointmentList(searchParams, pageRequst);
	}
	
	/**
	 * 考试管理=》考情分析(考试预约统计)
	 * @return
	 */
	public int getExamAppointmentCount(Map searchParams) {
		int num = 0;
		try {
			num = gjtExamRecordNewDao.getExamAppointmentCount(searchParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 导出考试情况数据
	 * @return
	 */
	public String exportExamAppointment(Map searchParams, String path) {
		String zipFileName = "examAppointment.zip";
		try {
			String folderName = "考试预约数据统计表";
			List list1 = gjtExamRecordNewDao.getRecordAppointmentPlan(searchParams);
			String[] titles1 = new String[] { "试卷号", "科目名称", "考试形式", "课程信息", "已达标，未预约人数", "已达标，已预约人数","未达标，已预约人数", "未达标，未预约人数"};
            String[] dbNames1 = new String[] {"EXAM_NO", "EXAM_PLAN_NAME", "EXAM_TYPE_NAME", "COURSE_NAME", "EXAM_STATE1", "EXAM_STATE2", "EXAM_STATE3", "EXAM_STATE4"};
			String fileName1 = ExcelService.renderExcel(list1, titles1, dbNames1, "考试情况统计表", path + folderName + File.separator, "考试情况统计表");
            
            String[] titles2 = new String[] { "姓名", "学号", "手机", "层次", "学期", "专业","试卷号",
                    "科目名称", "考试形式", "课程代码", "课程名称", "学习进度", "学习成绩", "考试要求", "考试预约状态" };
            String[] dbNames2 = new String[] {"XM", "XH", "SJH", "PYCC_NAME", "GRADE_NAME", "ZYMC", "EXAM_NO",
            		"EXAM_PLAN_NAME", "EXAM_TYPE_NAME", "SOURCE_KCH", "SOURCE_KCMC", "PROGRESS", "SCORE", "EXAM_PLAN_LIMIT", "BESPEAK_STATE"};
            
            List tempList = gjtExamRecordNewDao.getRecordAppointmentList(searchParams);
			List list2 = new ArrayList();
			for (int i=0; i<tempList.size(); i++) {
				Map tempMap = (Map)tempList.get(i);
				String exam_plan_limit = ObjectUtils.toString(tempMap.get("EXAM_PLAN_LIMIT"));
				String score = ObjectUtils.toString(tempMap.get("SCORE"));
				if (ToolUtil.isNumeric(exam_plan_limit) && ToolUtil.isNumeric(score) && Double.parseDouble(score)>=Double.parseDouble(exam_plan_limit)) {
					exam_plan_limit = "已达标";
				} else {
					exam_plan_limit = "未达标";
				}
				
				String bespeak_state = ObjectUtils.toString(tempMap.get("BESPEAK_STATE")); 
				if ("1".equals(bespeak_state)) {
					bespeak_state = "已预约";
				} else {
					bespeak_state = "未预约";
				}
				tempMap.put("EXAM_PLAN_LIMIT", exam_plan_limit);
				tempMap.put("BESPEAK_STATE", bespeak_state);
				
				String exam_state = ObjectUtils.toString(tempMap.get("EXAM_STATE"));
				if (!"2".equals(exam_state)) {
					tempMap.put("SCORE",ObjectUtils.toString(tempMap.get("EXAM_SCORE")));
				}
				
				list2.add(tempMap);
			}
            int page = 1, pageSize = 65500;
            // 是否有下一页
            while(((page - 1)*pageSize) < list2.size()) { 
                List examNewList = new CopyOnWriteArrayList();
                // 当前页数据
                for(int i = (page - 1)*pageSize; i < page*pageSize && i < list2.size(); i++) { 
                	examNewList.add(list2.get(i));
                }
                String fileName = ExcelService.renderExcel(examNewList, titles2, dbNames2, "考试情况明细统计表", path + folderName + File.separator, "考试情况明细统计表" + page);
                page++;
            }

            // zip
            ZipFileUtil.zipDir(path + folderName, path + zipFileName);
            FileKit.delFile(path + folderName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipFileName;
	}
	
	/**
	 * 考试情况明细查询列表
	 * @return
	 */
	public Page getExamDetailList(Map searchParams, PageRequest pageRequst) {
		return gjtExamRecordNewDao.getExamDetailList(searchParams, pageRequst);
	}
	
	/**
	 * 导出考试情况明细
	 * @return
	 */
	public String exportExamDetail(Map searchParams, String path) {
		String zipFileName = "examDetail.zip";
		try {
			String folderName = "考试情况统计表";
			List list1 = gjtExamRecordNewDao.getExamDetailPlan(searchParams);
			String[] titles1 = new String[] { "试卷号", "科目名称", "考试形式", "课程信息", "已达标，未考试人数","已达标，登记中人数", "已达标，已通过人数", "已达标，未通过人数", "未达标，未考试人数", "未达标，登记中人数", "未达标，未通过人数"};
            String[] dbNames1 = new String[] {"EXAM_NO", "EXAM_PLAN_NAME", "EXAM_TYPE_NAME", "COURSE_NAME", "EXAM_STATE1", "EXAM_STATE2", "EXAM_STATE3", "EXAM_STATE4", "EXAM_STATE5", "EXAM_STATE6", "EXAM_STATE7"};
			String fileName1 = ExcelService.renderExcel(list1, titles1, dbNames1, "考试情况统计表", path + folderName + File.separator, "考试情况统计表");
            
            String[] titles2 = new String[] { "姓名", "学号", "手机", "层次", "学期", "专业","试卷号",
                    "科目名称", "考试形式", "课程代码", "课程名称", "学习进度", "学习成绩", "考试成绩", "总成绩", "考试要求", "考试成绩状态" };
            String[] dbNames2 = new String[] {"XM", "XH", "SJH", "PYCC_NAME", "GRADE_NAME", "ZYMC", "EXAM_NO",
            		"EXAM_PLAN_NAME", "EXAM_TYPE_NAME", "SOURCE_KCH", "SOURCE_KCMC", "PROGRESS", "XCX_SCORE", "ZJX_SCORE", "ZCJ_SCORE", "ZCJ_SCORE", "ZCJ_SCORE"};
            
			List list2 = gjtExamRecordNewDao.getExamDetailList(searchParams);
            int page = 1, pageSize = 65500;
            // 是否有下一页
            while(((page - 1)*pageSize) < list2.size()) { 
                List examNewList = new CopyOnWriteArrayList();
                // 当前页数据
                for(int i = (page - 1)*pageSize; i < page*pageSize && i < list2.size(); i++) { 
                	examNewList.add(list2.get(i));
                }
                String fileName = ExcelService.renderExcel(examNewList, titles2, dbNames2, "考试情况明细统计表", path + folderName + File.separator, "考试情况明细统计表" + page);
                page++;
            }

            // zip
            ZipFileUtil.zipDir(path + folderName, path + zipFileName);
            FileKit.delFile(path + folderName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipFileName;
	}
	
	/**
	 * 考试情况明细查询列表(统计数字)
	 * @return
	 */
	public int getExamDetailCount(Map searchParams) {
		return gjtExamRecordNewDao.getExamDetailCount(searchParams);
	}

	@Override
	public Page<Map<String, Object>> queryStudentExamListByPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtExamRecordNewDao.queryStudentExamListByPage(searchParams, pageRequst);
	}

	@Override
	public long getStudentExamCount(Map searchParams) {
		return gjtExamRecordNewDao.getStudentExamCount(searchParams);
	}

	/**
	 * 学生综合信息查询=》学员考情导出
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadExcelExportByExamAppointment(Map searchParams) {
		Date now = new Date();
		List<Map<String, Object>> resultList = gjtExamRecordNewDao.getStudentExamList(searchParams);
		XSSFWorkbook wb = new XSSFWorkbook();
		//为了能够使用换行，您需要设置单元格的样式 wrap=true  
		XSSFCellStyle s = wb.createCellStyle();  
        s.setWrapText(true);
		Sheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			Row row;
			Cell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学员类型");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学籍状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("应考科目");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("已考科目");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未考科目");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("本次已预约");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("本次未预约");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XXZX_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, ObjectUtils.toString(e.get("USER_TYPE"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, ObjectUtils.toString(e.get("XJZT"))));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				BigDecimal shouldExamCount = (BigDecimal) e.get("SHOULD_EXAM_COUNT");
				cell.setCellValue(shouldExamCount.intValue());
				cell = row.createCell(cellIndex++);
				BigDecimal beenExamCount = (BigDecimal) e.get("BEEN_EXAM_COUNT");
				BigDecimal beenThroughCount = (BigDecimal) e.get("BEEN_THROUGH_COUNT");
				BigDecimal notThroughCount = (BigDecimal) e.get("NOT_THROUGH_COUNT");
				String beenExam = beenExamCount.intValue() + "\n(已通过："+beenThroughCount.intValue()+" 未通过："+notThroughCount.intValue()+")";
				cell.setCellValue(beenExam);
				cell.setCellStyle(s);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(shouldExamCount.intValue() - beenExamCount.intValue());
				cell = row.createCell(cellIndex++);
				BigDecimal makeCount = (BigDecimal) e.get("MAKE_COUNT");
				String make = makeCount.intValue() + (makeCount.intValue() >= 8 ?"\n(已约满8科)" : "");
				cell.setCellValue(make);
				cell.setCellStyle(s);
				cell = row.createCell(cellIndex++);
				// 获取预约时间
				Date bookSt = (Date) e.get("BOOK_ST");
				Date bookEnd = (Date) e.get("BOOK_END");
				Date booksSt = (Date) e.get("BOOKS_ST");
				Date booksEnd = (Date) e.get("BOOKS_END");
				String notApp = (shouldExamCount.intValue() - makeCount.intValue()) + "";
				/**
				 * 这里的逻辑是:
				 * ├─1.第一种情况：应考科目 = 已约科目					===→ 考试正常
				 * ├─2.第二种情况：应考科目 > 已约科目 (再次细分)
				 * │  ├─2.1.当前时间未到预约时间						===→ 考试正常
				 * │  ├─2.2.当前时间在预约范围之内(再次细分)
				 * │  │  ├─2.2.1.已约满8科							===→ 异常，已约满，需下次再约
				 * │  │  └─2.2.2.未约满8科							===→ 异常，预约范围内，需督促
				 * │  │  
				 * │  └─2.3.当前时间在预约结束之后（如果有第二次预约时间，那么有两个区间，第一次预约结束时间至第二次预约开始时间/第二次预约结束时间之后）(再次细分)
				 * │     ├─2.3.1.已约满8科							===→ 异常，已约满，需下次再约
				 * │     └─2.3.2.未约满8科							===→ 异常，预约已过期，漏报考
				 * │     
				 */
				if(shouldExamCount.intValue() == makeCount.intValue()) {
					notApp += "\n(正常)";
				} else {
					if(now.getTime() < bookSt.getTime()) {
						notApp += "\n(正常)";
					} else if(makeCount.intValue() >= 8) { // 合并2.2和2.3的已约满8科 ===→ 异常，已约满，需下次再约
						notApp += "\n(异常，已约满，需下次再约)";
					} else {
						if(now.getTime() >= bookSt.getTime() && now.getTime() <= bookEnd.getTime() || (booksEnd != null && now.getTime() >= booksSt.getTime() && now.getTime() <= booksEnd.getTime())) {
							notApp += "\n(异常，预约范围内，需督促)";
						} else {
							notApp += "\n(异常，预约已过期，漏报考)";
						}
					}
				}
				cell.setCellValue(notApp);
				cell.setCellStyle(s);
				cell = row.createCell(cellIndex++);
			}
		}
		return wb;
	}

	/**
	 * 学员考试情况
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> countStudentExamSituation(Map searchParams) {
		return gjtExamRecordNewDao.countStudentExamSituation(searchParams);
	}
}
