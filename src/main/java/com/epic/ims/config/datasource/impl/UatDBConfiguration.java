package com.epic.ims.config.datasource.impl;

import com.epic.ims.config.datasource.DBConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("uat")
public class UatDBConfiguration implements DBConfiguration {

    @Override
    public DataSource setup() {
        final JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
        jndiDataSourceLookup.setResourceRef(true);
        return jndiDataSourceLookup.getDataSource("jdbc/ims");
    }
}
