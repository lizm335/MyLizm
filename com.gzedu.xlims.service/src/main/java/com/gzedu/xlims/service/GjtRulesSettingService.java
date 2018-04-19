package com.gzedu.xlims.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtRulesSetting;

/**
 * 
 * 功能说明：规则设置
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtRulesSettingService {
	public void deleteById(String[] ids);

	public Page<GjtRulesSetting> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public GjtRulesSetting queryBy(String id);

	public void delete(Iterable<String> ids);

	public void delete(String id);

	public void insert(GjtRulesSetting entity);

	public void update(GjtRulesSetting entity);
}
