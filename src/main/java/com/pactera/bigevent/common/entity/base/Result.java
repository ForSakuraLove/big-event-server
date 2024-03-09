package com.pactera.bigevent.common.entity.base;


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

    public Result(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    //快速返回操作成功响应结果(带响应数据)
    public static <E> Result<E> success(E data) {
        return new Result<>(200, "操作成功", data);
    }

    //快速返回操作成功响应结果
    public static Result success() {
        return new Result(200, "操作成功");
    }

    public static Result error(String message) {
        return new Result(500, message);
    }
}
