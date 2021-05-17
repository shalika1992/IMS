package com.epic.ims.repository.sampleverifyfile;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.samplefileverification.SampleFileVerificationInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.sampleverifyfile.SampleVerifyFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
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
import java.util.Date;
import java.util.List;

@Repository
@Scope("prototype")
public class SampleVerifyFileRepository {
    private static Logger logger = LogManager.getLogger(SampleVerifyFileRepository.class);

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
    private final String SQL_FIND_SAMPLEDATAVERIFICATION = "select referenceNo, receivedDate, institutionCode, status from sample_data where id = ?";

    @LogRepository
    @Transactional(readOnly = true)
    public long getDataCount(SampleFileVerificationInputBean sampleFileVerificationInputBean) throws Exception {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            //create the where clause
            dynamicClause = this.setDynamicClause(sampleFileVerificationInputBean, dynamicClause);
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
                    break;
                case 2:
                    col = "i.receiveddate";
                    break;
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
                    col = "s.status";
                    break;
                case 11:
                    col = "i.address";
                    break;
                case 12:
                    col = "i.district ";
                    break;
                case 13:
                    col = "i.contactno";
                    break;
                case 14:
                    col = "i.secondarycontactno ";
                    break;
                case 15:
                    col = "i.createduser";
                    break;
                case 16:
                    col = "i.specimenid ";
                    break;
                case 17:
                    col = "i.barcode ";
                    break;
                default:
                    col = "i.createdtime";
            }
            sortingStr = " order by " + col + " " + sampleFileVerificationInputBean.sortDirections.get(0);

            String sql = "select i.id as id, i.referenceno as referenceno, i.receiveddate as receiveddate, i.institutioncode as institutioncode, " +
                    "i.name as name, i.age = age, i.gender = gender,i.symptomatic = symptomatic, i.contacttype = contacttype, i.nic = nic " +
                    "i.address as address, i.district = district, i.contactno = contactno, i.secondarycontactno = secondarycontactno, i.specimenid  = specimenid" +
                    "s.description as statusdescription, " +
                    "i.createdtime as createdtime, i.createduser as createduser, i.barcode as barcode, i.specimenid =specimenid " +
                    "from sample_data i " +
                    "left join status s on s.code = i.status where " +
                    dynamicClause.toString() + sortingStr +
                    " limit " + sampleFileVerificationInputBean.displayLength + " offset " + sampleFileVerificationInputBean.displayStart;


            sampleVerifyFileList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                SampleVerifyFile sampleVerifyFile = new SampleVerifyFile();

                try {
                    sampleVerifyFile.setReferenceNo(rs.getString("referenceNo"));
                } catch (Exception e) {
                    sampleVerifyFile.setReferenceNo(null);
                }

                try {
                    sampleVerifyFile.setReceivedDate(rs.getString("receivedDate"));
                } catch (Exception e) {
                    sampleVerifyFile.setReceivedDate(null);
                }

                try {
                    sampleVerifyFile.setInstitutionCode(rs.getString("institutionCode"));
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
                    sampleVerifyFile.setContactType(rs.getString("contactType"));
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
                    sampleVerifyFile.setResidentDistrict(rs.getString("residentDistrict"));
                } catch (Exception e) {
                    sampleVerifyFile.setResidentDistrict(null);
                }

                try {
                    sampleVerifyFile.setContactNumber(rs.getString("contactNumber"));
                } catch (Exception e) {
                    sampleVerifyFile.setContactNumber(null);
                }

                try {
                    sampleVerifyFile.setSecondaryContactNumber(rs.getString("secondaryContactNumber"));
                } catch (Exception e) {
                    sampleVerifyFile.setSecondaryContactNumber(null);
                }

                try {
                    sampleVerifyFile.setSpecimenid(rs.getString("specimenId"));
                } catch (Exception e) {
                    sampleVerifyFile.setSpecimenid(null);
                }

                try {
                    sampleVerifyFile.setBarcode(rs.getString("barCode"));
                } catch (Exception e) {
                    sampleVerifyFile.setBarcode(null);
                }


                try {
                    sampleVerifyFile.setCreatedTime(new Date(rs.getDate("createdTime").getTime()));
                } catch (Exception e) {
                    sampleVerifyFile.setCreatedTime(null);
                }

                try {
                    sampleVerifyFile.setCreatedUser(rs.getString("createdUser"));
                } catch (SQLException e) {
                    sampleVerifyFile.setCreatedUser(null);
                }

                try {
                    sampleVerifyFile.setLastUpdatedTime(new Date(rs.getDate("lastUpdatedTime").getTime()));
                } catch (SQLException e) {
                    sampleVerifyFile.setLastUpdatedTime(null);
                }

                try {
                    sampleVerifyFile.setLastUpdatedUser(rs.getString("lastUpdatedUser"));
                } catch (SQLException e) {
                    sampleVerifyFile.setLastUpdatedUser(null);
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
                        sampleVerifyFile.setReferenceNo(rs.getString("referenceNo"));
                    } catch (Exception e) {
                        sampleVerifyFile.setReferenceNo(null);
                    }

                    try {
                        sampleVerifyFile.setReceivedDate(rs.getString("receivedDate"));
                    } catch (Exception e) {
                        sampleVerifyFile.setReceivedDate(null);
                    }

                    try {
                        sampleVerifyFile.setInstitutionCode(rs.getString("institutionCode"));
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
                        sampleVerifyFile.setContactType(rs.getString("contactType"));
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
                        sampleVerifyFile.setResidentDistrict(rs.getString("residentDistrict"));
                    } catch (Exception e) {
                        sampleVerifyFile.setResidentDistrict(null);
                    }

                    try {
                        sampleVerifyFile.setContactNumber(rs.getString("contactNumber"));
                    } catch (Exception e) {
                        sampleVerifyFile.setContactNumber(null);
                    }

                    try {
                        sampleVerifyFile.setSecondaryContactNumber(rs.getString("secondaryContactNumber"));
                    } catch (Exception e) {
                        sampleVerifyFile.setSecondaryContactNumber(null);
                    }

                    try {
                        sampleVerifyFile.setSpecimenid(rs.getString("specimenId"));
                    } catch (Exception e) {
                        sampleVerifyFile.setSpecimenid(null);
                    }

                    try {
                        sampleVerifyFile.setBarcode(rs.getString("barCode"));
                    } catch (Exception e) {
                        sampleVerifyFile.setBarcode(null);
                    }

                    try {
                        sampleVerifyFile.setCreatedTime(new Date(rs.getDate("createdTime").getTime()));
                    } catch (Exception e) {
                        sampleVerifyFile.setCreatedTime(null);
                    }

                    try {
                        sampleVerifyFile.setCreatedUser(rs.getString("createdUser"));
                    } catch (SQLException e) {
                        sampleVerifyFile.setCreatedUser(null);
                    }

                    try {
                        sampleVerifyFile.setLastUpdatedTime(new Date(rs.getDate("lastUpdatedTime").getTime()));
                    } catch (SQLException e) {
                        sampleVerifyFile.setLastUpdatedTime(null);
                    }

                    try {
                        sampleVerifyFile.setLastUpdatedUser(rs.getString("lastUpdatedUser"));
                    } catch (SQLException e) {
                        sampleVerifyFile.setLastUpdatedUser(null);
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

    private StringBuilder setDynamicClause(SampleFileVerificationInputBean sampleFileVerificationInputBean, StringBuilder dynamicClause) {
        dynamicClause.append("1=1 ");
        try {
            if (sampleFileVerificationInputBean.getReceivedDate() != null && !sampleFileVerificationInputBean.getReceivedDate().isEmpty()) {
                dynamicClause.append("and lower(i.receivedDate) like lower('%").append(sampleFileVerificationInputBean.getReceivedDate()).append("%') ");
            }

            if (sampleFileVerificationInputBean.getInstitutionCode() != null && !sampleFileVerificationInputBean.getInstitutionCode().isEmpty()) {
                dynamicClause.append("and i.institutionCode like '%").append(sampleFileVerificationInputBean.getInstitutionCode()).append("%') ");
            }

            if (sampleFileVerificationInputBean.getReferenceNo() != null && !sampleFileVerificationInputBean.getReferenceNo().isEmpty()) {
                dynamicClause.append("and lower(i.referenceNo) like lower('%").append(sampleFileVerificationInputBean.getReferenceNo()).append("%') ");
            }

            if (sampleFileVerificationInputBean.getStatus() != null && !sampleFileVerificationInputBean.getStatus().isEmpty()) {
                dynamicClause.append("and i.status like '%").append(sampleFileVerificationInputBean.getStatus()).append("%' ");
            }
        } catch (Exception exception) {
            throw exception;
        }
        return dynamicClause;
    }
}
