package com.l7bug.web.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * JacksonConfig
 *
 * @author Administrator
 * @since 2025/8/25 11:28
 */
public class JacksonConfig {


	// 定义统一的日期时间格式
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_PATTERN = "yyyy-MM-dd";

	// 创建格式化器
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	@Bean
	@Primary
	public JsonMapper objectMapper(JsonMapper.Builder builder) {

		// 1. 创建一个 SimpleModule 来替代旧的 JavaTimeModule
		SimpleModule javaTimeModule = new SimpleModule();

		// 配置 LocalDateTime 的序列化与反序列化
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));

		// 配置 LocalDate 的序列化与反序列化
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));

		// 2. 配置 Long 类型转为 String（解决前端精度丢失问题）
		javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
		javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		builder.changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.NON_NULL));
		builder
			// 注册配置好的时间模块
			.addModule(javaTimeModule)
			// 设置序列化时忽略 null 值字段
			// .serializationInclusion(JsonInclude.Include.NON_NULL)
			// 设置时区为北京时间 (东八区)
			.defaultTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
			// 禁用将日期序列化为时间戳
			// .disable(DeserializationFeature.)
			// 忽略未知属性（反序列化时）
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return builder.build();
	}
}
