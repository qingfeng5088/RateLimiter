package com.qf.ratelimiter.rule.parser;

import com.qf.ratelimiter.rule.RuleConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlRuleConfigParser implements IRuleConfigParser{
    @Override
    public RuleConfig parse(String configText) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(configText, RuleConfig.class);
    }

    @Override
    public RuleConfig parse(InputStream in) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(in, RuleConfig.class);
    }
}
