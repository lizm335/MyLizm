/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageComment;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月7日
 * @version 3.0
 *
 */
public interface GjtMessageCommentService {

	public GjtMessageComment save(GjtMessageComment entity);

	public GjtMessageComment update(GjtMessageComment entity);

	public GjtMessageComment save(String messageId, String content, String imgUrl, GjtUserAccount user, int platform);

	public GjtMessageComment queryById(String id);

	public List<Map<String, Object>> queryAll(Map<String, Object> map);

	void addLike(String id);

}
