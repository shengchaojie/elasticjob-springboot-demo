package com.scj.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by shengchaojie on 2018/5/10.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class ElasticJobProperties {

    @Value("${elasticjob.serverLists}")
    private String serverLists;

    @Value("${elasticjob.namespace}")
    private String namespace;

    @Value("${elasticjob.db.url}")
    private String url;

    @Value("${elasticjob.db.username}")
    private String username;

    @Value("${elasticjob.db.password}")
    private String password;

}
