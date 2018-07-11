package com.ruijie.startup;

import com.ruijie.service.DemoAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 系统启动的时候执行初始化操作
 * 当出现多个启动操作类，order代表优先级
 */
@Component
@Order(value = 1)
public class DemoStartupRunner implements CommandLineRunner{

    @Autowired
    private DemoAsyncService demoAsyncService;

    @Override
    public void run(String... strings) throws Exception {
        // 应用启动时的业务逻辑写在这
//        demoAsyncService.testCustomAsyn();
    }
}
