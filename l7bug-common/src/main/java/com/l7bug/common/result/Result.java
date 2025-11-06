package com.l7bug.common.result;

/**
 * Result
 *
 * @author l
 * @since 2025/11/6 22:11
 */
public record Result<T>(String requestId, String code, String message, T data) {
}
