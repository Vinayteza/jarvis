package com.daimler.duke.document.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.CreateUploadChunkSizeResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.service.DocumentUploadChunkService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  ApplicationRunStatusController.class, DocumentResourceLogController.class
})
public class ApplicationRunStatusControllerTest {

    private static String                  chunkSize = "1024";
    private DbDocumentMetaDataRepository   dbDocumentMetaDataRepository;
    private ApplicationRunStatusController applicationRunStatusController;
    private DocumentUploadChunkService     documentService;

    @Before
    public void init() throws Exception {

        dbDocumentMetaDataRepository = PowerMockito.mock(DbDocumentMetaDataRepository.class);

        documentService = PowerMockito.mock(DocumentUploadChunkService.class);

        applicationRunStatusController = new ApplicationRunStatusController();
        Whitebox.setInternalState(applicationRunStatusController,
                                  "dbDocumentMetaDataRepository",
                                  dbDocumentMetaDataRepository);
        Whitebox.setInternalState(applicationRunStatusController, "documentService", documentService);
    }

    /*
     * @Test public void testCheckApplicationRunningStatus() { // method under test RestResponseObject response =
     * applicationRunStatusController.checkApplicationRunningStatus(); // Assertions here.
     * Assert.assertNotNull(response); Assert.assertNotNull(response.getStatusCode());
     * assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
     * assertThat("Response StatusMessage should be successfull", response.getStatusMessage(), equalTo("successfull"));
     * assertThat("Response result should be :", response.getResult(), equalTo("Document service is running fine")); }
     */

    @Test
    public void testCheckDatabaseRunningStatus() {

        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(new DbDocumentMetaData());

        // method under test
        RestResponseObject response = applicationRunStatusController.checkDatabaseRunningStatus();

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
        assertThat("Response result should be :", response.getResult(), equalTo("Document service is running fine"));
    }

    @Test
    public void testCheckDatabaseRunningStatusIfException() {

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("something went wrong"));

            // method under test
            applicationRunStatusController.checkDatabaseRunningStatus();
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    @Test
    public void testGetUploadChunkSize() {
        PowerMockito.when(documentService.getUploadChunkSize()).thenReturn(createDocumentMataData(chunkSize));
        CreateUploadChunkSizeResponseV1 response = applicationRunStatusController.getUploadChunkSize();
        // Assert.assertEquals(dbUploadFileChunkSize.getUploadCunkSize(),chunkSize);
    }

    private DbUploadFileChunkSize createDocumentMataData(String chunkSize) {
        DbUploadFileChunkSize document = new DbUploadFileChunkSize();
        document.setUploadCunkSize(chunkSize);
        return document;
    }
}
