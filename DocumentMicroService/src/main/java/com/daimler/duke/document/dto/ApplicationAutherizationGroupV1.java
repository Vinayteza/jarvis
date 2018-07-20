package com.daimler.duke.document.dto;

import java.io.Serializable;

public class ApplicationAutherizationGroupV1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8485723418431567642L;

    /**
     * name of the autherizationGroup.
     */
    private String            autherizationGroup;

    /**
     * @return the autherizationGroup
     */
    public String getAutherizationGroup() {
        return autherizationGroup;
    }

    /**
     * @param autherizationGroup the autherizationGroup to set
     */
    public void setAutherizationGroup(String autherizationGroup) {
        this.autherizationGroup = autherizationGroup;
    }

    @Override
    public String toString() {
        return "ApplicationAutherizationGroupV1 [autherizationGroup=" + autherizationGroup + "]";
    }

}
