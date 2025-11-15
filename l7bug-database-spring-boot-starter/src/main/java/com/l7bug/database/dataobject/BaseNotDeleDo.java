package com.l7bug.database.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseDo
 *
 * @author Administrator
 * @since 2025/8/20 14:06
 */
@Data
public class BaseNotDeleDo implements Serializable {
	@Serial
	private static final long serialVersionUID = -7865608655588766372L;

	@TableId
	private Long id;

	@TableField(fill = FieldFill.INSERT)
	private Long createBy;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateBy;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
}
