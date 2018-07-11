package com.ruijie.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步调用（多线程）
 * 配合@Async进行使用
 * 可以通过@bean定义多个线程池，然后在@Async的value进行指定，若不指定默认，使用getAsyncExecutor()方法生成的线程池
 */
@Configuration
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutorConfig.class);

    @Value("${taskExecutor.corePoolSize}")
    private int corePoolSize;

    @Value("${taskExecutor.maxPoolSize}")
    private int maxPoolSize;

    @Value("${taskExecutor.queueCapacity}")
    private int queueCapacity;

    @Value("${taskExecutor.keepAliveSeconds}")
    private int keepAliveSeconds;

    /**
     *自定义线程池
     * @return
     */
    @Bean(name = "customTaskExecutor")
    public Executor customTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setThreadNamePrefix("custom-task-executor-");
        // rejection-policy：当pool已经达到max size的时候——不在新线程中执行任务，而是由调用者所在的线程来执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * 默认线程池
     * @return
     * ThreadPoolTaskExecutor
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setThreadNamePrefix("default-task-executor-");
        // rejection-policy：当pool已经达到max size的时候——不在新线程中执行任务，而是由调用者所在的线程来执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     *
     */
    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            logger.info("Exception message - {} " , throwable.getMessage());
            logger.info("Method name - {}" , method.getName());
            for (Object param : obj) {
                logger.info("Parameter value - {} " , param);
            }
        }

    }

}
