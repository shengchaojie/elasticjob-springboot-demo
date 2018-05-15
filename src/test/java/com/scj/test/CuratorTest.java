package com.scj.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * LeaderLatch测试
 * Created by shengchaojie on 2018/5/15.
 */
public class CuratorTest {

    @Test
    public void deleteNode() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",new ExponentialBackoffRetry(1000,3));
        client.start();
        client.delete().forPath("/test");
        if(client.checkExists().forPath("/test") == null){
            client.create().forPath("/test");
        }
    }

    @Test
    public void leaderLatchTest() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",new ExponentialBackoffRetry(1000,3));
        client.start();
        client.blockUntilConnected();
        LeaderLatchListener leaderLatchListener = new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println("i am leader");
            }

            @Override
            public void notLeader() {
                System.out.println("i am not leader");
            }
        };
        try(LeaderLatch leaderLatch = new LeaderLatch(client, "/test")){
            leaderLatch.addListener(leaderLatchListener);
            leaderLatch.start();
            leaderLatch.await();
            System.out.println("get leader");
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Test
    public void leaderLatchMultiThreadTest() throws InterruptedException, IOException {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",new ExponentialBackoffRetry(1000,3));
        client.start();
        client.blockUntilConnected();
        LeaderLatchListener leaderLatchListener = new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println("i am leader");
            }

            @Override
            public void notLeader() {
                System.out.println("i am not leader");
            }
        };
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for(int i = 0 ;i<4;i++){
            final int count = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        LeaderLatch leaderLatch = new LeaderLatch(client, "/test");
                        leaderLatch.addListener(leaderLatchListener);
                        countDownLatch.await();
                        leaderLatch.start();
                        leaderLatch.await();
                        client.setData().forPath("/test",("test"+count).getBytes());
                        System.out.println("get leader");
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }


                }
            }).start();
        }

        countDownLatch.countDown();
        System.in.read();

    }

    @Test
    public void leaderLatchMultiThreadTest2() throws InterruptedException, IOException {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",new ExponentialBackoffRetry(1000,3));
        client.start();
        client.blockUntilConnected();
        LeaderLatchListener leaderLatchListener = new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println("i am leader");
            }

            @Override
            public void notLeader() {
                System.out.println("i am not leader");
            }
        };
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for(int i = 0 ;i<4;i++){
            final int count = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try(LeaderLatch leaderLatch = new LeaderLatch(client, "/test")){
                        leaderLatch.addListener(leaderLatchListener);
                        countDownLatch.await();
                        leaderLatch.start();
                        leaderLatch.await();
                        client.setData().forPath("/test",("test"+count).getBytes());
                        System.out.println("get leader");
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }


                }
            }).start();
        }

        countDownLatch.countDown();

        System.in.read();

    }

}
