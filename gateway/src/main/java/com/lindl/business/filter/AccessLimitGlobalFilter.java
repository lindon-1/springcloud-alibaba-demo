package com.lindl.business.filter;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 限制接口访问
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        String ip = remoteAddress.getAddress().toString();
        if(ip.equals("127.0.0.1") || ip.equals("/0:0:0:0:0:0:0:1")){
            //根据网卡取本机配置的IP
            InetAddress inet=null;
            try {
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
            }
            if (inet.getHostAddress() != null) {
                ip= inet.getHostAddress();
            }
        }
        String path = exchange.getRequest().getPath().toString();
        String key = "REDIS:" + ip + ":" + path;
        // 加分布式锁
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock();
            Integer allowNum = redisTemplate.opsForValue().get(key);
            System.out.println("剩余访问次数:" + allowNum);
            if (!Objects.isNull(allowNum) && allowNum > 0) {
                System.out.println(redisTemplate.getExpire(key));
                redisTemplate.opsForValue().set(key, allowNum - 1, redisTemplate.getExpire(key) , TimeUnit.SECONDS);
            } else if (!Objects.isNull(allowNum) && allowNum <= 0) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
                return response.setComplete();
            } else {
                redisTemplate.opsForValue().set(key, 3, 1, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
