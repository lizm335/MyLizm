/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.systemManage;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.TblPriLoginLog;

/**
 * 
 * 功能说明：登陆日志管理
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月5日
 * @version 2.5
 *
 */
public interface TblPriLoginLogService extends BaseService<TblPriLoginLog> {

	boolean save(GjtUserAccount user, HttpServletRequest request, String sessionId);

	boolean updateEntity(TblPriLoginLog tblPriLoginLog);

	boolean updateBySessionId(String sessionId, long min);

	boolean updateNewSessionByOldSession(String oldSessionId, String newSessionId);

	boolean delete(Iterable<String> ids);

	/**
	 * 按规则搜索日志内容，参考 Criterion
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<TblPriLoginLog> queryLoginLogByPage(Map<String, Object> searchParams, PageRequest pageRequest);
	
	/**
	 * 获取所有日志？？
	 * @param searchParams
	 * @return
	 */
	public List<TblPriLoginLog> queryAllLoginLog(Map<String, Object> searchParams);
	
}
