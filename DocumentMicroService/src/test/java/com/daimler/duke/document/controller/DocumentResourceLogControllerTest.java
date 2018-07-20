package com.daimler.duke.document.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.RestResponseObject;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentResourceLogController.class
})
public class DocumentResourceLogControllerTest {

    private DocumentResourceLogController documentResourceLogController;

    @Before
    public void init() throws Exception {
        documentResourceLogController = new DocumentResourceLogController();
    }

    @Test
    public void testSetInputRequestLoggerSwitchOn() {

        // method under test
        RestResponseObject response = documentResourceLogController.setInputRequestLoggerSwitch("1");

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response StatusMessage should be successfull", response.getStatusMessage(), equalTo("successfull"));
        assertThat("Response result should be :", response.getResult(), equalTo("ON"));
    }

    @Test
    public void testSetInputRequestLoggerSwitchOff() {

        // method under test
        RestResponseObject response = documentResourceLogController.setInputRequestLoggerSwitch("0");

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response StatusMessage should be successfull", response.getStatusMessage(), equalTo("successfull"));
        assertThat("Response result should be :", response.getResult(), equalTo("OFF"));
    }

    @Test
    public void testLogJsonRequestIfException() {
        try {
            documentResourceLogController.setInputRequestLoggerSwitch("1");
            Object request = new Object();
            // method under test
            documentResourceLogController.logJsonRequest(request);
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Error in logging input request"));
        }
    }

    @Test
    public void testLogJsonRequestIflogRequestIsFalse() {
        Object request = new Object();
        // method under test
        documentResourceLogController.logJsonRequest(request);
    }

    @Test
    public void testLogJsonRequest() {
        documentResourceLogController.setInputRequestLoggerSwitch("1");
        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);
        // method under test
        documentResourceLogController.logJsonRequest(documentMetaData);
    }

    private DocumentMetaDataV1 createDocumentMataData(String documentId, String name, String type, Long size) {
        DocumentMetaDataV1 documentMetaData = new DocumentMetaDataV1();
        documentMetaData.setDocumentId(documentId);
        documentMetaData.setDocumentName(name);
        documentMetaData.setDocumentType(type);
        documentMetaData.setComment("test");
        documentMetaData.setFirstName("Andreas");
        documentMetaData.setLastName("Gollmann");
        documentMetaData.setDepartment("ITP/DT");
        documentMetaData.setShortId("AGOLLMAN");
        return documentMetaData;
    }

}
