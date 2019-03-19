package com.senpure.snowflake.service;

import com.senpure.snowflake.model.ServerCenterAndWork;

/**
 * SnowflakeDispatchService
 *
 * @author senpure
 * @time 2019-03-12 10:04:00
 */
public interface SnowflakeDispatchService {

    ServerCenterAndWork dispatch(String serverName, String serverKey);
}
