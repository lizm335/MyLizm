package com.ouchgzee.study.serviceImpl.exam;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.ZipFileUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JSONObject;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamAppointmentNewRepository;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointAppointmentNewRepository;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointNewRepository;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.remote.BillRemoteNewService;
import com.gzedu.xlims.service.remote.OrderBillInfo;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.ouchgzee.study.dao.course.CourseLearningDao;
import com.ouchgzee.study.dao.exam.ExamNewDaoImpl;
import com.ouchgzee.study.dao.exam.ExamServeDao;
import com.ouchgzee.study.service.exam.ExamServeService;
import com.ouchgzee.study.serviceImpl.course.CourseRemoteService;


@Service
public class ExamServeServiceImpl implements ExamServeService {

    private static final Log log = LogFactory.getLog(ExamServeServiceImpl.class);

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private ExamServeDao examServeDao;

    @Autowired
    private ExamNewDaoImpl examNewDaoImpl;

    @Autowired
    private GjtExamAppointmentNewDao gjtExamAppointmentNewDao;

    @Autowired
    private GjtExamAppointmentNewRepository gjtExamAppointmentNewRepository;

    @Autowired
    private GjtExamPointNewRepository gjtExamPointNewRepository;

    @Autowired
    private GjtExamPointAppointmentNewRepository gjtExamPointAppointmentNewRepository;

    @Autowired
    private GjtStudentInfoDao gjtStudentInfoDao;

    @Resource
    private CourseRemoteService courseRemoteService;

    @Autowired
    private CourseLearningDao courseLearningDao;

    @Autowired
    private GjtExamCostService gjtExamCostService;

    @Autowired
    private GjtRecResultService gjtRecResultService;

    @Autowired
    private GjtExamPlanNewService gjtExamPlanNewService;

    @Autowired
    private GjtCourseService gjtCourseService;
    
    @Autowired
    private GjtSignupService gjtSignupService;

    @Autowired
    private BillRemoteNewService billRemoteNewService;

    @Autowired
    private CacheService cacheService;

    @Value("#{configProperties['pcenterStudyServer']}")
    private String pcenterStudyServer;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map examAttention(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        try {
            List list = examServeDao.examAttention(searchParams);
            if (EmptyUtils.isNotEmpty(list)) {
                resultMap = (Map) list.get(0);
                if (EmptyUtils.isNotEmpty(resultMap.get("CONTENT"))) {
                    String reString = "";
                    Reader is = ((Clob) resultMap.get("CONTENT")).getCharacterStream();
                    BufferedReader br = new BufferedReader(is);
                    String s = br.readLine();
                    StringBuffer sBuffer = new StringBuffer();
                    while (s != null) {
                        sBuffer.append(s);
                        s = br.readLine();
                    }
                    reString = sBuffer.toString();
                    resultMap.put("CONTENT", reString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map examAdmissionInfo(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try {

            //机考准考证查询临时使用该代码
            /*
            if("11".equals(ObjectUtils.toString(searchParams.get("TYPE")))){
                List indexList = courseLearningDao.getTermListIndex(searchParams);
                if(EmptyUtils.isNotEmpty(indexList)){
                    Map map = (Map) indexList.get(0);
                    resultMap.put("STUDY_YEAR_NAME",ObjectUtils.toString(map.get("GRADE_NAME")));
                }

                List admiList = examServeDao.getJKAdmissionInfo(searchParams);
                if(EmptyUtils.isNotEmpty(admiList)){
                    Map adminMap = (Map)admiList.get(0);
                    resultMap.put("ADMISSION_NAME",ObjectUtils.toString(adminMap.get("ADMISSION_NAME")));
                    resultMap.put("STU_NUMBER",ObjectUtils.toString(adminMap.get("STU_NUMBER")));
                    resultMap.put("STU_PHOTO",ObjectUtils.toString(adminMap.get("STU_PHOTO")));
                    resultMap.put("EXAM_POINT_NAME",ObjectUtils.toString(adminMap.get("EXAM_POINT_NAME")));
                    resultMap.put("EXAM_ADDRESS",ObjectUtils.toString(adminMap.get("EXAM_ADDRESS")));
                    resultMap.put("XXMC",ObjectUtils.toString(adminMap.get("XXMC")));
                }

                List admiList2 = examServeDao.getJKAdmissionInfo(searchParams);
                if(EmptyUtils.isNotEmpty(admiList2)){
                    resultMap.put("ADMISSLIST", admiList2);
                }
            }else{
                List list = examServeDao.admissionByUserInfo(searchParams);
                if (EmptyUtils.isNotEmpty(list)) {
                    resultMap = (Map) list.get(0);
                }
                List examList = examServeDao.admissionByExamInfo(searchParams);
                if (EmptyUtils.isNotEmpty(examList)) {
                    resultMap.put("ADMISSLIST", examList);
                }
            }
            */


            List list = examServeDao.admissionByUserInfo(searchParams);
            if (EmptyUtils.isNotEmpty(list)) {
                resultMap = (Map) list.get(0);
            }
            List examList = examServeDao.admissionByExamInfo(searchParams);
            if (EmptyUtils.isNotEmpty(examList)) {
                resultMap.put("ADMISSLIST", examList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    @Override
    public List<Map> getAppointExamPointByStudent(Map<String, Object> searchParams) {
        return examServeDao.getAppointExamPointByStudent(searchParams);
    }

    @SuppressWarnings("rawtypes")
    public Map queryPointInfo(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();

        try {
            List pointList = examServeDao.queryPointInfo(searchParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;

    }

    @SuppressWarnings("rawtypes")
    @Override
    public List queryExamPoint(Map<String, Object> searchParams) {
        return examServeDao.queryPointInfo(searchParams);
    }

    /**
     * 保存预约考点信息
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public boolean saveExamPointApp(Map<String, Object> searchParams) {
        String examBatchCode = (String) searchParams.get("EXAM_BATCH_CODE");
        String studentId = (String) searchParams.get("STUDENT_ID");
        String examPointId = (String) searchParams.get("EXAM_POINT_ID");
        GjtExamPointNew point = gjtExamPointNewRepository.findOne(examPointId);
        if(point != null) {
            // 预约的考点需要在考试计划范围内
            if(org.apache.commons.lang3.StringUtils.equals(point.getExamBatchCode(), examBatchCode)) {
                // 预约考点之前删除原来预约考点数据
                gjtExamAppointmentNewDao.deletePointExamAppointment(examBatchCode, studentId, examPointId);

                // 添加预约
                GjtExamPointAppointmentNew examPointAppointmentNew = new GjtExamPointAppointmentNew();
                examPointAppointmentNew.setId(UUIDUtils.random());
                examPointAppointmentNew.setStudentId(studentId);
                examPointAppointmentNew.setExamBatchCode(examBatchCode);
                examPointAppointmentNew.setExamPointId(examPointId);
                examPointAppointmentNew.setCreatedBy(studentId);
                examPointAppointmentNew.setCreatedDt(new Date());
                gjtExamPointAppointmentNewRepository.save(examPointAppointmentNew);
                return true;
            }
        }
        return false;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map queryAppointmentExam(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try{
            String examBatchCode = ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"), "").trim();
            if (EmptyUtils.isEmpty(examBatchCode)) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "参数不能为空！");
                return resultMap;
            }
            String studentId = ObjectUtils.toString(searchParams.get("STUDENT_ID"), "").trim();
            if (EmptyUtils.isEmpty(studentId)) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "studentId参数不能为空！");
                return resultMap;
            }

            String userType = ObjectUtils.toString(searchParams.get("USER_TYPE"),"");
            String xjzt = ObjectUtils.toString(searchParams.get("XJZT"),"");
            List userTypeList = examServeDao.getStuXjZtAndUserType(searchParams);
            if(EmptyUtils.isNotEmpty(userTypeList)){
                Map typeMap = (Map) userTypeList.get(0);
                userType = ObjectUtils.toString(typeMap.get("USER_TYPE"));
                xjzt = ObjectUtils.toString(typeMap.get("XJZT"));
            }

            searchParams.put("USER_TYPE",userType);
            searchParams.put("XJZT",xjzt);


            String end_date = null;
            List endTerm = examServeDao.getEndTermByExamBatch(searchParams);
            if(EmptyUtils.isNotEmpty(endTerm)){
                Map dateMap = (Map)endTerm.get(0);
                end_date = ObjectUtils.toString(dateMap.get("END_DATE"));
            }

            List currentFlag = examServeDao.getCurrentExamBathcFlag(searchParams);
            if(EmptyUtils.isNotEmpty(currentFlag)){
                Map currentMap = (Map) currentFlag.get(0);
                resultMap.put("TIME_FLAG", ObjectUtils.toString(currentMap.get("TIME_FLAG")));
            }else{
                resultMap.put("TIME_FLAG", "--");
            }

            if (!"1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))){
                //待预约考点个数
                List pointList = examServeDao.getAppointExamPointByStudent(searchParams);
                if(EmptyUtils.isNotEmpty(pointList)){
                    resultMap.put("PENDING_APPOINTMENT", "0");
                }else{
                    resultMap.put("PENDING_APPOINTMENT", "1");
                }
            }

            //===========待预约考试统计开始==============//
            searchParams.put("END_DATE",end_date);
            List<Map> resultList = new ArrayList<Map>();
            List recResultList = examServeDao.getChooseCourseByStudent(searchParams);
    		Map<String, String> signupCopyDataTemp = gjtSignupService.getSignupCopyData(ObjectUtils.toString(searchParams.get("STUDENT_ID")));
            
            for(int i=0;i<recResultList.size();i++){
                Map temp1 = (Map) recResultList.get(i);

                Map courseMap = new HashMap();

                courseMap.put("APPOINTMENT_FLAG","1"); //待预约列表
                courseMap.put("XX_ID",searchParams.get("XX_ID"));
                courseMap.put("EXAM_BATCH_CODE",searchParams.get("EXAM_BATCH_CODE"));
                courseMap.put("USER_TYPE",searchParams.get("USER_TYPE"));
                courseMap.put("XJZT",searchParams.get("XJZT"));
                courseMap.put("SCHOOL_MODEL",searchParams.get("SCHOOL_MODEL"));
                courseMap.put("SPECIALTY_ID",ObjectUtils.toString(searchParams.get("KKZY")));
        		// 2018春新规 按照教学计划的试卷号查询
//                    courseMap.put("COURSE_ID",ObjectUtils.toString(temp1.get("COURSE_ID")));
                courseMap.put("EXAM_NO", ObjectUtils.toString(temp1.get("EXAM_NO")));
                courseMap.put("SCHOOL_MODEL",ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")));
                courseMap.put("condition", searchParams.get("condition")); // 开通特殊渠道导出学员预约数据

                if(StringUtils.isNotBlank(ObjectUtils.toString(temp1.get("EXAM_NO")))) {
                    // 根据现在的课程找到对应的开考科目
                    List courseListInfo = examServeDao.getExamPlanAndCourseInfo(courseMap);
                    if(EmptyUtils.isNotEmpty(courseListInfo) && courseListInfo.size() > 0){
                        Map temp2 = (Map)courseListInfo.get(0);

                        Map temp3 = new LinkedHashMap();
                        temp3.putAll(temp1);
                        temp3.put("COURSE_CODE", ObjectUtils.toString(temp1.get("KCH")));
                        temp3.put("URL_NEW", signupCopyDataTemp.get("zp"));

                        temp3.put("EXAM_TYPE", ObjectUtils.toString(temp2.get("EXAM_TYPE")));
                        temp3.put("EXAM_STYLE", ObjectUtils.toString(temp2.get("EXAM_STYLE")));
                        temp3.put("KSFS_FLAG", ObjectUtils.toString(temp2.get("TYPE")));
                        temp3.put("EXAM_PLAN_ID", ObjectUtils.toString(temp2.get("EXAM_PLAN_ID")));
                        temp3.put("EXAM_PLAN_LIMIT", ObjectUtils.toString(temp2.get("EXAM_PLAN_LIMIT")));
                        temp3.put("EXAM_STIME", ObjectUtils.toString(temp2.get("EXAM_STIME")));
                        temp3.put("EXAM_ETIME", ObjectUtils.toString(temp2.get("EXAM_ETIME")));
                        String bookStartTime = ObjectUtils.toString(temp2.get("BOOK_STARTTIME"));
                        String bookEndTime = ObjectUtils.toString(temp2.get("BOOK_ENDTIME"));
                        String booksStartTime = ObjectUtils.toString(temp2.get("BOOKS_STARTTIME"));
                        String booksEndTime = ObjectUtils.toString(temp2.get("BOOKS_ENDTIME"));
                        if(StringUtils.isNotBlank(booksStartTime) && new Date().after(DateUtil.parseDate(booksStartTime))) {  // 如果当前时间在某时间之后
                            temp3.put("BOOK_STARTTIME", booksStartTime);
                            temp3.put("BOOK_ENDTIME", booksEndTime);
                        } else {
                            temp3.put("BOOK_STARTTIME", bookStartTime);
                            temp3.put("BOOK_ENDTIME", bookEndTime);
                        }
                        temp3.put("RECORD_END", ObjectUtils.toString(temp2.get("RECORD_END")));
                        temp3.put("EXAM_BATCH_CODE",ObjectUtils.toString(temp2.get("EXAM_BATCH_CODE")));
                        BigDecimal xkPercentBd = (BigDecimal) temp2.get("XK_PERCENT");
                        int xkPercent = xkPercentBd != null ? xkPercentBd.intValue() : 0;
                        temp3.put("EXAM_PLAN_XK_PERCENT", xkPercent);
                        temp3.put("EXAM_PLAN_ZJ_PERCENT", 100 - xkPercent);

                        resultList.add(temp3);
                    }

//                    List<Object> myAppointmentList = new ArrayList<Object>();
//                    if(EmptyUtils.isNotEmpty(resultList)){
//                        for(int i=0;i<resultList.size();i++){
//                            Map tempPlan = (Map) resultList.get(i);
//                            Map viewMap = new HashMap();
//                            viewMap.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
//                            viewMap.put("COURSE_ID",ObjectUtils.toString(tempPlan.get("COURSE_ID")));
//                            viewMap.put("TYPE",ObjectUtils.toString(tempPlan.get("KSFS_FLAG")));
//                            List viewList= examServeDao.getViewExamPlanByAcadeMy(viewMap);
//                            if(EmptyUtils.isNotEmpty(viewList)){
//                                for (int j = 0;j<viewList.size();j++){
//                                    Map viewScMap = (Map)viewList.get(j);
//                                    if(ObjectUtils.toString(searchParams.get("KKZY")).equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
//                                            || "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))){
//                                        BigDecimal xkPercentBd = (BigDecimal) viewScMap.get("XK_PERCENT");
//                                        int xkPercent = xkPercentBd != null ? xkPercentBd.intValue() : 0;
//                                        tempPlan.put("EXAM_PLAN_XK_PERCENT", xkPercent);
//                                        tempPlan.put("EXAM_PLAN_ZJ_PERCENT", 100 - xkPercent);
//                                        myAppointmentList.add(tempPlan);
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }
            
            //待预约列表为空时,表示学员无可预约的课程，则考点也不可预约，PENDING_APPOINTMENT置为0
            if(EmptyUtils.isEmpty(resultList)){
                resultMap.put("PENDING_APPOINTMENT", "0");
            }

            int pending_count = 0;
            int already_count = 0;
            for (int i=0;i<resultList.size();i++){
                Map tMap = resultList.get(i);
                if("0".equals(ObjectUtils.toString(tMap.get("BESPEAK_STATE")))){
                    pending_count = pending_count+1;
                }else{
                    already_count = already_count+1;
                }
            }
            //待预约考试科目
            resultMap.put("PENDING_COUNT", ObjectUtils.toString(pending_count));
            //已预约考试科目
            resultMap.put("ALREADY_COUNT", ObjectUtils.toString(already_count));
            //===========待预约考试统计结束==============//

            //===========我的考试统计开始==============//
            int dks_count = 0;
            int exam_count = 0;
            int yks_count = 0;

            Map myExamList = myExamDataList(searchParams);
            List<Map> termList = (List<Map>) myExamList.get("LIST");
            for (Map term : termList) {
                List<Map> examList = (List<Map>) term.get("EXAMLIST");
                for (Map e : examList) {
                    String examState = (String) e.get("EXAM_STATE");
                    if("0".equals(examState) || "--".equals(examState)){
                        dks_count = dks_count+1;
                    }else if("3".equals(examState)){
                        exam_count =exam_count+1;
                    }else{
                        yks_count =yks_count+1;
                    }
                }
            }

            resultMap.put("DKS_COUNT",ObjectUtils.toString(dks_count));  //待考试
            resultMap.put("EXAM_COUNT",ObjectUtils.toString(exam_count)); //考试中
            resultMap.put("YKS_COUNT",ObjectUtils.toString(yks_count)); //已结束
            //===========我的考试统计结束==============//

            if (!"1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))){
                //查询当前期
                List gradeList = examServeDao.getCurrentTerm(searchParams);
                String term_name = null;
                String term_id = null;
                if(EmptyUtils.isNotEmpty(gradeList)){
                    Map gradeMap = (Map) gradeList.get(0);
                    term_id = ObjectUtils.toString(gradeMap.get("GRADE_ID"));
                    term_name = ObjectUtils.toString(gradeMap.get("GRADE_NAME"));
                }

                //可预约时间配置
                List time = examServeDao.appointmentTimes(searchParams);
                Map timeMap = null;
                if(EmptyUtils.isNotEmpty(time)){
                    timeMap = (Map) time.get(0);
                    String bookStartTime = ObjectUtils.toString(timeMap.get("BOOK_ST"));
                    String bookEndTime = ObjectUtils.toString(timeMap.get("BOOK_END"));
                    String booksStartTime = ObjectUtils.toString(timeMap.get("BOOKS_ST"));
                    String booksEndTime = ObjectUtils.toString(timeMap.get("BOOKS_END"));
                    if(StringUtils.isNotBlank(booksStartTime) && new Date().after(DateUtil.parseDate(booksStartTime))) {  // 如果当前时间在某时间之后
                        timeMap.put("BOOK_ST", booksStartTime);
                        timeMap.put("BOOK_END", booksEndTime);
                    } else {
                        timeMap.put("BOOK_ST", bookStartTime);
                        timeMap.put("BOOK_END", bookEndTime);
                    }
                }

                List bookPointList = examServeDao.getAppointExamPointByStudent(searchParams);
                List<Object> bookList = new ArrayList<Object>();
                
                Map pointMap2 = new LinkedHashMap();
                if(EmptyUtils.isNotEmpty(bookPointList)){
                	if(bookPointList.size()==1){
                		Map tPointMap = (Map) bookPointList.get(0); 
                		Map pointMap = new LinkedHashMap();
                        pointMap.put("EXAM_POINT_ID", ObjectUtils.toString(tPointMap.get("EXAM_POINT_ID")));
                        pointMap.put("POINT_NAME", ObjectUtils.toString(tPointMap.get("POINT_NAME")));
                        pointMap.put("AREA_NAME", ObjectUtils.toString(tPointMap.get("AREA_NAME")));
                        pointMap.put("ADDRESS", ObjectUtils.toString(tPointMap.get("ADDRESS")));                     
                        pointMap.put("EXAM_POINT", ObjectUtils.toString(tPointMap.get("EXAM_POINT")));
                        pointMap.put("SDATE", ObjectUtils.toString(timeMap.get("BOOK_ST")));
                        pointMap.put("EDATE", ObjectUtils.toString(timeMap.get("BOOK_END")));
                        pointMap.put("TERM_ID", term_id);
                        pointMap.put("TERM_NAME", term_name);
                        pointMap.put("EXAM_TYPE", ObjectUtils.toString(tPointMap.get("EXAM_TYPE")));
                        bookList.add(pointMap);
                        //因为学习空间预约考点列表固定"机考"和"笔考"两种形式，所以都要返回2条数据
                        if("8".equals(ObjectUtils.toString(tPointMap.get("EXAM_TYPE")))){
                        	pointMap2.put("EXAM_POINT_ID", null);
                        	pointMap2.put("POINT_NAME", "--");
                        	pointMap2.put("AREA_NAME","--");
                        	pointMap2.put("ADDRESS", "--");
                        	pointMap2.put("EXAM_POINT", "");
                        	pointMap2.put("SDATE", ObjectUtils.toString(timeMap.get("BOOK_ST")));
                        	pointMap2.put("EDATE", ObjectUtils.toString(timeMap.get("BOOK_END")));
                        	pointMap2.put("TERM_ID", term_id);
                        	pointMap2.put("TERM_NAME", term_name);
                        	pointMap2.put("EXAM_TYPE", "11");//机考
                        	bookList.add(pointMap2);
                        }
                        if("11".equals(ObjectUtils.toString(tPointMap.get("EXAM_TYPE")))){
                        	pointMap2.put("EXAM_POINT_ID", null);
                        	pointMap2.put("POINT_NAME", "--");
                        	pointMap2.put("AREA_NAME","--");
                        	pointMap2.put("ADDRESS", "--");
                        	pointMap2.put("EXAM_POINT", "");
                        	pointMap2.put("SDATE", ObjectUtils.toString(timeMap.get("BOOK_ST")));
                        	pointMap2.put("EDATE", ObjectUtils.toString(timeMap.get("BOOK_END")));
                        	pointMap2.put("TERM_ID", term_id);
                        	pointMap2.put("TERM_NAME", term_name);
                        	pointMap2.put("EXAM_TYPE", "8");//笔试
                        	bookList.add(pointMap2);
                        }                       
                        resultMap.put("POINTLIST", bookList);
                	}
                	if(bookPointList.size()==2){
                		for(int i=0;i<bookPointList.size();i++){
                			 Map tPointMap = (Map) bookPointList.get(i); 
                			 Map pointMapMap = new LinkedHashMap();
                			 pointMapMap.put("EXAM_POINT_ID", ObjectUtils.toString(tPointMap.get("EXAM_POINT_ID")));
                			 pointMapMap.put("POINT_NAME", ObjectUtils.toString(tPointMap.get("POINT_NAME")));
                			 pointMapMap.put("AREA_NAME", ObjectUtils.toString(tPointMap.get("AREA_NAME")));
                			 pointMapMap.put("ADDRESS", ObjectUtils.toString(tPointMap.get("ADDRESS")));                     
                			 pointMapMap.put("EXAM_POINT", ObjectUtils.toString(tPointMap.get("EXAM_POINT")));
                			 pointMapMap.put("SDATE", ObjectUtils.toString(timeMap.get("BOOK_ST")));
                			 pointMapMap.put("EDATE", ObjectUtils.toString(timeMap.get("BOOK_END")));
                			 pointMapMap.put("TERM_ID", term_id);
                			 pointMapMap.put("TERM_NAME", term_name);
                			 pointMapMap.put("EXAM_TYPE", ObjectUtils.toString(tPointMap.get("EXAM_TYPE")));
                             bookList.add(pointMapMap);
                		}
                		resultMap.put("POINTLIST", bookList);
                	}
                }else{
                    Map tPointMap1 = new LinkedHashMap();
                    Map tPointMap2 = new LinkedHashMap();
                    tPointMap1.put("EXAM_POINT_ID", null);
                    tPointMap1.put("POINT_NAME", "--");
                    tPointMap1.put("AREA_NAME","--");
                    tPointMap1.put("ADDRESS", "--");
                    tPointMap1.put("EXAM_POINT", "");
                    tPointMap1.put("SDATE", ObjectUtils.toString(timeMap.get("BOOK_ST")));
                    tPointMap1.put("EDATE", ObjectUtils.toString(timeMap.get("BOOK_END")));
                    tPointMap1.put("TERM_ID", term_id);
                    tPointMap1.put("TERM_NAME", term_name);
                    tPointMap1.put("EXAM_TYPE", "8");//笔试
                    
                    tPointMap2.put("EXAM_POINT_ID", null);
                    tPointMap2.put("POINT_NAME", "--");
                    tPointMap2.put("AREA_NAME","--");
                    tPointMap2.put("ADDRESS", "--");
                    tPointMap2.put("EXAM_POINT", "");
                    tPointMap2.put("SDATE", ObjectUtils.toString(timeMap.get("BOOK_ST")));
                    tPointMap2.put("EDATE", ObjectUtils.toString(timeMap.get("BOOK_END")));
                    tPointMap2.put("TERM_ID", term_id);
                    tPointMap2.put("TERM_NAME", term_name);
                    tPointMap2.put("EXAM_TYPE", "11");//机考
                    bookList.add(tPointMap1);
                    bookList.add(tPointMap2);
                    resultMap.put("POINTLIST", bookList);
                }
            }

            //===========待预约考试列表开始==============//
            Map<String, Object> tempMap = new LinkedMap();
            for(int i=0;i<resultList.size();i++){
                Map temp = resultList.get(i);
                tempMap.put(ObjectUtils.toString(temp.get("TERM_ID")), ObjectUtils.toString(temp.get("TERM_ID")));
            }

            List<Object> list = new ArrayList<Object>();
            for(Object key:tempMap.keySet()){
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                List<Object> course_list = new ArrayList<Object>();
                for(int i=0;i<resultList.size();i++){

                    Map temp = resultList.get(i);
                    if(ObjectUtils.toString(key).equals(ObjectUtils.toString(temp.get("TERM_ID")))){
                        Map<String, Object> course_map = new LinkedHashMap<String, Object>();
                        map.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
                        map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));

                        course_map.put("COURSE_ID", ObjectUtils.toString(temp.get("COURSE_ID")));
                        course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
                        course_map.put("NEW_EXAM_SCORE", ObjectUtils.toString(temp.get("NEW_EXAM_SCORE")));
                        course_map.put("NEW_EXAM_SCORE1", ObjectUtils.toString(temp.get("NEW_EXAM_SCORE1")));
                        course_map.put("NEW_EXAM_SCORE2", ObjectUtils.toString(temp.get("NEW_EXAM_SCORE2")));
                        course_map.put("EXAM_STATE",ObjectUtils.toString(temp.get("EXAM_STATE"))); //状态0:未通过;1:已通过;2:学习中;3:登记中
                        course_map.put("KCH", ObjectUtils.toString(temp.get("KCH")));
                        course_map.put("COURSE_COST", ObjectUtils.toString(temp.get("COURSE_COST")));
                        course_map.put("KCXXBZ", temp.get("KCXXBZ"));
                        course_map.put("KCKSBZ", temp.get("KCKSBZ"));
                        course_map.put("EXAM_PLAN_XK_PERCENT", temp.get("EXAM_PLAN_XK_PERCENT"));
                        course_map.put("EXAM_PLAN_ZJ_PERCENT", temp.get("EXAM_PLAN_ZJ_PERCENT"));
                        course_map.put("EXAM_TYPE", ObjectUtils.toString(temp.get("EXAM_TYPE")));
                        course_map.put("EXAM_STYLE", ObjectUtils.toString(temp.get("EXAM_STYLE")));
                        course_map.put("KSFS_FLAG", ObjectUtils.toString(temp.get("KSFS_FLAG")));
                        course_map.put("EXAM_PLAN_ID", ObjectUtils.toString(temp.get("EXAM_PLAN_ID")));
                        course_map.put("EXAM_PLAN_LIMIT", ObjectUtils.toString(temp.get("EXAM_PLAN_LIMIT")));
                        course_map.put("EXAM_STIME", ObjectUtils.toString(temp.get("EXAM_STIME")));
                        course_map.put("EXAM_ETIME", ObjectUtils.toString(temp.get("EXAM_ETIME")));
                        course_map.put("BOOK_STARTTIME", ObjectUtils.toString(temp.get("BOOK_STARTTIME")));
                        course_map.put("BOOK_ENDTIME", ObjectUtils.toString(temp.get("BOOK_ENDTIME")));
                        course_map.put("RECORD_END", ObjectUtils.toString(temp.get("RECORD_END")));
                        course_map.put("MAKEUP", ObjectUtils.toString(temp.get("MAKEUP")));
                        course_map.put("PAY_STATE", ObjectUtils.toString(temp.get("PAY_STATE")));
                        course_map.put("BESPEAK_STATE", ObjectUtils.toString(temp.get("BESPEAK_STATE")));
                        course_map.put("TEACH_PLAN_ID", ObjectUtils.toString(temp.get("TEACH_PLAN_ID")));
                        course_map.put("COURSE_CODE", ObjectUtils.toString(temp.get("COURSE_CODE")));

                        // 选课如果是待缴费，需要先支付补考费
                        if(Constants.BOOLEAN_0.equals(ObjectUtils.toString(temp.get("PAY_STATE")))) {
                            String recId = ObjectUtils.toString(temp.get("REC_ID"));
                            List notifyDataList = new ArrayList();
                            Map notifyData = new HashMap();
                            notifyData.put("recId", recId);
                            notifyData.put("examPlanId", ObjectUtils.toString(temp.get("EXAM_PLAN_ID")));
                            notifyDataList.add(notifyData);
                            course_map.put("url", String.format("/pcenter/exam/createOrder?data=%s",
                                    URLEncoder.encode(GsonUtils.toJson(notifyDataList), Constants.CHARSET)
                                    )
                            );
                            course_map.put("pay_url_app", String.format("%s/api/exam/createOrder?data=%s&studentId=%s",
                                    pcenterStudyServer, URLEncoder.encode(GsonUtils.toJson(notifyDataList), Constants.CHARSET), searchParams.get("STUDENT_ID")
                                    )
                            ); // APP端用
                        }

                        course_map.put("REC_ID", ObjectUtils.toString(temp.get("REC_ID")));
                        course_map.put("action", AppConfig.getProperty("oclass.url"));
                        String str =  EncryptUtils.encrypt(ObjectUtils.toString(temp.get("REC_ID")) + "," + ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                        course_map.put("USER_INFO", str);
                        course_map.put("URL_NEW", ObjectUtils.toString(temp.get("URL_NEW")));
                        course_map.put("sourceCourseKch", ObjectUtils.toString(temp.get("sourceCourseKch")));
                        course_map.put("sourceCourseKcmc", ObjectUtils.toString(temp.get("sourceCourseKcmc")));
                        course_map.put("courseKch", ObjectUtils.toString(temp.get("courseKch")));
                        course_map.put("courseKcmc", ObjectUtils.toString(temp.get("courseKcmc")));
                        course_map.put("EXAM_BATCH_CODE",ObjectUtils.toString(temp.get("EXAM_BATCH_CODE")));
                        course_map.put("LEARNING_STYLE",ObjectUtils.toString(temp.get("LEARNING_STYLE")));

                        String type = ObjectUtils.toString(temp.get("KSFS_FLAG"));
                        int isNeedAppointmentPoint = 0; // 是否需要预约考点
                        if("8".equals(type) || "11".equals(type)) {
                            boolean isAppointmentPoint = false;
                            List<Map> bookPointList = examServeDao.getAppointExamPointByStudent(searchParams);
                            if (bookPointList != null && bookPointList.size() > 0) {
                                for (Map point : bookPointList) {
                                    //因为学习空间预约考点列表固定"机考"和"笔考"两种形式，所以都要返回2条数据
                                    if (StringUtils.equals(ObjectUtils.toString(point.get("EXAM_TYPE"), ""), type)) {
                                        isAppointmentPoint = true;
                                        break;
                                    }
                                }
                            }
                            if (!isAppointmentPoint) {
                                isNeedAppointmentPoint = 1;
                            }
                        }
                        course_map.put("isNeedAppointmentPoint", isNeedAppointmentPoint);

                        course_list.add(course_map);
                        map.put("APPOINTMENTLIST", course_list);

                    }
                }

                list.add(map);
            }
            resultMap.put("LIST", list);
            //===========待预约考试列表结束==============//
            
            // 全国统考预约地址
            resultMap.put("EXAM_TYPE17_URL", AppConfig.getProperty("exam_type17_url"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 待预约考试
     * @param searchParams
     * @return
     */
    public Map queryAppointmentExamByAcadeMy(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try{

            String examBatchCode = ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"), "").trim();
            if (EmptyUtils.isEmpty(examBatchCode)) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "参数不能为空！");
                return resultMap;
            }

            String end_date = null;
            List endTerm = examServeDao.getEndTermByExamBatch(searchParams);
            if(EmptyUtils.isNotEmpty(endTerm)){
                Map dateMap = (Map)endTerm.get(0);
                end_date = ObjectUtils.toString(dateMap.get("END_DATE"));
            }

            List currentFlag = examServeDao.getCurrentExamBathcFlag(searchParams);
            if(EmptyUtils.isNotEmpty(currentFlag)){
                Map currentMap = (Map) currentFlag.get(0);
                resultMap.put("TIME_FLAG", ObjectUtils.toString(currentMap.get("TIME_FLAG")));
            }else{
                resultMap.put("TIME_FLAG", "--");
            }

            //查询开考科目与课程的关联
            searchParams.put("TYPE","7");
            searchParams.put("APPOINTMENT_FLAG","1");
            List<Object> planList = new ArrayList<Object>();
            List planAndCourse = examServeDao.getExamPlanAndCourseInfo(searchParams);
            if(EmptyUtils.isNotEmpty(planAndCourse)){
                for (int i=0;i<planAndCourse.size();i++){
                    Map tempPlan = (Map) planAndCourse.get(i);

                    Map planMap = new LinkedHashMap();

                    Map viewMap = new HashMap();
                    viewMap.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
                    viewMap.put("COURSE_ID",ObjectUtils.toString(tempPlan.get("COURSE_ID")));
                    viewMap.put("TYPE",ObjectUtils.toString(tempPlan.get("TYPE")));
                    List viewList= examServeDao.getViewExamPlanByAcadeMy(viewMap);
                    if(EmptyUtils.isNotEmpty(viewList)){
                        for (int j = 0;j<viewList.size();j++){
                            Map viewScMap = (Map)viewList.get(j);
                            if(ObjectUtils.toString(searchParams.get("KKZY")).equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
                                    || "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))){
                                planList.add(tempPlan);
                                break;
                            }
                        }
                    }
                }
            }

            searchParams.put("END_DATE",end_date);
            searchParams.remove("APPOINTMENT_FLAG");

            //===========待预约考试统计开始==============//
            List<Object> resultList = new ArrayList<Object>();
            List recResultList = examServeDao.getChooseCourseByStudent(searchParams);

            if (EmptyUtils.isNotEmpty(recResultList)){
                for(int i=0;i<recResultList.size();i++){

                    Map temp1 = (Map) recResultList.get(i);

                    for(int j=0;j<planList.size();j++){
                        Map temp2 = (Map)planList.get(j);

                        Map temp3 = new LinkedHashMap();

                        if(ObjectUtils.toString(temp1.get("COURSE_ID")).equals(ObjectUtils.toString(temp2.get("COURSE_ID")))){
                            temp3.put("TERM_CODE",ObjectUtils.toString(temp1.get("TERM_CODE")));
                            temp3.put("TERM_ID",ObjectUtils.toString(temp1.get("TERM_ID")));
                            temp3.put("TERM_NAME",ObjectUtils.toString(temp1.get("TERM_NAME")));
                            temp3.put("COURSE_ID",ObjectUtils.toString(temp1.get("COURSE_ID")));
                            temp3.put("COURSE_NAME",ObjectUtils.toString(temp1.get("COURSE_NAME")));
                            temp3.put("KCH",ObjectUtils.toString(temp1.get("KCH")));
                            temp3.put("COURSE_COST",ObjectUtils.toString(temp1.get("COURSE_COST")));
                            temp3.put("MAKEUP",ObjectUtils.toString(temp1.get("MAKEUP")));
                            temp3.put("PAY_STATE",ObjectUtils.toString(temp1.get("PAY_STATE")));
                            temp3.put("BESPEAK_STATE",ObjectUtils.toString(temp1.get("BESPEAK_STATE")));
                            temp3.put("TEACH_PLAN_ID",ObjectUtils.toString(temp1.get("TEACH_PLAN_ID")));
                            temp3.put("COURSE_CODE",ObjectUtils.toString(temp1.get("COURSE_CODE")));
                            temp3.put("REC_ID",ObjectUtils.toString(temp1.get("REC_ID")));
                            temp3.put("STUDENT_ID",ObjectUtils.toString(temp1.get("STUDENT_ID")));
                            temp3.put("URL_NEW",ObjectUtils.toString(temp1.get("URL_NEW")));
                            temp3.put("KSFS_FLAG",ObjectUtils.toString(temp2.get("KSFS_FLAG")));
                            temp3.put("EXAM_TYPE",ObjectUtils.toString(temp2.get("EXAM_TYPE")));
                            temp3.put("EXAM_STYLE",ObjectUtils.toString(temp2.get("EXAM_STYLE")));
                            temp3.put("EXAM_STIME",ObjectUtils.toString(temp2.get("EXAM_STIME")));
                            temp3.put("EXAM_ETIME",ObjectUtils.toString(temp2.get("EXAM_ETIME")));
                            temp3.put("BOOK_STARTTIME",ObjectUtils.toString(temp2.get("BOOK_STARTTIME")));
                            temp3.put("BOOK_ENDTIME",ObjectUtils.toString(temp2.get("BOOK_ENDTIME")));
                            temp3.put("EXAM_PLAN_ID",ObjectUtils.toString(temp2.get("EXAM_PLAN_ID")));
                            temp3.put("EXAM_BATCH_CODE",ObjectUtils.toString(temp2.get("EXAM_BATCH_CODE")));

                            resultList.add(temp3);
                        }
                    }

                }
            }

            //删除重复数据(根据COURSE_ID相同的)
            for(int i=0;i<resultList.size();i++){
                Map map1 = (Map)resultList.get(i);
                for(int j=resultList.size()-1;j>i;j--){
                    Map map2 = (Map)resultList.get(j);
                    if(ObjectUtils.toString(map1.get("COURSE_ID")).equals(ObjectUtils.toString(map2.get("COURSE_ID")))){
                        resultList.remove(j);
                    }
                }
            }

            int pending_count = 0;
            int already_count = 0;

            for (int i=0;i<resultList.size();i++){
                Map tMap = (Map)resultList.get(i);
                if("0".equals(ObjectUtils.toString(tMap.get("BESPEAK_STATE")))){
                    pending_count = pending_count+1;
                }else{
                    already_count = already_count+1;
                }
            }
            //待预约考试科目
            resultMap.put("PENDING_COUNT", ObjectUtils.toString(pending_count));
            //已预约考试科目
            resultMap.put("ALREADY_COUNT", ObjectUtils.toString(already_count));

            //===========待预约考试统计结束==============//

            //===========我的考试统计开始==============//
            List<Object> myPlanList = new ArrayList<Object>();
            searchParams.put("TYPE","7");
            List myPlanAndCourse = examServeDao.getExamPlanAndCourseInfo(searchParams);
            if(EmptyUtils.isNotEmpty(myPlanAndCourse)){
                for (int i=0;i<myPlanAndCourse.size();i++){
                    Map tempPlan = (Map) myPlanAndCourse.get(i);

                    Map viewMap = new HashMap();
                    viewMap.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
                    viewMap.put("COURSE_ID",ObjectUtils.toString(tempPlan.get("COURSE_ID")));
                    viewMap.put("TYPE",ObjectUtils.toString(tempPlan.get("TYPE")));
                    List viewList= examServeDao.getViewExamPlanByAcadeMy(viewMap);

                    if(EmptyUtils.isNotEmpty(viewList)){
                        for (int j = 0;j<viewList.size();j++){
                            Map viewScMap = (Map)viewList.get(j);
                            if(ObjectUtils.toString(searchParams.get("KKZY")).equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
                                    || "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))){
                                myPlanList.add(tempPlan);
                                break;
                            }
                        }
                    }
                }
            }
            searchParams.remove("TYPE");
            //查询我的考试数据
            searchParams.put("BESPEAK_STATE","1");
            List<Object> myExamResult = new ArrayList<Object>();
            List myExamList = examServeDao.getChooseCourseByStudent(searchParams);
            if (EmptyUtils.isNotEmpty(myExamList)){
                for(int i=0;i<myExamList.size();i++){
                    Map temp1 = (Map) myExamList.get(i);
                    for(int j=0;j<myPlanList.size();j++){
                        Map temp2 = (Map)myPlanList.get(j);

                        Map temp3 = new LinkedHashMap();

                        if(ObjectUtils.toString(temp1.get("COURSE_ID")).equals(ObjectUtils.toString(temp2.get("COURSE_ID")))){
                            temp3.put("TERM_CODE",ObjectUtils.toString(temp1.get("TERM_CODE")));
                            temp3.put("TERM_ID",ObjectUtils.toString(temp1.get("TERM_ID")));
                            temp3.put("TERM_NAME",ObjectUtils.toString(temp1.get("TERM_NAME")));
                            temp3.put("COURSE_ID",ObjectUtils.toString(temp1.get("COURSE_ID")));
                            temp3.put("COURSE_NAME",ObjectUtils.toString(temp1.get("COURSE_NAME")));
                            temp3.put("KCH",ObjectUtils.toString(temp1.get("KCH")));
                            temp3.put("KSFS_FLAG",ObjectUtils.toString(temp2.get("KSFS_FLAG")));
                            temp3.put("EXAM_STATE",ObjectUtils.toString(temp2.get("EXAM_STATE")));
                            temp3.put("EXAM_STYLE",ObjectUtils.toString(temp2.get("EXAM_STYLE")));
                            temp3.put("EXAM_STIME",ObjectUtils.toString(temp2.get("EXAM_STIME")));
                            temp3.put("EXAM_ETIME",ObjectUtils.toString(temp2.get("EXAM_ETIME")));

                            myExamResult.add(temp3);
                        }
                    }
                }
            }

            searchParams.remove("BESPEAK_STATE");
            //删除重复数据(根据COURSE_ID相同的)
            for(int i=0;i<myExamResult.size();i++){
                Map map1 = (Map)myExamResult.get(i);
                for(int j=myExamResult.size()-1;j>i;j--){
                    Map map2 = (Map)myExamResult.get(j);
                    if(ObjectUtils.toString(map1.get("COURSE_ID")).equals(ObjectUtils.toString(map2.get("COURSE_ID")))){
                        myExamResult.remove(j);
                    }
                }
            }

            int dks_count = 0;
            int exam_count = 0;
            int yks_count = 0;

            for(int i=0;i<myExamResult.size();i++){
                Map tMap = (Map)myExamResult.get(i);

                if("0".equals(ObjectUtils.toString(tMap.get("EXAM_STATE"))) || "--".equals(ObjectUtils.toString(tMap.get("EXAM_STATE")))){
                    dks_count = dks_count+1;
                }else if("1".equals(ObjectUtils.toString(tMap.get("EXAM_STATE")))){
                    exam_count =exam_count+1;
                }else{
                    yks_count =yks_count+1;
                }
            }

            resultMap.put("DKS_COUNT",ObjectUtils.toString(dks_count));  //待考试
            resultMap.put("EXAM_COUNT",ObjectUtils.toString(exam_count)); //考试中
            resultMap.put("YKS_COUNT",ObjectUtils.toString(yks_count)); //已结束

            //===========我的考试统计结束==============//


            //===========待预约考试列表开始==============//
            Map<String, Object> tempMap = new LinkedMap();
            if(EmptyUtils.isNotEmpty(resultList)){
                for(int i=0;i<resultList.size();i++){
                    Map temp = (Map) resultList.get(i);
                    tempMap.put(ObjectUtils.toString(temp.get("TERM_ID")), ObjectUtils.toString(temp.get("TERM_ID")));
                }
            }
            List<Object> list = new ArrayList<Object>();
            if(EmptyUtils.isNotEmpty(resultList)){
                for(Object key:tempMap.keySet()){
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    List<Object> course_list = new ArrayList<Object>();

                    for(int i=0;i<resultList.size();i++){
                        Map temp = (Map) resultList.get(i);
                        if(ObjectUtils.toString(key).equals(ObjectUtils.toString(temp.get("TERM_ID")))){
                            Map<String, Object> course_map = new LinkedHashMap<String, Object>();
                            map.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
                            map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));

                            course_map.put("COURSE_ID", ObjectUtils.toString(temp.get("COURSE_ID")));
                            course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
                            course_map.put("KCH", ObjectUtils.toString(temp.get("KCH")));
                            course_map.put("COURSE_COST", ObjectUtils.toString(temp.get("COURSE_COST")));
                            course_map.put("EXAM_STYLE", ObjectUtils.toString(temp.get("EXAM_STYLE")));
                            course_map.put("KSFS_FLAG", ObjectUtils.toString(temp.get("KSFS_FLAG")));
                            course_map.put("EXAM_TYPE", ObjectUtils.toString(temp.get("EXAM_TYPE")));
                            course_map.put("EXAM_PLAN_ID", ObjectUtils.toString(temp.get("EXAM_PLAN_ID")));
                            course_map.put("EXAM_STIME", ObjectUtils.toString(temp.get("EXAM_STIME")));
                            course_map.put("EXAM_ETIME", ObjectUtils.toString(temp.get("EXAM_ETIME")));
                            course_map.put("BOOK_STARTTIME", ObjectUtils.toString(temp.get("BOOK_STARTTIME")));
                            course_map.put("BOOK_ENDTIME", ObjectUtils.toString(temp.get("BOOK_ENDTIME")));
                            course_map.put("MAKEUP", ObjectUtils.toString(temp.get("MAKEUP")));
                            course_map.put("PAY_STATE", ObjectUtils.toString(temp.get("PAY_STATE")));
                            course_map.put("BESPEAK_STATE", ObjectUtils.toString(temp.get("BESPEAK_STATE")));
                            course_map.put("TEACH_PLAN_ID", ObjectUtils.toString(temp.get("TEACH_PLAN_ID")));
                            course_map.put("COURSE_CODE", ObjectUtils.toString(temp.get("COURSE_CODE")));

                            course_map.put("url", ObjectUtils.toString(AppConfig.getProperty("payHost")));
                            if("00815".equals(ObjectUtils.toString(temp.get("COURSE_CODE")))){
                                //课程号00815固定的productId为d88118101c33479ca67afd0136001da7
                                course_map.put("productId", ObjectUtils.toString(AppConfig.getProperty("productId00815")).trim());
                            }else if("02970".equals(ObjectUtils.toString(temp.get("COURSE_CODE")))){
                                //课程号02970固定的productId为41b8cd2b8d8e4416ab6dc641ce1c2c13
                                course_map.put("productId", ObjectUtils.toString(AppConfig.getProperty("productId02970")).trim());
                            }else{
                                //其他课程号固定的productId为1d28c85e43b34805a17e94d9236504e5
                                course_map.put("productId", ObjectUtils.toString(AppConfig.getProperty("productIdOther")).trim());
                            }
                            course_map.put("username", ObjectUtils.toString(searchParams.get("username")).trim());
                            course_map.put("referrer", ObjectUtils.toString(AppConfig.getProperty("payReferrer")).trim()); //缴费固定的referrer值为041
                            course_map.put("REMARK", ObjectUtils.toString(SequenceUUID.getSequence())); //值不同,保证在招生平台缴费时，传的值每次不一样
                            course_map.put("LEARNCENTER_CODE", ObjectUtils.toString(AppConfig.getProperty("learnCenterCode")).trim());
                            course_map.put("REC_ID", ObjectUtils.toString(temp.get("REC_ID")));
                            course_map.put("URL_NEW", ObjectUtils.toString(temp.get("URL_NEW")));
                            course_map.put("EXAM_BATCH_CODE",ObjectUtils.toString(temp.get("EXAM_BATCH_CODE")));

                            course_list.add(course_map);
                            map.put("APPOINTMENTLIST", course_list);
                        }
                    }
                    list.add(map);
                }
            }
            resultMap.put("LIST", list);
            //===========待预约考试列表结束==============//

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 查询考试计划
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map getExamBatchData(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try{
            String studentId = (String) searchParams.get("STUDENT_ID");
            if(!"1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))){
                searchParams.put("CURRENT_FLAG", "1");
            }else{
                searchParams.put("CURRENT_FLAG", "2");
            }
            List list = examServeDao.getExamBatchData(searchParams);
            Map<String, Object> batchMap = new LinkedHashMap<String, Object>();
            if(EmptyUtils.isNotEmpty(list)){
                for(int i = 0;i<list.size();i++){
                    Map temp = (Map) list.get(i);
                    batchMap.put("EXAM_BATCH_ID", ObjectUtils.toString(temp.get("EXAM_BATCH_ID")));
                }
            }

            searchParams.remove("CURRENT_FLAG");
            List list1 = examServeDao.getExamBatchData(searchParams);
            if(EmptyUtils.isNotEmpty(list1)){

                List<Object> batchList = new ArrayList<Object>();

                for(int i = 0;i<list1.size();i++){
                    Map map = (Map) list1.get(i);
                    Map<String, Object> plan_map = new LinkedHashMap<String, Object>();
                    plan_map.put("EXAM_BATCH_ID", ObjectUtils.toString(map.get("EXAM_BATCH_ID")));
                    plan_map.put("EXAM_BATCH_CODE", ObjectUtils.toString(map.get("EXAM_BATCH_CODE")));
                    plan_map.put("EXAM_BATCH_NAME", ObjectUtils.toString(map.get("EXAM_BATCH_NAME")));
                    String bookStartTime = ObjectUtils.toString(map.get("BOOK_ST"));
                    String bookEndTime = ObjectUtils.toString(map.get("BOOK_END"));
                    String booksStartTime = ObjectUtils.toString(map.get("BOOKS_ST"));
                    String booksEndTime = ObjectUtils.toString(map.get("BOOKS_END"));
                    if(StringUtils.isNotBlank(booksStartTime) && new Date().after(DateUtil.parseDate(booksStartTime))) {  // 如果当前时间在某时间之后
                        plan_map.put("BOOK_ST", booksStartTime);
                        plan_map.put("BOOK_END", booksEndTime);
                    } else {
                        plan_map.put("BOOK_ST", bookStartTime);
                        plan_map.put("BOOK_END", bookEndTime);
                    }
                    plan_map.put("ONLINE_ST", ObjectUtils.toString(map.get("ONLINE_ST")));
                    plan_map.put("ONLINE_END", ObjectUtils.toString(map.get("ONLINE_END")));

                    if("1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))){

                    }else {
                        plan_map.put("PAPER_ST", ObjectUtils.toString(map.get("PAPER_ST")));
                        plan_map.put("PAPER_END", ObjectUtils.toString(map.get("PAPER_END")));
                        plan_map.put("THESIS_END", ObjectUtils.toString(map.get("THESIS_END")));
                        plan_map.put("MACHINE_ST", ObjectUtils.toString(map.get("MACHINE_ST")));
                        plan_map.put("MACHINE_END", ObjectUtils.toString(map.get("MACHINE_END")));
                        plan_map.put("REPORT_END", ObjectUtils.toString(map.get("REPORT_END")));
                        plan_map.put("PROVINCE_ONLINE_ST", ObjectUtils.toString(map.get("PROVINCE_ONLINE_ST")));
                        plan_map.put("PROVINCE_ONLINE_END", ObjectUtils.toString(map.get("PROVINCE_ONLINE_END")));
                        plan_map.put("SHAPE_END", ObjectUtils.toString(map.get("SHAPE_END")));
                        plan_map.put("RECORD_ST", ObjectUtils.toString(map.get("RECORD_ST")));
                        plan_map.put("RECORD_END", ObjectUtils.toString(map.get("RECORD_END")));
                    }
                    boolean currentFlag = ObjectUtils.toString(map.get("EXAM_BATCH_ID")).equals(ObjectUtils.toString(batchMap.get("EXAM_BATCH_ID")));
                    // 是否为当前考试计划，1：是，0：否
                    plan_map.put("CURRENT_FLAG", currentFlag ? "1" : "0");
                    int isNeedAppointmentPoint = 0; // 是否需要预约考点，1-是 0-否
                    if(currentFlag) {
                        GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
                        Map searchParams2 = new HashMap();
                        searchParams2.put("EXAM_BATCH_CODE", ObjectUtils.toString(map.get("EXAM_BATCH_CODE")));
                        searchParams2.put("STUDENT_ID", studentId);
                        searchParams2.put("USER_TYPE", info.getUserType());
                        searchParams2.put("username", info.getSfzh());
                        searchParams2.put("XJZT", info.getXjzt());
                        searchParams2.put("KKZY", info.getMajor());
                        searchParams2.put("XX_ID", info.getXxId());
                        Map examResultMap = queryAppointmentExam(searchParams2);

                        List<Map> termList = (List<Map>) examResultMap.get("LIST");
                        if (termList != null && termList.size() > 0) {
                            for (Map term : termList) {
                                List<Map> appointmentList = (List<Map>) term.get("APPOINTMENTLIST");
                                if (appointmentList != null && appointmentList.size() > 0) {
                                    for (Map appointment : appointmentList) {
                                        String type = ObjectUtils.toString(appointment.get("KSFS_FLAG"));
                                        if("8".equals(type) || "11".equals(type)) {
                                            isNeedAppointmentPoint = 1;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    plan_map.put("isNeedAppointmentPoint", isNeedAppointmentPoint);

                    batchList.add(plan_map);
                    resultMap.put("PLAN_LIST", batchList);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 我的考试
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map myExamDataList(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        String examUrl = AppConfig.getProperty("examServer") + AppConfig.getProperty("api.exam.login");
        try{
            String examBatchCode = ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"), "").trim();
            if (EmptyUtils.isEmpty(examBatchCode)) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "参数不能为空！");
                return resultMap;
            }

            //===========我的考试列表开始==============//
            List<Map> myExamList = examNewDaoImpl.myExamList(searchParams);

            Map<String, Map> tempMap = new LinkedHashMap<String, Map>();
            // 1.学期先去重复
            for(Map info : myExamList) {
                Map<String, Object> tempInfo = new HashMap<String, Object>();
                tempInfo.put("TERM_ID", info.get("TERM_ID"));
                tempInfo.put("TERM_CODE", info.get("TERM_CODE"));
                tempInfo.put("TERM_NAME", info.get("TERM_NAME"));
                tempInfo.put("EXAMLIST", new ArrayList<Map>());
                tempMap.put((String) info.get("TERM_ID"), tempInfo);
            }
            // 2.放到对应的学期里面去
            for(Map info : myExamList) {
                String examState = (String) info.get("EXAM_STATE");
                if(EmptyUtils.isNotEmpty(info.get("EXAM_SCORE"))){
                	info.put("EXAM_SCORE", subZeroAndDot(info.get("EXAM_SCORE").toString()));
                }
                String ksfsFlag = ((BigDecimal) info.get("KSFS_FLAG")) + "";
                if("19".equals(ksfsFlag)) {
                    info.put("action", AppConfig.getProperty("examOucnet_ks")); // 考试地址
                } else if("7".equals(ksfsFlag)){
                	info.put("USER_NO", searchParams.get("USER_NO"));
                	info.put("action", examUrl); // 网考单点登录地址
                } else {
                    info.put("action", examUrl); // 查看试卷地址
                }
                // 加入到对应的学期里面
                Map<String, Object> tempInfo = tempMap.get((String) info.get("TERM_ID"));
                List<Map> examList = (List<Map>) tempInfo.get("EXAMLIST");
                examList.add(info);
            }
            // 3.最后再将学期Map转化成List
            List<Object> resultList = new ArrayList<Object>();
            for(Map.Entry<String, Map> e : tempMap.entrySet()) {
                resultList.add(e.getValue());
            }
            resultMap.put("LIST", resultList);
            //===========我的考试列表结束==============//
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 我的考试(院校模式)
     * @param searchParams
     * @return
     */
    public Map myExamDataListByAcadeMy(Map<String, Object> searchParams) {
        return null;
    }

    /**
     * 统计考试数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map countExamDataInfo(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        try{
            searchParams.put("BESPEAK_STATE", "0");
            List list = examServeDao.getAppointmentExamList(searchParams);  //待预约
            if(EmptyUtils.isNotEmpty(list)){
                resultMap.put("PENDING_COUNT", list.size());
            }else{
                resultMap.put("PENDING_COUNT", "0");
            }
            searchParams.put("BESPEAK_STATE", "1");
            List list1 = examServeDao.getAppointmentExamList(searchParams); //已预约
            if(EmptyUtils.isNotEmpty(list1)){
                resultMap.put("ALREADY_COUNT", list.size());
            }else{
                resultMap.put("ALREADY_COUNT", "0");
            }
            searchParams.remove("BESPEAK_STATE");

            int dks_count = 0;
            int exam_count = 0;
            int yks_count = 0;

            Map myExamList = myExamDataList(searchParams);
            List<Map> termList = (List<Map>) myExamList.get("LIST");
            for (Map term : termList) {
                List<Map> examList = (List<Map>) term.get("EXAMLIST");
                for (Map e : examList) {
                    String examState = (String) e.get("EXAM_STATE");
                    if("0".equals(examState) || "--".equals(examState)){
                        dks_count = dks_count+1;
                    }else if("1".equals(examState)){
                        exam_count =exam_count+1;
                    }else{
                        yks_count =yks_count+1;
                    }
                }
            }

            resultMap.put("DKS_COUNT",ObjectUtils.toString(dks_count));  //待考试
            resultMap.put("EXAM_COUNT",ObjectUtils.toString(exam_count)); //考试中
            resultMap.put("YKS_COUNT",ObjectUtils.toString(yks_count)); //已结束

            //考试科目总数
            int kyy_count = Integer.parseInt(ObjectUtils.toString(resultMap.get("PENDING_COUNT")));
            int sum_count = kyy_count + dks_count + exam_count + yks_count;

            resultMap.put("SUM_COUNT", ObjectUtils.toString(sum_count));

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    public Page queryPointList(Map<String, Object> searchParams, PageRequest pageRequst) {
        return examServeDao.queryPointList(searchParams, pageRequst);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPersonalZP(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        List<Object> personal = new ArrayList<Object>();
        try{
            List list = examServeDao.queryPersonalZP(searchParams);
            if(EmptyUtils.isNotEmpty(list)){
                for(int i=0;i<list.size();i++){
                    Map map = (Map) list.get(i);
                    Map<String, Object> personal_map = new LinkedHashMap<String, Object>();
                    personal_map.put("STUDENT_ID", ObjectUtils.toString(map.get("STUDENT_ID")));
                    if(EmptyUtils.isNotEmpty(ObjectUtils.toString(map.get("URL_NEW")))){
                        personal_map.put("URL_NEW", ObjectUtils.toString(map.get("URL_NEW")));
                    }else{
                        personal_map.put("URL_NEW", ObjectUtils.toString(map.get("URL")));
                    }
                    personal.add(personal_map);
                    resultMap.put("PERSONLIST", personal);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map updatePersonalZP(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        try{
            int i = 0;
            List list = examServeDao.getIsExistZP(searchParams);
            if(EmptyUtils.isNotEmpty(list)){
                Map map = (Map) list.get(0);

                if("0".equals(ObjectUtils.toString(map.get("TEMP")))){
                    Map tempMap = new HashMap();
                    tempMap.put("SING_ID", SequenceUUID.getSequence());
                    tempMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                    tempMap.put("ID_NO", ObjectUtils.toString(searchParams.get("ID_NO")));
                    tempMap.put("FILE_TYPE", "zp");
                    tempMap.put("URL_NEW", ObjectUtils.toString(searchParams.get("URL_ADDRESS")));
                    i = examServeDao.insertSiguUpData(tempMap);
                    resultMap.put("result", ObjectUtils.toString(i));
                }else{
                    i = examServeDao.updatePersonalZP(searchParams);
                    resultMap.put("result", ObjectUtils.toString(i));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map makeAppointment(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        int result = 0;
        try{
            String recId = ObjectUtils.toString(searchParams.get("REC_ID"), "");
            if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"), "").trim())
                    || EmptyUtils.isEmpty(recId.trim())
                    || EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID"), "").trim())) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "参数不能为空！");
                return resultMap;
            }


            List payList = examServeDao.getIsPayStateByCourse(searchParams);
            if(EmptyUtils.isNotEmpty(payList)){
                Map payMap = (Map) payList.get(0);
                String payState = ObjectUtils.toString(payMap.get("PAY_STATE"));
                if("0".equals(payState)){
                    resultMap.put("result", "3");
                    resultMap.put("messageInfo", "该课程尚未缴费,请先缴费后再进行预约！");
                    return resultMap;
                }
            }

            int count = 0;
            List makeList = examServeDao.makeAppointCount(searchParams);
            if(EmptyUtils.isNotEmpty(makeList)){
                Map makeMap = (Map) makeList.get(0);
                count = Integer.parseInt(ObjectUtils.toString(makeMap.get("KS_COUNT")));
            }

            if(count >= WebConstants.EXAM_APPOINTMENT_LIMIT){
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "最多只能预约" + WebConstants.EXAM_APPOINTMENT_LIMIT + "门考试！");
            }else{
                List plan = examServeDao.getExamPlanBySubjectCode(searchParams);
                if(EmptyUtils.isNotEmpty(plan)){
                    Map planMap = (Map) plan.get(0);

                    String type = ((BigDecimal) planMap.get("TYPE")) + "";
                    if("8".equals(type) || "11".equals(type)) {
                        boolean isAppointmentPoint = false;
                        List<Map> bookPointList = examServeDao.getAppointExamPointByStudent(searchParams);
                        if (bookPointList != null && bookPointList.size() > 0) {
                            for (Map point : bookPointList) {
                                //因为学习空间预约考点列表固定"机考"和"笔考"两种形式，所以都要返回2条数据
                                if (StringUtils.equals(ObjectUtils.toString(point.get("EXAM_TYPE"), ""), type)) {
                                    isAppointmentPoint = true;
                                    break;
                                }
                            }
                        }
                        if (!isAppointmentPoint) {
                            resultMap.put("result", "4");
                            resultMap.put("messageInfo", "请先预约考点！");
                            return resultMap;
                        }
                    }

                    // 预约之前删除原来预约数据
                    gjtExamAppointmentNewDao.deleteExamAppointment(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")), ObjectUtils.toString(searchParams.get("STUDENT_ID")), ObjectUtils.toString(searchParams.get("REC_ID")));

                    Map appointmentMap = new HashMap();
                    appointmentMap.put("APPOINTMENT_ID", ObjectUtils.toString(SequenceUUID.getSequence()));
                    appointmentMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                    appointmentMap.put("EXAM_PLAN_ID", ObjectUtils.toString(planMap.get("EXAM_PLAN_ID")));
                    appointmentMap.put("TYPE", ObjectUtils.toString(planMap.get("TYPE")));
                    appointmentMap.put("CREATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                    appointmentMap.put("XX_ID", ObjectUtils.toString(planMap.get("XX_ID")));
                    appointmentMap.put("REC_ID", recId);
                    appointmentMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
                    appointmentMap.put("STATUS", "0");
                    result = examServeDao.saveAppointmentData(appointmentMap);

                    if(result == 1){
                        Map recordMap = new HashMap();
                        recordMap.put("RECORD_ID", ObjectUtils.toString(SequenceUUID.getSequence()));
                        recordMap.put("REC_ID", recId);
                        GjtRecResult recResult = gjtRecResultService.queryById(recId);
                        recordMap.put("TEACH_PLAN_ID", recResult.getTeachPlanId());
                        recordMap.put("EXAM_STATE", "1");
                        recordMap.put("IS_CANCEL", "0");
                        recordMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                        recordMap.put("BESPEAK_STATE", "1");
                        examServeDao.updateRecResultState(recordMap);
                        examServeDao.inactiveExamState(recordMap);
                        int i = examServeDao.saveExamRecord(recordMap);
                        resultMap.put("result", ObjectUtils.toString(i));
                    }
                }else{
                    resultMap.put("result", "0");
                    resultMap.put("messageInfo", "该课程尚未创建开考科目, 无法预约");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            resultMap.put("result", "-1");
            resultMap.put("messageInfo", "服务器异常");
        }
        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional
    public Map cancelAppointment(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        int result = 0;
        try{
            if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"), "").trim())
                    || EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("REC_ID"), "").trim())
                    || EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID"), "").trim())) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "参数不能为空！");
                return resultMap;
            }

            List recList = examServeDao.getRecResultExamState(searchParams);
            if(EmptyUtils.isNotEmpty(recList)){
                Map temp = (Map) recList.get(0);
                if(!"1".equals(ObjectUtils.toString(temp.get("BESPEAK_STATE")))){
                    resultMap.put("result", "0");
                    resultMap.put("messageInfo", "课程不是已预约状态, 操作失败.");
                    return resultMap;
                }
            }
            List plan = examServeDao.getExamPlanBySubjectCode(searchParams);
            if(EmptyUtils.isNotEmpty(plan)){
                Map planMap = (Map) plan.get(0);
                Map entityMap = new HashMap();
                entityMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                entityMap.put("EXAM_PLAN_ID", ObjectUtils.toString(planMap.get("EXAM_PLAN_ID")));
                entityMap.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
                entityMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
                List entity = examServeDao.getAppointmentByStuidAndPlanid(entityMap);
                Map appointmentMap = new HashMap();
                if(EmptyUtils.isNotEmpty(entity)){
                    Map appoint = (Map) entity.get(0);
                    appointmentMap.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                    appointmentMap.put("APPOINTMENT_ID", ObjectUtils.toString(appoint.get("APPOINTMENT_ID")));
                    appointmentMap.put("EXAM_PLAN_ID", ObjectUtils.toString(planMap.get("EXAM_PLAN_ID")));
                    appointmentMap.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
                    appointmentMap.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
                    appointmentMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
                    appointmentMap.put("IS_DELETED", "1");
                    result = examServeDao.updateAppointmentPlan(appointmentMap);
                }
                // 取消预约删除原来预约数据
                gjtExamAppointmentNewDao.deleteExamAppointment((String) entityMap.get("EXAM_BATCH_CODE"), (String) entityMap.get("STUDENT_ID"), (String) entityMap.get("REC_ID"));
                if(result == 1){
                    Map recordMap = new HashMap();
                    recordMap.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
                    recordMap.put("BESPEAK_STATE", "0");
                    examServeDao.inactiveExamState(recordMap);
                    int i = examServeDao.updateRecResultState(recordMap);
                    resultMap.put("result", ObjectUtils.toString(i));
                }
            }else{
                resultMap.put("messageInfo", "该课程尚未创建开考科目, 无法预约");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @Transactional
    public Map appointPay(Map<String, Object> params) {
        Map resultMap = new HashMap();
        try{
            String recId = ObjectUtils.toString(params.get("REC_ID"));
            String studentId = ObjectUtils.toString(params.get("STUDENT_ID"));
            String examPlanId = ObjectUtils.toString(params.get("EXAM_PLAN_ID"));
            String paySn = ObjectUtils.toString(params.get("PAY_SN"));
            if(StringUtils.isEmpty(recId) || StringUtils.isEmpty(examPlanId) || StringUtils.isEmpty(studentId) || StringUtils.isEmpty(paySn)) {
                resultMap.put("FLAG", "2");
                return resultMap;
            }
            GjtRecResult recResult = gjtRecResultService.queryById(recId);
            // 判断是否缴费了，若无则需要查询招生平台缴费单信息
            if(StringUtils.equals(recResult.getPayState(), Constants.BOOLEAN_1)) {
                resultMap.put("FLAG", "1");
                return resultMap;
            }
            // 查询缴费单信息
            OrderBillInfo orderBillInfo = billRemoteNewService.getOrderBillsInfoForInterface(paySn, null);
            if(orderBillInfo != null) {
                // 判断是否支付成功
                if (orderBillInfo.getPay_status() == 1) {
                    Map payMap = new HashMap();
                    payMap.put("REC_ID", recId);
                    payMap.put("STUDENT_ID", studentId);
                    payMap.put("PAY_STATE", "1");
                    payMap.put("ORDER_MARK", paySn);
                    boolean flag = examServeDao.updatePayState(payMap);
                    if(flag) {
                        // 更新支付记录
                        Map<String, Object> searchParams = new HashMap<String, Object>();
                        searchParams.put("EQ_studentId", studentId);
                        searchParams.put("EQ_examPlanId", examPlanId);
                        searchParams.put("EQ_paySn", paySn);
                        List<GjtExamCost> examCostList = gjtExamCostService.queryBy(searchParams, null);
                        if (examCostList == null || examCostList.size() == 0) {
                            GjtExamCost examCost = new GjtExamCost();
                            examCost.setCostId(UUIDUtils.random());
                            examCost.setPaySn(paySn);
                            examCost.setStudentId(recResult.getStudentId());
                            examCost.setCourseId(recResult.getCourseId());
                            GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(examPlanId);
                            examCost.setGradeId(examPlanNew.getGradeId());
                            examCost.setTeachPlanId(recResult.getTeachPlanId());
                            examCost.setExamBatchCode(examPlanNew.getExamBatchCode());
                            examCost.setExamPlanId(examPlanNew.getExamPlanId());
                            examCost.setKsfs(examPlanNew.getType() + "");
                            examCost.setMakeup("1");
                            examCost.setPayStatus("0");
                            examCost.setCourseCost(orderBillInfo.getPay_amount());
                            examCost.setCourseCode(gjtCourseService.queryById(recResult.getCourseId()).getKch());
                            examCost.setPayDate(new Date(orderBillInfo.getPay_time()*1000));
                            gjtExamCostService.insert(examCost);
                        } else {
                            for (GjtExamCost examCost : examCostList) {
                                examCost.setPayStatus("0");
                                examCost.setPayDate(new Date(orderBillInfo.getPay_time()*1000));
                                examCost.setUpdatedDt(new Date());
                                examCost.setUpdatedBy(recResult.getStudentId());
                                gjtExamCostService.save(examCost);
                            }
                        }
                    }
                    resultMap.put("FLAG", "1");
                } else {
                    Map<String, Object> searchParams = new HashMap<String, Object>();
                    searchParams.put("EQ_studentId", studentId);
                    searchParams.put("EQ_examPlanId", examPlanId);
                    searchParams.put("EQ_paySn", paySn);
                    List<GjtExamCost> examCostList = gjtExamCostService.queryBy(searchParams, null);
                    if(examCostList == null || examCostList.size() == 0) {
                        GjtExamCost examCost = new GjtExamCost();
                        examCost.setCostId(UUIDUtils.random());
                        examCost.setPaySn(paySn);
                        examCost.setStudentId(recResult.getStudentId());
                        examCost.setCourseId(recResult.getCourseId());
                        GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(examPlanId);
                        examCost.setGradeId(examPlanNew.getGradeId());
                        examCost.setTeachPlanId(recResult.getTeachPlanId());
                        examCost.setExamBatchCode(examPlanNew.getExamBatchCode());
                        examCost.setExamPlanId(examPlanNew.getExamPlanId());
                        examCost.setKsfs(examPlanNew.getType() + "");
                        examCost.setMakeup("1");
                        examCost.setPayStatus("1");
                        examCost.setCourseCost(orderBillInfo.getPay_amount());
                        examCost.setCourseCode(gjtCourseService.queryById(recResult.getCourseId()).getKch());
                        gjtExamCostService.insert(examCost);
                    } else {
                        for(GjtExamCost examCost : examCostList) {
                            examCost.setPayStatus("1");
                            examCost.setUpdatedDt(new Date());
                            examCost.setUpdatedBy(recResult.getStudentId());
                            gjtExamCostService.save(examCost);
                        }
                    }
                    resultMap.put("FLAG", "0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map getAdmissionData(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try{
            Map<String, Object> tempMap = new LinkedHashMap();
            List list = examServeDao.getAdmissionData(searchParams);
            if(EmptyUtils.isNotEmpty(list)){
                for(int i=0;i<list.size();i++){
                    Map temp = (Map) list.get(i);
                    tempMap.put(ObjectUtils.toString(temp.get("STUDENT_ID")), ObjectUtils.toString(temp.get("STUDENT_ID")));
                }
            }
            List<Object> list1 = new ArrayList<Object>();
            if(EmptyUtils.isNotEmpty(list)){
                for(Object key:tempMap.keySet()){
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    List<Object> course_list = new ArrayList<Object>();

                    for(int i=0;i<list.size();i++){
                        Map temp = (Map) list.get(i);
                        if(ObjectUtils.toString(key).equals(ObjectUtils.toString(temp.get("STUDENT_ID")))){
                            Map<String, Object> course_map = new LinkedHashMap<String, Object>();

                            map.put("STUDENT_ID", ObjectUtils.toString(temp.get("STUDENT_ID")));
                            map.put("ADMISSION_ZKZ", ObjectUtils.toString(temp.get("ADMISSION_ZKZ")));
                            map.put("STU_PHOTO", ObjectUtils.toString(temp.get("STU_PHOTO")));
                            map.put("ADMISSION_NAME", ObjectUtils.toString(temp.get("ADMISSION_NAME")));
                            map.put("STU_NUMBER", ObjectUtils.toString(temp.get("STU_NUMBER")));
                            map.put("EXAM_POINT_NAME", ObjectUtils.toString(temp.get("EXAM_POINT_NAME")));
                            map.put("EXAM_ADDRESS", ObjectUtils.toString(temp.get("EXAM_ADDRESS")));
                            map.put("TYPE", ObjectUtils.toString(temp.get("TYPE")));

                            course_map.put("EXAM_NO", ObjectUtils.toString(temp.get("EXAM_NO")));
                            course_map.put("COURSE_NAME", ObjectUtils.toString(temp.get("COURSE_NAME")));
                            course_map.put("EXAM_TYPE", ObjectUtils.toString(temp.get("EXAM_TYPE")));
                            course_map.put("EXAM_STYLE", ObjectUtils.toString(temp.get("EXAM_STYLE")));
                            course_map.put("EXAM_DATE", ObjectUtils.toString(temp.get("EXAM_DATE")));
                            course_map.put("EXAM_TIME", ObjectUtils.toString(temp.get("EXAM_TIME")));
                            course_map.put("SEAT_NO", ObjectUtils.toString(temp.get("SEAT_NO")));
                            if (Integer.parseInt(ObjectUtils.toString(temp.get("PLAN_COUNT")))>1) {
                            	course_map.put("PLAN_COUNT", "连考"+ObjectUtils.toString(temp.get("PLAN_COUNT"))+"科");
                            } else {
                            	course_map.put("PLAN_COUNT", "");
                            }

                            course_list.add(course_map);
                            map.put("ADMISSION", course_list);
                        }
                    }
                    list1.add(map);
                }
            }
            resultMap.put("LIST", list1);
        }catch(Exception e){
            e.printStackTrace();
        }

        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map entryExam(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        try{

            List project = courseRemoteService.getProjectTestInfo(searchParams);
            if (EmptyUtils.isNotEmpty(project)) {
                resultMap = (Map) project.get(0);
            }
            Map map = new HashMap();
            map.put("CURRENTDATE", DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            resultMap.putAll(map);
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map getSignUpId(Map<String, Object> searchParams) {
        Map resultMap = new HashMap();
        try{
            List list = examServeDao.getSignUpId(searchParams);
            if(EmptyUtils.isNotEmpty(list)){
                Map map = (Map) list.get(0);
                resultMap.put("SIGNUP_ID", ObjectUtils.toString(map.get("SIGNUP_ID")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public Map queryGjtExamAppointmentNewData(Map searchParams) throws ServiceException {
        Map resultMap = new HashMap();
        try{
            List list = examServeDao.getAppointmentByStudentAndTeachPlanId(searchParams);
            if(EmptyUtils.isNotEmpty(list)){
                resultMap = (Map) list.get(0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }



    public static Map<String,Object> appointmentMap(Map tMap){
        Map resultMap = new LinkedHashMap();

        try{
            resultMap.put("TERM_CODE",ObjectUtils.toString(tMap.get("TERM_CODE")));
            resultMap.put("TERM_ID",ObjectUtils.toString(tMap.get("TERM_ID")));
            resultMap.put("TERM_NAME",ObjectUtils.toString(tMap.get("TERM_NAME")));
            resultMap.put("COURSE_ID",ObjectUtils.toString(tMap.get("COURSE_ID")));
            resultMap.put("COURSE_NAME",ObjectUtils.toString(tMap.get("COURSE_NAME")));
            resultMap.put("KCH",ObjectUtils.toString(tMap.get("KCH")));
            resultMap.put("COURSE_COST",ObjectUtils.toString(tMap.get("COURSE_COST")));
            resultMap.put("MAKEUP",ObjectUtils.toString(tMap.get("MAKEUP")));
            resultMap.put("PAY_STATE",ObjectUtils.toString(tMap.get("PAY_STATE")));
            resultMap.put("BESPEAK_STATE",ObjectUtils.toString(tMap.get("BESPEAK_STATE")));
            resultMap.put("TEACH_PLAN_ID",ObjectUtils.toString(tMap.get("TEACH_PLAN_ID")));
            resultMap.put("COURSE_CODE",ObjectUtils.toString(tMap.get("COURSE_CODE")));
            resultMap.put("REC_ID",ObjectUtils.toString(tMap.get("REC_ID")));
            resultMap.put("STUDENT_ID",ObjectUtils.toString(tMap.get("STUDENT_ID")));
            resultMap.put("URL_NEW",ObjectUtils.toString(tMap.get("URL_NEW")));
            resultMap.put("KSFS_FLAG",ObjectUtils.toString(tMap.get("KSFS_FLAG")));
            resultMap.put("EXAM_TYPE",ObjectUtils.toString(tMap.get("EXAM_TYPE")));
            resultMap.put("EXAM_STYLE",ObjectUtils.toString(tMap.get("EXAM_STYLE")));
            resultMap.put("EXAM_STIME",ObjectUtils.toString(tMap.get("EXAM_STIME")));
            resultMap.put("EXAM_ETIME",ObjectUtils.toString(tMap.get("EXAM_ETIME")));
            resultMap.put("BOOK_STARTTIME",ObjectUtils.toString(tMap.get("BOOK_STARTTIME")));
            resultMap.put("BOOK_ENDTIME",ObjectUtils.toString(tMap.get("BOOK_ENDTIME")));
            resultMap.put("EXAM_PLAN_ID",ObjectUtils.toString(tMap.get("EXAM_PLAN_ID")));
            resultMap.put("EXAM_BATCH_CODE",ObjectUtils.toString(tMap.get("EXAM_BATCH_CODE")));

        }catch(Exception e){
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * 查询往期考试记录(移动端使用)
     * @param searchParams
     * @return
     */
    public Map getPastLearnRecord(Map searchParams) {
        Map resultMap = new LinkedHashMap();
        try{
            if (EmptyUtils.isEmpty(searchParams.get("EXAM_BATCH_CODES"))) {
                resultMap.put("result", "2");
                resultMap.put("messageInfo", "参数不能为空！");
                return resultMap;
            }

            //查询选课信息
            searchParams.put("BESPEAK_STATE","1");
            List<Object> myExamResult = new ArrayList<Object>();
            List myExamList = examServeDao.getChooseCourseByStudent(searchParams);

            if(EmptyUtils.isNotEmpty(myExamList)){
                for (int i=0;i<myExamList.size();i++){
                    Map temp1 = (Map) myExamList.get(i);

                    Map courseMap = new HashMap();
                    searchParams.put("XX_ID",searchParams.get("XX_ID"));
                    courseMap.put("EXAM_BATCH_CODES",searchParams.get("EXAM_BATCH_CODES"));
                    courseMap.put("COURSE_ID",ObjectUtils.toString(temp1.get("COURSE_ID")));
                    courseMap.put("TERM_ID",ObjectUtils.toString(temp1.get("TERM_ID")));
                    List courseListInfo = examServeDao.getPastLearnRecord(courseMap);
                    if(EmptyUtils.isNotEmpty(courseListInfo)){
                        for (int j=0;j<courseListInfo.size();j++){
                            Map batchMap = (Map) courseListInfo.get(j);

                            Map temp3 = new LinkedHashMap();

                            temp3.put("TERM_CODE",ObjectUtils.toString(temp1.get("TERM_CODE")));
                            temp3.put("TERM_ID",ObjectUtils.toString(temp1.get("TERM_ID")));
                            temp3.put("TERM_NAME",ObjectUtils.toString(temp1.get("GRADE_NAME")));
                            temp3.put("START_DATE",ObjectUtils.toString(temp1.get("START_DATE")));
                            temp3.put("END_DATE",ObjectUtils.toString(temp1.get("END_DATE")));
                            temp3.put("COURSE_ID",ObjectUtils.toString(temp1.get("COURSE_ID")));
                            temp3.put("COURSE_NAME",ObjectUtils.toString(temp1.get("COURSE_NAME")));
                            temp3.put("REC_ID", ObjectUtils.toString(temp1.get("REC_ID")));
                            temp3.put("CHOOSE_ID", ObjectUtils.toString(temp1.get("REC_ID")));
                            temp3.put("TEACH_PLAN_ID", ObjectUtils.toString(temp1.get("TEACH_PLAN_ID")));
                            temp3.put("SCORE_STATE",ObjectUtils.toString(temp1.get("SCORE_STATE"))); //状态0:未通过;1:已通过;2:学习中;3:登记中
                            temp3.put("KCXXBZ",ObjectUtils.toString(temp1.get("KCXXBZ")));
                            temp3.put("KCKSBZ",ObjectUtils.toString(temp1.get("KCKSBZ")));
                            temp3.put("EXAM_SCORE",ObjectUtils.toString(temp1.get("EXAM_SCORE1")));//形成性成绩
                            temp3.put("EXAM_SCORE1",ObjectUtils.toString(temp1.get("EXAM_SCORE"))); //终结性成绩 (形成性成绩/终结性成绩,与PC端公用同一条sql sql取值有字段有修改)
                            temp3.put("EXAM_SCORE2",ObjectUtils.toString(temp1.get("EXAM_SCORE2")));//总成绩
                            temp3.put("KCH",ObjectUtils.toString(temp1.get("KCH")));
                            temp3.put("KSFS_FLAG",ObjectUtils.toString(batchMap.get("KSFS_FLAG")));
                            temp3.put("EXAM_TYPE",ObjectUtils.toString(batchMap.get("EXAM_TYPE")));
                            temp3.put("EXAM_STATE",ObjectUtils.toString(batchMap.get("EXAM_STATE"))); //0:待考试,1：考试中,2：已结束
                            temp3.put("EXAM_STYLE",ObjectUtils.toString(batchMap.get("EXAM_STYLE")));
                            temp3.put("EXAM_STIME",ObjectUtils.toString(batchMap.get("EXAM_STIME")));
                            temp3.put("EXAM_ETIME",ObjectUtils.toString(batchMap.get("EXAM_ETIME")));
                            temp3.put("EXAM_NO",ObjectUtils.toString(batchMap.get("EXAM_NO")));
                            temp3.put("EXAM_BATCH_CODE", ObjectUtils.toString(batchMap.get("EXAM_BATCH_CODE")));
                            temp3.put("EXAM_PLAN_ID", ObjectUtils.toString(batchMap.get("EXAM_PLAN_ID")));

                            myExamResult.add(temp3);
                        }
                    }
                }
            }

            searchParams.remove("BESPEAK_STATE");
            //删除重复数据(根据COURSE_ID相同的)
            for(int i=0;i<myExamResult.size();i++){
                Map map1 = (Map)myExamResult.get(i);
                for(int j=myExamResult.size()-1;j>i;j--){
                    Map map2 = (Map)myExamResult.get(j);
                    if((ObjectUtils.toString(map1.get("REC_ID")).equals(ObjectUtils.toString(map2.get("REC_ID"))))){
                        myExamResult.remove(j);
                    }
                }
            }

            //匹配学员专业与开考科目
            List<Object> planList = new ArrayList<Object>();
            if(EmptyUtils.isNotEmpty(myExamResult)){
                for(int i=0;i<myExamResult.size();i++){
                    Map tempPlan = (Map) myExamResult.get(i);

                    Map viewMap = new HashMap();
                    viewMap.put("EXAM_BATCH_CODE",ObjectUtils.toString(tempPlan.get("EXAM_BATCH_CODE")));
                    viewMap.put("COURSE_ID",ObjectUtils.toString(tempPlan.get("COURSE_ID")));
                    viewMap.put("TYPE",ObjectUtils.toString(tempPlan.get("KSFS_FLAG")));
                    List viewList= examServeDao.getViewExamPlanByAcadeMy(viewMap);
                    if(EmptyUtils.isNotEmpty(viewList)){
                        for (int j = 0;j<viewList.size();j++){
                            Map viewScMap = (Map)viewList.get(j);
                            if(ObjectUtils.toString(searchParams.get("KKZY")).equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))
                                    || "-1".equals(ObjectUtils.toString(viewScMap.get("SPECIALTY_ID")))){
                                planList.add(viewRecExamPlanMap(tempPlan));
                                break;
                            }
                        }
                    }

                }
            }

            Map<String, Object> tempMap = new LinkedHashMap();
            if(EmptyUtils.isNotEmpty(planList)){
                for(int i=0;i<planList.size();i++){
                    Map temp = (Map) planList.get(i);
                    tempMap.put(ObjectUtils.toString(temp.get("TERM_ID")), ObjectUtils.toString(temp.get("TERM_ID")));
                }
            }

            List<Object> list1 = new ArrayList<Object>();
            if(EmptyUtils.isNotEmpty(planList)){
                for(Object key:tempMap.keySet()){
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    List<Object> course_list = new ArrayList<Object>();

                    for(int i=0;i<planList.size();i++){
                        Map temp = (Map) planList.get(i);
                        if(ObjectUtils.toString(key).equals(ObjectUtils.toString(temp.get("TERM_ID")))){
                            Map<String, Object> course_map = new LinkedHashMap<String, Object>();

                            map.put("TERM_CODE", ObjectUtils.toString(temp.get("TERM_CODE")));
                            map.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
                            map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
                            map.put("START_DATE",ObjectUtils.toString(temp.get("START_DATE")));
                            map.put("END_DATE",ObjectUtils.toString(temp.get("END_DATE")));


                            course_map.put("COURSE_ID",ObjectUtils.toString(temp.get("COURSE_ID")));
                            course_map.put("COURSE_NAME",ObjectUtils.toString(temp.get("COURSE_NAME")));
                            course_map.put("REC_ID", ObjectUtils.toString(temp.get("REC_ID")));
                            course_map.put("CHOOSE_ID", ObjectUtils.toString(temp.get("REC_ID")));
                            course_map.put("TEACH_PLAN_ID", ObjectUtils.toString(temp.get("TEACH_PLAN_ID")));
                            course_map.put("SCORE_STATE",ObjectUtils.toString(temp.get("SCORE_STATE"))); //状态0:未通过;1:已通过;2:学习中;3:登记中
                            course_map.put("KCXXBZ",ObjectUtils.toString(temp.get("KCXXBZ")));
                            course_map.put("KCKSBZ",ObjectUtils.toString(temp.get("KCKSBZ")));
                            course_map.put("EXAM_SCORE",ObjectUtils.toString(temp.get("EXAM_SCORE1")));//形成性成绩
                            course_map.put("EXAM_SCORE1",ObjectUtils.toString(temp.get("EXAM_SCORE"))); //终结性成绩 (形成性成绩/终结性成绩,与PC端公用同一条sql sql取值有字段有修改)
                            course_map.put("EXAM_SCORE2",ObjectUtils.toString(temp.get("EXAM_SCORE2")));//总成绩
                            course_map.put("KCH",ObjectUtils.toString(temp.get("KCH")));
                            course_map.put("KSFS_FLAG",ObjectUtils.toString(temp.get("KSFS_FLAG")));
                            course_map.put("EXAM_TYPE",ObjectUtils.toString(temp.get("EXAM_TYPE")));
                            course_map.put("EXAM_STATE",ObjectUtils.toString(temp.get("EXAM_STATE"))); //0:待考试,1：考试中,2：已结束
                            course_map.put("EXAM_STYLE",ObjectUtils.toString(temp.get("EXAM_STYLE")));
                            course_map.put("EXAM_STIME",ObjectUtils.toString(temp.get("EXAM_STIME")));
                            course_map.put("EXAM_ETIME",ObjectUtils.toString(temp.get("EXAM_ETIME")));
                            course_map.put("EXAM_NO",ObjectUtils.toString(temp.get("EXAM_NO")));
                            course_map.put("EXAM_BATCH_CODE", ObjectUtils.toString(temp.get("EXAM_BATCH_CODE")));
                            course_map.put("EXAM_PLAN_ID", ObjectUtils.toString(temp.get("EXAM_PLAN_ID")));

                            course_list.add(course_map);
                            map.put("EXAMLIST", course_list);
                        }
                    }
                    list1.add(map);
                }
            }
            resultMap.put("LIST", list1);
        }catch(Exception e){
            e.printStackTrace();
        }

        return resultMap;
    }

    public static Map<String,Object> viewRecExamPlanMap(Map viewMap){
        Map resutlMap = new LinkedHashMap();
        try{
            resutlMap.put("TERM_CODE",ObjectUtils.toString(viewMap.get("TERM_CODE")));
            resutlMap.put("TERM_ID",ObjectUtils.toString(viewMap.get("TERM_ID")));
            resutlMap.put("TERM_NAME",ObjectUtils.toString(viewMap.get("TERM_NAME")));
            resutlMap.put("START_DATE",ObjectUtils.toString(viewMap.get("START_DATE")));
            resutlMap.put("END_DATE",ObjectUtils.toString(viewMap.get("END_DATE")));
            resutlMap.put("COURSE_ID",ObjectUtils.toString(viewMap.get("COURSE_ID")));
            resutlMap.put("COURSE_NAME",ObjectUtils.toString(viewMap.get("COURSE_NAME")));
            resutlMap.put("REC_ID", ObjectUtils.toString(viewMap.get("REC_ID")));
            resutlMap.put("CHOOSE_ID", ObjectUtils.toString(viewMap.get("REC_ID")));
            resutlMap.put("TEACH_PLAN_ID", ObjectUtils.toString(viewMap.get("TEACH_PLAN_ID")));
            resutlMap.put("SCORE_STATE",ObjectUtils.toString(viewMap.get("SCORE_STATE"))); //状态0:未通过;1:已通过;2:学习中;3:登记中
            resutlMap.put("KCXXBZ",ObjectUtils.toString(viewMap.get("KCXXBZ")));
            resutlMap.put("KCKSBZ",ObjectUtils.toString(viewMap.get("KCKSBZ")));
            resutlMap.put("EXAM_SCORE",ObjectUtils.toString(viewMap.get("EXAM_SCORE1")));//形成性成绩
            resutlMap.put("EXAM_SCORE1",ObjectUtils.toString(viewMap.get("EXAM_SCORE"))); //终结性成绩 (形成性成绩/终结性成绩,与PC端公用同一条sql sql取值有字段有修改)
            resutlMap.put("EXAM_SCORE2",ObjectUtils.toString(viewMap.get("EXAM_SCORE2")));//总成绩
            resutlMap.put("KCH",ObjectUtils.toString(viewMap.get("KCH")));
            resutlMap.put("KSFS_FLAG",ObjectUtils.toString(viewMap.get("KSFS_FLAG")));
            resutlMap.put("EXAM_TYPE",ObjectUtils.toString(viewMap.get("EXAM_TYPE")));
            resutlMap.put("EXAM_STATE",ObjectUtils.toString(viewMap.get("EXAM_STATE"))); //0:待考试,1：考试中,2：已结束
            resutlMap.put("EXAM_STYLE",ObjectUtils.toString(viewMap.get("EXAM_STYLE")));
            resutlMap.put("EXAM_STIME",ObjectUtils.toString(viewMap.get("EXAM_STIME")));
            resutlMap.put("EXAM_ETIME",ObjectUtils.toString(viewMap.get("EXAM_ETIME")));
            resutlMap.put("EXAM_NO",ObjectUtils.toString(viewMap.get("EXAM_NO")));
            resutlMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(viewMap.get("EXAM_BATCH_CODE")));
            resutlMap.put("EXAM_PLAN_ID", ObjectUtils.toString(viewMap.get("EXAM_PLAN_ID")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return  resutlMap;
    }

    @Override
    public String exportExamAppointmentStudentSituation(final String examBatchCode, Map<String, Object> params, String path) {
        String zipFileName = "statistics.zip";
        List<Map> studentInfos = examNewDaoImpl.getExamStudentList(examBatchCode, params);

        try {
            String[] titles = new String[] { "入学学期", "层次", "专业", "学号", "姓名", "手机号", "学员类型","学籍状态",
                    "学期", "课程号", "课程名称", "替换课课程号", "替换课名称", "学习成绩", "考试科目","试卷号", "预约状态", "预约时间" };
            String[] dbNames = new String[] { "GRADE_NAME", "PYCCNAME", "ZYMC", "XH", "XM", "SJH", "USER_TYPE_NAME", "XJZTNAME",
                    "TERM_NAME", "sourceCourseKch", "sourceCourseKcmc", "courseKch","courseKcmc", "LEARING_EXAM_SCORE", "examPlanName","examNo", "APPOINTMENT_STATE", "APPOINTMENT_DT" };
            String[] titles2 = new String[] { "入学学期", "层次", "专业", "学号", "姓名", "学员类型","学籍状态",
                    "学期", "考试形式", "考点名称" };
            String[] dbNames2 = new String[] { "GRADE_NAME", "PYCCNAME", "ZYMC", "XH", "XM", "USER_TYPE_NAME", "XJZTNAME",
                    "TERM_NAME", "EXAM_TYPE_NAME", "POINT_NAME" };
            String[] titles3 = new String[] { "学员ID", "入学学期", "层次", "专业", "学号", "姓名", "身份证号",
                    "缴费状态" };
            String[] dbNames3 = new String[] { "STUDENT_ID", "GRADE_NAME", "PYCCNAME", "ZYMC", "XH", "XM", "SFZH",
                    "PAY_STATE" };
            String[] titles4 = new String[] { "学员ID", "入学学期", "层次", "专业", "学号", "姓名", "身份证号",
                    "课程号", "课程名称", "纠正前EXAM_PLAN_ID", "纠正前试卷号", "纠正后EXAM_PLAN_ID" };
            String[] dbNames4 = new String[] { "STUDENT_ID", "GRADE_NAME", "PYCCNAME", "ZYMC", "XH", "XM", "SFZH",
                    "KCH", "KCMC", "Q_EXAM_PLAN_ID", "Q_EXAM_NO", "H_EXAM_PLAN_ID" };

            final int nThreads = Runtime.getRuntime().availableProcessors() * 8;
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            final CountDownLatch complete = new CountDownLatch(studentInfos.size());
            long startTime = System.currentTimeMillis();

            final Set<String> yingNum = new ConcurrentSkipListSet<String>();
            final Set<String> yiNum = new ConcurrentSkipListSet<String>();
            final List<Map> examList = new CopyOnWriteArrayList<Map>(); // 总=未预约+已预约
            final List<Map> updateExamPlanList = new CopyOnWriteArrayList<Map>();
            final List<Map> waitPayList = new CopyOnWriteArrayList<Map>();
            final List<Map> pointList = new CopyOnWriteArrayList<Map>();
            for (int i = 0; i < studentInfos.size(); i++) {
                final Map<String, String> info = studentInfos.get(i);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String studentId = info.get("STUDENT_ID");
                        Map searchParams = new HashMap();
                        searchParams.put("EXAM_BATCH_CODE", examBatchCode);
                        searchParams.put("STUDENT_ID", studentId);
                        searchParams.put("USER_TYPE", info.get("USER_TYPE"));
                        searchParams.put("username", info.get("SFZH"));
                        searchParams.put("XJZT", info.get("XJZT"));
                        searchParams.put("KKZY", info.get("MAJOR"));
                        searchParams.put("XX_ID", info.get("XX_ID"));
                        searchParams.put("condition", "1");
                        // 预约列表
                        Map examResultMap = queryAppointmentExam(searchParams);

                        // 获取未预约的学员
                        boolean waitPayFlag = false;
                        List<Map> termList = (List<Map>) examResultMap.get("LIST");
                        if (termList != null && termList.size() > 0) {
                            for (Map term : termList) {
                                List<Map> appointmentList = (List<Map>) term.get("APPOINTMENTLIST");
                                if (appointmentList != null && appointmentList.size() > 0) {
                                    for (Map appointment : appointmentList) {
                                    	if(Constants.BOOLEAN_0.equals(appointment.get("BESPEAK_STATE"))) {
	                                        Map exam = new HashMap();
	                                        exam.put("GRADE_NAME", ObjectUtils.toString(info.get("GRADE_NAME"), ""));
	                                        exam.put("PYCCNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, info.get("PYCC")), ""));
	                                        exam.put("ZYMC", ObjectUtils.toString(info.get("ZYMC"), ""));
	                                        exam.put("XH", ObjectUtils.toString(info.get("XH"), ""));
	                                        exam.put("XM", ObjectUtils.toString(info.get("XM"), ""));
	                                        exam.put("SJH", ObjectUtils.toString(info.get("SJH"), ""));
	                                        exam.put("USER_TYPE_NAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, info.get("USER_TYPE")), ""));
	                                        exam.put("XJZTNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, info.get("XJZT")), ""));
	
	                                        exam.put("TERM_NAME", ObjectUtils.toString(term.get("TERM_NAME"), ""));
	                                        exam.put("KCH", ObjectUtils.toString(appointment.get("KCH"), ""));
	                                        exam.put("KCMC", ObjectUtils.toString(appointment.get("COURSE_NAME"), ""));
                                            exam.put("APPOINTMENT_STATE", "未预约");
                                            exam.put("APPOINTMENT_DT", "");
                                            exam.put("LEARING_EXAM_SCORE", ObjectUtils.toString(appointment.get("NEW_EXAM_SCORE"), ""));
                                            exam.put("sourceCourseKch", ObjectUtils.toString(appointment.get("sourceCourseKch")));
                                            exam.put("sourceCourseKcmc", ObjectUtils.toString(appointment.get("sourceCourseKcmc")));
                                            exam.put("courseKch", ObjectUtils.toString(appointment.get("courseKch"), ""));
                                            exam.put("courseKcmc", ObjectUtils.toString(appointment.get("courseKcmc"), ""));
                                            String examPlanId = (String) appointment.get("EXAM_PLAN_ID");
                                            GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(examPlanId);
                                            exam.put("examPlanName", ObjectUtils.toString(examPlanNew.getExamPlanName(), ""));
                                            exam.put("examNo", ObjectUtils.toString(examPlanNew.getExamNo(), ""));
                                            examList.add(exam);
                                        } else {
                                        	GjtExamAppointmentNew appointmentNew = gjtExamAppointmentNewRepository.findStudentExamAppointment(examBatchCode, studentId, (String) appointment.get("REC_ID"));
                                            if (appointmentNew != null) {
                                                // 纠正EXAM_PLAN_ID
                                                String examPlanId = (String) appointment.get("EXAM_PLAN_ID");
                                                if(StringUtils.isNotBlank(examPlanId) && !StringUtils.equals(examPlanId, appointmentNew.getExamPlanId())) {
                                                    String oldExamPlanId = appointmentNew.getExamPlanId();
                                                    GjtExamPlanNew examPlan = gjtExamPlanNewService.queryById(oldExamPlanId);
                                                    String oldExamNo = examPlan.getExamNo();
                                                    appointmentNew.setExamPlanId(examPlanId);
                                                    appointmentNew.setUpdatedDt(new Date());
                                                    appointmentNew.setUpdatedBy("20171106纠正科目ID");
                                                    gjtExamAppointmentNewRepository.save(appointmentNew);

                                                    Map updateExamPlanMap = new HashMap();
                                                    updateExamPlanMap.put("STUDENT_ID", studentId);
                                                    updateExamPlanMap.put("GRADE_NAME", ObjectUtils.toString(info.get("GRADE_NAME"), ""));
                                                    updateExamPlanMap.put("PYCCNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, info.get("PYCC")), ""));
                                                    updateExamPlanMap.put("ZYMC", ObjectUtils.toString(info.get("ZYMC"), ""));
                                                    updateExamPlanMap.put("XH", ObjectUtils.toString(info.get("XH"), ""));
                                                    updateExamPlanMap.put("XM", ObjectUtils.toString(info.get("XM"), ""));
                                                    updateExamPlanMap.put("SFZH", ObjectUtils.toString(info.get("SFZH"), ""));

                                                    updateExamPlanMap.put("KCH", ObjectUtils.toString(appointment.get("KCH"), ""));
                                                    updateExamPlanMap.put("KCMC", ObjectUtils.toString(appointment.get("COURSE_NAME"), ""));
                                                    updateExamPlanMap.put("Q_EXAM_PLAN_ID", ObjectUtils.toString(oldExamPlanId, ""));
                                                    updateExamPlanMap.put("Q_EXAM_NO", ObjectUtils.toString(oldExamNo, ""));
                                                    updateExamPlanMap.put("H_EXAM_PLAN_ID", ObjectUtils.toString(examPlanId, ""));
                                                    updateExamPlanList.add(updateExamPlanMap);
                                                }
                                            }
                                        }

                                        // 待缴费的学员
                                        if(Constants.BOOLEAN_0.equals(appointment.get("PAY_STATE"))) {
                                            waitPayFlag = true;
                                        }
                                        
                                        yingNum.add(studentId); // 应该预约人数
                                    }
                                }
                            }
                        }

                        // 我的考试
                        Map searchParams2 = new HashMap();
                        searchParams2.put("EXAM_BATCH_CODE", examBatchCode);
                        searchParams2.put("STUDENT_ID", studentId);
                        List<Map> myExamList = examNewDaoImpl.myExamList(searchParams2);
                        for (Map appointment : myExamList) {
                            Map exam = new HashMap();
                            exam.put("GRADE_NAME", ObjectUtils.toString(info.get("GRADE_NAME"), ""));
                            exam.put("PYCCNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, info.get("PYCC")), ""));
                            exam.put("ZYMC", ObjectUtils.toString(info.get("ZYMC"), ""));
                            exam.put("XH", ObjectUtils.toString(info.get("XH"), ""));
                            exam.put("XM", ObjectUtils.toString(info.get("XM"), ""));
                            exam.put("SJH", ObjectUtils.toString(info.get("SJH"), ""));
                            exam.put("USER_TYPE_NAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, info.get("USER_TYPE")), ""));
                            exam.put("XJZTNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, info.get("XJZT")), ""));

                            exam.put("TERM_NAME", ObjectUtils.toString(appointment.get("TERM_NAME"), ""));
                            exam.put("KCH", ObjectUtils.toString(appointment.get("KCH"), ""));
                            exam.put("KCMC", ObjectUtils.toString(appointment.get("COURSE_NAME"), ""));
                            GjtExamAppointmentNew appointmentNew = gjtExamAppointmentNewRepository.findStudentExamAppointment(examBatchCode, studentId, (String) appointment.get("REC_ID"));
                            if (appointmentNew != null) {
                                exam.put("APPOINTMENT_STATE", "已预约");
                                exam.put("APPOINTMENT_DT", DateUtil.toString(appointmentNew.getCreatedDt(), "yyyy-MM-dd HH:mm:ss"));
                                GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(appointmentNew.getExamPlanId());
                                exam.put("examPlanName", ObjectUtils.toString(examPlanNew.getExamPlanName(), ""));
                                exam.put("examNo", ObjectUtils.toString(examPlanNew.getExamNo(), ""));
                                yiNum.add(studentId);
                            } else {
                                exam.put("APPOINTMENT_STATE", "未预约");
                                exam.put("APPOINTMENT_DT", "");
                            }
                            exam.put("LEARING_EXAM_SCORE", ObjectUtils.toString(appointment.get("NEW_EXAM_SCORE"), ""));
                            exam.put("sourceCourseKch", ObjectUtils.toString(appointment.get("sourceCourseKch")));
                            exam.put("sourceCourseKcmc", ObjectUtils.toString(appointment.get("sourceCourseKcmc")));
                            exam.put("courseKch", ObjectUtils.toString(appointment.get("courseKch"), ""));
                            exam.put("courseKcmc", ObjectUtils.toString(appointment.get("courseKcmc"), ""));
                            examList.add(exam);
                        }
                        
                        // 没订单号的学员
                        if(waitPayFlag) {
                            Map<String, Object> signInfo = examServeDao.getOrderSn(studentId);
                            if(signInfo != null && StringUtils.isBlank((String) signInfo.get("ORDER_SN"))) {
                                Map waitPay = new HashMap();
                                waitPay.put("STUDENT_ID", studentId);
                                waitPay.put("GRADE_NAME", ObjectUtils.toString(info.get("GRADE_NAME"), ""));
                                waitPay.put("PYCCNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, info.get("PYCC")), ""));
                                waitPay.put("ZYMC", ObjectUtils.toString(info.get("ZYMC"), ""));
                                waitPay.put("XH", ObjectUtils.toString(info.get("XH"), ""));
                                waitPay.put("XM", ObjectUtils.toString(info.get("XM"), ""));
                                waitPay.put("SFZH", ObjectUtils.toString(info.get("SFZH"), ""));

                                waitPay.put("PAY_STATE", "待缴费");
                                waitPayList.add(waitPay);
                            }
                        }
                        // 考点预约
                        List<Map> pList = (List<Map>) examResultMap.get("POINTLIST");
                        if (pList != null && pList.size() > 0) {
                            for (Map point : pList) {
                                if(point.get("EXAM_POINT_ID") != null) {
                                    Map exam = new HashMap();
                                    exam.put("GRADE_NAME", ObjectUtils.toString(info.get("GRADE_NAME"), ""));
                                    exam.put("PYCCNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, info.get("PYCC")), ""));
                                    exam.put("ZYMC", ObjectUtils.toString(info.get("ZYMC"), ""));
                                    exam.put("XH", ObjectUtils.toString(info.get("XH"), ""));
                                    exam.put("XM", ObjectUtils.toString(info.get("XM"), ""));
                                    exam.put("USER_TYPE_NAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, info.get("USER_TYPE")), ""));
                                    exam.put("XJZTNAME", ObjectUtils.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, info.get("XJZT")), ""));

                                    if ("8".equals(point.get("EXAM_TYPE"))) {
                                        exam.put("EXAM_TYPE_NAME", "笔试");
                                    } else if ("11".equals(point.get("EXAM_TYPE"))) {
                                        exam.put("EXAM_TYPE_NAME", "机考");
                                    }
                                    exam.put("POINT_NAME", point.get("POINT_NAME"));
                                    pointList.add(exam);
                                }
                            }
                        }
                        complete.countDown();
                        log.error("exportExamAppointmentStudentSituation <br/>thread size: " + nThreads + ", not execute size: " + complete.getCount() + ".");
                    }
                });
            }

            try {
                // 等待完成，设置超时时间25分钟
                complete.await(25, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
            long endTime = System.currentTimeMillis();
            log.error("exportExamAppointmentStudentSituation <br/>thread size: " + nThreads + ", time consuming: " + (endTime - startTime) + "ms, student size: " + studentInfos.size() + ", not execute size: " + complete.getCount() + ".");

            // excel
            String folderName = "考试预约数据统计表-是否完整导完_"+(complete.getCount() == 0 ? "是" : "否") + "-----应预约人数"+yingNum.size()+"-已预约人数"+yiNum.size()+"_"+System.currentTimeMillis();
            int page = 1, pageSize = 65500;
            while(((page - 1)*pageSize) < examList.size()) { // 是否有下一页
                List<Map> examNewList = new CopyOnWriteArrayList<Map>();
                for(int i = (page - 1)*pageSize; i < page*pageSize && i < examList.size(); i++) { // 当前页数据
                	examNewList.add(examList.get(i));
                }
                String fileName = ExcelService.renderExcel(examNewList, titles, dbNames, "预约课程情况", path + folderName + File.separator, "预约课程情况" + page);
                page++;
            }
            String fileName2 = ExcelService.renderExcel(pointList, titles2, dbNames2, "预约考点情况", path + folderName + File.separator, "预约考点情况");
            String fileName3 = ExcelService.renderExcel(waitPayList, titles3, dbNames3, "待缴费无报读订单号学员列表", path + folderName + File.separator, "待缴费无报读订单号学员列表");
            String fileName4 = ExcelService.renderExcel(updateExamPlanList, titles4, dbNames4, "预约科目有误预约记录列表", path + folderName + File.separator, "预约科目有误预约记录列表");

            // zip
            ZipFileUtil.zipDir(path + folderName, path + zipFileName);
            FileKit.delFile(path + folderName);
            return zipFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    @Transactional
    public int saveStudentDownToken(Map formMap){
    	return examServeDao.saveStudentDownToken(formMap);
    }

	@Override
	@Transactional
	public int updateExamScore(Map<String, Object> searchParams) {
		try{
			int num= 0;
			List studentList = examNewDaoImpl.getNoExamScoreList(searchParams);
			if(EmptyUtils.isNotEmpty(studentList) && studentList.size() > 0){
				String url = AppConfig.getProperty("examServer") + "/api/getExamResult.do";
				for(int i = 0;i < studentList.size();i++){
					Map<String,Object> param = new HashMap<String,Object>();
					Map<String,Object> formMap = new HashMap<String,Object>();
					Map m = (Map) studentList.get(i);
					param.put("examNo", m.get("EXAM_NO"));
					param.put("examBatchCode", m.get("EXAM_BATCH_CODE"));
					param.put("userNo", m.get("XH"));
					param.put("userNum", m.get("SFZH"));
					formMap.put("APPOINTMENT_ID", m.get("APPOINTMENT_ID"));
					String jsonStr = HttpClientUtils.doHttpPost(url, param, 60 * 1000, "UTF-8");
					if(EmptyUtils.isNotEmpty(jsonStr)){
						JSONObject jsonObj = new JSONObject(jsonStr);
						if("200".equals(jsonObj.get("code").toString()) && jsonObj.has("point")){
							if(EmptyUtils.isNotEmpty(jsonObj.get("point"))){
								double point = Double.parseDouble(jsonObj.get("point").toString());
								if(point >= 60){
									formMap.put("EXAM_STATUS", "1");
								}else{
									formMap.put("EXAM_STATUS", "2");
								}
								formMap.put("EXAM_SCORE", point);
								formMap.put("EXAM_COUNT", jsonObj.get("count"));
								num = num + examServeDao.updateExamScore(formMap);
							}
						}
					}
				}
			}
			return num;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	/** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */
	private static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }

}
