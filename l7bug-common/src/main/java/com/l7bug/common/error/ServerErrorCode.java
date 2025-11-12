package com.l7bug.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统执行出错
 *
 * @author l
 * @since 2025/11/6 21:55
 */
@Getter
@RequiredArgsConstructor
public enum ServerErrorCode implements BaseErrorCode {
	SERVER_ERROR("S000000", "系统执行出错"),
	TIME_OUT("S000001", "当前操作频繁,请稍后再试"),
	;
	private final String code;
	private final String message;
}
