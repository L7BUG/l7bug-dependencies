package com.l7bug.database.config;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DatabaseType
 *
 * @author Administrator
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "l7bug.database")
public class DatabaseType {
	private DbType dbType = DbType.POSTGRE_SQL;
}
