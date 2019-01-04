package com.senpure.demo;


import com.senpure.base.AppEvn;
import com.senpure.base.generator.CodeGenerator;

/**
 * DemoCodeGenerator
 *
 * @author senpure
 * @time 2019-01-03 16:07:04
 */
public class DemoCodeGenerator {
    public static void main(String[] args) {
        AppEvn.markClassRootPath();
      CodeGenerator codeGenerator = new CodeGenerator();
      codeGenerator.generate("com.senpure.book");
      //  EntityReader entityReader = new EntityReader(new GeneratorConfig());
    }
}
