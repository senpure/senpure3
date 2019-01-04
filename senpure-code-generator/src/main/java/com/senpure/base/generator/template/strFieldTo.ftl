<#if field.strShow>
    <#if field.hasExplain>
        //${field.explain}
    </#if>
        if (${field.name} != null) {
<#if field.javaNullable>
    <#if field.clazzType="String">
            criteria.set${field.name?cap_first}(${field.name});
    <#elseif field.clazzType="Date">

    <#if field.criteriaEquals>
         criteria.set${field.name?cap_first}(${field.name}Valid.getDate());
        <#else >
        criteria.setStart${field.name?cap_first}(start${field.name?cap_first}Valid.getDate());
        criteria.setEnd${field.name?cap_first}(end${field.name?cap_first}Valid.getDate());
    </#if>
        <#if field.longDate??>
            <#if field.criteriaEquals>
             if (${field.name}Valid.getDate() != null) {
                criteria.set${field.longDate.name?cap_first}(${field.name}Valid.getDate().getTime());
             }
            <#else >
             if (start${field.name?cap_first}Valid.getDate() != null) {
                criteria.setStart${field.longDate.name?cap_first}(start${field.name?cap_first}Valid.getDate().getTime());
             }
              if (end${field.name?cap_first}Valid.getDate() != null) {
                criteria.setEnd${field.longDate.name?cap_first}(end${field.name?cap_first}Valid.getDate().getTime());
             }
            </#if>

        </#if>
    <#else >
            criteria.set${field.name?cap_first}(${field.clazzType}.valueOf(${field.name}));
    </#if>
<#else>
        criteria.set${field.name?cap_first}(${.globals[field.clazzType]!field.clazzType?cap_first}.parse${field.clazzType?cap_first}(${field.name}));
</#if>
        }
</#if>
<#if field.order&&field.htmlShow>
        //table [${tableName}][column = ${field.column}] order
        if (${field.name}Order != null) {
            criteria.set${field.name?cap_first}Order(${field.name}Order);
        }
</#if>
