package com.hibase.baseweb.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.hibase.baseweb.core.web.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * swagger配置类
 *
 * @author chenfeng
 * @create 2018-08-31 13:53
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${hibase.basic.swagger.showSwagger:}")
    private boolean showSwagger;
    @Value("${hibase.basic.swagger.title}")
    private String title;

    @Value("${hibase.basic.swagger.contact.name:}")
    private String name = "";

    @Value("${hibase.basic.swagger.contact.url:}")
    private String url = "";

    @Value("${hibase.basic.swagger.contact.email:}")
    private String email = "";

    @Value("${hibase.basic.swagger.description:}")
    private String description = "";

    @Value("${hibase.basic.swagger.version:}")
    private String version;

    @Value("${hibase.basic.swagger.basePackage:}")
    private String basePackage;

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).enable(showSwagger)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(basePackage(basePackage))
                .paths(PathSelectors.any())
                .build().securitySchemes(securitySchemes())
                //.genericModelSubstitutes(ResponseEntity.class)
                // todo 支持泛型返回
                .securityContexts(securityContexts()).alternateTypeRules(newRule(typeResolver.resolve(DeferredResult.class,
                        typeResolver.resolve(ResponseModel.class, WildcardType.class)),
                        typeResolver.resolve(WildcardType.class)));
    }

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title(title)
                //创建人
                .contact(new Contact(name, url, email))
                //版本号
                .version(version)
                //描述
                .description(description)
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("token", authorizationScopes));
        return securityReferences;
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return new Predicate<RequestHandler>() {

            @Override
            public boolean apply(RequestHandler input) {
                return declaringClass(input).transform(handlerPackage(basePackage)).or(true);
            }
        };
    }

    /**
     * @param input RequestHandler
     * @return Optional
     */
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return new Function<Class<?>, Boolean>() {

            @Override
            public Boolean apply(Class<?> input) {
                for (String strPackage : basePackage.split(",")) {
                    boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                    if (isMatch) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
