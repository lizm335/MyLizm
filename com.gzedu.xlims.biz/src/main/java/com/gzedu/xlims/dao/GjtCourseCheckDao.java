/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtCourseCheck;

/**
 * 
 * 功能说明：课程验收审批记录
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年9月25日
 * @version 3.0
 *
 */
public interface GjtCourseCheckDao
		extends JpaRepository<GjtCourseCheck, String>, JpaSpecificationExecutor<GjtCourseCheck> {

}
