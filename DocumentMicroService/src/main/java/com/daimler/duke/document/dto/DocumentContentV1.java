package com.daimler.duke.document.dto;

import java.io.Serializable;

/**
 * @author SANDGUP.
 *
 */
public class DocumentContentV1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -803502876134040080L;

    /**
     * documentMtDtdId.
     */
    private String            documentMtDtdId;

    /**
     * data.
     */
    private String            data;

    /**
     * 
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * 
     * @param input
     */
    public void setData(final String input) {
        this.data = input;
    }

    /**
     * @return the documentMtDtdId
     */
    public String getDocumentMtDtdId() {
        return documentMtDtdId;
    }

    /**
     * @param documentMtDtdId the documentMtDtdId to set
     */
    public void setDocumentMtDtdId(String documentMtDtdId) {
        this.documentMtDtdId = documentMtDtdId;
    }

    @Override
    public String toString() {
        return "DocumentContentV1 [documentMtDtdId=" + documentMtDtdId + "]";
    }

}
