package com.senpure.base.configuration;


import com.senpure.base.util.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Configuration
public class ConverterConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());
    //   // @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        return new HttpMessageConverters(fastConverter);
//    }

    /**
     * 防止 其他组件更改为xml等为默认格式
     *
     * @return
     */
  //  @Bean
    public HttpMessageConverters jacksonHttpMessageConverters(Jackson2ObjectMapperBuilder builder) {
        logger.info("初始化Jackson2HttpMessageConverter -------------------------");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(builder.createXmlMapper(false).build());
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.ALL);
         converter.setSupportedMediaTypes(mediaTypes);

        return new HttpMessageConverters(converter);
    }

    @Bean(name = "zh_cnStrDateConverter")
    @ConditionalOnMissingBean(name = "zh_cnStrDateConverter")
    public Converter<String, Date> dateConverter() {

        return new Str2Date();
    }

    class Str2Date implements Converter<String, Date> {
        @Override
        public Date convert(String s) {
            try {
                if (s != null && s.length() > 0) {
                    return DateFormatUtil.getDateFormat(DateFormatUtil.DFP_Y2S).parse(s);
                }
                return null;
            } catch (ParseException e) {
                return null;
            }
        }
    }

}
