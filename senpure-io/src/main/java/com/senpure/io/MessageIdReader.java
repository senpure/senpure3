package com.senpure.io;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageIdReader
 *
 * @author senpure
 * @time 2019-02-15 16:48:04
 */
public class MessageIdReader {

    public static Map<Long, String> idMaps = new HashMap<>();

    public static String read(long messageId) {
        return idMaps.computeIfAbsent(messageId, aLong -> aLong + "");
    }
}
