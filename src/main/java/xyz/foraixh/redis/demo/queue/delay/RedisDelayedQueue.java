package xyz.foraixh.redis.demo.queue.delay;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author myvina
 * @date 2021/04/20 15:53
 * @usage redis延时队列
 */
public class RedisDelayedQueue {
    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueue.class);

    private final RedissonClient redissonClient;

    public RedisDelayedQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 添加队列
     * @param queueName 延时队列名称
     * @param t        DTO传输类
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <T>      泛型
     */
    public <T> void addItem(T t, long delay, TimeUnit timeUnit, String queueName) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(t, delay, timeUnit);
    }
}
