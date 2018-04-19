package com.gzedu.xlims.common;

import java.math.BigDecimal;

/** 
 * @ClassName: MathTools 
 * @Description: 精确数学运算工具类 
 */
public class MathUtils {

	/**
	 * @Title: add 
	 * @Description: 精确加法运算
	 * @param d1	被加数
	 * @param d2	加数
	 * @return
	 */
	public static BigDecimal add(double d1,double d2){
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.add(bd2);
	}
	
	/**
	 * @Title: sub 
	 * @Description: 精确减法运算
	 * @param d1	被减数
	 * @param d2	减数
	 * @return
	 */
	public static BigDecimal sub(double d1,double d2){
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.subtract(bd2);
	}
	
	/**
	 * @Title: mul 
	 * @Description: 精确乘法运算
	 * @param d1	被乘数
	 * @param d2	乘数
	 * @return
	 */
	public static BigDecimal mul(double d1,double d2){
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.multiply(bd2);
	}
	
	/**
	 * @Title: div 
	 * @Description: 默认精确除法运算,四舍五入,保留两位小数
	 * @param d1	被除数
	 * @param d2	除数
	 * @return
	 */
	public static BigDecimal div(double d1,double d2){
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.divide(bd2,2,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * @Title: div 
	 * @Description: 默认精确除法运算,四舍五入
	 * @param d1	被除数
	 * @param d2	除数
	 * @param len	保留小数位数
	 * @return
	 */
	public static BigDecimal div(double d1,double d2,int len){
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.divide(bd2,2,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * @Title: div 
	 * @Description: 精确除法运算
	 * @param d1		被除数
	 * @param d2		除数
	 * @param len		保留位数
	 * @param roundType	舍入类型
	 * @return
	 */
	public static BigDecimal div(double d1,double d2,int len,int roundType){
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.divide(bd2,len,roundType);
	}
	
	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param d
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */

	public static double round(double d, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(d));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 去尾法
	 * 
	 * @param amount
	 * @param scale
	 * @return
	 */
	public static double trunc(double amount, int scale) {

		BigDecimal b = new BigDecimal(Double.toString(amount));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_DOWN).doubleValue();

	}

	/**
	 * 进一法
	 * 
	 * @param amount
	 * @param scale
	 * @return
	 */
	public static double ceil(double amount, int scale) {
		BigDecimal b = new BigDecimal(Double.toString(amount));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_CEILING).doubleValue();
	}
	
}
