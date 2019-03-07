package com.senpure.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JackJsonUtil 初始化了一个默认objectMapper null 不序列化
 *
 * @author senpure
 * @time 2019-03-07 09:42:10
 */
public class JacksonUtil {

    private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static ObjectMapper defaultMapper = new ObjectMapper();

    static {
        // defaultMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        defaultMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        // @Deprecated
        //defaultMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public static String toJSONString(Object object) {
        try {
            return defaultMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("jackson 对象序列化json字符串出错 " + object.toString(), e);
        }
        return "{}";
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        try {
            return defaultMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            logger.error("jackson json字符串反序列化对象出错 " + jsonStr, e);
        }
        return null;
    }

    public static ObjectMapper getDefaultMapper() {
        return defaultMapper;
    }

}
