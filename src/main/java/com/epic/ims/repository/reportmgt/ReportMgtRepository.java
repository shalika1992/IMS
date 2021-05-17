//package com.epic.ims.repository.reportmgt;
//
//import com.epic.ims.bean.institutionmgt.masterDataInputBeen;
//import com.epic.ims.bean.reportmgt.MasterDataInputBeen;
//import com.epic.ims.bean.session.SessionBean;
//import com.epic.ims.service.sysuser.common.CommonService;
//import com.epic.ims.util.varlist.CommonVarList;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Scope;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//@Repository
//@Scope("prototype")
//public class ReportMgtRepository {
//    private final Log logger = LogFactory.getLog(getClass());
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    SessionBean sessionBean;
//
//    @Autowired
//    CommonService commonService;
//
//    @Autowired
//    CommonVarList commonVarList;
//
//    @Autowired
//    MessageSource messageSource;
//
//    @Transactional(readOnly = true)
//    public long getCount(masterDataInputBeen masterDataInputBeen) throws Exception {
//        long count = 0;
//
//        try {
//            StringBuilder dynamicClause = new StringBuilder(SQL_GET_COUNT);
//            this.setDynamicClause(masterDataInputBeen, dynamicClause);
//
//            count = jdbcTemplate.queryForObject(dynamicClause.toString(), Long.class);
//
//        }catch (Exception exception){
//            logger.error(exception);
//            throw exception;
//        }
//
//        return count;
//    }
//
//    private StringBuilder setDynamicClause(MasterDataInputBeen masterDataInputBeen, StringBuilder dynamicClause){
//        dynamicClause.append("1=1 ");
//
//        try{
//            if (masterDataInputBeen.getReceivedDate()!=null){
//                dynamicClause.append("and m.receiveddate = '").append(masterDataInputBeen.getReceivedDate()).append("' ");
//            }
//
//            if (masterDataInputBeen.getReferenceNumber()!=null && !masterDataInputBeen.getReferenceNumber().isEmpty()){
//                dynamicClause.append("and lower(m.referenceno) like lower('%").append(masterDataInputBeen.getReferenceNumber()).append("%') ");
//            }
//
//            if (masterDataInputBeen.getName()!=null && !masterDataInputBeen.getName().isEmpty()){
//                dynamicClause.append("and lower(m.name) like lower('%").append(masterDataInputBeen.getName()).append("%') ");
//            }
//
//            if (masterDataInputBeen.getNic()!=null && !masterDataInputBeen.getNic().isEmpty()){
//                dynamicClause.append("and lower(m.nic) like lower('%").append(masterDataInputBeen.getNic()).append("%') ");
//            }
//
//            if (masterDataInputBeen.getStatus()!=null && !masterDataInputBeen.getStatus().isEmpty()){
//                dynamicClause.append("and m.status = '").append(masterDataInputBeen.getStatus()).append("' ");
//            }
//
//            if (masterDataInputBeen.getInstitutionCode()!=null && !masterDataInputBeen.getInstitutionCode().isEmpty()){
//                dynamicClause.append("and m.institutioncode = '").append(masterDataInputBeen.getInstitutionCode()).append("' ");
//            }
//
//            if (masterDataInputBeen.getResult()!=null && !masterDataInputBeen.getResult().isEmpty()){
//                dynamicClause.append("and m.result = '").append(masterDataInputBeen.getResult()).append("' ");
//            }
//        }catch (Exception exception){
//            throw exception;
//        }
//
//        return dynamicClause;
//    }
//}
