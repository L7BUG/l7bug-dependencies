package com.l7bug.web.annotation;

/**
 * HasAuthorities
 *
 * @author Administrator
 * @since 2026/1/16 14:56
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasAuthorities {
	String value();
}
