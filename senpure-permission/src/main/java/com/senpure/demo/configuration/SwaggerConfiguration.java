package com.senpure.demo.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {

        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("x-requested-with").description("ajax").modelRef(new ModelRef("string")).parameterType("header").defaultValue("XMLHttpRequest").required(true).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(apiInfo())


                .select()

                //.apis(RequestHandlerSelectors.basePackage("com.senpure"))
                .apis(notPackage("com.senpure.base"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SENPURE-PERMISSION SWAGGER API")
                .description("API")
                .termsOfServiceUrl("http://senpure.com")
                .version("1.0")
                .build();
    }

    public static Predicate<RequestHandler> notPackage(final String basePackage) {
        return new Predicate<RequestHandler>() {
            public boolean apply(RequestHandler input) {
               return   !ClassUtils.getPackageName(input.declaringClass()).startsWith(basePackage);

                  }
        };
    }
}
