package com.daimler.duke.document.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Using for transferring Chunk Upload status
 * 
 * @author kshebin
 *
 */
public class DocumentChunkTransferredStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4279051621352866953L;

    /**
     * Document MetaDataId
     */
    private String            documentMetaDataId;

    /**
     * Chunk ids
     */
    private List<Long>        chunkIds;

    public DocumentChunkTransferredStatus() {
        chunkIds = new ArrayList<>();
    }

    /**
     * @return the documentMetaDataId
     */
    public String getDocumentMetaDataId() {
        return documentMetaDataId;
    }

    /**
     * @param documentMetaDataId the documentMetaDataId to set
     */
    public void setDocumentMetaDataId(String documentMetaDataId) {
        this.documentMetaDataId = documentMetaDataId;
    }

    /**
     * @return the chunkIds
     */
    public List<Long> getChunkIds() {
        return chunkIds;
    }

    public void addChunkId(Long chunkId) {
        if (chunkIds == null) {
            chunkIds = new ArrayList<>();
        }
        chunkIds.add(chunkId);
    }

    @Override
    public String toString() {
        return "DocumentChunkTransferredStatus [documentMetaDataId=" + documentMetaDataId
                + ", chunkIds="
                + chunkIds
                + "]";
    }

}
