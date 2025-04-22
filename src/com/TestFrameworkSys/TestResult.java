package com.TestFrameworkSys;

import java.time.LocalDateTime;

public class TestResult {
    private boolean passed;
    private LocalDateTime timestamp;
    private String details;

    public TestResult(boolean passed, LocalDateTime timestamp, String details) {
        this.passed = passed;
        this.timestamp = timestamp;
        this.details = details;
    }

    public boolean isPassed() {
        return passed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDetails() {
        return details;
    }
}