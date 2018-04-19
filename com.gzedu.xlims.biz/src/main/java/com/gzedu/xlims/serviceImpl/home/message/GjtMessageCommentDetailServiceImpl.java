/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.home.message.GjtMessageCommentDetailDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageCommentDetail;
import com.gzedu.xlims.service.home.message.GjtMessageCommentDetailService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月8日
 * @version 3.0
 *
 */
@Service
public class GjtMessageCommentDetailServiceImpl implements GjtMessageCommentDetailService {

	@Autowired
	GjtMessageCommentDetailDao gjtMessageCommentDetailDao;

	@Autowired
	CommonDao commonDao;

	@Override
	public GjtMessageCommentDetail save(GjtMessageCommentDetail entity) {
		return gjtMessageCommentDetailDao.save(entity);
	}

	@Override
	public GjtMessageCommentDetail save(String commentId, String content, GjtUserAccount user, int platform) {
		GjtMessageCommentDetail item = new GjtMessageCommentDetail();
		item.setId(UUIDUtils.random());
		item.setCreatedDt(DateUtils.getNowTime());
		item.setCreatedBy(user.getId());
		item.setContent(content);
		item.setIsDeleted("N");
		item.setLikecount(0);
		item.setUserName(user.getRealName());
		item.setPlatform(String.valueOf(platform));
		item.setCommentId(commentId);
		return this.save(item);
	}

	@Override
	public List<Map<String, Object>> queryByCommId(String commentId, String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select m.user_Name \"userName\",to_char(m.created_dt,'yyyy-mm-dd hh24:mi:ss')  \"createdDt\", ");
		sql.append(" m.platform \"platform\",m.content \"content\" ,m.id \"id\" ,");
		sql.append("  m.user_Id \"userId\",(select count(0)");
		sql.append("  from gjt_message_comment_like cl");
		sql.append("  where cl.comment_id = m.id");
		sql.append("  and cl.user_id = :userId) \"isLike\",");// 是否已经点赞
		sql.append("  (select count(0)");
		sql.append("  from gjt_message_comment_like cl");
		sql.append("  where cl.comment_id = m.id) \"likecount\"");// 一共多少赞
		// sql.append(" ,(select s.avatar from gjt_student_info s where
		// s.account_id=m.created_by ) \"avatar\" ");
		sql.append("  from gjt_message_comment_detail m");
		sql.append("  where m.is_deleted = 'N' and m.comment_id=:commentId");
		sql.append(" order by m.created_dt desc");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("commentId", commentId);
		params.put("userId", userId);
		List<Map<String, Object>> list = commonDao.queryForMapList(sql.toString(), params);
		return list;
	}

	@Override
	public GjtMessageCommentDetail update(GjtMessageCommentDetail entity) {
		return gjtMessageCommentDetailDao.save(entity);
	}

	@Override
	public GjtMessageCommentDetail queryById(String id) {
		return gjtMessageCommentDetailDao.findOne(id);
	}

}
