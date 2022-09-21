package com.capol.component.framework.core;


import com.capol.component.framework.enums.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 核心切换数据源方式
 */
@Slf4j
public class RoutingDataSourceContext extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DBTypeEnum dbTypeEnum = null;
        if (DynamicDataSourceHolder.get() == null) {
            dbTypeEnum = DBTypeEnum.MASTER;
        } else {
            dbTypeEnum = DynamicDataSourceHolder.get();
        }
        log.info("数据源切换!!! [ datasource ] change to :{}", dbTypeEnum);
        return dbTypeEnum;
    }
}
