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
	private String requestId;
	private String code;
	private String message;
	private T data;

	public boolean isSuccess() {
		return SUCCESS.equals(code);
	}
}
