package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CreateMetadataIdTokenRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1823655270709081478L;

    @ApiModelProperty(notes = "response data")
    private Object            metadataTokenMap;

    /**
     * default constructor.
     */
    public CreateMetadataIdTokenRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public CreateMetadataIdTokenRestResponseV1(final Object valueInput) {
        this.metadataTokenMap = valueInput;
    }

    /**
     * @return the metadataTokenMap
     */
    public Object getMetadataTokenMap() {
        return metadataTokenMap;
    }

    @Override
    public String toString() {
        return "CreateMetadataIdTokenRestResponseV1 [metadataTokenMap=" + metadataTokenMap + "]";
    }

}
