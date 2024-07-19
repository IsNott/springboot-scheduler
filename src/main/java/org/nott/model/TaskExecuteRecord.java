package org.nott.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Nott
 * @date 2024-7-17
 */

@Data
@TableName("table_task_execute_record")
public class TaskExecuteRecord {

    private String id;

    private String taskId;

    private Date executeTime;

    private Integer executeStatus;

    private String executeMsg;

    private String errorMsg;

    @Override
    public String toString() {
        return "TaskExecuteRecord{" +
                "id='" + id + '\'' +
                ", taskId='" + taskId + '\'' +
                ", executeTime=" + executeTime +
                ", executeStatus=" + executeStatus +
                ", executeMsg='" + executeMsg + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
