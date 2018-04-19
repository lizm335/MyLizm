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
import com.gzedu.xlims.dao.home.message.GjtMessageCommentDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageComment;
import com.gzedu.xlims.service.home.message.GjtMessageCommentService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月7日
 * @version 3.0
 *
 */
@Service
public class GjtMessageCommentServiceImpl implements GjtMessageCommentService {
	@Autowired
	private GjtMessageCommentDao gjtMessageCommentDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	public GjtMessageComment save(GjtMessageComment entity) {
		return gjtMessageCommentDao.save(entity);
	}

	@Override
	public List<Map<String, Object>> queryAll(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select m.user_Name \"userName\",to_char(m.created_dt,'yyyy-mm-dd hh24:mi:ss')  \"createdDt\", ");
		sql.append(" m.platform \"platform\",m.content \"content\",m.img_urls \"imgUrls\" ,m.id \"id\" ,");
		sql.append("  m.created_by \"userId\",(select count(0)");
		sql.append("  from gjt_message_comment_like cl");
		sql.append("  where cl.comment_id = m.id");
		sql.append("  and cl.user_id = :userId) \"isLike\",");// 是否已经点赞
		sql.append("  (select count(0)");
		sql.append("  from gjt_message_comment_like cl");
		sql.append("  where cl.comment_id = m.id) \"likecount\",");// 一共多少赞
		sql.append("  (select count(0)");
		sql.append("  from gjt_message_comment_detail cd");
		sql.append("  where cd.comment_id = m.id");
		sql.append("  and cd.is_deleted = 'N') \"detailCount\",");// 一共多少回复
		sql.append(" (select s.avatar  from gjt_student_info s where s.account_id=m.created_by ) \"avatar\" ");
		sql.append("  from gjt_message_comment m");
		sql.append("  where m.is_deleted = 'N' and m.message_Id=:messageId");
		sql.append(" order by m.created_dt desc");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", map.get("userId"));
		params.put("messageId", map.get("messageId"));
		List<Map<String, Object>> list = commonDao.queryForMapList(sql.toString(), params);
		return list;
	}

	@Override
	public void addLike(String id) {
		gjtMessageCommentDao.addLike(id);
	}

	@Override
	public GjtMessageComment queryById(String id) {
		return gjtMessageCommentDao.findOne(id);
	}

	@Override
	public GjtMessageComment save(String messageId, String content, String imgUrl, GjtUserAccount user, int platform) {
		GjtMessageComment item = new GjtMessageComment();
		item.setId(UUIDUtils.random());
		item.setCreatedDt(DateUtils.getNowTime());
		item.setCreatedBy(user.getId());
		item.setContent(content);
		item.setIsDeleted("N");
		item.setLikecount(0);
		item.setMessageId(messageId);
		item.setPlatform(String.valueOf(platform));
		item.setUserName(user.getRealName());
		item.setImgUrls(imgUrl.toString());
		return this.save(item);
	}

	@Override
	public GjtMessageComment update(GjtMessageComment entity) {
		return gjtMessageCommentDao.save(entity);
	}

}
