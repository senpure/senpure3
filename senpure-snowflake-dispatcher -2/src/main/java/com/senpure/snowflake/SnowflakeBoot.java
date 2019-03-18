package com.senpure.snowflake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SnowflakeBoot
 *
 * @author senpure
 * @time 2019-03-11 17:16:52
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SnowflakeBoot {
    public static void main(String[] args) {

        SpringApplication.run(SnowflakeBoot.class, args);
    }
}
