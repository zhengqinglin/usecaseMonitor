package com.ruijie.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class DemoScheduleService {

    /**
     * 代表上一次开始执行时间点后fixedRate毫秒再次执行
     * fixedRate、initialDelay单位毫秒
     * initialDelay：代表第一次延迟initialDelay毫秒执行
     */
//    @Scheduled(fixedRate = 1000, initialDelay = 1000)
    public void testFixedRate(){
        System.out.println("正在以fixedRate方式执行定时任务，当前线程名称："+Thread.currentThread().getName()+"线程ID："+Thread.currentThread().getId());
    }

    /**
     * 按cron规则执行
     * 5秒执行一次
     */
//    @Scheduled(cron = "5 * * * * ?")
    public void testCron(){
        System.out.println("正在以cron方式执行定时任务，当前线程名称："+Thread.currentThread().getName()+"线程ID："+Thread.currentThread().getId());
    }

    /**
     * 上一次执行完毕时间点后fixedDelay毫秒再次执行
     * fixedDelay、initialDelay单位毫秒
     * initialDelay：代表第一次延迟initialDelay毫秒执行
     */
//    @Scheduled(fixedDelay = 1000,initialDelay = 1000)
    public void testFixedDelay(){
        System.out.println("正在以fixedDelay方式执行定时任务，当前线程名称："+Thread.currentThread().getName()+"线程ID："+Thread.currentThread().getId());
    }
}
