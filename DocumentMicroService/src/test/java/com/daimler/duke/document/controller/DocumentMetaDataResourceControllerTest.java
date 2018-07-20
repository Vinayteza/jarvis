package com.daimler.duke.document.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.CreateMetaDataRequestV1;
import com.daimler.duke.document.dto.CreateMetadataIdIndexRestResponseV1;
import com.daimler.duke.document.dto.CreateMetadataIdTokenRestResponseV1;
import com.daimler.duke.document.dto.CreateMetatadaRestResponseV1;
import com.daimler.duke.document.dto.DocumentMetaDataTokenMapV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentMetaDataWithSerialNoV1;
import com.daimler.duke.document.dto.GetMetadataRestResponseV1;
import com.daimler.duke.document.service.DocumentUploadChunkService;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.service.TokenStatusService;

/**
 * unit test for DocumentMetaDataResourceController class.
 * 
 * @author nayasar
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentMetaDataResourceController.class, DocumentResourceLogController.class
})
public class DocumentMetaDataResourceControllerTest {

    private DocumentMetaDataResourceController documentMetaDataResourceController;
    private IDocumentService                   documentService;
    private IDocumentAppIdentifierService      documentAppIdentifierService;
    private TokenStatusService                 tokenStatusService;
    private DocumentUploadChunkService         documentUploadChunkService;

    @Before
    public void init() throws Exception {

        PowerMockito.mockStatic(DocumentResourceLogController.class);
        PowerMockito.doNothing()
                    .when(DocumentResourceLogController.class, "logJsonRequest", Mockito.any(DocumentMetaDataV1.class));

        documentService = PowerMockito.mock(IDocumentService.class);
        documentAppIdentifierService = PowerMockito.mock(IDocumentAppIdentifierService.class);
        tokenStatusService = PowerMockito.mock(TokenStatusService.class);
        documentUploadChunkService = PowerMockito.mock(DocumentUploadChunkService.class);

        documentMetaDataResourceController = new DocumentMetaDataResourceController();
        Whitebox.setInternalState(documentMetaDataResourceController, "documentService", documentService);
        Whitebox.setInternalState(documentMetaDataResourceController,
                                  "documentAppIdentifierService",
                                  documentAppIdentifierService);
        Whitebox.setInternalState(documentMetaDataResourceController, "tokenStatusService", tokenStatusService);

        Whitebox.setInternalState(documentMetaDataResourceController,
                                  "documentUploadChunkService",
                                  documentUploadChunkService);

    }

    @Test
    public void testCreateDocumentMetaData() {

        String docId = "5a223f3e5953c7136439beb5";
        CreateMetaDataRequestV1 documentMetaData = createMetaDataRequest("", "TerstName", "pdf", 1l);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        PowerMockito.when(documentService.createDocumentMetaData(Mockito.any(String.class),
                                                                 Mockito.any(DocumentMetaDataV1.class)))
                    .thenReturn(docId);

        // method under test
        CreateMetatadaRestResponseV1 response =
                documentMetaDataResourceController.createDocumentMetaData("token123", "DukE", documentMetaData);

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + docId, response.getMetadataId(), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testCreateDocumentMetaDataErrorPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.createDocumentMetaData(Mockito.any(String.class),
                                                                     Mockito.any(DocumentMetaDataV1.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentMetaDataResourceController.createDocumentMetaData("token123",
                                                                      "DukE",
                                                                      new CreateMetaDataRequestV1());
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    @Test
    public void testGetDocumentMetaData() {

        String documentId = "5a223f3e5953c7136439beb5";
        DocumentMetaDataV1 testDocumentMetaData = createDocumentMataData(documentId, "TerstName", "pdf", 1l);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");
        PowerMockito.when(documentService.getDocumentMetaData(Mockito.any(String.class), Mockito.any(String.class)))
                    .thenReturn(testDocumentMetaData);

        // method under test
        GetMetadataRestResponseV1 response =
                documentMetaDataResourceController.getDocumentMetaData("token123", "DukE", documentId);
        DocumentMetaDataV1 result = (DocumentMetaDataV1) response.getDocumentMetadata();

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + documentId, result.getDocumentId(), equalTo(documentId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testGetDocumentMetaDataErrorPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.getDocumentMetaData(Mockito.any(String.class), Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Object not found"));
            // method under test
            documentMetaDataResourceController.getDocumentMetaData("token123", "DukE", "test id");
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("Object not found"));
        }
    }

    //

    @Test
    public void testUpdateDocumentMetaData() {

        String docId = "5a223f3e5953c7136439beb5";
        DocumentMetaDataV1 documentMetaData = createDocumentMataData(docId, "TerstName", "pdf", 1l);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        PowerMockito.when(documentService.updateDocumentMetaData(Mockito.any(String.class),
                                                                 Mockito.any(DocumentMetaDataV1.class)))
                    .thenReturn(docId);

        // method under test
        CreateMetatadaRestResponseV1 response =
                documentMetaDataResourceController.updateDocumentMetaData("token123", "DukE", documentMetaData);

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        assertThat("Response payload should be " + docId, response.getMetadataId(), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testUpdateDocumentMetaDataErrorPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.updateDocumentMetaData(Mockito.any(String.class),
                                                                     Mockito.any(DocumentMetaDataV1.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentMetaDataResourceController.updateDocumentMetaData("token123", "DukE", new DocumentMetaDataV1());
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    //

    @Test
    public void testGetListDocumentMetaData() {

        String docId = "5a223f3e5953c7136439beb5";
        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        List<DocumentMetaDataV1> documentMetaDataList = new ArrayList<>();
        documentMetaDataList.add(createDocumentMataData(docId, "TerstName", "pdf", 1l));

        PowerMockito.when(documentService.getListDocumentMetaData(Mockito.any(String.class), Mockito.any(List.class)))
                    .thenReturn(documentMetaDataList);

        // method under test
        GetMetadataRestResponseV1 response =
                documentMetaDataResourceController.getListDocumentMetaData("token123", "DukE", new ArrayList<String>());

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));
        List<DocumentMetaDataV1> result = (List<DocumentMetaDataV1>) response.getDocumentMetadata();
        assertThat("Response payload should be " + docId, result.get(0).getDocumentId(), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testGetListDocumentMetaDataErrorPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.getListDocumentMetaData(Mockito.any(String.class),
                                                                      Mockito.any(List.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentMetaDataResourceController.getListDocumentMetaData("token123", "DukE", new ArrayList<String>());
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    ///

    @Test
    public void testCreateMultipleDocumentMetaData() {

        String docId = "5a223f3e5953c7136439beb5";
        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        final Map<Integer,String> docIds = new HashMap<>();
        docIds.put(1, docId);
        PowerMockito.when(documentService.createMultipleDocumentMetaData(Mockito.any(String.class),
                                                                         Mockito.any(List.class)))
                    .thenReturn(docIds);

        // method under test
        CreateMetadataIdIndexRestResponseV1 response =
                documentMetaDataResourceController.createMultipleDocumentMetaData("token123",
                                                                                  "DukE",
                                                                                  new ArrayList<DocumentMetaDataWithSerialNoV1>());

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));

        Map<Integer,String> idsMap = (Map<Integer,String>) response.getMetadataIndexMap();
        assertThat("Response payload should be " + docId, idsMap.get(1), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testCreateMultipleDocumentMetaDataErrorPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.createMultipleDocumentMetaData(Mockito.any(String.class),
                                                                             Mockito.any(List.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentMetaDataResourceController.createMultipleDocumentMetaData("token123",
                                                                              "DukE",
                                                                              new ArrayList<DocumentMetaDataWithSerialNoV1>());
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    ///

    @Test
    public void testSaveMultipleDocumentMetaDataWithToken() {

        String docId = "5a223f3e5953c7136439beb5";
        DocumentMetaDataV1 documentMetaData = createDocumentMataData("", "TerstName", "pdf", 1l);

        PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                    .thenReturn("Duke");

        final Map<Integer,String> docIds = new HashMap<>();
        docIds.put(1, docId);
        PowerMockito.when(documentService.createMultipleDocumentMetaData(Mockito.any(String.class),
                                                                         Mockito.any(List.class)))
                    .thenReturn(docIds);

        List<DocumentMetaDataTokenMapV1> documentTokenInfoList = new ArrayList<>();
        DocumentMetaDataTokenMapV1 e = new DocumentMetaDataTokenMapV1();
        e.setDocumentId(docId);
        documentTokenInfoList.add(e);
        PowerMockito.when(tokenStatusService.createToken(Mockito.any(Map.class))).thenReturn(documentTokenInfoList);

        // method under test
        CreateMetadataIdTokenRestResponseV1 response =
                documentMetaDataResourceController.createMultipleDocumentMetaDataWithToken("token123",
                                                                                           "DukE",
                                                                                           new ArrayList<DocumentMetaDataWithSerialNoV1>());

        // Assertions here.
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatusCode());
        assertThat("Response Code should be 200", response.getStatusCode(), equalTo(200));

        List<DocumentMetaDataTokenMapV1> idList = (List<DocumentMetaDataTokenMapV1>) response.getMetadataTokenMap();
        assertThat("Response payload should be " + docId, idList.get(0).getDocumentId(), equalTo(docId));
        assertThat("Response payload should be successfull", response.getStatusMessage(), equalTo("successfull"));
    }

    @Test
    public void testSaveMultipleDocumentMetaDataWithTokenErrorPath() {
        try {

            PowerMockito.when(documentAppIdentifierService.decryptApplicationName(Mockito.any(String.class)))
                        .thenReturn("Duke");
            PowerMockito.when(documentService.createMultipleDocumentMetaData(Mockito.any(String.class),
                                                                             Mockito.any(List.class)))
                        .thenThrow(new RuntimeException("something went wrong"));
            // method under test
            documentMetaDataResourceController.createMultipleDocumentMetaDataWithToken("token123",
                                                                                       "DukE",
                                                                                       new ArrayList<DocumentMetaDataWithSerialNoV1>());
            Assert.fail();
        }
        catch (Exception ex) {
            assertThat("", ex.getMessage(), equalTo("something went wrong"));
        }
    }

    @Test
    public void testSetUploadChunkSize() {
        PowerMockito.when(documentUploadChunkService.saveUploadChunkSize(Mockito.any(String.class)))
                    .thenReturn(createDocumentMataData("1024"));
        documentMetaDataResourceController.uploadChunkSize("token123", "dule", "1024");
        // Assert.assertEquals(dbUploadFileChunkSize.getUploadCunkSize(),chunkSize);
    }

    @Test
    public void testSetUploadChunkSizeError() {
        try {
            PowerMockito.when(documentUploadChunkService.saveUploadChunkSize(null))
                        .thenThrow(new RuntimeException("Chunk size is null or non-numberic"));
            documentMetaDataResourceController.uploadChunkSize("7656rfyh", "duke", null);
            Assert.fail();
        }
        catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Chunk size is null or non-numberic");
        }
    }

    private DbUploadFileChunkSize createDocumentMataData(String chunkSize) {
        DbUploadFileChunkSize document = new DbUploadFileChunkSize();
        document.setUploadCunkSize(chunkSize);
        return document;
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

    private CreateMetaDataRequestV1 createMetaDataRequest(String documentId, String name, String type, Long size) {
        CreateMetaDataRequestV1 documentMetaData = new CreateMetaDataRequestV1();
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
