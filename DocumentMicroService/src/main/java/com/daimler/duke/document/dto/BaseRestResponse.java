package com.daimler.duke.document.dto;

import org.springframework.http.HttpStatus;

public abstract class BaseRestResponse {

    /**
     * Error code if exists.
     */

    private int    statusCode       = HttpStatus.OK.value();

    /**
     * Statusmessage of the call.
     */
    private String statusMessage    = "successfull";

    /**
     * statusMessageDetail of the call.
     */
    private String statusMessageDtl = "";

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * @return the statusMessageDtl
     */
    public String getStatusMessageDtl() {
        return statusMessageDtl;
    }

    /**
     * @param statusMessageDtl the statusMessageDtl to set
     */
    public void setStatusMessageDtl(String statusMessageDtl) {
        this.statusMessageDtl = statusMessageDtl;
    }

}
