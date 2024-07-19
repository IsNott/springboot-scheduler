package org.nott.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

/**
 * @author Nott
 * @date 2024-7-18
 */
@Slf4j
public class MyTaskErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        String message = t.getMessage();
        Thread thread = Thread.currentThread();
        log.error("{} - Task execute error {}", thread.getName(), message);
    }
}
