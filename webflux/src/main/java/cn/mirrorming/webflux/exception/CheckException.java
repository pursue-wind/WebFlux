package cn.mirrorming.webflux.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: mirrorming
 * @create: 2019-06-30 21:40
 **/
@Data
@AllArgsConstructor
public class CheckException extends RuntimeException {

    private String fieldName;
    private String fieldValue;

    public CheckException() {
        super();
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }

    protected CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}