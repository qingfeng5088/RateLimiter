package com.qf.ratelimiter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RateLimiterConfig {
    public static String RATELIMITALG = "WindowRateLimitAlg";
    public static int STATISTICALINTERVAL = 1;

    static {
        InputStream in = RateLimiterConfig.class.getClassLoader().getResourceAsStream("rateLimiterConfig.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            RATELIMITALG = prop.getProperty("RateLimitAlg");
            STATISTICALINTERVAL = Integer.parseInt(prop.getProperty("StatisticalInterval"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
