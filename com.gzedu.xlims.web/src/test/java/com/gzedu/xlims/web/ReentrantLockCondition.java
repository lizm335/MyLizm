package com.gzedu.xlims.web;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by paul on 2017/8/18.
 */
public class ReentrantLockCondition implements  Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public static void main(String[] args) throws  InterruptedException {
        ReentrantLockCondition rc = new ReentrantLockCondition();
        System.out.println(" begin " + new Date());
        Thread t = new Thread(rc);
        t.start();
        Thread.sleep(2000);
        lock.lock();
        condition.signal();
        lock.unlock();
        System.out.println(" end " + new Date());
    }

    @Override
    public void run() {
        try {
            System.out.println(" 开始拿锁咧 ");
            lock.lock();
            condition.await();
            System.out.println(" thread is going on ....................  " + new Date());
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
