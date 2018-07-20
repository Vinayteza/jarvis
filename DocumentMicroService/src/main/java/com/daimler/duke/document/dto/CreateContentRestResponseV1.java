package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CreateContentRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7253444461379089822L;

    @ApiModelProperty(notes = "response data")
    private String            documentContentId;

    /**
     * default constructor.
     */
    public CreateContentRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public CreateContentRestResponseV1(final String valueInput) {
        this.documentContentId = valueInput;
    }

    /**
     * @return the documentContentId
     */
    public String getDocumentContentId() {
        return documentContentId;
    }

    @Override
    public String toString() {
        return "CreateContentRestResponseV1 [documentContentId=" + documentContentId + "]";
    }

}
