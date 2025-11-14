package com.l7bug.common.page;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础分页对象
 *
 * @author Administrator
 * @since 2025/11/14 16:14
 */
@Getter
@Setter
public class PageQuery {
	/**
	 * 当前页
	 */
	private long current = 1;
	/**
	 * 页面大小
	 */
	private long size = 0;
	/**
	 * 需要进行排序的字段,默认 id
	 */
	private String column = "id";
	/**
	 * 是否正序排列，默认 true
	 */
	private boolean asc = true;
}
