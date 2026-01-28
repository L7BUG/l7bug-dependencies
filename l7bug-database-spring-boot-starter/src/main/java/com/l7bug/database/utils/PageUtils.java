package com.l7bug.database.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.l7bug.common.page.PageQuery;

/**
 * PageUtils
 *
 * @author Administrator
 * @since 2026/1/28 18:17
 */
public class PageUtils {
	public static <T> Page<T> buildMybatisPlusPage(PageQuery pageQuery) {
		Page<T> page = new Page<>();
		page.setCurrent(pageQuery.getCurrent());
		page.setSize(pageQuery.getSize());
		for (PageQuery.OrderItem order : pageQuery.getOrders()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setColumn(order.getColumn());
			orderItem.setAsc(order.isAsc());
			page.addOrder(orderItem);
		}
		return page;
	}
}
