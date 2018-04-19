package com.gzedu.xlims.serviceImpl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.api.ApiOpeanClassDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.dao.edumanage.OpenClassDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.opi.OpiCourse;
import com.gzedu.xlims.pojo.opi.OpiCourseData;
import com.gzedu.xlims.pojo.opi.OpiStudchoose;
import com.gzedu.xlims.pojo.opi.OpiStudent;
import com.gzedu.xlims.pojo.opi.OpiStudentData;
import com.gzedu.xlims.pojo.opi.OpiTchchoose;
import com.gzedu.xlims.pojo.opi.OpiTeacher;
import com.gzedu.xlims.pojo.opi.OpiTeacherData;
import com.gzedu.xlims.pojo.opi.OpiTerm;
import com.gzedu.xlims.pojo.opi.OpiTermChooseData;
import com.gzedu.xlims.pojo.opi.OpiTermClass;
import com.gzedu.xlims.pojo.opi.OpiTermClassData;
import com.gzedu.xlims.pojo.opi.OpiTermCourse;
import com.gzedu.xlims.pojo.opi.OpiTermCourseData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.gzedu.xlims.service.pcourse.PCourseServer;

import net.spy.memcached.MemcachedClient;

@Service
public class ApiOpenClassServiceImpl implements ApiOpenClassService {
	
	@Autowired
	ApiOpeanClassDao apiOpeanClassDao;
	
	@Autowired
	private PCourseServer pCourseServer;
	
	@Autowired
	private OpenClassDao openClassDao;
	
	@Autowired
    private GjtGradeDao gjtGradeDao;
	
	@Autowired
    private GjtCourseDao gjtCourseDao;
	
	@Autowired
    private GjtSchoolInfoDao gjtSchoolInfoDao;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	@Autowired
	private GjtCourseService gjtCourseService;
	
	@Autowired
	private GjtRecResultDao gjtRecResultDao;
	
	@Autowired
	private LcmsOnlineLessonService lcmsOnlineLessonService;
	
	@Autowired
	ApiOucSyncService apiOucSyncService;

	/**
	 * 初始化学员选课记录
	 */
	public Map initStudentChoose(Map formMap) {
		Map resultMap = new HashMap();
		int recNum = 0;
		int classNum = 0;
		int syncNum = 0;
		int lessonNum = 0;
		String studentId = ObjectUtils.toString(formMap.get("STUDENT_ID"));
		try {
			// 是否转学期标识
			String turnTermFlg = ObjectUtils.toString(formMap.get("TURN_TERM_FLG"));
			
			// 放入缓存，在两分钟内不重复调用
			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("CHOOSE_"+studentId)))) {
				return resultMap;
			} else {
				memcachedClient.add("CHOOSE_"+studentId, 120, studentId);
			}
			
			// 批次号
			String batchNo = DateUtils.getStrDate();
			formMap.put("BATCHNO", batchNo);
			
			if (EmptyUtils.isNotEmpty(studentId)) {
				List stuList = apiOpeanClassDao.getStudentInfo(formMap);
				if (EmptyUtils.isNotEmpty(stuList)) {
					Map stuMap = (Map)stuList.get(0);
					
					// school_model 5 学习网模式，不影响现有业务先加这个判断，后面业务确定再改，先这样吧
					String school_model = ObjectUtils.toString(stuMap.get("SCHOOL_MODEL"));
					formMap.put("SCHOOL_MODEL", school_model);
					
					String pcourse_stud_id = ObjectUtils.toString(stuMap.get("PCOURSE_STUD_ID"));
					if (EmptyUtils.isEmpty(pcourse_stud_id)) {
						// 同步用户信息
						OpiStudent op = new OpiStudent();
						op.setCREATED_BY("admin");
						op.setEE_NO(ObjectUtils.toString(stuMap.get("EENO")));
						op.setEMAIL(ObjectUtils.toString(stuMap.get("DZXX")));
						op.setIMG_PATH(ObjectUtils.toString(stuMap.get("ZP")));
						op.setLMS_NO(ObjectUtils.toString(stuMap.get("XH")));
						op.setMANAGER_ID(ObjectUtils.toString(stuMap.get("BZR_ID")));
						op.setMOBILE_NO(ObjectUtils.toString(stuMap.get("SJH")));
						op.setREALNAME(ObjectUtils.toString(stuMap.get("XM")));
						if ("1".equals(ObjectUtils.toString(stuMap.get("XBM")))) {
							op.setSEX("男");
						} else if ("2".equals(ObjectUtils.toString(stuMap.get("XBM")))) {
							op.setSEX("女");
						} else {
							op.setSEX(ObjectUtils.toString(stuMap.get("XBM")));
						}
						op.setSTUD_AREA(ObjectUtils.toString(stuMap.get("HKSZD")));
						op.setSTUD_ID(ObjectUtils.toString(stuMap.get("STUDENT_ID")));
						op.setSTUD_USER_ID(ObjectUtils.toString(stuMap.get("ATID")));
						op.setXLCLASS_ID(ObjectUtils.toString(stuMap.get("CLASS_ID")));
						op.setXLCLASS_NAME(ObjectUtils.toString(stuMap.get("BJMC")));

						OpiStudentData od = new OpiStudentData(ObjectUtils.toString(stuMap.get("APPID")), op);
						RSimpleData synchroCourse = pCourseServer.synchroStudent(od);
						if (EmptyUtils.isNotEmpty(synchroCourse) && synchroCourse.getStatus()==1) {
							apiOpeanClassDao.updStudentSyncState(formMap);
						}
					}
					
					if (!"5".equals(school_model)) {
						// 删除不存在自己报读的产品教学计划内的选课
						List noRecList = apiOpeanClassDao.getNoPlanRecList(formMap);
						if (EmptyUtils.isNotEmpty(noRecList)) {
							int num = apiOpeanClassDao.delNoPlanRecList(formMap);
							if (num>0) {
								// 删除学习平台的选课
								for (int i=0; i<noRecList.size(); i++) {
									Map recMap = (Map)noRecList.get(i);
									if (EmptyUtils.isNotEmpty(ObjectUtils.toString(recMap.get("CLASS_ID")))) {
										apiOpeanClassDao.delNoPlanRecClass(recMap);
									}
								}
							}
						}
					}
					
					List recList = apiOpeanClassDao.getNoRecResult(formMap);
					if (EmptyUtils.isNotEmpty(recList)) {
						// 新增的选课记录
						recNum = apiOpeanClassDao.addRecResult(formMap);
					}
					
					// 新增课程班记录
					List classList = apiOpeanClassDao.getNoCourseClass(formMap);
					if (EmptyUtils.isNotEmpty(classList)) {
						for (int i=0; i<classList.size(); i++) {
							Map classMap = (Map)classList.get(i);
							
							String plan_termcourse_id = ObjectUtils.toString(classMap.get("PLAN_TERMCOURSE_ID"));
							String old_termcourse_id = ObjectUtils.toString(classMap.get("OLD_TERMCOURSE_ID"));
							if (EmptyUtils.isNotEmpty(plan_termcourse_id)) {
								classMap.put("TERMCOURSE_ID", plan_termcourse_id);
							}
							classMap.put("TURN_TERM_FLG", turnTermFlg);
							if (EmptyUtils.isNotEmpty(ObjectUtils.toString(classMap.get("TERMCOURSE_ID")))) {
								List cList = apiOpeanClassDao.getClassInfo(classMap);
								
								String bh = "";
								String className = "";
								String kch = ObjectUtils.toString(classMap.get("KCH"));
								
								if (EmptyUtils.isNotEmpty(cList)) {
									Map cMap = (Map)cList.get(0);
									bh = ObjectUtils.toString(formMap.get("BH"));
									className = ObjectUtils.toString(formMap.get("BJMC"));
									classMap.put("CLASS_ID", ObjectUtils.toString(cMap.get("CLASS_ID")));
									classMap.put("CLASS_STUDENT_ID", SequenceUUID.getSequence());
									classNum+=apiOpeanClassDao.addCourseClassStu(classMap);
									apiOpeanClassDao.updRecTermCourse(classMap);
								} else {
									bh = ToolUtil.getRandomNum(8);
									int class_count = apiOpeanClassDao.getClassCount(classMap)+1;
									// 新增班级
									className = ObjectUtils.toString(classMap.get("KCMC"))+ObjectUtils.toString(classMap.get("GRADE_NAME"))+"0"+class_count+"班";
									if ("Y".equals(turnTermFlg) && EmptyUtils.isNotEmpty(old_termcourse_id)) {
										classMap.put("TURN_TERM_FLG", "Y");
										classMap.put("OLD_TERMCOURSE_ID", old_termcourse_id);
										className = ObjectUtils.toString(classMap.get("KCMC"))+ObjectUtils.toString(classMap.get("GRADE_NAME"))+class_count+"跟读班";
									} else {
										classMap.put("TURN_TERM_FLG", "N");
										classMap.put("OLD_TERMCOURSE_ID", "");
									}
									String termId = ObjectUtils.toString(classMap.get("TERM_ID"));
									String courseId = ObjectUtils.toString(classMap.get("COURSE_ID"));
									String xxId = ObjectUtils.toString(classMap.get("XX_ID"));
									String termcourse_id = ObjectUtils.toString(classMap.get("TERMCOURSE_ID"));
									String gradeName = ObjectUtils.toString(classMap.get("GRADE_NAME"));
									String class_id = SequenceUUID.getSequence();
			            			classMap.put("BH", bh);
			        				classMap.put("BJMC", className);
			        				classMap.put("TERM_ID", termId);
			        				classMap.put("CLASS_TYPE", "course");
			        				classMap.put("COURSE_ID", courseId);
			        				classMap.put("XX_ID", xxId);
			        				classMap.put("TERMCOURSE_ID", termcourse_id);
			        				classMap.put("AOTU_FLG", "Y");
			        				classMap.put("LIMIT_NUM", "500");
		            				classMap.put("CLASS_ID", class_id);
		            				openClassDao.addCourseClass(classMap);
		            				
		            				classMap.put("CLASS_STUDENT_ID", SequenceUUID.getSequence());
									classNum+=apiOpeanClassDao.addCourseClassStu(classMap);
									apiOpeanClassDao.updRecTermCourse(classMap);
		            				
		            				if (EmptyUtils.isNotEmpty(class_id)) {
		                				OpiTermClass termClass = new OpiTermClass();
		                                termClass.setCLASS_ID(class_id);
		                                termClass.setTERMCOURSE_ID(termcourse_id);
		                                termClass.setCLASS_NAME(className);
		                                termClass.setYEAR(gradeName);
		                                termClass.setCREATED_BY("admin");
		                                termClass.setIS_EXPERIENCE("N");
		                                termClass.setTURN_TERM_FLG(ObjectUtils.toString(classMap.get("TURN_TERM_FLG")));
		                                termClass.setOLD_TERMCOURSE_ID(ObjectUtils.toString(classMap.get("OLD_TERMCOURSE_ID")));

		                                OpiTermClassData data = new OpiTermClassData(termClass);
		                                RSimpleData rSimpleData = pCourseServer.synchroTermClass(data);
		                                if (EmptyUtils.isNotEmpty(rSimpleData) && rSimpleData.getStatus() == 1) {
		                                	// 标识已同步
		                                	openClassDao.updClassSync(classMap);
		                                }
		                			}
								}
							}
						}
					}
					
					List noPRecList = apiOpeanClassDao.getNoPcoruseRecList(formMap);
					if (EmptyUtils.isNotEmpty(noPRecList)) {
						for (int i=0; i<noPRecList.size(); i++) {
							Map chooseMap = (Map)noPRecList.get(i);
							Map tempMap = new HashMap();
							tempMap.put("CLASS_ID", ObjectUtils.toString(chooseMap.get("CLASS_ID")));
							tempMap.put("TERMCOURSE_ID", ObjectUtils.toString(chooseMap.get("TERMCOURSE_ID")));
							tempMap.put("CHOOSE_ID", ObjectUtils.toString(chooseMap.get("CHOOSE_ID")));
							tempMap.put("STUD_ID", ObjectUtils.toString(chooseMap.get("STUD_ID")));
							tempMap.put("UPDATED_BY", "admin");
							pCourseServer.delStudChoose(tempMap);
						}
					}
					
					// 同步选课到学习平台
					List chooseList = apiOpeanClassDao.getNoSyncRecResult(formMap);
					if (EmptyUtils.isNotEmpty(chooseList)) {
						for (int i=0; i<chooseList.size(); i++) {
							Map chooseMap = (Map)chooseList.get(i);
							OpiStudchoose chooseData = new OpiStudchoose();
							chooseData.setCHOOSE_ID(ObjectUtils.toString(chooseMap.get("REC_ID")));
							chooseData.setCLASS_ID(ObjectUtils.toString(chooseMap.get("CLASS_ID")));
							chooseData.setCREATED_BY("admin");
							chooseData.setSTUD_ID(ObjectUtils.toString(chooseMap.get("STUDENT_ID")));
							chooseData.setTERMCOURSE_ID(ObjectUtils.toString(chooseMap.get("TERMCOURSE_ID")));
							OpiTermChooseData data = new OpiTermChooseData(chooseData);
							// 同步学习平台选课接口
							RSimpleData rData = pCourseServer.synchroTermChoose(data);
							
							if (EmptyUtils.isNotEmpty(rData) && rData.getStatus()==1) {
								// 更新同步成功状态
								apiOpeanClassDao.updClassStuSyncStatus(chooseMap);
								apiOpeanClassDao.updRecSyncStatus(chooseMap);
								syncNum++;
							}
						}
					}
				}
				
				if (recNum>0 || classNum>0 || syncNum>0) {
					List oldList = apiOpeanClassDao.getOldRecId(formMap);
					if (EmptyUtils.isNotEmpty(oldList)) {
						// 恢复学习平台的成绩记录
						for (int i=0; i<oldList.size(); i++) {
							Map oldMap = (Map)oldList.get(i);
							String old_chooseid = ObjectUtils.toString(oldMap.get("OLD_CHOOSE_ID"));
							String old_chooseid1 = ObjectUtils.toString(oldMap.get("OLD_CHOOSE_ID1"));
							if (EmptyUtils.isEmpty(old_chooseid) || "NULL".equals(old_chooseid1)) {
								old_chooseid = old_chooseid1;
							}
							
							//恢复学员选课表中的成绩记录
							apiOpeanClassDao.updateGjtRecResult(ObjectUtils.toString(oldMap.get("OLD_CHOOSE_ID")),ObjectUtils.toString(oldMap.get("REC_ID")), batchNo);
							
							oldMap.put("OLD_CHOOSEID", old_chooseid);
							oldMap.put("NEW_CHOOSEID", ObjectUtils.toString(oldMap.get("REC_ID")));
							pCourseServer.copyActDynaToNewCourse(oldMap);
						}
						
						for(int i=0;i<oldList.size();i++){
							Map recMap = (Map)oldList.get(i);
							
							String old_chooseid = ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID"));
							String old_chooseid1 = ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID1"));
							if (EmptyUtils.isNotEmpty(old_chooseid1)) {
								recMap.put("OLD_CHOOSE_ID", old_chooseid1);
							}
							
							if (EmptyUtils.isNotEmpty(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID"))) && !"NULL".equals(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")))) {
								//更改学员预约表中的REC_ID
								apiOpeanClassDao.updateGjtExamAppointment(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")),ObjectUtils.toString(recMap.get("REC_ID")));
								//更改学员的成绩历史记录表
								GjtRecResult oldGjtRecResult=gjtRecResultDao.findOne(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")));//旧的选课记录
								GjtRecResult newGjtRecResult=gjtRecResultDao.findOne(ObjectUtils.toString(recMap.get("REC_ID")));//新的选课记录
								
								if (EmptyUtils.isNotEmpty(oldGjtRecResult) && EmptyUtils.isNotEmpty(newGjtRecResult)) {
									//查询旧的历史成绩表
									List learnRepairList=apiOpeanClassDao.
											getGjtLearnRepair(ObjectUtils.toString(oldGjtRecResult.getTeachPlanId()),ObjectUtils.toString(recMap.get("STUDENT_ID")));
									if(EmptyUtils.isNotEmpty(learnRepairList)){
										for(int j=0;j<learnRepairList.size();j++){
											Map learnRepairMap=(Map) learnRepairList.get(j);
											//更新历史成绩表中的TEACH_PLAN_ID
											apiOpeanClassDao.updateLearnRepair(learnRepairMap,newGjtRecResult.getTeachPlanId());
										}
									}
								}
							}
						}												
					}
				}

				// 同步直播信息
				try {
					lessonNum = lcmsOnlineLessonService.initStudentOnlineLesson(studentId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				resultMap.put("syncNum", syncNum);
				resultMap.put("recNum", recNum);
				resultMap.put("classNum", classNum);
				resultMap.put("lessonNum", lessonNum);
				resultMap.put("result", "success");
				resultMap.put("message", "成功");
			} else {
				resultMap.put("result", "failure");
				resultMap.put("message", "参数STUDENT_ID不能为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
			memcachedClient.delete("CHOOSE_"+studentId);
		}
		return resultMap;
	}
	
	/**
	 * 恢复旧的学习记录
	 * @param formMap
	 * @return
	 */
	public Map stuOldToNewRec(Map formMap) {
		Map resultMap = new HashMap();
		int num = 0;
		int rec = 0;
		try {
			List oldList = apiOpeanClassDao.getOldRecId(formMap);
			System.out.println("==="+oldList.size());
			if (EmptyUtils.isNotEmpty(oldList)) {
				// 恢复学习平台的成绩记录
				for (int i=0; i<oldList.size(); i++) {
					Map oldMap = (Map)oldList.get(i);
					String old_chooseid = ObjectUtils.toString(oldMap.get("OLD_CHOOSE_ID"));
					String old_chooseid1 = ObjectUtils.toString(oldMap.get("OLD_CHOOSE_ID1"));
					if (EmptyUtils.isEmpty(old_chooseid) || "NULL".equals(old_chooseid)) {
						old_chooseid = old_chooseid1;
					}
					oldMap.put("OLD_CHOOSEID", old_chooseid);
					oldMap.put("NEW_CHOOSEID", ObjectUtils.toString(oldMap.get("REC_ID")));
					pCourseServer.copyActDynaToNewCourse(oldMap);
					rec++;
				}
				
				for(int i=0;i<oldList.size();i++){
					Map recMap = (Map)oldList.get(i);
					
					String old_chooseid = ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID"));
					String old_chooseid1 = ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID1"));
					if (EmptyUtils.isNotEmpty(old_chooseid1)) {
						recMap.put("OLD_CHOOSE_ID", old_chooseid1);
					}
					
					if (EmptyUtils.isNotEmpty(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID"))) && !"NULL".equals(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")))) {
						//恢复学员选课表中的成绩记录
						// apiOpeanClassDao.updateGjtRecResult(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")),ObjectUtils.toString(recMap.get("REC_ID")));							
						//更改学员预约表中的REC_ID
						apiOpeanClassDao.updateGjtExamAppointment(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")),ObjectUtils.toString(recMap.get("REC_ID")));
						//更改学员的成绩历史记录表
						GjtRecResult oldGjtRecResult=gjtRecResultDao.findOne(ObjectUtils.toString(recMap.get("OLD_CHOOSE_ID")));//旧的选课记录
						GjtRecResult newGjtRecResult=gjtRecResultDao.findOne(ObjectUtils.toString(recMap.get("REC_ID")));//新的选课记录
						//查询旧的历史成绩表
						if (EmptyUtils.isNotEmpty(oldGjtRecResult) && EmptyUtils.isNotEmpty(newGjtRecResult)) {
							List learnRepairList=apiOpeanClassDao.getGjtLearnRepair(ObjectUtils.toString(oldGjtRecResult.getTeachPlanId()),ObjectUtils.toString(recMap.get("STUDENT_ID")));
							if(EmptyUtils.isNotEmpty(learnRepairList)){
								for(int j=0;j<learnRepairList.size();j++){
									Map learnRepairMap=(Map) learnRepairList.get(j);
									//更新历史成绩表中的TEACH_PLAN_ID
									apiOpeanClassDao.updateLearnRepair(learnRepairMap,newGjtRecResult.getTeachPlanId());
								}
							}
						}
						num++;
					}
				}												
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("rec", rec);
		resultMap.put("num", num);
		return resultMap;
	}
	
	/***
	 * 初始化期课程记录
	 * @param
	 * @return
	 */
	public Map initTermCourse(Map formMap) {
		Map resultMap = new HashMap();
		/*int initNum = 0;
		try {
			List tList = apiOpeanClassDao.getNoTermCourse(formMap);
			if (EmptyUtils.isNotEmpty(tList)) {
				for (int i=0; i<tList.size(); i++) {
					Map tMap = (Map)tList.get(i);
					List planList = apiOpeanClassDao.getTeachPlanId(tMap);
					if (EmptyUtils.isNotEmpty(planList)) {
						Map pMap = (Map)planList.get(0);
						tMap.put("TERMCOURSE_ID", ObjectUtils.toString(pMap.get("TEACH_PLAN_ID")));
						tMap.put("TERM_ID", ObjectUtils.toString(tMap.get("GRADE_ID")));
						initNum+=apiOpeanClassDao.addTermCourseinfo(tMap);
						
						List cList = apiOpeanClassDao.getClassTerm(tMap);
						if (EmptyUtils.isNotEmpty(cList)) {
							Map cMap = (Map)cList.get(0);
							cMap.put("TERMCOURSE_ID", ObjectUtils.toString(tMap.get("TERMCOURSE_ID")));
							cMap.put("TEACH_PLAN_ID", ObjectUtils.toString(tMap.get("TERMCOURSE_ID")));
							apiOpeanClassDao.updClassTerm(cMap);
							apiOpeanClassDao.updRecTerm(cMap);
						}
					}
				}
			}
			
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}*/
		return resultMap;
	}
	
	/***
	 * 初始化调班数据
	 * @param
	 * @return
	 */
	@Deprecated
	public Map initCourseClass(Map formMap) {
		Map resultMap = new HashMap();
		int initNum = 0;
		try {
			// 查询出期课程需要调班的学员数据
			List recList = apiOpeanClassDao.getRecClassList(formMap);
			if (EmptyUtils.isNotEmpty(recList)) {
				for (int i=0; i<recList.size(); i++) {
					Map recMap = (Map)recList.get(i);
					String termcourse_id = ObjectUtils.toString(recMap.get("TERMCOURSE_ID"));
					String new_termcourse_id = ObjectUtils.toString(recMap.get("NEW_TERMCOURSE_ID"));
					String class_student_id = ObjectUtils.toString(recMap.get("CLASS_STUDENT_ID"));
					String class_id = ObjectUtils.toString(recMap.get("CLASS_ID"));
					String xx_id = ObjectUtils.toString(recMap.get("XX_ID"));
					String student_id = ObjectUtils.toString(recMap.get("STUDENT_ID"));
					String rec_id = ObjectUtils.toString(recMap.get("REC_ID"));
					String term_id = ObjectUtils.toString(recMap.get("TERM_ID"));
					String new_class_id = "";
					
					if (EmptyUtils.isEmpty(termcourse_id) || EmptyUtils.isEmpty(class_id)) {
						
						// 查询需要调班的班级
						List cList = apiOpeanClassDao.getClassInfo(formMap);
						if (EmptyUtils.isNotEmpty(cList)) {
							Map cMap = (Map)cList.get(0);
							new_class_id = ObjectUtils.toString(cMap.get("CLASS_ID"));
						} else {
							// 没有查到需要调整的班级新增班级
							Map classMap = new HashMap();
							int class_count = Integer.parseInt(ObjectUtils.toString(recMap.get("CLASS_COUNT")))+1;
							String bh =  ToolUtil.getRandomNum(8);
							// 新增班级
							String className = ObjectUtils.toString(recMap.get("KCMC"))+ObjectUtils.toString(recMap.get("GRADE_NAME"))+class_count+"班";
							String termId = ObjectUtils.toString(recMap.get("TERM_ID"));
							String courseId = ObjectUtils.toString(recMap.get("COURSE_ID"));
							String xxId = ObjectUtils.toString(recMap.get("XX_ID"));
							String gradeName = ObjectUtils.toString(recMap.get("GRADE_NAME"));
							new_class_id = SequenceUUID.getSequence();
	            			classMap.put("BH", bh);
	        				classMap.put("BJMC", className);
	        				classMap.put("TERM_ID", termId);
	        				classMap.put("CLASS_TYPE", "course");
	        				classMap.put("COURSE_ID", courseId);
	        				classMap.put("XX_ID", xxId);
	        				classMap.put("TERMCOURSE_ID", new_termcourse_id);
	        				classMap.put("AOTU_FLG", "Y");
	        				classMap.put("LIMIT_NUM", "500");
	        				classMap.put("CLASS_ID", new_class_id);
	        				openClassDao.addCourseClass(classMap);
	        				
	        				OpiTermClass termClass = new OpiTermClass();
	                        termClass.setCLASS_ID(new_class_id);
	                        termClass.setTERMCOURSE_ID(new_termcourse_id);
	                        termClass.setCLASS_NAME(className);
	                        termClass.setYEAR(gradeName);
	                        termClass.setCREATED_BY("admin");
	                        termClass.setIS_EXPERIENCE("N");

	                        OpiTermClassData data = new OpiTermClassData(termClass);
	                        RSimpleData rSimpleData = pCourseServer.synchroTermClass(data);
	                        if (EmptyUtils.isNotEmpty(rSimpleData) && rSimpleData.getStatus() == 1) {
	                        	// 标识已同步
	                        	openClassDao.updClassSync(classMap);
	                        }
						}
						
						// 调整课程班级数据
						//1、 删除原有课程班关系数据
						Map tempMap = new HashMap();
						tempMap.put("CLASS_STUDENT_ID", class_student_id);
						apiOpeanClassDao.delClassStudent(tempMap);
						
						//2、 新增学员课程班级数据
						tempMap = new HashMap();
						tempMap.put("CLASS_STUDENT_ID", SequenceUUID.getSequence());
						tempMap.put("CLASS_ID", new_class_id);
						tempMap.put("XX_ID", xx_id);
						tempMap.put("STUDENT_ID", student_id);
						apiOpeanClassDao.addClassStudent(tempMap);
						
						// 调整选课数据
						tempMap = new HashMap();
						String new_rec_id = SequenceUUID.getSequence();
						tempMap.put("REC_ID", rec_id);
						tempMap.put("NEW_REC_ID", new_rec_id);
						tempMap.put("TERM_ID", term_id);
						tempMap.put("TERMCOURSE_ID", new_termcourse_id);
						// 删除原来的选课
						apiOpeanClassDao.delRecInfo(tempMap);
						// 新增选课记录
						apiOpeanClassDao.addRecInfo(tempMap);

						try {
                            // 调班学情数据调整
                            apiOpeanClassDao.delStudyRecord(tempMap);
                            apiOpeanClassDao.addStudyRecord(tempMap);

                            apiOpeanClassDao.delStudySituation(tempMap);
                            apiOpeanClassDao.addStudySituation(tempMap);
                        }catch (Exception e){
						    e.printStackTrace();
                        }

						// 同步选课到学习平台
						OpiStudchoose chooseData = new OpiStudchoose();
						chooseData.setCHOOSE_ID(new_rec_id);
						chooseData.setCLASS_ID(ObjectUtils.toString(new_class_id));
						chooseData.setCREATED_BY("admin");
						chooseData.setSTUD_ID(ObjectUtils.toString(student_id));
						chooseData.setTERMCOURSE_ID(ObjectUtils.toString(new_termcourse_id));
						OpiTermChooseData data = new OpiTermChooseData(chooseData);
						// 同步学习平台选课接口
						RSimpleData rData = pCourseServer.synchroTermChoose(data);
						if (EmptyUtils.isNotEmpty(rData) && rData.getStatus()==1) {
							// 更新同步成功状态
							tempMap = new HashMap();
							tempMap.put("OLD_CHOOSEID", rec_id);
							tempMap.put("NEW_CHOOSEID", new_rec_id);
							
							// pcourse平台调班
							pCourseServer.copyActDynaToNewCourse(tempMap);
						}
					}
				}
			}
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
	/***
	 * 初始化课程班级的辅导老师和督导老师数据
	 * @param
	 * @return
	 */
	public Map initCourseTeacher(Map formMap) {
		Map resultMap = new HashMap();
		int initNum = 0;
		try {
			List teaList = apiOpeanClassDao.getCourseTeacher(formMap);
			for (int i=0; i<teaList.size(); i++) {
				Map teaMap = (Map)teaList.get(i);
				String class_id = ObjectUtils.toString(teaMap.get("CLASS_ID"));
				String termcourse_id = ObjectUtils.toString(teaMap.get("TERMCOURSE_ID"));
				String lms_no = ObjectUtils.toString(teaMap.get("LOGIN_ACCOUNT"));
				String choose_id = SequenceUUID.getSequence();
				String manager_id = ObjectUtils.toString(teaMap.get("EMPLOYEE_ID"));
				
				String app_id = ObjectUtils.toString(teaMap.get("APPID"));
				
				OpiTeacher teach = new OpiTeacher();
				teach.setCREATED_BY("admin");
				teach.setEE_NO(ObjectUtils.toString(teaMap.get("EENO")));
				teach.setEMAIL(ObjectUtils.toString(teaMap.get("DZXX")));
				teach.setIMG_PATH(ObjectUtils.toString(teaMap.get("ZP")));
				teach.setLMS_NO(ObjectUtils.toString(teaMap.get("LOGIN_ACCOUNT")));
				teach.setMANAGER_ID(ObjectUtils.toString(teaMap.get("EMPLOYEE_ID")));
				teach.setMANAGER_ROLE("teacher");
				teach.setMOBILE_NO(ObjectUtils.toString(teaMap.get("SJH")));
				teach.setREALNAME(ObjectUtils.toString(teaMap.get("XM")));
				teach.setSEX(ObjectUtils.toString(teaMap.get("XBM")));
				OpiTeacherData opiTeacherData = new OpiTeacherData(app_id, teach);
				pCourseServer.synchroTeacher(opiTeacherData);
				
				OpiTchchoose tchchoose = new OpiTchchoose();
				tchchoose.setCLASS_ID(class_id);
				tchchoose.setCREATED_BY("admin");
				tchchoose.setFROM_JIJIAO("N");
				tchchoose.setIS_INVISIBILITY("N");
				tchchoose.setLMS_NO(lms_no);
				tchchoose.setMANAGER_CHOOSE_ID(choose_id);
				tchchoose.setMANAGER_ID(manager_id);
				tchchoose.setMANAGER_ROLE("teacher");
				tchchoose.setTERMCOURSE_ID(termcourse_id);
				OpiTermChooseData data = new OpiTermChooseData(null, tchchoose);
				pCourseServer.synchroTermChoose(data);
				initNum++;
			}
			
			List inspectorList = apiOpeanClassDao.getCourseInspector(formMap);
			for (int i=0; i<inspectorList.size(); i++) {
				Map teaMap = (Map)inspectorList.get(i);
				String class_id = ObjectUtils.toString(teaMap.get("CLASS_ID"));
				String termcourse_id = ObjectUtils.toString(teaMap.get("TERMCOURSE_ID"));
				String lms_no = ObjectUtils.toString(teaMap.get("LOGIN_ACCOUNT"));
				String choose_id = SequenceUUID.getSequence();
				String manager_id = ObjectUtils.toString(teaMap.get("EMPLOYEE_ID"));
				
				String app_id = ObjectUtils.toString(teaMap.get("APPID"));
				
				OpiTeacher teach = new OpiTeacher();
				teach.setCREATED_BY("admin");
				teach.setEE_NO(ObjectUtils.toString(teaMap.get("EENO")));
				teach.setEMAIL(ObjectUtils.toString(teaMap.get("DZXX")));
				teach.setIMG_PATH(ObjectUtils.toString(teaMap.get("ZP")));
				teach.setLMS_NO(ObjectUtils.toString(teaMap.get("LOGIN_ACCOUNT")));
				teach.setMANAGER_ID(ObjectUtils.toString(teaMap.get("EMPLOYEE_ID")));
				teach.setMANAGER_ROLE("inspector");
				teach.setMOBILE_NO(ObjectUtils.toString(teaMap.get("SJH")));
				teach.setREALNAME(ObjectUtils.toString(teaMap.get("XM")));
				teach.setSEX(ObjectUtils.toString(teaMap.get("XBM")));
				OpiTeacherData opiTeacherData = new OpiTeacherData(app_id, teach);
				pCourseServer.synchroTeacher(opiTeacherData);
				
				OpiTchchoose tchchoose = new OpiTchchoose();
				tchchoose.setCLASS_ID(class_id);
				tchchoose.setCREATED_BY("admin");
				tchchoose.setFROM_JIJIAO("N");
				tchchoose.setIS_INVISIBILITY("N");
				tchchoose.setLMS_NO(lms_no);
				tchchoose.setMANAGER_CHOOSE_ID(choose_id);
				tchchoose.setMANAGER_ID(manager_id);
				tchchoose.setMANAGER_ROLE("inspector");
				tchchoose.setTERMCOURSE_ID(termcourse_id);
				OpiTermChooseData data = new OpiTermChooseData(null, tchchoose);
				pCourseServer.synchroTermChoose(data);
				initNum++;
			}
			
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
	/***
	 * 检查同步期课程班级是否同步到学习平台
	 * @param
	 * @return
	 */
	public Map syncTermClass(Map formMap) {
		Map resultMap = new HashMap();
		int initNum = 0;
		try {
			List termCourseList = apiOpeanClassDao.getTermCourseNoPcourse(formMap);
			if (EmptyUtils.isNotEmpty(termCourseList)) {
				for (int i=0; i<termCourseList.size(); i++) {
					Map termCourseMap = (Map)termCourseList.get(i);
					String termcourse_id = ObjectUtils.toString(termCourseMap.get("TERMCOURSE_ID"));
					String term_id = ObjectUtils.toString(termCourseMap.get("TERM_ID"));
					String xx_id = ObjectUtils.toString(termCourseMap.get("XX_ID"));
					String course_id = ObjectUtils.toString(termCourseMap.get("COURSE_ID"));
					String pcourse_termcourse_id = ObjectUtils.toString(termCourseMap.get("PCOURSE_TERMCOURSE_ID"));
					if (EmptyUtils.isEmpty(pcourse_termcourse_id)) {
						GjtGrade termInfo = gjtGradeDao.findOne(term_id);
						GjtCourse courseInfo = gjtCourseDao.findOne(course_id);
						GjtSchoolInfo schoolInfo = gjtSchoolInfoDao.findOne(xx_id);
						String appId = ObjectUtils.toString(schoolInfo.getAppid());
						OpiTerm opiTerm = new OpiTerm();
                        opiTerm.setCREATED_BY("admin");
                        opiTerm.setTERM_CODE(String.valueOf(termInfo.getGradeCode()));
                        opiTerm.setTERM_END_DT(DateUtils.getTimeYMD(termInfo.getCourseEndDate()));
                        opiTerm.setTERM_START_DT(DateUtils.getTimeYMD(termInfo.getCourseStartDate()));
                        opiTerm.setTERM_NAME(termInfo.getGradeName());
                        opiTerm.setTERM_ID(termInfo.getGradeId());

                        OpiTermCourse opiTermCourse = new OpiTermCourse();
                        opiTermCourse.setTERMCOURSE_ID(ObjectUtils.toString(termcourse_id));
                        opiTermCourse.setTERM_ID(ObjectUtils.toString(term_id));
                        opiTermCourse.setCOURSE_ID(course_id);
                        opiTermCourse.setCREATED_BY("admin");
                        opiTermCourse.setTERMCOURSE_START_DT(DateUtils.getTimeYMD(termInfo.getCourseStartDate()));
                        opiTermCourse.setTERMCOURSE_END_DT(DateUtils.getTimeYMD(termInfo.getCourseEndDate()));

                        OpiTermCourseData data = new OpiTermCourseData(appId, opiTerm, opiTermCourse);
                        RSimpleData rSimpleData = pCourseServer.synchroTermCourse(data);
                        if (EmptyUtils.isNotEmpty(rSimpleData) && rSimpleData.getStatus() == 1) {
                        	// 同步成功
                        }
					}
				}
			}
			
			List classList = apiOpeanClassDao.getCourseClassNoPcourse(formMap);
			if (EmptyUtils.isNotEmpty(classList)) {
				for (int i=0; i<classList.size(); i++) {
					Map classMap = (Map)classList.get(i);
					String pcourse_class_id = ObjectUtils.toString(classMap.get("PCOURSE_CLASS_ID"));
					String class_id = ObjectUtils.toString(classMap.get("CLASS_ID"));
					String termcourse_id = ObjectUtils.toString(classMap.get("TERMCOURSE_ID"));
					String bjmc = ObjectUtils.toString(classMap.get("BJMC"));
					String grade_name = ObjectUtils.toString(classMap.get("GRADE_NAME"));
					if (EmptyUtils.isEmpty(pcourse_class_id)) {
						OpiTermClass termClass = new OpiTermClass();
	                    termClass.setCLASS_ID(class_id);
	                    termClass.setTERMCOURSE_ID(termcourse_id);
	                    termClass.setCLASS_NAME(bjmc);
	                    termClass.setYEAR(grade_name);
	                    termClass.setCREATED_BY("admin");
	                    termClass.setIS_EXPERIENCE("N");

	                    OpiTermClassData data = new OpiTermClassData(termClass);
	                    RSimpleData rSimpleData = pCourseServer.synchroTermClass(data);
	                    if (EmptyUtils.isNotEmpty(rSimpleData) && rSimpleData.getStatus() == 1) {
	                    	// 同步成功
	                    }
					}
				}
			}
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
	/***
	 * 教学计划保存的时候初始化学员的选课信息
	 * @param
	 * @return
	 */
	public Map initPlanStuRec(Map formMap) {
		Map resultMap = new HashMap();
		int initNum = 0;
		try {
			List stuList = apiOpeanClassDao.getPlanStudentList(formMap);
			for (int i=0; i<stuList.size(); i++) {
				Map tempMap = (Map)stuList.get(i);
				initStudentChoose(tempMap);
				initNum++;
			}
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
	/***
	 * 开课完成初始化学员的选课（点击开课流程的时候调用）
	 * @param
	 * @return
	 */
	public Map initTermcourseStuRec(Map formMap) {
		Map resultMap = new HashMap();
		int initNum = 0;
		try {
			List stuList = apiOpeanClassDao.getTermcourseStuRec(formMap);
			for (int i=0; i<stuList.size(); i++) {
				Map tempMap = (Map)stuList.get(i);
				initStudentChoose(tempMap);
				initNum++;
			}
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
	/***
	 * 初始化课程数据
	 * @param
	 * @return
	 */
	public Map initCourseInfo(Map formMap) {
		Map resultMap = new HashMap();
		int initNum = 0;
		try {
			List courseList = apiOpeanClassDao.getCourseInfo(formMap);
			for (int i=0; i<courseList.size(); i++) {
				Map tempMap = (Map)courseList.get(i);
				String appId = ObjectUtils.toString(tempMap.get("APPID"));
				String courseId = ObjectUtils.toString(tempMap.get("COURSE_ID"));
				
				OpiCourse obj = new OpiCourse();
				obj.setCOURSE_ID(ObjectUtils.toString(tempMap.get("COURSE_ID")));
				obj.setCOURSE_NAME(ObjectUtils.toString(tempMap.get("KCMC")));
				obj.setCOURSE_STATUS("active");
				obj.setCREATED_BY(ObjectUtils.toString(tempMap.get("CREATED_BY")));
				obj.setPUBLISH_DT(ObjectUtils.toString(tempMap.get("CREATED_DT")));
				
				if (EmptyUtils.isEmpty(ObjectUtils.toString(ObjectUtils.toString(tempMap.get("KCH"))))) {
					obj.setCOURSE_CODE("--");
		    	} else {
		    		obj.setCOURSE_CODE(ObjectUtils.toString(ObjectUtils.toString(tempMap.get("KCH"))));
		    	}
				obj.setCOURSE_DES(ObjectUtils.toString(ObjectUtils.toString(tempMap.get("KCJJ"))));
				obj.setEDUCATION_LEVEL(ObjectUtils.toString(ObjectUtils.toString(tempMap.get("PYCC_NAME"))));
				obj.setLABEL(ObjectUtils.toString(ObjectUtils.toString(tempMap.get("LABEL"))));
				String zymc = ObjectUtils.toString(ObjectUtils.toString(tempMap.get("ZYMC")));
				
				obj.setPROFESSION(zymc);
				
				OpiCourseData data = new OpiCourseData(appId, obj);
				RSimpleData synchroCourse = pCourseServer.synchroCourse(data);
				
				initNum++;
			}
			resultMap.put("initNum", initNum);
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
	/***
	 * 定时调用
	 * 1、选课数据不足的补齐
	 * 2、删除多余的选课信息
	 * 3、删除开课为0、班级为0 的数据
	 * 4、学习平台的选课数据保持一致
	 */
	public Map initStudentRec(Map formMap) {
		Map resultMap = new HashMap();
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
