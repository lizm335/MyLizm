/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年3月29日
 * @version 3.0
 *
 */
public enum DistributeStatusEnum {
	未就绪(0), 待配送(1), 配送中(2), 已签收(3);
	private DistributeStatusEnum(int code) {
		this.code = code;
	}

	private int code;

	public int getCode() {
		return code;
	}

	public static String getName(Integer code) {
		for (DistributeStatusEnum entity : DistributeStatusEnum.values()) {
			if (entity.getCode() == code) {
				return entity.toString();
			}
		}
		return null;
	}

}
