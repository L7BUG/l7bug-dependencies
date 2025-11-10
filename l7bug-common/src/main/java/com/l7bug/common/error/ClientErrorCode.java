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
	CLIENT_ERROR("C000000", "客户端错误"),
	NOT_AUTHENTICATION("C000001", "未登录!请登录后访问!"),
	ACCESS_DENIED("C000003", "未授权禁止访问!请联系管理员为你授权!"),
	LOGIN_ERROR("C000004", "登录失败!账号或密码错误!"),
	USER_IS_DISABLE("C000005", "登录失败!用户已被禁用!请联系管理员解除封禁!"),
	;
	private final String code;
	private final String message;
}
