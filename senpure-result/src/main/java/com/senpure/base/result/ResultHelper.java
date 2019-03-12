package com.senpure.base.result;

import com.senpure.base.AppEvn;
import com.senpure.base.util.Assert;
import com.senpure.base.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;


public class ResultHelper implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    public static List<Result> results = new ArrayList<>();
    public static List<FieldAndInstance> fieldAndInstances = new ArrayList<>();
    private static boolean develop = false;
    private static boolean force = false;
    private static Logger logger = LoggerFactory.getLogger(ResultHelper.class);
    private static Map<Integer, String> codeMap = new HashMap();
    private static Map<Integer, String> codeName = new HashMap();
    private static Map<String, String> keyMap = new HashMap();
    private static String BASE_NAME = "i18n/result/result";


    public static String getKey(int code) {

        String key = codeMap.get(code);
        return key == null ? codeMap.get(Result.FAILURE) : key;
    }


    public static String getMessage(int code, Locale locale) {
        try {
            return ResourceBundle.getBundle(BASE_NAME, locale).getString(getKey(code));
        } catch (MissingResourceException e) {

            return "RESULT_CODE[" + code + "]";
        }

    }

    public static String getMessage(String key, Locale locale) {
        try {
            return ResourceBundle.getBundle(BASE_NAME, locale).getString(key);
        } catch (MissingResourceException e) {

            return "Message[" + key + "]";
        }

    }

    public static String getMessage(int code, Locale locale, Object... args) {


        return MessageFormat.format(getMessage(code, locale), args);

    }

    public static ResultMap wrapMessage(ResultMap resultMap, Locale locale) {

        if (resultMap.getArgs() != null && !resultMap.isClientFormat()) {
            return wrapMessage(resultMap, locale, resultMap.getArgs().toArray());
        }
        return
                resultMap.put(ResultMap.MESSAGE_KEY, ResultHelper.getMessage(resultMap.getCode(), locale));
    }

    public static ResultMap wrapMessage(ResultMap resultMap, Locale locale, Object... args) {

        return
                resultMap.put(ResultMap.MESSAGE_KEY, ResultHelper.getMessage(resultMap.getCode(), locale, args));
    }


    public static void refreshProperties() {
        ResourceBundle.clearCache();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (event.getApplicationContext().getParent() == null ||
                    event.getApplicationContext().getParent() instanceof AnnotationConfigApplicationContext) {
                syncResults();
            }
        } catch (Exception e) {
            logger.error("result解析出错", e);
            // act.close();
        }

    }


    public static void syncResults() {
        for (Result result : results) {
            report(result);

        }
        String rootPath = AppEvn.getClassRootPath();
        logger.debug("rootPath {}", rootPath);
        logger.debug("result baseName {}", BASE_NAME);
        logger.debug("startClass {}", AppEvn.getStartClass());
        URL url = AppEvn.getStartClass().getResource("/" + BASE_NAME + ".properties");
        logger.debug("url {}", url);
        File i18n = null;
        try {
            if (url != null) {
                String path = url.toURI().getPath();
                if (path != null) {
                    i18n = new File(path);
                }
            } else {
                logger.warn("资源文件不存在 {} ", BASE_NAME);
            }

        } catch (Exception e) {
            logger.error("", e);
        }
        if (develop) {
            i18n = new File(new File(rootPath).getParentFile().getParentFile(), "src/main/resources/" + BASE_NAME + ".properties");
        }
        boolean exist = false;
        boolean create = false;
        boolean update = false;
        if (i18n != null) {
            if (i18n.exists()) {
                exist = true;

            } else {
                if (develop) {
                    create = true;
                    i18n.getParentFile().mkdirs();
                }
            }
        }
        Properties props = new Properties();
        SortProperties save = new SortProperties();
        try {
            if (exist) {
                logger.debug("{} 资源文件完整路径：{}", i18n.exists(), i18n.getAbsolutePath());
                InputStream in = null;
                in = new FileInputStream(i18n);
                props.load(in);
                in.close();
            } else if (url != null) {
                logger.debug("资源文件完整路径：{}", url);
                props.load(url.openStream());
            } else {
                logger.warn("资源文件不存在 {} ", BASE_NAME);
                //  logger.warn("{}", ResourceBundle.getBundle(BASE_NAME));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<CodeAndInstance> codeAndInstanceList = new ArrayList<>();
        for (FieldAndInstance fieldAndInstance : fieldAndInstances) {

            for (Field field : fieldAndInstance.fields) {
                int code = 0;
                try {
                    code = field.getInt(fieldAndInstance.instance);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                    continue;
                }
                CodeAndInstance codeAndInstance = new CodeAndInstance();
                codeAndInstance.code = code;
                codeAndInstance.field = field;
                codeAndInstance.instance = fieldAndInstance.instance;
                codeAndInstanceList.add(codeAndInstance);
            }
        }
        Collections.sort(codeAndInstanceList, Comparator.comparingInt(o -> o.code));

        refreshProperties();
        keyMap.clear();
        codeMap.clear();
        StringBuilder updateBuilder = new StringBuilder();

        for (CodeAndInstance codeAndInstance : codeAndInstanceList) {
            Field field = codeAndInstance.field;
            Object instance = codeAndInstance.instance;
            int code = codeAndInstance.code;
            Message m = field.getAnnotation(Message.class);
            String name = m.key();
            if (StringUtil.isNullOrEmptyTrim(name)) {
                name = field.getName().replace("_", ".").toLowerCase();
            }
            //重复的时候提示使用
            String tempName = instance.getClass().getName() + "_" + field.getName();
            Assert.isNull(keyMap.get(name), "key不能重复 [" + name + "]\n" + keyMap.get(name) + "\n" + tempName);
            Assert.isNull(codeMap.get(code), "错误码不能重复 [" + code + "]\n" + codeName.get(code) + "\n" + tempName);
            codeName.put(code, tempName);
            keyMap.put(name, tempName);
            codeMap.put(code, name);

            String thisValue = null;
            if (m != null && m.value().trim().length() != 0) {
                thisValue = m.value();
            } else {
                thisValue = "RESULT-CODE[" + code + "]";

            }
            logger.trace(code + " >> " + name + " >> " + "RESULT-CODE[" + code + "]");
            save.put(name, thisValue);
            String value = props.getProperty(name);
            if (value == null) {
                update = true;
                updateBuilder.append(name).append("\n");

            } else if (force) {
                if (!value.equals(thisValue)) {
                    update = true;
                    updateBuilder.append(name).append("\n");
                }

            }
        }
        if (develop && update) {
            OutputStream out = null;
            try {
                out = new FileOutputStream(i18n);
                if (create) {
                    logger.debug("创建 result 资源文件");
                    save.store(out, "create properties");
                } else {
                    logger.debug("更新 result 资源文件");
                    save.store(out, "update name:\n" +
                            "############################################################################\n"
                            + updateBuilder.toString()
                            + "############################################################################");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        StringBuilder info = new StringBuilder();
        int codeMaxLen = 0;
        int keyMaxLen = 0;
        for (Map.Entry<Integer, String> entry : codeMap.entrySet()) {

            int len = (entry.getKey() + "").length();
            codeMaxLen = codeMaxLen > len ? codeMaxLen : len;
        }
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            int len = entry.getKey().length();
            keyMaxLen = keyMaxLen > len ? keyMaxLen : len;
        }
        for (CodeAndInstance codeAndInstance : codeAndInstanceList) {
            int codeLen = (codeAndInstance.code + "").length();
            String key = getKey(codeAndInstance.code);
            int keyLen = key.length();
            info.append(key).append(" ");
            for (int i = keyLen; i < keyMaxLen; i++) {
                info.append(" ");
            }
            info.append("[").append(codeAndInstance.code).append("] ");
            for (int i = codeLen; i < codeMaxLen; i++) {
                info.append(" ");
            }
            info.append(":").append(save.get(key)).append("\n");
        }
        logger.debug("结果集对照表\n{}", info.toString());

    }

    private AbstractApplicationContext act;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        act = (AbstractApplicationContext) applicationContext;

    }

    private static void report(Result result) {
        Field[] fields = result.getClass().getDeclaredFields();

        FieldAndInstance fieldAndInstance = new FieldAndInstance();
        for (Field field : fields) {

            if ("logger".equals(field.getName())) {
                continue;
            }
            if ("int".equals(field.getGenericType().getTypeName())) {
                fieldAndInstance.fields.add(field);
            } else {

                logger.warn("该类型的字段不支持 [{}],仅支持 [int] ", field.getGenericType().getTypeName());
            }

        }
        if (fieldAndInstance.fields.size() > 0) {
            fieldAndInstance.instance = result;
            ResultHelper.fieldAndInstances.add(fieldAndInstance);
        }
    }

    private static void findResult(Map<String, Result> map, Result result) {
        Result temp = map.get(result.getClass().getName());
        if (temp != null) {
            return;
        }
        map.put(result.getClass().getName(), result);
        try {
            Object object = result.getClass().getSuperclass().newInstance();
            if (object instanceof Result) {
                findResult(map, (Result) object);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public static void devSyncResult() {
        devSyncResult(new Result());
    }

    public static void devSyncResult(Result... result) {
        AppEvn.tryMarkClassRootPath();

        develop = true;
        force = true;
        if (result != null) {
            Map<String, Result> map = new LinkedHashMap<>();
            for (Result obj : result) {
                findResult(map, obj);
            }
            List<Result> results = new ArrayList<>();
            results.addAll(map.values());
            Collections.reverse(results);
            for (Result r : results) {
                report(r);
            }
            syncResults();
        }
        //  for (Class<Result> result : results) {
        //   result.newInstance().report();
        // }


    }

    public static String getResultBaseName() {
        return BASE_NAME;
    }

    public static void setResultBaseName(String resultBaseName) {
        BASE_NAME = resultBaseName;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, URISyntaxException {
        //AppEvn.markClassRootPath();
        devSyncResult(new Result());


    }


}
