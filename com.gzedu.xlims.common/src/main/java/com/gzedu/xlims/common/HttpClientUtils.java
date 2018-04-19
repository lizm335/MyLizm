package com.gzedu.xlims.common;

import com.gzedu.xlims.common.exception.ServiceException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

/**
 * HTTP请求工具类<br/>
 * 注：统一使用这个工具类<br/>
 * com.gzedu.xlims.common.HttpClientUtils<br/>
 */
public class HttpClientUtils {

	private static final Log log = LogFactory.getLog(HttpClientUtils.class);

	private static Integer SC_OK = 200;
	private static Integer SC_NOT_EXISTS = 404;
	private static Integer SC_SERVER_EXCEPTION = 500;
	private static Integer SC_SERVER_EXCEPTION2 = 502;

	/**
	 * get
	 *
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String doHttpGet(String url, Map params, int timeout, String encode) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset(encode);
		StringBuffer sb = new StringBuffer();
		if (params != null && !params.isEmpty()) {
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				sb.append(entry.getKey().toString() + "=" + entry.getValue().toString() + "&");
			}

			url = url + "?" + sb.substring(0, sb.length() - 1);
		}
		log.info(">>>> 请求接口地址为：" + url);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == SC_OK) {
				byte[] responseBody = getMethod.getResponseBodyAsString().getBytes(encode);
				responseMsg = new String(responseBody);
				log.info(">>>> 接口地址返回:" + responseMsg);
			} else if (statusCode == SC_NOT_EXISTS) {
				throw new ServiceException("对方服务器未找到");
			} else if (statusCode == SC_SERVER_EXCEPTION || statusCode == SC_SERVER_EXCEPTION2) {
				byte[] responseBody = getMethod.getResponseBodyAsString().getBytes(encode);
				responseMsg = new String(responseBody);
				log.error("=====> 远程调用失败[" + statusCode + ":远程服务器内部异常。 =====> " + responseMsg);
				throw new ServiceException("对方服务器内部异常");
			}
		} catch (Exception e) {
			log.error(">>>> 调用接口异常");
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return responseMsg;
	}

	public static String doHttpGetStream(String url, Map params, int timeout, String encode) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset(encode);
		StringBuffer sb = new StringBuffer();
		if (params != null && !params.isEmpty()) {
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				sb.append(entry.getKey().toString() + "=" + entry.getValue().toString() + "&");
			}

			url = url + "?" + sb.substring(0, sb.length() - 1);
		}
		log.info(">>>> 请求接口地址为：" + url);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
			int num = httpClient.executeMethod(getMethod);
			if (num == 200) {
				InputStream in = getMethod.getResponseBodyAsStream();

				int c;
				byte[] buffer = new byte[1024];
				StringBuffer content = new StringBuffer(1024);
				String temp = null;

				while ((c = in.read(buffer)) != -1) {
					temp = new String(buffer, 0, c, encode);
					content.append(temp);
				}

				responseMsg = content.toString();
				log.info(">>>> 接口地址返回:" + responseMsg);
			}
		} catch (Exception e) {
			log.error(">>>> 调用接口异常");
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return responseMsg;
	}

	/**
	 * post
	 *
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String doHttpPost(String url, Map params, int timeout, String encode) {
		return doHttpPost(url, null, params, timeout, encode);
	}

	/**
	 * post
	 *
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String doHttpPost(String url, Map headers, Map params, int timeout, String encode) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset(encode);
		PostMethod postMethod = new PostMethod(url);
		if (headers != null && !headers.isEmpty()) {
			Iterator iterator = headers.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				postMethod.setRequestHeader(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		if (params != null && !params.isEmpty()) {
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				postMethod.addParameter(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == SC_OK) {
				responseMsg = postMethod.getResponseBodyAsString().trim();
			} else if (statusCode == SC_NOT_EXISTS) {
				throw new ServiceException("对方服务器未找到");
			} else if (statusCode == SC_SERVER_EXCEPTION || statusCode == SC_SERVER_EXCEPTION2) {
				responseMsg = postMethod.getResponseBodyAsString().trim();
				log.error("=====> 远程调用失败[" + statusCode + ":远程服务器内部异常。 =====> " + responseMsg);
				throw new ServiceException("对方服务器内部异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return responseMsg;
	}

	/***
	 * 发送xml文件
	 *
	 * @param url
	 * @param xmlString
	 * @param timeout
	 * @param encode
	 * @return
	 */
	public static InputStream doHttpPostXml(String url, String xmlString, int timeout, String encode)
			throws RuntimeException {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
		PostMethod postMethod = new PostMethod(url);
		try {
			postMethod.setRequestHeader("Content-Type", "text/xml,charset=" + encode);
			postMethod.setRequestEntity(new StringRequestEntity(xmlString, "text/xml", encode));
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == SC_OK) {
				InputStream is = postMethod.getResponseBodyAsStream();
				return is;
			} else if (statusCode == SC_NOT_EXISTS) {
				throw new ServiceException("对方服务器未找到");
			} else if (statusCode == SC_SERVER_EXCEPTION || statusCode == SC_SERVER_EXCEPTION2) {
				log.error("=====> 远程调用失败[" + statusCode + ":远程服务器内部异常。");
				throw new ServiceException("对方服务器内部异常");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * @param url
	 * @param xmlString
	 * @param timeout
	 * @param encode
	 * @return
	 */
	public static String doHttpPostXml1(String url, String xmlString, int timeout, String encode) {
		String responseString = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
		PostMethod postMethod = new PostMethod(url);
		try {
			postMethod.setRequestHeader("Content-Type", "text/xml,charset=" + encode);
			postMethod.setRequestEntity(new StringRequestEntity(xmlString, "text/xml", encode));
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == SC_OK) {
				InputStream is = postMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
				String tempbf;
				StringBuffer html = new StringBuffer(1000);
				while ((tempbf = br.readLine()) != null) {
					html.append(tempbf);
				}
				responseString = html.toString();
			} else if (statusCode == SC_NOT_EXISTS) {
			} else if (statusCode == SC_SERVER_EXCEPTION) {
				InputStream is = postMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
				String tempbf;
				StringBuffer html = new StringBuffer(1000);
				while ((tempbf = br.readLine()) != null) {
					html.append(tempbf);
				}
				responseString = html.toString();
				log.error("=====> 远程调用失败[" + HttpStatus.SC_INTERNAL_SERVER_ERROR + ":远程服务器内部异常。 =====> " + responseString);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;
	}

	/***
	 * 发送xml文件
	 *
	 * @param uri
	 * @param xmlString
	 * @param timeout
	 * @param charset
	 * @return
	 */
	public static String doHttpPostXml2(String uri, String xmlString, int timeout, String charset) {

		String responseString = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(uri);
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
			postMethod.setRequestHeader("Content-Type", "text/xml,charset=" + charset);
			postMethod.setRequestEntity(new StringRequestEntity(xmlString, "text/xml", charset));
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				byte[] data = postMethod.getResponseBody();
				responseString = EncodingUtil.getString(data, charset);
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				log.error("=====> 远程调用失败[" + HttpStatus.SC_NOT_FOUND + ":请求接口地址不存在。");
			} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				byte[] data = postMethod.getResponseBody();
				responseString = EncodingUtil.getString(data, charset);
				log.error("=====> 远程调用失败[" + HttpStatus.SC_INTERNAL_SERVER_ERROR + ":远程服务器内部异常。 =====> " + responseString);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;
	}

	/**
	 * post方式提交资料
	 *
	 * @return String
	 * @author xkqu
	 */
	public static String PostData(String url, String params, String enc) {
		String ret = "";
		try {
			URL posturl = new URL(url);
			URLConnection connection = posturl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			OutputStreamWriter out = null;
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(encode(params, enc));
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

	/**
	 * get方式提交数据
	 * @param url
	 * @param params
	 * @throws Exception
	 */
	public static String GetData(String url, String params) {
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

	/**
	 * 处理编码 xkqu
	 *
	 * @param params
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String encode(String params, String encode) throws UnsupportedEncodingException {
		if (params == null || "".equals(params))
			return "";
		if (encode == null)
			return params;
		StringTokenizer stk = new StringTokenizer(params, "&");
		StringBuffer bfurl = new StringBuffer();
		for (; stk.hasMoreTokens();) {
			String pd = stk.nextToken();
			if (pd != null && !pd.equals(""))
				if (pd.indexOf("=") > -1) {
					int pos = pd.indexOf("=");
					bfurl.append("&" + pd.substring(0, pos + 1));
					bfurl.append(URLEncoder.encode(pd.substring(pos + 1), encode));
				} else {
					bfurl.append("&" + pd);
				}
		}
		return bfurl.toString().substring(1);
	}

	/***
	 * @param url
	 * @param xmlString
	 * @param timeout
	 * @param encode
	 * @return
	 */
	public static String doHttpPostXml3(String url, String xmlString, int timeout, String encode) {
		String responseString = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

		PostMethod postMethod = new PostMethod(url);
		try {
			postMethod.setRequestHeader("Content-Type", "text/xml,charset=" + encode);
			postMethod.setRequestEntity(new StringRequestEntity(xmlString, "text/xml", encode));
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == SC_OK) {
				InputStream is = postMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, encode));
				String tempbf;
				StringBuffer html = new StringBuffer(1000);
				while ((tempbf = br.readLine()) != null) {
					html.append(tempbf);
				}
				responseString = html.toString();
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				log.error("=====> 远程调用失败[" + HttpStatus.SC_NOT_FOUND + ":请求接口地址不存在。");
			} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				log.error("=====> 远程调用失败[" + HttpStatus.SC_INTERNAL_SERVER_ERROR + ":远程服务器内部异常。");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;
	}

	/**
	 * 获取用户操作系统。
	 *
	 * @param req
	 * @return
	 */
	public static String getOperationSystem(HttpServletRequest req) {
		// 得到用户的操作系统
		String agent = req.getHeader("User-Agent"); // 客户端浏览器
		String osVersion = "其它";
		String useros = agent;

		if (useros.indexOf("NT 6.1") > 0)
			osVersion = "Windows 2007";
		else if (useros.indexOf("NT 6.2") > 0)
			osVersion = "Windows 2008";
		else if (useros.indexOf("NT 6.0") > 0)
			osVersion = "Windows Vista/Server 2008";
		else if (useros.indexOf("NT 5.2") > 0)
			osVersion = "Windows Server 2003";
		else if (useros.indexOf("NT 5.1") > 0)
			osVersion = "Windows XP";
		else if (useros.indexOf("NT 5") > 0)
			osVersion = "Windows 2000";
		else if (useros.indexOf("NT 4") > 0)
			osVersion = "Windows NT4";
		else if (useros.indexOf("Windows 4.9") > 0)
			osVersion = "Windows Me";
		else if (useros.indexOf("98") > 0)
			osVersion = "Windows 98";
		else if (useros.indexOf("95") > 0)
			osVersion = "Windows 95";
		else if (useros.indexOf("Mac") > 0)
			osVersion = "Mac";
		else if (useros.indexOf("Unix") > 0)
			osVersion = "UNIX";
		else if (useros.indexOf("Linux") > 0)
			osVersion = "Linux";
		else if (useros.indexOf("SunOS") > 0)
			osVersion = "SunOS";
		return osVersion;
	}

	/**
	 * 获取用户的浏览器名.
	 *
	 * @param req
	 * @return String
	 */
	public static String getBrowser(HttpServletRequest req) {
		String agent = req.getHeader("User-Agent"); // 客户端浏览器
		String browser = "";
		String sep = ";";
		if (agent.indexOf(sep) == -1) {
			sep = " ";
		}
		StringTokenizer st = new StringTokenizer(agent, sep);// Chrome 是空格分隔
		if (st.hasMoreTokens())
			st.nextToken();
		if (st.hasMoreTokens())
			browser = st.nextToken();
		String browserVersion = "其它";
		// 得到用户的浏览器名
		String userbrowser = agent;
		if (userbrowser.indexOf("MSIE") > 0)
			browserVersion = browser.replaceAll("MSIE", "IE");
		else if (userbrowser.indexOf("Firefox") > 0)
			browserVersion = "Firefox";
		else if (userbrowser.indexOf("Chrome") > 0)
			browserVersion = "Chrome";
		else if (userbrowser.indexOf("Safari") > 0)
			browserVersion = "Safari";
		else if (userbrowser.indexOf("Camino") > 0)
			browserVersion = "Camino";
		else if (userbrowser.indexOf("Konqueror") > 0)
			browserVersion = "Konqueror";
		return browserVersion;
	}
}
