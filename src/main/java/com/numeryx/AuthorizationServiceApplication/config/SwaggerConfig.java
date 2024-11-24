package com.numeryx.AuthorizationServiceApplication.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com")
public class SwaggerConfig {


    @Value("${config.oauth2.accessTokenUri}")
    private String accessTokenUri;


    public static final String SECURITYSCHEMAOAUTH2 = "oauth2schema";
    public static final String AUTHORIZATIONSCOPEGLOBAL = "global";
    public static final String AUTHORIZATIONSCOPEGLOBALDESC = "accessEverything";

    private final ServletContext servletContext;

    @Autowired
    public SwaggerConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    /**
     * @return Docket
     */
    @Bean
    public Docket productApi() {


        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.numeryx.AuthorizationServiceApplication.resource"))
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema(), apiKey(), apiCookieKey()))
                .apiInfo(apiInfo())
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        return "";
                    }
                });
    }

    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey(HttpHeaders.AUTHORIZATION, "apiKey", "header");
    }

    @Bean
    public SecurityScheme apiCookieKey() {
        return new ApiKey(HttpHeaders.COOKIE, "apiKey", "cookie");
    }

    private OAuth securitySchema() {

        List<AuthorizationScope> authorizationScopeList = newArrayList();
        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
        authorizationScopeList.add(new AuthorizationScope("write", "access all"));
        authorizationScopeList.add(new AuthorizationScope("trust", "access all"));

        List<GrantType> grantTypes = newArrayList();
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
        grantTypes.add(passwordCredentialsGrant);

        return new OAuth("oauth2", authorizationScopeList, grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .build();
    }


    private List<SecurityReference> defaultAuth() {

        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
        authorizationScopes[0] = new AuthorizationScope("read", "read all");
        authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
        authorizationScopes[2] = new AuthorizationScope("write", "write all");

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration
                ("client", "secret", "", "", "Bearer access token", ApiKeyVehicle.HEADER, HttpHeaders.AUTHORIZATION, "");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("authorization server").description("@2019")
                .contact(new Contact("SAB", "", ""))
                .license("")
                .licenseUrl("")
                .build();

    }


    @Bean
    PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor nameExtractor,
                                                                  TypeResolver resolver) {

        return new PageableParameterBuilderPlugin(nameExtractor, resolver);
    }

}
