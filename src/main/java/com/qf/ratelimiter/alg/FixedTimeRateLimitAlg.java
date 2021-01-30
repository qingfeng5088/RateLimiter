package com.qf.ratelimiter.alg;

import com.google.common.base.Stopwatch;
import com.qf.ratelimiter.config.RateLimiterConfig;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 固定时间窗口算法
 */
public class FixedTimeRateLimitAlg implements IRateLimitAlg {
    private static final long TRY_LOCK_TIMEOUT = 200L; //200ms
    private final Stopwatch stopwatch;

    private final AtomicInteger currentCount = new AtomicInteger(0);
    private final int limit;

    private final Lock lock = new ReentrantLock();

    public FixedTimeRateLimitAlg(int limit) {
        this(limit, Stopwatch.createStarted());
    }

    protected FixedTimeRateLimitAlg(int limit, Stopwatch stopwatch) {
        this.limit = limit;
        this.stopwatch = stopwatch;
    }

    @Override
    public boolean tryAcquire() {
        int updatedCount = currentCount.incrementAndGet();
        if (updatedCount < limit) return true;
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(RateLimiterConfig.STATISTICALINTERVAL)) {
                        currentCount.set(0);
                        stopwatch.reset();
                        stopwatch.start();
                    }
                    updatedCount = currentCount.incrementAndGet();
                    return updatedCount <= limit;
                } finally {
                    lock.unlock();
                }
            } else {
                throw new InternalError();
            }
        } catch (InterruptedException e) {
            throw new InternalError();
        }
    }
}
