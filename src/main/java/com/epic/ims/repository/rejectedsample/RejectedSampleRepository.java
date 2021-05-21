package com.epic.ims.repository.rejectedsample;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.rejectedsample.RejectedSampleDataInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.rejectedsampledata.RejectedSampleData;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;


@Repository
@Scope("request")
public class RejectedSampleRepository {
    private static Logger logger = LogManager.getLogger(RejectedSampleRepository.class);

    @Autowired
    SessionBean sessionBean;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    private final String SQL_GET_COUNT = "select count(*) from reject_data rd where ";

    @LogRepository
    @Transactional(readOnly = true)
    public long getCount(RejectedSampleDataInputBean rejectedSampleDataInputBean) throws Exception {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            //create the where clause
            dynamicClause = this.setDynamicClause(rejectedSampleDataInputBean, dynamicClause);
            //create the query
            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
        } catch (Exception exception) {
            throw exception;
        }
        return count;
    }

    public List<RejectedSampleData> getRejectedSampleSearchList(RejectedSampleDataInputBean rejectedSampleDataInputBean) {
        List<RejectedSampleData> rejectedSampleDataList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(rejectedSampleDataInputBean, new StringBuilder());
            //create sorting order
            String sortingStr = "";
            String col = "";

            switch (rejectedSampleDataInputBean.sortedColumns.get(0)) {
                case 0:
                    col = "rd.referenceno";
                    break;
                case 1:
                    col = "inst.institutioncode";
                    break;
                case 2:
                    col = "rd.name";
                    break;
                case 3:
                    col = "rd.age";
                    break;
                case 4:
                    col = "rd.gender";
                    break;
                case 5:
                    col = "rd.symptomatic";
                    break;
                case 6:
                    col = "rd.contacttype";
                    break;
                case 7:
                    col = "rd.nic";
                    break;
                case 8:
                    col = "rd.address";
                    break;
                case 9:
                    col = "rd.district";
                    break;
                case 10:
                    col = "rd.contactno";
                    break;
                case 11:
                    col = "rd.receiveddate";
                    break;
                case 12:
                    col = "rd.status";
                    break;
                case 13:
                    col = "rd.remark";
                    break;
                default:
                    col = "rd.createdtime";
            }
            sortingStr = " order by " + col + " " + rejectedSampleDataInputBean.sortDirections.get(0);

            String sql = "" +
                    "select rd.referenceno as referenceno, inst.institutioncode as institutioncode, rd.name as name, rd.age as age, rd.gender as gender, " +
                    "rd.symptomatic as symptomatic,rd.contacttype as contacttype,rd.nic as nic, rd.address as address, rd.district as district ,rd.contactno as contactno,rd.secondarycontactno , rd.receiveddate as receiveddate,s.description as status,rd.remark as remark,rd.createduser as createduser , rd.createdtime as createdtime " +
                    "from reject_data rd " +
                    "left join institution inst on inst.institutioncode = rd.institutioncode " +
                    "left join status s on s.code = rd.status where " + dynamicClause.toString() + sortingStr +
                    " limit " + rejectedSampleDataInputBean.displayLength + " offset " + rejectedSampleDataInputBean.displayStart;


            rejectedSampleDataList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                RejectedSampleData rejectedSampleData = new RejectedSampleData();

                try {
                    rejectedSampleData.setReferenceNo(common.handleNullAndEmptyValue(rs.getString("referenceno")));
                } catch (Exception e) {
                    rejectedSampleData.setReferenceNo("--");
                }

                try {
                    rejectedSampleData.setInstitutionCode(common.handleNullAndEmptyValue(rs.getString("institutioncode")));
                } catch (Exception e) {
                    rejectedSampleData.setInstitutionCode("--");
                }

                try {
                    rejectedSampleData.setName(common.handleNullAndEmptyValue(rs.getString("name")));
                } catch (Exception e) {
                    rejectedSampleData.setName("--");
                }

                try {
                    rejectedSampleData.setAge(common.handleNullAndEmptyValue(rs.getString("age")));
                } catch (SQLException e) {
                    rejectedSampleData.setAge("--");
                }

                try {
                    rejectedSampleData.setGender(common.handleNullAndEmptyValue(rs.getString("gender")));
                } catch (Exception e) {
                    rejectedSampleData.setGender("--");
                }

                try {
                    rejectedSampleData.setSymptomatic(common.handleNullAndEmptyValue(rs.getString("symptomatic")));
                } catch (Exception e) {
                    rejectedSampleData.setStatus("--");
                }

                try {
                    rejectedSampleData.setContactType(common.handleNullAndEmptyValue(rs.getString("contacttype")));
                } catch (Exception e) {
                    rejectedSampleData.setContactType("--");
                }

                try {
                    rejectedSampleData.setNic(common.handleNullAndEmptyValue(rs.getString("nic")));
                } catch (Exception e) {
                    rejectedSampleData.setCreatedTime(null);
                }

                try {
                    rejectedSampleData.setAddress(common.handleNullAndEmptyValue(rs.getString("address")));
                } catch (Exception e) {
                    rejectedSampleData.setAddress("--");
                }

                try {
                    rejectedSampleData.setDistrict(common.handleNullAndEmptyValue(rs.getString("district")));
                } catch (Exception e) {
                    rejectedSampleData.setDistrict("--");
                }

                try {
                    rejectedSampleData.setContactNo(common.handleNullAndEmptyValue(rs.getString("contactno")));
                } catch (Exception e) {
                    rejectedSampleData.setContactNo("--");
                }

                try {
                    rejectedSampleData.setSecondaryContactNo(common.handleNullAndEmptyValue(rs.getString("secondarycontactno")));
                } catch (Exception e) {
                    rejectedSampleData.setSecondaryContactNo("--");
                }

                try {
                    rejectedSampleData.setReceivedDate(common.handleNullAndEmptyValue(rs.getString("receiveddate")));
                } catch (Exception e) {
                    rejectedSampleData.setReceivedDate("--");
                }

                try {
                    rejectedSampleData.setStatus(common.handleNullAndEmptyValue(rs.getString("status")));
                } catch (Exception e) {
                    rejectedSampleData.setStatus("--");
                }

                try {
                    rejectedSampleData.setRemark(common.handleNullAndEmptyValue(rs.getString("remark")));
                } catch (Exception e) {
                    rejectedSampleData.setRemark("--");
                }

                try {
                    rejectedSampleData.setCreatedUser(common.handleNullAndEmptyValue(rs.getString("createduser")));
                } catch (Exception e) {
                    rejectedSampleData.setCreatedUser("--");
                }

                try {
                    rejectedSampleData.setCreatedTime(rs.getTimestamp("createdtime"));
                } catch (Exception e) {
                    rejectedSampleData.setCreatedTime(null);
                }

                return rejectedSampleData;
            });
        } catch (Exception exception) {
            throw exception;
        }
        return rejectedSampleDataList;
    }

    private StringBuilder setDynamicClause(RejectedSampleDataInputBean rejectedSampleDataInputBean, StringBuilder dynamicClause) {
        dynamicClause.append("1=1 ");
        try {
            if (rejectedSampleDataInputBean.getReferenceNo() != null && !rejectedSampleDataInputBean.getReferenceNo().isEmpty()) {
                dynamicClause.append("and lower(rd.referenceno) like lower('%").append(rejectedSampleDataInputBean.getReferenceNo()).append("%') ");
            }

            if (rejectedSampleDataInputBean.getInstitutionCode() != null && !rejectedSampleDataInputBean.getInstitutionCode().isEmpty()) {
                dynamicClause.append("and lower(rd.institutioncode) like lower('%").append(rejectedSampleDataInputBean.getInstitutionCode()).append("%') ");
            }

            if (rejectedSampleDataInputBean.getName() != null && !rejectedSampleDataInputBean.getName().isEmpty()) {
                dynamicClause.append("and lower(rd.name) like lower('%").append(rejectedSampleDataInputBean.getName()).append("%') ");
            }

            if (rejectedSampleDataInputBean.getNic() != null && !rejectedSampleDataInputBean.getNic().isEmpty()) {
                dynamicClause.append("and lower(rd.nic) like lower('%").append(rejectedSampleDataInputBean.getNic()).append("%') ");
            }

            if (rejectedSampleDataInputBean.getReceivedDate() != null) {
                dynamicClause.append(" and rd.receiveddate = '").append(rejectedSampleDataInputBean.getReceivedDate()).append("'");
            }
        } catch (Exception exception) {
            throw exception;
        }

        return dynamicClause;
    }
}
