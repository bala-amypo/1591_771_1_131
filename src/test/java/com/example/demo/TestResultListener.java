package com.example.demo;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestExecutionResult;

public class TestResultListener implements TestExecutionListener {

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            System.out.println("Test started: " + testIdentifier.getDisplayName());
        }
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier,
                                  TestExecutionResult result) {
        if (testIdentifier.isTest()) {
            System.out.println(
                "Test finished: " + testIdentifier.getDisplayName()
                + " -> " + result.getStatus()
            );
        }
    }
}
