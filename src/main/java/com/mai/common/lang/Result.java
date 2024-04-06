package com.mai.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于异步统一返回的结果封装。包含：
 * <p>
 * - 是否成功，可用code表示（如200表示成功，400表示异常）
 * - 结果消息
 * - 结果数据
 */
@Data
public class Result implements Serializable {
    private int code; // 200表示成功
    private String msg;
    private Object data;

    public static Result succ(Object data) {
        return succ(200, "操作成功", data);
    }

    public static Result succ(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}