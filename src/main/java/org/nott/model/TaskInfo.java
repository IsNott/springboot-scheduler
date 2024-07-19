package org.nott.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Nott
 * @date 2024-7-17
 */
@Data
@TableName("table_task_info")
public class TaskInfo {

    private String id;

    private String className;

    private String beanName;

    private String executeMethod;

    private Integer executeMode;

    private String cron;

    private String param;

    private Long period;

    private String periodUnit;

    private Timestamp executeTime;

    private Date createTime;

    private Date updateTime;

    @Override
    public String toString() {
        return "TaskInfo{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ", beanName='" + beanName + '\'' +
                ", executeMethod='" + executeMethod + '\'' +
                ", executeMode=" + executeMode +
                ", cron='" + cron + '\'' +
                ", param='" + param + '\'' +
                ", period=" + period +
                ", periodUnit='" + periodUnit + '\'' +
                ", executeTime=" + executeTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
