package com.qf.ratelimiter;

import com.qf.ratelimiter.alg.RateLimitAlg;
import com.qf.ratelimiter.rule.ApiLimit;
import com.qf.ratelimiter.rule.RateLimitRule;
import com.qf.ratelimiter.rule.RuleConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流主类
 */
public class RateLimiter {
  //  private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);
    // 为每个api在内存中存储限流计数器
    private final ConcurrentHashMap<String, RateLimitAlg> counters = new ConcurrentHashMap<>();
    private final RateLimitRule rule;

    public RateLimiter() throws IOException {
        // 将限流规则配置文件ratelimiter-rule.yaml中的内容读取到RuleConfig中
        RuleConfig ruleConfig = null;
        try (InputStream in = this.getClass().getResourceAsStream("/ratelimiter-rule.yaml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(in, RuleConfig.class);
            }
        }
        // 将限流规则构建成支持快速查找的数据结构
        this.rule = new RateLimitRule(ruleConfig);
    }

    public boolean limit(String appId, String url) throws InternalError {
        ApiLimit apiLimit = rule.getLimit(appId, url);
        if (apiLimit == null) return true;

        // 获取api对应在内存中的限流计数器（rateLimiterCounter）
        String counterKey = appId + ":" + apiLimit.getApi();
        RateLimitAlg rateLimitCounter = counters.get(counterKey);

        if (rateLimitCounter == null) {
            RateLimitAlg newRateLimitCounter = new RateLimitAlg(apiLimit.getLimit());
            rateLimitCounter = counters.putIfAbsent(counterKey, newRateLimitCounter);
            if (rateLimitCounter == null) {
                rateLimitCounter = newRateLimitCounter;
            }
        }

        // 判断是否限流
        return rateLimitCounter.tryAcquire();
    }
}
