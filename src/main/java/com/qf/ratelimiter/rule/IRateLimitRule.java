package com.qf.ratelimiter.rule;

public interface IRateLimitRule {
    ApiLimit getLimit(String appId, String url);
}
