package com.gzedu.xlims.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.StringTokenizer;

public class HttpUtil {

	public static String postData(String url, String params, String encode) {
		String ret = "";
		try {
			URL posturl = new URL(url);
			URLConnection connection = posturl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			OutputStreamWriter out = null;
			if (encode != null) {
				out = new OutputStreamWriter(connection.getOutputStream(), encode);
			} else {
				out = new OutputStreamWriter(connection.getOutputStream());
			}
			out.write(params);
			out.flush();
			out.close();
			String line;
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = in.readLine()) != null) {
				ret += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String postForm(String sendurl, String encode) {
		String theString = "";

		try {
			URL url = new URL(sendurl);
			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);

			OutputStream raw = uc.getOutputStream();
			OutputStream buf = new BufferedOutputStream(raw);
			OutputStreamWriter out = new OutputStreamWriter(buf, encode);
			out.flush();
			out.close();

			InputStream in = uc.getInputStream();
			in = new BufferedInputStream(in);

			String result = "";
			StringBuffer strbf = new StringBuffer("");
			try {
				BufferedReader bfreader = new BufferedReader(new InputStreamReader(in));
				while ((result = bfreader.readLine()) != null) {
					strbf.append(result);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				in.close();
				theString = strbf.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return theString;
	}

	public static String getData(String url, String params) {
		String ret = "";
		try {
			URL posturl = new URL(url + "?" + params);
			URLConnection connection = posturl.openConnection();
			connection.setDoInput(true);
			String line;
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = in.readLine()) != null) {
				ret += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static String encode(String params, String encode) throws UnsupportedEncodingException {
		if ((params == null) || "".equals(params)) {
			return "";
		}
		if (encode == null) {
			return params;
		}
		StringTokenizer stk = new StringTokenizer(params, "&");
		StringBuffer bfurl = new StringBuffer();
		for (; stk.hasMoreTokens();) {
			String pd = stk.nextToken();
			if ((pd != null) && !pd.equals("")) {
				if (pd.indexOf("=") > -1) {
					int pos = pd.indexOf("=");
					bfurl.append("&" + pd.substring(0, pos + 1));
					bfurl.append(URLEncoder.encode(pd.substring(pos + 1), encode));
				} else {
					bfurl.append("&" + pd);
				}
			}
		}
		return bfurl.toString().substring(1);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String params = "formMap.LEARNCENTER_TYPE=O";
		String s = HttpUtil.postData("http://eomp.oucnet.cn/interface/learncenter/getLearncenter.do", params, "UTF-8");
		System.out.println(s);
	}

}
