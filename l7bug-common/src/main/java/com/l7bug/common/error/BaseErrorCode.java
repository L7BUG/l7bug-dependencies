package com.l7bug.common.error;

/**
 * BaseErrorCode
 *
 * @author l
 * @since 2025/11/6 21:51
 */
public interface BaseErrorCode {
	BaseErrorCode SUCCESS = new BaseErrorCode() {
		@Override
		public String getCode() {
			return "0";
		}

		@Override
		public String getMessage() {
			return "success";
		}
	};

	/**
	 * 错误码
	 *
	 * @return 错误码
	 */
	String getCode();

	/**
	 * 错误内容
	 *
	 * @return 错误内容
	 */
	String getMessage();
}
