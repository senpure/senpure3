package com.senpure.base.generator;

/**
 * ModelConfig
 *
 * @author senpure
 * @time 2019-01-04 15:44:13
 */
public class ModelConfig {


    private boolean generateModel = true;
    //生成mapper
    private boolean generateMapper = true;
    //生成service
    private boolean generateService = true;
    //生成controller
    private boolean generateController = true;
    //生成权限
    private boolean generatePermission = true;
    //生成菜单
    private boolean generateMenu = true;

    private boolean generateCriteria = true;
    private boolean useCriteriaStr = true;

    //覆盖model
    private boolean coverModel = false;
    //覆盖mapper
    private boolean coverMapper = false;
    //覆盖service
    private boolean coverService = false;
    //覆盖controller
    private boolean coverController = false;
    //覆盖criteria
    private boolean coverCriteria = false;

    //表类型 mix single
    private String tableType = GeneratorConfig.TABLE_TYPE_SINGLE;

    public boolean isGenerateMapper() {
        return generateMapper;
    }

    public void setGenerateMapper(boolean generateMapper) {
        this.generateMapper = generateMapper;
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

    public boolean isGeneratePermission() {
        return generatePermission;
    }

    public void setGeneratePermission(boolean generatePermission) {
        this.generatePermission = generatePermission;
    }

    public boolean isGenerateMenu() {
        return generateMenu;
    }

    public void setGenerateMenu(boolean generateMenu) {
        this.generateMenu = generateMenu;
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

    public boolean isCoverMapper() {
        return coverMapper;
    }

    public void setCoverMapper(boolean coverMapper) {
        this.coverMapper = coverMapper;
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
}
