package com.daimler.duke.document.dto;

import java.io.Serializable;

import javax.inject.Named;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author RMAHAKU
 *
 */
@Named
@ApiModel("RestResponseObject")
public class RestResponseObject extends BaseRestResponse implements Serializable {

    /**
     * The generated serial version UID.
     */
    private static final long serialVersionUID = -4758226303115564334L;

    /**
     * The responseobject itself.
     */
    @ApiModelProperty(notes = "response data")
    private Object            result;

    /**
     * default constructor.
     */
    public RestResponseObject() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public RestResponseObject(final Object payLoadInput) {
        this.result = payLoadInput;
    }

    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "RestResponseObject [result=" + result + "]";
    }

}
