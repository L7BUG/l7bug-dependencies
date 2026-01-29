package com.l7bug.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 客户端错误
 *
 * @author l
 * @since 2025/11/6 21:55
 */
@Getter
@RequiredArgsConstructor
public enum ClientErrorCode implements BaseErrorCode {
	CLIENT_ERROR("C000000", "客户端错误"),
	/*认证相关问题 0开头*/
	NOT_AUTHENTICATION("C000001", "未登录!请登录后访问!"),
	ACCESS_DENIED("C000003", "未授权禁止访问!请联系管理员为你授权!"),
	LOGIN_ERROR("C000004", "登录失败!账号或密码错误!"),
	USER_IS_DISABLE("C000005", "登录失败!用户已被禁用!请联系管理员解除封禁!"),
	USER_NOT_NULL("C000006", "注册失败,该用户已被注册"),
	USER_PASSWORD_IS_ERROR("C000007", "用户密码错误!"),
	/*
	通用问题,1开头
	 */
	START_TIME_AFTER_NOW("C100001", "开始时间在当前时间之后!"),
	END_TIME_BEFORE_NOW("C100002", "结束时间在当前时间之前!"),
	START_AFTER_END("C100003", "开始时间在结束时间之后!"),
	VALIDATION_FAILED("C100004", "参数校验不通过!"),
	NO_DUPLICATE_SUBMIT("C100005", "重复提交!"),
	DATA_IS_NULL("C100005", "操作的数据不存在!"),
	/*system非法操作 2开头*/
	CHILDREN_IS_NOT_NULL("C200001", "子节点不为空!"),
	FATHER_IS_NOT_FOLDER("C200002", "父节点的类型不为文件夹!"),
	FATHER_IS_NOT_PAGE("C200003", "父节点的类型不为页面"),
	NODE_IS_NOT_NULL("C200004", "节点已存在"),

	/*coupon非法操作 3开头*/
	COUPON_ERROR("C300000", "优惠卷模块调用错误"),
	ALL_PLATFORM_PRODUCT_DISABLE_OPTION("C300001", "全店铺通用禁止设置固定商品"),
	COUPON_PRODUCT_SPECIFIC_UNCONFIGURED_ITEM("C300002", "全店铺通用禁止设置固定商品"),
	COUPON_TEMPLATE_IS_END("C300003", "优惠卷已结束!"),
	COUPON_TEMPLATE_PUSH_TASK_CREATE_FAILED("C300004", "优惠卷推送任务创建失败!"),
	;
	private final String code;
	private final String message;
}
