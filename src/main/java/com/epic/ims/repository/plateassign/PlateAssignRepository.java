package com.epic.ims.repository.plateassign;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.plate.SwapBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapper.mastertemp.MasterTempDataMapper;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Scope("prototype")
public class PlateAssignRepository {
    private static Logger logger = LogManager.getLogger(PlateAssignRepository.class);

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
    MasterTempDataMapper masterTempDataMapper;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final int PLATE_SIZE = 93;
    private final int PLATE_C3_POSITION = 26;
    private final int PLATE_G12_POSITION = 83;
    private final int PLATE_H12_POSITION = 95;
    private final String SQL_DELETE_MASTER_TEMP_TABLE = "delete from master_temp_data";
    private final String SQL_GET_MAX_PLATE_CODE = "select max(code) as maxplateid from plate where receiveddate = ?";
    private final String SQL_GET_SAMPLEFILE_LIST = "select sd.id , sd.referenceno, sd.institutioncode , sd.name , sd.age , sd.gender , sd.symptomatic , sd.contacttype , sd.nic , sd.address ,sd.status as status, sd.district , sd.contactno , sd.secondarycontactno , sd.specimenid , sd.barcode , sd.receiveddate ,sd.ward, sd.createdtime as createdtime,sd.createduser as createduser from sample_data sd where sd.status in (?,?)";
    private final String SQL_GET_EXCELBLOCK_VALUE = "select code from excelblock eb where eb.indexvalue = ?";
    private final String SQL_INSERT_MASTERTEMPRECORD = "insert into master_temp_data(sampleid,referenceno,institutioncode,name,age,gender,nic,address,district,contactno,receiveddate,status,plateid,blockvalue,labcode,createduser) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String SQL_GET_DEFAULT_PLATE_LIST = "select @n := @n + 1 n,GROUP_CONCAT(id SEPARATOR '|') as id,GROUP_CONCAT(referenceno SEPARATOR '|') as referenceno,GROUP_CONCAT(name SEPARATOR '|') as name,GROUP_CONCAT(nic SEPARATOR '|') as nic,labcode,plateid,blockvalue,ispool from master_temp_data, (select @n := -1) m where status = ? group by labcode , plateid , blockvalue , ispool order by labcode";

    @LogRepository
    public String createDefaultPlateList(String receivedDate) throws Exception {
        String message = "";
        try {
            //delete all from master temp table
            this.deleteAllFromMasterTempTable();
            //get the max plate id for corresponding date
            int maxPlateId = this.getMaxPlateIdForCorrespondingDate(receivedDate);
            //get the sample data list from sample table
            List<SampleFile> sampleFileList = this.getSampleFileList(receivedDate);
            //check the sample file length
            if (sampleFileList != null && sampleFileList.size() > 0) {
                maxPlateId = maxPlateId + 1;
                int plateSize = sampleFileList.size();
                int noOfPaltes = (plateSize / PLATE_SIZE) + 1;
                //get the list
                int startStartingIndex = 0;
                int startEndingIndex = 93;
                for (int i = 1; i <= noOfPaltes; i++) {
                    int startingIndex = startStartingIndex + (i - 1) * PLATE_SIZE;
                    int endingIndex = startEndingIndex + (i - 1) * PLATE_SIZE;

                    //get the sub list
                    //check the sample file list size is enough for sub list
                    List<SampleFile> subList = null;
                    if (sampleFileList.size() >= (endingIndex - 1)) {
                        subList = sampleFileList.subList(startingIndex, endingIndex);
                    } else {
                        endingIndex = sampleFileList.size() - 1;
                        subList = sampleFileList.subList(startingIndex, endingIndex);
                    }

                    //handle the c3 value in master plate
                    //add 1 to position when checking the length
                    if (subList != null && subList.size() > (PLATE_C3_POSITION + 1)) {
                        subList.add(PLATE_C3_POSITION, new SampleFile(null, "", receivedDate, "N/A", "N/A"));
                    }

                    //handle the g12 value in master plate
                    //add 1 to position when checking the length
                    //if (subList != null && subList.size() > (PLATE_G12_POSITION + 1)) {
                    //    subList.add(PLATE_G12_POSITION, new SampleFile(null, "", receivedDate, "N/A", "N/A"));
                    //}

                    //handle the h12 value in master plate
                    //add 1 to position when checking the length
                    //if (subList != null && subList.size() > (PLATE_H12_POSITION - 1)) {
                    //    subList.add(PLATE_H12_POSITION, new SampleFile(null, "", receivedDate, "N/A", "N/A"));
                    //}

                    for (int j = 0; j < subList.size(); j++) {
                        SampleFile sFile = subList.get(j);
                        Map<String, Object> map = jdbcTemplate.queryForMap(SQL_GET_EXCELBLOCK_VALUE, new Object[]{j + ""});
                        if (map.size() != 0) {
                            String blockCode = map.get("code") != null ? map.get("code").toString() : "";
                            sFile.setPlateId(maxPlateId + "");
                            sFile.setBlockValue(blockCode);
                        }
                    }

                    //get the master temp data list using sample file list
                    List<MasterTemp> masterTempList = masterTempDataMapper.sampleListMasterTempList(subList);
                    //insert the batch to master temp data table
                    this.insertMasterTempBatch(masterTempList);
                    //increase the plate id by one to create nex plate
                    maxPlateId++;
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
            message = MessageVarList.COMMON_ERROR_RECORD_DOESNOT_EXISTS;
        } catch (Exception e) {
            logger.error(e);
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public void deleteAllFromMasterTempTable() {
        try {
            jdbcTemplate.update(SQL_DELETE_MASTER_TEMP_TABLE);
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    @LogRepository
    @Transactional(readOnly = true)
    public int getMaxPlateIdForCorrespondingDate(String receivedDate) {
        int maxPalteId = 0;
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(SQL_GET_MAX_PLATE_CODE, new Object[]{receivedDate});
            if (map.size() != 0) {
                maxPalteId = map.get("maxplateid") != null ? Integer.parseInt(map.get("maxplateid").toString()) : 0;
            }
        } catch (EmptyResultDataAccessException ex) {
            maxPalteId = 0;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
        return maxPalteId;
    }


    @LogRepository
    @Transactional
    public List<SampleFile> getSampleFileList(String receivedDate) {
        List<SampleFile> sampleFileList = null;
        try {
            sampleFileList = jdbcTemplate.query(SQL_GET_SAMPLEFILE_LIST, new Object[]{commonVarList.STATUS_VALIDATED, commonVarList.STATUS_REPEATED}, (rs, id) -> {
                SampleFile sampleFile = new SampleFile();

                try {
                    sampleFile.setId(rs.getInt("id") + "");
                } catch (Exception e) {
                    sampleFile.setId(0 + "");
                }

                try {
                    sampleFile.setReferenceNo(common.handleNullAndEmptyValue(rs.getString("referenceno")));
                } catch (Exception e) {
                    sampleFile.setReferenceNo("--");
                }

                try {
                    sampleFile.setInstitutionCode(common.handleNullAndEmptyValue(rs.getString("institutioncode")));
                } catch (Exception e) {
                    sampleFile.setInstitutionCode("--");
                }

                try {
                    sampleFile.setName(common.handleNullAndEmptyValue(rs.getString("name")));
                } catch (Exception e) {
                    sampleFile.setName("--");
                }

                try {
                    sampleFile.setAge(common.handleNullAndEmptyValue(rs.getString("age")));
                } catch (Exception e) {
                    sampleFile.setAge("--");
                }

                try {
                    sampleFile.setGender(common.handleNullAndEmptyValue(rs.getString("gender")));
                } catch (Exception e) {
                    sampleFile.setGender("--");
                }

                try {
                    sampleFile.setSymptomatic(common.handleNullAndEmptyValue(rs.getString("symptomatic")));
                } catch (Exception e) {
                    sampleFile.setSymptomatic("--");
                }

                try {
                    sampleFile.setContactType(common.handleNullAndEmptyValue(rs.getString("contacttype")));
                } catch (Exception e) {
                    sampleFile.setContactType("--");
                }

                try {
                    sampleFile.setNic(common.handleNullAndEmptyValue(rs.getString("nic")));
                } catch (Exception e) {
                    sampleFile.setNic("--");
                }

                try {
                    sampleFile.setAddress(common.handleNullAndEmptyValue(rs.getString("address")));
                } catch (Exception e) {
                    sampleFile.setAddress("--");
                }

                try {
                    sampleFile.setStatus(common.handleNullAndEmptyValue(rs.getString("status")));
                } catch (Exception e) {
                    sampleFile.setStatus("--");
                }

                try {
                    sampleFile.setResidentDistrict(common.handleNullAndEmptyValue(rs.getString("district")));
                } catch (Exception e) {
                    sampleFile.setResidentDistrict("--");
                }

                try {
                    sampleFile.setContactNumber(common.handleNullAndEmptyValue(rs.getString("contactno")));
                } catch (Exception e) {
                    sampleFile.setContactNumber("--");
                }

                try {
                    sampleFile.setSecondaryContactNumber(common.handleNullAndEmptyValue(rs.getString("secondarycontactno")));
                } catch (Exception e) {
                    sampleFile.setSecondaryContactNumber("--");
                }

                try {
                    sampleFile.setSpecimenid(common.handleNullAndEmptyValue(rs.getString("specimenid")));
                } catch (Exception e) {
                    sampleFile.setSpecimenid("--");
                }

                try {
                    sampleFile.setBarcode(common.handleNullAndEmptyValue(rs.getString("barcode")));
                } catch (Exception e) {
                    sampleFile.setBarcode("--");
                }

                try {
                    sampleFile.setReceivedDate(common.handleNullAndEmptyValue(rs.getString("receiveddate")));
                } catch (Exception e) {
                    sampleFile.setReceivedDate("--");
                }

                try {
                    sampleFile.setWard(common.handleNullAndEmptyValue(rs.getString("ward")));
                } catch (Exception e) {
                    sampleFile.setWard("--");
                }

                try {
                    sampleFile.setCreatedTime(rs.getTimestamp("createdtime"));
                } catch (Exception e) {
                    sampleFile.setCreatedTime(null);
                }

                try {
                    sampleFile.setCreatedUser(common.handleNullAndEmptyValue(rs.getString("createduser")));
                } catch (Exception e) {
                    sampleFile.setCreatedUser("--");
                }

                return sampleFile;
            });
        } catch (EmptyResultDataAccessException ex) {
            return sampleFileList;
        } catch (Exception e) {
            throw e;
        }
        return sampleFileList;
    }

    @LogRepository
    @Transactional
    public String insertMasterTempBatch(List<MasterTemp> masterTempList) throws Exception {
        String message = "";
        try {
            for (int j = 0; j < masterTempList.size(); j += commonVarList.BULKUPLOAD_BATCH_SIZE) {
                final List<MasterTemp> batchList = masterTempList.subList(j, j + commonVarList.BULKUPLOAD_BATCH_SIZE > masterTempList.size() ? masterTempList.size() : j + commonVarList.BULKUPLOAD_BATCH_SIZE);
                jdbcTemplate.batchUpdate(SQL_INSERT_MASTERTEMPRECORD, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MasterTemp masterTemp = batchList.get(i);
                        ps.setString(1, masterTemp.getSampleId());
                        ps.setString(2, masterTemp.getReferenceNo());
                        ps.setString(3, masterTemp.getInstitutionCode());
                        ps.setString(4, masterTemp.getName());
                        ps.setString(5, masterTemp.getAge());
                        ps.setString(6, masterTemp.getGender());
                        ps.setString(7, masterTemp.getNic());
                        ps.setString(8, masterTemp.getAddress());
                        ps.setString(9, masterTemp.getResidentDistrict());
                        ps.setString(10, masterTemp.getContactNumber());
                        ps.setString(11, masterTemp.getReceivedDate());
                        ps.setString(12, commonVarList.STATUS_PLATEASSIGNED);
                        ps.setString(13, masterTemp.getPlateId());
                        ps.setString(14, masterTemp.getBlockValue());
                        ps.setString(15, "");
                        ps.setString(16, sessionBean.getUsername());
                    }

                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                });
            }
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public Map<Integer, List<DefaultBean>> getDefaultPlateList() {
        Map<Integer, List<DefaultBean>> defaultPlateMap = new HashMap<>();
        try {
            jdbcTemplate.query(SQL_GET_DEFAULT_PLATE_LIST, new Object[]{commonVarList.STATUS_PLATEASSIGNED}, (ResultSet rs) -> {
                while (rs.next()) {
                    defaultPlateMap.put(rs.getInt("n"), Arrays.asList(
                            new DefaultBean(
                                    rs.getString("id").split("\\|"),
                                    rs.getString("referenceno").split("\\|"),
                                    rs.getString("name").split("\\|"),
                                    rs.getString("nic").split("\\|"),
                                    rs.getString("labcode"),
                                    rs.getString("plateid"),
                                    rs.getString("blockvalue"),
                                    rs.getString("ispool")
                            )
                    ));
                }
                return defaultPlateMap;
            });
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
            return defaultPlateMap;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
        return defaultPlateMap;
    }


    @LogRepository
    @Transactional
    public String swapBlockPlate(SwapBean swapBean) {
        String message = "";
        try {
            String swapSql = "" +
                    "update master_temp_data a " +
                    "inner join master_temp_data b on a.id <> b.id " +
                    "set a.sampleid = b.sampleid,a.referenceno = b.referenceno,a.institutioncode = b.institutioncode,a.name = b.name,a.age = b.age,a.gender = b.gender, a.nic = b.nic,a.address = b.address," +
                    "a.district = b.district,a.contactno = b.contactno,a.receiveddate = b.receiveddate,a.status = b.status,a.plateid = b.plateid,a.blockvalue = b.blockvalue,a.labcode = b.labcode,a.createduser = b.createduser " +
                    "where a.labcode in (:aLabCodes) and b.labcode (:bLabCodes)";

            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
            //create the integer set
            List<String> labNoList = Arrays.asList(swapBean.getLabCode1(), swapBean.getLabCode2());
            idSetParameterMap.addValue("aLabCodes", labNoList);
            idSetParameterMap.addValue("bLabCodes", labNoList);
            //execute the query
            int value = namedParameterJdbcTemplate.update(swapSql, idSetParameterMap);
            if (value <= 0) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (EmptyResultDataAccessException ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String MergeBlockPlate(PlateBean plateBean) {
        String message = "";
        try {
            System.out.println("--------------plate array--------------------------");
            plateBean.getPlateArray().forEach((key, value) -> System.out.println(key + "--:" + new Gson().toJson(plateBean.getPlateArray())));
            System.out.println("--------------plate array--------------------------");

            System.out.println("--------------merge array--------------------------");
            plateBean.getMergeArray().forEach((key, value) -> System.out.println(key + "--:" + new Gson().toJson(plateBean.getMergeArray())));
            System.out.println("--------------merge array--------------------------");

            message = "DB updated merge";
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
        } catch (Exception e) {
            logger.error(e);
        }
        return message;
    }


}
