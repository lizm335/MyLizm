package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviserMsg;

/**
 * 毕业指导老师接收信息权限
 * @author eenet09
 *
 */
public interface GjtGraduationAdviserMsgDao extends JpaRepository<GjtGraduationAdviserMsg, String>, JpaSpecificationExecutor<GjtGraduationAdviserMsg> {

}
