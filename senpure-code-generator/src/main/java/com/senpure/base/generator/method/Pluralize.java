package com.senpure.base.generator.method;

import com.senpure.base.util.Inflector;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * 复数
 */
public class Pluralize implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        SimpleScalar simpleScalar= (SimpleScalar) list.get(0);
        return Inflector.getInstance().pluralize(simpleScalar.getAsString());
    }

    public static void main(String[] args) {

        System.out.println(Inflector.getInstance().pluralize("data"));
    }
}
