/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
public interface GjtRulesSettingDao
		extends JpaRepository<GjtRulesSetting, String>, JpaSpecificationExecutor<GjtRulesSetting> {

}
