package com.l7bug.database.config;

import org.hibernate.annotations.IdGeneratorType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 自定义雪花ID注解，替换已弃用的 @GenericGenerator
 */
@IdGeneratorType(MplusSnowflakeGenerator.class) // 指向生成器实现类
@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD })
public @interface SnowflakeId {
}
