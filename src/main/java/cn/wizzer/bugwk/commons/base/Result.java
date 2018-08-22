package cn.wizzer.bugwk.commons.base;

import org.nutz.lang.Strings;

import java.io.Serializable;

/**
 * 统一的后台输出格式
 * Created by wizzer on 2018.08
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private Object data;

    public Result() {

    }

    public static Result NEW() {
        return new Result();
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = Strings.sNull(msg);
        this.data = data;
    }

    public static Result success(String content) {
        return new Result(0, content, null);
    }

    public static Result success(Object data) {
        return new Result(0, "操作成功", data);
    }

    public static Result success(String content, Object data) {
        return new Result(0, content, data);
    }

    public static Result error(int code, String content) {
        return new Result(code, content, null);
    }

    public static Result error(String content) {
        return new Result(1, content, null);
    }

    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    public static Result error() {
        return new Result(1, "操作失败", null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
