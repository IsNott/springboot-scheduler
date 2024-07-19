package org.nott.configuration;

import org.nott.handler.MyTaskErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Nott
 * @date 2024-7-17
 */

@Configuration
public class TaskConfiguration {

    @Bean(name = "myTaskScheduler")
    public ThreadPoolTaskScheduler setThreadPoolTaskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("task-pool-");
        // 执行shutdown时，等待前一个任务执行完
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 执行shutdown时，等待的超时时间
        taskScheduler.setAwaitTerminationSeconds(30);
        // 自定义错误处理器，当某个线程执行时出现错误会跳过线程内部的Try/catch，进入ErrorHandler
        // 需要实现 ErrorHandler接口
//        taskScheduler.setErrorHandler(new MyTaskErrorHandler());
        return taskScheduler;
    }
}
