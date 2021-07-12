package com.epic.ims.repository.common;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.common.CommonInstitution;
import com.epic.ims.bean.common.Result;
import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.resultupdate.ResultPlateBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.district.District;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.mapping.plate.ResultPlate;
import com.epic.ims.mapping.reportmgt.MasterData;
import com.epic.ims.mapping.result.ResultType;
import com.epic.ims.mapping.user.usermgt.UserRole;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Scope("prototype")
public class CommonRepository {
    private static Logger logger = LogManager.getLogger(CommonRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    CommonVarList commonVarList;

    private final String SQL_GET_STATUS_LIST_BY_CATEGORY = "select code, description from status where statuscategory=?";
    private final String SQL_GET_STATUS_LIST_BY_CATEGORIES = "select code, description from status where statuscategory = ? and code in (?,?)";
    private final String SQL_GET_USERROLE_LIST = "select userrolecode, description, status,createdtime, lastupdatedtime, lastupdateduser from userrole";
    private final String SQL_GET_DISTRICT_LIST = "select code, description from district order by description asc";
    private final String SQL_GET_INSTITUTION_LIST = "select institutioncode as code, name from institution where status=? order by name asc";
    private final String SQL_GETALL_INSTITUTION_LIST = "select * from institution";
    private final String SQL_GET_PLATE_LIST = "select id , code , receiveddate , createddate from plate where receiveddate = ? order by id asc";
    private final String SQL_GET_MAX_PLATE = "select max(distinct plateid) as max_plate_id from master_temp_data where receiveddate = ?";
    private final String SQL_GET_RESULT_TYPE_LIST = "select code , description from result order by code asc";
    private final String SQL_SYSTEM_TIME = "select SYSDATE() as currentdate";
    private final String SQL_SYSTEM_DATE = "select CURDATE() as currentdate";
    private final String SQL_USERROLE_STATUS_BY_USERROLECODE = "select status from userrole where userrolecode=?";
    private final String SQL_USERPARAM_BY_PARAMCODE = "select value from passwordparam where passwordparam = ?";
    private final String SQL_GET_RESULT_LIST = "select code,description from result";
    private final String SQL_GET_STATUS_LIST_SAMPLEVERIDY = "select code, description from status where code in (?, ?)";
    private final String SQL_GET_STATUS_LIST_REPORT = "select code, description from status where code in (?, ?, ?)";
    private final String SQL_GET_RESULT_PLATE = " select sampleid,    " +
                                                " IFNULL(ct_target1,'NA') as ct_target1," +
                                                " IFNULL(ct_target2,'NA') as ct_target2," +
                                                " IFNULL(rejectremark,'NA') as rejectremark, " +
                                                " IFNULL(result,'NA') as result " +
                                                " from master_data where barcode = ? ";
    @LogRepository
    @Transactional(readOnly = true)
    public List<Status> getStatusList(String statusCategory) throws Exception {
        List<Status> statusBeanList;
        try {
            List<Map<String, Object>> statusList = jdbcTemplate.queryForList(SQL_GET_STATUS_LIST_BY_CATEGORIES, statusCategory, commonVarList.STATUS_ACTIVE, commonVarList.STATUS_DEACTIVE);
            statusBeanList = statusList.stream().map((record) -> {
                Status statusBean = new Status();
                statusBean.setStatusCode(record.get("code").toString());
                statusBean.setDescription(record.get("description").toString());
                return statusBean;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            statusBeanList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return statusBeanList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public String getCurrentDateTimeAsString() throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAsString = "";
        try {
            Map<String, Object> currentDate = jdbcTemplate.queryForMap(SQL_SYSTEM_TIME);
            Date formattedCurrentDate = formatter.parse(currentDate.get("currentdate").toString());
            currentDateAsString = formatter.format(formattedCurrentDate);
        } catch (Exception e) {
            throw e;
        }
        return currentDateAsString;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public String getCurrentDateAsString() throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateAsString = "";
        try {
            Map<String, Object> currentDate = jdbcTemplate.queryForMap(SQL_SYSTEM_TIME);
            Date formattedCurrentDate = formatter.parse(currentDate.get("currentdate").toString());
            currentDateAsString = formatter.format(formattedCurrentDate);
        } catch (Exception e) {
            throw e;
        }
        return currentDateAsString;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public String getCurrentDateAsYYYYMMDD() throws Exception {
        SimpleDateFormat formatterOne = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatterTwo = new SimpleDateFormat("yyyyMMdd");
        String currentDateAsString = "";
        try {
            Map<String, Object> currentDate = jdbcTemplate.queryForMap(SQL_SYSTEM_DATE);
            String date = currentDate.get("currentdate").toString();
            Date formattedCurrentDate = formatterOne.parse(date);
            currentDateAsString = formatterTwo.format(formattedCurrentDate);
        } catch (Exception e) {
            throw e;
        }
        return currentDateAsString;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public Date getCurrentDate() throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date formattedCurrentDate = null;
        try {
            Map<String, Object> currentDate = jdbcTemplate.queryForMap(SQL_SYSTEM_TIME);
            formattedCurrentDate = formatter.parse(currentDate.get("currentdate").toString());
        } catch (Exception e) {
            throw e;
        }
        return formattedCurrentDate;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<UserRole> getUserRoleList() throws Exception {
        List<UserRole> userroleList;
        try {
            List<Map<String, Object>> userRoleList = jdbcTemplate.queryForList(SQL_GET_USERROLE_LIST);
            userroleList = userRoleList.stream().map((record) -> {
                UserRole userrole = new UserRole();
                userrole.setUserroleCode(record.get("userrolecode").toString());
                userrole.setDescription(record.get("description").toString());
                userrole.setStatus(record.get("status").toString());
                userrole.setCreatedTime(record.get("createdtime") != null ? (Date) record.get("createdtime") : null);
                userrole.setLastUpdatedTime(record.get("lastupdatedtime") != null ? (Date) record.get("lastupdatedtime") : null);
                userrole.setLastUpdatedUser(record.get("lastupdateduser") != null ? record.get("lastupdateduser").toString() : null);
                return userrole;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            userroleList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return userroleList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Result> getResultList() throws Exception {
        List<Result> resultList;
        try {
            resultList = jdbcTemplate.query(SQL_GET_RESULT_LIST, new RowMapper<Result>() {
                @Override
                public Result mapRow(ResultSet resultSet, int i) throws SQLException {
                    Result result = new Result();
                    result.setCode(resultSet.getString("code"));
                    result.setDescription(resultSet.getString("description"));
                    return result;
                }
            });
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            resultList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return resultList;
    }

    @Transactional(readOnly = true)
    public List<CommonInstitution> getCommonInstitutionList() throws Exception {
        List<CommonInstitution> commonInstitutionList;
        try {
            commonInstitutionList = jdbcTemplate.query(SQL_GETALL_INSTITUTION_LIST, new RowMapper<CommonInstitution>() {
                @Override
                public CommonInstitution mapRow(ResultSet resultSet, int i) throws SQLException {
                    CommonInstitution commonInstitution = new CommonInstitution();

                    commonInstitution.setInstitutionCode(resultSet.getString("institutionCode"));
                    commonInstitution.setInstitutionName(resultSet.getString("name"));

                    return commonInstitution;
                }
            });
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            commonInstitutionList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return commonInstitutionList;
    }

    public int getPasswordParam(String paramcode) {
        int count = 0;
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(SQL_USERPARAM_BY_PARAMCODE, new Object[]{paramcode});
            if (map.size() != 0) {
                count = map.get("value") != null ? Integer.parseInt(map.get("value").toString()) : 0;
            }
        } catch (EmptyResultDataAccessException ere) {
            count = 0;
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public String getUserRoleStatusCode(String userrole) {
        String statusCode = "";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(SQL_USERROLE_STATUS_BY_USERROLECODE, new Object[]{userrole});
            if (map.size() != 0) {
                statusCode = (String) map.get("status");
            }
        } catch (EmptyResultDataAccessException ere) {
            statusCode = "";
        } catch (Exception e) {
            throw e;
        }
        return statusCode;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<District> getDistrictList() throws Exception {
        List<District> dList;
        try {
            List<Map<String, Object>> districtList = jdbcTemplate.queryForList(SQL_GET_DISTRICT_LIST);
            dList = districtList.stream().map((record) -> {
                District district = new District();
                district.setCode(record.get("code").toString());
                district.setDescription(record.get("description").toString());
                return district;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            dList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return dList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Institution> getInstitutionList() {
        List<Institution> institutionList;
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_INSTITUTION_LIST, new Object[]{commonVarList.STATUS_ACTIVE});
            institutionList = list.stream().map((record) -> {
                Institution institution = new Institution();
                institution.setInstitutionCode(record.get("code").toString());
                institution.setInstitutionName(record.get("name").toString());
                return institution;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            institutionList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return institutionList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Plate> getPlateListList(String receivedDate) {
        List<Plate> plateList;
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_PLATE_LIST, new Object[]{receivedDate});
            plateList = list.stream().map((record) -> {
                Plate plate = new Plate();
                plate.setId(record.get("id").toString());
                plate.setCode(record.get("code").toString());
                plate.setReceivedDate(record.get("receiveddate") != null ? (Date) record.get("receiveddate") : null);
                plate.setCreatedDate(record.get("createddate") != null ? (Date) record.get("createddate") : null);
                return plate;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            plateList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return plateList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public Plate getMaxPlateId(String receivedDate) {
        List<Plate> plateList;
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_MAX_PLATE, new Object[]{receivedDate});
            plateList = list.stream().map((record) -> {
                Plate plate = new Plate();
                plate.setId(record.get("max_plate_id").toString());
                return plate;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            plateList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return plateList.get(0);
    }

    @LogRepository
    @Transactional(readOnly = true)
    public ResultPlate getMarkedDetails(String barcode) {
        List<ResultPlate> resultPlateList = new ArrayList<>();
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_RESULT_PLATE, new Object[]{barcode});
            if(list!=null && !list.isEmpty()) {
                resultPlateList = list.stream().map((record) -> {
                    Plate plate = new Plate();

                    ResultPlate resultPlate = new ResultPlate();
                    resultPlate.setBarcode(barcode);//barcode
                    resultPlate.setCt1(record.get("ct_target1").toString());//ct_target1
                    resultPlate.setCt2(record.get("ct_target2").toString());//ct_target2
                    resultPlate.setRemark(record.get("rejectremark").toString());//rejectremark
                    resultPlate.setResultId(record.get("result").toString());//result
                    //resultPlate.setReceivedDate("");

                    return resultPlate;
                }).collect(Collectors.toList());
            }
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            resultPlateList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return resultPlateList.get(0);
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<ResultType> getResultTypeList() {
        List<ResultType> resultTypeList;
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_RESULT_TYPE_LIST
                    , new Object[]{});
            resultTypeList = list.stream().map((record) -> {
                ResultType resultType = new ResultType();
                resultType.setCode(record.get("code").toString());
                resultType.setDescription(record.get("description").toString());
                return resultType;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            resultTypeList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return resultTypeList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Status> getStatusListForSampleVerify() throws Exception {
        List<Status> statusBeanList;
        try {
            //List<Map<String, Object>> statusList = jdbcTemplate.queryForList(SQL_GET_STATUS_LIST_SAMPLEVERIDY, new Object[]{commonVarList.STATUS_PENDING, commonVarList.STATUS_VALIDATED, commonVarList.STATUS_REPEATED});
            List<Map<String, Object>> statusList = jdbcTemplate.queryForList(SQL_GET_STATUS_LIST_SAMPLEVERIDY, new Object[]{commonVarList.STATUS_PENDING, commonVarList.STATUS_VALIDATED});
            statusBeanList = statusList.stream().map((record) -> {
                Status statusBean = new Status();
                statusBean.setStatusCode(record.get("code").toString());
                statusBean.setDescription(record.get("description").toString());
                return statusBean;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            statusBeanList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return statusBeanList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<Status> getReportStatusList() {
        List<Status> statusBeanList;
        try {
            List<Map<String, Object>> statusList = jdbcTemplate.queryForList(SQL_GET_STATUS_LIST_REPORT, new Object[]{commonVarList.STATUS_PLATEASSIGNED, commonVarList.STATUS_COMPLETED, commonVarList.STATUS_REPEATED});
            statusBeanList = statusList.stream().map((record) -> {
                Status statusBean = new Status();
                statusBean.setStatusCode(record.get("code").toString());
                statusBean.setDescription(record.get("description").toString());
                return statusBean;
            }).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException ere) {
            //handle the empty result data access exception
            statusBeanList = new ArrayList<>();
        } catch (Exception e) {
            throw e;
        }
        return statusBeanList;
    }
}
