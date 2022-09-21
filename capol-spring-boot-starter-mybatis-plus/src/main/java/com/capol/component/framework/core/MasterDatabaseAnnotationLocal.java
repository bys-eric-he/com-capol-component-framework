package com.capol.component.framework.core;

public class MasterDatabaseAnnotationLocal {

    private static final ThreadLocal<Boolean> local = new ThreadLocal<>();

    public static void masterSetting() {
        local.set(true);
    }

    public static boolean get() {
        if (local.get() == null) {
            return false;
        }
        return local.get();
    }

    public static void clear() {
        local.remove();
    }

}