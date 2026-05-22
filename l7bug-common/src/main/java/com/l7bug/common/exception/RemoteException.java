package com.l7bug.common.exception;

import com.l7bug.common.error.RemoteErrorCode;

import java.io.Serial;


/**
 * 第三方服务调用异常
 *
 * @author l
 * @since 2025/11/6 22:02
 */
public class RemoteException extends AbstractException {

	@Serial
	private static final long serialVersionUID = 5861460393188118437L;

	public RemoteException(RemoteErrorCode code) {
		super(code.getCode(), code.getMessage());

	}

	public RemoteException(RemoteErrorCode code, Throwable e) {
		super(code.getCode(), code.getMessage(), e);
	}
}
