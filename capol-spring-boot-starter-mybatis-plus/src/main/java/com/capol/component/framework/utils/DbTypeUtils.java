package com.capol.component.framework.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.capol.component.framework.properties.RwDatasourceProperties;

public class DbTypeUtils {
    public static DbType getDbType(RwDatasourceProperties rwDatasourceProperty) {
        DbType dbType = DbType.MARIADB;
        if (rwDatasourceProperty != null && rwDatasourceProperty.getDbType() != null && !"".equals(rwDatasourceProperty.getDbType())) {
            dbType = DbType.getDbType(rwDatasourceProperty.getDbType());
        }
        return dbType;
    }
}
