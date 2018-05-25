package org.security.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Ajax请求Json响应结果模型.
 * 
 * @author joey
 * @date 2018年1月7日 21:16:18
 */
public class Result implements Serializable {

	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 警告
	 */
	public static final int WARN = 2;
	/**
	 * 失败
	 */
	public static final int ERROR = 0;

	/**
	 * 成功消息
	 */
	public static final String SUCCESS_MSG = "操作成功！";
	/**
	 * 失败消息
	 */
	public static final String ERROR_MSG = "操作失败！";

	/**
	 * 结果状态码(可自定义结果状态码) 1:操作成功 0:操作失败
	 */
	private int code;
	/**
	 * 响应结果描述
	 */
	private String msg;
	/**
	 * 其它数据信息（比如跳转地址）
	 */
	private Object obj;

	public Result() {
		super();
	}

	/**
	 * 
	 * @param code
	 *            结果状态码(可自定义结果状态码或者使用内部静态变量 1:操作成功 0:操作失败 2:警告)
	 * @param msg
	 *            响应结果描述
	 * @param obj
	 *            其它数据信息（比如跳转地址）
	 */
	public Result(int code, String msg, Object obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}

	/**
	 * 默认操作成功结果.
	 */
	public static Result successResult() {
		return new Result(SUCCESS, SUCCESS_MSG, null);
	}

	/**
	 * 默认操作成功结果.结果集
	 */
	public static Result successResult(Object obj) {
		return new Result(SUCCESS, SUCCESS_MSG, obj);
	}

	/**
	 * 默认操作失败结果.
	 */
	public static Result errorResult() {
		return new Result(ERROR, ERROR_MSG, null);
	}

	/**
	 * 默认操作失败结果.返回错误原因
	 */
	public static Result errorResult(String msg) {
		return new Result(ERROR, msg, null);
	}

	/**
	 * 结果状态码(可自定义结果状态码) 1:操作成功 0:操作失败
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置结果状态码(约定 1:操作成功 0:操作失败 2:警告)
	 * 
	 * @param code
	 *            结果状态码
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 响应结果描述
	 */
	public String getMsg() {
		if (msg == null) {
			msg = "";
		}
		return msg;
	}

	/**
	 * 设置响应结果描述
	 * 
	 * @param msg
	 *            响应结果描述
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 其它数据信息（比如跳转地址）
	 */
	public Object getObj() {
		if (obj == null) {
			obj = "";
		}
		return obj;
	}

	/**
	 * 设置其它数据信息（比如跳转地址）
	 * 
	 * @param obj
	 *            其它数据信息（比如跳转地址）
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", code);
		map.put("msg", msg);
		map.put("obj", obj);
		try {
			return  mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return  null;
	}
}
