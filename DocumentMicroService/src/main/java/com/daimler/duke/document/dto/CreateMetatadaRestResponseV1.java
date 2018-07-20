package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CreateMetatadaRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1823655270709081478L;

    @ApiModelProperty(notes = "response data")
    private String            metadataId;

    /**
     * default constructor.
     */
    public CreateMetatadaRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public CreateMetatadaRestResponseV1(final String valueInput) {
        this.metadataId = valueInput;
    }

    /**
     * @return the metadataId
     */
    public String getMetadataId() {
        return metadataId;
    }

    @Override
    public String toString() {
        return "CreateMetatadaRestResponseV1 [metadataId=" + metadataId + "]";
    }

}
