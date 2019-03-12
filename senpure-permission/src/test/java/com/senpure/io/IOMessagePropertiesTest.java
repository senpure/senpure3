package com.senpure.io;


import com.senpure.base.struct.SpringConfigurationMetadata;
import com.senpure.base.util.JSON;
import org.junit.Test;

/**
 * IOMessagePropertiesTest
 *
 * @author senpure
 * @time 2018-11-19 10:28:20
 */
public class IOMessagePropertiesTest {


    @Test
    public void metadata(){

        SpringConfigurationMetadata metadata = new SpringConfigurationMetadata();

        SpringConfigurationMetadata.Group group =new SpringConfigurationMetadata.Group();

        group.setName("server.io");
        group.setType(String.class.getTypeName());
        group.setSourceType(IOServerProperties.class.getTypeName());
        metadata.addGroup(group);

        SpringConfigurationMetadata.Property ssl = new    SpringConfigurationMetadata.Property ();
        ssl.setName("ssl");
        ssl.setDefaultValue(false);
        metadata.addProperty(ssl);
        System.out.println(JSON.toJSONString(metadata,true));
    }
}