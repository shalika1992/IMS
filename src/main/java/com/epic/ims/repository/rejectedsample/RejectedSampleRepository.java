package com.epic.ims.repository.rejectedsample;


import com.epic.ims.bean.rejectedsample.RejectedSampleDataInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.rejectedsampledata.RejectedSampleData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    SessionBean sessionBean;

    @Autowired
    JdbcTemplate jdbcTemplate;


    private final String SQL_GET_COUNT = "select count(*) from reject_data rd where ";

    @Transactional(readOnly = true)
    public long getCount(RejectedSampleDataInputBean rejectedSampleDataInputBean) throws Exception {

        long count = 0;

        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            this.setDynamicClause(rejectedSampleDataInputBean, dynamicClause);

            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);

        } catch (Exception exception) {
            logger.error(exception);
            throw exception;
        }

        return count;
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
                dynamicClause.append("and rd.receiveddate like '%").append(rejectedSampleDataInputBean.getReceivedDate()).append("%' ");
            }

        } catch (Exception exception) {
            throw exception;
        }

        return dynamicClause;
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

            String sql = "select rd.referenceno as referenceno, inst.institutioncode as institutioncode, rd.name as name, rd.age as age, rd.gender as gender, " +
                    "rd.symptomatic as symptomatic,rd.contacttype as contacttype," +
                    "rd.nic as nic, rd.address as address, rd.district as district ,rd.contactno as contactno,rd.receiveddate as receiveddate,rd.status as status,rd.remark as remark,rd.createdtime as createdtime " +
                    "from reject_data rd " +
                    "left join institution inst on inst.institutioncode = rd.institutioncode " +
                    "left join status s on s.code = rd.status where " + dynamicClause.toString() + sortingStr +
                    " limit " + rejectedSampleDataInputBean.displayLength + " offset " + rejectedSampleDataInputBean.displayStart;


            rejectedSampleDataList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                RejectedSampleData rejectedSampleData = new RejectedSampleData();

                try {
                    rejectedSampleData.setReferenceNo(rs.getString("referenceno"));
                } catch (Exception e) {
                    rejectedSampleData.setReferenceNo(null);
                }

                try {
                    rejectedSampleData.setInstitutionCode(rs.getString("institutioncode"));
                } catch (Exception e) {
                    rejectedSampleData.setInstitutionCode(null);
                }

                try {
                    rejectedSampleData.setName(rs.getString("name"));
                } catch (Exception e) {
                    rejectedSampleData.setName(null);
                }

                try {
                    rejectedSampleData.setAge(rs.getString("age"));
                } catch (SQLException e) {
                    rejectedSampleData.setAge(null);
                }

                try {
                    rejectedSampleData.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    rejectedSampleData.setGender(null);
                }

                try {
                    rejectedSampleData.setSymptomatic(rs.getString("symptomatic"));
                } catch (Exception e) {
                    rejectedSampleData.setStatus(null);
                }

                try {
                    rejectedSampleData.setContactType(rs.getString("contacttype"));
                } catch (Exception e) {
                    rejectedSampleData.setContactType(null);
                }

                try {
                    rejectedSampleData.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    rejectedSampleData.setCreatedTime(null);
                }

                try {
                    rejectedSampleData.setAddress(rs.getString("address"));
                } catch (Exception e) {
                    rejectedSampleData.setAddress(null);
                }

                try {
                    rejectedSampleData.setDistrict(rs.getString("district"));
                } catch (Exception e) {
                    rejectedSampleData.setDistrict(null);
                }

                try {
                    rejectedSampleData.setContactNo(rs.getString("contactno"));
                } catch (Exception e) {
                    rejectedSampleData.setContactNo(null);
                }

                try {
                    rejectedSampleData.setReceivedDate(rs.getDate("receiveddate"));
                } catch (Exception e) {
                    rejectedSampleData.setReceivedDate(null);
                }

                try {
                    rejectedSampleData.setStatus(rs.getString("status"));
                } catch (Exception e) {
                    rejectedSampleData.setStatus(null);
                }

                try {
                    rejectedSampleData.setRemark(rs.getString("remark"));
                } catch (Exception e) {
                    rejectedSampleData.setRemark(null);
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


}
