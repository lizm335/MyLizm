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
public class Task implements Runnable {
	private String name;

	public Task(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		for (int i = 0; i < 50000; i++) {
			System.out.println(name + "运行  :  " + i);
			try {
				Thread.sleep((int) Math.random() * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
