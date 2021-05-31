package com.epic.ims.repository.plateassign;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.plate.DefaultBean;
import com.epic.ims.bean.plate.PoolBean;
import com.epic.ims.bean.plate.SwapBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapper.mastertemp.MasterTempDataMapper;
import com.epic.ims.mapping.mastertemp.MasterTemp;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import com.epic.ims.util.varlist.MessageVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
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
import java.util.*;

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

    private final String SQL_GET_SAMPLEFILE_LIST = "" +
            "select sd.id , sd.referenceno, sd.institutioncode , sd.name , sd.age , sd.gender , sd.symptomatic , sd.contacttype , sd.nic , sd.address ,sd.status as status, sd.district , sd.contactno , " +
            "sd.secondarycontactno , sd.specimenid , sd.barcode , sd.receiveddate ,sd.ward, sd.createdtime as createdtime,sd.createduser as createduser " +
            "from sample_data sd where sd.status in (?,?) ";
    //"from sample_data sd where sd.status in (?,?) and sd.borcode not null and and sd.borcode <> ''";

    private final String SQL_GET_EXCELBLOCK_VALUE = "select code from excelblock eb where eb.indexvalue = ?";
    private final String SQL_INSERT_MASTERTEMPRECORD = "insert into master_temp_data(sampleid,referenceno,institutioncode,name,age,gender,symptomatic,contacttype,nic,address,district,contactno,secondarycontactno,receiveddate,status,plateid,blockvalue,ward,labcode,createduser) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String SQL_GET_DEFAULT_PLATE_LIST = "select @n := @n + 1 n,GROUP_CONCAT(id SEPARATOR '|') as id,GROUP_CONCAT(referenceno SEPARATOR '|') as referenceno,GROUP_CONCAT(name SEPARATOR '|') as name,GROUP_CONCAT(nic SEPARATOR '|') as nic,labcode,plateid,blockvalue,ispool from master_temp_data, (select @n := -1) m where status = ? group by labcode , plateid , blockvalue , ispool order by labcode";

    private final String SQL_SWAP_DEFAULT_PLATE_LIST = "" +
            "update master_temp_data a " +
            "inner join master_temp_data b on a.id <> b.id " +
            "set a.sampleid = b.sampleid,a.referenceno = b.referenceno,a.institutioncode = b.institutioncode,a.name = b.name,a.age = b.age,a.gender = b.gender, a.nic = b.nic,a.address = b.address," +
            "a.district = b.district,a.contactno = b.contactno,a.receiveddate = b.receiveddate,a.status = b.status,a.plateid = b.plateid,a.blockvalue = b.blockvalue,a.labcode = b.labcode,a.createduser = b.createduser " +
            "where a.labcode in (:aLabCodes) and b.labcode in (:bLabCodes)";

    private final String SQL_MERGE_DEFAULT_PLATE_LIST = "" +
            "update master_temp_data as m1, " +
            "master_temp_data as m2 " +
            "set m1.labcode = m2.labcode , m1.blockvalue = m2.blockvalue , m1.plateid = m2.plateid , m1.ispool= :ispool " +
            "where m1.labcode in (:labCodes) and m2.labcode = :labcode";

    private final String SQL_GET_MASTERTEMP_LIST = "" +
            "select m.id,m.sampleid,m.referenceno,m.institutioncode,m.name,m.age,m.gender,m.symptomatic,m.contacttype,m.nic,m.address,m.district,m.contactno,m.secondarycontactno,m.serialno,m.specimenid,m.labcode," +
            "m.receiveddate,s.description as description ,m.plateid,m.blockvalue,e.indexvalue,m.ispool,m.ward,m.createduser,m.createdtime " +
            "from master_temp_data m " +
            "left outer join status s on s.code=m.status " +
            "left outer join excelblock e on e.code=m.blockvalue where plateid = ? order by cast(e.indexvalue as unsigned)";

    private final String SQL_INSERT_MASTERRECORD = "insert into master_data(sampleid,referenceno,institutioncode ,name,age,gender,symptomatic,contacttype,nic,address,district,contactno,secondarycontactno,serialno,specimenid,barcode,receiveddate,status,plateid,blockvalue,ward,ispool,createduser) values(?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String SQL_UPDATE_SAMPLEDATA_LIST = "update sample_data set status = :status where id in (:ids)";

    @LogRepository
    public String createDefaultPlateList(String receivedDate) throws Exception {
        String message = "";
        try {
            String currentDate = commonRepository.getCurrentDateAsString();
            //delete all from master temp table
            this.deleteAllFromMasterTempTable();
            //get the max plate id for corresponding date
            int maxPlateId = this.getMaxPlateIdForCorrespondingDate(currentDate);
            //get the sample data list from sample table
            List<SampleFile> sampleFileList = this.getSampleFileList(currentDate);
            //check the sample file length
            if (sampleFileList != null && sampleFileList.size() > 0) {
                int plateSize = sampleFileList.size();
                int noOfPaltes = (plateSize / PLATE_SIZE) + 1;
                //get the list
                int startStartingIndex = 0;
                int startEndingIndex = 93;
                for (int i = 1; i <= noOfPaltes; i++) {
                    maxPlateId = maxPlateId + 1;
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
                        String barcode = commonRepository.getCurrentDateAsYYYYMMDD() + maxPlateId + commonVarList.PLATE_POSITION_C3;
                        subList.add(PLATE_C3_POSITION, new SampleFile(null, "", receivedDate, "N/A", "N/A", barcode));
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
    @Transactional(readOnly = true)
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
                        ps.setString(7, masterTemp.getSymptomatic());
                        ps.setString(8, masterTemp.getContactType());
                        ps.setString(9, masterTemp.getNic());
                        ps.setString(10, masterTemp.getAddress());
                        ps.setString(11, masterTemp.getResidentDistrict());
                        ps.setString(12, masterTemp.getContactNumber());
                        ps.setString(13, masterTemp.getSecondaryContactNumber());
                        ps.setString(14, masterTemp.getReceivedDate());
                        ps.setString(15, commonVarList.STATUS_PLATEASSIGNED);
                        ps.setString(16, masterTemp.getPlateId());
                        ps.setString(17, masterTemp.getBlockValue());
                        ps.setString(18, masterTemp.getWard());
                        ps.setString(19, "");
                        ps.setString(20, sessionBean.getUsername());
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
            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
            //create the integer set
            List<String> labNoList = Arrays.asList(swapBean.getLabCode1(), swapBean.getLabCode2());
            idSetParameterMap.addValue("aLabCodes", labNoList);
            idSetParameterMap.addValue("bLabCodes", labNoList);
            //execute the query
            int value = namedParameterJdbcTemplate.update(SQL_SWAP_DEFAULT_PLATE_LIST, idSetParameterMap);
            if (value < 0) {
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
    public String MergeBlockPlate(PoolBean poolBean) {
        String message = "";
        try {
            List<ArrayList<String>> poolList = poolBean.getPoolList();
            //validate the pool list
            if (poolList != null && !poolList.isEmpty() && poolList.size() > 0) {
                for (ArrayList<String> arrayList : poolList) {
                    //validate the array list one by one inside pool list
                    if (arrayList != null && !arrayList.isEmpty() && arrayList.size() > 0) {
                        //validate the staring index
                        String startIndexValue = arrayList.get(0);
                        if (startIndexValue != null && !startIndexValue.isEmpty()) {
                            logger.info("--Executing pool list index no --" + poolList.indexOf(arrayList) + "-start");
                            MapSqlParameterSource idSetParameterMap = new MapSqlParameterSource();
                            idSetParameterMap.addValue("ispool", 1);
                            idSetParameterMap.addValue("labCodes", arrayList);
                            idSetParameterMap.addValue("labcode", startIndexValue);
                            int value = namedParameterJdbcTemplate.update(SQL_MERGE_DEFAULT_PLATE_LIST, idSetParameterMap);
                            if (value < 0) {
                                message = MessageVarList.COMMON_ERROR_PROCESS;
                                break;
                            }
                            logger.info("--Executing pool list index no --" + poolList.indexOf(arrayList) + "-end");
                        }
                    }
                }
            }
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
        } catch (Exception e) {
            logger.error(e);
        }
        return message;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<String> getPlateNumberList() {
        List<String> plateNumberList = new ArrayList<>();
        try {
            String sql = "select distinct(plateid) as plateid from master_temp_data";
            jdbcTemplate.query(sql, new Object[]{commonVarList.STATUS_PLATEASSIGNED}, (ResultSet rs) -> {
                while (rs.next()) {
                    plateNumberList.add(rs.getString("plateid"));
                }
                return plateNumberList;
            });
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
            return plateNumberList;
        } catch (Exception e) {
            logger.error(e);
            return plateNumberList;
        }
        return plateNumberList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<MasterTemp> getMasterTempList(String plateId) {
        List<MasterTemp> masterTempList = null;
        try {
            masterTempList = jdbcTemplate.query(SQL_GET_MASTERTEMP_LIST, new Object[]{plateId}, (rs, id) -> {
                MasterTemp masterTemp = new MasterTemp();

                try {
                    masterTemp.setId(rs.getInt("id"));
                } catch (Exception e) {
                    masterTemp.setId(0);
                }

                try {
                    masterTemp.setSampleId(rs.getString("sampleid"));
                } catch (Exception e) {
                    masterTemp.setSampleId(null);
                }

                try {
                    masterTemp.setReferenceNo(rs.getString("referenceno"));
                } catch (Exception e) {
                    masterTemp.setReferenceNo(null);
                }

                try {
                    masterTemp.setInstitutionCode(rs.getString("institutioncode"));
                } catch (Exception e) {
                    masterTemp.setInstitutionCode(null);
                }

                try {
                    masterTemp.setName(rs.getString("name"));
                } catch (Exception e) {
                    masterTemp.setName(null);
                }

                try {
                    masterTemp.setAge(rs.getString("age"));
                } catch (Exception e) {
                    masterTemp.setAge(null);
                }

                try {
                    masterTemp.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    masterTemp.setGender(null);
                }

                try {
                    masterTemp.setSymptomatic(rs.getString("symptomatic"));
                } catch (Exception e) {
                    masterTemp.setSymptomatic(null);
                }

                try {
                    masterTemp.setContactType(rs.getString("contacttype"));
                } catch (Exception e) {
                    masterTemp.setContactType(null);
                }

                try {
                    masterTemp.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    masterTemp.setNic(null);
                }

                try {
                    masterTemp.setAddress(rs.getString("address"));
                } catch (Exception e) {
                    masterTemp.setAddress(null);
                }

                try {
                    masterTemp.setResidentDistrict(rs.getString("district"));
                } catch (Exception e) {
                    masterTemp.setResidentDistrict(null);
                }

                try {
                    masterTemp.setContactNumber(rs.getString("contactno"));
                } catch (Exception e) {
                    masterTemp.setContactNumber(null);
                }

                try {
                    masterTemp.setSecondaryContactNumber(rs.getString("secondarycontactno"));
                } catch (Exception e) {
                    masterTemp.setSecondaryContactNumber(null);
                }

                try {
                    masterTemp.setSerialNo(rs.getString("serialno"));
                } catch (Exception e) {
                    masterTemp.setSerialNo(null);
                }

                try {
                    masterTemp.setSpecimenid(rs.getString("specimenid"));
                } catch (Exception e) {
                    masterTemp.setSpecimenid(null);
                }

                try {
                    masterTemp.setBarcode(rs.getString("labcode"));
                } catch (Exception e) {
                    masterTemp.setBarcode(null);
                }

                try {
                    masterTemp.setReceivedDate(rs.getString("receiveddate"));
                } catch (Exception e) {
                    masterTemp.setReceivedDate(null);
                }

                try {
                    masterTemp.setStatus(rs.getString("description"));
                } catch (Exception e) {
                    masterTemp.setStatus(null);
                }

                try {
                    masterTemp.setPlateId(rs.getString("plateid"));
                } catch (Exception e) {
                    masterTemp.setPlateId(null);
                }

                try {
                    masterTemp.setBlockValue(rs.getString("blockvalue"));
                } catch (Exception e) {
                    masterTemp.setBlockValue(null);
                }

                try {
                    masterTemp.setIndexValue(rs.getString("indexvalue"));
                } catch (Exception e) {
                    masterTemp.setIndexValue(null);
                }

                try {
                    masterTemp.setIsPool(rs.getString("ispool"));
                } catch (Exception e) {
                    masterTemp.setIsPool("--");
                }

                try {
                    masterTemp.setWard(rs.getString("ward"));
                } catch (Exception e) {
                    masterTemp.setWard("--");
                }

                try {
                    masterTemp.setCreatedUser(common.handleNullAndEmptyValue(rs.getString("createduser")));
                } catch (Exception e) {
                    masterTemp.setCreatedUser("--");
                }

                try {
                    masterTemp.setCreatedTime(rs.getTimestamp("createdtime"));
                } catch (Exception e) {
                    masterTemp.setCreatedTime(null);
                }

                return masterTemp;
            });
        } catch (EmptyResultDataAccessException ex) {
            return masterTempList;
        } catch (Exception e) {
            throw e;
        }
        return masterTempList;
    }

    @LogRepository
    @Transactional
    public String createPlate(String plateId) {
        String message = "";
        try {
            int value = 0;
            String currentDate = commonRepository.getCurrentDateAsString();
            value = jdbcTemplate.update("insert into plate(code,receiveddata) values(?,?)", new Object[]{plateId, currentDate});

            if (value != 1) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (DuplicateKeyException dke) {
            message = MessageVarList.COMMON_ERROR_RECORD_ALREADY_EXISTS;
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String insertMasterBatch(List<MasterTemp> masterTempList) throws Exception {
        String message = "";
        try {
            for (int j = 0; j < masterTempList.size(); j += commonVarList.BULKUPLOAD_BATCH_SIZE) {
                final List<MasterTemp> batchList = masterTempList.subList(j, j + commonVarList.BULKUPLOAD_BATCH_SIZE > masterTempList.size() ? masterTempList.size() : j + commonVarList.BULKUPLOAD_BATCH_SIZE);
                jdbcTemplate.batchUpdate(SQL_INSERT_MASTERRECORD, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        MasterTemp masterTemp = batchList.get(i);
                        ps.setString(1, masterTemp.getSampleId());
                        ps.setString(2, masterTemp.getReferenceNo());
                        ps.setString(3, masterTemp.getInstitutionCode());
                        ps.setString(4, masterTemp.getName());
                        ps.setString(5, masterTemp.getAge());
                        ps.setString(6, masterTemp.getGender());
                        ps.setString(7, masterTemp.getSymptomatic());
                        ps.setString(8, masterTemp.getContactType());
                        ps.setString(9, masterTemp.getNic());
                        ps.setString(10, masterTemp.getAddress());
                        ps.setString(11, masterTemp.getResidentDistrict());
                        ps.setString(12, masterTemp.getContactNumber());
                        ps.setString(13, masterTemp.getSecondaryContactNumber());
                        ps.setString(14, masterTemp.getSerialNo());
                        ps.setString(15, masterTemp.getSpecimenid());
                        ps.setString(16, masterTemp.getBarcode());
                        ps.setString(17, masterTemp.getReceivedDate());
                        ps.setString(18, commonVarList.STATUS_PLATEASSIGNED);
                        ps.setString(19, masterTemp.getPlateId());
                        ps.setString(20, masterTemp.getBlockValue());
                        ps.setString(21, masterTemp.getWard());
                        ps.setString(22, masterTemp.getIsPool());
                        ps.setString(23, sessionBean.getUsername());
                    }

                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                });
            }
        } catch (Exception e) {
            message = MessageVarList.COMMON_ERROR_PROCESS;
        }
        return message;
    }

    @LogRepository
    @Transactional
    public String updateSampleDataList(List<String> sampleIdList) {
        String message = "";
        try {
            MapSqlParameterSource parameterMap = new MapSqlParameterSource();
            //create the integer set
            parameterMap.addValue("status", commonVarList.STATUS_PLATEASSIGNED);
            parameterMap.addValue("ids", sampleIdList);

            //execute the query
            int value = namedParameterJdbcTemplate.update(SQL_UPDATE_SAMPLEDATA_LIST, parameterMap);
            if (value < 0) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception e) {
            throw e;
        }
        return message;
    }
}
