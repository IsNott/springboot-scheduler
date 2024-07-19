package org.nott.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nott.mapper.TaskExecuteRecordMapper;
import org.nott.model.TaskExecuteRecord;
import org.nott.param.TaskExecuteParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author Nott
 * @date 2024-7-18
 */

@Service
public class TaskExecuteRecordService extends ServiceImpl<TaskExecuteRecordMapper, TaskExecuteRecord> {

    public void write(TaskExecuteParam param){
        TaskExecuteRecord record = new TaskExecuteRecord();
        BeanUtils.copyProperties(param,record);
        record.setExecuteTime(new Date());
        record.setId(UUID.randomUUID().toString());
        this.save(record);
    }
}
