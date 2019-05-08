package com.nyist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/7/12/012.
 */

@Configuration
@PropertySource(value = "classpath:redis.properties")
public class RedisSpringConfig {
    @Value("${redis.maxTotal}")
    private Integer redisMaxTotal;
    @Value("${redis.node1.host}")
    private String redisNode1Host;
    @Value("${redis.node1.port}")
    private Integer redisNode1Port;
    @Value("${redis.clusterNodes}")
    private String clusterNodes;
    @Value("${redis.commandTimeout}")
    private Integer commandTimeout;
    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisMaxTotal);
        return jedisPoolConfig;
    }
    @Bean
    public JedisPool getJedisPool(){    // 省略第一个参数则是采用 Protocol.DEFAULT_DATABASE
        JedisPool jedisPool = new JedisPool(jedisPoolConfig(), redisNode1Host, redisNode1Port);
        return jedisPool;
    }
    @Bean
    public ShardedJedisPool shardedJedisPool() {
        List<JedisShardInfo> jedisShardInfos = new ArrayList<JedisShardInfo>();
        jedisShardInfos.add(new JedisShardInfo(redisNode1Host, redisNode1Port));
        return new ShardedJedisPool(jedisPoolConfig(), jedisShardInfos);
    }

    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort: serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));
        }
        return new JedisCluster(nodes, commandTimeout);
    }

}
