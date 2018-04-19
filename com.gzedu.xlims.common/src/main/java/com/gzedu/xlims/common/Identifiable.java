package com.gzedu.xlims.common;


import java.io.Serializable;


/**
 * @Function: 主键标识
 * @ClassName: Identifiable 
 * @date: 2016年4月17日 下午9:42:09 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
public interface Identifiable extends Serializable {
	/**
	 * 获取主键
	 * @author zhy
	 * @return
	 * @date 2016年4月17日 下午5:56:13
	 */
	public String getId();

	/**
	 * 设置ID属性
	 * @param id
	 */
	public void setId(String id);
}
