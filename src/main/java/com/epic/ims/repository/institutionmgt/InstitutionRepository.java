package com.epic.ims.repository.institutionmgt;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institution.Institution;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@Scope("request")
public class InstitutionRepository {
    private static Logger logger = LogManager.getLogger(InstitutionRepository.class);

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

    private final String SQL_GET_COUNT = "select count(*) from institution i where ";
    private final String SQL_FIND_INSTITUTION = "select institutioncode, name, address, contactno, status,lastupdateduser,lastupdatedtime,createdtime from institution where institutioncode = ?";
    private final String SQL_INSERT_INSTITUTION = "insert into institution(institutioncode, name, address, contactno, status, createduser, createdtime,lastupdateduser, lastupdatedtime) values (?,?,?,?,?,?,?,?,?)";
    private final String SQL_UPDATE_INSTITUTION = "update institution set name = ?, address = ?, contactno = ?, status = ? where institutioncode = ?";
    private final String SQL_DELETE_INSTITUTION = "delete from institution where institutioncode = ?";

    @LogRepository
    @Transactional(readOnly = true)
    public long getCount(InstitutionInputBean institutionInputBean) throws Exception {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            //create the where clause
            dynamicClause = this.setDynamicClause(institutionInputBean, dynamicClause);
            //create the query
            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Institution> getInstitutionSearchList(InstitutionInputBean institutionInputBean) {
        List<Institution> institutionList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(institutionInputBean, new StringBuilder());
            //create sorting order
            String sortingStr = "";
            String col = "";

            switch (institutionInputBean.sortedColumns.get(0)) {
                case 0:
                    col = "i.institutioncode";
                    break;
                case 1:
                    col = "i.name";
                    break;
                case 2:
                    col = "i.address";
                    break;
                case 3:
                    col = "i.contactno";
                    break;
                case 4:
                    col = "s.description";
                    break;
                case 6:
                    col = "i.lastupdatedtime";
                    break;
                case 7:
                    col = "i.lastupdateduser";
                    break;
                default:
                    col = "i.createdtime";
            }
            sortingStr = " order by " + col + " " + institutionInputBean.sortDirections.get(0);

            String sql = "select i.institutioncode as institutioncode, i.address as address, i.name as institutionname, i.contactno as contactnumber, " +
                    "s.description as statusdescription, " +
                    "i.createdtime as createdtime, i.lastupdateduser as lastupdateduser, i.lastupdatedtime as lastupdatedtime " +
                    "from institution i " + "left join status s on s.code = i.status where " + dynamicClause.toString() + sortingStr + " limit " + institutionInputBean.displayLength + " offset " + institutionInputBean.displayStart;

            institutionList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Institution institution = new Institution();
                try {
                    institution.setInstitutionCode(rs.getString("institutioncode"));
                } catch (Exception e) {
                    institution.setInstitutionCode(null);
                }

                try {
                    institution.setInstitutionName(rs.getString("institutionname"));
                } catch (Exception e) {
                    institution.setInstitutionName(null);
                }

                try {
                    institution.setAddress(rs.getString("address"));
                } catch (Exception e) {
                    institution.setAddress(null);
                }

                try {
                    institution.setContactNumber(rs.getString("contactnumber"));
                } catch (Exception e) {
                    institution.setContactNumber(null);
                }

                try {
                    institution.setStatus(rs.getString("statusdescription"));
                } catch (Exception e) {
                    institution.setStatus(null);
                }

                try {
                    institution.setCreatedTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    institution.setCreatedTime(null);
                }

                try {
                    institution.setLastUpdatedTime(rs.getDate("lastupdatedtime"));
                } catch (Exception e) {
                    institution.setLastUpdatedTime(null);
                }

                try {
                    institution.setLastUpdatedUser(rs.getString("lastupdateduser"));
                } catch (Exception e) {
                    institution.setLastUpdatedUser(null);
                }
                return institution;
            });
        } catch (Exception exception) {
            throw exception;
        }
        return institutionList;
    }

    @LogRepository
    @Transactional
    public String insertInstitution(InstitutionInputBean institutionInputBean) throws Exception {
        String message = "";
        try {
            int value = 0;
            //insert query
            value = jdbcTemplate.update(SQL_INSERT_INSTITUTION,
                    institutionInputBean.getInstitutionCode(),
                    institutionInputBean.getInstitutionName(),
                    institutionInputBean.getAddress(),
                    institutionInputBean.getContactNumber(),
                    institutionInputBean.getStatus(),
                    sessionBean.getUsername(),
                    institutionInputBean.getCreatedTime(),
                    institutionInputBean.getLastUpdatedUser(),
                    institutionInputBean.getLastUpdatedTime()
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
    public Institution getInstitution(String institutionCode) throws SQLException {
        Institution institution = null;
        try {
            institution = jdbcTemplate.queryForObject(SQL_FIND_INSTITUTION, new Object[]{institutionCode}, new RowMapper<Institution>() {
                @Override
                public Institution mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Institution institution = new Institution();
                    try {
                        institution.setInstitutionCode(rs.getString("institutioncode"));
                    } catch (Exception e) {
                        institution.setInstitutionCode(null);
                    }

                    try {
                        institution.setInstitutionName(rs.getString("name"));
                    } catch (Exception e) {
                        institution.setInstitutionName(null);
                    }

                    try {
                        institution.setAddress(rs.getString("address"));
                    } catch (Exception e) {
                        institution.setAddress(null);
                    }

                    try {
                        institution.setContactNumber(rs.getString("contactno"));
                    } catch (Exception e) {
                        institution.setContactNumber(null);
                    }

                    try {
                        institution.setStatus(rs.getString("status"));
                    } catch (Exception e) {
                        institution.setStatus(null);
                    }

                    try {
                        institution.setCreatedTime(new Date(rs.getDate("createdtime").getTime()));
                    } catch (Exception e) {
                        institution.setCreatedTime(null);
                    }

                    try {
                        institution.setLastUpdatedTime(new Date(rs.getDate("lastupdatedtime").getTime()));
                    } catch (SQLException e) {
                        institution.setLastUpdatedTime(null);
                    }

                    try {
                        institution.setLastUpdatedUser(rs.getString("lastupdateduser"));
                    } catch (SQLException e) {
                        institution.setLastUpdatedUser(null);
                    }
                    return institution;
                }
            });
        } catch (EmptyResultDataAccessException erse) {
            return institution;
        } catch (Exception e) {
            throw e;
        }
        return institution;
    }

    @LogRepository
    @Transactional
    public String updateInstitution(InstitutionInputBean institutionInputBean) {
        String message = "";
        try {
            int value = jdbcTemplate.update(SQL_UPDATE_INSTITUTION,
                    institutionInputBean.getInstitutionName(),
                    institutionInputBean.getAddress(),
                    institutionInputBean.getContactNumber(),
                    institutionInputBean.getStatus(),
                    institutionInputBean.getInstitutionCode()
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
    @Transactional
    public String deleteInstitution(String institutionCode) {
        String message = "";
        try {
            int value = jdbcTemplate.update(SQL_DELETE_INSTITUTION, institutionCode);
            if (value != 1) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String insertInstitutionBulk(List<InstitutionInputBean> institutionInputBeanList) throws Exception {
        String message = "";
        StringBuilder bulkInsertSql = new StringBuilder("insert into institution(institutioncode, name, address, contactno, status, createduser, createdtime, lastupdateduser, lastupdatedtime) values");
        try {
            int value = 0;
            int listSize = institutionInputBeanList.size();

            String SQL_INSERT_INSTITUTION_BULK = createBulkInsertClause(institutionInputBeanList, bulkInsertSql);

            value = jdbcTemplate.update(SQL_INSERT_INSTITUTION_BULK);
            if (value != listSize) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (DuplicateKeyException ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    private StringBuilder setDynamicClause(InstitutionInputBean institutionInputBean, StringBuilder dynamicClause) {
        dynamicClause.append("1=1 ");
        try {
            if (institutionInputBean.getInstitutionName() != null && !institutionInputBean.getInstitutionName().isEmpty()) {
                dynamicClause.append("and lower(i.name) like lower('%").append(institutionInputBean.getInstitutionName()).append("%') ");
            }

            if (institutionInputBean.getInstitutionCode() != null && !institutionInputBean.getInstitutionCode().isEmpty()) {
                dynamicClause.append("and lower(i.institutionCode) like lower('%").append(institutionInputBean.getInstitutionCode()).append("%') ");
            }

            if (institutionInputBean.getContactNumber() != null && !institutionInputBean.getContactNumber().isEmpty()) {
                dynamicClause.append("and lower(i.contactno) like lower('%").append(institutionInputBean.getContactNumber()).append("%') ");
            }

            if (institutionInputBean.getStatus() != null && !institutionInputBean.getStatus().isEmpty()) {
                dynamicClause.append("and i.status like '%").append(institutionInputBean.getStatus()).append("%' ");
            }
        } catch (Exception exception) {
            throw exception;
        }
        return dynamicClause;
    }

    private String createBulkInsertClause(List<InstitutionInputBean> institutionInputBeanList, StringBuilder bulkInsertSql) {
        try {
            int count = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

            InstitutionInputBean institutionInputBean = institutionInputBeanList.get(0);
            int listLength = institutionInputBeanList.size();

            String formattedCCreatedTime = formatter.format(institutionInputBean.getCreatedTime());
            String formattedCLastUpdatedTime = formatter.format(institutionInputBean.getLastUpdatedTime());

            String status = institutionInputBean.getStatus();
            String createdUser = sessionBean.getUsername();
            String lastUpdatedUser = institutionInputBean.getLastUpdatedUser();

            String commonValues = "'" + status + "', " + "'" + createdUser + "', " + "'" + formattedCCreatedTime + "', " + "'" + lastUpdatedUser + "', " + "'" + formattedCLastUpdatedTime + "'";
            for (InstitutionInputBean inputBean : institutionInputBeanList) {
                count++;

                bulkInsertSql.append("('");
                bulkInsertSql.append(inputBean.getInstitutionCode()).append("', '");
                bulkInsertSql.append(inputBean.getInstitutionName()).append("', '");
                bulkInsertSql.append(inputBean.getAddress()).append("', '");
                bulkInsertSql.append(inputBean.getContactNumber()).append("', ");
                bulkInsertSql.append(commonValues);
                bulkInsertSql.append(")");

                if (count != listLength) {
                    bulkInsertSql.append(",");
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return bulkInsertSql.toString();
    }
}
