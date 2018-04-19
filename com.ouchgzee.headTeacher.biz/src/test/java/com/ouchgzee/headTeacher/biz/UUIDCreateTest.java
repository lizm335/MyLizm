
package com.ouchgzee.headTeacher.biz;

import com.gzedu.xlims.common.UUIDUtils;

public class UUIDCreateTest {

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			System.out.println(UUIDUtils.random());
		}
		System.out.println(UUIDUtils.random());
	}

}
