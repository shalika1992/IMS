package com.epic.ims.bean.profile;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PasswordChangeBean {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String oldHashPassword;
    private String newHashPassword;
    private Date passwordExpiryDate;
}
