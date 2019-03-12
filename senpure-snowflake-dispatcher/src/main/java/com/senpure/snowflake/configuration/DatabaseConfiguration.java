package com.senpure.snowflake.configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.senpure.base.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * DatabaseConfigurtion
 *
 * @author senpure
 * @time 2019-01-04 10:58:29
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class DatabaseConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        DataSourceProperties prop = dataSourceProperties();
        DatabaseUtil.checkAndCreateDatabase(prop);
        return DruidDataSourceBuilder.create().build();
    }


}
