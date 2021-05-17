package com.epic.ims.repository.plateassign;

import com.epic.ims.annotation.logrespository.LogRepository;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.repository.common.CommonRepository;
import com.epic.ims.repository.login.LoginRepository;
import com.epic.ims.util.varlist.CommonVarList;
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
    private static Logger logger = LogManager.getLogger(LoginRepository.class);

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
}
