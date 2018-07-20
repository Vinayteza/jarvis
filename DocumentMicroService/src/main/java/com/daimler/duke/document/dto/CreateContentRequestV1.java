package com.daimler.duke.document.dto;

import java.io.Serializable;

public class CreateContentRequestV1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2435320433357391068L;

    /**
     * data.
     */
    private String            data;

    /**
     * 
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * 
     * @param input
     */
    public void setData(final String input) {
        this.data = input;
    }

}
