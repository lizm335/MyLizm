/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.common.task;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.dao.exam.GjtExamRecordNewDao;
import com.gzedu.xlims.dao.studymanage.StudyManageDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookDistributeRepository;
import com.gzedu.xlims.daoImpl.StudentInfoDaoImpl;
import com.gzedu.xlims.pojo.dto.SynthesizeInfoDto;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;

/**
 *学员综合信息定时器<br/>
 * 功能说明：<br/>
 *		Schedule定时任务<br/>
 *		cron表达式，有专门的语法，而且感觉有点绕人，不过简单来说，大家记住一些常用的用法即可，特殊的语法可以单独去查。<br/>
 *		cron一共有7位，但是最后一位是年，可以留空，所以我们可以写6位：<br/>
 *		* 第一位，表示秒，取值0-59<br/>
 * 		第二位，表示分，取值0-59<br/>
 * 		第三位，表示小时，取值0-23<br/>
 * 		第四位，日期天/日，取值1-31<br/>
 * 		第五位，日期月份，取值1-12<br/>
 * 		第六位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思 另外：1表示星期天，2表示星期一。<br/>
 * 		第7为，年份，可以留空，取值1970-2099<br/>
 *
 *		cron中，还有一些特殊的符号，含义如下：<br/>
 *		(*)星号：可以理解为每的意思，每秒，每分，每天，每月，每年...<br/>
 *		(?)问号：问号只能出现在日期和星期这两个位置，表示这个位置的值不确定，每天3点执行，所以第六位星期的位置，我们是不需要关注的，就是不确定的值。同时：日期和星期是两个相互排斥的元素，通过问号来表明不指定值。比如，1月10日，比如是星期1，如果在星期的位置是另指定星期二，就前后冲突矛盾了。<br/>
 *		(-)减号：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12<br/>
 *		(,)逗号：表达一个列表值，如在星期字段中使用“1,2,4”，则表示星期一，星期二，星期四<br/>
 *		(/)斜杠：如：x/y，x是开始值，y是步长，比如在第一位（秒） 0/15就是，从0秒开始，每15秒，最后就是0，15，30，45，60    另：*\/y，等同于0/y<br/>
 *
 *		cron中，还有一些特殊的符号，含义如下：<br/>
 *		0 0 3 * * ?     每天3点执行<br/>
 *		0 5 3 * * ?     每天3点5分执行<br/>
 *		0 5 3 ? * *     每天3点5分执行，与上面作用相同<br/>
 *		0 5/10 3 * * ?  每天3点的 5分，15分，25分，35分，45分，55分这几个时间点执行<br/>
 *		0 10 3 ? * 1    每周星期天，3点10分 执行，注：1表示星期天<br/>
 *		0 10 3 ? * 1#3  每个月的第三个星期，星期天 执行，#号只能出现在星期的位置<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年02月25日
 * @version 3.0
 */
@Component
@EnableScheduling
public class StudentSynthesizeTask {

	private static final Logger log = LoggerFactory.getLogger(StudentSynthesizeTask.class);
	
	private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	@Autowired
	private StudentInfoDaoImpl studentInfoDaoImpl;

	@Autowired
	private GjtTextbookDistributeRepository gjtTextbookDistributeRepository;

	@Autowired
	private StudyManageDao studyManageDao;

	@Autowired
	private GjtExamRecordNewDao gjtExamRecordNewDao;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 学员综合信息定时器<br/>
	 * 每天凌晨1点触发一次
	 */
	@Scheduled(cron = "0 0 1 * * ?")
//	@Scheduled(initialDelay = 5000, fixedRate = 24 * 60 * 60 * 1000) // 测试时用
	public void task() {
		final Date now = new Date();
		String dateStr = DateUtils.getStringToDate(now);
		log.error("------------ 这是学员综合信息定时器，当前执行一次，当前时间 StudentSynthesizeTask ------------" + dateStr);

		long startTime = System.currentTimeMillis();
		int count = 0;
		
		// 缓存热点 key 重建优化，防止tomcat集群服务重复处理
		Boolean flag = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				RedisSerializer stringRedisSerializer = redisTemplate.getKeySerializer(); // 配置文件配置的StringRedisSerializer
				RedisSerializer jdkSerializationRedisSerializer = redisTemplate.getValueSerializer(); // 配置文件配置的JdkSerializationRedisSerializer
				byte[] key = stringRedisSerializer.serialize("StudentSynthesizeTask");
				byte[] value = jdkSerializationRedisSerializer.serialize("1");
				return redisConnection.setNX(key, value);
			}

		});

		if(flag) {
			// 如果加锁成功，设置个有效时间
			String key = "StudentSynthesizeTask";
			redisTemplate.expire(key, 30, TimeUnit.MINUTES);
			// 开始执行任务

			Map<String, Object> searchParams = new HashMap<String, Object>();
//			// 测试时用
//			List<String> sfzh = new ArrayList<String>();
//			sfzh.add("440981199806112520");
//			searchParams.put("IN_sfzh", sfzh);
//			// ./end 测试时用
			List<Map> studentInfos = studentInfoDaoImpl.findStudentSignupInfoBy(searchParams);

			for (int i = 0; i < studentInfos.size(); i++) {
				count++;
				final Map info = studentInfos.get(i);
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						String studentId = (String) info.get("STUDENT_ID");

						studentInfoDaoImpl.deleteTempStudentSynthesize(studentId);
						SynthesizeInfoDto dto = new SynthesizeInfoDto();
						// 学籍状态
						dto.setRollStatus(Constants.BOOLEAN_0.equals(info.get("AUDIT_STATE")) ? 2 : Constants.BOOLEAN_0.equals(info.get("XJZT")) || "3".equals(info.get("XJZT")) ? 3 : 1);
						// 缴费状态
						dto.setPaymentStatus(Constants.BOOLEAN_0.equals(info.get("CHARGE")) || Constants.BOOLEAN_1.equals(info.get("CHARGE")) ? 1 : "2".equals(info.get("CHARGE")) ? 2 : "3".equals(info.get("CHARGE")) ? 3 : 2);
						// 教材状态
						Long waitCount = gjtTextbookDistributeRepository.countByStudentIdAndIsDeletedAndStatusIn(studentId, Constants.BOOLEAN_NO, Arrays.asList(new Integer[]{1, 2}));
						dto.setTextbookStatus(waitCount == 0 ? 1 : 2);
						// 学习状态
						Map searchParams = new HashMap();
						searchParams.put("STUDENT_ID", studentId);
						searchParams.put("XX_ID", info.get("XX_ID"));
						searchParams.put("STUDY_STATUS", "1");
						long behindCount = studyManageDao.getStudentCourseCount(searchParams);
						dto.setStudyStatus(behindCount == 0 ? 1 : 2);
						// 考勤状态
						dto.setClockingStatus(1);
						dto.setIsOnline(Constants.BOOLEAN_NO); // 初始数据
						Map searchParams2 = new HashMap();
						searchParams2.put("STUDENT_ID", studentId);
						searchParams2.put("XX_ID", info.get("XX_ID"));
						List<Map<String, String>> studentLoginList = studyManageDao.getStudentLoginList(searchParams2);
						if(studentLoginList != null && studentLoginList.size() > 0) {
							Map<String, String> studentLogin = studentLoginList.get(0);

							String device = ObjectUtils.toString(studentLogin.get("DEVICE"));
							String isOnline = ObjectUtils.toString(studentLogin.get("IS_ONLINE"));
							int loginTimes = NumberUtils.toInt(ObjectUtils.toString(studentLogin.get("LOGIN_TIMES")));
							int lastLoginTime = NumberUtils.toInt(ObjectUtils.toString(studentLogin.get("LAST_LOGIN_TIME")));
							if ("Y".equals(isOnline)) {
								dto.setClockingStatus(1);
								dto.setIsOnline(Constants.BOOLEAN_YES);
								dto.setDevice(device);
							} else if ("N".equals(isOnline)) {
								if (loginTimes == 0) {
									dto.setClockingStatus(5);
								} else {
									if (lastLoginTime <= 3) {
										dto.setClockingStatus(2);
									} else if (lastLoginTime <= 7) {
										dto.setClockingStatus(3);
									} else {
										dto.setClockingStatus(4);
									}
								}
							} else if ("".equals(isOnline)) {
								dto.setClockingStatus(5);
							}
							dto.setLoginTimes(loginTimes);
							dto.setLastLoginTime(lastLoginTime);
						}
						// 考试状态
						dto.setExamStatus(1); // 初始数据
						Map searchParams3 = new HashMap();
						searchParams3.put("STUDENT_ID", studentId);
						searchParams3.put("XX_ID", info.get("XX_ID"));
						// 默认选择当前期(批次)
						String code = commonMapService.getCurrentGjtExamBatchNew((String) info.get("XX_ID"));
						searchParams3.put("examBatchCode", code);
						List<Map<String, Object>> studentExamList = gjtExamRecordNewDao.getStudentExamList(searchParams3);
						if(studentExamList != null && studentExamList.size() > 0) {
							Map<String, Object> studentExam = studentExamList.get(0);

							int status;
							// 获取预约时间
							Date now = new Date();
							Date bookSt = (Date) studentExam.get("BOOK_ST");
							Date bookEnd = (Date) studentExam.get("BOOK_END");
							Date booksSt = (Date) studentExam.get("BOOKS_ST");
							Date booksEnd = (Date) studentExam.get("BOOKS_END");
							BigDecimal shouldExamCount = (BigDecimal) studentExam.get("SHOULD_EXAM_COUNT");
							BigDecimal makeCount = (BigDecimal) studentExam.get("MAKE_COUNT");
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
								status = 1;
							} else {
								if(now.getTime() < bookSt.getTime()) {
									status = 1;
								} else if(makeCount.intValue() >= 8) { // 合并2.2和2.3的已约满8科 ===→ 异常，已约满，需下次再约
									status = 4;
								} else {
									if(now.getTime() >= bookSt.getTime() && now.getTime() <= bookEnd.getTime() || (booksEnd != null && now.getTime() >= booksSt.getTime() && now.getTime() <= booksEnd.getTime())) {
										status = 2;
									} else {
										status = 3;
									}
								}
							}
							dto.setExamStatus(status);
						}
						// 论文状态
						dto.setThesisStatus(1); // 初始数据
						Boolean isLastGradeToApply = false; // 是否最后一个学期，然后开启毕业论文和社会实践
//						// 根据产品ID获取学员的所有学期中的最后一个学期
//						List<Map<String, Object>> teachPlanList = gjtTeachPlanService.getGjtTeachPlanInfo((String) info.get("GRADE_SPECIALTY_ID"));
//						if (teachPlanList != null && teachPlanList.size() > 0) {
//							Map<String, Object> teachMap = teachPlanList.get(teachPlanList.size() - 1);
//							// 获取最后学期的学期开始时间
//							GjtGrade gjtGrade = gjtGradeService.queryById(ObjectUtils.toString(teachMap.get("ACTUAL_GRADE_ID")));
//
//							if (gjtGrade != null && now.getTime() > gjtGrade.getStartDate().getTime()) {
//								isLastGradeToApply = true;
//							}
//						}
//						GjtGrade grade = gjtGradeService.findCurrentGrade((String) info.get("XX_ID"));
//						if(isLastGradeToApply) {
//							Float score = gjtThesisApplyService.getScore((String) info.get("XX_ID"), grade.getGradeId(),
//									(String) info.get("GRADE_SPECIALTY_ID"), studentId);
//							if (score < 60) { // 没及格
//								GjtThesisApply apply2 = gjtThesisApplyService.findCompletedApply(studentId);
//								if(apply2 == null) { // 没有通过的
//									GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(grade.getGradeId(), (String) info.get("XX_ID"), 3);
//									if (thesisPlan != null) { // 有论文计划
//										
//									}
//								}
//							}
//						}
						// 实战状态
						dto.setPracticeStatus(1); // 初始数据
						if(isLastGradeToApply) {
							
						}
						// 毕业状态
						if("8".equals(info.get("XJZT"))) {
							dto.setGraduationStatus(1);
						} else {
							// 毕业还没做完以后再优化 2-毕业异常，到期未满足 3-毕业异常，过期未申请
							dto.setGraduationStatus(1);
						}
						// 链接状态
						dto.setLinkStatus(1);
						dto.setAppIsOnline(Constants.BOOLEAN_NO);
						dto.setPcIsOnline(Constants.BOOLEAN_NO);
						// 初始数据
						Map searchParams4 = new HashMap();
						searchParams4.put("STUDENT_ID", studentId);
						searchParams4.put("XX_ID", info.get("XX_ID"));
						List<Map<String, Object>> studentLinkList = studyManageDao.getStudentLinkList(searchParams4);
						if(studentLinkList != null && studentLinkList.size() > 0) {
							Map<String, Object> studentLink = studentLinkList.get(0);

							int isUsePC, offlineDayPC = 0, isUseAPP, offlineDayAPP = 0;
							BigDecimal pcLastLoginTime = (BigDecimal) studentLink.get("PC_LAST_LOGIN_TIME");
							String pcIsOnline = (String) studentLink.get("PC_IS_ONLINE");
							if(pcLastLoginTime != null) {
								isUsePC = 1;
								if(StringUtils.equals(pcIsOnline, Constants.BOOLEAN_YES)) {
									dto.setPcIsOnline(Constants.BOOLEAN_YES);
								} else {
									offlineDayPC = pcLastLoginTime.intValue();
									dto.setPcLastLoginTime(offlineDayPC);
								}
							} else {
								isUsePC = 0;
							}

							BigDecimal appLastLoginTime = (BigDecimal) studentLink.get("APP_LAST_LOGIN_TIME");
							String appIsOnline = (String) studentLink.get("APP_IS_ONLINE");
							if(appLastLoginTime != null) {
								isUseAPP = 1;
								if(StringUtils.equals(appIsOnline, Constants.BOOLEAN_YES)) {
									dto.setAppIsOnline(Constants.BOOLEAN_YES);
								} else {
									offlineDayAPP = appLastLoginTime.intValue();
									dto.setAppLastLoginTime(offlineDayAPP);
								}
							} else {
								isUseAPP = 0;
							}
							BigDecimal isBindingWx = (BigDecimal) studentLink.get("IS_BANDING_WX");
							if(isUseAPP == 0) {
								dto.setLinkStatus(2);
							} else if(isUsePC == 0) {
								dto.setLinkStatus(3);
							} else if(offlineDayPC > 7 && offlineDayAPP > 7) {
								dto.setLinkStatus(4);
							} else if(isBindingWx.intValue() == 0) {
								dto.setLinkStatus(5);
							}
							dto.setIsBandingWx(isBindingWx.intValue());
						}

						Map<String, Object> temp = new HashMap<String, Object>();
						temp.put("STUDENT_ID", studentId);
						temp.put("STATUS", dto.validationGetStatus());
						temp.put("SYNTHESIZE_INFO", GsonUtils.toJson(dto));
						studentInfoDaoImpl.saveTempStudentSynthesize(temp);
					}
				});
			}
		}

		long endTime = System.currentTimeMillis();
		log.error("StudentSynthesizeTask <br/> time consuming: "
				+ (endTime - startTime) + "ms, student size: " + count + ".");
	}

}
