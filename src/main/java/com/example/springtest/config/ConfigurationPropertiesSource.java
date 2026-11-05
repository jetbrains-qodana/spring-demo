package com.example.springtest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
class ConfigurationPropertiesSource {
  @Bean
  @ConfigurationProperties("sample.bean")
  BeanProperties beanProperties() {
    return new BeanProperties();
  }
}

class BeanProperties {
  private Expression expression;

  public void setExpression(String value) {
    expression = new SpelExpressionParser().parseExpression(value);
  }

  Object evaluate() {
    return expression.getValue();
  }
}

@ConfigurationProperties("sample.class")
class ClassProperties {
  private Expression expression;

  public void setExpression(String value) {
    expression = new SpelExpressionParser().parseExpression(value);
  }

  Object evaluate() {
    return expression.getValue();
  }
}
