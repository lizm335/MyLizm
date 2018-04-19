package com.gzedu.xlims.dao.textbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.textbook.GjtTextbookGrade;

/**
 * 
 * 功能说明：教材计划所属
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年9月13日
 * @version 3.0
 *
 */
public interface GjtTextbookGradeDao
		extends JpaRepository<GjtTextbookGrade, String>, JpaSpecificationExecutor<GjtTextbookGrade> {

}
