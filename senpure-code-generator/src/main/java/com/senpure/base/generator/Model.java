package com.senpure.base.generator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Model {
    /**
     * 是否有注释
     */
    private boolean hasExplain;
    /**
     * 注释
     */
    private String explain;

    /**
     * 是否有data类型字段 只有java.untl.Date
     */
    private boolean hasDate;
    /**
     * 是否还有long类型的日期
     */
    private boolean hasLongDate;

    private boolean hasRange;
    /**
     * 数据库表明
     */
    private String tableName;
    private String entityPackage;
    private String modelPackage;
    private String mapperPackage;
    private String criteriaPackage;
    private String servicePackage;
    private String controllerPackage;
    private String resultPackage;
    /**
     * 模块
     */
    private String module;
    /**
     * 类名
     */
    private String name;

    private String tableType;

    /**
     * 生成菜单的id
     */
    private int menuId = 0;

    private boolean generatePermission;
    private boolean generateMenu;
    private boolean useCriteriaStr = true;
    private boolean currentService;


    //class 和super class
    private List<String> clazzs = new ArrayList<>();
    /**
     * class
     */
    private Class clazz;
    private Map<String, ModelField> modelFieldMap = new LinkedHashMap<>();
    //外键放在前面
    private Map<String, ModelField> criteriaFieldMap = new LinkedHashMap<>();

    AfterReadColumn afterReadColumn = new AfterReadColumn();
    /**
     * 主键字段
     */
    private ModelField id;

    private ModelField version;

    private Map<String, ModelField> dateFieldMap = new LinkedHashMap<>();
    /**
     * 混合表名
     */
    private ModelField table;
    private List<ModelField> findModeFields = new ArrayList<>();

    private Model child;
    private ModelField childField;


    private List<ModelField> allFields = new ArrayList<>();

    private ModelConfig config;
    private GeneratorConfig globalConfig;


    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ModelField> getModelFieldMap() {
        return modelFieldMap;
    }

    public void setModelFieldMap(Map<String, ModelField> modelFieldMap) {
        this.modelFieldMap = modelFieldMap;
    }


    public ModelField getId() {
        return id;
    }

    public void setId(ModelField id) {
        this.id = id;
    }


    public ModelField getVersion() {
        return version;
    }

    public void setVersion(ModelField version) {
        this.version = version;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public List<String> getClazzs() {
        return clazzs;
    }

    public void setClazzs(List<String> clazzs) {
        this.clazzs = clazzs;
    }

    public boolean isHasExplain() {
        return hasExplain;
    }


    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
        hasExplain = true;
    }

    public boolean isCurrentService() {
        return currentService;
    }

    public void setCurrentService(boolean currentService) {
        this.currentService = currentService;
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCriteriaPackage() {
        return criteriaPackage;
    }

    public void setCriteriaPackage(String criteriaPackage) {
        this.criteriaPackage = criteriaPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }


    public ModelField getTable() {
        return table;
    }

    public void setTable(ModelField table) {
        this.table = table;
    }


    public AfterReadColumn getAfterReadColumn() {
        return afterReadColumn;
    }

    public void setAfterReadColumn(AfterReadColumn afterReadColumn) {
        this.afterReadColumn = afterReadColumn;
    }

    private void checkAllFields() {
        if (allFields.size() == 0) {
            allFields.clear();
            if (id != null) {
                allFields.add(id);
            }
            if (version != null) {
                allFields.add(version);
            }
            allFields.addAll(modelFieldMap.values());
        }
    }

    public int getXmlMaxLen() {
        checkAllFields();
        int maxLen = 0;
        for (ModelField modelField : allFields) {
            int len = modelField.getXmlLen();
            maxLen = len > maxLen ? len : maxLen;
        }
        return maxLen + 1;
    }

    public int getColumnMaxLen() {
        checkAllFields();
        int maxLen = 0;
        for (ModelField modelField : allFields) {
            int len = modelField.getColumnLen();
            maxLen = len > maxLen ? len : maxLen;
        }
        return maxLen + 1;
    }

    public int getNameMaxLen() {
        checkAllFields();
        int maxLen = 0;
        for (ModelField modelField : allFields) {
            int len = modelField.getNameLen();
            maxLen = len > maxLen ? len : maxLen;
        }
        return maxLen + 1;
    }

    public int getJdbcMaxLen() {
        checkAllFields();
        int maxLen = 0;
        for (ModelField modelField : allFields) {
            int len = modelField.getJdbcLen();
            maxLen = len > maxLen ? len : maxLen;
        }
        return maxLen + 1;
    }

    public Map<String, ModelField> getCriteriaFieldMap() {
        return criteriaFieldMap;
    }

    public void setCriteriaFieldMap(Map<String, ModelField> criteriaFieldMap) {
        this.criteriaFieldMap = criteriaFieldMap;
    }

    public List<ModelField> getFindModeFields() {
        return findModeFields;
    }

    public void setFindModeFields(List<ModelField> findModeFields) {
        this.findModeFields = findModeFields;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public Model getChild() {
        return child;
    }

    public void setChild(Model child) {
        this.child = child;
    }

    public ModelField getChildField() {
        return childField;
    }

    public void setChildField(ModelField childField) {
        this.childField = childField;
    }

    public boolean isUseCriteriaStr() {
        return useCriteriaStr;
    }

    public void setUseCriteriaStr(boolean useCriteriaStr) {
        this.useCriteriaStr = useCriteriaStr;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }


    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Map<String, ModelField> getDateFieldMap() {
        return dateFieldMap;
    }

    public void setDateFieldMap(Map<String, ModelField> dateFieldMap) {
        this.dateFieldMap = dateFieldMap;
    }

    public boolean isHasLongDate() {
        return hasLongDate;
    }

    public void setHasLongDate(boolean hasLongDate) {
        this.hasLongDate = hasLongDate;
    }

    public boolean isHasRange() {
        return hasRange;
    }

    public void setHasRange(boolean hasRange) {
        this.hasRange = hasRange;
    }


    public String getResultPackage() {
        return resultPackage;
    }

    public void setResultPackage(String resultPackage) {
        this.resultPackage = resultPackage;
    }

    public ModelConfig getConfig() {
        return config;
    }

    public void setConfig(ModelConfig config) {
        this.config = config;
    }


    public GeneratorConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GeneratorConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public boolean isGeneratePermission() {
        return generatePermission;
    }

    public Model setGeneratePermission(boolean generatePermission) {
        this.generatePermission = generatePermission;
        return this;
    }

    public boolean isGenerateMenu() {
        return generateMenu;
    }

    public Model setGenerateMenu(boolean generateMenu) {
        this.generateMenu = generateMenu;
        return this;
    }

    @Override
    public String toString() {
        return "Model{" +
                "hasDate=" + hasDate +
                ", tableName='" + tableName + '\'' +
                '}';
    }

}
