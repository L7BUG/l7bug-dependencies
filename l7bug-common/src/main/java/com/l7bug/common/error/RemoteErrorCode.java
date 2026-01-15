package com.l7bug.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 调用第三方服务出错
 *
 * @author l
 * @since 2025/11/6 21:57
 */
@Getter
@RequiredArgsConstructor
public enum RemoteErrorCode implements BaseErrorCode {
	REMOTE_ERROR("R000000", "调用第三方服务出错"),
	EMAIL_CLIENT_ERROR("R000001", "连接邮件服务失败"),
	FALLBACK("R100000", "远程服务降级"),
	;
	private final String code;
	private final String message;
}
