package ru.otus.testRunner.enums;

import lombok.Getter;

@Getter
public enum TestStatus {
    SUCCESS,
    FAILED,
    SKIPPED,
    FAILED_ON_CLEANUP;
}
