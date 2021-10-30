package com.codeshu.vueblog.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ShuCode
 * @date 2021/10/15 14:04
 * @Email 13828965090@163.com
 */
@Data
public class Result implements Serializable {
	private int code;  //用来表示本次请求是否成功，200成功，非200失败
	private String message;  //结果消息，无论接口因为什么原因调用失败，如果后端希望告知前端，都可以将必要信息存入这个字段
	private Object data;  //结果数据，用来存储实际的返回数据

	//响应正常数据时调用
	public static Result success(Object data){
		return success(200,"操作成功",data);
	}

	public static Result success(int code,String message,Object data){
		Result result = new Result();
		result.setCode(code);
		result.setMessage(message);
		result.setData(data);
		return result;
	}

	//响应异常数据时调用
	public static Result fail(String message,Object data){
		return fail(400,message,data);
	}
	public static Result fail(String message){
		return fail(400,message,null);
	}
	public static Result fail(int code,String message,Object data){
		Result result = new Result();
		result.setCode(code);
		result.setMessage(message);
		result.setData(data);
		return result;
	}


}
