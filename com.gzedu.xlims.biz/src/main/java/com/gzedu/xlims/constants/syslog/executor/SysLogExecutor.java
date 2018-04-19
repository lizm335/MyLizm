package com.gzedu.xlims.constants.syslog.executor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 系统日志线程池，单例模式<br/>
 *
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年07月25日
 */
public class SysLogExecutor {

    private static final int IDLE_TIME_MILLISECOND = 10*60*1000; // 空闲时间 单位：毫秒

    private static ExecutorService executorService = null;

    private static Timer timer = null; // 定时释放资源

    /**
     * 添加线程任务执行
     * @param r
     */
    public static void execute(Runnable r) {
        getInstance().execute(r);
        shutdownTimeout();
    }

    private static ExecutorService getInstance() {
        if (executorService == null || executorService.isTerminated()) {
            synchronized (SysLogExecutor.class) {
                if (executorService == null || executorService.isTerminated()) {
//                    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                    executorService = Executors.newCachedThreadPool();
                }
            }
        }
        return executorService;
    }

    /**
     * 在一定范围内线程池未被使用时，释放资源
     */
    private static void shutdownTimeout() {
        if(timer != null) timer.cancel(); // 终止此计时器，丢弃所有当前已安排的任务。
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getInstance().shutdown();
            }
        }, IDLE_TIME_MILLISECOND);
    }

}
