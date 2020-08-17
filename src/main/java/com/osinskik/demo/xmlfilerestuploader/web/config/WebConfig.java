package com.osinskik.demo.xmlfilerestuploader.web.config;

import com.osinskik.demo.xmlfilerestuploader.web.sorting.EpaperRequestSortingTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new EpaperRequestSortingTypeConverter());
  }

}
