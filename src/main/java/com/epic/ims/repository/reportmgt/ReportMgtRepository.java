package com.epic.ims.repository.reportmgt;

import com.epic.ims.bean.institutionmgt.InstitutionInputBean;
import com.epic.ims.bean.reportmgt.MasterDataInputBeen;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.institution.Institution;
import com.epic.ims.mapping.reportmgt.MasterData;
import com.epic.ims.service.common.CommonService;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public long getCount(MasterDataInputBeen masterDataInputBeen) throws Exception {
        long count = 0;

        try {
            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
            this.setDynamicClause(masterDataInputBeen, dynamicClause);

            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);

        }catch (Exception exception){
            logger.error(exception);
            throw exception;
        }

        return count;
    }

    @Transactional(readOnly = true)
    public List<MasterData> getMasterDataSearchList(MasterDataInputBeen masterDataInputBeen) {
        List<MasterData> masterDataList = null;

        try {
            StringBuilder dynamicClause = this.setDynamicClause(masterDataInputBeen, new StringBuilder());

            //create sorting order
            String sortingStr = "";
            String col = "";

            switch (masterDataInputBeen.sortedColumns.get(0)) {
                case 0:
                    col = "m.referenceno";
                    break;
                case 1:
                    col = "i.name";
                    break;
                case 2:
                    col = "m.name";
                    break;
                case 3:
                    col = "m.age";
                    break;
                case 4:
                    col = "m.gender";
                    break;
                case 6:
                    col = "m.nic";
                    break;
                case 7:
                    col = "m.contactno";
                    break;
                case 8:
                    col = "m.serialno";
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
                    col = "m.createdtime";
            }
            sortingStr = " order by " + col + " " + masterDataInputBeen.sortDirections.get(0);

            String sql = "select " +
                    "m.referenceno as referenceNumber, " +
                    "m.institutioncode as institutionCode, " +
                    "i.name as institutionName, " +
                    "m.name as name, " +
                    "m.age as age, " +
                    "m.gender as gender, " +
                    "m.nic as nic, " +
                    "m.contactno as contactnumber, " +
                    "m.serialno as serialNumber, " +
                    "m.specimenid as specimenID, " +
                    "m.barcode as barcode, " +
                    "m.receiveddate as receivedDate ," +
                    "s.description as statusDescription, " +
                    "p.code as plateCode, " +
                    "m.blockvalue as blockValue, " +
                    "r.description as resultDescription, " +
                    "m.createduser as createdUser, " +
                    "m.createdtime as createdTime, " +
                    "m.reporttime as reportTime " +
                    "from master_data m " +
                    "left join status s on s.code = m.status " +
                    "left join plate p on m.plateid = p.id "  +
                    "left join institution i on i.institutioncode = m.institutioncode " +
                    "left join result r on r.code = m.result " +
                    "where " +
                    dynamicClause.toString() + sortingStr +
                    " limit " + masterDataInputBeen.displayLength + " offset " + masterDataInputBeen.displayStart;

            System.out.println(sql);


            masterDataList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                MasterData masterData = new MasterData();

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
                    masterData.setReceivedDate(rs.getDate("receivedDate"));
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
                    masterData.setCreatedUser(rs.getString("createdUser"));
                } catch (Exception e) {
                    masterData.setCreatedUser(null);
                }

                try {
                    masterData.setCreatedTime(rs.getDate("createdTime"));
                } catch (Exception e) {
                    masterData.setCreatedTime(null);
                }


                try {
                    masterData.setReportTime(rs.getDate("reportTime"));
                } catch (Exception e) {
                    masterData.setReportTime(null);
                }

                return masterData;
            });

        } catch (Exception exception) {
            throw exception;
        }

        return masterDataList;
    }

    private StringBuilder setDynamicClause(MasterDataInputBeen masterDataInputBeen, StringBuilder dynamicClause){
        dynamicClause.append("1=1 ");

        try{
            if (masterDataInputBeen.getReceivedDate()!=null){
                dynamicClause.append("and m.receiveddate = '").append(masterDataInputBeen.getReceivedDate()).append("' ");
            }

            if (masterDataInputBeen.getReferenceNumber()!=null && !masterDataInputBeen.getReferenceNumber().isEmpty()){
                dynamicClause.append("and lower(m.referenceno) like lower('%").append(masterDataInputBeen.getReferenceNumber()).append("%') ");
            }

            if (masterDataInputBeen.getName()!=null && !masterDataInputBeen.getName().isEmpty()){
                dynamicClause.append("and lower(m.name) like lower('%").append(masterDataInputBeen.getName()).append("%') ");
            }

            if (masterDataInputBeen.getNic()!=null && !masterDataInputBeen.getNic().isEmpty()){
                dynamicClause.append("and lower(m.nic) like lower('%").append(masterDataInputBeen.getNic()).append("%') ");
            }

            if (masterDataInputBeen.getStatus()!=null && !masterDataInputBeen.getStatus().isEmpty()){
                dynamicClause.append("and m.status = '").append(masterDataInputBeen.getStatus()).append("' ");
            }

            if (masterDataInputBeen.getInstitutionCode()!=null && !masterDataInputBeen.getInstitutionCode().isEmpty()){
                dynamicClause.append("and m.institutioncode = '").append(masterDataInputBeen.getInstitutionCode()).append("' ");
            }

            if (masterDataInputBeen.getResult()!=null && !masterDataInputBeen.getResult().isEmpty()){
                dynamicClause.append("and m.result = '").append(masterDataInputBeen.getResult()).append("' ");
            }
        }catch (Exception exception){
            throw exception;
        }

        return dynamicClause;
    }
}
