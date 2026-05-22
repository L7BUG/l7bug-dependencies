package com.l7bug.common.exception;

import com.l7bug.common.error.BaseErrorCode;
import com.l7bug.common.error.ServerErrorCode;

import java.io.Serial;


/**
 * 服务器异常，程序内部产生
 *
 * @author l
 * @since 2025/11/6 22:02
 */
public class ServerException extends AbstractException {

	@Serial
	private static final long serialVersionUID = -2792205451717430321L;

	public ServerException(ServerErrorCode code) {
		super(code.getCode(), code.getMessage());

	}

	public ServerException(BaseErrorCode code, Throwable e) {
		super(code.getCode(), code.getMessage(), e);
	}

	public ServerException(ServerErrorCode code, String message) {
		super(code.getCode(), message);
	}
}
