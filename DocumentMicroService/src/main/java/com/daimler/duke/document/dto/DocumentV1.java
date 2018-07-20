package com.daimler.duke.document.dto;

import java.io.Serializable;

/**
 * @author SANDGUP.
 *
 */

public class DocumentV1 implements Serializable {

    /**
     * 
     */
    private static final long  serialVersionUID = 6708327237563881731L;

    /**
     * 
     */
    private DocumentMetaDataV1 documentMetaData;
    /**
     * 
     */
    private DocumentContentV1  documentContent;

    /**
     * 
     */
    public DocumentV1() {
        // Default constructor
    }

    /**
     * @param inputDocMetaData.
     * @param inputDocContent
     */
    public DocumentV1(final DocumentMetaDataV1 inputDocMetaData, final DocumentContentV1 inputDocContent) {
        super();
        this.documentMetaData = inputDocMetaData;
        this.documentContent = inputDocContent;
    }

    /**
     * 
     * @return
     */
    public DocumentMetaDataV1 getDocumentMetaData() {
        return documentMetaData;
    }

    /**
     * 
     * @param input
     */
    public void setDocumentMetaData(final DocumentMetaDataV1 input) {
        this.documentMetaData = input;
    }

    /**
     * 
     * @return
     */
    public DocumentContentV1 getDocumentContent() {
        return documentContent;
    }

    /**
     * 
     * @param input
     */

    public void setDocumentContent(final DocumentContentV1 input) {
        this.documentContent = input;
    }

    @Override
    public String toString() {
        return "DocumentV1 [documentMetaData=" + documentMetaData + ", documentContent=" + documentContent + "]";
    }

}
