package com.l7bug.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result
 *
 * @author l
 * @since 2025/11/6 22:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
	public final static String SUCCESS = "0";
	/**
	 * 请求ID,用于链路追踪,查看日志时使用
	 */
	private String requestId;
	/**
	 * 状态码,0成功
	 */
	private String code;
	/**
	 * 响应信息
	 */
	private String message;
	/**
	 * 响应数据
	 */
	private T data;

	public boolean isSuccess() {
		return SUCCESS.equals(code);
	}

	public boolean isFailure() {
		return !isSuccess();
	}
}
