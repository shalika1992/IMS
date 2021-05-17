package com.epic.ims.repository.resultupdate;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.resultupdate.ResultUpdateInputBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.plate.Plate;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private final String SQL_GET_LIST_DATA_COUNT = "select count(*) from master_data md left outer join status s on s.code=md.status where";

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
    public List<Plate> getResultUpdateSearchList(ResultUpdateInputBean resultUpdateInputBean) {
        List<Plate> plateList = null;
        try {

        } catch (EmptyResultDataAccessException ex) {
            return plateList;
        } catch (Exception e) {
            throw e;
        }
        return plateList;
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

//    private String receivedDate;
//    private String referenceNo;
//    private String name;
//    private String nic;
//    private String institutionCode;
//    private String plateId;

    private StringBuilder setDynamicClause(ResultUpdateInputBean resultUpdateInputBean, StringBuilder dynamicClause) {
        dynamicClause.append(" 1=1 and md.status = '").append(commonVarList.STATUS_PLATEASSIGNED).append("'");
        try {
            if (resultUpdateInputBean.getReceivedDate() != null && !resultUpdateInputBean.getReceivedDate().isEmpty()) {
                dynamicClause.append(" and md.receiveddate = '").append(resultUpdateInputBean.getReceivedDate()).append("'");
            }

        } catch (Exception e) {
            throw e;
        }
        return dynamicClause;
    }
}
