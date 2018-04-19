/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.systemManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;

/**
 * 
 * 功能说明：登陆日志管理
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月5日
 * @version 2.5
 *
 */
@Deprecated public interface BzrTblPriLoginLogService {

	Page<BzrTblPriLoginLog> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	void saveEntity(BzrTblPriLoginLog tblPriLoginLog);

	boolean save(BzrGjtUserAccount user, HttpServletRequest request, String sessionId);

	void updateEntity(BzrTblPriLoginLog tblPriLoginLog);

	boolean updateBySessionId(String sessionId, long min);

	boolean updateNewSessionByOldSession(String oldSessionId, String newSessionId);

	BzrTblPriLoginLog queryById(String id);

	boolean delete(Iterable<String> ids);

	boolean delete(String ids);

}
