package com.l7bug.database.config;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * MyAuditorAware
 *
 * @author Administrator
 * @since 2025/12/19 15:24
 */
@AllArgsConstructor
public class MyAuditorAware implements AuditorAware<Long> {
	private final DataBaseAutoConfiguration.CurrentUserId currentUserId;
	@NullMarked
	@Override
	public Optional<Long> getCurrentAuditor() {
		return Optional.of(currentUserId.getCurrentUserId());
	}
}
