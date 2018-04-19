package com.ouchgzee.study.web.filter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.catalina.connector.CoyoteOutputStream;
import org.apache.catalina.connector.OutputBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzedu.xlims.common.constants.MessageCode;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class FilterResponse extends HttpServletResponseWrapper {
	
	private static final Log log = LogFactory.getLog(FilterResponse.class);
	
	private boolean hasWrite = false;

	public FilterResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		ServletOutputStream sos = null;
		try {
			sos = FilterOutputStreamProxy.getInstance(super.getOutputStream(), hasWrite);
			hasWrite = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sos = super.getOutputStream();
		}
		
		return sos;
	}
	
	static class FilterOutputStreamProxy implements MethodInterceptor {
		
		private FilterOutputStream filterOutputStream;
		
		private boolean hasWrite;
		
		private List<byte[]> datas = new ArrayList<byte[]>();

		public FilterOutputStreamProxy(FilterOutputStream filterOutputStream, boolean hasWrite) {
			this.filterOutputStream = filterOutputStream;
			this.hasWrite = hasWrite;
		}
		
		public static ServletOutputStream getInstance(ServletOutputStream outputStream, boolean hasWrite) throws IllegalArgumentException, IllegalAccessException {
			CoyoteOutputStream cos = (CoyoteOutputStream)outputStream;
			OutputBuffer ob = null;
			@SuppressWarnings("unchecked")
			Class<CoyoteOutputStream> clazz = (Class<CoyoteOutputStream>) cos.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String type = field.getType().toString();
				if (type.endsWith("OutputBuffer")) {
					field.setAccessible(true);
					ob = (OutputBuffer)field.get(cos);
				}
			}
			
			FilterOutputStream fos = new FilterOutputStream();
			fos.setOb(ob);
			
			FilterOutputStreamProxy proxy = new FilterOutputStreamProxy(fos, hasWrite);
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(fos.getClass());
			enhancer.setCallback(proxy);
			
			return (ServletOutputStream)enhancer.create();
		}

		@Override
		public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if ("write".equals(method.getName())) {
				byte[] data = (byte[])args[0];
				int start = (Integer)args[1];
				int lenth = (Integer)args[2];
				
				datas.add(Arrays.copyOfRange(data, start, lenth));
				
				return null;
			} else if ("flush".equals(method.getName())) {
				if (!hasWrite) {
					filterOutputStream.write("{".getBytes());
					filterOutputStream.write("\"msgCode\":".getBytes());
					filterOutputStream.write((String.valueOf(MessageCode.RESP_OK.getMsgCode())).getBytes());
					filterOutputStream.write(",".getBytes());
					filterOutputStream.write("\"message\":".getBytes());
					filterOutputStream.write(("\"" + MessageCode.RESP_OK.getMessage() + "\"").getBytes("UTF-8"));
					filterOutputStream.write(",".getBytes());
					filterOutputStream.write("\"data\":".getBytes());
					
					for (int i = 0; i < datas.size(); i++) {
						filterOutputStream.write(datas.get(i));
					}

					filterOutputStream.write("}".getBytes());
					
					hasWrite = true;
				}
				
				return method.invoke(filterOutputStream, args);
			}
			
			return method.invoke(filterOutputStream, args);
		}
		
	}

}
