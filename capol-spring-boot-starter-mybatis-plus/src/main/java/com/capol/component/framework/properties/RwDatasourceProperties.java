package com.capol.component.framework.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = RwDatasourceProperties.PREFIX)
public class RwDatasourceProperties {
    private boolean enable;

    private String dbType;

    public static final String PREFIX = "capol.datasource.rw";

    public static final String PREFIX_MASTER = ".write";

    public static final String PREFIX_SLAVE = ".read";

    public static final String PREFIX_DEFAULT = ".default";
}
