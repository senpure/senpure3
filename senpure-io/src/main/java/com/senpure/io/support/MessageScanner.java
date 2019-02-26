package com.senpure.io.support;

import com.senpure.io.bean.IdName;
import com.senpure.io.protocol.Message;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * MessageScanner
 *
 * @author senpure
 * @time 2019-02-20 14:10:41
 */
public class MessageScanner {

    public static List<IdName> scan(String basePackage) {
        return scan(StringUtils.commaDelimitedListToStringArray(basePackage));
    }

    public static List<IdName> scan(String[] packages) {
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet();
        ClassPathScanningCandidateComponentProvider scan =
                new ClassPathScanningCandidateComponentProvider(false);
        scan.addIncludeFilter((metadataReader, metadataReaderFactory) -> Message.class.getName().equals(metadataReader.getClassMetadata().getSuperClassName()));
        for (String aPackage : packages) {
            beanDefinitions.addAll(scan.findCandidateComponents(aPackage));
        }
        List<IdName> idNames = new ArrayList<>();
        for (BeanDefinition definition : beanDefinitions) {
            Message message = null;
            try {
                message = (Message) Class.forName(definition.getBeanClassName()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (message != null) {
                IdName idName = new IdName();
                idName.setId(message.getMessageId());
                idName.setMessageName(message.getClass().getSimpleName());
                idNames.add(idName);
            }
        }
        return idNames;
    }




    public static void main(String[] args) {


    }
}
