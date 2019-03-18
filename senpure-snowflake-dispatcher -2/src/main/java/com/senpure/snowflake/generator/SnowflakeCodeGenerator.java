package com.senpure.snowflake.generator;

import com.senpure.base.generator.CodeGenerator;

/**
 * SnowflakeCodeGenerator
 *
 * @author senpure
 * @time 2019-03-11 17:26:44
 */
public class SnowflakeCodeGenerator {

    public static void main(String[] args) {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generate("com.senpure.snowflake");
    }
}
