/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class BasicCommonMapServiceImpl {

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	public Map<String, String> getMap(String sql) {
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Object obj : rows) {
			Map<String, String> m = (Map<String, String>) obj;
			resultMap.put(m.get("ID"), m.get("NAME"));
		}
		return resultMap;
	}

}
