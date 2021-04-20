package xyz.foraixh.redis.demo.configuration.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author myvina
 * @date 2021/04/20 16:02
 * @usage
 */

@Configuration
public class RedisThreadPoolConfiguration {
    @Bean
    public ExecutorService redisDelayedQueueConsumerPool() {
        return new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new DefaultThreadFactory("redis-delayed-queue-consumer-pool"));
    }

    /**
     * 默认的线程池工厂类；可自定义线程名称前缀
     */
    public static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = prefix + "-pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
