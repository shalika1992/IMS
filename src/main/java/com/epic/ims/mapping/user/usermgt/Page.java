package com.epic.ims.mapping.user.usermgt;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class Page {
    private String pageCode;
    private String description;
    private String url;
    private int sortKey;
    private String statusCode;
    private String sectionCode;
    private String userRoleCode;
    private Date createdTime;
    private String lastUpdatedUser;
    private Date lastUpdatedTime;

    public Page() {
    }

    public Page(String pageCode, String description, String url, int sortKey, String statusCode, String sectionCode, String userRoleCode, Date createdTime, String lastUpdatedUser, Date lastUpdatedTime) {
        this.pageCode = pageCode;
        this.description = description;
        this.url = url;
        this.sortKey = sortKey;
        this.statusCode = statusCode;
        this.sectionCode = sectionCode;
        this.userRoleCode = userRoleCode;
        this.createdTime = createdTime;
        this.lastUpdatedUser = lastUpdatedUser;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSortKey() {
        return sortKey;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getUserRoleCode() {
        return userRoleCode;
    }

    public void setUserRoleCode(String userRoleCode) {
        this.userRoleCode = userRoleCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
