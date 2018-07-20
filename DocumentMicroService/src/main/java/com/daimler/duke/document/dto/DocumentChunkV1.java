package com.daimler.duke.document.dto;

import java.io.Serializable;

/**
 * @author kshebin
 *
 */
public class DocumentChunkV1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8182145683185020225L;
    /**
     * chunkId.
     */

    private String            chunkId;
    /**
     * documentMtDtdId.
     */
    private String            documentMetaDataId;
    /**
     * data.
     */
    private String            content;
    /**
     * totalNumOfChunks
     */
    private String            totalNumOfChunks;

    /**
     * to get total number chunks
     * 
     * @return total number of chunks
     */
    public String getTotalNumOfChunks() {
        return totalNumOfChunks;
    }

    public void setTotalNumOfChunks(String totalNumOfChunks) {
        this.totalNumOfChunks = totalNumOfChunks;
    }

    /**
     * to get chunk id
     * 
     * @return chunk id
     */
    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    /**
     * Get the documentMetaDataId.
     * 
     * @return documentMetaDataId
     */
    public String getDocumentMetaDataId() {
        return documentMetaDataId;
    }

    /**
     * Set the documentMetaDataId.
     * 
     * @param input the documentMetaDataId to set
     */
    public void setDocumentMetaDataId(String documentMetaDataId) {
        this.documentMetaDataId = documentMetaDataId;
    }

    /**
     * Get the data.
     * 
     * @return data
     */
    public String getContent() {
        return content;

    }

    /**
     * Set the data.
     * 
     * @param input the data to set
     */

    public void setContent(final String input) {
        this.content = input;

    }

    public String getUniqueChunkFileName() {
        return this.documentMetaDataId + ":" + Long.parseLong(this.chunkId);

    }

    @Override
    public String toString() {
        return "DocumentChunkV1 [chunkId=" + chunkId
                + ", documentMetaDataId="
                + documentMetaDataId
                + ", totalNumOfChunks="
                + totalNumOfChunks
                + "]";
    }

}
