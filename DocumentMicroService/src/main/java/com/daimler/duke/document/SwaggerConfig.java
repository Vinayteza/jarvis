package com.daimler.duke.document;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author RMAHAKU
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
  /**
   * productApi.
   */
  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2).select()
                                                  // .apis(RequestHandlerSelectors.basePackage("com.daimler.duke"))
                                                  .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                                                  .build()
                                                  .apiInfo(metaData())
                                                  .directModelSubstitute(java.time.LocalDate.class,
                                                                         String.class);

  }

  private ApiInfo metaData() {
    ApiInfo apiInfo = new ApiInfo("Duke Document API",
                                  "Document API to handle all kinds of documents",
                                  "Duke Doc App Version 1.0",
                                  "Terms of service",
                                  "",
                                  "",
                                  "");
    return apiInfo;
  }
}
