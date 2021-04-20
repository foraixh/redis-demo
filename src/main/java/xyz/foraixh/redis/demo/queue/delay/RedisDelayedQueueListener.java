package xyz.foraixh.redis.demo.queue.delay;

import java.util.function.Consumer;

/**
 * @author myvina
 * @date 2021/04/20 15:55
 * @usage 延时队列监听器接口类
 */
public interface RedisDelayedQueueListener<T> extends Consumer<T> {
    /**
     * 用作定义测试延时队列名称
     */
    String TEST_LISTENER = "TEST_LISTENER";
}
