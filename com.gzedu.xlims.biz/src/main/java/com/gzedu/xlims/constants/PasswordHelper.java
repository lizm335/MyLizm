/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.constants;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 *
 */
@Service
public class PasswordHelper {
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

	public void encryptPassword(GjtUserAccount user) {

		user.setSalt(randomNumberGenerator.nextBytes().toHex());

		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();

		user.setPassword(newPassword);
	}

	public static void main(String args[]) {

		PasswordHelper helper = new PasswordHelper();
		String npd = helper.encryptPassword("123456");

		System.out.println(npd);
	}
}
