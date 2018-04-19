package com.gzedu.xlims.dao.edumanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtGrantCourseScore;

public interface GjtGrantCourseScoreDao extends JpaRepository<GjtGrantCourseScore, String>, JpaSpecificationExecutor<GjtGrantCourseScore>{

}
