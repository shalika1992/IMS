package com.epic.ims.repository.passwordpolicy;

import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.mapping.passwordpolicy.PasswordPolicy;
import com.epic.ims.util.common.Common;
import com.epic.ims.util.varlist.CommonVarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope("prototype")
public class PasswordPolicyRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    Common common;

    @Autowired
    SessionBean sessionBean;

    private final String SQL_FIND_WEB_PASSWORDPOLICY = "select passwordpolicyid , minimumlength , maximumlength , minimumspecialcharacters , minimumuppercasecharacters , minimumnumericalcharacters , minimumlowercasecharacters ,createduser,createdtime, lastupdateduser , lastupdatedtime from web_passwordpolicy where passwordpolicyid=?";

    @Transactional(readOnly = true)
    public PasswordPolicy getWebPasswordPolicy(int passwordPolicyCode) {
        PasswordPolicy passwordPolicy = null;
        try {
            passwordPolicy = jdbcTemplate.queryForObject(SQL_FIND_WEB_PASSWORDPOLICY, new Object[]{passwordPolicyCode}, (rs, rowNum) -> {
                PasswordPolicy pwdPolicy = new PasswordPolicy();

                try {
                    pwdPolicy.setPasswordPolicyId(rs.getLong("passwordpolicyid"));
                } catch (Exception e) {
                    pwdPolicy.setPasswordPolicyId(0);
                }

                try {
                    pwdPolicy.setMinimumLength(rs.getLong("minimumlength"));
                } catch (Exception e) {
                    pwdPolicy.setMinimumLength(0);
                }

                try {
                    pwdPolicy.setMaximumLength(rs.getLong("maximumlength"));
                } catch (Exception e) {
                    pwdPolicy.setMaximumLength(0);
                }

                try {
                    pwdPolicy.setMinimumSpecialCharacters(rs.getLong("minimumspecialcharacters"));
                } catch (Exception e) {
                    pwdPolicy.setMinimumSpecialCharacters(0);
                }

                try {
                    pwdPolicy.setMinimumUpperCaseCharacters(rs.getLong("minimumuppercasecharacters"));
                } catch (Exception e) {
                    pwdPolicy.setMinimumUpperCaseCharacters(0);
                }

                try {
                    pwdPolicy.setMinimumNumericalCharacters(rs.getLong("minimumnumericalcharacters"));
                } catch (Exception e) {
                    pwdPolicy.setMinimumNumericalCharacters(0);
                }

                try {
                    pwdPolicy.setMinimumLowerCaseCharacters(rs.getLong("minimumlowercasecharacters"));
                } catch (Exception e) {
                    pwdPolicy.setMinimumLowerCaseCharacters(0);
                }

                try {
                    pwdPolicy.setCreatedUser(rs.getString("createduser"));
                } catch (Exception e) {
                    pwdPolicy.setCreatedUser(null);
                }

                try {
                    pwdPolicy.setCreatedTime(rs.getDate("createdtime"));
                } catch (Exception e) {
                    pwdPolicy.setCreatedTime(null);
                }

                try {
                    pwdPolicy.setLastUpdatedUser(rs.getString("lastupdateduser"));
                } catch (Exception e) {
                    pwdPolicy.setLastUpdatedUser(null);
                }

                try {
                    pwdPolicy.setLastUpdatedTime(rs.getDate("lastupdatedtime"));
                } catch (Exception e) {
                    pwdPolicy.setLastUpdatedTime(null);
                }
                return pwdPolicy;
            });
        } catch (EmptyResultDataAccessException ex) {
            return passwordPolicy;
        } catch (Exception e) {
            throw e;
        }
        return passwordPolicy;
    }
}
