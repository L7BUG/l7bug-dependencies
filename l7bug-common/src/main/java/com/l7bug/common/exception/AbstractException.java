package com.l7bug.common.exception;

import com.l7bug.common.error.BaseErrorCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;


/**
 * 公共抽象异常类
 *
 * @author l
 * @since 2025/11/6 22:02
 */
@ToString
@Getter
public abstract class AbstractException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -7704782583563750938L;
	private final String code;

	private final String message;

	public AbstractException(BaseErrorCode code) {
		this(code.getCode(), code.getMessage());
	}

	public AbstractException(BaseErrorCode code, Throwable e) {
		this(code.getCode(), code.getMessage(), e);
	}

	public AbstractException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public AbstractException(String code, String message, Throwable e) {
		super(message, e);
		this.code = code;
		this.message = message;
	}
}
