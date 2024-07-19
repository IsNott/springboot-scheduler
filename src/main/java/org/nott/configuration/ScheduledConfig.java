package org.nott.configuration;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nott.model.TaskInfo;
import org.nott.service.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Nott
 * @date 2024-7-19
 */
@Slf4j
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {

    @Resource
    private TaskService taskService;

    @Bean(name = "myExecutor")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        List<TaskInfo> taskInfos = taskService.queryStableCronTask();
        for (TaskInfo info : taskInfos) {
            // addCronTask(Runnable task, String expression)
            // 接收一个Runnable对象，和执行的表达式
            taskRegistrar.addCronTask(taskService.getRunnableTask(info),
                    info.getCron());
            log.info("添加新定时任务id{},Cron-》{}",info.getId(),info.getCron());
        }
    }
}
