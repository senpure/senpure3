package com.senpure.base.generator.method;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 补充空格
 */
public class SpaceResidual implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        SimpleNumber simpleNumber = (SimpleNumber) list.get(0);
        int len = simpleNumber.getAsNumber().intValue();
        SimpleNumber max = (SimpleNumber) list.get(1);
        int maxLen = max.getAsNumber().intValue();
        String space = "";
        for (int i = len; i < maxLen; i++) {
            space += " ";
        }
        return space;
    }

    public static void main(String[] args) {
        String a = StringUtils.leftPad("abc", 12);

        System.out.println("j" + a + "45");
    }
}
