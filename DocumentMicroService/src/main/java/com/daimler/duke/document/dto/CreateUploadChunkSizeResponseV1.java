package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CreateUploadChunkSizeResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6475164104209604894L;

    @ApiModelProperty(notes = "response data")
    private String            uploadChunkSize;

    /**
     * default constructor.
     */
    public CreateUploadChunkSizeResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput String
     */
    public CreateUploadChunkSizeResponseV1(final String valueInput) {
        this.uploadChunkSize = valueInput;
    }

    /**
     * @return the uploadChunkSize
     */
    public String getUploadChunkSize() {
        return uploadChunkSize;
    }

    @Override
    public String toString() {
        return "CreateUploadChunkSizeResponseV1 [uploadChunkSize=" + uploadChunkSize + "]";
    }

}
