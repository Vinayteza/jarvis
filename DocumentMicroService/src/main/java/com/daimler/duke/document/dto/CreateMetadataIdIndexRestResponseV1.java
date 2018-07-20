package com.daimler.duke.document.dto;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class CreateMetadataIdIndexRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long   serialVersionUID = -4259638746526704952L;

    @ApiModelProperty(notes = "response data")
    private Map<Integer,String> metadataIndexMap;

    /**
     * default constructor.
     */
    public CreateMetadataIdIndexRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public CreateMetadataIdIndexRestResponseV1(final Map<Integer,String> valueInput) {
        this.metadataIndexMap = valueInput;
    }

    /**
     * @return the metadataIndexMap
     */
    public Map<Integer,String> getMetadataIndexMap() {
        return metadataIndexMap;
    }

    @Override
    public String toString() {
        return "CreateMetadataIdIndexRestResponseV1 [metadataIndexMap=" + metadataIndexMap + "]";
    }

}
