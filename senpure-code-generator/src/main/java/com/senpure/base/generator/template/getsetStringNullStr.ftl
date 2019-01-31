<#if field.strShow>
    <#if field.hasExplain>
    /**
     * get ${field.explain}
     *
     * @return
     */
    </#if>
    public String get${field.name?cap_first}() {
        return ${field.name};
    }

    <#if field.hasExplain>
    /**
     * set ${field.explain}
     *
     * @return
     */</#if>
    public ${name} set${field.name?cap_first}(String ${field.name}) {
        if (${field.name} != null && ${field.name}.trim().length() == 0) {
            return this;
        }
        this.${field.name} = ${field.name};
        return this;
    }

    <#if field.date>
    public String get${field.name?cap_first}Pattern() {
        return ${field.name}Pattern;
    }

    public ${name} set${field.name?cap_first}Pattern(String ${field.name}Pattern) {
        if (${field.name}Pattern != null && ${field.name}Pattern.trim().length() == 0) {
            return this;
        }
        this.${field.name}Pattern = ${field.name}Pattern;
        <#if field.hasCriteriaRange>
        this.start${field.name?cap_first}Valid.setPattern(${field.name}Pattern);
        this.end${field.name?cap_first}Valid.setPattern(${field.name}Pattern);
        <#else >
        this.${field.name}Valid.setPattern(${field.name}Pattern);
        </#if>
        return this;
    }

    </#if><#--时间类型-->
    <#if field.hasCriteriaRange><#--start end get set -->
        <#if field.hasExplain>
    /**
     * get start ${field.explain}
     *
     * @return
     */
        </#if>
    public String getStart${field.name?cap_first}() {
        return start${field.name?cap_first};
    }

        <#if field.hasExplain>
    /**
     * set start ${field.explain}
     *
     * @return
     */
        </#if>
    public ${name} setStart${field.name?cap_first}(String start${field.name?cap_first}) {
        if (start${field.name?cap_first} != null && start${field.name?cap_first}.trim().length() == 0) {
            return this;
        }
        this.start${field.name?cap_first} = start${field.name?cap_first};
        <#if field.clazzType=="Date">
        this.start${field.name?cap_first}Valid.setDateStr(start${field.name?cap_first});
        </#if>
        return this;
    }

        <#if field.hasExplain>
    /**
     * get end ${field.explain}
     *
     * @return
     */
        </#if>
    public String getEnd${field.name?cap_first}() {
        return end${field.name?cap_first};
    }

        <#if field.hasExplain>
    /**
     * set end ${field.explain}
     *
     * @return
     */
        </#if>
    public ${name} setEnd${field.name?cap_first}(String end${field.name?cap_first}) {
        if (end${field.name?cap_first} != null && end${field.name?cap_first}.trim().length() == 0) {
            return this;
        }
        this.end${field.name?cap_first} = end${field.name?cap_first};
        <#if field.clazzType=="Date">
        this.end${field.name?cap_first}Valid.setDateStr(end${field.name?cap_first});
        </#if>
        return this;
    }

    </#if><#--start end get set  结束-->
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
        if (${field.name}Order != null && ${field.name}Order.trim().length() == 0) {
            this.${field.name}Order = null;
            return this;
        }
        this.${field.name}Order = ${field.name}Order;
        return this;
    }

    </#if>
</#if>