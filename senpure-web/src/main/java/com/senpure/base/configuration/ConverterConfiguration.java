package com.senpure.base.configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.senpure.base.util.DateFormatUtil;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

//@Configuration
public class ConverterConfiguration {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        return new HttpMessageConverters(fastConverter);
    }
    @Bean
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
