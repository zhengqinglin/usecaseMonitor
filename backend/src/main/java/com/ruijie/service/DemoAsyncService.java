package com.ruijie.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 实现多线程异步操作
 */
@Service
public class DemoAsyncService {

    @Async(value = "customTaskExecutor")
    public void testCustomAsyn(){
        System.out.println("当前正在执行异步操作，线程名称："+Thread.currentThread().getName()+"线程ID："+Thread.currentThread().getId());
    }

    @Async
    public void testDefaultAsyn(){
        System.out.println("当前正在执行异步操作，线程名称："+Thread.currentThread().getName()+"线程ID："+Thread.currentThread().getId());
    }
}
