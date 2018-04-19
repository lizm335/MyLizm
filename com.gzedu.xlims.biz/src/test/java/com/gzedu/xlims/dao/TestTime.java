/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class TestTime {
	public static void main(String[] args) {
		new Thread(new Task("C")).start();
		System.out.println("结束");
	}
}
