package com.senpure.base.generator.method;

import com.senpure.base.util.DateFormatUtil;
import com.senpure.base.util.StringUtil;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.Date;
import java.util.List;

/**
 * 给mapperxml的提示
 */
public class MapperXmlTips implements TemplateMethodModelEx {

    private static int lineLen = 88;
    private static String newLine = "\n";
    private static String space = "    ";

    @Override
    public Object exec(List list) throws TemplateModelException {
        SimpleScalar simpleScalar = (SimpleScalar) list.get(0);
        String name = simpleScalar.getAsString();
        SimpleScalar packageName = (SimpleScalar) list.get(1);
        return nameRule(name, packageName.getAsString());
    }

    public static String nameRule(String name, String pageName) {

        StringBuilder sb = new StringBuilder();
        String lowName = StringUtil.toLowerFirstLetter(name);
        sb.append("该xml为[senpure-code-generator]于[")
                .append(DateFormatUtil.format(new Date())).
                append("]自动生成，如果预计字段经常变动，不建议修改。如果该xml不能满足需要可新建一个mxl，如")
                .append(name).append("ExpandMapper.xml，").
                append("将命名空间指向").append(pageName)
                .append(".ModelAMapper即可，该xml中定义的")
                .append(lowName).append("ResultMap与").append(lowName).append("AllColumns等其他可以在新建的xml中直接引用。");

        int len = 0;
        StringBuilder out = new StringBuilder();
        out.append(space);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (isChinese(c)) {
                len += 2;
            } else {
                len++;
            }

            if (len == lineLen) {
                out.append(c);
                if (i < sb.length() - 1) {
                    out.append(newLine).append(space);
                    len = 0;
                }
            } else if (len > lineLen) {
                out.append(newLine).append(space);
                out.append(c);
                len = 2;
            } else {
                out.append(c);
            }
        }


        return out.toString();
    }

    private static boolean isChinese(char c) {
        if (c >= 0x4E00 && c <= 0x9FA5) {
            return true;
        }
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }


        return false;
    }


    public static void main(String[] args) {

        System.out.println(nameRule("User", "com.senoure"));
    }
}
