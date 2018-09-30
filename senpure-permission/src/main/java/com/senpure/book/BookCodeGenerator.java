package com.senpure.book;

import com.senpure.base.generator.Config;
import com.senpure.base.generator.DatabaseGenerator;


public class BookCodeGenerator {
    public static void main(String[] args) {


        DatabaseGenerator generator=new DatabaseGenerator();
        Config config = new Config();
        config.setAllCover(true);
        generator.generate("com.senpure.book",config);
    }
}
