package com.senpure.base.generator;

import com.senpure.base.generator.naming.NamingStrategy;
import org.hibernate.dialect.Dialect;

import java.util.HashMap;
import java.util.Map;

/**
 * CodeConfig
 *
 * @author senpure
 * @time 2019-01-03 10:12:33
 */
public class GeneratorConfig {

    public static final String TABLE_TYPE_MIX = "MIX";
    public static final String TABLE_TYPE_SINGLE = "SINGLE";
    /**
     * 项目下的java代码根路径
     */
    private String projectJavaCodePath = "src\\main\\java";
    /**
     * class 与项目相隔几个路径
     */
    private int classLevel = 2;
    private String class2SourceClass = "target\\classes";
    private String class2SourceSource = "src\\main\\java";

    private String modelTemplate = "com/senpure/base/generator/template/model.ftl";
    private String serviceTemplate = "com/senpure/base/generator/template/service.ftl";
    private String serviceMapCacheTemplate = "com/senpure/base/generator/template/serviceMapCache.ftl";

    private String serviceSpringCacheTemplate = "com/senpure/base/generator/template/serviceSpringCache.ftl";

    private String mapperJavaTemplate = "com/senpure/base/generator/template/mapperJava.ftl";
    private String mapperXmlTemplate = "com/senpure/base/generator/template/mapperXml.ftl";
    private String controllerXmlTemplate = "com/senpure/base/generator/template/controller.ftl";
    private String criteriaTemplate = "com/senpure/base/generator/template/criteria.ftl";
    private String criteriaStrTemplate = "com/senpure/base/generator/template/criteriaStr.ftl";
    private String configurationTemplate = "com/senpure/base/generator/template/LocalRemoteConfiguration.ftl";

    private String entityPartName = "entity";
    private String modelPartName = "model";
    private String servicePartName = "service";
    private String mapperPartName = "mapper";
    private String controllerPartName = "controller";
    private String criteriaPartName = "criteria";
    private String configurationPartName = "configuration";

    private String configurationSuffix="Configuration";
    private String mapperSuffix="Mapper";
    private String serviceSuffix="Service";
    private String criteriaSuffix="Criteria";
    private String criteriaStrSuffix="CriteriaStr";


    /**
     * model 生成根路径
     */
    private String modelRootPath;
    /**
     * 条件生成 根路径
     */
    private String criteriaRootPath;

    //命名策略
    private NamingStrategy namingStrategy;
    //数据库方言
    private Dialect dialect;

    //生成页面视图


    private ModelConfig defaultModelConfig = new ModelConfig();
    private Map<String, ModelConfig> modelConfigMap = new HashMap<>();


    public void setDefaultModelConfig(ModelConfig defaultModelConfig) {
        this.defaultModelConfig = defaultModelConfig;
    }

    public void setModelConfig(String modelName, ModelConfig modelConfig) {
        modelConfigMap.put(modelName, modelConfig);
    }

    public ModelConfig getModelConfig(String modelName) {

        ModelConfig modelConfig = modelConfigMap.get(modelName);
        if (modelConfig == null) {
            return defaultModelConfig;
        }
        return modelConfig;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public String getClass2SourceClass() {
        return class2SourceClass;
    }

    public void setClass2SourceClass(String class2SourceClass) {
        this.class2SourceClass = class2SourceClass;
    }

    public String getClass2SourceSource() {
        return class2SourceSource;
    }

    public void setClass2SourceSource(String class2SourceSource) {
        this.class2SourceSource = class2SourceSource;
    }

    public String getConfigurationTemplate() {
        return configurationTemplate;
    }

    public void setConfigurationTemplate(String configurationTemplate) {
        this.configurationTemplate = configurationTemplate;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public String getModelTemplate() {
        return modelTemplate;
    }

    public void setModelTemplate(String modelTemplate) {
        this.modelTemplate = modelTemplate;
    }

    public String getServiceTemplate() {
        return serviceTemplate;
    }

    public void setServiceTemplate(String serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }

    public String getMapperJavaTemplate() {
        return mapperJavaTemplate;
    }

    public void setMapperJavaTemplate(String mapperJavaTemplate) {
        this.mapperJavaTemplate = mapperJavaTemplate;
    }

    public String getMapperXmlTemplate() {
        return mapperXmlTemplate;
    }

    public void setMapperXmlTemplate(String mapperXmlTemplate) {
        this.mapperXmlTemplate = mapperXmlTemplate;
    }

    public String getControllerXmlTemplate() {
        return controllerXmlTemplate;
    }

    public void setControllerXmlTemplate(String controllerXmlTemplate) {
        this.controllerXmlTemplate = controllerXmlTemplate;
    }

    public String getServiceSuffix() {
        return serviceSuffix;
    }

    public String getConfigurationPartName() {
        return configurationPartName;
    }

    public void setConfigurationPartName(String configurationPartName) {
        this.configurationPartName = configurationPartName;
    }

    public String getConfigurationSuffix() {
        return configurationSuffix;
    }

    public void setConfigurationSuffix(String configurationSuffix) {
        this.configurationSuffix = configurationSuffix;
    }

    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public String getCriteriaTemplate() {
        return criteriaTemplate;
    }

    public void setCriteriaTemplate(String criteriaTemplate) {
        this.criteriaTemplate = criteriaTemplate;
    }

    public String getCriteriaStrTemplate() {
        return criteriaStrTemplate;
    }

    public void setCriteriaStrTemplate(String criteriaStrTemplate) {
        this.criteriaStrTemplate = criteriaStrTemplate;
    }

    public String getProjectJavaCodePath() {
        return projectJavaCodePath;
    }

    public void setProjectJavaCodePath(String projectJavaCodePath) {
        this.projectJavaCodePath = projectJavaCodePath;
    }

    public String getModelRootPath() {
        return modelRootPath;
    }

    public void setModelRootPath(String modelRootPath) {
        this.modelRootPath = modelRootPath;
    }

    public String getCriteriaRootPath() {
        return criteriaRootPath;
    }

    public void setCriteriaRootPath(String criteriaRootPath) {
        this.criteriaRootPath = criteriaRootPath;
    }


    public int getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(int classLevel) {
        this.classLevel = classLevel;
    }

    public String getModelPartName() {
        return modelPartName;
    }

    public void setModelPartName(String modelPartName) {
        this.modelPartName = modelPartName;
    }

    public String getServicePartName() {
        return servicePartName;
    }

    public void setServicePartName(String servicePartName) {
        this.servicePartName = servicePartName;
    }

    public String getMapperPartName() {
        return mapperPartName;
    }

    public void setMapperPartName(String mapperPartName) {
        this.mapperPartName = mapperPartName;
    }

    public String getControllerPartName() {
        return controllerPartName;
    }

    public void setControllerPartName(String controllerPartName) {
        this.controllerPartName = controllerPartName;
    }

    public String getCriteriaPartName() {
        return criteriaPartName;
    }

    public void setCriteriaPartName(String criteriaPartName) {
        this.criteriaPartName = criteriaPartName;
    }

    public String getEntityPartName() {
        return entityPartName;
    }

    public void setEntityPartName(String entityPartName) {
        this.entityPartName = entityPartName;
    }

    public String getMapperSuffix() {
        return mapperSuffix;
    }

    public void setMapperSuffix(String mapperSuffix) {
        this.mapperSuffix = mapperSuffix;
    }

    public String getCriteriaSuffix() {
        return criteriaSuffix;
    }

    public void setCriteriaSuffix(String criteriaSuffix) {
        this.criteriaSuffix = criteriaSuffix;
    }

    public String getCriteriaStrSuffix() {
        return criteriaStrSuffix;
    }

    public void setCriteriaStrSuffix(String criteriaStrSuffix) {
        this.criteriaStrSuffix = criteriaStrSuffix;
    }


    public String getServiceMapCacheTemplate() {
        return serviceMapCacheTemplate;
    }

    public void setServiceMapCacheTemplate(String serviceMapCacheTemplate) {
        this.serviceMapCacheTemplate = serviceMapCacheTemplate;
    }

    public String getServiceSpringCacheTemplate() {
        return serviceSpringCacheTemplate;
    }

    public void setServiceSpringCacheTemplate(String serviceSpringCacheTemplate) {
        this.serviceSpringCacheTemplate = serviceSpringCacheTemplate;
    }
}
