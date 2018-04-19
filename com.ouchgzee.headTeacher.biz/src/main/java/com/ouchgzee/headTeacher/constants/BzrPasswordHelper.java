/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.constants;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 *
 */
@Deprecated @Service("bzrPasswordHelper") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class BzrPasswordHelper {
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	@Value("md5")
	private String algorithmName = "md5";
	@Value("2")
	private int hashIterations = 2;

	public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}

	public String encryptPassword(String password) {

		String salt = randomNumberGenerator.nextBytes().toHex();

		String newPassword = new SimpleHash(algorithmName, password, salt, hashIterations).toHex();

		return newPassword;
	}

	public String encryptPassword(String password, String salt) {

		String newPassword = new SimpleHash(algorithmName, password, salt, hashIterations).toHex();

		return newPassword;
	}

	public void encryptPassword(BzrGjtUserAccount user) {

		user.setSalt(randomNumberGenerator.nextBytes().toHex());

		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();

		user.setPassword(newPassword);
	}

	public static void main(String args[]) {

		BzrPasswordHelper helper = new BzrPasswordHelper();
		String npd = helper.encryptPassword("123", "b429331aa3b95e710bc0f5a766b1a920");

		System.out.println(npd);
	}
}
