package com.epic.ims.repository.resultupdate;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.plate.ResultBean;
import com.epic.ims.bean.resultupdate.ResultIdListBean;
import com.epic.ims.bean.resultupdate.ResultPlateBean;
import com.epic.ims.bean.resultupdate.ResultUpdateInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.mapping.result.Result;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_GET_LIST_DATA_COUNT = "select count(*) from master_data md left outer join status s on s.code=md.status where";
    private final String SQL_UPDATE_LIST_DETECTED = "update master_data set status =:status , isverified=:isverified , iscomplete=:iscomplete , result=:result where id in (:ids)";
    private final String SQL_UPDATE_LIST_NOTDETECTED = "update master_data set status =:status , isverified=:isverified , iscomplete=:iscomplete , result=:result where id in (:ids)";
    private final String SQL_UPDATE_LIST_PENDING = "update master_data set status =:status , isverified=:isverified , iscomplete=:iscomplete , result=:result where id in (:ids)";
    //private final String SQL_GET_MASTER_RESULT_PLATE_LIST = "select @n := @n + 1 n,GROUP_CONCAT(id SEPARATOR '|') as id,GROUP_CONCAT(referenceno SEPARATOR '|') as referenceno,GROUP_CONCAT(name SEPARATOR '|') as name,GROUP_CONCAT(nic SEPARATOR '|') as nic,barcode,plateid,blockvalue,ispool,iscomplete,result from master_data, (select @n := -1) m where plateid = ? and createdtime = ? group by barcode , plateid , blockvalue , ispool order by barcode";
    private final String SQL_GET_MASTER_RESULT_PLATE_LIST = "" +
            "select m.id,m.referenceno,m.name,m.nic,m.barcode,m.plateid,m.blockvalue,m.ispool,m.iscomplete,m.result from " +
            "(select GROUP_CONCAT(id SEPARATOR '|') as id, " +
            "GROUP_CONCAT(referenceno SEPARATOR '|') as referenceno, GROUP_CONCAT(name SEPARATOR '|') as name,GROUP_CONCAT(nic SEPARATOR '|') as nic," +
            "barcode,plateid,blockvalue,ispool ,iscomplete,result  from master_data as mt where plateid = ? and createdtime = ? " +
            "group by barcode , plateid , blockvalue , ispool , iscomplete,result ) as m " +
            "inner join excelblock e on m.blockvalue = e. code order by m.plateid , cast(e.indexvalue as unsigned)";

    private final String SQL_UPDATE_MASTER_RESULT = "update master_data set status =:status, isverified=:isverified, iscomplete=:iscomplete , result=:result, ct_target1=:ct1, ct_target2=:ct2 where barcode =:barcode";
    private final String SQL_UPDATE_MASTER_RESULT_WITHOUT_CT = "update master_data set status =:status, isverified=:isverified, iscomplete=:iscomplete , result=:result where barcode =:barcode";

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
    public Map<Integer, List<ResultBean>> getMasterResultPlateList(int plateid, String receivedDate) {
        Map<Integer, List<ResultBean>> masterPlateMap = new HashMap<>();
        try {
            jdbcTemplate.query(SQL_GET_MASTER_RESULT_PLATE_LIST, new Object[]{plateid, receivedDate}, (ResultSet rs) -> {
                int counter = 0;
                while (rs.next()) {
                    masterPlateMap.put(counter, Arrays.asList(
                            new ResultBean(
                                    rs.getString("id").split("\\|"),
                                    rs.getString("referenceno").split("\\|"),
                                    rs.getString("name").split("\\|"),
                                    rs.getString("nic").split("\\|"),
                                    rs.getString("barcode"),
                                    rs.getString("plateid"),
                                    rs.getString("blockvalue"),
                                    rs.getString("ispool"),
                                    rs.getString("iscomplete"),
                                    rs.getString("result")
                            )
                    ));
                    counter++;
                }
                return masterPlateMap;
            });
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
            return masterPlateMap;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
        return masterPlateMap;
    }

    @LogRepository
    @Transactional
    public String updateMasterResult(ResultPlateBean resultPlateBean) {
        String message = "";
        try {
            //create the parameter map
            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
            if (resultPlateBean.getResultId().equals(commonVarList.RESULT_CODE_DETECTED)) {
                idSetParameterMap.addValue("status", commonVarList.STATUS_COMPLETED);
                idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_DETECTED);
                idSetParameterMap.addValue("ct1", resultPlateBean.getCt1());
                idSetParameterMap.addValue("ct2", resultPlateBean.getCt2());
            } else if (resultPlateBean.getResultId().equals(commonVarList.RESULT_CODE_NOTDETECTED)) {
                idSetParameterMap.addValue("status", commonVarList.STATUS_COMPLETED);
                idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_NOTDETECTED);
            } else if (resultPlateBean.getResultId().equals(commonVarList.RESULT_CODE_INCONCLUSIVE)) {
                idSetParameterMap.addValue("status", commonVarList.STATUS_COMPLETED);
                idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_INCONCLUSIVE);
            } else if (resultPlateBean.getResultId().equals(commonVarList.RESULT_CODE_REPEATED)) {
                idSetParameterMap.addValue("status", commonVarList.STATUS_REPEATED);
                idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_REPEATED);
            }
            idSetParameterMap.addValue("isverified", 1);
            idSetParameterMap.addValue("iscomplete", 1);
            idSetParameterMap.addValue("barcode", resultPlateBean.getBarcode());
            //execute the query
            int value;
            if (resultPlateBean.getResultId().equals(commonVarList.RESULT_CODE_DETECTED)) {
                value = namedParameterJdbcTemplate.update(SQL_UPDATE_MASTER_RESULT, idSetParameterMap);
            } else {
                value = namedParameterJdbcTemplate.update(SQL_UPDATE_MASTER_RESULT_WITHOUT_CT, idSetParameterMap);
            }
            if (value <= 0) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
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
                sortingStr = " order by md.sampleid asc ";
            } else {
                sortingStr = " order by md.sampleid " + resultUpdateInputBean.sortDirections.get(0);
            }

            String sql = "" +
                    " select " +
                    " md.id , md.sampleid, md.referenceno , md.institutioncode , md.name , md.age , md.gender , md.nic , md.address , md.district , md.contactno ,s.description as statusdescription," +
                    " md.serialno , md.specimenid , md.barcode , md.receiveddate ,md.plateid,md.blockvalue,md.ispool,md.poolid, md.createdtime as createdtime,md.createduser as createduser from master_data md " +
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
                    result.setSampleId(common.handleNullAndEmptyValue(rs.getString("sampleid")));
                } catch (Exception e) {
                    result.setSampleId("--");
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
                    result.setDistrict(common.handleNullAndEmptyValue(rs.getString("district")));
                } catch (Exception e) {
                    result.setDistrict("--");
                }

                try {
                    result.setContactNo(common.handleNullAndEmptyValue(rs.getString("contactno")));
                } catch (Exception e) {
                    result.setContactNo("--");
                }

                try {
                    result.setStatus(common.handleNullAndEmptyValue(rs.getString("statusdescription")));
                } catch (Exception e) {
                    result.setStatus("--");
                }

                try {
                    result.setPlateId(rs.getInt("plateid") + "");
                } catch (Exception e) {
                    result.setPlateId("--");
                }

                try {
                    result.setBlockValue(common.handleNullAndEmptyValue(rs.getString("blockvalue")));
                } catch (Exception e) {
                    result.setBlockValue("--");
                }

                try {
                    result.setPool(rs.getBoolean("ispool"));
                } catch (Exception e) {
                    result.setPool(false);
                }

                try {
                    result.setPoolId(rs.getInt("poolid") + "");
                } catch (Exception e) {
                    result.setPoolId("--");
                }

                try {
                    result.setStatus(common.handleNullAndEmptyValue(rs.getString("statusdescription")));
                } catch (Exception e) {
                    result.setStatus("--");
                }


                try {
                    result.setSerialNo(common.handleNullAndEmptyValue(rs.getString("serialno")));
                } catch (Exception e) {
                    result.setSerialNo("--");
                }

                try {
                    result.setSpecimenId(common.handleNullAndEmptyValue(rs.getString("specimenid")));
                } catch (Exception e) {
                    result.setSpecimenId("--");
                }

                try {
                    result.setBarCode(common.handleNullAndEmptyValue(rs.getString("barcode")));
                } catch (Exception e) {
                    result.setBarCode("--");
                }

                try {
                    result.setReceivedDate(common.handleNullAndEmptyValue(rs.getString("receiveddate")));
                } catch (Exception e) {
                    result.setReceivedDate("--");
                }

                try {
                    result.setCreatedDateTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    result.setCreatedDateTime(null);
                }

                try {
                    result.setCreatedUser(common.handleNullAndEmptyValue(rs.getString("createduser")));
                } catch (Exception e) {
                    result.setCreatedUser("--");
                }

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

    @LogRepository
    @Transactional
    public String markAsDetected(ResultIdListBean resultIdListBean) {
        String message = "";
        try {
            //create the parameter map
            Set<Integer> idSet = Arrays.stream(resultIdListBean.getIdList()).boxed().collect(Collectors.toSet());
            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
            idSetParameterMap.addValue("status", commonVarList.STATUS_COMPLETED);
            idSetParameterMap.addValue("isverified", 1);
            idSetParameterMap.addValue("iscomplete", 1);
            idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_DETECTED);
            idSetParameterMap.addValue("ids", idSet);
            //execute the query
            int value = namedParameterJdbcTemplate.update(SQL_UPDATE_LIST_DETECTED, idSetParameterMap);
            if (value <= 0) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String markAsNotDetected(ResultIdListBean resultIdListBean) {
        String message = "";
        try {
            //create the parameter map
            Set<Integer> idSet = Arrays.stream(resultIdListBean.getIdList()).boxed().collect(Collectors.toSet());
            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
            idSetParameterMap.addValue("status", commonVarList.STATUS_COMPLETED);
            idSetParameterMap.addValue("isverified", 1);
            idSetParameterMap.addValue("iscomplete", 1);
            idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_NOTDETECTED);
            idSetParameterMap.addValue("ids", idSet);
            //execute the query
            int value = namedParameterJdbcTemplate.update(SQL_UPDATE_LIST_NOTDETECTED, idSetParameterMap);
            if (value <= 0) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String markAsRepeated(ResultIdListBean resultIdListBean) {
        String message = "";
        try {
            //create the parameter map
            Set<Integer> idSet = Arrays.stream(resultIdListBean.getIdList()).boxed().collect(Collectors.toSet());
            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
            idSetParameterMap.addValue("status", commonVarList.STATUS_REPEATED);
            idSetParameterMap.addValue("isverified", 1);
            idSetParameterMap.addValue("iscomplete", 1);
            idSetParameterMap.addValue("result", commonVarList.RESULT_CODE_PENDING);
            idSetParameterMap.addValue("ids", idSet);
            //execute the query
            int value = namedParameterJdbcTemplate.update(SQL_UPDATE_LIST_PENDING, idSetParameterMap);
            if (value <= 0) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
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
