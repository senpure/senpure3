package com.senpure.base.generator;


import com.senpure.base.AppEvn;
import com.senpure.base.generator.method.*;
import com.senpure.base.util.Assert;
import com.senpure.base.util.StringUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CodeGenerator
 *
 * @author senpure
 * @time 2019-01-03 16:04:24
 */
public class CodeGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> exists = new ArrayList<>();
    private List<String> springLocal = new ArrayList<>();

    public void generate(Object object, Template template, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            template.process(object, new OutputStreamWriter(fos, "utf-8"));
            fos.close();

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (TemplateException e) {
            logger.error("TemplateException", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException ", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }

    private String getCache(ModelConfig config) {
        if (config.isCache()) {
            if (config.isRemoteCache()) {
                return "true:springCache";
            } else if (config.isLocalCache()) {
                return "true:springLocal";
            } else if (config.isLocalCache()) {
                return "true:localMap";
            }
        }
        return "false";
    }


    private void generateFile(Template template, Map<String, Object> args, File file, boolean cover) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            if (cover) {
                logger.debug("{}{}", AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "覆盖生成"}), file.getAbsolutePath());
                generate(args, template, file);
            } else {

                logger.warn("{}存在无法生成", file.getAbsolutePath());
            }
        } else {
            logger.debug("生成{}", file.getAbsolutePath());

            generate(args, template, file);
        }
    }

    private void generateFile(Template template, Model model, File file, boolean cover) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            if (cover) {
                if (model.isCurrentService()) {
                    logger.debug("{}{} useCache:{}", AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "覆盖生成"}), file.getAbsolutePath(), AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, getCache(model.getConfig())}));

                } else {
                    logger.debug("{}{}", AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "覆盖生成"}), file.getAbsolutePath());
                }
                generate(model, template, file);
            } else {
                exists.add(file.getAbsolutePath());
                if (model.isCurrentService()) {
                    springLocal.remove(model.getName());
                }
                logger.warn("{}存在无法生成", file.getAbsolutePath());
            }

        } else {
            if (model.isCurrentService()) {
                logger.debug("生成{} useCache:{}", file.getAbsolutePath(), AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, getCache(model.getConfig())}));
            } else {
                logger.debug("生成{}", file.getAbsolutePath());
            }
            generate(model, template, file);

        }
    }

    public void generate(String part) {
        generate(part, new GeneratorConfig());
    }

    public void generate(String part, GeneratorConfig config) {
        prepLog();
        int point = StringUtil.indexOf(part, ".", 1, true);
        String module = part;
        if (point > 0) {
            module = part.substring(point + 1);
        }
        EntityReader reader = new EntityReader(config);
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setSharedVariable("pluralize", new Pluralize());
        cfg.setSharedVariable("nameRule", new NameRule());
        cfg.setSharedVariable("space", new SpaceResidual());
        cfg.setSharedVariable("serial", new HashCode());
        cfg.setSharedVariable("labelFormat", new LabelFormat());
        cfg.setSharedVariable("mapperXmlTips", new MapperXmlTips());
        ApiModelProperty apiModelProperty = new ApiModelProperty();
        cfg.setSharedVariable("apiModelProperty", apiModelProperty);
        cfg.setClassForTemplateLoading(getClass(), "/");
        Template modelTemplate = null;
        Template serviceTemplate = null;
        Template serviceMapCacheTemplate = null;
        Template serviceSpringCacheTemplate = null;
        Template mapperJavaTemplate = null;
        Template mapperXmlTemplate = null;
        Template criteriaTemplate = null;
        Template criteriaTemplateStr = null;
        Template configurationTemplate = null;
        Template controllerTemplate = null;
        Template resultRecordTemplate = null;
        Template resultPageTemplate = null;
        try {
            modelTemplate = cfg.getTemplate(
                    config.getModelTemplate(),
                    "utf-8");
            serviceTemplate = cfg.getTemplate(
                    config.getServiceTemplate(),
                    "utf-8");
            serviceMapCacheTemplate = cfg.getTemplate(
                    config.getServiceMapCacheTemplate(),
                    "utf-8");
            serviceSpringCacheTemplate = cfg.getTemplate(
                    config.getServiceSpringCacheTemplate(),
                    "utf-8");
            mapperJavaTemplate = cfg.getTemplate(
                    config.getMapperJavaTemplate(),
                    "utf-8");
            mapperXmlTemplate = cfg.getTemplate(
                    config.getMapperXmlTemplate(),
                    "utf-8");
            criteriaTemplate = cfg.getTemplate(
                    config.getCriteriaTemplate(),
                    "utf-8");
            criteriaTemplateStr = cfg.getTemplate(
                    config.getCriteriaStrTemplate(),
                    "utf-8");
            configurationTemplate = cfg.getTemplate(
                    config.getConfigurationTemplate(),
                    "utf-8");
            resultRecordTemplate = cfg.getTemplate(
                    config.getResultRecordTemplate(),
                    "utf-8");

            resultPageTemplate = cfg.getTemplate(
                    config.getResultPageTemplate(),
                    "utf-8");
            controllerTemplate = cfg.getTemplate(
                    config.getControllerTemplate(),
                    "utf-8");

        } catch (IOException e) {

            logger.error("", e);
        }
        File file = new File(AppEvn.getClassRootPath());
        File project = file;
        for (int i = 0; i < config.getClassLevel(); i++) {
            project = project.getParentFile();
        }
        File javaPart = new File(project,
                config.getProjectJavaCodePath() + "/" + part.replace(".", "/"));
        Map<String, Model> modelMap = reader.read(part + "." + config.getEntityPartName());

        int menuId = config.getMenuStartId();
        if (menuId == 0) {
            menuId = module.hashCode() / 100 * 100;
        }
        for (Model model : modelMap.values()) {
            if (model.getId() == null) {
                Assert.error(model.getName() + "没有主键");
            }
            ModelConfig modelConfig = config.getModelConfig(model.getName());
            model.setGlobalConfig(config);
            model.setConfig(modelConfig);
            model.setMenuId(menuId);

            menuId += 100;
            model.setGenerateMenu(config.isGenerateMenu());
            model.setGeneratePermission(config.isGeneratePermission());
            model.setUseCriteriaStr(config.isUseCriteriaStr());
            model.setModule(module);

            model.setModelPackage(part + "." + config.getModelPartName());
            model.setMapperPackage(part + "." + config.getMapperPartName());
            model.setCriteriaPackage(part + "." + config.getCriteriaPartName());
            model.setServicePackage(part + "." + config.getServicePartName());
            model.setControllerPackage(part + "." + config.getControllerPartName());
            model.setModelPackage(part + "." + config.getModelPartName());
            model.setResultPackage(part + "." + config.getResultPartName());
            model.setTableType(modelConfig.getTableType());

            if (!modelConfig.getTableType().equalsIgnoreCase(config.TABLE_TYPE_SINGLE)) {
                ModelField modelField = new ModelField();
                modelField.setAccessType("private");
                modelField.setClazzType("String");
                modelField.setName("tableName");
                model.setTable(modelField);
            }
            if (modelConfig.isGenerateModel()) {
                int startPosition = apiModelProperty.getStartPosition();
                File modelFile = null;
                if (config.getModelRootPath() == null) {
                    modelFile = new File(javaPart, config.getModelPartName() + "/" + model.getName() + ".java");
                } else {
                    modelFile = new File(file(config.getModelRootPath(), project), config.getModelPartName() + "/" + model.getName() + ".java");
                }
                generateFile(modelTemplate, model, modelFile, modelConfig.isCoverModel());
                apiModelProperty.setStartPosition(startPosition);

            } else {
                logger.info("{} 不生成model", model.getName());
            }
            if (modelConfig.isGenerateMapperJava()) {
                File javaFile = new File(javaPart, config.getMapperPartName() + "/" + model.getName() + config.getMapperSuffix() + ".java");
                generateFile(mapperJavaTemplate, model, javaFile, modelConfig.isCoverMapperJava());
            } else {
                logger.info("{} 不生成mapperJava", model.getName());
            }
            if (modelConfig.isGenerateMapperJava()) {
                File xmlFile = new File(javaPart, config.getMapperPartName() + "/" + model.getName() + config.getMapperSuffix() + ".xml");
                generateFile(mapperXmlTemplate, model, xmlFile, modelConfig.isCoverMapperJava());
            } else {
                logger.info("{} 不生成mapperXml", model.getName());
            }

            if (modelConfig.isGenerateService()) {
                File serviceFile = new File(javaPart, config.getServicePartName() + "/" + model.getName() + config.getServiceSuffix() + ".java");
                Template template = null;
                if (modelConfig.isCache()) {
                    if (modelConfig.isRemoteCache()) {
                        template = serviceSpringCacheTemplate;
                    } else if (modelConfig.isLocalCache()) {
                        template = serviceSpringCacheTemplate;
                        springLocal.add(model.getName());
                    } else {
                        template = serviceMapCacheTemplate;
                    }
                } else {
                    template = serviceTemplate;
                }
                model.setCurrentService(true);
                generateFile(template, model, serviceFile, modelConfig.isCoverService());
                model.setCurrentService(false);
            } else {
                logger.info("{} 不生成service", model.getName());
            }
            if (modelConfig.isGenerateCriteria()) {
                File criteriaFile = null;
                if (config.getCriteriaRootPath() == null) {
                    criteriaFile = new File(javaPart, config.getCriteriaPartName() + "/" + model.getName() + config.getCriteriaSuffix() + ".java");
                } else {
                    criteriaFile = new File(file(config.getCriteriaRootPath(), project), config.getCriteriaPartName() + "/" + model.getName() + config.getCriteriaSuffix() + ".java");
                }

                generateFile(criteriaTemplate, model, criteriaFile, modelConfig.isCoverCriteria());
                if (modelConfig.isUseCriteriaStr()) {
                    criteriaFile = new File(javaPart, config.getCriteriaPartName() + "/" + model.getName() + config.getCriteriaStrSuffix() + ".java");
                    generateFile(criteriaTemplateStr, model, criteriaFile, modelConfig.isCoverCriteria());
                }
            } else {
                logger.info("{} 不生成criteria", model.getName());
            }
            if (modelConfig.isGenerateResult()) {
                File recordFile = null;
                File pageFile = null;
                if (config.getResultRootPath() == null) {
                    recordFile = new File(javaPart, config.getResultPartName() + "/" + model.getName() + config.getResultRecordSuffix() + ".java");
                    pageFile = new File(javaPart, config.getResultPartName() + "/" + model.getName() + config.getResultPageSuffix() + ".java");

                }
                else {
                    File p = file(config.getResultRootPath(), project);
                    recordFile = new File(p, config.getResultPartName() + "/" + model.getName() + config.getResultRecordSuffix() + ".java");
                    pageFile = new File(p, config.getResultPartName() + "/" + model.getName() + config.getResultPageSuffix() + ".java");

                }
                generateFile(resultRecordTemplate, model, recordFile, modelConfig.isCoverResult());
                generateFile(resultPageTemplate, model, pageFile, modelConfig.isCoverResult());

            } else {
                logger.info("{} 不生成Result", model.getName());
            }

            if (modelConfig.isGenerateController()) {
                File controllerFile = new File(javaPart, config.getControllerPartName() + "/" + model.getName() + config.getControllerSuffix() + ".java");
                generateFile(controllerTemplate, model, controllerFile, modelConfig.isCoverController());

            } else {
                logger.info("{} 不生成Controller", model.getName());
            }
        }
        generateSpringCacheConfiguration(part, javaPart, config, configurationTemplate);
        if (exists.size() > 0) {
            logger.warn(AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "↓↓↓↓↓↓↓↓↓↓以下文件存在没有生成↓↓↓↓↓↓↓↓↓↓"}));
            for (String name : exists) {
                logger.warn(AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_CYAN, name}));
            }
            logger.warn(AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "↑↑↑↑↑↑↑↑↑↑以上文件存在没有生成↑↑↑↑↑↑↑↑↑↑"}));
        }
    }

    private void generateSpringCacheConfiguration(String part, File javaPart, GeneratorConfig config, Template template) {
        if (springLocal.size() > 0) {
            logger.debug("springLocal = {}", springLocal);
        }

        String configName = "LocalCacheConfiguration";
        if ("com.senpure.base".equals(part)) {
            configName = "LocalCacheConfiguration";
        } else {
            int index = StringUtil.indexOf(part, ".", 1, true);
            String tempPart;
            if (index > 0) {
                tempPart = part.substring(index + 1);
            } else {
                tempPart = part;
            }
            configName = StringUtil.toUpperFirstLetter(tempPart) + "LocalCache" + config.getConfigurationSuffix();
        }
        File configFile = new File(javaPart, config.getConfigurationPartName() + "/" + configName + ".java");
        if (springLocal.size() > 0) {
            Class clazz = null;
            try {
                clazz = Class.forName("org.springframework.data.redis.core.RedisTemplate");
            } catch (ClassNotFoundException e) {
                logger.debug("没有找到{} 不用生成redis的本地缓存配置", "org.springframework.data.redis.core.RedisTemplate");
            }
            if (clazz != null) {
                Map<String, Object> args = new HashMap<>(16);
                args.put("configName", configName);
                args.put("names", springLocal);
                args.put("package", part + ".configuration");

                if (template != null) {
                    generateFile(template, args, configFile, true);
                }
            }
        } else {

            if (configFile.exists()) {
                logger.info("{} {}", AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "删除"}), configFile.getAbsolutePath());
                configFile.delete();
            }

        }
    }

    private void prepLog() {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        AppEvn.tryMarkClassRootPath();

    }

    public static File file(String path, File project) {
        File file;
        path = path.replace("\\", "/");
        String upperPath = path.toUpperCase();
        if (path.startsWith("/") || upperPath.startsWith("C:") || upperPath.startsWith("D:") || upperPath.startsWith("E:")
                || upperPath.startsWith("F:") || upperPath.startsWith("G:") || upperPath.startsWith("H:")
                || upperPath.startsWith("H:") || upperPath.startsWith("I:") || upperPath.startsWith("G:")
        ) {
            file = new File(path);
        } else if (path.startsWith("../")) {
            int count = StringUtil.count(path, "../");
            File parent = project;
            for (int i = 0; i < count; i++) {
                parent = parent.getParentFile();
            }
            file = new File(parent, path.replace("../", ""));
        } else {
            file = new File(path, path);
        }
        return file;
    }

    public static void main(String[] args) {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generate("com.senpure.demo");
    }

}
