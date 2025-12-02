package com.l7bug.web.handler;


import com.l7bug.common.error.ServerErrorCode;
import com.l7bug.common.exception.AbstractException;
import com.l7bug.common.result.Result;
import com.l7bug.common.result.Results;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler
 *
 * @author Administrator
 * @since 2025/12/2 09:57
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * 拦截应用内抛出的异常
	 */
	@ExceptionHandler(value = {AbstractException.class})
	public Result<Void> abstractException(HttpServletRequest request, AbstractException ex) {
		if (ex.getCause() != null) {
			log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex, ex.getCause());
			return Results.failure(ex.getCode(), ex.getMessage());
		}
		StringBuilder stackTraceBuilder = new StringBuilder();
		stackTraceBuilder.append(ex.getClass().getName()).append(": ").append(ex.getCode()).append("\n");
		StackTraceElement[] stackTrace = ex.getStackTrace();
		for (int i = 0; i < Math.min(5, stackTrace.length); i++) {
			stackTraceBuilder.append("\tat ").append(stackTrace[i]).append("\n");
		}
		log.error("[{}] {} [ex] {} \n\n{}", request.getMethod(), request.getRequestURL().toString(), ex, stackTraceBuilder);
		return Results.failure(ex.getCode(), ex.getMessage());
	}

	/**
	 * 拦截未捕获异常
	 */
	@ExceptionHandler(value = Throwable.class)
	public Result<String> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
		log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
		return Results.failure(ServerErrorCode.SERVER_ERROR.getCode(), throwable.getMessage());
	}

	private String getUrl(HttpServletRequest request) {
		if (!StringUtils.hasText(request.getQueryString())) {
			return request.getRequestURL().toString();
		}
		return request.getRequestURL().toString() + "?" + request.getQueryString();
	}
}
