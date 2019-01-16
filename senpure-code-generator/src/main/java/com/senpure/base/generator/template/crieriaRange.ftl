<#list criteriaFieldMap?values as field>
    <#if field.strShow&&field.hasCriteriaRange>
        <#if field.clazzType ="Date">
            <#if field.longDate??>
            <if test="start${field.name?cap_first} != null">
                and ${field.longDate.column} >= ${r'#{'}start${field.name?cap_first}.time}
            </if>
            <if test="end${field.name?cap_first} != null">
                and ${field.longDate.column} &lt;= ${r'#{'}end${field.name?cap_first}.time}
            </if>
            <#else>
            <if test="start${field.name?cap_first} != null">
                and ${field.column} >= ${r'#{'}start${field.name?cap_first}}
            </if>
            <if test="end${field.name?cap_first} != null">
                and ${field.column} &lt;= ${r'#{'}end${field.name?cap_first}}
            </if>
            </#if>
        <#else><#--date-->
            <if test="start${field.name?cap_first} != null">
                and ${field.column} >= ${r'#{'}start${field.name?cap_first}}
            </if>
            <if test="end${field.name?cap_first} != null">
                and ${field.column} &lt;= ${r'#{'}end${field.name?cap_first}}
            </if>
        </#if>
    </#if>
</#list>