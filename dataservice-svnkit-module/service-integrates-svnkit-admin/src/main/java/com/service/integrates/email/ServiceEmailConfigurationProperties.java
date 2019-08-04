package com.service.integrates.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("service.integrates.email")
public class ServiceEmailConfigurationProperties {
    /**
     * service integrates default email.
     */
    private boolean withDefault;

    public boolean isWithDefault() {
        return withDefault;
    }

    public void setWithDefault(boolean withDefault) {
        this.withDefault = withDefault;
    }

}
