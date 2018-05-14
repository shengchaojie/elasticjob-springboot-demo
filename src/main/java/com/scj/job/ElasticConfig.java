package com.scj.job;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventListener;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by shengchaojie on 2018/5/10.
 */
@Configuration
public class ElasticConfig {

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Autowired
    private TestJob testJob;

    @Autowired
    private JobEventRdbConfiguration jobEventRdbConfiguration;

    @PostConstruct
    public void initJobs(){
        initSimpleJob("testjob","* * 4 * * ? ",3,testJob);
    }

    private void initSimpleJob(final String jobName, final String cron, final int sharingCount, ElasticJob elasticJob){
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(jobName,cron,sharingCount).build(),elasticJob.getClass().getCanonicalName());
        LiteJobConfiguration jobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
        new SpringJobScheduler(elasticJob,zookeeperRegistryCenter,jobConfiguration,jobEventRdbConfiguration).init();
    }

}
