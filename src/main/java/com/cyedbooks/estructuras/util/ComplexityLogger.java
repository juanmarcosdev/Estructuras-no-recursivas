package com.cyedbooks.estructuras.util;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ComplexityLogger {

    private static final AtomicBoolean ENABLED = new AtomicBoolean(false);

    private ComplexityLogger() {
    }

    public static void setEnabled(boolean enabled) {
        ENABLED.set(enabled);
    }

    public static boolean isEnabled() {
        return ENABLED.get();
    }

    public static void log(String structureName, String operation, String timeComplexity) {
        if (ENABLED.get()) {
            System.out.printf("[complejidad] %s.%s -> %s%n", structureName, operation, timeComplexity);
        }
    }
}
