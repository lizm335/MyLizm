/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.home.message.GjtMessageInportUserDao;
import com.gzedu.xlims.pojo.message.GjtMessageInportUser;
import com.gzedu.xlims.service.home.message.GjtMessageInportUserService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月29日
 * @version 3.0
 *
 */
@Service
public class GjtMessageInportUserServiceImpl implements GjtMessageInportUserService {

	@Autowired
	GjtMessageInportUserDao gjtMessageInportUserDao;

	@Autowired
	CommonDao commonDao;

	@Override
	public GjtMessageInportUser save(GjtMessageInportUser item) {
		GjtMessageInportUser save = gjtMessageInportUserDao.save(item);
		return save;
	}

	@Override
	public void save(List<GjtMessageInportUser> item) {
		gjtMessageInportUserDao.save(item);
	}

	@Override
	public GjtMessageInportUser update(GjtMessageInportUser item) {
		GjtMessageInportUser save = gjtMessageInportUserDao.save(item);
		return save;
	}

	@Override
	public void delete(String[] ids) {
		if (EmptyUtils.isNotEmpty(ids)) {
			for (String id : ids) {
				gjtMessageInportUserDao.delete(id);
			}
		}
	}

	@Override
	public long getImportStudentCount(String messageId) {
		return gjtMessageInportUserDao.findByMessageIdCount(messageId);
	}

	@Override
	public List<Map<String, Object>> queryImportStudent(String messageId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  select distinct a.student_id,");
		sql.append("  a.account_id id,");
		sql.append("  a.xxzx_id, a.sfzh, a.atid");
		sql.append("  from gjt_student_info a");
		sql.append("  inner join gjt_message_inport_user b");
		sql.append("  on a.account_id = b.user_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and b.message_id = :messageId");
		params.put("messageId", messageId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

}
