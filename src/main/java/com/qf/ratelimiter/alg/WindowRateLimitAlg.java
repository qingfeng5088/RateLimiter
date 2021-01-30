package com.qf.ratelimiter.alg;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.qf.ratelimiter.config.RateLimiterConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 滑动时间窗口算法
 * <p>
 * 滑动时间窗口计数器算法思想：针对固定时间算法会在临界点存在瞬间大流量冲击的场景，滑动时间窗口计数器算法应运而生。
 * 它将时间窗口划分为更小的时间片段，每过一个时间片段，我们的时间窗口就会往右滑动一格，每个时间片段都有独立的计数器。
 * 我们在计算整个时间窗口内的请求总数时会累加所有的时间片段内的计数器。时间窗口划分的越细，那么滑动窗口的滚动就越平滑，
 * 限流的统计就会越精确。
 */
public class WindowRateLimitAlg implements IRateLimitAlg {
    //限流阈值
    private final int limit;
    private final LoadingCache<Long, AtomicLong> counter =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(RateLimiterConfig.STATISTICALINTERVAL, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, AtomicLong>() {
                        @Override
                        public AtomicLong load(Long seconds) {
                            return new AtomicLong(0);
                        }
                    });

    public WindowRateLimitAlg(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean tryAcquire() {
        long time = System.currentTimeMillis();
        try {
            counter.get(time).addAndGet(1);
            return counter.asMap().values().stream().mapToLong(AtomicLong::get).sum() < limit;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
