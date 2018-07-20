package com.daimler.duke.document.db.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.daimler.duke.document.constant.MongoDbCollections;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = MongoDbCollections.TOKEN_STATUS)
public class DbTokenStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1767274431550135393L;

    /**
     * id of the applicationId.
     */
    @Id
    @ApiModelProperty(notes = "The database generated product ID")
    private String            tokenStatusId;

    /**
     * tokenValue of the token
     */
    private String            tokenValue;

    /**
     * documentId of the token
     */
    private String            documentId;

    /**
     * expireTime of the token
     */

    private Date              createTime;

    /**
     * active status for token.
     */
    private String            active;

    /**
     * @return the tokenStatusId
     */
    public String getTokenStatusId() {
        return tokenStatusId;
    }

    /**
     * @param tokenStatusId the tokenStatusId to set
     */
    public void setTokenStatusId(String tokenStatusId) {
        this.tokenStatusId = tokenStatusId;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * @param tokenValue the tokenValue to set
     */
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    /**
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
