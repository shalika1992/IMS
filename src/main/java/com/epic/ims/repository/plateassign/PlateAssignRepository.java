package com.epic.ims.repository.plateassign;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.plate.PlateBean;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.util.varlist.CommonVarList;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
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

    private final String SQL_GET_DEFAULT_PLATE_LIST = "select @n := @n + 1 n,id,referenceno,name,nic from sample_data, (select @n := -1) m where status = ? and receiveddate=? order by id";

    @LogRepository
    @Transactional(readOnly = true)
    public Map<Integer, List<String>> getDefaultPlateList(String receivedDate) {
        Map<Integer, List<String>> defaultPlateMap = new HashMap<>();
        try {
            jdbcTemplate.query(SQL_GET_DEFAULT_PLATE_LIST, new Object[]{commonVarList.STATUS_VALIDATED, receivedDate}, (ResultSet rs) -> {
                while (rs.next()) {
                    defaultPlateMap.put(rs.getInt("n"), Arrays.asList(
                            rs.getString("id"),
                            rs.getString("referenceno"),
                            rs.getString("name"),
                            rs.getString("nic")
                    ));
                }
                System.out.println("repo1:"+ defaultPlateMap);
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
    public String swapBlockPlate(PlateBean plateBean) {
        String message = "";
        try {
            System.out.println("--------------plate array--------------------------");
            plateBean.getPlateArray().forEach((key, value) -> System.out.println(key + "--:" + new Gson().toJson(plateBean.getPlateArray())));
            System.out.println("--------------plate array--------------------------");


            System.out.println("--------------swap array--------------------------");
            plateBean.getSwapArray().forEach((key, value) -> System.out.println(key + "--:" + new Gson().toJson(plateBean.getSwapArray())));
            System.out.println("--------------swap array--------------------------");

            message = "DB updated swap";

        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex);
        } catch (Exception e) {
            logger.error(e);
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
