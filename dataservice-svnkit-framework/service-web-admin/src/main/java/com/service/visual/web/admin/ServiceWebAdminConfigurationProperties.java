package com.service.visual.web.admin;

import com.service.visual.web.admin.menus.KaptchaType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("service.visual.web")
public class ServiceWebAdminConfigurationProperties {

    /**
     * The web application name.
     */
    private String applicationName;

    /**
     * Use captcha when login.
     */
    private Boolean useKaptcha;

    /**
     * The captcha type. charkaptcha / mathkaptcha
     */
    private KaptchaType kaptchaType;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Boolean getUseKaptcha() {
        return useKaptcha;
    }

    public void setUseKaptcha(Boolean useKaptcha) {
        this.useKaptcha = useKaptcha;
    }

    public KaptchaType getKaptchaType() {
        return kaptchaType;
    }

    public void setKaptchaType(KaptchaType kaptchaType) {
        this.kaptchaType = kaptchaType;
    }

}
