package org.nott.global;

import org.nott.model.TaskInfo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Nott
 * @date 2024-7-17
 */

public class TaskInfoHolder {

    // 任务MAP
    public static ConcurrentHashMap<String, TaskInfo> TASK_INFO_MAP = new ConcurrentHashMap<>(16);

    // 已调度的任务MAP
    public static ConcurrentHashMap<String, ScheduledFuture<?>> SCHEDULE_FUTRUE_MAP = new ConcurrentHashMap<>(16);

}
