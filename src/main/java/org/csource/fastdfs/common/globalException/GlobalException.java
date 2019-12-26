package org.csource.fastdfs.common.globalException;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by liujiakuan on 2019/11/2.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private CodeMsg exceptionMessage;

    public GlobalException(CodeMsg exceptionMessage) {
        super(exceptionMessage.toString());
        this.exceptionMessage = exceptionMessage;
    }
}
