package com.daimler.duke.document.dto;

public class User {

    private String            shortId;

    private String            firstName;

    private String            lastName;

    private String            departmentName;

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "User [shortId=" + shortId
                + ", firstName="
                + firstName
                + ", lastName="
                + lastName
                + ", departmentName="
                + departmentName
                + "]";
    }
    
    
}
