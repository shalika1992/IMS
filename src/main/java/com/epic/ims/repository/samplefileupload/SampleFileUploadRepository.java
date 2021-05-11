package com.epic.ims.repository.samplefileupload;

import com.epic.ims.bean.samplefileupload.SampleFileInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.samplefile.SampleFile;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.varlist.MessageVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Scope("prototype")
public class SampleFileUploadRepository {
    @Autowired
    SessionBean sessionBean;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonRepository commonRepository;

    private final String SQL_GET_LIST_DATA_COUNT = "select count(*) from sample_data sd left outer join status s on s.code=sd.status where";
    private final String SQL_FIND_SAMPLEFILERECORD = "select sd.id , sd.referenceno, sd.institutioncode , sd.name , sd.age , sd.gender , sd.symptomatic , sd.contacttype , sd.nic , sd.address ,sd.status as status, sd.district , sd.contactno , sd.secondarycontactno , sd.specimenid , sd.barcode , sd.receiveddate , sd.createdtime as createdtime,sd.createduser as createduser from sample_data sd where sd.referenceno = ?";
    private final String SQL_UPDATE_SAMPLEFILERECORD = "update sample_data sd set name = ? , age = ? , gender = ? , nic = ? , address = ? , district = ? , contactno = ? , secondarycontactno = ? where sd.referenceno = ?";

    @Transactional(readOnly = true)
    public long getDataCount(SampleFileInputBean sampleFileInputBean) {
        long count = 0;
        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_LIST_DATA_COUNT);
            //create the where clause
            dynamicClause = this.setDynamicClause(sampleFileInputBean, dynamicClause);
            //create the query
            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
        } catch (EmptyResultDataAccessException ere) {
            count = 0;
        } catch (Exception e) {
            throw e;
        }
        return count;
    }

    @Transactional(readOnly = true)
    public List<SampleFile> getSampleFileSearchList(SampleFileInputBean sampleFileInputBean) {
        List<SampleFile> sampleFileList = null;
        try {
            StringBuilder dynamicClause = this.setDynamicClause(sampleFileInputBean, new StringBuilder());
            //create sorting order
            String sortingStr = "";
            if (sampleFileInputBean.sortedColumns.get(0) == 0) {
                sortingStr = " order by sd.createdtime desc ";
            } else {
                sortingStr = " order by sd.createdtime " + sampleFileInputBean.sortDirections.get(0);
            }

            String sql = "" +
                    " select " +
                    " sd.id , sd.referenceno, sd.institutioncode , sd.name , sd.age , sd.gender , sd.symptomatic , sd.contacttype , sd.nic , sd.address ,s.description as statusdescription," +
                    " sd.district , sd.contactno , sd.secondarycontactno , sd.specimenid , sd.barcode , sd.receiveddate , sd.createdtime as createdtime,sd.createduser as createduser from sample_data sd " +
                    " left outer join status s on s.code=sd.status " +
                    " where " + dynamicClause.toString() + sortingStr +
                    " limit " + sampleFileInputBean.displayLength + " offset " + sampleFileInputBean.displayStart;

            sampleFileList = jdbcTemplate.query(sql, (rs, id) -> {
                SampleFile sampleFile = new SampleFile();
                try {
                    sampleFile.setId(rs.getInt("code"));
                } catch (Exception e) {
                    sampleFile.setId(0);
                }

                try {
                    sampleFile.setReferenceNo(rs.getString("referenceno"));
                } catch (Exception e) {
                    sampleFile.setReferenceNo(null);
                }

                try {
                    sampleFile.setInstitutionCode(rs.getString("institutioncode"));
                } catch (Exception e) {
                    sampleFile.setInstitutionCode(null);
                }

                try {
                    sampleFile.setName(rs.getString("name"));
                } catch (Exception e) {
                    sampleFile.setName(null);
                }

                try {
                    sampleFile.setAge(rs.getString("age"));
                } catch (Exception e) {
                    sampleFile.setAge(null);
                }

                try {
                    sampleFile.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    sampleFile.setGender(null);
                }

                try {
                    sampleFile.setContactType(rs.getString("contacttype"));
                } catch (Exception e) {
                    sampleFile.setContactType(null);
                }

                try {
                    sampleFile.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    sampleFile.setNic(null);
                }

                try {
                    sampleFile.setAddress(rs.getString("address"));
                } catch (Exception e) {
                    sampleFile.setAddress(null);
                }

                try {
                    sampleFile.setStatus(rs.getString("statusdescription"));
                } catch (Exception e) {
                    sampleFile.setStatus(null);
                }

                try {
                    sampleFile.setResidentDistrict(rs.getString("district"));
                } catch (Exception e) {
                    sampleFile.setResidentDistrict(null);
                }

                try {
                    sampleFile.setContactNumber(rs.getString("contactno"));
                } catch (Exception e) {
                    sampleFile.setContactNumber(null);
                }

                try {
                    sampleFile.setSecondaryContactNumber(rs.getString("secondarycontactno"));
                } catch (Exception e) {
                    sampleFile.setSecondaryContactNumber(null);
                }

                try {
                    sampleFile.setSpecimenid(rs.getString("specimenid"));
                } catch (Exception e) {
                    sampleFile.setSpecimenid(null);
                }

                try {
                    sampleFile.setSpecimenid(rs.getString("specimenid"));
                } catch (Exception e) {
                    sampleFile.setSpecimenid(null);
                }

                try {
                    sampleFile.setBarcode(rs.getString("barcode"));
                } catch (Exception e) {
                    sampleFile.setBarcode(null);
                }

                try {
                    sampleFile.setReceivedDate(rs.getString("receiveddate"));
                } catch (Exception e) {
                    sampleFile.setReceivedDate(null);
                }

                try {
                    sampleFile.setCreatedTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    sampleFile.setCreatedTime(null);
                }

                try {
                    sampleFile.setCreatedUser(rs.getString("createduser"));
                } catch (Exception e) {
                    sampleFile.setCreatedUser(null);
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

    @Transactional(readOnly = true)
    public SampleFile getSampleRecord(String referenceNo) {
        SampleFile sampleFile;
        try {
            sampleFile = jdbcTemplate.queryForObject(SQL_FIND_SAMPLEFILERECORD, new Object[]{referenceNo}, (rs, rowNum) -> {
                SampleFile s = new SampleFile();
                try {
                    s.setId(rs.getInt("code"));
                } catch (Exception e) {
                    s.setId(0);
                }

                try {
                    s.setReferenceNo(rs.getString("referenceno"));
                } catch (Exception e) {
                    s.setReferenceNo(null);
                }

                try {
                    s.setInstitutionCode(rs.getString("institutioncode"));
                } catch (Exception e) {
                    s.setInstitutionCode(null);
                }

                try {
                    s.setName(rs.getString("name"));
                } catch (Exception e) {
                    s.setName(null);
                }

                try {
                    s.setAge(rs.getString("age"));
                } catch (Exception e) {
                    s.setAge(null);
                }

                try {
                    s.setGender(rs.getString("gender"));
                } catch (Exception e) {
                    s.setGender(null);
                }

                try {
                    s.setContactType(rs.getString("contacttype"));
                } catch (Exception e) {
                    s.setContactType(null);
                }

                try {
                    s.setNic(rs.getString("nic"));
                } catch (Exception e) {
                    s.setNic(null);
                }

                try {
                    s.setAddress(rs.getString("address"));
                } catch (Exception e) {
                    s.setAddress(null);
                }

                try {
                    s.setStatus(rs.getString("status"));
                } catch (Exception e) {
                    s.setStatus(null);
                }

                try {
                    s.setResidentDistrict(rs.getString("district"));
                } catch (Exception e) {
                    s.setResidentDistrict(null);
                }

                try {
                    s.setContactNumber(rs.getString("contactno"));
                } catch (Exception e) {
                    s.setContactNumber(null);
                }

                try {
                    s.setSecondaryContactNumber(rs.getString("secondarycontactno"));
                } catch (Exception e) {
                    s.setSecondaryContactNumber(null);
                }

                try {
                    s.setSpecimenid(rs.getString("specimenid"));
                } catch (Exception e) {
                    s.setSpecimenid(null);
                }

                try {
                    s.setSpecimenid(rs.getString("specimenid"));
                } catch (Exception e) {
                    s.setSpecimenid(null);
                }

                try {
                    s.setBarcode(rs.getString("barcode"));
                } catch (Exception e) {
                    s.setBarcode(null);
                }

                try {
                    s.setReceivedDate(rs.getString("receiveddate"));
                } catch (Exception e) {
                    s.setReceivedDate(null);
                }

                try {
                    s.setCreatedTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    s.setCreatedTime(null);
                }

                try {
                    s.setCreatedUser(rs.getString("createduser"));
                } catch (Exception e) {
                    s.setCreatedUser(null);
                }

                return s;
            });
        } catch (EmptyResultDataAccessException erse) {
            sampleFile = null;
        } catch (Exception e) {
            throw e;
        }
        return sampleFile;
    }

    @Transactional
    public String updateSampleFileRecord(SampleFileInputBean sampleFileInputBean) {
        String message = "";
        try {
            int value = jdbcTemplate.update(SQL_UPDATE_SAMPLEFILERECORD, new Object[]{
                    sampleFileInputBean.getName(),
                    sampleFileInputBean.getAge(),
                    sampleFileInputBean.getGender(),
                    sampleFileInputBean.getNic(),
                    sampleFileInputBean.getAddress(),
                    sampleFileInputBean.getResidentDistrict(),
                    sampleFileInputBean.getContactNumber(),
                    sampleFileInputBean.getSecondaryContactNumber(),
                    sampleFileInputBean.getReferenceNo(),
            });
            if (value != 1) {
                message = MessageVarList.COMMON_ERROR_PROCESS;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return message;
    }

    private StringBuilder setDynamicClause(SampleFileInputBean sampleFileInputBean, StringBuilder dynamicClause) {
        dynamicClause.append(" 1=1 ");
        try {
            if (sampleFileInputBean.getReceivedDate() != null && !sampleFileInputBean.getReceivedDate().isEmpty()) {
                //add to dynamic clause
                dynamicClause.append(" and sd.receiveddate >='").append(sampleFileInputBean.getReceivedDate()).append("'");
            }

            if (sampleFileInputBean.getReferenceNo() != null && !sampleFileInputBean.getReferenceNo().isEmpty()) {
                dynamicClause.append(" and sd.referenceno like '%").append(sampleFileInputBean.getReferenceNo()).append("%'");
            }

            if (sampleFileInputBean.getInstitutionCode() != null && !sampleFileInputBean.getInstitutionCode().isEmpty()) {
                dynamicClause.append(" and sd.institutioncode like '%").append(sampleFileInputBean.getInstitutionCode()).append("%'");
            }

            if (sampleFileInputBean.getName() != null && !sampleFileInputBean.getName().isEmpty()) {
                dynamicClause.append(" and sd.name like '%").append(sampleFileInputBean.getName()).append("%'");
            }

            if (sampleFileInputBean.getNic() != null && !sampleFileInputBean.getNic().isEmpty()) {
                dynamicClause.append(" and sd.nic = '").append(sampleFileInputBean.getNic()).append("'");
            }
        } catch (Exception e) {
            throw e;
        }
        return dynamicClause;
    }
}
