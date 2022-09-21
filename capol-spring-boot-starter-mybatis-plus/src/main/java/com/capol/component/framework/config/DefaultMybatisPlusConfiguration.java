package com.capol.component.framework.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.capol.component.framework.properties.RwDatasourceProperties;
import com.capol.component.framework.utils.DbTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@ConditionalOnProperty(prefix = RwDatasourceProperties.PREFIX, name = "enable", havingValue = "false", matchIfMissing = true)
@EnableConfigurationProperties(RwDatasourceProperties.class)
@Configuration
public class DefaultMybatisPlusConfiguration {

    @Autowired
    private RwDatasourceProperties rwDatasourceProperties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DbType dbType = DbTypeUtils.getDbType(rwDatasourceProperties);

        // 添加分页拦截
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        return interceptor;
    }
}