package com.gzedu.xlims.serviceImpl.exam;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.ExcelReader;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.exam.GjtExamSubjectCourseDao;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamSubjectCourseService;

import net.sf.jxls.transformer.XLSTransformer;

@Service
public class GjtExamSubjectCourseServiceImpl implements GjtExamSubjectCourseService {
	
	@Autowired
	private CodeGeneratorService codeGeneratorService;
	
	@Autowired
	private GjtExamSubjectCourseDao gjtExamSubjectCourseDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Page getGjtCourseList(Map<String, Object> searchParams, PageRequest pageRequest) {
		return gjtExamSubjectCourseDao.getGjtCourseList(searchParams, pageRequest);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map saveExamSubjectCourse(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			Map subjectMap = new HashMap();
			String subject_id = SequenceUUID.getSequence();
			String subject_code = ObjectUtils.toString(searchParams.get("entity_subjectCode"));
			subjectMap.put("SUBJECT_ID", subject_id);
			subjectMap.put("SUBJECT_CODE", subject_code);
			subjectMap.put("NAME", ObjectUtils.toString(searchParams.get("entity_name")));
			if (Integer.parseInt(ObjectUtils.toString(searchParams.get("entity_type"))) != WebConstants.EXAM_WK_TYPE 
					&& Integer.parseInt(ObjectUtils.toString(searchParams.get("entity_type"))) != WebConstants.EXAM_XK_TYPE
					&& Integer.parseInt(ObjectUtils.toString(searchParams.get("entity_type"))) != WebConstants.EXAM_WKS_TYPE) {
				subjectMap.put("STATUS", "3");
			} else {
				subjectMap.put("STATUS", "1");
			}
			subjectMap.put("TYPE", ObjectUtils.toString(searchParams.get("entity_type")));
			subjectMap.put("EXAM_NO", ObjectUtils.toString(searchParams.get("entity_examNo")));
			subjectMap.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
			subjectMap.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
			
			int i = gjtExamSubjectCourseDao.insertExamSubject(subjectMap);
			if(i==1){
				Map courseMap = new HashMap();
				String subjectCode = ObjectUtils.toString(searchParams.get("entity_subjectCode")).trim();
				if(searchParams.get("COURSE_"+subjectCode) instanceof String[]){
					String[] course_ids = (String[]) searchParams.get("COURSE_"+subjectCode);
					String[] kch_ids = (String[])searchParams.get("kch_"+subjectCode);
					if(EmptyUtils.isNotEmpty(course_ids)){
						for(int j=0;j<course_ids.length;j++){
							courseMap.put("SUBJECT_COURSE_ID", SequenceUUID.getSequence());
							courseMap.put("COURSE_ID", ObjectUtils.toString(course_ids[j]).trim());
							courseMap.put("SUBJECT_ID", subject_id);
							courseMap.put("SUBJECT_CODE", subject_code);
							courseMap.put("SUBJECT_TYPE", ObjectUtils.toString(searchParams.get("entity_type")));
							courseMap.put("KCH", ObjectUtils.toString(kch_ids[j]).trim());
							courseMap.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
							courseMap.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
							courseMap.put("EXAM_NO", ObjectUtils.toString(searchParams.get("entity_examNo")));
							gjtExamSubjectCourseDao.insertExamSubjectCourse(courseMap);
						}
					}
				}else{
					String course_id = ObjectUtils.toString(searchParams.get("COURSE_"+subjectCode));
					String kch_id = ObjectUtils.toString(searchParams.get("kch_"+subjectCode));
					if(EmptyUtils.isNotEmpty(course_id)){
						courseMap.put("SUBJECT_COURSE_ID", SequenceUUID.getSequence());
						courseMap.put("COURSE_ID", course_id);
						courseMap.put("SUBJECT_ID", subject_id);
						courseMap.put("SUBJECT_CODE", subject_code);
						courseMap.put("SUBJECT_TYPE", ObjectUtils.toString(searchParams.get("entity_type")));
						courseMap.put("KCH", kch_id);
						courseMap.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
						courseMap.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
						courseMap.put("EXAM_NO", ObjectUtils.toString(searchParams.get("entity_examNo")));
						gjtExamSubjectCourseDao.insertExamSubjectCourse(courseMap);	
					}
				}
			}
			resultMap.put("result", ObjectUtils.toString(i));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getIsExistExamNo(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			searchParams.put("EXAM_NO", ObjectUtils.toString(searchParams.get("entity_examNo")));
			searchParams.put("TYPE", ObjectUtils.toString(searchParams.get("entity_type")));
			List list = gjtExamSubjectCourseDao.getIsExistExamNo(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				resultMap = (Map) list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public String exportExamSubjectCourse(Map searchParams, HttpServletRequest request, HttpServletResponse response) {
		String fileName = GjtExamSubjectCourseServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/temp/未创建考试科目的课程目录表"+new Date().getTime()+".xls";
		try{
			List courseList = gjtExamSubjectCourseDao.noSubjectCourseList(searchParams);
			if(EmptyUtils.isNotEmpty(courseList)){
				Map beans = new HashMap();
				beans.put("dataList", courseList);
				XLSTransformer transformer = new XLSTransformer();
				transformer.transformXLS(GjtExamSubjectCourseServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/未创建考试科目的课程目录表_new.xls", beans, fileName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 导入考试课程
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Transactional
	public Map importCourseSubject(String filePaths, Map searchParams, HttpServletRequest request,
			HttpServletResponse response) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try{
			String userId = ObjectUtils.toString(searchParams.get("USER_ID"));
			int examType = Integer.parseInt(ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			String[] titles = {"kch", "courseName", "subjectName", "examNo", "memo"};
			File file = new File(filePaths);
			ExcelReader reader = new ExcelReader(file, titles, 3);
			List list = reader.readAsList();
			if(EmptyUtils.isNotEmpty(list)){
				for(Object object:list){
					Map errorInfo = new HashMap();
					String errorstr = "";
					int errorNum = 1;
					
					Map tempMap = (Map<String, Object>) object;
					tempMap.put("xxId", xxId);
					tempMap.put("examType", examType);
					String kch = ObjectUtils.toString(tempMap.get("kch")).trim();
					String courseName = ObjectUtils.toString(tempMap.get("courseName")).trim();
					String subjectName = ObjectUtils.toString(tempMap.get("subjectName")).trim();
					String examNo = ObjectUtils.toString(tempMap.get("examNo")).trim();
					String memo = ObjectUtils.toString(tempMap.get("memo")).trim();
					String courseid = "";
					
					if(EmptyUtils.isNotEmpty(kch) || EmptyUtils.isNotEmpty(courseName)){
						if (EmptyUtils.isEmpty(kch)) {
							errorstr += "课程号不能为空，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(courseName)) {
							errorstr += "课程名称不能为空，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(subjectName)) {
							errorstr += "科目名称不能为空，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(examNo)) {
							errorstr += "试卷号不能为空，";
	                        errorNum++;
						}
						
						if (memo.length()>200) {
							errorstr += "标签说明不能大于100字，";
	                        errorNum++;
						}
						
						List subList = gjtExamSubjectCourseDao.getCourseExamNo(tempMap);
						if(EmptyUtils.isNotEmpty(subList)){
							Map subMap = (Map)subList.get(0);
							courseid = ObjectUtils.toString(subMap.get("COURSE_ID"));
							int subject_count = Integer.parseInt(ObjectUtils.toString(subMap.get("SUBJECT_COUNT")));
							String subject_code = ObjectUtils.toString(subMap.get("SUBJECT_CODE"));
							if (EmptyUtils.isNotEmpty(subject_code)) {
								errorstr += "试卷号已经关联其它科目，";
		                        errorNum++;
							}
//							if (subject_count>0) {
//								errorstr += "该课程已经创建考试科目，";
//		                        errorNum++;
//							} else if (EmptyUtils.isNotEmpty(subject_code)) {
//								errorstr += "试卷号已经关联其它科目，";
//		                        errorNum++;
//							}
						}else{
							errorstr += "该课程不存在，";
	                        errorNum++;
						}
						
						if(EmptyUtils.isEmpty(errorstr)){
							Date now = new Date();
							Map subjectMap = new HashMap();
							String subject_id = SequenceUUID.getSequence();
							String subject_code = ObjectUtils.toString(this.getSubjectCode(examType));
							subjectMap.put("SUBJECT_ID", subject_id);
							subjectMap.put("SUBJECT_CODE", subject_code);
							subjectMap.put("NAME", subjectName);
							subjectMap.put("TYPE", examType);
							subjectMap.put("EXAM_NO", examNo);
							subjectMap.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
							subjectMap.put("CREATED_BY", userId);
							
							if (examType != WebConstants.EXAM_WK_TYPE 
									&& examType != WebConstants.EXAM_XK_TYPE
									&& examType != WebConstants.EXAM_WKS_TYPE) {
								subjectMap.put("STATUS", "3");
							} else {
								subjectMap.put("STATUS", "1");
							}
							
							int  i = gjtExamSubjectCourseDao.insertExamSubject(subjectMap);
							if(i==1){
								Map courseMap = new HashMap();
								courseMap.put("SUBJECT_COURSE_ID", SequenceUUID.getSequence());
								courseMap.put("COURSE_ID", courseid);
								courseMap.put("SUBJECT_ID", subject_id);
								courseMap.put("SUBJECT_CODE", subject_code);
								courseMap.put("SUBJECT_TYPE", examType);
								courseMap.put("KCH", kch);
								courseMap.put("CREATED_BY", userId);
								courseMap.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
								courseMap.put("EXAM_NO", examNo);
								gjtExamSubjectCourseDao.insertExamSubjectCourse(courseMap);
								rightList.add(tempMap);
							}else{
								tempMap.put("errorstr", "导入异常");
								errorList.add(tempMap);
							}
						}else{
							tempMap.put("errorstr", errorstr);
							errorList.add(tempMap);
						}
					}
				}
			}
			
			String timeStr = ObjectUtils.toString(new Date().getTime());
			
			// 导出成功记录
			String tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/未创建考试科目的课程目录成功表.xls";
			String downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/temp/未创建考试科目的课程目录成功表"+timeStr+".xls";
			Map beans = new HashMap();
		    beans.put("dataList", rightList);
		    XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("RIGHT_DOWNPATH", downPath);
			
            // 导出失败记录
            tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/未创建考试科目的课程目录失败表.xls";
			downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/temp/未创建考试科目的课程目录失败表"+timeStr+".xls";
            beans = new HashMap();
            beans.put("dataList", errorList);
            transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("ERROR_DOWNPATH", downPath);
			
		}catch (Exception e) {
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
	 * 考试科目编码规则:默认在前面加上考试类型缩写
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public String getSubjectCode(int type) {
		String prefix = "BS";
		switch (type) {
		case 1:
			prefix = "WK";
			break;
		case 2:
			prefix = "BS";
			break;
		case 3:
			prefix = "JK";
			break;
		case 4:
			prefix = "XK";
			break;
		case 5:
			prefix = "WK";
			break;
		case 6:
			prefix = "GK";
			break;
		case 7:
			prefix = "SK";
			break;
		case 8:
			prefix = "LW";
			break;
		case 9:
			prefix = "BG";
			break;
		case 10:
			prefix = "ZY";
			break;
		case 11:
			prefix = "LG";
			break;
		default:
			break;
		}
		return prefix + codeGeneratorService.codeGenerator(CacheConstants.SUBJECT_CODE);
	}

}
