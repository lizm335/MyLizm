package com.gzedu.xlims.dao.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class HomeDao {

	@Autowired
	private CommonDao commonDao;
	
	public List<Object[]> getMessageList(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ")
			.append("        	\"messageId\", ")
			.append("        	\"sendType\", ")
			.append("        	\"infoType\", ")
			.append("        	\"infoTheme\", ")
			.append("        	\"createdDt\", ")
			.append("        	\"isRead\" ")
			.append("    from ")
			.append("        	( ")
			.append("        	 select ")
			.append("        	 		i.message_id as \"messageId\", ")
			.append("        	 		2 as \"sendType\", ")
			.append("        	 		i.info_type as \"infoType\", ")
			.append("        	 		i.info_theme as \"infoTheme\", ")
			.append("        	 		i.created_dt as \"createdDt\", ")
			.append("        	 		u.is_enabled as \"isRead\" ")
			.append("        	 from ")
			.append("        	 		gjt_message_user u, ")
			.append("        	 		gjt_message_info i ")
			.append("        	 where ")
			.append("        	 		u.user_id = :userId ")
			.append("        	 		and u.is_deleted = 'N' ")
			.append("        	 		and u.message_id = i.message_id ")
			.append("        	 		and i.is_deleted = 'N' ")
			.append("        	 union all ")
			.append("        	 select ")
			.append("        	 		i.message_id as \"messageId\", ")
			.append("        	 		1 as \"sendType\", ")
			.append("        	 		i.info_type as \"infoType\", ")
			.append("        	 		i.info_theme as \"infoTheme\", ")
			.append("        	 		i.created_dt as \"createdDt\", ")
			.append("        	 		'1' as \"isRead\" ")
			.append("        	 from ")
			.append("        	 		gjt_message_info i ")
			.append("        	 where ")
			.append("        	 		i.put_user = :userId ")
			.append("        	 		and i.is_deleted = 'N' ")
			.append("        	 order by \"createdDt\" desc ")
			.append("        	) ")
			.append("    where rownum <= 6 ");

		map.put("userId", userId);

		return commonDao.queryForObjectListNative(sb.toString(), map);
	}

}
