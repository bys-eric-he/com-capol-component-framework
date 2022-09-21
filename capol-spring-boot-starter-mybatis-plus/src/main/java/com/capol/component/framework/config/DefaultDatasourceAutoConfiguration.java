package com.capol.component.framework.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.capol.component.framework.properties.RwDatasourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 默认数据源自动注入
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RwDatasourceProperties.class)
@AutoConfigureBefore(value = DataSourceAutoConfiguration.class, name = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure")
@Import(value = {DruidDynamicDataSourceConfiguration.class, DefaultMybatisPlusConfiguration.class})
@ConditionalOnProperty(prefix = RwDatasourceProperties.PREFIX, name = "enable", havingValue = "false", matchIfMissing = true)
public class DefaultDatasourceAutoConfiguration {

    @Primary
    @Bean(name = "defaultDataSource")
    @ConfigurationProperties(prefix = RwDatasourceProperties.PREFIX + RwDatasourceProperties.PREFIX_DEFAULT)
    public DataSource masterDataSource() {
        log.info("默认数据源自动注入, 数据源初始化 [ datasource ] default init ...");
        return DruidDataSourceBuilder.create().build();
    }
}