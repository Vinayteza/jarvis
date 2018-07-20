package com.daimler.duke.document.db.entity;

import java.io.Serializable;

/**
 * 
 * @author NAYASAR
 *
 */
public class DbApplicationAutherizationGroup implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8903379353316788749L;

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

}
