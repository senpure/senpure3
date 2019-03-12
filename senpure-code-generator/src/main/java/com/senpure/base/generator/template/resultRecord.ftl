package ${resultPackage};

import com.senpure.base.result.ActionResult;
import com.senpure.base.result.Result;
import ${modelPackage}.${name};
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Locale;

/**<#if hasExplain>
 * ${explain}
 *</#if>
 * @author senpure-generator
 * @version ${.now?datetime}
 */
public class ${name}${globalConfig.resultRecordSuffix} extends ActionResult {
    private static final long serialVersionUID = ${serial(modelFieldMap)}L;

    public static final String RECORD_NAME = "${nameRule(name)}";

    <#--这里的注释swagger 不会读取，不用注释-->
    @ApiModelProperty(position = 3)
    private ${name} ${nameRule(name)};

    public static ${name}${globalConfig.resultRecordSuffix} success() {
        return new ${name}${globalConfig.resultRecordSuffix}(Result.SUCCESS);
    }

    public static ${name}${globalConfig.resultRecordSuffix} dim() {
        return new ${name}${globalConfig.resultRecordSuffix}(Result.ERROR_DIM);
    }

    public static ${name}${globalConfig.resultRecordSuffix} failure() {
        return new ${name}${globalConfig.resultRecordSuffix}(Result.FAILURE);
    }

    public static ${name}${globalConfig.resultRecordSuffix} notExist() {
        return new ${name}${globalConfig.resultRecordSuffix}(Result.TARGET_NOT_EXIST);
    }

    public static ${name}${globalConfig.resultRecordSuffix} result(int code) {
        return new ${name}${globalConfig.resultRecordSuffix}(code);
    }

    public ${name}${globalConfig.resultRecordSuffix}() {
    }

    public ${name}${globalConfig.resultRecordSuffix}(int code) {
        super(code);
    }

    public ${name} get${nameRule(name)?cap_first}() {
        return ${nameRule(name)};
    }

    public ${name}${globalConfig.resultRecordSuffix} set${nameRule(name)?cap_first}(${name} ${nameRule(name)}) {
        this.${nameRule(name)} = ${nameRule(name)};
        return this;
    }

    @Override
    public ${name}${globalConfig.resultRecordSuffix} setClientFormat(boolean clientFormat) {
        super.setClientFormat(clientFormat);
        return this;
    }

    @Override
    public ${name}${globalConfig.resultRecordSuffix} setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    @Override
    public ${name}${globalConfig.resultRecordSuffix} setMessageArgs(List<String> messageArgs) {
        super.setMessageArgs(messageArgs);
        return this;
    }

    @Override
    public ${name}${globalConfig.resultRecordSuffix} wrapMessage(Locale locale) {
        super.wrapMessage(locale);
        return this;
    }

    @Override
    public ${name}${globalConfig.resultRecordSuffix} wrapMessage(Locale locale, Object... args) {
        super.wrapMessage(locale, args);
        return this;
    }
}