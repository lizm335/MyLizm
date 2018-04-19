package com.gzedu.xlims.constants.syslog.executor;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.pojo.SysLogEntity;
import com.gzedu.xlims.service.SysLogService;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * 添加系统日志的线程<br/>
 *
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年07月25日
 */
public class SysLogRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SysLogRunnable.class);

    private JoinPoint joinPoint;

    private SysLogService sysLogService;

    private SysLogEntity sysLog;

    private Map<String, String[]> requestParams; // 请求参数

    public SysLogRunnable(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
        this.sysLog = new SysLogEntity();
    }

    public SysLogRunnable(JoinPoint joinPoint, SysLogService sysLogService) {
        this.joinPoint = joinPoint;
        this.sysLogService = sysLogService;
        this.sysLog = new SysLogEntity();
    }

    @Override
    public void run() {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysLog syslog = method.getAnnotation(SysLog.class);
            if(syslog != null){
                //注解上的描述
                sysLog.setOperation(syslog.value());
            }

            // 请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            sysLog.setMethod(className + "." + methodName + "()\n\r<p></p>[" + sysLog.getMethod() + "]");

            // 拼接请求的参数
            Map<String, String> args = new TreeMap<String, String>();
            if(this.requestParams != null) {
                for (Map.Entry<String, String[]> entry : this.requestParams.entrySet()) {
                    String name = entry.getKey();
                    String[] values = entry.getValue();
                    String valueStr = "";
                    for (int i = 0; i < values.length; i++) {
                        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                    }
                    // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                    // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                    args.put(name, valueStr);
                }
            }
            StringBuilder params = new StringBuilder("Params");
            params.append(JSON.toJSONString(args));

            /*// 是否导入了Excel文件
            InputStream excelIs = null;
            Object[] methodArgs = joinPoint.getArgs();
            for (int i = 0; i < methodArgs.length; i++) {
                Object arg = methodArgs[i];
                if(arg instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) arg;
                    if(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx")) {
                        try {
                            excelIs = file.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            if(excelIs != null) {
                List<List<String>> list = ExcelUtils.readExcelToList(excelIs);
                try {
                    excelIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.append("\n\r\n\r<p></p>");
                params.append("Excel:<table border='1px'>");
                for (List<String> row : list) {
                    params.append("<tr>");
                    for (String col : row) {
                        params.append("<td>");
                        params.append(col);
                        params.append("</td>");
                    }
                    params.append("</tr>");
                }
                params.append("</table>");
            }*/
            sysLog.setParams(params.toString());

            //用户名
            String username = Objects.toString(SecurityUtils.getSubject().getPrincipal(), "");
            sysLog.setUsername(username);

            //保存系统日志
            sysLogService.insert(sysLog);
        } catch (Exception e) {
            log.error(Thread.currentThread().getName() + " save syslog fail 保存系统日志失败:{}", e.getMessage());
        }
    }

    public SysLogEntity getSysLog() {
        return sysLog;
    }

    public Map<String, String[]> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String[]> requestParams) {
        this.requestParams = requestParams;
    }

}
