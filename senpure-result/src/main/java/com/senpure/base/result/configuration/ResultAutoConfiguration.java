package com.senpure.base.result.configuration;

import com.senpure.base.result.Result;
import com.senpure.base.result.ResultHelper;
import org.springframework.context.annotation.Bean;

/**
 * ResultAutoConfiguration
 *
 * @author senpure
 * @time 2019-01-18 17:39:58
 */
public class ResultAutoConfiguration {

    @Bean
    public ResultHelper resultHelper() {
        return new ResultHelper();
    }

    @Bean
    public Result result() {
        return new Result();
    }
}
