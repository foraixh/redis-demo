package xyz.foraixh.redis.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.foraixh.redis.demo.queue.delay.RedisDelayedQueue;
import xyz.foraixh.redis.demo.queue.delay.RedisDelayedQueueListener;

import java.util.concurrent.TimeUnit;

/**
 * @author myvina
 * @date 2021/04/20 16:11
 * @usage
 */
@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private final RedisDelayedQueue redisDelayedQueue;

    public TestController(RedisDelayedQueue redisDelayedQueue) {
        this.redisDelayedQueue = redisDelayedQueue;
    }

    @GetMapping("/redisDelayQueueTest")
    public String redisDelayQueueTest() {
        redisDelayedQueue.addItem("延时测试", 5, TimeUnit.HOURS, RedisDelayedQueueListener.TEST_LISTENER);
        log.info("延时测试, 5秒后触发");
        return "延时测试, 5秒后触发";
    }
}
