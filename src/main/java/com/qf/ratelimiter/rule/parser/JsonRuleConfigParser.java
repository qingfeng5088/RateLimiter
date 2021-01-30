package com.qf.ratelimiter.rule.parser;

import com.alibaba.fastjson.JSON;
import com.qf.ratelimiter.rule.RuleConfig;

import java.io.IOException;
import java.io.InputStream;

public class JsonRuleConfigParser implements IRuleConfigParser {
    @Override
    public RuleConfig parse(String configText) {
        return JSON.parseObject(configText, RuleConfig.class);
    }

    @Override
    public RuleConfig parse(InputStream in) throws IOException {
        return JSON.parseObject(in, RuleConfig.class);
    }
}
