package com.service.framework.jdbc.export;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class ExportProperties {
    @NestedConfigurationProperty
    private FormatConfig format;

    /**
     * Export data with chk file?
     */
    private boolean withChk;

    public FormatConfig getFormat() {
        return format;
    }

    public void setFormat(FormatConfig format) {
        this.format = format;
    }

    public boolean isWithChk() {
        return withChk;
    }

    public void setWithChk(boolean withChk) {
        this.withChk = withChk;
    }
}
