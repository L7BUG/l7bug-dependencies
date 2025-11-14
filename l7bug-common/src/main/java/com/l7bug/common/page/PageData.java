package com.l7bug.common.page;

import java.util.Collection;

/**
 * PageData
 *
 * @author Administrator
 * @since 2025/11/14 16:24
 */
public record PageData<T>(long total, Collection<T> data) {
}
