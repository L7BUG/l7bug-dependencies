package com.l7bug.database.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * BaseDo
 *
 * @author Administrator
 * @since 2025/8/20 14:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class BaseDo extends BaseNotDeleDo {
	@Serial
	private static final long serialVersionUID = -7865608655588766372L;

	/**
	 * 删除标识 0：未删除 1：已删除
	 */
	@TableField(fill = FieldFill.INSERT)
	@TableLogic(value = "false",delval = "true")
	private Boolean delFlag = false;
}
