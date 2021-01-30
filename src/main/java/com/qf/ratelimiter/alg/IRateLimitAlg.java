package com.qf.ratelimiter.alg;

public interface IRateLimitAlg {
    boolean tryAcquire();
}
