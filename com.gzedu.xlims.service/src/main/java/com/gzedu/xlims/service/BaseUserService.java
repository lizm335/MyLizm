package com.gzedu.xlims.service;

import com.gzedu.xlims.pojo.GjtUserAccount;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月27日
 * @version 2.5
 *
 */
public abstract class BaseUserService {

	protected GjtUserAccount user;

	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager em;

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

	public GjtUserAccount getUser() {
		return user;
	}

	public void setUser(GjtUserAccount user) {
		this.user = user;
	}

}
