package com.senpure.demo;

import com.senpure.base.AppEvn;
import com.senpure.base.generator.Config;
import com.senpure.base.generator.DatabaseGenerator;


public class DemoGenerator {
    public static void main(String[] args) {

      //

        System.setProperty("PID", AppEvn.getPid());
        DatabaseGenerator mybatisGenerator = new DatabaseGenerator();
        System.out.println(AppEvn.getClassRootPath());
        Config config = new Config();
        config.setAllCover(true);
        config.setSingleCheck(true);

        config.setUserCriteriaStr(true);
        config.setMenuStartId(1000);

        config.setDefaultCache(true,false,false,true);

        mybatisGenerator.generate("com.senpure.demo",config);
    }
}
