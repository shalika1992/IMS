package com.epic.ims.config.datasource;

import javax.sql.DataSource;

@FunctionalInterface
public interface DBConfiguration {
    /**
     *   @Author shalika_w
     *   @CreatedTime 2021-05-05 10:41:18 AM				
     *   @Version V1.00				
     *   @MethodName setup			
     *   @MethodParams []		
     *   @MethodDescription - This method returns the datasource object for give active profile.
     */
    public DataSource setup();
}
