package ru.otus.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Класс конфигурации Swagger2.
 * После запуска приложения переходим по http://localhost:8080/swagger-ui.html
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.otus.library"))
                .paths(PathSelectors.any())
                .build();
    }
}
