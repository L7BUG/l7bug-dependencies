
package com.l7bug.database.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.AllArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.time.LocalDateTime;
import java.util.Optional;

@AutoConfiguration
@AllArgsConstructor
@EnableConfigurationProperties(DatabaseType.class)
public class DataBaseAutoConfiguration {

	private final DatabaseType databaseType;
	private final CurrentUserId currentUserId;

	/**
	 * MyBatis-Plus MySQL 分页插件
	 */
	@Bean
	@ConditionalOnMissingBean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(databaseType.getDbType()));
		return interceptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public CurrentUserId currentUserId() {
		return new CurrentUserId() {
		};
	}


	/**
	 * MyBatis-Plus 源数据自动填充类
	 */
	@Bean
	@ConditionalOnMissingBean
	public MyMetaObjectHandler myMetaObjectHandler() {
		return new MyMetaObjectHandler(currentUserId);
	}

	public interface CurrentUserId {
		default Long getCurrentUserId() {
			return -1L;
		}
	}

	@AllArgsConstructor
	public static class MyMetaObjectHandler implements MetaObjectHandler {


		private final CurrentUserId currentUserId;

		@Override
		public void insertFill(MetaObject metaObject) {
			strictInsertFill(metaObject, "createBy", currentUserId::getCurrentUserId, Long.class);
			strictInsertFill(metaObject, "updateBy", currentUserId::getCurrentUserId, Long.class);
			strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
			strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
			strictInsertFill(metaObject, "delFlag", () -> false, Boolean.class);
		}

		@Override
		public void updateFill(MetaObject metaObject) {
			strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
			strictInsertFill(metaObject, "updateBy", currentUserId::getCurrentUserId, Long.class);
		}
	}

	@Bean
	public MyAuditorAware auditorProvider() {
		return new MyAuditorAware(currentUserId);
	}
}
