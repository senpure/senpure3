<#if field.hasExplain>
    /**
     * <#if field.clazzType="boolean">is<#else>get</#if> ${field.explain}
     *
     * @return
     */
</#if>
    public ${field.clazzType} <#if field.clazzType="boolean">is<#else>get</#if>${field.name?cap_first}() {
        return ${field.name};
    }

<#if field.hasExplain>
    /**
     * set ${field.explain}
     *
     * @return
     */</#if>
    public ${name} set${field.name?cap_first}(${field.clazzType} ${field.name}) {
<#if field.clazzType=='String'>
        if (${field.name} != null && ${field.name}.trim().length() == 0) {
            this.${field.name} = null;
            return this;
        }
</#if>
        this.${field.name} = ${field.name};
        return this;
    }

<#if field.hasCriteriaRange>
    public ${field.clazzType} getStart${field.name?cap_first}() {
        return start${field.name?cap_first};
    }

    public ${name} setStart${field.name?cap_first}(${field.clazzType} start${field.name?cap_first}) {
    <#if field.clazzType=='String'>
        if (start${field.name?cap_first} != null && start${field.name?cap_first}.trim().length() == 0) {
            this.start${field.name?cap_first} = null;
            return this;
        }
    </#if>
        this.start${field.name?cap_first} = start${field.name?cap_first};
        return this;
    }

    public ${field.clazzType} getEnd${field.name?cap_first}() {
        return end${field.name?cap_first};
    }

    public ${name} setEnd${field.name?cap_first}(${field.clazzType} end${field.name?cap_first}) {
    <#if field.clazzType=='String'>
        if (end${field.name?cap_first} != null && end${field.name?cap_first}.trim().length() == 0) {
            this.end${field.name?cap_first} = null;
            return this;
        }
    </#if>
        this.end${field.name?cap_first} = end${field.name?cap_first};
        return this;
    }

</#if>
<#if field.criteriaOrder>
    /**
     * get table [${tableName}][column = ${field.column}] criteriaOrder
     *
     * @return
     */
    public String get${field.name?cap_first}Order() {
        return ${field.name}Order;
    }

    /**
     * set table [${tableName}][column = ${field.column}] criteriaOrder DESC||ASC
     *
     * @return
     */
    public ${name} set${field.name?cap_first}Order(String ${field.name}Order) {
        this.${field.name}Order = ${field.name}Order;
    <#if field.longDate??>
        putSort("${field.longDate.column}", ${field.name}Order);
    <#else >
        putSort("${field.column}", ${field.name}Order);
    </#if>
        return this;
    }

</#if>
