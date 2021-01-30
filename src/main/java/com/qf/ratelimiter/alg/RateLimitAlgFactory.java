package com.qf.ratelimiter.alg;

public class RateLimitAlgFactory {

    public static IRateLimitAlg createRateLimitAlg(String type, int limit) {
        IRateLimitAlg alg = null;
        if ("window".equals(type)) {
            alg = new WindowRateLimitAlg(limit);
        } else {
            alg = new FixedTimeRateLimitAlg(limit);
        }
        return alg;
    }
}
