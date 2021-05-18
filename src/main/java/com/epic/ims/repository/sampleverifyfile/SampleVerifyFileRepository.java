package com.epic.ims.repository.sampleverifyfile;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
public class SampleVerifyFileRepository {
    private final Log logger = LogFactory.getLog(getClass());

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

    private final String SQL_GET_COUNT = "select count(*) from sample_data i where ";
    private final String SQL_FIND_SAMPLEDATAVERIFICATION = "select id, referenceno, institutioncode, name, age, gender, symptomatic, contacttype, nic, address, district, contactno, secondarycontactno, specimenid, barcode, receiveddate, status,createduser, createdtime from sample_data where id = ?";

    @LogRepository
    @Transactional(readOnly = true)
    public long getDataCount(SampleFileVerificationInputBean sampleFileVerificationInputBean) throws Exception {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            //create the where clause
            dynamicClause =this.setDynamicClause(sampleFileVerificationInputBean, dynamicClause);
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
    public List<SampleVerifyFile> getSampleVerifyFileSearchList(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        List<SampleVerifyFile> sampleVerifyFileList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(sampleFileVerificationInputBean, new StringBuilder());
            //create sorting order
            String sortingStr = "";
            String col = "";

            switch (sampleFileVerificationInputBean.sortedColumns.get(0)) {
                case 0:
                    col = "i.id";
                    break;
                case 1:
                    col = "i.referenceno ";
                case 3:
                    col = "i.institutioncode ";
                    break;
                case 4:
                    col = "i.name";
                    break;
                case 5:
                    col = "i.age";
                    break;
                case 6:
                    col = "i.gender";
                    break;
                case 7:
                    col = "i.symptomatic ";
                    break;
                case 8:
                    col = "i.contacttype ";
                    break;
                case 9:
                    col = "i.nic";
                    break;
                case 10:
                    col = "i.address";
                    break;
                case 11:
                    col = "i.district ";
                    break;
                case 12:
                    col = "i.contactno";
                    break;
                case 13:
                    col = "i.secondarycontactno ";
                    break;
                case 14:
                    col = "i.specimenid";
                    break;
                case 16:
                    col = "i.barcode";
                    break;
                case 17:
                    col = "i.receiveddate";
                    break;
                case 18:
                    col = "s.description";
                    break;
                case 19:
                    col = "i.createduser";
                    break;
                default:
                    col = "i.createdtime";
            }
            sortingStr = " order by " + col + " " + sampleFileVerificationInputBean.sortDirections.get(0);

            String sql = "" +
                    "select i.id as id, i.referenceno as referenceno, i.institutioncode as institutioncode, " +
                    " i.name as name, i.age as age, i.gender as gender,i.symptomatic as symptomatic, i.contacttype as contacttype, i.nic as nic, " +
                    " i.address as address, i.district as residentdistrict, i.secondarycontactno as secondarycontactnumber, i.specimenid as specimenid, " +
                    " i.barcode as barcode, i.receiveddate as receiveddate, s.description as status, i.createduser as createduser, i.createdtime as createdtime " +
                    " from sample_data i left join status s on s.code = i.status where " + dynamicClause.toString() + sortingStr +
                    " limit " + sampleFileVerificationInputBean.displayLength + " offset " + sampleFileVerificationInputBean.displayStart;

            sampleVerifyFileList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                SampleVerifyFile sampleVerifyFile = new SampleVerifyFile();

                try {
                    sampleVerifyFile.setId(rs.getInt("id"));
                } catch (Exception e) {
                    sampleVerifyFile.setId(0);
                }

                try {
                    sampleVerifyFile.setReferenceNo(rs.getString("referenceno"));
                } catch (Exception e) {
                    sampleVerifyFile.setReferenceNo("--");
                }

                try {
                    sampleVerifyFile.setReceivedDate(rs.getString("receiveddate"));
                } catch (Exception e) {
                    sampleVerifyFile.setReceivedDate("--");
                }

                try {
                    sampleVerifyFile.setInstitutionCode(rs.getString("institutioncode"));
                } catch (Exception e) {
                    sampleVerifyFile.setInstitutionCode("--");
                }

                try {
                    sampleVerifyFile.setName(rs.getString("name"));
                } catch (Exception e) {
                    sampleVerifyFile.setName("--");
                }

                try {
                    sampleVerifyFile.setAge(rs.getString("age"));
                } catch (Exception e) {
                    sampleVerifyFile.setAge("--");
                }

                try {
                    sampleVerifyFile.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    sampleVerifyFile.setGender("--");
                }

                try {
                    sampleVerifyFile.setSymptomatic(rs.getString("symptomatic"));
                } catch (Exception e) {
                    sampleVerifyFile.setSymptomatic("--");
                }

                try {
                    sampleVerifyFile.setContactType(rs.getString("contacttype"));
                } catch (Exception e) {
                    sampleVerifyFile.setContactType("--");
                }

                try {
                    sampleVerifyFile.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    sampleVerifyFile.setNic("--");
                }

                try {
                    sampleVerifyFile.setAddress(rs.getString("address"));
                } catch (Exception e) {
                    sampleVerifyFile.setAddress("--");
                }

                try {
                    sampleVerifyFile.setStatus(rs.getString("status"));
                } catch (Exception e) {
                    sampleVerifyFile.setStatus("--");
                }

                try {
                    sampleVerifyFile.setResidentDistrict(rs.getString("residentdistrict"));
                } catch (Exception e) {
                    sampleVerifyFile.setResidentDistrict("--");
                }

                try {
                    sampleVerifyFile.setContactNumber(rs.getString("contactnumber"));
                } catch (Exception e) {
                    sampleVerifyFile.setContactNumber("--");
                }

                try {
                    sampleVerifyFile.setSecondaryContactNumber(rs.getString("secondarycontactnumber"));
                } catch (Exception e) {
                    sampleVerifyFile.setSecondaryContactNumber("--");
                }

                try {
                    sampleVerifyFile.setSpecimenid(rs.getString("specimenid"));
                } catch (Exception e) {
                    sampleVerifyFile.setSpecimenid("--");
                }

                try {
                    sampleVerifyFile.setBarcode(rs.getString("barcode"));
                } catch (Exception e) {
                    sampleVerifyFile.setBarcode("--");
                }

                try {
                    sampleVerifyFile.setCreatedTime(new Date(rs.getDate("createdtime").getTime()));
                } catch (Exception e) {
                    sampleVerifyFile.setCreatedTime(null);
                }

                try {
                    sampleVerifyFile.setCreatedUser(rs.getString("createduser"));
                } catch (SQLException e) {
                    sampleVerifyFile.setCreatedUser("--");
                }
                return sampleVerifyFile;
            });
        } catch (Exception exception) {
            throw exception;
        }
        return sampleVerifyFileList;
    }

    @LogRepository
    @Transactional(readOnly = true)
    public SampleVerifyFile getSampleVerifyRecord(String id) throws SQLException {
        SampleVerifyFile sampleVerifyFile = null;
        try {
            sampleVerifyFile = jdbcTemplate.queryForObject(SQL_FIND_SAMPLEDATAVERIFICATION, new Object[]{id}, new RowMapper<SampleVerifyFile>() {
                @Override
                public SampleVerifyFile mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SampleVerifyFile sampleVerifyFile = new SampleVerifyFile();
                    try {
                        sampleVerifyFile.setReferenceNo(rs.getString("id"));
                    } catch (Exception e) {
                        sampleVerifyFile.setId(0);
                    }

                    try {
                        sampleVerifyFile.setReferenceNo(rs.getString("referenceno"));
                    } catch (Exception e) {
                        sampleVerifyFile.setReferenceNo(null);
                    }

                    try {
                        sampleVerifyFile.setReceivedDate(rs.getString("receiveddate"));
                    } catch (Exception e) {
                        sampleVerifyFile.setReceivedDate(null);
                    }

                    try {
                        sampleVerifyFile.setInstitutionCode(rs.getString("institutioncode"));
                    } catch (Exception e) {
                        sampleVerifyFile.setInstitutionCode(null);
                    }

                    try {
                        sampleVerifyFile.setName(rs.getString("name"));
                    } catch (Exception e) {
                        sampleVerifyFile.setName(null);
                    }

                    try {
                        sampleVerifyFile.setAge(rs.getString("age"));
                    } catch (Exception e) {
                        sampleVerifyFile.setAge(null);
                    }

                    try {
                        sampleVerifyFile.setGender(rs.getString("gender"));
                    } catch (Exception e) {
                        sampleVerifyFile.setGender(null);
                    }

                    try {
                        sampleVerifyFile.setSymptomatic(rs.getString("symptomatic"));
                    } catch (Exception e) {
                        sampleVerifyFile.setSymptomatic(null);
                    }

                    try {
                        sampleVerifyFile.setContactType(rs.getString("contacttype"));
                    } catch (Exception e) {
                        sampleVerifyFile.setContactType(null);
                    }

                    try {
                        sampleVerifyFile.setNic(rs.getString("nic"));
                    } catch (Exception e) {
                        sampleVerifyFile.setNic(null);
                    }

                    try {
                        sampleVerifyFile.setAddress(rs.getString("address"));
                    } catch (Exception e) {
                        sampleVerifyFile.setAddress(null);
                    }

                    try {
                        sampleVerifyFile.setStatus(rs.getString("status"));
                    } catch (Exception e) {
                        sampleVerifyFile.setStatus(null);
                    }

                    try {
                        sampleVerifyFile.setResidentDistrict(rs.getString("residentdistrict"));
                    } catch (Exception e) {
                        sampleVerifyFile.setResidentDistrict(null);
                    }

                    try {
                        sampleVerifyFile.setContactNumber(rs.getString("contactnumber"));
                    } catch (Exception e) {
                        sampleVerifyFile.setContactNumber(null);
                    }

                    try {
                        sampleVerifyFile.setSecondaryContactNumber(rs.getString("secondarycontactnumber"));
                    } catch (Exception e) {
                        sampleVerifyFile.setSecondaryContactNumber(null);
                    }

                    try {
                        sampleVerifyFile.setSpecimenid(rs.getString("specimenid"));
                    } catch (Exception e) {
                        sampleVerifyFile.setSpecimenid(null);
                    }

                    try {
                        sampleVerifyFile.setBarcode(rs.getString("barcode"));
                    } catch (Exception e) {
                        sampleVerifyFile.setBarcode(null);
                    }


                    try {
                        sampleVerifyFile.setCreatedTime(new Date(rs.getDate("createdtime").getTime()));
                    } catch (Exception e) {
                        sampleVerifyFile.setCreatedTime(null);
                    }

                    try {
                        sampleVerifyFile.setCreatedUser(rs.getString("createduser"));
                    } catch (SQLException e) {
                        sampleVerifyFile.setCreatedUser(null);
                    }

                    return sampleVerifyFile;
                }
            });
        } catch (EmptyResultDataAccessException erse) {
            sampleVerifyFile = null;
        } catch (Exception e) {
            throw e;
        }
        return sampleVerifyFile;
    }

    public String rejectSampleRecord(SampleFileVerificationInputBean sampleFileVerificationInputBean) {
        return null;
    }

    private StringBuilder setDynamicClause(SampleFileVerificationInputBean sampleFileVerificationInputBean, StringBuilder dynamicClause) {
        dynamicClause.append("1=1 ");
        try {
            if (sampleFileVerificationInputBean.getReceivedDate() != null && !sampleFileVerificationInputBean.getReceivedDate().isEmpty()) {
                dynamicClause.append(" and i.receiveddate = '").append(sampleFileVerificationInputBean.getReceivedDate()).append("'");
            }

            if (sampleFileVerificationInputBean.getInstitutionCode() != null && !sampleFileVerificationInputBean.getInstitutionCode().isEmpty()) {
                dynamicClause.append(" and i.institutioncode = '").append(sampleFileVerificationInputBean.getInstitutionCode()).append("'");
            }

            if (sampleFileVerificationInputBean.getReferenceNo() != null && !sampleFileVerificationInputBean.getReferenceNo().isEmpty()) {
                dynamicClause.append("and lower(i.referenceno) like lower('%").append(sampleFileVerificationInputBean.getReferenceNo()).append("%') ");
            }

            if (sampleFileVerificationInputBean.getStatus() != null && !sampleFileVerificationInputBean.getStatus().isEmpty()) {
                dynamicClause.append(" and i.status = '").append(sampleFileVerificationInputBean.getStatus()).append("'");
            }
        } catch (Exception exception) {
            throw exception;
        }
        return dynamicClause;
    }
}
