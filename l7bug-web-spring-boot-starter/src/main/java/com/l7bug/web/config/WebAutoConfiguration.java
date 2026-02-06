package com.l7bug.web.config;


import com.l7bug.common.error.ClientErrorCode;
import com.l7bug.common.etc.Headers;
import com.l7bug.common.exception.ClientException;
import com.l7bug.common.result.Result;
import com.l7bug.web.annotation.HasAuthorities;
import com.l7bug.web.context.MdcUserInfoContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

/**
 * WebAutoConfiguration
 *
 * @author Administrator
 * @since 2025/11/4 17:52
 */
public class WebAutoConfiguration implements WebMvcConfigurer {
	private static final Logger log = LoggerFactory.getLogger(WebAutoConfiguration.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
				@Override
				public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
					String requestId = request.getHeader(Headers.REQUEST_ID);
					String token = request.getHeader(Headers.TOKEN);
					String username = request.getHeader(Headers.USERNAME);
					String userId = request.getHeader(Headers.USER_ID);
					String authorities = request.getHeader(Headers.AUTHORITIES);
					if (!StringUtils.hasText(requestId)) {
						requestId = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT);
					}
					if (StringUtils.hasText(username)) {
						MdcUserInfoContext.putMdcUsername(username);
					}
					if (StringUtils.hasText(authorities)) {
						MdcUserInfoContext.putMdcAuthorities(authorities);
					}
					MdcUserInfoContext.putMdcTraceId(requestId);
					if (StringUtils.hasText(token)) {
						MdcUserInfoContext.putMdcToken(token);
					}
					if (StringUtils.hasText(userId)) {
						MdcUserInfoContext.putMdcUserId(token);
					}
					log.info("[{}]开始请求:{}", request.getMethod(), request.getRequestURI());
					long currentTimeMillis = System.currentTimeMillis();
					MDC.put("currentTimeMillis", String.valueOf(currentTimeMillis));
					return HandlerInterceptor.super.preHandle(request, response, handler);
				}

				@Override
				public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
					String s = MDC.get("currentTimeMillis");
					log.info("请求耗时:{}/ms", System.currentTimeMillis() - Long.parseLong(s));
					MDC.clear();
					HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
				}
			})
			.order(Integer.MIN_VALUE)
			.addPathPatterns("/**");
	}

	@Bean
	public SetRequestAspect setRequestAspect() {
		return new SetRequestAspect();
	}

	@Bean
	public AuthoritiesAspect authoritiesAspect() {
		return new AuthoritiesAspect();
	}

	/**
	 * WebAutoConfiguration
	 *
	 * @author Administrator
	 * @since 2025/11/4 17:52
	 */
	@Aspect
	public static class SetRequestAspect {

		@AfterReturning(value = "execution(public com.l7bug.common.result.Result *(..))", returning = "result")
		public void afterReturning(Result<?> result) {
			result.setRequestId(MdcUserInfoContext.getMdcTraceId());
		}
	}

	@Aspect
	public static class AuthoritiesAspect {
		@Before("@annotation(hasAuthorities)")
		public void before(HasAuthorities hasAuthorities) {
			if (!Arrays.asList(MdcUserInfoContext.getMdcAuthorities().split(",")).contains(hasAuthorities.value())) {
				throw new ClientException(ClientErrorCode.ACCESS_DENIED);
			}
		}
	}
}
