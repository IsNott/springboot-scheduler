package org.nott.enums;

/**
 * @author Nott
 * @date 2024-7-17
 */
public enum TaskStatus {

    CANCEL(-1),
    INIT(0),
    SCHEDULED(1),
    FINISH(2),
    ERROR(3)
    ;

    private Integer code;

    TaskStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
