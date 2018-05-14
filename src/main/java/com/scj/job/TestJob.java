package com.scj.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by shengchaojie on 2018/5/10.
 */
@Component
public class TestJob implements SimpleJob{
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("start: "+new Date());
        switch (shardingContext.getShardingItem()) {
            case 0:
                System.out.println("sharding 0");
                break;
            case 1:
                System.out.println("sharding 1");
                break;
            case 2:
                System.out.println("sharding 2");
                break;
            default:
                System.out.println("sharding not match");
        }
    }
}
