package com.daimler.duke.document.dto;

import java.io.Serializable;

public class DocumentMetaDataTokenMapV1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8100356668494520734L;
    private String            documentId;
    private Integer           index;
    private String            token;

    /**
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "DocumentMetaDataTokenMapV1 [documentId=" + documentId + ", index=" + index + ", token=" + token + "]";
    }

}
