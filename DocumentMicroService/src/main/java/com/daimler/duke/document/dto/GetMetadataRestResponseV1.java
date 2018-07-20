package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class GetMetadataRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5479557354091611796L;

    /**
     * The responseobject itself.
     */
    @ApiModelProperty(notes = "response data")
    private Object            documentMetadata;

    /**
     * default constructor.
     */
    public GetMetadataRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public GetMetadataRestResponseV1(final Object payLoadInput) {
        this.documentMetadata = payLoadInput;
    }

    /**
     * @return the documentMetadata
     */
    public Object getDocumentMetadata() {
        return documentMetadata;
    }

    @Override
    public String toString() {
        return "GetMetadataRestResponseV1 [documentMetadata=" + documentMetadata + "]";
    }

}
