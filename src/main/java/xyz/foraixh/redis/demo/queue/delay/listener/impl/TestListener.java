package xyz.foraixh.redis.demo.queue.delay.listener.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.foraixh.redis.demo.queue.delay.RedisDelayedQueueListener;

/**
 * @author myvina
 * @date 2021/04/20 16:07
 * @usage
 */

@Component(RedisDelayedQueueListener.TEST_LISTENER)
public class TestListener implements RedisDelayedQueueListener<String> {
    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void accept(String s) {
        log.info("listener test: {}", s);
    }
}
