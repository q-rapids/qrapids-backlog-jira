package com.bittium.qrapids.issuetracker.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration via Springfox
 *
 * @author Veikko Rytivaara
 * @author http://www.bittium.com
 *
 * @EnableSwagger2 Annotation that enables SpringFox support for Swagger 2.
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    /**
     * Swagger configuration via Springfox
     *
     * @link #SWAGGER_2 Tells the Docket bean that we are using version 2 of Swagger specification.
     * @link #select() Creates a builder, which is used to define which controllers and which of
     *       their methods should be included in the generated documentation.
     * @link #apis() Defines the classes (controller and model classes) to be included. Here we are
     *       including all of them, but you can limit them by a base package, class annotations and
     *       more.
     * @link #paths() Allow you to define which controller's methods should be included based on
     *       their path mappings. We are now including all of them but you can limit it using regex
     *       and more.
     */
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }
}
