package ${criteriaPackage};

import com.senpure.base.criterion.CriteriaStr;
<#if hasDate>
import com.senpure.base.struct.PatternDate;
import com.senpure.base.validator.DynamicDate;
import io.swagger.annotations.ApiModelProperty;
</#if>

import java.io.Serializable;

<#global "int"="Integer"/>
/**<#if hasExplain>
 * ${explain}
 *</#if>
 * @author senpure-generator
 * @version ${.now?datetime}
 */
public class ${name}CriteriaStr extends CriteriaStr implements Serializable {
    private static final long serialVersionUID = ${serial(modelFieldMap)}L;

<#if id.hasExplain>
    //${id.explain}
</#if>
    ${id.accessType} String ${id.name};
<#if version??>
    <#if version.hasExplain>
    //${version.explain}
    </#if>
      @ApiModelProperty(hidden = true )
    ${version.accessType} String ${version.name};
</#if>
<#list modelFieldMap?values as field>
    <#if field.strShow>
        <#if field.hasExplain>
    //${field.explain}
        </#if>
    ${apiModelProperty(name,field)}
    ${field.accessType} String ${field.name};
    <#if field.hasCriteriaRange>
     ${apiModelProperty(name,field,"start")}
    ${field.accessType} String start${field.name?cap_first};
     ${apiModelProperty(name,field,"end")}
    ${field.accessType} String end${field.name?cap_first};
     </#if><#-- 范围判断-->
        <#if field.date>
    //${field.name} 时间格式
    ${field.accessType} String ${field.name}Pattern ;
    @DynamicDate
    ${field.accessType} PatternDate ${field.name}Valid = new PatternDate();
            <#if field.hasCriteriaRange>
    @DynamicDate
    ${field.accessType} PatternDate start${field.name?cap_first}Valid = new PatternDate();
    @DynamicDate
    ${field.accessType} PatternDate end${field.name?cap_first}Valid = new PatternDate();
            </#if><#--范围-->
        </#if><#--时间类型-->
    </#if>
</#list>
<#list modelFieldMap?values as field>
    <#if field.strShow>
        <#if field.criteriaOrder>
    //table [${tableName}][column = <#if field.longDate??>${field.longDate.column}<#else >${field.column}</#if>] criteriaOrder
    ${apiModelProperty(name,field,"order")}
    ${field.accessType} String ${field.name}Order;
        </#if>
    </#if>
</#list>
    public ${name}Criteria to${name}Criteria() {
        ${name}Criteria criteria = new ${name}Criteria();
        criteria.setUsePage(Boolean.valueOf(getUsePage()));
        criteria.setPage(Integer.valueOf(getPage()));
        criteria.setPageSize(Integer.valueOf(getPageSize()));
        criteria.setStartDate(getStartDateValid().getDate());
        criteria.setEndDate(getEndDateValid().getDate());
<#assign field = id/>
<#include "strFieldTo.ftl">
<#if version??>
<#assign field = version/>
<#include "strFieldTo.ftl">
</#if>
<#list modelFieldMap?values as field>
<#include "strFieldTo.ftl">
</#list>
        return criteria;
    }
<#if hasRange>

    @Override
    protected void rangeStr(StringBuilder sb) {
    <#list modelFieldMap?values as field>
        <#if field.strShow&&field.hasCriteriaRange>
        if (start${field.name?cap_first} != null) {
            sb.append("start${field.name?cap_first}=").append(start${field.name?cap_first}).append(",");
        }
        if (end${field.name?cap_first} != null) {
            sb.append("end${field.name?cap_first}=").append(end${field.name?cap_first}).append(",");
        }
        </#if>
    </#list>
    }
</#if>

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("${name}CriteriaStr{");
        if (${id.name} != null) {
            sb.append("${id.name}=").append(${id.name}).append(",");
        }
<#if version??>
        if (${version.name} != null) {
            sb.append("${version.name}=").append(${version.name}).append(",");
        }
</#if>
<#list modelFieldMap?values as field>
    <#if field.strShow>
        if (${field.name} != null) {
            sb.append("${field.name}=").append(${field.name}).append(",");
        }
    </#if>
</#list>
    }

    @Override
    protected void afterStr(StringBuilder sb) {
<#list modelFieldMap?values as field>
        <#if field.criteriaOrder>
        if (${field.name}Order != null) {
            sb.append("${field.name}Order=").append(${field.name}Order).append(",");
        }
        </#if>
</#list>
        super.afterStr(sb);
    }

<#assign field = id />
<#assign name >${name}CriteriaStr</#assign>
<#include "getsetStringNullStr.ftl">
<#if version??>
    <#assign field = version />
    <#include "getsetStringNullStr.ftl">
</#if>
<#list modelFieldMap?values as field>
    <#include "getsetStringNullStr.ftl">
</#list>
}