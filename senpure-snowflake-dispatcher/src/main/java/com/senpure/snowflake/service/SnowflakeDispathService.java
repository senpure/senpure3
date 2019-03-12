package com.senpure.snowflake.service;

import com.senpure.snowflake.model.ServerCenterAndWork;

/**
 * SnowflakeDispathService
 *
 * @author senpure
 * @time 2019-03-12 10:04:00
 */
public interface SnowflakeDispathService {

    ServerCenterAndWork dispatch(String serverName, String serverKey);
}
