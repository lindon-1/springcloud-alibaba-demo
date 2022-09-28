package com.lindl.business.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Value("${spring.redisson.address}")
    String REDIS_URL;

    @Value("${spring.redisson.database:8}")
    Integer REDIS_DATABASE;

    @Value("${spring.redisson.password}")
    String REDIS_PASSWORD;


    @Bean(destroyMethod="shutdown")
    RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.setCodec(StringCodec.INSTANCE);
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(REDIS_URL)
                .setDatabase(REDIS_DATABASE)
                .setPassword(REDIS_PASSWORD);

        return Redisson.create(config);
   }

}
