package com.qf.ratelimiter.rule.datasource;

import com.qf.ratelimiter.rule.RuleConfig;

public interface IRuleConfigSource {
    RuleConfig load();
}
