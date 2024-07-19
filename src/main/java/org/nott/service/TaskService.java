package org.nott.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nott.enums.PeriodUnit;
import org.nott.enums.TaskMode;
import org.nott.enums.TaskStatus;
import org.nott.global.TaskInfoHolder;
import org.nott.mapper.TaskInfoMapper;
import org.nott.model.TaskInfo;
import org.nott.param.TaskExecuteParam;
import org.nott.param.TaskParam;
import org.nott.utils.SpringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Nott
 * @date 2024-7-17
 */
@Service
@Slf4j
public class TaskService extends ServiceImpl<TaskInfoMapper, TaskInfo> {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Resource
    private TaskExecuteRecordService taskExecuteRecordService;

    public Collection<TaskInfo> getTaskList() {
        return TaskInfoHolder.TASK_INFO_MAP.values();
    }


    public List<TaskInfo> queryStableCronTask(){
        LambdaQueryWrapper<TaskInfo> wrapper = new LambdaQueryWrapper<TaskInfo>().eq(TaskInfo::getExecuteMode, TaskMode.CRON.getCode())
                .isNotNull(TaskInfo::getCron);
        return this.list(wrapper);
    }

    public void run(String id){
        this.run(id,false);
    }

    public void run(String id,boolean isAsync){
        TaskInfo taskInfo = this.getById(id);
        Objects.requireNonNull(taskInfo);
        if(isAsync){
            Runnable runnable = this.getRunnableTask(taskInfo);
            threadPoolTaskScheduler.execute(runnable);
        }else {
            this.executeAbleTask(taskInfo);
        }
    }

    public boolean cancelTask(String id) {
        if (!TaskInfoHolder.TASK_INFO_MAP.containsKey(id)) {
            return false;
        }

        this.removeById(id);
        TaskInfoHolder.SCHEDULE_FUTRUE_MAP.remove(id);
        TaskInfoHolder.TASK_INFO_MAP.remove(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTask(TaskParam param) {
        if(StringUtils.isEmpty(param.getBeanName()) && StringUtils.isEmpty(param.getClassName())){
            throw new RuntimeException("传入的任务参数有误");
        }

        TaskInfo info = new TaskInfo();
        BeanUtils.copyProperties(param, info);
        info.setId(UUID.randomUUID().toString());
        info.setCreateTime(new Date());
        this.save(info);
        this.putTaskMap(info, null);
        if(param.isScheduleNow()){
            ScheduledFuture<?> future = this.scheduleTaskByMode(info);

            if(future == null){
                throw new RuntimeException("传入的任务参数调度失败");
            }
        }
    }

    private ScheduledFuture<?> scheduleTaskByMode(TaskInfo info) {
        Integer mode = info.getExecuteMode();
        Timestamp executeTime = info.getExecuteTime();
        Long period = info.getPeriod();
        String periodUnit = info.getPeriodUnit();
        // ScheduledFuture接口实现类内部附带cancel方法，遇到方法需要修改时可cancel后重新调度
        ScheduledFuture<?> future;
        switch (mode) {
            default -> future = null;
            case 0 -> {
                future = threadPoolTaskScheduler.schedule(getRunnableTask(info), new CronTrigger(info.getCron()));
                break;
            }
            case 1 -> {
                future = threadPoolTaskScheduler.schedule(getRunnableTask(info), executeTime.toInstant());
                break;
            }
            case 2 -> {
                future = threadPoolTaskScheduler.scheduleAtFixedRate(getRunnableTask(info), Duration.of(period, PeriodUnit.getByName(periodUnit)));
                break;
            }
            case 3 -> {
                future = threadPoolTaskScheduler.scheduleWithFixedDelay(getRunnableTask(info), executeTime.toInstant(), Duration.of(period, PeriodUnit.getByName(periodUnit)));
                break;
            }
        }
        log.info("调度一个任务：\n{}",info.toString());
        this.putTaskMap(info, future);
        return future;
    }

    private void putTaskMap(TaskInfo info, ScheduledFuture<?> future) {
        TaskInfoHolder.TASK_INFO_MAP.put(info.getId(), info);
        if (future != null) {
            TaskInfoHolder.SCHEDULE_FUTRUE_MAP.put(info.getId(), future);
        }
    }

    public Runnable getRunnableTask(TaskInfo info) {
        return () -> {
            long s = System.currentTimeMillis();
            log.info("----Task Execute----");
            this.executeAbleTask(info);
            log.info("----Task End [{}ms]----", System.currentTimeMillis() - s);
        };
    }

    private void executeAbleTask(TaskInfo info) {
        Class clazz = null;
        Method method;
        Object bean;
        Object result = null;

        String className = info.getClassName();
        String beanName = info.getBeanName();
        String param = info.getParam();
        String executeMethod = info.getExecuteMethod();
        boolean withError = false;
        StringBuffer sb = new StringBuffer();
        try {
            boolean hasParam = StringUtils.isNotEmpty(param);
            if (StringUtils.isNotEmpty(className)) {
                clazz = Class.forName(className);
                bean = SpringUtils.getBean(clazz);
            } else {
                bean = SpringUtils.getBean(beanName);
            }
            method = hasParam ? clazz.getMethod(executeMethod, JSONObject.class) : clazz.getMethod(executeMethod);
            result = hasParam ? method.invoke(bean, param) : method.invoke(bean);
        } catch (Exception e) {
            sb.append(e.getMessage());
            sb.append("\n");
            withError = true;
        } finally {
            TaskExecuteParam executeParam = new TaskExecuteParam();
            executeParam.setErrorMsg(sb.toString());
            executeParam.setExecuteMsg(result != null ? JSONObject.toJSONString(result) : "");
            executeParam.setTaskId(info.getId());
            executeParam.setExecuteStatus(withError ? TaskStatus.ERROR.getCode() : TaskStatus.FINISH.getCode());
            taskExecuteRecordService.write(executeParam);
        }
    }
}
