package com.epic.ims.repository.resultupdate;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.resultupdate.ResultUpdateInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.mapping.result.Result;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Scope("prototype")
public class ResultUpdateRepository {
    private static Logger logger = LogManager.getLogger(ResultUpdateRepository.class);

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

    private final String SQL_GET_LIST_DATA_COUNT = "select count(*) from master_data md left outer join status s on s.code=md.status where";

    @LogRepository
    @Transactional(readOnly = true)
    public long getDataCount(ResultUpdateInputBean resultUpdateInputBean) {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_LIST_DATA_COUNT);
            //create the where clause
            dynamicClause = this.setDynamicClause(resultUpdateInputBean, dynamicClause);
            //create the query
            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
        } catch (EmptyResultDataAccessException ere) {
            count = 0;
        } catch (Exception e) {
            throw e;
        }
        return count;
    }


    @LogRepository
    @Transactional(readOnly = true)
    public List<Result> getResultUpdateSearchList(ResultUpdateInputBean resultUpdateInputBean) {
        List<Result> resultList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(resultUpdateInputBean, new StringBuilder());
            //create sorting order
            String sortingStr = "";
            if (resultUpdateInputBean.sortedColumns.get(0) == 0) {
                sortingStr = " order by md.createdtime desc ";
            } else {
                sortingStr = " order by md.createdtime " + resultUpdateInputBean.sortDirections.get(0);
            }

            String sql = "" +
                    " select " +
                    " md.id , md.sampleid, md.referenceno , md.institutioncode , md.name , md.age , md.gender , md.nic , md.address , md.district , md.contactno ,s.description as statusdescription," +
                    " md.serialno , md.contactno , md.serialno , md.specimenid , md.barcode , md.receiveddate ,md.plateid,md.blockvalue,md.ispool,md.poolid, md.createdtime as createdtime,md.createduser as createduser from master_data md " +
                    " left outer join status s on s.code = md.status " +
                    " where " + dynamicClause.toString() + sortingStr +
                    " limit " + resultUpdateInputBean.displayLength + " offset " + resultUpdateInputBean.displayStart;

            resultList = jdbcTemplate.query(sql, (rs, id) -> {
                Result result = new Result();
                try {
                    result.setId(rs.getString("id"));
                } catch (Exception e) {
                    result.setId("--");
                }

                try {
                    result.setReferenceNo(common.handleNullAndEmptyValue(rs.getString("referenceno")));
                } catch (Exception e) {
                    result.setReferenceNo("--");
                }

                try {
                    result.setInstitutionCode(common.handleNullAndEmptyValue(rs.getString("institutioncode")));
                } catch (Exception e) {
                    result.setInstitutionCode("--");
                }

                try {
                    result.setName(common.handleNullAndEmptyValue(rs.getString("name")));
                } catch (Exception e) {
                    result.setName("--");
                }

                try {
                    result.setAge(common.handleNullAndEmptyValue(rs.getString("age")));
                } catch (Exception e) {
                    result.setAge("--");
                }

                try {
                    result.setGender(common.handleNullAndEmptyValue(rs.getString("gender")));
                } catch (Exception e) {
                    result.setGender("--");
                }

//                try {
//                    result.setContactType(common.handleNullAndEmptyValue(rs.getString("contacttype")));
//                } catch (Exception e) {
//                    result.setContactType("--");
//                }

                try {
                    result.setNic(common.handleNullAndEmptyValue(rs.getString("nic")));
                } catch (Exception e) {
                    result.setNic("--");
                }

                try {
                    result.setAddress(common.handleNullAndEmptyValue(rs.getString("address")));
                } catch (Exception e) {
                    result.setAddress("--");
                }

                try {
                    result.setStatus(common.handleNullAndEmptyValue(rs.getString("statusdescription")));
                } catch (Exception e) {
                    result.setStatus("--");
                }

//                try {
//                    result.setResidentDistrict(common.handleNullAndEmptyValue(rs.getString("district")));
//                } catch (Exception e) {
//                    result.setResidentDistrict("--");
//                }
//
//                try {
//                    result.setContactNumber(common.handleNullAndEmptyValue(rs.getString("contactno")));
//                } catch (Exception e) {
//                    result.setContactNumber("--");
//                }
//
//                try {
//                    result.setSecondaryContactNumber(common.handleNullAndEmptyValue(rs.getString("secondarycontactno")));
//                } catch (Exception e) {
//                    result.setSecondaryContactNumber("--");
//                }
//
//                try {
//                    result.setSpecimenid(common.handleNullAndEmptyValue(rs.getString("specimenid")));
//                } catch (Exception e) {
//                    result.setSpecimenid("--");
//                }
//
//                try {
//                    result.setBarcode(common.handleNullAndEmptyValue(rs.getString("barcode")));
//                } catch (Exception e) {
//                    result.setBarcode("--");
//                }

                try {
                    result.setReceivedDate(common.handleNullAndEmptyValue(rs.getString("receiveddate")));
                } catch (Exception e) {
                    result.setReceivedDate("--");
                }

//                try {
//                    result.setCreatedTime(rs.getDate("createdtime"));
//                } catch (Exception e) {
//                    result.setCreatedTime(null);
//                }
//
//                try {
//                    result.setCreatedUser(common.handleNullAndEmptyValue(rs.getString("createduser")));
//                } catch (Exception e) {
//                    result.setCreatedUser("--");
//                }

                return result;
            });
        } catch (EmptyResultDataAccessException ex) {
            return resultList;
        } catch (Exception e) {
            throw e;
        }
        return resultList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public Plate getRecord(String id) {
        Plate plate;
        try {
            return null;
        } catch (EmptyResultDataAccessException erse) {
            plate = null;
        } catch (Exception e) {
            throw e;
        }
        return plate;
    }


    private StringBuilder setDynamicClause(ResultUpdateInputBean resultUpdateInputBean, StringBuilder dynamicClause) {
        dynamicClause.append(" 1=1 and md.status = '").append(commonVarList.STATUS_PLATEASSIGNED).append("'");
        try {
            if (resultUpdateInputBean.getReceivedDate() != null && !resultUpdateInputBean.getReceivedDate().isEmpty()) {
                dynamicClause.append(" and md.receiveddate = '").append(resultUpdateInputBean.getReceivedDate()).append("'");
            }
            if (resultUpdateInputBean.getReferenceNo() != null && !resultUpdateInputBean.getReferenceNo().isEmpty()) {
                dynamicClause.append(" and md.referenceno like '%").append(resultUpdateInputBean.getReferenceNo()).append("%'");
            }
            if (resultUpdateInputBean.getName() != null && !resultUpdateInputBean.getName().isEmpty()) {
                dynamicClause.append(" and md.name like '%").append(resultUpdateInputBean.getName()).append("%'");
            }
            if (resultUpdateInputBean.getNic() != null && !resultUpdateInputBean.getNic().isEmpty()) {
                dynamicClause.append(" and md.nic like '%").append(resultUpdateInputBean.getName()).append("%'");
            }
            if (resultUpdateInputBean.getInstitutionCode() != null && !resultUpdateInputBean.getInstitutionCode().isEmpty()) {
                dynamicClause.append(" and md.institutioncode = '").append(resultUpdateInputBean.getInstitutionCode()).append("'");
            }
            if (resultUpdateInputBean.getPlateId() != null && !resultUpdateInputBean.getPlateId().isEmpty()) {
                dynamicClause.append(" and md.plateid = '").append(resultUpdateInputBean.getPlateId()).append("'");
            }
        } catch (Exception e) {
            throw e;
        }
        return dynamicClause;
    }
}
