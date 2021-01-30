package com.qf.ratelimiter;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        RateLimiter rateLimiter = new RateLimiter();

        for (int i = 0; i < 15; i++) {
            try {
                Thread.sleep(155);
            } catch (Exception e) {
            }
            System.out.println(rateLimiter.limit("app-1", "/v1/user/info"));
        }
    }
}
