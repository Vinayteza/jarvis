
package com.daimler.duke.document.dto;

import java.io.Serializable;

/**
 * Request Object for DocumentMetaData.
 * 
 * @author NAYASAR
 *
 */
public class DocumentMetaDataV1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1700813567961638688L;

    /**
     * documentId of the document
     */

    private String            documentId;

    /**
     * name of the document.
     */
    private String            documentName;

    /**
     * type of the document.
     */
    private String            documentType;

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
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }

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
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return "DocumentMetaDataV1 [documentId=" + documentId
                + ", documentName="
                + documentName
                + ", documentType="
                + documentType
                + ", comment="
                + comment
                + ", firstName="
                + firstName
                + ", lastName="
                + lastName
                + ", department="
                + department
                + ", shortId="
                + shortId
                + ", size="
                + size
                + "]";
    }

}
