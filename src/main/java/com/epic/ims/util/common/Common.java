package com.epic.ims.util.common;

import com.epic.ims.bean.common.Status;
import com.epic.ims.bean.session.SessionBean;
import com.epic.ims.util.varlist.CommonVarList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Scope("prototype")
public class Common {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CommonVarList commonVarList;

    @Autowired
    SessionBean sessionBean;

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-01-22 12:20:25 PM
     * @Version V1.00
     * @MethodName formatDateToString
     * @MethodParams [date]
     * @MethodDescription - This method format the date to string
     */
    public String formatDateToString(Date date) {
        String strDate = "--";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (date != null) {
                strDate = dateFormat.format(date);
            } else {
                strDate = "--";
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return strDate;
    }

    /**
     * @Author shalika_w
     * @CreatedTime 2021-01-22 12:20:06 PM
     * @Version V1.00
     * @MethodName countRepeatedCharacters
     * @MethodParams [password]
     * @MethodDescription - This method calculate the repeat characters in a string
     */
    public int countRepeatedCharacters(String password) {
        int countMax = 0;
        int count = 1;
        int pos = 1;

        if (password != null && password.length() > 1) {
            for (pos = 1; pos < password.length(); pos++) {
                if (password.charAt(pos) == password.charAt(pos - 1)) {
                    count++;
                } else {
                    count = 1;
                }

                if (count > countMax) {
                    countMax = count;
                }
            }
        } else if (password != null) {
            countMax = 1;
        }
        return countMax;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 10:42:24 AM
     * @Version V1.00
     * @MethodName getActiveStatusList
     * @MethodParams []
     * @MethodDescription - returns allowed task list of current user
     */
    public List<Status> getActiveStatusList() {
        List<Status> statusList = new ArrayList<>();
        Status status = new Status();
        status.setStatusCode(commonVarList.STATUS_ACTIVE);
        status.setDescription("Active");
        statusList.add(status);
        return statusList;
    }


    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 10:42:45 AM
     * @Version V1.00
     * @MethodName replaceEmptyorNullStringToALL
     * @MethodParams [string]
     * @MethodDescription - replace employer null string to ALL
     */
    public String replaceEmptyorNullStringToALL(String string) {
        String value = "-ALL-";
        if (string != null && !string.trim().isEmpty()) {
            value = string;
        }
        return value;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 10:43:14 AM
     * @Version V1.00
     * @MethodName zipFiles
     * @MethodParams [listFiles]
     * @MethodDescription - create zip file
     */
    public static ByteArrayOutputStream zipFiles(File[] listFiles) throws Exception {
        byte[] buffer;
        ByteArrayOutputStream outputStream = null;
        ZipOutputStream zipOutputStream = null;
        FileInputStream fileInputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            zipOutputStream = new ZipOutputStream(outputStream);
            for (File file : listFiles) {
                buffer = new byte[(int) file.length()];
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(buffer, 0, (int) file.length());
                ZipEntry ze = new ZipEntry(file.getName());

                zipOutputStream.putNextEntry(ze);
                zipOutputStream.write(buffer);
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (zipOutputStream != null) {
                zipOutputStream.finish();
                zipOutputStream.close();
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
        return outputStream;
    }

    /**
     * @Author dilanka_w
     * @CreatedTime 2021-03-23 10:43:53 AM
     * @Version V1.00
     * @MethodName appendSpecialCharacterToCsv
     * @MethodParams [string]
     * @MethodDescription - Append special characters to csv file
     */
    public String appendSpecialCharacterToCsv(String string) {
        return "\"" + string + "\"";
    }

    /**
     * @Author shalika_w
     * @CreatedTime 2021-03-23 10:55:26 AM
     * @Version V1.00
     * @MethodName formatDateToStringCsvFile
     * @MethodParams [date]
     * @MethodDescription - Format date to string
     */
    public String formatDateToStringCsvFile(Date date) {
        String strDate = "--";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (date != null) {
                strDate = dateFormat.format(date);
            } else {
                strDate = "--";
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return strDate;
    }
}
