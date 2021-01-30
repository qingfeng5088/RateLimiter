package com.qf.ratelimiter.rule.datasource;

import com.qf.ratelimiter.rule.RuleConfig;
import com.qf.ratelimiter.rule.parser.IRuleConfigParser;
import com.qf.ratelimiter.rule.parser.JsonRuleConfigParser;
import com.qf.ratelimiter.rule.parser.YamlRuleConfigParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileRuleConfigSource implements IRuleConfigSource {
    // private static final Logger log = LoggerFactory.getLogger(FileRuleConfigSource.class);

    public static final String API_LIMIT_CONFIG_NAME = "ratelimiter-rule";
    public static final String YAML_EXTENSION = "yaml";
    public static final String YML_EXTENSION = "yml";
    public static final String JSON_EXTENSION = "json";

    private static final String[] SUPPORT_EXTENSIONS = new String[]{YAML_EXTENSION, YML_EXTENSION, JSON_EXTENSION};

    private static final Map<String, IRuleConfigParser> PARSER_MAP = new HashMap<>();

    static {
        PARSER_MAP.put(YAML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(YML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(JSON_EXTENSION, new JsonRuleConfigParser());
    }

    @Override
    public RuleConfig load() {
        for (String extension : SUPPORT_EXTENSIONS) {
            try (InputStream in = this.getClass().getResourceAsStream("/" + getFileNameByExt(extension))) {
                if (in != null) {
                    IRuleConfigParser parser = PARSER_MAP.get(extension);
                    return parser.parse(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String getFileNameByExt(String extension) {
        return API_LIMIT_CONFIG_NAME + "." + extension;
    }

}
