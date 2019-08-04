package com.service.framework.core.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional({ServiceConditionalOnPropertyImpl.class})
public @interface ServiceConditionalOnProperty {
    String value();

    String havingValue();
}
