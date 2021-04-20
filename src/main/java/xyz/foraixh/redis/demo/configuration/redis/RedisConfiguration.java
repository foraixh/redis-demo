package xyz.foraixh.redis.demo.configuration.redis;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.foraixh.redis.demo.queue.delay.RedisDelayedQueue;
import xyz.foraixh.redis.demo.queue.delay.RedisDelayedQueueListener;

import java.util.concurrent.*;

/**
 * @author myvina
 * @date 2021/04/20 15:52
 * @usage redis相关配置
 */

@Configuration
public class RedisConfiguration implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);

    private final RedissonClient redissonClient;
    @Qualifier("redisDelayedQueueConsumerPool")
    private final ExecutorService redisDelayedQueueConsumerPool;

    public RedisConfiguration(RedissonClient redissonClient, ExecutorService redisDelayedQueueConsumerPool) {
        this.redissonClient = redissonClient;
        this.redisDelayedQueueConsumerPool = redisDelayedQueueConsumerPool;
    }

    @Bean
    public RedisDelayedQueue redisDelayedQueue() {
        return new RedisDelayedQueue(redissonClient);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(RedisDelayedQueueListener.class).forEach(this::listenerStart);
    }

    /**
     * 启动线程获取队列*
     * @param queueName                 queueName
     * @param redisDelayedQueueListener 任务回调监听
     * @param <T>                       泛型
     */
    private <T> void listenerStart(String queueName, RedisDelayedQueueListener<T> redisDelayedQueueListener) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);

        redisDelayedQueueConsumerPool.execute(() -> {
            T t;
            while (true) {
                try {
                    t = blockingFairQueue.take();
                    redisDelayedQueueListener.accept(t);
                } catch (InterruptedException e) {
                    log.error("处理错误", e);
                }
            }
        });
    }
}
