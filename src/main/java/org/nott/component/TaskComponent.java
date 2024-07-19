package org.nott.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nott
 * @date 2024-7-16
 */

@Component
@EnableScheduling
@Slf4j
public class TaskComponent {

    // 接收cron表达式作为@Scheduled参数
    // 下列表达式标识每五秒执行一次
    @Scheduled(cron = "0/5 * * * * ? ")
    public void originTask(){
      log.info("原始定时任务执行cron");
    }

    // 接收fixedDelay作为@Scheduled参数
    // 下列方法每延时五秒执行一次
    // 根据上一个方法结束开始计时
    @Scheduled(fixedDelay = 5000)
    public void fixedDelay(){
        log.info("原始定时任务执行delay");
    }

    // 接收fixedRate作为@Scheduled参数
    // 下列方法每间隔五秒执行一次
    //  根据上次任务开始时计时，假如中间任务花费了2.5秒，即+2.5秒开始执行
    // 假如间隔5秒，在单线程执行的情况下，A1任务执行7秒，A1还没执行完，A2会开始执行，此时A2会出现阻塞
    @Scheduled(fixedRate = 5000)
    public void fixedRate(){
        log.info("原始定时任务执行rate");
    }
}
