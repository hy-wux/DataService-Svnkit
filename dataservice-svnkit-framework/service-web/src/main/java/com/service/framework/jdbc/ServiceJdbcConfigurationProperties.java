package com.service.framework.jdbc;

import com.service.framework.jdbc.enums.DatabaseType;
import com.service.framework.jdbc.export.ExportProperties;
import com.service.framework.jdbc.klass.ClassProperties;
import com.service.framework.jdbc.mycat.MycatProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("service.jdbc")
@MapperScan(basePackages = {"com.service.**.mapper"})
public class ServiceJdbcConfigurationProperties {
    private DatabaseType databaseType;

    @NestedConfigurationProperty
    private ExportProperties export;

    @NestedConfigurationProperty
    private ClassProperties className;

    @NestedConfigurationProperty
    private MycatProperties mycat;

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public ExportProperties getExport() {
        return export;
    }

    public void setExport(ExportProperties export) {
        this.export = export;
    }

    public ClassProperties getClassName() {
        return className;
    }

    public void setClassName(ClassProperties className) {
        this.className = className;
    }

    public MycatProperties getMycat() {
        return mycat;
    }

    public void setMycat(MycatProperties mycat) {
        this.mycat = mycat;
    }
}
