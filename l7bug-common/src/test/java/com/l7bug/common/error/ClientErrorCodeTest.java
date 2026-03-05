package com.l7bug.common.error;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ClientErrorCodeTest {

	@BeforeEach
	void setUp() {

	}

	@Test
	void codeValid() {
		Map<String, List<ClientErrorCode>> collect = Arrays.stream(ClientErrorCode.values()).collect(Collectors.groupingBy(ClientErrorCode::getCode));
		for (Map.Entry<String, List<ClientErrorCode>> stringListEntry : collect.entrySet()) {
			Assertions.assertThat(stringListEntry.getValue())
				.as("状态码唯一")
				.hasSize(1);
		}
	}
}
