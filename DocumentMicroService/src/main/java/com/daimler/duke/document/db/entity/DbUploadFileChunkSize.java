package com.daimler.duke.document.db.entity;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.daimler.duke.document.constant.Constants;

@Document(collection = "UploadFileChunkSize")
public class DbUploadFileChunkSize implements Serializable {
    private static final long serialVersionUID = -723583058586873479L;
    private String            uploadCunkSize;
    private String            fetchNumber=Constants.FETCH_VALUE;

    public String getFetchNumber() {
        return fetchNumber;
    }

    public void setFetchNumber(String fetchNumber) {
        this.fetchNumber = fetchNumber;
    }

    public String getUploadCunkSize() {
        return uploadCunkSize;
    }

    public void setUploadCunkSize(String uploadCunkSize) {
        this.uploadCunkSize = uploadCunkSize;
    }

}
