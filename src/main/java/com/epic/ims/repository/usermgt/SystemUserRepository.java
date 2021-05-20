package com.epic.ims.repository.usermgt;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.bean.sysuser.SystemUserInputBean;
import com.epic.ims.mapping.user.usermgt.SystemUser;
import com.epic.ims.service.common.CommonService;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
@Scope("request")
public class SystemUserRepository {
    private static Logger logger = LogManager.getLogger(SystemUserRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonService commonService;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    MessageSource messageSource;


    private final String SQL_GET_COUNT = "select count(*) from web_systemuser wu where ";
    private final String SQL_INSERT_SYSTEMUSER = "insert into web_systemuser(username, password, userrole, expirydate, fullname, email, mobile, initialloginstatus, status, createduser, createdtime, lastupdateduser, lastupdatedtime) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String SQL_FIND_SYSTEMUSER = "select username, password, userrole, expirydate, fullname, email, mobile,noofinvalidattempt, lastloggeddate,initialloginstatus,status,lastupdateduser,lastupdatedtime,createdtime from web_systemuser where username = ?";
    private final String SQL_UPDATE_SYSTEMUSER = "update web_systemuser set userrole = ? , fullname = ? , email = ? , mobile = ? , status = ? where username = ?";
    private final String SQL_CHANGE_PASSWORD = "update web_systemuser set password = ? where username = ?";

    @LogRepository
    @Transactional(readOnly = true)
    public long getCount(SystemUserInputBean systemUserInputBean) throws Exception {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            //create the where clause
            this.setDynamicClause(systemUserInputBean, dynamicClause);
            //create the query
            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
        } catch (Exception exception) {
            logger.error(exception);
            throw exception;
        }
        return count;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<SystemUser> getSystemUserSearchList(SystemUserInputBean systemUserInputBean) {
        List<SystemUser> systemUserList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(systemUserInputBean, new StringBuilder());
            //create sorting order
            String sortingStr = "";
            String col = "";

            switch (systemUserInputBean.sortedColumns.get(0)) {
                case 0:
                    col = "wu.username";
                    break;
                case 1:
                    col = "wu.fullname";
                    break;
                case 2:
                    col = "ur.description";
                    break;
                case 3:
                    col = "wu.email";
                    break;
                case 4:
                    col = "wu.mobile";
                    break;
                case 5:
                    col = "wu.lastloggeddate";
                    break;
                case 6:
                    col = "s.description";
                    break;
                case 7:
                    col = "wu.createdtime";
                    break;
                case 8:
                    col = "wu.lastupdatedtime";
                    break;
                case 9:
                    col = "wu.lastupdateduser";
                    break;
                default:
                    col = "wu.createdtime";
            }
            sortingStr = " order by " + col + " " + systemUserInputBean.sortDirections.get(0);

            String sql = "select wu.username as username, ur.description as userroledescription, wu.fullname as fullname, wu.email as email, wu.mobile as mobile, " +
                    "s.description as statusdescription, wu.lastloggeddate as lastloggeddate," +
                    "wu.createdtime as createdtime, wu.lastupdateduser as lastupdateduser, wu.lastupdatedtime as lastupdatedtime " +
                    "from web_systemuser wu " +
                    "left join userrole ur on ur.userrolecode = wu.userrole " +
                    "left join status s on s.code = wu.status where " + dynamicClause.toString() + sortingStr +
                    " limit " + systemUserInputBean.displayLength + " offset " + systemUserInputBean.displayStart;


            systemUserList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                SystemUser systemUser = new SystemUser();
                try {
                    systemUser.setUserName(rs.getString("username"));
                } catch (Exception e) {
                    systemUser.setUserName(null);
                }

                try {
                    systemUser.setUserRole(rs.getString("userroledescription"));
                } catch (Exception e) {
                    systemUser.setUserRole(null);
                }

                try {
                    systemUser.setFullName(rs.getString("fullname"));
                } catch (Exception e) {
                    systemUser.setFullName(null);
                }

                try {
                    systemUser.setEmail(rs.getString("email"));
                } catch (SQLException e) {
                    systemUser.setEmail(null);
                }

                try {
                    systemUser.setMobileNumber(rs.getString("mobile"));
                } catch (Exception e) {
                    systemUser.setMobileNumber(null);
                }

                try {
                    systemUser.setStatus(rs.getString("statusdescription"));
                } catch (Exception e) {
                    systemUser.setStatus(null);
                }

                try {
                    systemUser.setLastLoggedDate(rs.getDate("lastloggeddate"));
                } catch (Exception e) {
                    systemUser.setLastLoggedDate(null);
                }

                try {
                    systemUser.setCreatedTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    systemUser.setCreatedTime(null);
                }

                try {
                    systemUser.setLastUpdatedTime(rs.getDate("lastupdatedtime"));
                } catch (Exception e) {
                    systemUser.setLastUpdatedTime(null);
                }

                try {
                    systemUser.setLastUpdatedUser(rs.getString("lastupdateduser"));
                } catch (Exception e) {
                    systemUser.setLastUpdatedUser(null);
                }
                return systemUser;
            });
        } catch (Exception exception) {
            throw exception;
        }
        return systemUserList;
    }

    @LogRepository
    @Transactional
    public String insertSystemUser(SystemUserInputBean systemUserInputBean) throws Exception {
        String message = "";
        try {
            int value = 0;
            Date userPasswordExpiryDate = commonService.getPasswordExpiryDate();
            //set default values to system user input bean
            systemUserInputBean.setExpiryDate(userPasswordExpiryDate);
            systemUserInputBean.setInitialLoginStatus(0);
            //insert query
            value = jdbcTemplate.update(SQL_INSERT_SYSTEMUSER,
                    systemUserInputBean.getUserName().trim(),
                    systemUserInputBean.getPassword(),
                    systemUserInputBean.getUserRoleCode(),
                    systemUserInputBean.getExpiryDate(),
                    systemUserInputBean.getFullName(),
                    systemUserInputBean.getEmail(),
                    systemUserInputBean.getMobileNumber(),
                    systemUserInputBean.getInitialLoginStatus(),
                    systemUserInputBean.getStatus(),
                    sessionBean.getUsername(),
                    systemUserInputBean.getCreatedTime(),
                    systemUserInputBean.getLastUpdatedUser(),
                    systemUserInputBean.getLastUpdatedTime()
            );

            if (value != 1) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (DuplicateKeyException ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public SystemUser getSystemUser(String userName) throws SQLException {
        SystemUser systemUser = null;
        try {
            systemUser = jdbcTemplate.queryForObject(SQL_FIND_SYSTEMUSER, new Object[]{userName}, new RowMapper<SystemUser>() {
                @Override
                public SystemUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SystemUser systemUser = new SystemUser();
                    try {
                        systemUser.setUserName(rs.getString("username"));
                    } catch (Exception e) {
                        systemUser.setUserName(null);
                    }

                    try {
                        systemUser.setFullName(rs.getString("fullname"));
                    } catch (Exception e) {
                        systemUser.setFullName(null);
                    }

                    try {
                        systemUser.setUserRoleCode(rs.getString("userrole"));
                    } catch (Exception e) {
                        systemUser.setUserRoleCode(null);
                    }

                    try {
                        systemUser.setEmail(rs.getString("email"));
                    } catch (Exception e) {
                        systemUser.setEmail(null);
                    }

                    try {
                        systemUser.setMobileNumber(rs.getString("mobile"));
                    } catch (Exception e) {
                        systemUser.setMobileNumber(null);
                    }

                    try {
                        systemUser.setNoOfInvalidAttempt(rs.getInt("noofinvalidattempt"));
                    } catch (Exception e) {
                        systemUser.setNoOfInvalidAttempt(0);
                    }

                    try {
                        systemUser.setExpiryDate(new Date(rs.getDate("expirydate").getTime()));
                    } catch (Exception e) {
                        systemUser.setExpiryDate(null);
                    }

                    try {
                        systemUser.setLastLoggedDate(new Date(rs.getDate("lastloggeddate").getTime()));
                    } catch (Exception e) {
                        systemUser.setLastLoggedDate(null);
                    }

                    try {
                        systemUser.setStatus(rs.getString("status"));
                    } catch (Exception e) {
                        systemUser.setStatus(null);
                    }

                    try {
                        systemUser.setCreatedTime(new Date(rs.getDate("createdtime").getTime()));
                    } catch (Exception e) {
                        systemUser.setCreatedTime(null);
                    }

                    try {
                        systemUser.setLastUpdatedTime(new Date(rs.getDate("lastupdatedtime").getTime()));
                    } catch (SQLException e) {
                        systemUser.setLastUpdatedTime(null);
                    }

                    try {
                        systemUser.setLastUpdatedUser(rs.getString("lastupdateduser"));
                    } catch (SQLException e) {
                        systemUser.setLastUpdatedUser(null);
                    }
                    return systemUser;
                }
            });
        } catch (EmptyResultDataAccessException erse) {
            systemUser = null;
        } catch (Exception e) {
            throw e;
        }
        return systemUser;
    }

    @LogRepository
    @Transactional
    public String updateSystemUser(SystemUserInputBean systemUserInputBean) {
        String message = "";
        try {
            int value = jdbcTemplate.update(SQL_UPDATE_SYSTEMUSER,
                    systemUserInputBean.getUserRoleCode(),
                    systemUserInputBean.getFullName(),
                    systemUserInputBean.getEmail(),
                    systemUserInputBean.getMobileNumber(),
                    systemUserInputBean.getStatus(),
                    systemUserInputBean.getUserName()
            );

            if (value != 1) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public String checkForSamePassword(SystemUserInputBean systemUserInputBean) {
        String message = "";
        String GET_COUNT_BY_USERNAME_PWD = "select count(username) from web_systemuser where username=? and password=?";
        try {
            String username = systemUserInputBean.getUserName();
            String password = systemUserInputBean.getPassword();

            long count = jdbcTemplate.queryForObject(GET_COUNT_BY_USERNAME_PWD, new Object[]{username, password}, Long.class);

            if (count != 0) {
                message = MessageVarList.PASSWORD_SAME_AS_PREVIOUS;
            }
        } catch (Exception exception) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String changePassword(SystemUserInputBean systemUserInputBean) {
        String message = "";
        try {
            int value = jdbcTemplate.update(SQL_CHANGE_PASSWORD, systemUserInputBean.getPassword(), systemUserInputBean.getUserName());
            if (value != 1) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception exception) {
            throw exception;
        }
        return message;
    }

    private StringBuilder setDynamicClause(SystemUserInputBean systemUserInputBean, StringBuilder dynamicClause) {
        dynamicClause.append("1=1 and wu.username != '").append(sessionBean.getUsername()).append("' ");
        try {
            if (systemUserInputBean.getUserName() != null && !systemUserInputBean.getUserName().isEmpty()) {
                dynamicClause.append("and lower(wu.username) like lower('%").append(systemUserInputBean.getUserName()).append("%') ");
            }

            if (systemUserInputBean.getFullName() != null && !systemUserInputBean.getFullName().isEmpty()) {
                dynamicClause.append("and lower(wu.fullname) like lower('%").append(systemUserInputBean.getFullName()).append("%') ");
            }

            if (systemUserInputBean.getEmail() != null && !systemUserInputBean.getEmail().isEmpty()) {
                dynamicClause.append("and lower(wu.email) like lower('%").append(systemUserInputBean.getEmail()).append("%') ");
            }

            if (systemUserInputBean.getMobileNumber() != null && !systemUserInputBean.getMobileNumber().isEmpty()) {
                dynamicClause.append("and lower(wu.mobile) like lower('%").append(systemUserInputBean.getMobileNumber()).append("%') ");
            }

            if (systemUserInputBean.getUserRoleCode() != null && !systemUserInputBean.getUserRoleCode().isEmpty()) {
                dynamicClause.append("and wu.userrole like '%").append(systemUserInputBean.getUserRoleCode()).append("%' ");
            }

            if (systemUserInputBean.getStatus() != null && !systemUserInputBean.getStatus().isEmpty()) {
                dynamicClause.append("and wu.status like '%").append(systemUserInputBean.getStatus()).append("%' ");
            }
        } catch (Exception exception) {
            throw exception;
        }
        return dynamicClause;
    }
}
