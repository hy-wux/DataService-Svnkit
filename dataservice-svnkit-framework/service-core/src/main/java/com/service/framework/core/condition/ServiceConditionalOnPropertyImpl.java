package com.service.framework.core.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class ServiceConditionalOnPropertyImpl implements Condition {
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ServiceConditionalOnProperty.class.getName());
        String property = attributes.get("value").toString();
        Object value = attributes.get("havingValue");
        return value.equals(context.getEnvironment().getProperty(property));
    }
}
