package com.service.integrates.svnkit.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("service.svnkit")
public class ServiceSvnkitConfigurationProperties {
    /**
     * The svn server version name. VisualSVN / Subversion
     */
    private ServiceSvnkitVersion version;

    /**
     * The svn server FileSystem path
     */
    private String path;

    private String svnServeConf;

    private String passwdFile;

    private String groupFile;

    private String authzFile;

    public ServiceSvnkitVersion getVersion() {
        return version;
    }

    public void setVersion(ServiceSvnkitVersion version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSvnServeConf() {
        return svnServeConf;
    }

    public void setSvnServeConf(String svnServeConf) {
        this.svnServeConf = svnServeConf;
    }

    public String getPasswdFile() {
        return passwdFile;
    }

    public void setPasswdFile(String passwdFile) {
        this.passwdFile = passwdFile;
    }

    public String getGroupFile() {
        return groupFile;
    }

    public void setGroupFile(String groupFile) {
        this.groupFile = groupFile;
    }

    public String getAuthzFile() {
        return authzFile;
    }

    public void setAuthzFile(String authzFile) {
        this.authzFile = authzFile;
    }
}