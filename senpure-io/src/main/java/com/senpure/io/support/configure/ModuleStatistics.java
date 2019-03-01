package com.senpure.io.support.configure;

import java.util.HashSet;
import java.util.Set;

/**
 * ModulleStatistics
 *
 * @author senpure
 * @time 2019-02-28 17:07:31
 */
public class ModuleStatistics {


    public static final String MODULE_GATEWAY = "GATEWAY";
    public static final String MODULE_PRODUCER = "PRODUCER";
    public static final String MODULE_CONSUMER = "CONSUMER";

    private static boolean ENABLE_GATEWAY = false;
    private static boolean ENABLE_PRODUCER = false;
    private static boolean ENABLE_CONSUMER = false;
    private static Set<String> modules = new HashSet<>();

    public static int getModuleAmount() {
        return modules.size();
    }

    public static void enableGateway() {
        ENABLE_GATEWAY = true;
        modules.add(MODULE_GATEWAY);
    }

    public static void enableConsumer() {
        ENABLE_CONSUMER = true;
        modules.add(MODULE_CONSUMER);
    }

    public static void enableProducer() {
        ENABLE_PRODUCER = true;
        modules.add(MODULE_PRODUCER);
    }

    public static boolean isEnableGateway() {
        return ENABLE_GATEWAY;
    }

    public static boolean isEnableProducer() {
        return ENABLE_PRODUCER;
    }

    public static boolean isEnableConsumer() {
        return ENABLE_CONSUMER;
    }
}
