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

import com.gzedu.xlims.pojo.ViewStudentInfo;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月1日
 * @version 2.5
 *
 */
public interface ViewStudentInfoDao
		extends JpaRepository<ViewStudentInfo, String>, JpaSpecificationExecutor<ViewStudentInfo> {

	ViewStudentInfo findByStudentId(String studentId);

	ViewStudentInfo findByXh(String xh);

	ViewStudentInfo findByXhAndXm(String xh, String xm);

}
