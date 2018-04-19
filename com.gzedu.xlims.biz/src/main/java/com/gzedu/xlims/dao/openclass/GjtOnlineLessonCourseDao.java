package com.gzedu.xlims.dao.openclass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.openClass.GjtOnlineLessonCourse;

/**
 * Created by paul on 2017/8/24.
 */
public interface GjtOnlineLessonCourseDao extends JpaRepository<GjtOnlineLessonCourse, String>, JpaSpecificationExecutor<GjtOnlineLessonCourse> {


    List<GjtOnlineLessonCourse> findByOnlineId(String onlineId);

    @Query("select golc.onlineLessonCourseId from GjtOnlineLessonCourse golc where golc.onlineId = ?1")
    List<String> findIdByOnlineId(String onlineId);

    @Query("select  TO_CHAR(WM_CONCAT(golc.termCourseId)) from GjtOnlineLessonCourse golc where golc.isDeleted='N' and golc.onlineId = ?1")
    String findTermcourseIdsIdByOnlineId(String onlineId);


    List<GjtOnlineLessonCourse> findByOnlineIdAndTermCourseIdAndIsDeleted(String onlineId,String termCourseId,String isDeleted);
}
