package com.senpure.base.generator.method;

import com.senpure.base.generator.ModelField;
import com.senpure.base.util.DateFormatUtil;
import com.senpure.base.util.StringUtil;
import com.senpure.base.util.TimeCalculator;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ApiModelProperty
 *
 * @author senpure
 * @time 2019-01-11 14:12:15
 */
public class ApiModelProperty implements TemplateMethodModelEx {

    private Map<String, Integer> map = new HashMap<>();
    private int startPostion = 1;

    @Override
    public Object exec(List list) throws TemplateModelException {

        SimpleScalar object = (SimpleScalar) list.get(0);
        StringModel field = (StringModel) list.get(1);
        String scalar = "";
        if (list.size() >= 3) {
            SimpleScalar simpleScalar = (SimpleScalar) list.get(2);
            scalar = simpleScalar.getAsString();
        }

        String name = object.getAsString();
        Integer position = map.get(name);
        if (position == null) {
            position = startPostion;
        }
        ModelField modelField = (ModelField) field.getWrappedObject();
        StringBuilder sb = new StringBuilder();
        sb.append("@ApiModelProperty(");
        if (modelField.isHasExplain()) {
            if (scalar.equals("start")) {
                sb.append("value = \"start ").append(modelField.getExplain()).append("\", ");
            } else if (scalar.equals("end")) {
                sb.append("value = \"end ").append(modelField.getExplain()).append("\", ");
            }
            else if (scalar.equals("order")) {
                sb.append("value = \"").append(modelField.getName()).append(" 排序\" , ");
                sb.append("allowableValues = \"ASC,DESC").append("\", ");
            }
            else {
                sb.append("value = \"").append(modelField.getExplain()).append("\", ");
            }

        }
        if (!modelField.getClazzType().equals("String")&&scalar.equals("order")) {
            sb.append("dataType = \"").append(StringUtil.toLowerFirstLetter(modelField.getClazzType())).append("\", ");
        }
        if (modelField.getClazzType().equals("String")) {
            sb.append("example = \"").append(modelField.getName()).append("\", ");
        } else if (modelField.getClazzType().equals("Date")) {
            if (scalar.equals("end")) {
                sb.append("example = \"").append(DateFormatUtil.format(TimeCalculator.getDayEnd().getTime())).append("\", ");
            } else {
                sb.append("example = \"").append(DateFormatUtil.format(TimeCalculator.getDayBegin().getTime())).append("\", ");
            }
        } else {
            sb.append("example = \"").append("0").append("\", ");
        }
        sb.append("position = ").append(position);
        sb.append(")");
        if (modelField.isHasExplain())
            position = position + 1;
        map.put(name, position);
        return sb.toString();
    }

}
