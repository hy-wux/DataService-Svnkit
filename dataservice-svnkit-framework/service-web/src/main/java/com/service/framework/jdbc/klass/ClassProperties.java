package com.service.framework.jdbc.klass;

public class ClassProperties {
    /**
     * The class name for data load.
     */
    private String loaderText;
    /**
     * The class name for data export.
     */
    private String exportText;

    public String getLoaderText() {
        return loaderText;
    }

    public void setLoaderText(String loaderText) {
        this.loaderText = loaderText;
    }

    public String getExportText() {
        return exportText;
    }

    public void setExportText(String exportText) {
        this.exportText = exportText;
    }
}
