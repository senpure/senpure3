package com.senpure.base.configuration;


import com.senpure.base.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;



@ConfigurationProperties(
        prefix = "snowflake"
)
public class IDGeneratorAutoConfiguration {

    protected Logger logger = LoggerFactory.getLogger(IDGeneratorAutoConfiguration.class);
    private int dataCenterId =0;
    private int workerId=0;
    @ConditionalOnProperty(
            prefix = "snowflake",
            name = {"enabled"}
    )
    @Bean
    public IDGenerator getIdGenerator() {
        logger.debug("workerId is {} dataCenterId is {}",workerId, dataCenterId);
        return new IDGenerator(dataCenterId, workerId);
    }

    public int getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(int dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }
}
