package com.sjxd.invoicecheckserver.util.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zyl
 * @date 2019/3/29 11:09
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String ip; // ip
    @Value("${spring.redis.port}")
    private int port; // 端口
    @Value("${spring.redis.password}")
    private String password; // 密码(原始默认是没有密码)
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive; // 最大连接数
    @Value("${spring.redis.timeOut}")
    private int timeOut; // 超时时间
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;
    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Integer maxWaitMillis;
    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;
    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;
//    @Value("${spring.redis.cluster.nodes}")
//    private String clusterNodes;
//    @Value("${spring.redis.cluster.max-redirects}")
//    private Integer mmaxRedirectsac;

    static JedisPool jedisPool;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(true);
        // jedisPoolConfig.setEvictionPolicy();
        // jedisPoolConfig.setEvictionPolicyClassName();
        // jedisPoolConfig.setEvictorShutdownTimeoutMillis();
        // jedisPoolConfig.setFairness();
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        // jedisPoolConfig.setJmxNameBase();
        // jedisPoolConfig.setJmxNamePrefix();
        // jedisPoolConfig.setLifo();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // jedisPoolConfig.setMinIdle();
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // jedisPoolConfig.setSoftMinEvictableIdleTimeMillis();
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnCreate(true);
        // jedisPoolConfig.setTestOnReturn();
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        RedisConfig.jedisPool = new JedisPool(jedisPoolConfig, ip, port, timeOut, null, false);
        return RedisConfig.jedisPool;
    }

}