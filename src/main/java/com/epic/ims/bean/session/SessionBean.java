package com.epic.ims.bean.session;

import com.epic.ims.mapping.passwordpolicy.PasswordPolicy;
import com.epic.ims.mapping.user.usermgt.Page;
import com.epic.ims.mapping.user.usermgt.Section;
import com.epic.ims.mapping.user.usermgt.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SessionBean {
    private String sessionid;
    private String username;
    private User user;
    private boolean changePwdMode;
    private int daysToExpire;
    private List<Section> sectionList = new ArrayList<>();
    private Map<String, List<Page>> pageMap = new HashMap<String, List<Page>>();
    private PasswordPolicy passwordPolicy;

    public SessionBean() {
    }

    public SessionBean(String sessionid, String username, User user, boolean changePwdMode, int daysToExpire, List<Section> sectionList, Map<String, List<Page>> pageMap, PasswordPolicy passwordPolicy) {
        this.sessionid = sessionid;
        this.username = username;
        this.user = user;
        this.changePwdMode = changePwdMode;
        this.daysToExpire = daysToExpire;
        this.sectionList = sectionList;
        this.pageMap = pageMap;
        this.passwordPolicy = passwordPolicy;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChangePwdMode() {
        return changePwdMode;
    }

    public void setChangePwdMode(boolean changePwdMode) {
        this.changePwdMode = changePwdMode;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(int daysToExpire) {
        this.daysToExpire = daysToExpire;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public Map<String, List<Page>> getPageMap() {
        return pageMap;
    }

    public void setPageMap(Map<String, List<Page>> pageMap) {
        this.pageMap = pageMap;
    }

    public PasswordPolicy getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }
}
