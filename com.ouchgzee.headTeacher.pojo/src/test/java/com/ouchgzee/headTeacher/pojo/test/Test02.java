/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.test;

import com.ouchgzee.headTeacher.pojo.status.IssueStatus;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK 1.7
 */
public class Test02 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IssueStatus valueOf = IssueStatus.valueOf("WAIT");
		System.out.println(valueOf.getValue() + "\t" + valueOf.getLabel());

		System.out.println("————————————————————————————————————————————");

		IssueStatus v2 = IssueStatus.getItem(1);
		System.out.println(v2.getValue() + "\t" + v2.getLabel());
	}

}
