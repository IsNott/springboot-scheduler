package org.nott.param;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Nott
 * @date 2024-7-17
 */
@Data
public class TaskParam {

    private String className;

    private String beanName;

    private String executeMethod;

    private Integer executeMode;

    private String cron;

    private String param;

    private Long period;

    private Timestamp executeTime;

    private boolean scheduleNow;
}
