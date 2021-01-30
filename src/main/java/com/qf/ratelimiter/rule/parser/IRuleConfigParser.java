package com.qf.ratelimiter.rule.parser;

import com.qf.ratelimiter.rule.RuleConfig;

import java.io.IOException;
import java.io.InputStream;

public interface IRuleConfigParser {
    RuleConfig parse(String configText);
    RuleConfig parse(InputStream in) throws IOException;
}
