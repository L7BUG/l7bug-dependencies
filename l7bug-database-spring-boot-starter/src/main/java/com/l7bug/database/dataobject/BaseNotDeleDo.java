package com.l7bug.database.dataobject;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.l7bug.database.config.SnowflakeId;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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
@MappedSuperclass
public abstract class BaseNotDeleDo implements Serializable {
	@Serial
	private static final long serialVersionUID = -7865608655588766372L;

	@Id
	@TableId
	@SnowflakeId
	private Long id;

	@CreatedBy
	@Column(updatable = false)
	@TableField(fill = FieldFill.INSERT)
	private Long createBy;

	@LastModifiedBy
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateBy;

	@CreatedDate
	@Column(updatable = false)
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	@LastModifiedDate
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
}
