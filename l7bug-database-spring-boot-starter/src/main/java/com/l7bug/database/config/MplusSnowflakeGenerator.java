package com.l7bug.database.config;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;

public class MplusSnowflakeGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        // 调用 MyBatis-Plus 内置的雪花算法
        return IdWorker.getId();
    }
}
