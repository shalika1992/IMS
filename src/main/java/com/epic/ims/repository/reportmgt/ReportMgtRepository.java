package com.epic.ims.repository.reportmgt;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.reportmgt.MasterDataInputBeen;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.reportmgt.MasterData;
import com.epic.ims.service.common.CommonService;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Scope("prototype")
public class ReportMgtRepository {
    private final Log logger = LogFactory.getLog(getClass());

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

    private final String SQL_GET_COUNT = "select count(*) from master_data m where ";

    @LogRepository
    @Transactional(readOnly = true)
    public long getCount(MasterDataInputBeen masterDataInputBeen) throws Exception {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            //create the where clause
            dynamicClause = this.setDynamicClause(masterDataInputBeen, dynamicClause);
            //create the query
            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
        } catch (Exception exception) {
            throw exception;
        }
        return count;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<MasterData> getMasterDataSearchList(MasterDataInputBeen masterDataInputBeen) {
        List<MasterData> masterDataList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(masterDataInputBeen, new StringBuilder());

            //create sorting order
            String sortingStr = "";
            String col = "";
            int sortCol;

            try {
                sortCol = masterDataInputBeen.sortedColumns.get(0);
            } catch (Exception e) {
                sortCol = 0;
            }

            switch (sortCol) {
                case 0:
                    col = "m.barcode";
                    break;
                case 1:
                    col = "m.referenceno";
                    break;
                case 2:
                    col = "i.name";
                    break;
                case 3:
                    col = "m.name";
                    break;
                case 4:
                    col = "m.age";
                    break;
                case 6:
                    col = "m.gender";
                    break;
                case 7:
                    col = "m.nic";
                    break;
                case 8:
                    col = "m.contactno";
                    break;
                case 9:
                    col = "m.specimenid";
                    break;
                case 10:
                    col = "m.barcode";
                    break;
                case 11:
                    col = "m.receiveddate";
                    break;
                case 12:
                    col = "r.description";
                    break;
                case 13:
                    col = "s.description";
                    break;
                default:
                    col = "m.barcode";
            }

            try {
                sortingStr = " order by " + col + " " + masterDataInputBeen.sortDirections.get(0);
            } catch (Exception e) {
                sortingStr = " order by " + col + " asc ";
            }

            String sql = "" +
                    "select m.id as id , m.sampleid as sampleid , m.referenceno as referenceNumber, m.institutioncode as institutionCode, i.name as institutionName, m.name as name, m.age as age, m.gender as gender, m.nic as nic, m.contactno as contactnumber, m.serialno as serialNumber, m.specimenid as specimenID, " +
                    "m.barcode as barcode, m.receiveddate as receivedDate ,s.code as statusCode,s.description as statusDescription, p.code as plateCode, m.blockvalue as blockValue, r.description as resultDescription, m.createduser as createdUser, m.createdtime as createdTime, m.reporttime as reportTime, m.ct_target1 as ct_target1, m.ct_target2 as ct_target2 " +
                    "from master_data m " +
                    "left join status s on s.code = m.status " +
                    "left join plate p on m.plateid = p.id " +
                    "left join institution i on i.institutioncode = m.institutioncode " +
                    "left join result r on r.code = m.result where " + dynamicClause.toString() + sortingStr +
                    " limit " + masterDataInputBeen.displayLength + " offset " + masterDataInputBeen.displayStart;

            masterDataList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                MasterData masterData = new MasterData();

                try {
                    masterData.setId(rs.getInt("id"));
                } catch (Exception e) {
                    masterData.setId(0);
                }

                try {
                    masterData.setSampleID(rs.getInt("sampleid"));
                } catch (Exception e) {
                    masterData.setSampleID(0);
                }

                try {
                    masterData.setReferenceNumber(rs.getString("referenceNumber"));
                } catch (Exception e) {
                    masterData.setReferenceNumber(null);
                }

                try {
                    masterData.setInstitutionCode(rs.getString("institutionCode"));
                } catch (Exception e) {
                    masterData.setInstitutionCode(null);
                }

                try {
                    masterData.setInstitutionName(rs.getString("institutionName"));
                } catch (Exception e) {
                    masterData.setInstitutionName(null);
                }

                try {
                    masterData.setName(rs.getString("name"));
                } catch (Exception e) {
                    masterData.setName(null);
                }

                try {
                    masterData.setAge(rs.getString("age"));
                } catch (Exception e) {
                    masterData.setAge(null);
                }

                try {
                    masterData.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    masterData.setGender(null);
                }

                try {
                    masterData.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    masterData.setNic(null);
                }

                try {
                    masterData.setContactNumber(rs.getString("contactNumber"));
                } catch (Exception e) {
                    masterData.setContactNumber(null);
                }

                try {
                    masterData.setSerialNumber(rs.getString("serialNumber"));
                } catch (Exception e) {
                    masterData.setSerialNumber(null);
                }

                try {
                    masterData.setSpecimenID(rs.getString("specimenID"));
                } catch (Exception e) {
                    masterData.setSpecimenID(null);
                }

                try {
                    masterData.setBarcode(rs.getString("barcode"));
                } catch (Exception e) {
                    masterData.setBarcode(null);
                }

                try {
                    masterData.setReceivedDate(rs.getString("receivedDate"));
                } catch (Exception e) {
                    masterData.setReceivedDate(null);
                }

                try {
                    masterData.setStatusCode(rs.getString("statusCode"));
                } catch (Exception e) {
                    masterData.setStatusCode(null);
                }

                try {
                    masterData.setStatusDescription(rs.getString("statusDescription"));
                } catch (Exception e) {
                    masterData.setStatusDescription(null);
                }

                try {
                    masterData.setPlateCode(rs.getString("plateCode"));
                } catch (Exception e) {
                    masterData.setPlateCode(null);
                }

                try {
                    masterData.setBlockValue(rs.getString("blockValue"));
                } catch (Exception e) {
                    masterData.setBlockValue(null);
                }

                try {
                    masterData.setResultDescription(rs.getString("resultDescription"));
                } catch (Exception e) {
                    masterData.setResultDescription(null);
                }

                try {
                    masterData.setCt_target1(rs.getString("ct_target1"));
                } catch (Exception e) {
                    masterData.setCt_target1(null);
                }

                try {
                    masterData.setCt_target2(rs.getString("ct_target2"));
                } catch (Exception e) {
                    masterData.setCt_target2(null);
                }

                try {
                    masterData.setCreatedUser(rs.getString("createdUser"));
                } catch (Exception e) {
                    masterData.setCreatedUser(null);
                }

                try {
                    masterData.setCreatedTime(rs.getTimestamp("createdTime"));
                } catch (Exception e) {
                    masterData.setCreatedTime(null);
                }

                return masterData;
            });
        } catch (Exception exception) {
            throw exception;
        }

        return masterDataList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public List<MasterData> getMasterDataSearchResultListForReport(MasterDataInputBeen masterDataInputBeen) {
        List<MasterData> masterDataList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(masterDataInputBeen, new StringBuilder());
            //create sorting order
            String sortingStr = " order by m.barcode desc ";

            String sql = "" +
                    "select m.id as id , m.sampleid as sampleid , m.referenceno as referenceNumber, m.institutioncode as institutionCode, i.name as institutionName, m.name as name, m.age as age, m.gender as gender, m.nic as nic, m.contactno as contactnumber, m.serialno as serialNumber, m.specimenid as specimenID, " +
                    "m.barcode as barcode, m.receiveddate as receivedDate ,s.description as statusDescription, p.code as plateCode, m.blockvalue as blockValue, r.description as resultDescription, m.createduser as createdUser, m.createdtime as createdTime, m.reporttime as reportTime, m.ct_target1 as ct_target1, m.ct_target2 as ct_target2 " +
                    "from master_data m " +
                    "left join status s on s.code = m.status " +
                    "left join plate p on m.plateid = p.id " +
                    "left join institution i on i.institutioncode = m.institutioncode " +
                    "left join result r on r.code = m.result where " + dynamicClause.toString() + sortingStr;

            masterDataList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                MasterData masterData = new MasterData();

                try {
                    masterData.setId(rs.getInt("id"));
                } catch (Exception e) {
                    masterData.setId(0);
                }

                try {
                    masterData.setSampleID(rs.getInt("sampleid"));
                } catch (Exception e) {
                    masterData.setSampleID(0);
                }

                try {
                    masterData.setReferenceNumber(rs.getString("referenceNumber"));
                } catch (Exception e) {
                    masterData.setReferenceNumber(null);
                }

                try {
                    masterData.setInstitutionCode(rs.getString("institutionCode"));
                } catch (Exception e) {
                    masterData.setInstitutionCode(null);
                }

                try {
                    masterData.setInstitutionName(rs.getString("institutionName"));
                } catch (Exception e) {
                    masterData.setInstitutionName(null);
                }

                try {
                    masterData.setName(rs.getString("name"));
                } catch (Exception e) {
                    masterData.setName(null);
                }

                try {
                    masterData.setAge(rs.getString("age"));
                } catch (Exception e) {
                    masterData.setAge(null);
                }

                try {
                    masterData.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    masterData.setGender(null);
                }

                try {
                    masterData.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    masterData.setNic(null);
                }

                try {
                    masterData.setContactNumber(rs.getString("contactNumber"));
                } catch (Exception e) {
                    masterData.setContactNumber(null);
                }

                try {
                    masterData.setSerialNumber(rs.getString("serialNumber"));
                } catch (Exception e) {
                    masterData.setSerialNumber(null);
                }

                try {
                    masterData.setSpecimenID(rs.getString("specimenID"));
                } catch (Exception e) {
                    masterData.setSpecimenID(null);
                }

                try {
                    masterData.setBarcode(rs.getString("barcode"));
                } catch (Exception e) {
                    masterData.setBarcode(null);
                }

                try {
                    masterData.setReceivedDate(rs.getString("receivedDate"));
                } catch (Exception e) {
                    masterData.setReceivedDate(null);
                }

                try {
                    masterData.setStatusDescription(rs.getString("statusDescription"));
                } catch (Exception e) {
                    masterData.setStatusDescription(null);
                }

                try {
                    masterData.setPlateCode(rs.getString("plateCode"));
                } catch (Exception e) {
                    masterData.setPlateCode(null);
                }

                try {
                    masterData.setBlockValue(rs.getString("blockValue"));
                } catch (Exception e) {
                    masterData.setBlockValue(null);
                }

                try {
                    masterData.setResultDescription(rs.getString("resultDescription"));
                } catch (Exception e) {
                    masterData.setResultDescription(null);
                }

                try {
                    masterData.setCt_target1(rs.getString("ct_target1"));
                } catch (Exception e) {
                    masterData.setCt_target1("--");
                }

                try {
                    masterData.setCt_target2(rs.getString("ct_target2"));
                } catch (Exception e) {
                    masterData.setCt_target2("--");
                }

                try {
                    masterData.setCreatedUser(rs.getString("createdUser"));
                } catch (Exception e) {
                    masterData.setCreatedUser(null);
                }

                try {
                    masterData.setCreatedTime(rs.getTimestamp("createdTime"));
                } catch (Exception e) {
                    masterData.setCreatedTime(null);
                }

                return masterData;
            });
        } catch (Exception exception) {
            throw exception;
        }
        return masterDataList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public MasterData getMasterDataSearchObjectForIndividualReport(MasterDataInputBeen masterDataInputBeen) {
        MasterData masterData;
        try {

            //create sorting order
            String sortingStr = " order by m.barcode desc ";
            String sql = "" +
                    "select m.id as id , m.sampleid as sampleid , m.referenceno as referenceNumber, m.institutioncode as institutionCode, i.name as institutionName, m.name as name, m.age as age, m.gender as gender, m.nic as nic,m.address as address, m.contactno as contactnumber, m.serialno as serialNumber, m.specimenid as specimenID, " +
                    "m.barcode as barcode, m.receiveddate as receivedDate ,s.description as statusDescription, p.code as plateCode, m.blockvalue as blockValue, r.description as resultDescription, m.createduser as createdUser, m.createdtime as createdTime, m.reporttime as reportTime, m.ct_target1 as ct_target1, m.ct_target2 as ct_target2 " +
                    "from master_data m " +
                    "left join status s on s.code = m.status " +
                    "left join plate p on m.plateid = p.id " +
                    "left join institution i on i.institutioncode = m.institutioncode " +
                    "left join result r on r.code = m.result where m.id = '" + masterDataInputBeen.getId() + "' " +sortingStr;

            masterData = jdbcTemplate.queryForObject(sql, new RowMapper<MasterData>() {
                @Override
                public MasterData mapRow(ResultSet rs, int rowNum) throws SQLException {
                    MasterData m = new MasterData();
                    try {
                        m.setId(rs.getInt("id"));
                    } catch (Exception e) {
                        m.setId(0);
                    }

                    try {
                        m.setSampleID(rs.getInt("sampleid"));
                    } catch (Exception e) {
                        m.setSampleID(0);
                    }

                    try {
                        m.setReferenceNumber(rs.getString("referenceNumber"));
                    } catch (Exception e) {
                        m.setReferenceNumber(null);
                    }

                    try {
                        m.setInstitutionCode(rs.getString("institutionCode"));
                    } catch (Exception e) {
                        m.setInstitutionCode(null);
                    }

                    try {
                        m.setInstitutionName(rs.getString("institutionName"));
                    } catch (Exception e) {
                        m.setInstitutionName(null);
                    }

                    try {
                        m.setName(rs.getString("name"));
                    } catch (Exception e) {
                        m.setName(null);
                    }

                    try {
                        m.setAge(rs.getString("age"));
                    } catch (Exception e) {
                        m.setAge(null);
                    }

                    try {
                        m.setGender(rs.getString("gender"));
                    } catch (Exception e) {
                        m.setGender(null);
                    }

                    try {
                        m.setNic(rs.getString("nic"));
                    } catch (Exception e) {
                        m.setNic(null);
                    }

                    try {
                        m.setAddress(rs.getString("address"));
                    } catch (Exception e) {
                        m.setAddress(null);
                    }

                    try {
                        m.setContactNumber(rs.getString("contactNumber"));
                    } catch (Exception e) {
                        m.setContactNumber(null);
                    }

                    try {
                        m.setSerialNumber(rs.getString("serialNumber"));
                    } catch (Exception e) {
                        m.setSerialNumber(null);
                    }

                    try {
                        m.setSpecimenID(rs.getString("specimenID"));
                    } catch (Exception e) {
                        m.setSpecimenID(null);
                    }

                    try {
                        m.setBarcode(rs.getString("barcode"));
                    } catch (Exception e) {
                        m.setBarcode(null);
                    }

                    try {
                        m.setReceivedDate(rs.getString("receivedDate"));
                    } catch (Exception e) {
                        m.setReceivedDate(null);
                    }

                    try {
                        m.setStatusDescription(rs.getString("statusDescription"));
                    } catch (Exception e) {
                        m.setStatusDescription(null);
                    }

                    try {
                        m.setPlateCode(rs.getString("plateCode"));
                    } catch (Exception e) {
                        m.setPlateCode(null);
                    }

                    try {
                        m.setBlockValue(rs.getString("blockValue"));
                    } catch (Exception e) {
                        m.setBlockValue(null);
                    }

                    try {
                        m.setResultDescription(rs.getString("resultDescription"));
                    } catch (Exception e) {
                        m.setResultDescription(null);
                    }

                    try {
                        m.setCt_target1(rs.getString("ct_target1"));
                    } catch (Exception e) {
                        m.setCt_target1(null);
                    }

                    try {
                        m.setCt_target2(rs.getString("ct_target2"));
                    } catch (Exception e) {
                        m.setCt_target2(null);
                    }

                    try {
                        m.setCreatedUser(rs.getString("createdUser"));
                    } catch (Exception e) {
                        m.setCreatedUser(null);
                    }

                    try {
                        m.setCreatedTime(rs.getTimestamp("createdTime"));
                    } catch (Exception e) {
                        m.setCreatedTime(null);
                    }
                    return m;
                }
            });
        } catch (Exception exception) {
            throw exception;
        }
        return masterData;
    }

    private StringBuilder setDynamicClause(MasterDataInputBeen masterDataInputBeen, StringBuilder dynamicClause) {
        dynamicClause.append("1=1 ");
        try {
            if (masterDataInputBeen.getReceivedDate() != null) {
                dynamicClause.append(" and m.receiveddate = '").append(masterDataInputBeen.getReceivedDate()).append("'");
            }

            if (masterDataInputBeen.getReferenceNumber() != null && !masterDataInputBeen.getReferenceNumber().isEmpty()) {
                dynamicClause.append("and lower(m.referenceno) like lower('%").append(masterDataInputBeen.getReferenceNumber()).append("%') ");
            }

            if (masterDataInputBeen.getName() != null && !masterDataInputBeen.getName().isEmpty()) {
                dynamicClause.append("and lower(m.name) like lower('%").append(masterDataInputBeen.getName()).append("%') ");
            }

            if (masterDataInputBeen.getNic() != null && !masterDataInputBeen.getNic().isEmpty()) {
                dynamicClause.append("and lower(m.nic) like lower('%").append(masterDataInputBeen.getNic()).append("%') ");
            }

            if (masterDataInputBeen.getStatus() != null && !masterDataInputBeen.getStatus().isEmpty()) {
                dynamicClause.append("and m.status = '").append(masterDataInputBeen.getStatus()).append("' ");
            }

            if (masterDataInputBeen.getInstitutionCode() != null && !masterDataInputBeen.getInstitutionCode().isEmpty()) {
                dynamicClause.append("and m.institutioncode = '").append(masterDataInputBeen.getInstitutionCode()).append("' ");
            }

            if (masterDataInputBeen.getResult() != null && !masterDataInputBeen.getResult().isEmpty()) {
                dynamicClause.append("and m.result = '").append(masterDataInputBeen.getResult()).append("' ");
            }
        } catch (Exception exception) {
            throw exception;
        }
        return dynamicClause;
    }


}
