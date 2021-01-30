package com.qf.ratelimiter;


import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {

        RateLimiter rateLimiter = new RateLimiter();
        System.out.println(rateLimiter.limit("app-1", "/v1/user/info"));

        try {
            Thread.sleep(800);
        } catch (Exception ignored) {
        }

        for (int i = 0; i < 180; i++) {
            System.out.println("当前第" + i + "个:" + rateLimiter.limit("app-1", "/v1/user/info"));
            try {
                Thread.sleep(2);
            } catch (Exception e) {
            }
        }
    }
}
