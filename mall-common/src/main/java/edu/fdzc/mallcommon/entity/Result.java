package edu.fdzc.mallcommon.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T>{
    private int code; //动态时，200成功，500失败
    private String msg;//响应信息
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //成功响应，带数据
    public static <T> Result<T> success(T data){
        return new Result<T>(200, "操作成功", data);
    }

    //成功响应，不带数据
    public static <T> Result<T> success(){
        return new Result<T>(200, "操作成功", null);
    }

    //失败响应
    public static <T> Result<T> error(){
        return new Result<T>(500, "操作失败", null);
    }

    //失败响应
    public static <T> Result<T> error(String msg){
        return new Result<T>(500, msg, null);
    }
}
