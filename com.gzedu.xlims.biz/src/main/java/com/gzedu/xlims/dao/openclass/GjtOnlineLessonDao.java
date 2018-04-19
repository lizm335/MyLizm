package com.gzedu.xlims.dao.openclass;

import com.gzedu.xlims.pojo.openClass.GjtOnlineLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by paul on 2017/8/24.
 */
public interface GjtOnlineLessonDao extends JpaRepository<GjtOnlineLesson, String>, JpaSpecificationExecutor<GjtOnlineLesson> {
}
