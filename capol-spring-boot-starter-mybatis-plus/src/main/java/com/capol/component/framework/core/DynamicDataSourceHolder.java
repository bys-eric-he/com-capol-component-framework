package com.capol.component.framework.core;

import com.capol.component.framework.enums.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 主从数据源切换
 */
@Slf4j
public class DynamicDataSourceHolder {
    private static final ThreadLocal<DBTypeEnum> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void set(DBTypeEnum dbType) {
        log.info("-->当前数据库为：{}", dbType);
        CONTEXT_HOLDER.set(dbType);
    }

    public static DBTypeEnum get() {
        log.info("-->当前线程指定的数据库为：{}", CONTEXT_HOLDER.get());
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        log.info("-->清除当前数据源!!!");
        CONTEXT_HOLDER.remove();
    }

    public static void master() {
        log.info("-->切换数据源到【主库】!!!");
        set(DBTypeEnum.MASTER);
    }

    public static void slave() {
        log.info("-->切换数据源到【从库】!!!");
        set(DBTypeEnum.SLAVE);
    }
}

