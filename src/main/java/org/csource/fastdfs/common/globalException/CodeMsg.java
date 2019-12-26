package org.csource.fastdfs.common.globalException;

/**
 * Created by liujiakuan on 2019/11/2.
 */
public enum CodeMsg {

    SERVER_EXCEPTION(500, "服务器异常"),
    BIND_EXCEPTION(500101, "参数校验异常"),
    NULL_POINTER_EXCEPTION(500102, "空指针异常"),
    CALCULATION_EXCEPTION(500103, "计算异常"),
    DATABASE_ERROR(500104, "数据库操作错误"),
    DATABASE_UPDATE_DISPLAY_ERROR(500105, "更新Dispaly失败，数据回滚");

    private int code;
    private String message;

    CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
