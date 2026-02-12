package com.l7bug.database.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * DefaultCurrentUserId
 *
 * @author Administrator
 * @since 2026/1/23 12:15
 */
@Component
@ConditionalOnMissingBean(CurrentUserId.class)
public class DefaultCurrentUserId implements CurrentUserId {
	@Override
	public Long getCurrentUserId() {
		return -1L;
	}
}
