package com.l7bug.common.result;

import com.l7bug.common.error.BaseErrorCode;

import static com.l7bug.common.error.BaseErrorCode.SUCCESS;

/**
 * Results
 *
 * @author l
 * @since 2025/11/6 22:15
 */
public final class Results {

	public static <T> Result<T> success() {
		return success(null);
	}

	public static <T> Result<T> success(T data) {
		return buildResult("", SUCCESS.getCode(), SUCCESS.getMessage(), data);
	}

	public static <T> Result<T> failure(BaseErrorCode errorCode) {
		return failure(errorCode.getCode(), errorCode.getMessage());
	}

	public static <T> Result<T> failure(String code, String message) {
		return buildResult("", code, message, null);
	}

	public static <T> Result<T> buildResult(String requestId, String code, String message, T data) {
		return new Result<>(requestId, code, message, data);
	}
}
