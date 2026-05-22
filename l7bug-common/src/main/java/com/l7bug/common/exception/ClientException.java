package com.l7bug.common.exception;

import com.l7bug.common.error.ClientErrorCode;

import java.io.Serial;


/**
 * 客户端异常
 *
 * @author l
 * @since 2025/11/6 22:02
 */
public class ClientException extends AbstractException {

	@Serial
	private static final long serialVersionUID = 3880071539113901982L;

	public ClientException(ClientErrorCode code) {
		super(code.getCode(), code.getMessage());

	}

	public ClientException(ClientErrorCode code, String message) {
		super(code.getCode(), message);

	}

	public ClientException(ClientErrorCode code, Throwable e) {
		super(code.getCode(), code.getMessage(), e);
	}
}
