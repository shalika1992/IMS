package com.epic.ims.repository.login;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.login.LoginBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.user.usermgt.Page;
import com.epic.ims.mapping.user.usermgt.Section;
import com.epic.ims.mapping.user.usermgt.User;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Scope("prototype")
public class LoginRepository {
    private static Logger logger = LogManager.getLogger(LoginRepository.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    private final String SQL_GET_USER_LOGIN = "select username,password,userrole,expirydate,fullname,email,mobile,noofinvalidattempt,lastloggeddate,initialloginstatus,status,createduser,createdtime,lastupdateduser,lastupdatedtime from web_systemuser where lower(username)=?";
    private final String SQL_UPDATE_VALID_USER_LOGIN = "update web_systemuser set noofinvalidattempt=? , lastloggeddate = ? , status = ? , lastupdatedtime = ? where lower(username) =?";
    private final String SQL_UPDATE_INVALID_USER_LOGIN = "update web_systemuser set noofinvalidattempt=? , status = ? , lastupdatedtime = ? where lower(username) =?";

    private final String SQL_GET_USER_SECTIONLIST = "select distinct distinct sp.section as section,s.description as description,  sp.userrole as userrole, s.sortkey as sortkey, s.status as status,s.createdtime as createdtime ,s.lastupdateduser as lastupdateduser, s.lastupdatedtime as lastupdatedtime  from web_sectionpage sp inner join web_section s on sp.section = s.sectioncode where s.status=? and sp.userrole=? order by s.sortkey";
    private final String SQL_GET_USER_PAGELIST = "select p.description as description,p.pagecode as pagecode ,p.url as url,p.sortkey as sortkey,p.status as status ,sp.section as section ,sp.userrole as userrole,p.lastupdateduser as lastupdateduser , p.createdtime as createtime ,p.lastupdatedtime as lastupdatedtime from web_sectionpage sp inner join web_page p on sp.page = p.pagecode where p.status = ? and sp.userrole = ? order by p.sortkey";

    @LogRepository
    @Transactional(readOnly = true)
    public User getUser(LoginBean loginBean) {
        User user = null;
        try {
            List<User> map = jdbcTemplate.query(SQL_GET_USER_LOGIN, new Object[]{loginBean.getUsername()}, (rs, rowNum) -> {
                User u = new User();

                try {
                    u.setUserName(rs.getString("username"));
                } catch (Exception e) {
                    u.setUserName(null);
                }

                try {
                    u.setPassword(rs.getString("password"));
                } catch (Exception e) {
                    u.setPassword(null);
                }

                try {
                    u.setUserRole(rs.getString("userrole"));
                } catch (Exception e) {
                    u.setUserRole(null);
                }

                try {
                    u.setExpiryDate(rs.getDate("expirydate"));
                } catch (Exception e) {
                    u.setExpiryDate(null);
                }

                try {
                    u.setFullName(rs.getString("fullname"));
                } catch (Exception e) {
                    u.setFullName(null);
                }

                try {
                    u.setEmail(rs.getString("email"));
                } catch (Exception e) {
                    u.setEmail(null);
                }

                try {
                    u.setMobile(rs.getString("mobile"));
                } catch (Exception e) {
                    u.setMobile(null);
                }

                try {
                    u.setNoOfInvlidAttempt(rs.getByte("noofinvalidattempt"));
                } catch (Exception e) {
                    u.setNoOfInvlidAttempt(null);
                }

                try {
                    u.setLoggedDate(rs.getDate("lastloggeddate"));
                } catch (Exception e) {
                    u.setLoggedDate(null);
                }

                try {
                    u.setInitialLoginStatus(rs.getString("initialloginstatus"));
                } catch (Exception e) {
                    u.setInitialLoginStatus(null);
                }

                try {
                    u.setStatus(rs.getString("status"));
                } catch (Exception e) {
                    u.setStatus(null);
                }

                try {
                    u.setCreatedUser(rs.getDate("createduser"));
                } catch (Exception e) {
                    u.setCreatedUser(null);
                }

                try {
                    u.setCreatedTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    u.setCreatedTime(null);
                }

                try {
                    u.setLastUpdatedUser(rs.getString("lastupdateduser"));
                } catch (Exception e) {
                    u.setLastUpdatedUser(null);
                }

                try {
                    u.setLastUpdatedTime(rs.getDate("lastupdatedtime"));
                } catch (Exception e) {
                    u.setLastUpdatedTime(null);
                }
                return u;
            });
            if (map.size() > 0) {
                user = map.get(0);
            }
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
            return user;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
        return user;
    }

    @LogRepository
    @Transactional
    public int updateUser(LoginBean loginBean, boolean flag) throws Exception {
        int update;
        try {
            Date currentDate = commonRepository.getCurrentDate();
            if (flag) {
                update = jdbcTemplate.update(SQL_UPDATE_VALID_USER_LOGIN, new Object[]{loginBean.getAttempts(), currentDate, loginBean.getStatusCode(), currentDate, loginBean.getUsername()});
            } else {
                update = jdbcTemplate.update(SQL_UPDATE_INVALID_USER_LOGIN, new Object[]{loginBean.getAttempts(), loginBean.getStatusCode(), currentDate, loginBean.getUsername()});
            }
        } catch (Exception e) {
            throw e;
        }
        return update;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Section> getUserSectionListByUserRoleCode(String userRole) {
        List<Section> sectionList = new ArrayList<>();
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_USER_SECTIONLIST, new Object[]{commonVarList.STATUS_ACTIVE, userRole});
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    String sectionCode = list.get(i).get("section") + "";
                    String description = list.get(i).get("description") + "";
                    String userRoleCode = list.get(i).get("userrole") + "";
                    int sortKey = Integer.parseInt(list.get(i).get("sortkey") + "");
                    String status = list.get(i).get("status") + "";
                    Date createdTime = (Date) list.get(i).get("createdtime");
                    String lastUpdatedUser = list.get(i).get("lastupdateduser") + "";
                    Date lastUpdatedTime = (Date) list.get(i).get("lastupdatedtime");
                    //set values in section list
                    Section section = new Section();
                    section.setSectionCode(sectionCode);
                    section.setDescription(description);
                    section.setSortKey(sortKey);
                    section.setStatus(status);
                    section.setCreatedTime(createdTime);
                    section.setLastUpdatedUser(lastUpdatedUser);
                    section.setLastUpdatedTime(lastUpdatedTime);
                    sectionList.add(section);
                }
            }
        } catch (EmptyResultDataAccessException ere) {
            return sectionList;
        } catch (Exception e) {
            throw e;
        }
        return sectionList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public Map<String, List<Page>> getUserPageListByUserRoleCode(String userRole) {
        Map<String, List<Page>> pageListMap = new HashMap<String, List<Page>>();
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_USER_PAGELIST, new Object[]{commonVarList.STATUS_ACTIVE, userRole});
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    String description = list.get(i).get("description") + "";
                    String pageCode = list.get(i).get("pagecode") + "";
                    String url = list.get(i).get("url") + "";
                    int sortKey = Integer.parseInt(list.get(i).get("sortkey") + "");
                    String statusCode = list.get(i).get("status") + "";
                    String sectionCode = list.get(i).get("section") + "";
                    String userRoleCode = list.get(i).get("userrole") + "";
                    Date createdTime = (Date) list.get(i).get("createdtime");
                    String lastUpdatedUser = list.get(i).get("lastupdateduser") + "";
                    Date lastUpdatedTime = (Date) list.get(i).get("lastupdatedtime");
                    //set values in page list
                    Page page = new Page();
                    page.setDescription(description);
                    page.setPageCode(pageCode);
                    page.setUrl(url);
                    page.setSortKey(sortKey);
                    page.setStatusCode(statusCode);
                    page.setSectionCode(sectionCode);
                    page.setUserRoleCode(userRoleCode);
                    page.setCreatedTime(createdTime);
                    page.setLastUpdatedUser(lastUpdatedUser);
                    page.setLastUpdatedTime(lastUpdatedTime);
                    //add the values map
                    if (pageListMap.containsKey(page.getSectionCode())) {
                        List<Page> pageList = pageListMap.get(page.getSectionCode());
                        pageList.add(page);
                        pageListMap.put(page.getSectionCode(), pageList);
                    } else {
                        List<Page> pageList = new ArrayList<Page>();
                        pageList.add(page);
                        pageListMap.put(page.getSectionCode(), pageList);
                    }
                }
            }
        } catch (EmptyResultDataAccessException ere) {
            return pageListMap;
        } catch (Exception e) {
            throw e;
        }
        return pageListMap;
    }
}
