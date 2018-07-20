package com.daimler.duke.document.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class GetContentRestResponseV1 extends BaseRestResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2429476614891091224L;

    /**
     * The responseobject itself.
     */
    @ApiModelProperty(notes = "response data")
    private Object            documentContent;

    /**
     * default constructor.
     */
    public GetContentRestResponseV1() {
        // default constructor
    }

    /**
     * constructor.
     * 
     * @param payLoadInput Object
     */
    public GetContentRestResponseV1(final Object payLoadInput) {
        this.documentContent = payLoadInput;
    }

    /**
     * @return the documentContent
     */
    public Object getDocumentContent() {
        return documentContent;
    }

}
