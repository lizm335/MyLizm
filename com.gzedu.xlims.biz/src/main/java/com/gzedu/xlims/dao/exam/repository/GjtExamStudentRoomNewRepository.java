package com.gzedu.xlims.dao.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.exam.GjtExamStudentRoomNew;

public interface GjtExamStudentRoomNewRepository
		extends JpaRepository<GjtExamStudentRoomNew, String>, JpaSpecificationExecutor<GjtExamStudentRoomNew> {

	// 获得当前考试批次，考点的考试计划科目
	@Query(nativeQuery = true, value = "select exam_plan_id from gjt_exam_student_room_new where exam_batch_code = ?1 and exam_point_id =?2 and  group by exam_plan_id")
	List<String> groupExamPlanIdByExamBatchCodeAndExamPointId(String examBatchCode, String examPointId, int examType);

	@Query(nativeQuery = true, value = "select a.* from gjt_exam_student_room_new a,gjt_exam_plan_new b where a.exam_plan_id = b.exam_plan_id and a.exam_batch_code = ?1 and a.exam_point_id = ?2 and a.exam_type = ?3 and a.appointment_id is not null order by b.exam_end,a.exam_plan_id,a.exam_room_id,a.seat_no")
	List<GjtExamStudentRoomNew> findByExamBatchCodeAndExamPointIdAndExamTypeOrderByExamPlanIdAndExamRoomIdAndSeatNo(
			String examBatchCode, String examPointId, int examType);
	
	GjtExamStudentRoomNew findByStudentIdAndExamPlanId(String studentId, String examPlanId);
	
	GjtExamStudentRoomNew findByExamPlanIdAndExamRoomIdAndSeatNo(String examPlanId, String examRoomId, int seatNo);
}
