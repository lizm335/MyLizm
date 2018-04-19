package com.gzedu.xlims.common; 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzedu.xlims.common.gzedu.EmptyUtils;

public class ToolUtil {
	
	private static Pattern changeNamePattern = Pattern.compile("[a-z\\d]{1}_[a-z]{1}");
	
	private static Pattern formatNamePattern = Pattern.compile("[a-z\\d]{1}[A-Z]{1}");

	/**
	 * 把啊啦伯数字转换成中文表示  
	 * @param number
	 * @return
	 */
	public static String convertToChina(long number){
		String result = null;
		
		StringBuffer buf = new StringBuffer(20);
		
		String[] basicNums = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"}; //中文数字
		String[] basicUnits = {"", "十", "百", "千"}; //四位数内的基本单位
		String[] heightUnits = {"", "万", "亿"}; //四位数的高级单位
		String zero = basicNums[0]; //零
		String zeroReg = zero.concat("+"); //把多个零替换成一个零的正则表达式
		
		long targetV = number; //从阿拉伯数字转换成中文数字的值
		long fsptorV = 10000; //以四个数字为转换单位
		long fmodV = 0; //千位余数
		int fcount = 0; //千位次数
		long tmpV = 0; //暂时保存千位余数值
		long tsptorV = 10; //10的余数为隔值
		long tmodV = 0; //十位余数
		int tcount = 0; //十位次数
		
		if(number > 0){
			do{
				fmodV = targetV % fsptorV; //取出四位数值
				targetV = targetV / fsptorV;
				tmpV = fmodV;
				tcount = 0; //初始值
				
				//一个四位数的高级单位
				buf.insert(0, heightUnits[fcount++]);
				
				String tmpStr = "";
				
				do{
					tmodV = tmpV % tsptorV; //取出一位数值
					tmpV = tmpV / tsptorV;
					String chaNum = basicNums[(int)tmodV]; //取出与其对应的中文数值
					
					boolean isZero = zero.equals(chaNum);
					
					if(isZero == false){//如果不为零时才加入四位数内的基本单位
						tmpStr = basicUnits[tcount] + tmpStr;
					}
					
					if((isZero && tmpStr.length() == 0) == false){//为值为零并且在此之前还没有其它数值的相反情况下才增加数值 
						tmpStr = chaNum + tmpStr;
					}
					
					//如果有多个连续的零只取一个
					tmpStr = tmpStr.replaceAll(zeroReg, zero);
					tcount++;
				}while(tmpV > 0);
				
				//因为数值的表示是最左边是最大的
				//所以每一次添加都要插入在首位
				buf.insert(0, tmpStr);
			}while(targetV > 0);
			
			result = buf.toString();
			result = result.replaceAll(zeroReg, zero);
		}else{
			result = zero;
		}
		
		return result;
	}
	
	/**
	 * 把字符串任一部分转换成大写
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String toUpperCase(String str, int start, int end){
		String part = str.substring(start, end);
		part = part.toUpperCase();
		
		String result = str.substring(0, start) + part + str.substring(end, str.length());
		
		return result;
	}
	
	/**
	 * 把字符串数组转变成Map对象
	 * @param strs
	 * @return
	 */
	public static Map createMap(String... strs){
		Map map = new HashMap();
		
		for(int i = 0; i < strs.length; i+=2){
			String key = strs[i];
			String value = strs[i + 1];
			
			map.put(key, value);
		}
		
		return map;
	}
	
	/**
	 * 把字符串任意的一部分替换，不支持正则表达式
	 * @param str
	 * @param fromStr
	 * @param toStr
	 * @return
	 */
	public static String replaceFirst(String str, String fromStr, String toStr){
		String result = str;
		
		int p = str.indexOf(fromStr);
		
		if(p != -1){
			String head = str.substring(0, p);
			String tail = str.substring(p + fromStr.length());
			
			result = head + toStr + tail;
		}
		
		return result;
	}
	
	/**
	 * 把表字段的命名规则转换成Java的命名规则
	 * @param name
	 * @return
	 */
	public static String changeName(String name){
		String reName = name.toLowerCase();
		
		Matcher matcher = changeNamePattern.matcher(reName);
		
		while(matcher.find()){
			String str = matcher.group();
			String[] strs = str.split("_");
			reName = replaceFirst(reName, str, strs[0].concat(strs[1].toUpperCase()));
		}
		
		return reName;
	}
	
	/**
	 * 把Java的命名规则转换成表的字段的命名规则
	 * @param name
	 * @return
	 */
	public static String formatName(String name){
		String reName = name;
		
		Matcher matcher = formatNamePattern.matcher(reName);
		
		while(matcher.find()){
			String str = matcher.group();
			String a = str.substring(0, 1);
			String b = str.substring(1, str.length());
			reName = reName.replaceAll(str, a + "_" + b.toLowerCase());
		}
		
		return reName;
	}
	
	/**
	 * 返回数字随机数字
	 * @param pwd_len 随机数长度
	 * @return 返回数字随机数字
	 */
	public static String getRandomInt(int pwd_len) {
		final int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			i = Math.abs(r.nextInt(maxNum));

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}
	
	/**
	 * 获取用户IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		try {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			if (ip.indexOf(",") > -1) {
				ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length());
			}
			return ip.trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static boolean isNumeric(String str){
		return isInteger(str) || isDouble(str);
	}
	
	// a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    
    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
     try {
      Integer.parseInt(value);
      return true;
     } catch (NumberFormatException e) {
      return false;
     }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
     try {
      Double.parseDouble(value);
      if (value.contains("."))
       return true;
      return false;
     } catch (NumberFormatException e) {
      return false;
     }
    }
    
    /**
     * 四舍五入取整 
     * @param dSource
     * @return
     */
	public static int getIntRound(double dSource) {
		int iRound = 0;;
		try {
			BigDecimal deSource = new BigDecimal(dSource);
			iRound = deSource.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			iRound = 0;
		}
		return iRound;
	}
	
	/***
	 * 四舍五入保留小数
	 * @param formatStr 0.0 0.00 
	 * @param dSource
	 * @return
	 */
	public static double getNumberFormat(String formatStr, double dSource) {
		try {
			DecimalFormat df = new DecimalFormat(formatStr); 
			return Double.parseDouble(df.format(dSource));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	/**
	 * 年月日+6位随机数
	 * @return
	 */
	public static String getRoundomNumber(){
		String roundStr = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String str = sdf.format(date);
			roundStr = str + getFixString(6);
		}catch(Exception e){
			e.printStackTrace();
		}
		return roundStr;
	}
	
	
	public static String getFixString(int length){
		Random rd = new Random();
		//取得随机数
		double unSure = (1+rd.nextDouble())*Math.pow(10, length);
		//将获得的获得随机数转化为字符串 
		String fixLengthString = String.valueOf(unSure);
		//返回固定的长度的随机数
		return fixLengthString.substring(1, length+1);
	}
	
	/**
	 * 文件下载- 另存为
	 * @param fullpath
	 */
	public static void download(String fullpath, HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(fullpath);
			if (!file.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			if (file.isDirectory()) {
				response.sendError(404, "File is directory!");
				return;
			}
			// 取得文件名。
			String filename = file.getName();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(filename.getBytes("GBK"), "iso-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType("application/octet-stream");
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/***
	 * Clob 转字符串
	 * @param clobStr
	 * @return
	 */
	public static String getClobStr(Clob clobStr ) {
		String reString = "";
		try {
			Reader is = clobStr.getCharacterStream();
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while(s !=null){
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reString;
	}
	
	
	/**
	 * 获取随机数
	 * @return
	 * @throws Exception
	 */
	public static int RandomNumBoth(int minnumber,int maxnumber) throws Exception
	{
		return (int) Math.round(Math.random()*(maxnumber-minnumber)+minnumber);
	}
	
	/**
	 * 获取几个随机数
	 * @param n
	 * @param minnumber
	 * @param maxnumber
	 * @return
	 * @throws Exception
	 */
	public static String RndNum(int n,int minnumber,int maxnumber) throws Exception
	{
		String string="";
		int uprndcount=0;
		for(int i=0;i<n;i++)
		{
			int rndcount=0;
			
			if(uprndcount==0
				&&minnumber<30
					&&maxnumber>=60)
			{
				rndcount=RandomNumBoth(30,60);

			}else if (uprndcount>30 
						&& uprndcount<120
							&&minnumber<120
								&&maxnumber>=300)
			{
				rndcount=RandomNumBoth(120,300);
			}
			else if (uprndcount>60 
						&& uprndcount<420
							&&minnumber<420
								&&maxnumber>=800)
			{
				rndcount=RandomNumBoth(420,800);
			}else
			{
				if(minnumber<800&&maxnumber>=800)
				{
					rndcount=RandomNumBoth(800,maxnumber);
				}else
				{
					rndcount=RandomNumBoth(minnumber,maxnumber);	
				}
			}
			
			if(rndcount>uprndcount)
			{
				string+=rndcount+",";
				uprndcount=rndcount;
			}else
			{
				i--;
			}
		}
		
		if (EmptyUtils.isNotEmpty(string)) 
		{
			string=string.substring(0,string.length()-1);
		}
		
		return string;
	}
	
	public static StringBuffer getTraceInfo(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = e.getStackTrace();
		for (int i = 0; i < stacks.length; i++) {
			sb.append("class: ");
			sb.append(stacks[i].getClassName());
			sb.append("; method: ");
			sb.append(stacks[i].getMethodName());
			sb.append("; line: ");
			sb.append(stacks[i].getLineNumber());
			sb.append(";  Exception: ");
			break;
		}
		return sb;
	}
	
	public static String getExceptionMessage(Exception e) 
	{
		if(EmptyUtils.isNotEmpty(e))
		{
			String message = e.toString();
			if (message.lastIndexOf(":") != -1)
			{
				message = message.substring(0, message.lastIndexOf(":"));
			}
			String errorstr=getTraceInfo(e).append(message).toString();
			if(errorstr.length()>4000)
			{
				errorstr=errorstr.substring(0,500)+errorstr.substring(errorstr.length()-500,errorstr.length());
			}
			return errorstr;
		}else {
			return "错误信息为空";
		}
	}
	
	public static String convertStreamToString(InputStream is) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	public static String getRandomNum(int length) {
		String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val.toUpperCase();  
	}
	
	public static void main(String[] args) {
		System.out.println(getRandomNum(8));
	}
	
}
