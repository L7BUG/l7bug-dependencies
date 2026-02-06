package com.l7bug.web.context;

import org.slf4j.MDC;

import java.util.Optional;

/**
 * TokenContext
 *
 * @author Administrator
 * @since 2025/11/10 15:13
 */
public final class MdcUserInfoContext {

	private final static String MDC_USERNAME = "username";
	private final static String MDC_TRACE_ID = "traceId";
	private final static String MDC_TOKEN = "token";
	private final static String AUTHORITIES = "authorities";
	private final static String MDC_USER_ID = "userId";

	private MdcUserInfoContext() {
	}

	public static void putMdcTraceId(String requestId) {
		MDC.put(MDC_TRACE_ID, requestId);
	}

	public static void putMdcUsername(String mdcUserName) {
		MDC.put(MDC_USERNAME, mdcUserName);
	}

	public static String getMdcUsername() {
		return Optional.ofNullable(MDC.get(MDC_USERNAME)).orElse("");
	}

	public static String getMdcTraceId() {
		return Optional.ofNullable(MDC.get(MDC_TRACE_ID)).orElse("");
	}

	public static void putMdcToken(String token) {
		MDC.put(MDC_TOKEN, token);
	}

	public static String getMdcToken() {
		return Optional.ofNullable(MDC.get(MDC_TOKEN)).orElse("");
	}

	public static void putMdcAuthorities(String authorities) {
		MDC.put(AUTHORITIES, authorities);
	}

	public static String getMdcAuthorities() {
		return Optional.ofNullable(MDC.get(AUTHORITIES)).orElse("");
	}

	public static void putMdcUserId(String userId) {
		MDC.put(MDC_USER_ID, userId);
	}

	public static String getMdcUserId() {
		return Optional.ofNullable(MDC.get(MDC_USER_ID)).orElse("");
	}
}
