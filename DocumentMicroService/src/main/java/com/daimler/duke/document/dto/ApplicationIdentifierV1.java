package com.daimler.duke.document.dto;

import java.io.Serializable;
import java.util.List;

public class ApplicationIdentifierV1 implements Serializable {

    /**
     * 
     */
    private static final long                     serialVersionUID = -3683111249874444448L;

    /**
     * id of the applicationId.
     */
    private String                                applicationId;

    /**
     * name of the application
     */

    private String                                applicationName;

    /**
     * firstName of the firstName.
     */
    private String                                firstName;

    /**
     * lastName of the creator.
     */
    private String                                lastName;

    /**
     * department of the creator.
     */
    private String                                department;

    /**
     * secretsKey of the application.
     */
    private String                                secretKey;

    /**
     * secretsCode of the application.
     */
    private String                                secretCode;

    /**
     * applicationAutherizationGrp of the application.
     */
    private List<ApplicationAutherizationGroupV1> applicationAutherizationGrp;

    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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
    public void setFirstName(String firstName) {
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
    public void setLastName(String lastName) {
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
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the applicationAutherizationGrp
     */
    public List<ApplicationAutherizationGroupV1> getApplicationAutherizationGrp() {
        return applicationAutherizationGrp;
    }

    /**
     * @param applicationAutherizationGrp the applicationAutherizationGrp to set
     */
    public void setApplicationAutherizationGrp(List<ApplicationAutherizationGroupV1> applicationAutherizationGrp) {
        this.applicationAutherizationGrp = applicationAutherizationGrp;
    }

    /**
     * @return the secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * @return the secretCode
     */
    public String getSecretCode() {
        return secretCode;
    }

    /**
     * @param secretCode the secretCode to set
     */
    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

}
