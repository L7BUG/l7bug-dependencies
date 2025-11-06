package com.l7bug.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 客户端错误
 *
 * @author l
 * @since 2025/11/6 21:55
 */
@Getter
@RequiredArgsConstructor
public enum ClientErrorCode implements BaseErrorCode {
	CLIENT_ERROR("C000000", "客户端错误");
	private final String code;
	private final String message;
}
