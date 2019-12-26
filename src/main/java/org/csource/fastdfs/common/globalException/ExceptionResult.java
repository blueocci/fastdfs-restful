package org.csource.fastdfs.common.globalException;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by liujiakuan on 2019/11/2.
 */
@Data
public class ExceptionResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private String exceptionStack;

    public static ExceptionResult error(CodeMsg exceptionMessage, String message, String exceptionStack) {
        return new ExceptionResult(exceptionMessage, message, exceptionStack);
    }

    private ExceptionResult(CodeMsg exceptionMessage, String message, String exceptionStack) {
        if (exceptionMessage == null) {
            return;
        }
        this.code = exceptionMessage.getCode();
        this.message = exceptionMessage.getMessage() + "-->" + message;
        this.exceptionStack = exceptionStack;
    }
}
