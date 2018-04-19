/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageCommentDetail;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月8日
 * @version 3.0
 *
 */
public interface GjtMessageCommentDetailService {

	public GjtMessageCommentDetail update(GjtMessageCommentDetail entity);

	public GjtMessageCommentDetail queryById(String id);

	public GjtMessageCommentDetail save(GjtMessageCommentDetail entity);

	public GjtMessageCommentDetail save(String commentId, String content, GjtUserAccount user, int platform);

	public List<Map<String, Object>> queryByCommId(String commentId, String userId);

}
