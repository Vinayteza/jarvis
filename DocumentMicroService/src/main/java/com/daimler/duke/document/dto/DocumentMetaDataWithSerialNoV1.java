package com.daimler.duke.document.dto;

import java.io.Serializable;

public class DocumentMetaDataWithSerialNoV1 implements Serializable {

    /**
     * 
     */
    private static final long       serialVersionUID = -7019005392534297260L;

    private Integer                 serialNo;

    private CreateMetaDataRequestV1 documentMetaData;

    /**
     * @return the documentMetaData
     */
    public CreateMetaDataRequestV1 getDocumentMetaData() {
        return documentMetaData;
    }

    /**
     * @param documentMetaData the documentMetaData to set
     */
    public void setDocumentMetaData(CreateMetaDataRequestV1 documentMetaData) {
        this.documentMetaData = documentMetaData;
    }

    /**
     * @return the serialNo
     */
    public Integer getSerialNo() {
        return serialNo;
    }

    /**
     * @param serialNo the serialNo to set
     */
    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public String toString() {
        return "DocumentMetaDataWithSerialNoV1 [serialNo=" + serialNo + ", documentMetaData=" + documentMetaData + "]";
    }

}
