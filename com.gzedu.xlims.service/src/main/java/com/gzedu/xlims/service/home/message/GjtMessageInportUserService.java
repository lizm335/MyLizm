/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.message.GjtMessageInportUser;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月29日
 * @version 3.0
 *
 */
public interface GjtMessageInportUserService {

	long getImportStudentCount(String messageId);

	public GjtMessageInportUser save(GjtMessageInportUser item);

	public void save(List<GjtMessageInportUser> item);

	public GjtMessageInportUser update(GjtMessageInportUser item);

	public void delete(String[] id);

	public List<Map<String, Object>> queryImportStudent(String messageId);

}
