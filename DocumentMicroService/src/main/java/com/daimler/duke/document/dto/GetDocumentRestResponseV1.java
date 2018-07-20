package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class GetDocumentRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7216592920558756380L;

    /**
     * The responseobject itself.
     */
    @ApiModelProperty(notes = "response data")
    private Object            document;

    /**
     * default constructor.
     */
    public GetDocumentRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public GetDocumentRestResponseV1(final Object payLoadInput) {
        this.document = payLoadInput;
    }

    /**
     * @return the document
     */
    public Object getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return "GetDocumentRestResponseV1 [document=" + document + "]";
    }

}
