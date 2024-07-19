package org.nott.controller;


import jakarta.annotation.Resource;
import org.nott.model.TaskInfo;
import org.nott.param.TaskParam;
import org.nott.service.TaskService;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Nott
 * @date 2024-7-16
 */

@RestController
@RequestMapping("/task/")
public class TaskController {

    @Resource
    private TaskService taskService;

    @Description("执行任务-主线程")
    @PostMapping("runSync")
    public void runTask(@PathVariable String id){
        taskService.run(id);
    }

    @Description("执行任务-调度器")
    @PostMapping("runAsync")
    public void runTaskAsync(@PathVariable String id){
        taskService.run(id,true);
    }

    @Description("任务列表")
    @GetMapping("list")
    public Collection<TaskInfo> taskList(){
        return taskService.getTaskList();
    }

    @Description("取消任务调度")
    @RequestMapping("cancel/{id}")
    public boolean cancelTask(@PathVariable String id){
        return taskService.cancelTask(id);
    }

    @Description("新增任务记录")
    @PostMapping("add")
    public void addTask(@RequestBody TaskParam param){
        taskService.addTask(param);
    }

}
