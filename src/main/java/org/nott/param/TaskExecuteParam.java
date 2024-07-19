package org.nott.param;

import lombok.Data;

/**
 * @author Nott
 * @date 2024-7-18
 */
@Data
public class TaskExecuteParam {

    private String taskId;
    private Integer executeStatus;
    private String executeMsg;
    private String errorMsg;
}
