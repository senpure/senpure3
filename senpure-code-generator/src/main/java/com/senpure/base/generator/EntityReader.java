package com.senpure.base.generator;

import com.senpure.base.AppEvn;
import com.senpure.base.annotation.Explain;
import com.senpure.base.annotation.LongDate;
import com.senpure.base.generator.comment.CommentReader;
import com.senpure.base.generator.naming.ImprovedNamingStrategy;
import com.senpure.base.generator.naming.NamingStrategy;
import com.senpure.base.util.Assert;
import com.senpure.base.util.StringUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL8Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.*;


public class EntityReader {

    private GeneratorConfig config;
    // org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
    // org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    private PhysicalNamingStrategy physicalNamingStrategy;

    private ImplicitNamingStrategy implicitNamingStrategy;
    private NamingStrategy namingStrategy;
    private Dialect dialect;

    public EntityReader(GeneratorConfig config) {
        this.config = config;
        namingStrategy = config.getNamingStrategy();
        if (namingStrategy == null) {
            namingStrategy = new ImprovedNamingStrategy();
        }
        if (dialect == null) {
            dialect = new MySQL8Dialect();
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
    Map<String, Model> modelMap = new HashMap<>();


    DefaultEntity defaultEntity = new DefaultEntity();
    private String packageName;
    private String[] unullable = {"int", "char", "short", "byte", "float", "double", "boolean", "long"};
    Column defaultColumn;

    {
        try {
            defaultColumn = defaultEntity.getClass().getDeclaredField("column").getAnnotation(Column.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Model> read(String packageName) {

        this.packageName = packageName;
        File file = new File(AppEvn.getClassRootPath(), packageName.replace(".", File.separator));
        return read(file);
    }

    public Map<String, Model> read(File file) {
        String classRootPath = AppEvn.getClassRootPath();
        File[] files = null;
        if (file.isDirectory()) {
            files = file.listFiles();
        } else {
            files = new File[1];
            files[0] = file;
        }
        List<String> javaSourceFiles = new ArrayList<>();
        for (File entity : files) {
            if (entity.getName().endsWith(".class")) {
                String path = entity.getAbsolutePath();
                path = path.replace(config.getClass2SourceClass(), config.getClass2SourceSource());
                path = path.replace(".class", ".java");
                javaSourceFiles.add(path);
            }
        }
        PrintStream out = System.out;
        try {
            File htmlFile = File.createTempFile("temp", ".html");
            // logger.debug(htmlFile.getAbsolutePath());
            System.setOut(new PrintStream(htmlFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CommentReader.readComment(javaSourceFiles);
        System.setOut(out);
        for (File entity : files) {
            if (entity.getName().endsWith(".class")) {
                String classPath = entity.getAbsolutePath().replace(classRootPath, "");
                classPath = classPath.replace(".class", "");
                classPath = classPath.replace(File.separatorChar, '.');
                if (classPath.startsWith(".")) {
                    classPath = classPath.replaceFirst("\\.", "");
                }
                // classPath =classPath  .replace('/','.');
                logger.debug("classPath {}", classPath);
                Class c = null;
                Object obj = null;
                try {
                    c = Class.forName(classPath);
                    obj = c.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                if (c != null) {
                    if (c.getAnnotation(Entity.class) != null) {
                        read(obj);
                    } else {
                        logger.debug("跳过 {}", c.getName());
                    }
                }
            }
        }
        readAfter();
        return modelMap;
    }

    private void read(Object obj) {
        logger.debug("reade {}", obj.getClass());
        Model model = new Model();
        model.setEntityPackage(obj.getClass().getPackage().getName());
        logger.debug(model.getEntityPackage());
        modelMap.put(obj.getClass().getName(), model);

        Table table = obj.getClass().getAnnotation(Table.class);
        if (table == null || table.name().length() == 0) {
            model.setTableName(namingStrategy.classToTableName(obj.getClass().getName()));
        } else {
            model.setTableName(namingStrategy.classToTableName(table.name()));
        }

        Explain explain = obj.getClass().getAnnotation(Explain.class);
        if (explain != null) {
            if (explain.value().length() > 0) {
                model.setExplain(explain.value());
            }
        } else {
            String explainStr = CommentReader.getExplain(obj.getClass());
            if (explainStr != null && explainStr.length() > 0) {
                model.setExplain(explainStr);
            }
        }

        String name = obj.getClass().getSimpleName();
        name = name.replace("Table", "");
        name = name.replace("Entity", "");
        model.setName(name);

        Field[] fields = obj.getClass().getDeclaredFields();
        model.setClazz(obj.getClass());
        readSuperClass(model, obj.getClass().getSuperclass());
        model.getClazzs().add(obj.getClass().getName());

        readFields(model, fields);
        logger.debug(model.toString());
    }

    private void readAfter() {
        logger.info("readAfter------------------");
        Iterator<Map.Entry<String, Model>> iterator = modelMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Model> entry = iterator.next();
            Model model = entry.getValue();
            List<String> targets = model.getAfterReadColumn().getTargetClass();
            if (targets.size() > 0) {
                for (String name : targets) {
                    Model target = modelMap.get(name);
                    if (target == null) {
                        Assert.error(name + "不存在 ");
                    }
                    // ModelField modelField = new ModelField();
                }
            }
            List<ModelField> modelFields = model.getAfterReadColumn().getModelFields();
            //没有id 和version
            Map<String, ModelField> tempModelFieldMap = new LinkedHashMap<>(model.getModelFieldMap());
            //完成外键信息补充
            for (ModelField modelField : modelFields) {
                Model target = modelMap.get(modelField.getName());
                if (target == null) {
                    Assert.error(modelField.getName() + "不存在 ");
                }
                logger.debug(target.toString());
                modelField.setClazzType(target.getId().getClazzType());
                modelField.setJdbcType(target.getId().getJdbcType());
                target.setChild(model);
                target.setChildField(modelField);
                find(model, modelField, false);
                if (modelField.getColumn() == null) {
                    modelField.setName(StringUtil.toLowerFirstLetter(target.getName()
                            + StringUtil.toUpperFirstLetter(target.getId().getName())));
                    modelField.setColumn(namingStrategy.columnName(target.getName() + target.getId().getName()));
                } else {
                    modelField.setName(modelField.getColumn());
                    modelField.setColumn(namingStrategy.columnName(modelField.getColumn()));
                }
                String ex = "外键," + "modelName:" + target.getName() + ",tableName:" + target.getTableName();
                if (modelField.getExplain() == null) {
                    modelField.setExplain(ex);
                } else if (!modelField.getExplain().contains("外键")) {
                    modelField.setExplain(modelField.getExplain() + "(" + ex + ")");
                }
                modelField.setJavaNullable(javaNullable(target.getId().getClazzType()));
                model.getModelFieldMap().put(modelField.getName(), modelField);
                model.getCriteriaFieldMap().put(modelField.getName(), modelField);
            }


            model.getCriteriaFieldMap().putAll(tempModelFieldMap);
            //将时间加入 >= <=的条件
            for (ModelField field : model.getDateFieldMap().values()) {
                field.setHasCriteriaRange(true);
                String longName = field.getName();
                //查找时间戳的字段
                if (longName.endsWith("Date")) {
                    logger.debug("time field {}", field);
                    longName = longName.substring(0, longName.length() - 4) + "Time";
                    logger.debug("time long name {}", longName);
                    ModelField temp = model.getModelFieldMap().get(longName);
                    if (temp != null) {
                        temp.setStrShow(false);
                        temp.setHasCriteriaRange(true);
                        temp.setLongTime(true);
                        field.setLongDate(temp);
                    }
                }
            }


            Collection<ModelField> modelFieldCollection = model.getModelFieldMap().values();
            for (ModelField modelField : modelFieldCollection) {
                if (modelField.isHasCriteriaRange()) {
                    model.setHasRange(true);
                    break;
                }
            }
            //解读出findBy的字段
            for (ModelField modelField : modelFieldCollection) {
                if (modelField.getName().equals("account")) {
                    find(model, modelField, true);
                } else if (modelField.getName().equals("name")) {
                    find(model, modelField, true);
                } else if (modelField.getName().endsWith("Name") && !modelField.getName().startsWith("readable")) {
                    find(model, modelField, false);
                } else if (modelField.getName().equals("nick")) {
                    find(model, modelField, false);
                } else if (modelField.getName().endsWith("Nick")) {
                    find(model, modelField, false);
                } else if (modelField.getName().endsWith("Id")) {
                    if (model.getName().endsWith("Info")
                            || model.getName().endsWith("info")
                            || model.getName().endsWith("Ext")
                            || model.getName().endsWith("ext")
                    ) {
                        find(model, modelField, true);
                    } else {
                        find(model, modelField, false);
                    }
                } else if (modelField.getName().equals("type")) {
                    find(model, modelField, false);
                } else if (modelField.getName().endsWith("Type")) {
                    find(model, modelField, false);
                } else if (modelField.getName().equals("key")) {
                    find(model, modelField, true);
                } else if (modelField.getName().endsWith("Key")) {
                    find(model, modelField, true);
                } else if (modelField.getName().equals("uriAndMethod")) {
                    find(model, modelField, false);
                } else if (modelField.getName().equalsIgnoreCase("gold")
                        || modelField.getName().equalsIgnoreCase("golds")
                        || modelField.getName().equalsIgnoreCase("diamond")
                ) {
                    find(model, modelField, false);
                }
            }
            if (model.getFindModeFields().size() == 0) {
                for (ModelField modelField : modelFieldCollection) {
                    if (modelField.getClazzType().equals("String")) {
                        if (modelField.getName().equals("text")) {
                            // model.getFindModeFields().add(modelField);
                            break;
                        }
                    }
                }
            }
        }


    }

    private void find(Model model, ModelField modelField, boolean findOne) {
        if (model.getFindModeFields().contains(modelField)) {
            logger.info("change {} to {},{}  {}", modelField.isFindOne(), findOne, model, modelField);
            modelField.setFindOne(findOne);
            modelField.setCriteriaOrder(true);
        } else {
            logger.info("find  {}  {}", model, modelField);
            modelField.setFindOne(findOne);
            modelField.setCriteriaOrder(true);
            model.getFindModeFields().add(modelField);
        }
    }

    private void readFields(Model model, Field[] fields) {
        for (Field field : fields) {
            //排除static
            if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                logger.info("跳过{}", field.getName());
                continue;
            }
            String simpleName = field.getType().getSimpleName();
            if ("List".equalsIgnoreCase(simpleName) ||
                    "Set".equalsIgnoreCase(simpleName)) {
                logger.info("跳过{}", field.getName());
                continue;
            }
            Transient tr = field.getAnnotation(Transient.class);
            if (tr == null) {
                ModelField modelField = new ModelField();
                Explain explain = field.getAnnotation(Explain.class);
                if (explain != null) {
                    if (explain.value().length() > 0) {
                        modelField.setExplain(explain.value());
                    }
                } else {
                    String explainStr = CommentReader.getExplain(model.getClazz(), field);
                    if (explainStr != null && explainStr.length() > 0) {
                        modelField.setExplain(explainStr);
                    }
                }
                ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                if (manyToOne != null) {
                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    if (joinColumn != null && joinColumn.name().length() > 0) {
                        // modelField.setColumn(namingStrategy.columnName(joinColumn.name()));
                        modelField.setColumn(joinColumn.name());
                        // logger.debug(modelField.getColumn());
                    }
                    modelField.setNullable(joinColumn.nullable());
                    modelField.setAccessType(Modifier.toString(field.getModifiers()));
                    modelField.setClazzType(field.getType().getName());
                    modelField.setName(field.getType().getName());
                    logger.debug("暂时不处理字段{} {}", field.getType().getName(), field.getName());
                    model.getAfterReadColumn().getTargetClass().add(field.getType().getName());
                    model.getAfterReadColumn().getModelFields().add(modelField);
                    continue;
                }

                modelField.setClazzType(field.getType().getSimpleName());
                modelField.setAccessType(Modifier.toString(field.getModifiers()));
                modelField.setName(field.getName());
                Column column = field.getAnnotation(Column.class);
                if (column == null) {
                    column = defaultColumn;
                }
                modelField.setNullable(column.nullable());
                String jdbcType = dialect.getTypeName(jdbcTypeCode(modelField.getClazzType()),
                        column.length(), column.precision(), column.scale()).toUpperCase();
                int index = jdbcType.indexOf("(");
                if (index > 0) {
                    jdbcType = jdbcType.substring(0, index);
                }
                modelField.setJdbcType(jdbcType);
                if ("Date".equals(modelField.getClazzType())) {
                    model.setHasDate(true);
                    model.getDateFieldMap().put(modelField.getName(), modelField);
                    modelField.setJdbcType("TIMESTAMP");
                    modelField.setDate(true);
                    modelField.setCriteriaOrder(true);
                }
                if ("DOUBLE PRECISION".equalsIgnoreCase(jdbcType)) {
                    modelField.setJdbcType("DOUBLE");
                }
                modelField.setJavaNullable(javaNullable(modelField.getClazzType()));
                if (column.name().length() == 0) {
                    modelField.setColumn(namingStrategy.columnName(field.getName()));
                } else {
                    modelField.setColumn(namingStrategy.columnName(column.name()));
                }
                if (field.getAnnotation(Id.class) == null) {
                    model.getModelFieldMap().put(modelField.getName(), modelField);
                } else {
                    modelField.setNullable(false);
                    //  modelField.setJavaNullable(false);
                    if (modelField.getExplain() == null) {
                        modelField.setExplain("主键");
                    } else if (!modelField.getExplain().contains("主键")) {
                        modelField.setExplain(modelField.getExplain() + "(主键)");
                    }
                    modelField.setId(true);
                    model.getModelFieldMap().remove(modelField.getName());
                    model.setId(modelField);
                    GenericGenerator generatedValue = field.getAnnotation(GenericGenerator.class);
                    if (generatedValue == null) {
                        modelField.setDatabaseId(true);
                    } else {
                        modelField.setDatabaseId(false);
                    }
                }
                if (field.getAnnotation(Version.class) != null) {
                    modelField.setNullable(false);
                    //  modelField.setJavaNullable(false);
                    modelField.setVersion(true);
                    modelField.setExplain("乐观锁，版本控制");
                    model.getModelFieldMap().remove(modelField.getName());
                    model.setVersion(modelField);

                }
                if (field.getAnnotation(LongDate.class) != null) {
                    if (modelField.getClazzType().equalsIgnoreCase("long")) {
                        model.setHasLongDate(true);
                        modelField.setLongTime(true);
                        model.getDateFieldMap().put(modelField.getName(), modelField);
                    }
                }
                logger.debug(modelField.toString());

            }
        }
    }

    private void readSuperClass(Model model, Class superClazz) {
        if (superClazz == null) {
            return;
        }
        readSuperClass(model, superClazz.getSuperclass());
        if (superClazz.getAnnotation(MappedSuperclass.class) != null) {
            model.getClazzs().add(superClazz.getName());
            Field[] fields = superClazz.getDeclaredFields();
            readFields(model, fields);
        }


    }

    private int jdbcTypeCode(String type) {

        switch (type) {
            case "char":
                return Types.CHAR;
            case "byte":
            case "Byte":
                return Types.TINYINT;
            case "String":
                return Types.VARCHAR;

            case "Date":
                return Types.DATE;
            case "boolean":
            case "Boolean":
                return Types.BOOLEAN;
            case "int":
            case "Integer":
                return Types.INTEGER;
            case "short":
            case "Short":
                return Types.SMALLINT;
            case "float":
            case "Float":
                return Types.FLOAT;
            case "double":
            case "Double":
                return Types.DOUBLE;
            case "long":
            case "Long":
                return Types.BIGINT;
        }

        Assert.error("不支持的Java类型" + type);
        return Types.VARCHAR;
    }

    private boolean javaNullable(String type) {
        for (String str : unullable) {
            if (str.equals(type)) {

                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {


    }
}
