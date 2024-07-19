package org.nott.enums;

/**
 * @author Nott
 * @date 2024-7-17
 */
public enum TaskMode {

    CRON(0),
    TIME(1),
    PERIOD(2),
    PERIOD_TIME(3) ;

    private Integer code;

    TaskMode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
