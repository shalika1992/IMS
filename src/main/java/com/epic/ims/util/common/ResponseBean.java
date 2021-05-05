package com.epic.ims.util.common;

public class ResponseBean {
    private boolean flag;
    private String successMessage;
    private String errorMessage;

    public ResponseBean() {
    }

    public ResponseBean(boolean flag, String successMessage, String errorMessage) {
        this.flag = flag;
        this.successMessage = successMessage;
        this.errorMessage = errorMessage;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
