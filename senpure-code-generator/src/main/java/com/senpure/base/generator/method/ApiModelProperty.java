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
    private int startPosition = 1;

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
            position = startPosition;
        }
        ModelField modelField = (ModelField) field.getWrappedObject();
        StringBuilder sb = new StringBuilder();
        sb.append("@ApiModelProperty(");
        if (scalar.equals("start")) {
            sb.append("value = \"").append(modelField.getName()).append(" 开始范围 (>=)\", ");
        } else if (scalar.equals("end")) {
            sb.append("value = \"").append(modelField.getName()).append(" 结束范围 (<=)\", ");
        } else if (scalar.equals("order")) {
            sb.append("value = \"").append(modelField.getName()).append(" 排序\" , ");
            sb.append("allowableValues = \"ASC,DESC").append("\", ");
        } else if (scalar.equals("pattern")) {
            sb.append("value = \"").append(modelField.getName()).append(" 格式\", ");
        } else {
            if (modelField.isHasExplain()) {
                sb.append("value = \"").append(modelField.getExplain()).append("\", ");
            }
        }

        if (!modelField.getClazzType().equals("String")) {
            if (!scalar.equals("order") && !scalar.equals("pattern")) {
                sb.append("dataType = \"").append(getDataType(modelField)).append("\", ");
            }
        }

        if (scalar.equals("order")) {
        } else if (modelField.getClazzType().equals("String")) {
            sb.append("example = \"").append(modelField.getName()).append("\", ");
        } else if (modelField.getClazzType().equalsIgnoreCase("boolean")) {
          //  sb.append("example = \"").append("true").append("\", ");
        } else if (modelField.getClazzType().equals("Date")) {
            if (scalar.equals("end")) {
                sb.append("example = \"").append(DateFormatUtil.format(TimeCalculator.getDayEnd().getTime())).append("\", ");
            } else if (scalar.equals("start")) {
                sb.append("example = \"").append(DateFormatUtil.format(TimeCalculator.getDayBegin().getTime())).append("\", ");

            } else if (scalar.equals("pattern")) {
                sb.append("example = \"").append(DateFormatUtil.DFP_Y2S).append("\", ");
            } else {
                sb.append("example = \"").append(DateFormatUtil.format(TimeCalculator.getDayBegin().getTime())).append("\", ");
            }
        } else {
            if (modelField.isLongTime()) {
                sb.append("example = \"").append(TimeCalculator.getDayBegin().getTimeInMillis()).append("\", ");
            } else {
                sb.append("example = \"").append(666666).append("\", ");
            }
        }
        if (position > 0) {
            sb.append("position = ").append(position);
        }

        sb.append(")");
        position = position + 1;
        map.put(name, position);
        return sb.toString();
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public String getDataType(ModelField modelField) {
        if (modelField.getClazzType().equalsIgnoreCase("Integer")) {
            return "int";
        } else if (modelField.getClazzType().equalsIgnoreCase("Date")) {
            return "date-time";
        } else {
            return StringUtil.toLowerFirstLetter(modelField.getClazzType());
        }
    }
}
