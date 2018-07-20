package com.daimler.duke.document.dto;

import java.io.Serializable;

public class CreateDocumentRequestV1 implements Serializable {

    /**
     * 
     */
    private static final long       serialVersionUID = 283666045795997402L;
    

    /**
     * 
     */
    private CreateMetaDataRequestV1 documentMetaData;
    /**
     * 
     */
    private CreateContentRequestV1  documentContent;

    /**
     * 
     */
    public CreateDocumentRequestV1() {
        // Default constructor
    }

    /**
     * @param inputDocMetaData.
     * @param inputDocContent
     */
    public CreateDocumentRequestV1(CreateMetaDataRequestV1 documentMetaData,
                                   CreateContentRequestV1 documentContent) {
        super();
        this.documentMetaData = documentMetaData;
        this.documentContent = documentContent;
    }
   

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
     * @return the documentContent
     */
    public CreateContentRequestV1 getDocumentContent() {
        return documentContent;
    }

    /**
     * @param documentContent the documentContent to set
     */
    public void setDocumentContent(CreateContentRequestV1 documentContent) {
        this.documentContent = documentContent;
    }

    @Override
    public String toString() {
        return "CreateDocumentRequestV1 [documentMetaData=" + documentMetaData
                + ", documentContent="
                + documentContent
                + "]";
    }
    
    
}
