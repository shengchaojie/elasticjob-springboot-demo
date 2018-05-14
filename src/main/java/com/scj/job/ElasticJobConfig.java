package com.scj.job;

import com.dangdang.ddframe.job.event.JobEventListener;
import com.dangdang.ddframe.job.event.JobEventListenerConfigurationException;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by shengchaojie on 2018/5/10.
 */
@Configuration
public class ElasticJobConfig {

    @Autowired
    private ElasticJobProperties elasticJobProperties;

    @Bean
    public ZookeeperConfiguration zkConfig(){
        return new ZookeeperConfiguration(elasticJobProperties.getServerLists(),elasticJobProperties.getNamespace());
    }

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(ZookeeperConfiguration zookeeperConfiguration){
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }

    @Bean
    public DataSource esDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        dataSource.setUrl(elasticJobProperties.getUrl());
        dataSource.setUsername(elasticJobProperties.getUsername());
        dataSource.setPassword(elasticJobProperties.getPassword());
        return dataSource;
    }

    @Bean
    public JobEventRdbConfiguration jobEventRdbConfiguration(DataSource esDataSource) throws JobEventListenerConfigurationException {
        return new JobEventRdbConfiguration(esDataSource);
    }


}
