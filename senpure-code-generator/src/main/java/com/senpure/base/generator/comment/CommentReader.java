package com.senpure.base.generator.comment;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CommentReader
 *
 * @author senpure
 * @time 2019-01-03 14:05:03
 */
public class CommentReader {

    private static Logger logger = LoggerFactory.getLogger(CommentReader.class);
    private static Map<String, ClassDoc> classDocMap = new HashMap<>();

    private static Map<String, List<FieldDoc>> fieldDocMap = new HashMap<>();
    private static RootDoc root;

    // 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
    public static class Doclet {

        public Doclet() {
        }

        public static boolean start(RootDoc root) {
            CommentReader.root = root;
            return true;
        }
    }


    public static String getExplain(Class clazz, Field field) {
        List<FieldDoc> fieldDocs = fieldDocMap.get(clazz.getName());

        if (fieldDocs != null) {
            for (FieldDoc fieldDoc : fieldDocs) {
                if (fieldDoc.name().equals(field.getName())) {
                    String explain = fieldDoc.commentText();
                    if (explain.trim().length() > 0) {
                        return explain;
                    }
                }
            }
        }

        return null;
    }

    public static String getExplain(Class clazz) {
        ClassDoc classDoc = classDocMap.get(clazz.getName());
        if (classDoc != null) {
            String comment = classDoc.commentText();
            if (comment.trim().length() > 0) {
                return comment;
            }
        }
        return null;
    }

    public static void readComment(List<String> files) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> cmd = new ArrayList<>();
        cmd.add("-doclet");
        cmd.add(Doclet.class.getName());
        cmd.add("-encoding");
        cmd.add("utf-8");
        cmd.add("-classpath");
        cmd.add(runtimeMXBean.getClassPath());
        for (String file : files) {
            cmd.add(file);
        }
        String[] str = new String[cmd.size()];
        com.sun.tools.javadoc.Main.execute(cmd.toArray(str));

        ClassDoc[] classes = root.classes();
        for (ClassDoc classDoc : classes) {

            classDocMap.put(classDoc.toString(), classDoc);
            List<FieldDoc> fieldDocs = new ArrayList<>();
            fieldDoc(classDoc, fieldDocs);
            fieldDocMap.put(classDoc.toString(), fieldDocs);
        }
    }

    private static void fieldDoc(ClassDoc classDoc, List<FieldDoc> fieldDocs) {
        if (classDoc.toString().equals("java.lang.Object")) {
            return;
        } else {
            fieldDoc(classDoc.superclass(), fieldDocs);
        }
        for (FieldDoc fieldDoc : classDoc.fields(false)) {
            fieldDocs.add(fieldDoc);
        }
    }


    public static void show() throws ClassNotFoundException {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];

            System.out.println(classDoc.superclass());
            System.out.println(classDoc.toString());
            for (FieldDoc fieldDoc : classDoc.fields(false)) {
                System.out.println(fieldDoc.toString() + "  :   " + fieldDoc.getRawCommentText());
            }

        }
    }


    public static void main(final String... args) throws Exception {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        String aray[] = new String[]{"D:\\Projects\\senpure\\senpure-code-generator\\src\\main\\java\\com\\senpure\\base\\generator\\Model.java", "D:\\Projects\\senpure\\senpure-code-generator\\src\\main\\java\\com\\senpure\\base\\generator\\ModelField.java"};
        com.sun.tools.javadoc.Main.execute(new String[]{"-doclet",
                Doclet.class.getName(),
// 因为自定义的Doclet类并不在外部jar中，就在当前类中，所以这里不需要指定-docletpath 参数，
//              "-docletpath",
//              Doclet.class.getResource("/").getPath(),
                "-encoding", "utf-8",
                "-classpath",
                runtimeMXBean.getClassPath(),
// 获取单个代码文件FaceLogDefinition.java的javadoc
                "D:\\Projects\\senpure\\senpure-code-generator\\src\\main\\java\\com\\senpure\\base\\generator\\Model.java", "D:\\Projects\\senpure\\senpure-code-generator\\src\\main\\java\\com\\senpure\\base\\generator\\ModelField.java"});

        show();
    }


}
