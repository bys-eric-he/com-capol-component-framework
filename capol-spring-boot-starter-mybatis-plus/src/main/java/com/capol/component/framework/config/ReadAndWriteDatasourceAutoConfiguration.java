package com.capol.component.framework.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.capol.component.framework.aop.MasterDatabaseAspect;
import com.capol.component.framework.core.RoutingDataSourceContext;
import com.capol.component.framework.core.SqlReadWriteInterceptor;
import com.capol.component.framework.enums.DBTypeEnum;
import com.capol.component.framework.properties.RwDatasourceProperties;
import com.capol.component.framework.utils.DbTypeUtils;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * ???????????????????????????
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RwDatasourceProperties.class)
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class, PageHelperAutoConfiguration.class}, name = {"com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure"})
@Import(value = {DruidDynamicDataSourceConfiguration.class})
@ConditionalOnProperty(prefix = RwDatasourceProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = false)
public class ReadAndWriteDatasourceAutoConfiguration {

    @Autowired
    private RwDatasourceProperties rwDatasourceProperty;

    @Autowired
    private Environment env;

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = RwDatasourceProperties.PREFIX + RwDatasourceProperties.PREFIX_MASTER)
    public DataSource masterDataSource() {
        log.info("???(MASTER)????????????????????? [ datasource ] master init ...");
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = RwDatasourceProperties.PREFIX + RwDatasourceProperties.PREFIX_SLAVE)
    public DataSource slaveDataSource() {
        log.info("???(SLAVE)????????????????????? [ datasource ] slave init ...");
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE, slaveDataSource);

        RoutingDataSourceContext context = new RoutingDataSourceContext();
        // ??????????????????
        context.setDefaultTargetDataSource(masterDataSource);
        context.setTargetDataSources(targetDataSources);
        return context;
    }

    @Bean
    public MasterDatabaseAspect myDataSourceAspect() {
        return new MasterDatabaseAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory customSqlSessionFactory(@Qualifier("masterDataSource") DataSource master,
                                                     @Qualifier("slaveDataSource") DataSource slave) throws Exception {

        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource(master, slave));
        factoryBean.setTypeAliasesPackage(env.getProperty("mybatis-plus.type-aliases-package"));
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis-plus.mapper-locations")));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        String property = env.getProperty("mybatis-plus.configuration.log-impl");
        if (property != null && !"".equals(property)) {
            configuration.setLogImpl((Class<? extends Log>) Class.forName(property));
        }
        factoryBean.setConfiguration(configuration);
        MybatisPlusInterceptor paginationInterceptor = new MybatisPlusInterceptor();
        // ?????????????????????
        DbType dbType = DbTypeUtils.getDbType(rwDatasourceProperty);
        // ??????????????????
        paginationInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        // ???????????????
        factoryBean.setPlugins(new Interceptor[]{
                paginationInterceptor,
                new SqlReadWriteInterceptor()
        });

        return factoryBean.getObject();
    }
}