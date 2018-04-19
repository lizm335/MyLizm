package com.gzedu.xlims.serviceImpl.exam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoomNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoundNewDao;
import com.gzedu.xlims.dao.exam.GjtExamStudentRoomNewDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.pojo.exam.GjtExamStudentRoomNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamStudentRoomNewService;
import com.gzedu.xlims.serviceImpl.exam.temp.Course;
import com.gzedu.xlims.serviceImpl.exam.temp.RoomVO;
import com.gzedu.xlims.serviceImpl.exam.temp.Student;
import com.gzedu.xlims.serviceImpl.exam.temp.StudentVO;

@Service
public class GjtExamStudentRoomNewServiceImpl implements GjtExamStudentRoomNewService {

	private static final Log log = LogFactory.getLog(GjtExamStudentRoomNewServiceImpl.class);

	@Autowired
	GjtExamStudentRoomNewDao gjtExamStudentRoomNewDao;
	@Autowired
	CommonMapService commonMapService;
	@Autowired
	GjtExamRoomNewDao gjtExamRoomNewDao;
	@Autowired
	GjtExamAppointmentNewDao gjtExamAppointmentNewDao;
	@Autowired
	GjtExamRoundNewDao gjtExamRoundNewDao;
	@Autowired
	GjtCourseDao gjtCourseDao;
	@Autowired
	GjtExamAppointmentNewService gjtExamAppointmentNewService;
	@Autowired
	GjtStudentInfoDao gjtStudentInfoDao;
	@Autowired
	GjtExamBatchNewDao gjtExamBatchNewDao;
	@Autowired
	GjtExamPointNewDao gjtExamPointNewDao;
	@Autowired
	ExportExamStudentRoomService exportExamStudentRoomService;
	@Autowired
	GjtExamPlanNewDao gjtExamPlanNewDao;

	@Override
	public Page<GjtExamStudentRoomNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtExamStudentRoomNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamStudentRoomNew.class);
		return gjtExamStudentRoomNewDao.findAll(spec, pageRequst);
	}

	@Override
	public Workbook exportExamStudentSeat(String examBatchCode, String examPointId, int examType) {
		// GjtExamBatchNew gjtExamBatchNew =
		// gjtExamBatchNewDao.queryByExamBatchCode(examBatchCode);
		// List<GjtExamPointNew> gjtExamPointNews =
		// gjtExamBatchNew.getGjtExamPointNews();

		GjtExamPointNew gjtExamPointNew = gjtExamPointNewDao.queryById(examPointId);

		Workbook workbook = new XSSFWorkbook();
		// for (GjtExamPointNew gjtExamPointNew : gjtExamPointNews) {
		// 根据 批次、考点、科目找学员考试位置
		String pointName = gjtExamPointNew.getName();
		List<RoomVO> roomVos = queryRoomVos(gjtExamPointNew, examBatchCode, examType);
		// if (roomVos.size() == 0) {
		// continue;
		// }
		Sheet sheet = workbook.createSheet(pointName);
		workbook = exportExamStudentRoomService.exportStudentSeat(workbook, sheet, roomVos);
		// }
		return workbook;
	}

	@Override
	public Workbook exportExamStudentSeat2(String examBatchCode, String examPointId, int examType) {
		GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewDao.queryByExamBatchCode(examBatchCode);

		List<GjtExamPointNew> gjtExamPointNews = new ArrayList<GjtExamPointNew>();

		Workbook workbook = new XSSFWorkbook();

		// 是否选择考点，否则导出所有
		if (StringUtils.isNotBlank(examPointId)) {
			List<GjtExamPointNew> tempGjtExamPointNews = gjtExamBatchNew.getGjtExamPointNews();
			for (GjtExamPointNew gjtExamPointNew : tempGjtExamPointNews) {
				if (examPointId.equals(gjtExamPointNew.getExamPointId())) {
					gjtExamPointNews.add(gjtExamPointNew);
				}
			}
		} else {
			gjtExamPointNews = gjtExamBatchNew.getGjtExamPointNews();
		}

		for (GjtExamPointNew gjtExamPointNew : gjtExamPointNews) {

			try {
				// 根据 批次、考点、科目找学员考试位置
				String pointName = gjtExamPointNew.getName();
				List<RoomVO> roomVos = queryRoomVos(gjtExamPointNew, examBatchCode, examType);
				if (roomVos.isEmpty()) {
					continue;
				}
				// ------- 创建返回结果excel start
				Sheet sheet = workbook.createSheet(pointName);
				// 第一行是标题
				Row outRow = sheet.createRow(0);
				outRow.createCell(0).setCellValue("学号");
				outRow.createCell(1).setCellValue("姓名");
				outRow.createCell(2).setCellValue("试卷号");
				outRow.createCell(3).setCellValue("课程名称");
				outRow.createCell(4).setCellValue("试卷形式");
				outRow.createCell(5).setCellValue("考试日期");
				outRow.createCell(6).setCellValue("考试时间");
				outRow.createCell(7).setCellValue("考场");
				outRow.createCell(8).setCellValue("座位号");
				outRow.createCell(9).setCellValue("考点");
				outRow.createCell(10).setCellValue("考点区域");
				outRow.createCell(11).setCellValue("考点详情");
				int rowIndex = 1;
				for (RoomVO record : roomVos) {
					for (StudentVO student : record.students) {
						outRow = sheet.createRow(rowIndex++);
						outRow.createCell(0).setCellValue(student.xh);
						outRow.createCell(1).setCellValue(student.name);
						outRow.createCell(2).setCellValue(record.examNo);
						outRow.createCell(3).setCellValue(record.courseName);
						outRow.createCell(4).setCellValue(record.examType);
						outRow.createCell(5).setCellValue(record.examDay);
						outRow.createCell(6).setCellValue(record.examTime);
						outRow.createCell(7).setCellValue(record.roomName);
						outRow.createCell(8).setCellValue(student.seatNo);
						outRow.createCell(9).setCellValue(record.pointName);
						outRow.createCell(10).setCellValue(record.pointArea);
						outRow.createCell(11).setCellValue(record.pointAddress);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		}
		
		if (workbook.getNumberOfSheets() == 0) {
			// ------- 创建空的返回结果excel start
			Sheet sheet = workbook.createSheet("无记录");
			// 第一行是标题
			Row outRow = sheet.createRow(0);
			outRow.createCell(0).setCellValue("学号");
			outRow.createCell(1).setCellValue("姓名");
			outRow.createCell(2).setCellValue("试卷号");
			outRow.createCell(3).setCellValue("课程名称");
			outRow.createCell(4).setCellValue("试卷形式");
			outRow.createCell(5).setCellValue("考试日期");
			outRow.createCell(6).setCellValue("考试时间");
			outRow.createCell(7).setCellValue("考场");
			outRow.createCell(8).setCellValue("座位号");
			outRow.createCell(9).setCellValue("考点");
			outRow.createCell(10).setCellValue("考点区域");
			outRow.createCell(11).setCellValue("考点详情");
		}
		
		return workbook;
	}

	// 是否已经排考完毕
	@Override
	public boolean isArrangeOver(String xxId, String examBatchCode, String examPointId, int examType) {

		GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewDao.queryByExamBatchCode(examBatchCode);
		// 该考点预约记录
		List<GjtExamAppointmentNew> examAppointments = gjtExamAppointmentNewDao.getAppointmentsByGrade(
				gjtExamBatchNew.getGradeId(), examPointId, examType);

		if (examAppointments.size() == 0) {
			return false;
		}

		/*for (GjtExamAppointmentNew gjtExamAppointmentNew : examAppointments) {
			if (gjtExamAppointmentNew.getGjtExamStudentRoomNew() == null) {
				return false;
			}
		}*/
		return true;
	}

	private List<RoomVO> queryRoomVos(GjtExamPointNew gjtExamPointNew, String examBatchCode, int examType) {
		String examPointId = gjtExamPointNew.getExamPointId();
		List<GjtExamStudentRoomNew> gjtExamStudentRoomNews = gjtExamStudentRoomNewDao
				.findByExamBatchCodeAndExamPointIdAndExamTypeOrderByExamPlanIdExamRoomIdSeatNo(examBatchCode,
						examPointId, examType);
		List<RoomVO> roomVos = new ArrayList<RoomVO>();
		RoomVO roomVo = new RoomVO();
		StudentVO studentVo = new StudentVO();
		GjtStudentInfo gjtStudentInfo = null;
		Map<String, String> areaMap = commonMapService.getAreaMap();
		for (GjtExamStudentRoomNew studentRoomNew : gjtExamStudentRoomNews) {
			if (StringUtils.isBlank(studentRoomNew.getAppointmentId())) {
				continue;
			}
			GjtExamRoomNew gjtExamRoomNew = studentRoomNew.getGjtExamRoomNew();
			GjtExamPlanNew gjtExamPlanNew = studentRoomNew.getGjtExamPlanNew();
			Date examSt = gjtExamPlanNew.getExamSt();
			Date examEnd = gjtExamPlanNew.getExamEnd();
			// 是否有考试科目+考场
			boolean existsExamRoomIdAndExamPlanId = studentRoomNew.getExamPlanId().equals(roomVo.planId)
					&& studentRoomNew.getExamRoomId().equals(roomVo.roomId);
			if (!existsExamRoomIdAndExamPlanId) {
				String areaId = gjtExamPointNew.getAreaId();
				String provinceCode = areaId.substring(0, 2) + "0000";
				String cityCode = areaId.substring(0, 4) + "00";
				String districtCode = areaId;
				String provinceName = areaMap.get(provinceCode);
				String cityName = areaMap.get(cityCode);
				String districtName = areaMap.get(districtCode);

				roomVo = new RoomVO();
				roomVo.seatTotal = gjtExamRoomNew.getSeats();
				roomVo.roomId = studentRoomNew.getExamRoomId();
				roomVo.pointName = gjtExamPointNew.getName();
				roomVo.pointArea = provinceName + "-" + cityName + "-" + districtName;
				roomVo.pointAddress = gjtExamPointNew.getAddress();
				roomVo.examType = "开/闭卷";// String.valueOf(examType);
				roomVo.roomName = gjtExamRoomNew.getName();
				roomVo.planId = gjtExamPlanNew.getExamPlanId();
//				roomVo.subjectName = gjtExamPlanNew.getExamSubjectNew().getName();
//				roomVo.examNo = gjtExamPlanNew.getExamSubjectNew().getExamNo();
				roomVo.examNo = gjtExamPlanNew.getExamNo();
				roomVo.title = " 考试考生签到表（成绩表）";
				
				if (examSt != null && examEnd != null) {
					roomVo.startDate = examSt;
					roomVo.endDate = examEnd;
					int year = examSt.getYear();
					int month = examSt.getMonth() + 1;
					int date = examSt.getDate();
					int sthours = examSt.getHours();
					int stseconds = examSt.getMinutes();
					int endhours = examEnd.getHours();
					int endseconds = examEnd.getMinutes();
					// r.examDay = "2016年7月9日";
					// r.examTime = "08:30-10:00";
					roomVo.examDay = 1900 + year + "年" + month + "月" + date + "日";
					String s1 = String.valueOf(sthours).length() == 1 ? "0" + String.valueOf(sthours)
							: String.valueOf(sthours);
					String s2 = String.valueOf(stseconds).length() == 1 ? "0" + String.valueOf(stseconds)
							: String.valueOf(stseconds);
					String s3 = String.valueOf(endhours).length() == 1 ? "0" + String.valueOf(endhours)
							: String.valueOf(endhours);
					String s4 = String.valueOf(endseconds).length() == 1 ? "0" + String.valueOf(endseconds)
							: String.valueOf(endseconds);
					roomVo.examTime = s1 + ":" + s2 + "-" + s3 + ":" + s4;
				}
				roomVo.students = new ArrayList<StudentVO>();
				roomVos.add(roomVo);
			}

			// 排位置
			studentVo = new StudentVO();
			gjtStudentInfo = studentRoomNew.getGjtStudentInfo();
			if (gjtStudentInfo != null) {
				studentVo.studentId = gjtStudentInfo.getStudentId();
				studentVo.name = gjtStudentInfo.getXm();
				// 1男2女
				if (gjtStudentInfo.getXbm().equals("1")) {
					studentVo.sex = "男";
				} else {
					studentVo.sex = "女";
				}
				studentVo.xh = gjtStudentInfo.getXh();
				studentVo.cardNo = gjtStudentInfo.getSfzh();
				studentVo.seatNo = studentRoomNew.getSeatNo();
				studentVo.isRepeat = "";
				roomVo.students.add(studentVo);
			}
			
//			String courseNames = "";
//			List<GjtCourse> gjtCourseList = gjtExamPlanNew.getGjtCourseList();
//			if (gjtCourseList != null) {
//				for (int i = 0; i < gjtCourseList.size(); i++) {
//					courseNames += gjtCourseList.get(i).getKcmc();
//					
//					if (i != gjtCourseList.size() - 1) {
//						courseNames += ",";
//					}
//				}
//			}
			String courseNames = gjtExamPlanNew.getExamPlanName(); // 科目名称
			roomVo.courseName = courseNames;
		}
		return roomVos;
	}

	// 自动排考
	@Override
	public Map<String, List<GjtExamStudentRoomNew>> arrangement(String examBatchCode, String examPointId, int examType) throws Exception {
		Map<String, List<GjtExamStudentRoomNew>> result = new HashMap<String, List<GjtExamStudentRoomNew>>();
		List<GjtExamStudentRoomNew> successList = new ArrayList<GjtExamStudentRoomNew>();
		List<GjtExamStudentRoomNew> failedList = new ArrayList<GjtExamStudentRoomNew>();
		result.put("success", successList);
		result.put("failed", failedList);
		
		// 校验参数
		GjtExamPointNew gjtExamPointNew = gjtExamPointNewDao.queryById(examPointId);
		if (gjtExamPointNew == null) {
			throw new ServiceException("参数错误！考点不存在");
		}
		GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewDao.queryByExamBatchCode(examBatchCode);
		if (gjtExamBatchNew == null) {
			throw new ServiceException("参数错误！批次不存在");
		}
		String gradeId = gjtExamBatchNew.getGradeId();
		if (gradeId == null || "".equals(gradeId)) {
			throw new ServiceException("数据错误！学期不存在");
		}
		List<GjtExamPlanNew> examPlans = gjtExamBatchNew.getGjtExamPlanNews();
		if (examPlans == null || examPlans.size() == 0) {
			throw new ServiceException("数据错误！该批次下的考试计划数据条数为0");
		}
		Map<String, GjtExamPlanNew> examPlanIdMap = new HashMap<String, GjtExamPlanNew>();
		for (GjtExamPlanNew gjtExamPlanNew : examPlans) {
			examPlanIdMap.put(gjtExamPlanNew.getExamPlanId(), gjtExamPlanNew);
		}

		// 该考点预约记录
		List<GjtExamAppointmentNew> examAppointments = gjtExamAppointmentNewDao.getAppointmentsByGrade(gradeId,
				examPointId, examType);
		if (examAppointments == null || examAppointments.size() == 0) {
			throw new ServiceException("数据提醒！该考点预约记录条数为0");
		}
		String errors = "";
		// 校验该考点下预约的考试计划是否已经有考试时间
		for (GjtExamAppointmentNew gjtExamAppointmentNew : examAppointments) {
			String examPlanId = gjtExamAppointmentNew.getExamPlanId();
			GjtExamPlanNew examPlanNew = examPlanIdMap.get(examPlanId);
			// 必须有设定考试时间
			if (examPlanNew == null) {
				throw new ServiceException("数据错误！预约记录的考试计划为NULL;预约ID=" + gjtExamAppointmentNew.getAppointmentId());
			}
			if (examPlanNew.getExamSt() == null || examPlanNew.getExamEnd() == null) {
				errors += examPlanNew.getExamPlanName() + ",";
			}
		}
		if (!errors.equals("")) {
			throw new ServiceException("数据提醒！该考点部分考试科目未设置考试时间,科目名称如下：" + errors);
		}
		// 校验该考点的预约人数和考场座位数
		/*int appointmentCount = examAppointments.size();
		int pointSeatCount = gjtExamPointNew.getSeatCount();

		if (appointmentCount > pointSeatCount) {
			throw new ServiceException("数据提醒！该考点的预约人数" + appointmentCount + "大于该考点座位总数量" + pointSeatCount);
		}*/

		List<Course> courses = new ArrayList<Course>();
		List<Student> students = new ArrayList<Student>();
		for (GjtExamAppointmentNew gjtExamAppointmentNew : examAppointments) {
			String examPlanId = gjtExamAppointmentNew.getExamPlanId();
			GjtExamPlanNew examPlanNew = examPlanIdMap.get(examPlanId);
			Course course = new Course(examPlanNew.getExamPlanId(), examPlanNew.getSubjectCode(),
					examPlanNew.getExamSt(), examPlanNew.getExamEnd());
			if (courses.contains(course)) {
				for (Course cc : courses) {
					if (cc.getId().equals(course.getId())) {
						course = cc;
						break;
					}
				}
			} else {
				courses.add(course);
			}

			Student student = new Student(gjtExamAppointmentNew.getStudentId(), "");
			if (students.contains(student)) {
				for (Student ss : students) {
					if (ss.getId().equals(student.getId())) {
						student = ss;
						break;
					}
				}
			} else {
				students.add(student);
			}
			course.getStudents().add(student);
			student.getCourses().add(course);
		}

		// 统计科目人数
		int score = courses.size();
		for (Course course : courses) {
			for (Student student : students) {
				if (student.getCourses().contains(course)) {
					course.getStudents().add(student);
					student.setScore(student.getScore() + score);
				}
			}
			score = score - 1;
		}

		Collections.sort(courses);
		Collections.sort(students);

		for (Course course : courses) {
			// 为科目创建考场
			// List<GjtExamStudentRoomNew> examStudentRoomNews =
			// this.createExamPlanRoom(gjtExamPointNew, course.getId(),
			// examType);
			String examPlanId = course.getId();
			// 该考试该考点的座位信息
			List<GjtExamStudentRoomNew> examStudentRoomNews = this.queryExamStudentRoomNews(examPointId, examPlanId);
			//List<GjtExamAppointmentNew> examAppointmentNews = new ArrayList<GjtExamAppointmentNew>();
			// 逻辑好乱待优化@liming
			for (Student student : students) {
				for (GjtExamAppointmentNew examStudent : examAppointments) {
					if (examStudent.getStudentId().equals(student.getId())
							&& examStudent.getExamPlanId().equals(course.getId())) {
						boolean isExist = false;
						for (GjtExamStudentRoomNew existExamStudentRoomNew : examStudentRoomNews) {
							// 如果已存在
							if (existExamStudentRoomNew.getExamPlanId().equals(examPlanId)
									&& existExamStudentRoomNew.getStudentId().equals(student.getId())) {
								isExist = true;
								successList.add(existExamStudentRoomNew);
								break;
							}
						}
						if (isExist) {
							break;
						}
						List<GjtExamRoomNew> examRooms = gjtExamPointNew.getGjtExamRoomNews();
						for (GjtExamRoomNew roomNew : examRooms) {
							boolean isAdd = false;
							int seats = roomNew.getSeats();
							for (int i = 1; i < seats + 1; i++) {
								GjtExamStudentRoomNew initStudentTORoom = initStudentTORoom(examStudent,
										gjtExamPointNew, roomNew, i);
								initStudentTORoom.setGjtStudentInfo(examStudent.getStudent());
								initStudentTORoom.setGjtExamPlanNew(examStudent.getExamPlanNew());
								initStudentTORoom.setGjtExamPointNew(gjtExamPointNew);
								initStudentTORoom.setGjtExamRoomNew(roomNew);
								boolean flag = false;

								// 检查该位置是否有人(同一个考点同一科目同一位置
								for (GjtExamStudentRoomNew existExamStudentRoomNew : examStudentRoomNews) {
									if (existExamStudentRoomNew.getSeatNo() == i
											&& existExamStudentRoomNew.getExamRoomId().equals(roomNew.getExamRoomId())
											&& existExamStudentRoomNew.getExamPlanId()
													.equals(examStudent.getExamPlanId())) {
										flag = true;
										break;
									}
								}
								if (!flag) {
									examStudentRoomNews.add(initStudentTORoom);
									gjtExamStudentRoomNewDao.save(initStudentTORoom);
									
									examStudent.setStatus(1);
									//examAppointmentNews.add(examStudent);
									gjtExamAppointmentNewDao.save(examStudent);

									successList.add(initStudentTORoom);
									isAdd = true;
									break;
								}
							}
							if (isAdd) {
								break;
							}
						}
						
						GjtExamStudentRoomNew examStudentRoom = gjtExamStudentRoomNewDao.findByStudentIdAndExamPlanId(student.getId(), course.getId());
						if (examStudentRoom == null) {
							GjtExamStudentRoomNew roomSeat = new GjtExamStudentRoomNew(gjtExamPointNew.getExamBatchCode(), examPlanId,
									gjtExamPointNew.getExamPointId(), "", 0, examStudent.getType());
							roomSeat.setAppointmentId(examStudent.getAppointmentId());
							roomSeat.setStudentId(examStudent.getStudentId());
							roomSeat.setExamBatchId(gjtExamPointNew.getExamBatchId());
							roomSeat.setGjtStudentInfo(examStudent.getStudent());
							roomSeat.setGjtExamPlanNew(examStudent.getExamPlanNew());
							roomSeat.setGjtExamPointNew(gjtExamPointNew);
							
							failedList.add(roomSeat);
						}

					}
				}
			}
			/*gjtExamStudentRoomNewDao.save(examStudentRoomNews);
			if (examAppointmentNews.size() > 0) {
				gjtExamAppointmentNewDao.save(examAppointmentNews);
			}*/
		}
		
		return result;
	}

	public List<GjtExamStudentRoomNew> queryAll(Map<String, Object> searchParams) {
		List<Order> orders = new ArrayList<Sort.Order>();
		orders.add(new Order(Direction.ASC, "examPlanId"));
		orders.add(new Order(Direction.ASC, "examRoomId"));
		orders.add(new Order(Direction.ASC, "seatNo"));
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtExamStudentRoomNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamStudentRoomNew.class);
		return gjtExamStudentRoomNewDao.findAll(spec, new Sort(orders));
	}

	// 根据考点，考试计划获得考场座位列表
	@Override
	public List<GjtExamStudentRoomNew> queryExamStudentRoomNews(String examPointId, String examPlanId) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_examPointId", examPointId);
		searchParams.put("EQ_examPlanId", examPlanId);
		return this.queryAll(searchParams);
	}

	@Override
	public List<GjtExamStudentRoomNew> queryExamStudentRoomNewsByExamRoomIdAndExamPlanId(String examRoomId,
			String examPlanId) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_examRoomId", examRoomId);
		searchParams.put("EQ_examPlanId", examPlanId);
		return this.queryAll(searchParams);
	}

	// 创建考试考场(废弃）
	@Override
	public List<GjtExamStudentRoomNew> createExamPlanRoom(GjtExamPointNew examPointNew, String examPlanNewId,
			int examType) {
		List<GjtExamRoomNew> examRooms = examPointNew.getGjtExamRoomNews();

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_examPointId", examPointNew.getExamPointId());
		searchParams.put("EQ_examType", examType);
		searchParams.put("EQ_examPlanId", examPlanNewId);
		List<GjtExamStudentRoomNew> gjtExamStudentRoomNews = this.queryAll(searchParams);

		List<GjtExamStudentRoomNew> entitys = new ArrayList<GjtExamStudentRoomNew>();

		for (GjtExamRoomNew roomNew : examRooms) {
			int seats = roomNew.getSeats();
			for (int i = 1; i < seats + 1; i++) {
				GjtExamStudentRoomNew roomSeat = new GjtExamStudentRoomNew(examPointNew.getExamBatchCode(),
						examPlanNewId, examPointNew.getExamPointId(), roomNew.getExamRoomId(), i, examType);
				// 不存在，就创建 科目考场座位
				if (!gjtExamStudentRoomNews.contains(roomSeat)) {
					entitys.add(roomSeat);
					gjtExamStudentRoomNews.add(roomSeat);
				}
			}
		}
		if (!entitys.isEmpty()) {
			gjtExamStudentRoomNewDao.save(entitys);
		}
		return gjtExamStudentRoomNews;
	}

	// 给预约的考生分配考场座位
	public void saveStudentTORoom(GjtExamAppointmentNew gjtExamAppointmentNew,
			List<GjtExamStudentRoomNew> examStudentRoomNews) {
		String appointmentId = gjtExamAppointmentNew.getAppointmentId();
		String studentId = gjtExamAppointmentNew.getStudentId();
		String examPlanId = gjtExamAppointmentNew.getExamPlanId();
		GjtExamStudentRoomNew empty = null;
		// 倒序查找
		for (int i = examStudentRoomNews.size() - 1; i >= 0; i--) {
			GjtExamStudentRoomNew gjtExamStudentRoomNew = examStudentRoomNews.get(i);
			String roomAppointmentId = gjtExamStudentRoomNew.getAppointmentId();
			String roomExamPlanId = gjtExamStudentRoomNew.getExamPlanId();
			// 判断是否相同的预约科目
			if (roomExamPlanId.equals(examPlanId)) {
				// 如果没人坐，就记录下
				if (StringUtils.isBlank(roomAppointmentId)) {
					empty = gjtExamStudentRoomNew;
					continue;
				}
				// 判断这个人是不是他
				if (roomAppointmentId.equals(appointmentId)) {
					return;
				}
			}
		}
		// 如果不存在，就给他空的位置坐
		if (empty != null) {
			empty.setAppointmentId(appointmentId);
			empty.setStudentId(studentId);
		}
		// 否则就是位置不够
	}

	// 给预约的考生分配考场座位
	@Override
	public GjtExamStudentRoomNew initStudentTORoom(GjtExamAppointmentNew gjtExamAppointmentNew,
			GjtExamPointNew gjtExamPointNew, GjtExamRoomNew examRoomNew, int seatNo) {
		String examPlanId = gjtExamAppointmentNew.getExamPlanId();
		int type = gjtExamAppointmentNew.getType();
		String appointmentId = gjtExamAppointmentNew.getAppointmentId();
		String studentId = gjtExamAppointmentNew.getStudentId();

		if (examRoomNew.getSeats() >= seatNo && seatNo > 0) {
			GjtExamStudentRoomNew roomSeat = new GjtExamStudentRoomNew(gjtExamPointNew.getExamBatchCode(), examPlanId,
					gjtExamPointNew.getExamPointId(), examRoomNew.getExamRoomId(), seatNo, type);
			roomSeat.setAppointmentId(appointmentId);
			roomSeat.setStudentId(studentId);
			roomSeat.setExamBatchId(gjtExamPointNew.getExamBatchId());
			return roomSeat;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.exam.GjtExamStudentRoomNewService#delete(java.
	 * util.List)
	 */
	@Override
	public void delete(List<String> ids) {
		gjtExamStudentRoomNewDao.delete(ids);

	}

	@Override
	public void delete(GjtExamStudentRoomNew entity) {
		gjtExamStudentRoomNewDao.delete(entity);
	}

	@Override
	public GjtExamStudentRoomNew findByExamPlanIdAndExamRoomIdAndSeatNo(String examPlanId, String examRoomId,
			int seatNo) {
		return gjtExamStudentRoomNewDao.findByExamPlanIdAndExamRoomIdAndSeatNo(examPlanId, examRoomId, seatNo);
	}

	@Override
	public GjtExamStudentRoomNew findByStudentIdAndExamPlanId(String studentId, String examPlanId) {
		return gjtExamStudentRoomNewDao.findByStudentIdAndExamPlanId(studentId, examPlanId);
	}

	@Override
	public GjtExamStudentRoomNew insert(GjtExamStudentRoomNew entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		
		return gjtExamStudentRoomNewDao.save(entity);
	}

	@Override
	public GjtExamStudentRoomNew update(GjtExamStudentRoomNew entity) {
		entity.setUpdatedDt(new Date());
		
		return gjtExamStudentRoomNewDao.save(entity);
	}

	@Override
	public Page<Map<String, Object>> findAdmissionTickets(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		return gjtExamStudentRoomNewDao.findAdmissionTickets(orgId, searchParams, pageRequst);
	}
	
	/**
	 * 查询排考信息
	 */
	@Override
	public Page getExamStudentRoomList(Map formMap, PageRequest pageRequst) {
		return gjtExamStudentRoomNewDao.getExamStudentRoomList(formMap, pageRequst);
	}
}
