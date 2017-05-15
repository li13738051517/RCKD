package com.rckd.utils;

/**
 * Created by LiZheng on 2017/3/15 0015.
 */

/**
 * Not a real crash reporting library!
 */
public class FakeCrashLibrary {
    private String tag=FakeCrashLibrary.class.getName();

    public static void log(int priority, String tag, String message) {
        // TODO add log entry to circular buffer.
    }

    public static void logWarning(Throwable t) {
        // TODO report non-fatal warning.
    }

    public static void logError(Throwable t) {
        // TODO report non-fatal error.
    }

    private FakeCrashLibrary() {
        throw new AssertionError("No instances.");
    }

}
