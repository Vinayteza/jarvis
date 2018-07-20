package com.daimler.duke.document.dto;

import java.io.Serializable;

import javax.inject.Named;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Named
@ApiModel("RestResponseValue")
public class RestResponseValue extends BaseRestResponse implements Serializable {

    private static final long serialVersionUID = 1962124432322316084L;

    @ApiModelProperty(notes = "response data")
    private Object            value;

    /**
     * default constructor.
     */
    public RestResponseValue() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public RestResponseValue(final Object valueInput) {
        this.value = valueInput;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

}
