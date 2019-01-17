package ${resultPackage};

import com.senpure.base.result.ActionResult;
import ${modelPackage}.${name};
import io.swagger.annotations.ApiModelProperty;

/**<#if hasExplain>
 * ${explain}
<#else >
 * ${name}RecordResult
 *</#if>
 * @author senpure-generator
 * @version ${.now?datetime}
 */
public class ${name}RecordResult extends ActionResult implements Serializable {
    private static final long serialVersionUID = ${serial(modelFieldMap)}L;

    @ApiModelProperty(position = 3)
    private ${name} ${nameRule(name)};

    public ${name} get${nameRule(name)?cap_first}) {
        return ${nameRule(name)};
    }

    public ${name}RecordResult set${nameRule(name)?cap_first}) {
        this.${nameRule(name)?cap_first} = ${nameRule(name)?cap_first};
        return this;
    }


}