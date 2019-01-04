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
import java.util.Map;

/**
 * CodeGenerator
 *
 * @author senpure
 * @time 2019-01-03 16:04:24
 */
public class CodeGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());


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

    private void generateFile(Template template, Model model, File file, boolean cover) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            if (cover) {
                if (file.getName().endsWith("Service.java")) {
                    // logger.debug("{}{} useCache:{}", AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "覆盖生成"}), file.getAbsolutePath(), AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, getCache((Model) model)}));

                } else {
                    logger.debug("{}{}", AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, "覆盖生成"}), file.getAbsolutePath());
                }
                generate(model, template, file);
            } else {
//                exists.add(file.getAbsolutePath());
//                if (file.getName().endsWith("Service.java") && model instanceof Model) {
//
//                    springLocal.remove(((Model) model).getName());
//                }
//                logger.warn("{}存在无法生成", file.getAbsolutePath());
            }

        } else {
            if (file.getName().endsWith("Service.java")) {
                //  logger.debug("生成{} useCache:{}", file.getAbsolutePath(), AnsiOutput.toString(new Object[]{AnsiColor.BRIGHT_RED, getCache((Model) model)}));
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

        cfg.setClassForTemplateLoading(getClass(), "/");
        Template modelTemplate = null;
        Template serviceTemplate = null;
        Template mapperJavaTemplate = null;
        Template mapperXmlTemplate = null;
        Template criteriaTemplate = null;
        Template criteriaTemplateStr = null;
        try {
            modelTemplate = cfg.getTemplate(
                    config.getModelTemplate(),
                    "utf-8");
            serviceTemplate = cfg.getTemplate(
                    config.getServiceTemplate(),
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

        } catch (IOException e) {

            logger.error("", e);
        }
        File file = new File(AppEvn.getClassRootPath());
        File parent = file;
        for (int i = 0; i < config.getClassLevel(); i++) {
            parent = parent.getParentFile();
        }
        File javaPart = new File(parent,
                config.getProjectJavaCodePath() + "/" + part.replace(".", "/"));
        Map<String, Model> modelMap = reader.read(part + "." + config.getEntityPartName());
        for (Model model : modelMap.values()) {
            if (model.getId() == null) {
                Assert.error(model.getName() + "没有主键");
            }
            ModelConfig modelConfig = config.getModelConfig(model.getName());
            model.setModelPackage(part + "." + config.getModelPartName());
            model.setMapperPackage(part + "." + config.getMapperPartName());
            model.setCriteriaPackage(part + "." + config.getCriteriaPartName());
            model.setServicePackage(part + "." + config.getServicePartName());
            model.setControllerPackage(part + "." + config.getControllerPartName());
            model.setModelPackage(part + "." + config.getModelPartName());
            model.setTableType(modelConfig.getTableType());

            if (!modelConfig.getTableType().equalsIgnoreCase(config.TABLE_TYPE_SINGLE)) {
                ModelField modelField = new ModelField();
                modelField.setAccessType("private");
                modelField.setClazzType("String");
                modelField.setName("tableName");
                model.setTable(modelField);
            }
            if (modelConfig.isGenerateModel()) {
                File modelFile = new File(javaPart, config.getModelPartName() + "/" + model.getName() + ".java");
                generateFile(modelTemplate, model, modelFile, modelConfig.isCoverModel());

            } else {
                logger.info("{} 不生成model", model.getName());
            }
            if (modelConfig.isGenerateMapper()) {
                File javaFile = new File(javaPart, config.getMapperPartName() + "/" + model.getName() + config.getMapperSuffix() + ".java");
                generateFile(mapperJavaTemplate, model, javaFile, modelConfig.isCoverMapper());
                File xmlFile = new File(javaPart, config.getMapperPartName() + "/" + model.getName() + config.getMapperSuffix() + ".xml");
                generateFile(mapperXmlTemplate, model, xmlFile, modelConfig.isCoverMapper());
            } else {
                logger.info("{} 不生成mapper", model.getName());
            }
            if (modelConfig.isGenerateCriteria()) {
                File criteriaFile = new File(javaPart, config.getCriteriaPartName() + "/" + model.getName() + config.getCriteriaSuffix() + ".java");
                generateFile(criteriaTemplate, model, criteriaFile, modelConfig.isCoverCriteria());
                if (modelConfig.isUseCriteriaStr()) {
                    criteriaFile = new File(javaPart, config.getCriteriaPartName() + "/" + model.getName() + config.getCriteriaStrSuffix() + ".java");
                    generateFile(criteriaTemplateStr, model, criteriaFile, modelConfig.isCoverCriteria());
                }
            } else {
                logger.info("{} 不生成criteria", model.getName());
            }

        }
    }


    private void prepLog() {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);

    }

    public static void main(String[] args) {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.generate("com.senpure.demo");
    }

}
