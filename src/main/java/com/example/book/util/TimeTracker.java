package com.example.book.util;

import java.time.Duration;
import java.time.Instant;
// 크롤링 속도확인을 하기 위한 클래스
public class TimeTracker {
    private Instant startTime = null;
    private Instant endTime = null;

    public void start() {
        startTime = Instant.now();
    }

    public void stop() {
        endTime = Instant.now();
    }

    public Duration getElapsedTime() {
        if (startTime == null || endTime == null) {
            throw new IllegalStateException("Cannot get elapsed time without starting and stopping the timer.");
        }
        return Duration.between(startTime, endTime);
    }
}
