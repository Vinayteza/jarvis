package com.daimler.duke.document.db.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.daimler.duke.document.constant.MongoDbCollections;

/**
 * @author NAYASAR
 *
 */
@Document(collection = MongoDbCollections.DCMNT_MTDT_COLLECTION)
// @JsonIgnoreProperties("authorId") if u want to ignore some property
public class DbDocumentMetaData extends BaseMongoDocument implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8404534913798998391L;

    /**
     * name of the document.
     */
    private String            applicationId;

    /**
     * name of the document.
     */
    private String            documentName;

    /**
     * type of the document.
     */
    private String            documentype;

    /**
     * keywords of the document.
     */
    private String            comment;

    /**
     * lastName of the firstName.
     */
    private String            firstName;

    /**
     * lastName of the creator.
     */
    private String            lastName;

    /**
     * department of the creator.
     */
    private String            department;

    /**
     * shortId of the user
     */
    private String            shortId;



    /**
     * size of the document.
     */
    private Long              size;

    /**
     * creation time of the document.
     */
    private Date              creationTime;

    /**
     * creation time of the document.
     */
    private Date              updatedTime;

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName the documentName to set
     */
    public void setDocumentName(final String documentName) {
        this.documentName = documentName;
    }

    /**
     * @return the documentype
     */
    public String getDocumentype() {
        return documentype;
    }

    /**
     * @param documentype the documentype to set
     */
    public void setDocumentype(final String documentype) {
        this.documentype = documentype;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(final String department) {
        this.department = department;
    }

    /**
     * @return the shortId
     */
    public String getShortId() {
        return shortId;
    }

    /**
     * @param shortId the shortId to set
     */
    public void setShortId(final String shortId) {
        this.shortId = shortId;
    }

    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(final Long size) {
        this.size = size;
    }

    /**
     * @return the creationTime
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return the updatedTime
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime the updatedTime to set
     */
    public void setUpdatedTime(final Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }

}
