package com.pactera.bigevent.common.entity.base;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应结果
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;//业务状态码  200-成功  500-失败

    private String message;//提示信息

    private T data;//响应数据

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //快速返回操作成功响应结果
    public static Result success(String message) {
        return new Result(200, message);
    }

    public static <E> Result<E> success(String msg, E data) {
        return new Result<>(200, msg, data);
    }

    public static Result error(String message) {
        return new Result(500, message);
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

}
