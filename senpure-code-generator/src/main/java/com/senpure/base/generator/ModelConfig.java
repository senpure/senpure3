package com.senpure.base.generator;

/**
 * ModelConfig
 *
 * @author senpure
 * @time 2019-01-04 15:44:13
 */
public class ModelConfig {


    public static ModelConfig getCoverAllInstance() {
        ModelConfig config = new ModelConfig();
        config.setCoverModel(true);
        config.setCoverService(true);
        config.setCoverCriteria(true);
        config.setCoverController(true);
        config.setCoverMapperJava(true);
        config.setCoverMapperXml(true);
        config.setCoverResult(true);
        return config;
    }

    private boolean generateModel = true;
    //生成mapper
    private boolean generateMapperJava = true;
    private boolean generateMapperXml = true;
    //生成service
    private boolean generateService = false;
    //生成controller
    private boolean generateController = false;


    private boolean generateCriteria = true;
    private boolean generateResult= true;



    private boolean useCriteriaStr = true;

    //覆盖model
    private boolean coverModel = false;
    //覆盖mapper
    private boolean coverMapperJava = false;
    private boolean coverMapperXml = false;
    //覆盖service
    private boolean coverService = false;
    //覆盖controller
    private boolean coverController = false;
    //覆盖criteria
    private boolean coverCriteria = false;
    //覆盖result
    private boolean coverResult= false;

    //表类型 mix single
    private String tableType = GeneratorConfig.TABLE_TYPE_SINGLE;
    //service 开启缓存
    private boolean cache=false;
    //cache =true 时生效 spring cache
    private boolean remoteCache=true;
    //cache =true 时生效 spring cache local混合
    private boolean localCache;
    //cache =true 时生效 本地map缓存
    private boolean mapCache;


    public boolean isGenerateMapperJava() {
        return generateMapperJava;
    }

    public void setGenerateMapperJava(boolean generateMapperJava) {
        this.generateMapperJava = generateMapperJava;
    }

    public boolean isGenerateMapperXml() {
        return generateMapperXml;
    }

    public void setGenerateMapperXml(boolean generateMapperXml) {
        this.generateMapperXml = generateMapperXml;
    }

    public boolean isCoverMapperJava() {
        return coverMapperJava;
    }

    public void setCoverMapperJava(boolean coverMapperJava) {
        this.coverMapperJava = coverMapperJava;
    }

    public boolean isCoverMapperXml() {
        return coverMapperXml;
    }

    public void setCoverMapperXml(boolean coverMapperXml) {
        this.coverMapperXml = coverMapperXml;
    }

    public boolean isGenerateService() {
        return generateService;
    }

    public void setGenerateService(boolean generateService) {
        this.generateService = generateService;
    }

    public boolean isGenerateController() {
        return generateController;
    }

    public void setGenerateController(boolean generateController) {
        this.generateController = generateController;
    }



    public boolean isGenerateModel() {
        return generateModel;
    }

    public void setGenerateModel(boolean generateModel) {
        this.generateModel = generateModel;
    }

    public boolean isCoverModel() {
        return coverModel;
    }

    public void setCoverModel(boolean coverModel) {
        this.coverModel = coverModel;
    }


    public boolean isCoverService() {
        return coverService;
    }

    public void setCoverService(boolean coverService) {
        this.coverService = coverService;
    }

    public boolean isCoverController() {
        return coverController;
    }

    public void setCoverController(boolean coverController) {
        this.coverController = coverController;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public boolean isGenerateCriteria() {
        return generateCriteria;
    }

    public boolean isCoverCriteria() {
        return coverCriteria;
    }

    public void setCoverCriteria(boolean coverCriteria) {
        this.coverCriteria = coverCriteria;
    }

    public void setGenerateCriteria(boolean generateCriteria) {
        this.generateCriteria = generateCriteria;
    }

    public boolean isUseCriteriaStr() {
        return useCriteriaStr;
    }

    public void setUseCriteriaStr(boolean useCriteriaStr) {
        this.useCriteriaStr = useCriteriaStr;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isRemoteCache() {
        return remoteCache;
    }

    public void setRemoteCache(boolean remoteCache) {
        this.remoteCache = remoteCache;
    }

    public boolean isLocalCache() {
        return localCache;
    }

    public void setLocalCache(boolean localCache) {
        this.localCache = localCache;
    }

    public boolean isMapCache() {
        return mapCache;
    }

    public void setMapCache(boolean mapCache) {
        this.mapCache = mapCache;
    }

    public boolean isGenerateResult() {
        return generateResult;
    }

    public void setGenerateResult(boolean generateResult) {
        this.generateResult = generateResult;
    }

    public boolean isCoverResult() {
        return coverResult;
    }

    public void setCoverResult(boolean coverResult) {
        this.coverResult = coverResult;
    }
}
